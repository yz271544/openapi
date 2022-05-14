package com.teradata.openapi.framework.launcher;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import static com.teradata.openapi.framework.launcher.CommandBuilderUtils.*;

/**
 * Abstract Openapi command builder that defines common functionality.
 */
abstract class AbstractCommandBuilder {

    boolean verbose;
    String appName;
    String appResource;
    String deployMode;
    String javaHome;
    String mainClass;
    String master;
    protected String propertiesFile;
    final List<String> appArgs;
    final List<String> jars;
    final List<String> files;
    final List<String> pyFiles;
    final Map<String, String> childEnv;
    final Map<String, String> conf;

    // The merged configuration for the application. Cached to avoid having to read / parse
    // properties files multiple times.
    private Map<String, String> effectiveConfig;

    public AbstractCommandBuilder() {
        this.appArgs = new ArrayList<String>();
        this.childEnv = new HashMap<String, String>();
        this.conf = new HashMap<String, String>();
        this.files = new ArrayList<String>();
        this.jars = new ArrayList<String>();
        this.pyFiles = new ArrayList<String>();
    }

    /**
     * Builds the command to execute.
     *
     * @param env A map containing environment variables for the child process. It may already contain
     *            entries defined by the user (such as OPENAPI_HOME, or those defined by the
     *            OpenapiLauncher constructor that takes an environment), and may be modified to
     *            include other variables needed by the process to be executed.
     */
    abstract List<String> buildCommand(Map<String, String> env) throws IOException;

    /**
     * Builds a list of arguments to run java.
     * <p>
     * This method finds the java executable to use and appends JVM-specific options for running a
     * class with Openapi in the classpath. It also loads options from the "java-opts" file in the
     * configuration directory being used.
     * <p>
     * Callers should still add at least the class to run, as well as any arguments to pass to the
     * class.
     */
    List<String> buildJavaCommand(String extraClassPath,String conf) throws IOException {
        List<String> cmd = new ArrayList<String>();
        String envJavaHome;

        if (javaHome != null) {
            cmd.add(join(File.separator, javaHome, "bin", "java"));
        } else if ((envJavaHome = System.getenv("JAVA_HOME")) != null) {
            cmd.add(join(File.separator, envJavaHome, "bin", "java"));
        } else {
            cmd.add(join(File.separator, System.getProperty("java.home"), "bin", "java"));
        }

        // Load extra JAVA_OPTS from conf/java-opts, if it exists.
        File javaOpts = new File(join(File.separator, getConfDir(), "java-opts"));
        if (javaOpts.isFile()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(javaOpts), "UTF-8"));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    addOptionString(cmd, line);
                }
            } finally {
                br.close();
            }
        }

        cmd.add("-cp");
        cmd.add(join(File.pathSeparator, buildClassPath(extraClassPath,conf)));
        return cmd;
    }

    void addOptionString(List<String> cmd, String options) {
        if (!isEmpty(options)) {
            for (String opt : parseOptionString(options)) {
                cmd.add(opt);
            }
        }
    }

    /**
     * Builds the classpath for the application. Returns a list with one classpath entry per element;
     * each entry is formatted in the way expected by <i>java.net.URLClassLoader</i> (more
     * specifically, with trailing slashes for directories).
     */
    List<String> buildClassPath(String appClassPath,String conf) throws IOException {
        String openapiHome = getOpenapiHome();

        List<String> cp = new ArrayList<String>();
        addToClassPath(cp, getenv("OPENAPI_CLASSPATH"));
        addToClassPath(cp, appClassPath);

        addToClassPath(cp, getConfDir(conf));

        boolean prependClasses = !isEmpty(getenv("OPENAPI_PREPEND_CLASSES"));
        boolean isTesting = "1".equals(getenv("OPENAPI_TESTING"));
        if (prependClasses || isTesting) {
            String scala = getScalaVersion();
            List<String> projects = Arrays.asList("core", "repl", "mllib", "bagel", "graphx",
                    "streaming", "tools", "sql/catalyst", "sql/core", "sql/hive", "sql/hive-thriftserver",
                    "yarn", "launcher", "network/common", "network/shuffle", "network/yarn");
            if (prependClasses) {
                if (!isTesting) {
                    System.err.println(
                            "NOTE: OPENAPI_PREPEND_CLASSES is set, placing locally compiled Openapi classes ahead of " +
                                    "assembly.");
                }
                for (String project : projects) {
                    addToClassPath(cp, String.format("%s/%s/target/scala-%s/classes", openapiHome, project,
                            scala));
                }
            }
            if (isTesting) {
                for (String project : projects) {
                    addToClassPath(cp, String.format("%s/%s/target/scala-%s/test-classes", openapiHome,
                            project, scala));
                }
            }

            // Add this path to include jars that are shaded in the final deliverable created during
            // the maven build. These jars are copied to this directory during the build.
            addToClassPath(cp, String.format("%s/core/target/jars/*", openapiHome));
        }

        // We can't rely on the ENV_OPENAPI_ASSEMBLY variable to be set. Certain situations, such as
        // when running unit tests, or user code that embeds Openapi and creates a OpenapiContext
        // with a local or local-cluster master, will cause this code to be called from an
        // environment where that env variable is not guaranteed to exist.
        //
        // For the testing case, we rely on the test code to set and propagate the test classpath
        // appropriately.
        //
        // For the user code case, we fall back to looking for the Openapi assembly under OPENAPI_HOME.
        // That duplicates some of the code in the shell scripts that look for the assembly, though.
        String assembly = getenv(ENV_OPENAPI_ASSEMBLY);
        if (assembly == null && !isTesting) {
            assembly = findAssembly();
        }
        addToClassPath(cp, assembly);

        // Datanucleus jars must be included on the classpath. Datanucleus jars do not work if only
        // included in the uber jar as plugin.xml metadata is lost. Both sbt and maven will populate
        // "lib_managed/jars/" with the datanucleus jars when Openapi is built with Hive
        File libdir;
        if (new File(openapiHome, "RELEASE").isFile()) {
            libdir = new File(openapiHome, "applib");
        } else {
            libdir = new File(openapiHome, "lib_managed/jars");
        }

        List<String> loadJars = Arrays.asList("openapi-client|OPENAPI_CLIENT_CONF_DIR", "openapi-master|OPENAPI_CONF_DIR", "openapi-worker|OPENAPI_WORKER_CONF_DIR");
        if (libdir.isDirectory()) {
            for (File jar : libdir.listFiles()) {
                String jarName = jar.getName();
                for (String loadJarName : loadJars) {
                    String [] jarConf=loadJarName.split("\\|");
                    if (jarName.startsWith(jarConf[0]) && jarConf[1].equals(conf)) {
                        addToClassPath(cp, jar.getAbsolutePath());
                    }
                }

            }
        } else {
            checkState(isTesting, "Library directory '%s' does not exist.", libdir.getAbsolutePath());
        }

        addToClassPath(cp, getenv("OPENAPI_DIST_CLASSPATH"));
        return cp;
    }

    /**
     * Adds entries to the classpath.
     *
     * @param cp      List to which the new entries are appended.
     * @param entries New classpath entries (separated by File.pathSeparator).
     */
    private void addToClassPath(List<String> cp, String entries) {
        if (isEmpty(entries)) {
            return;
        }
        String[] split = entries.split(Pattern.quote(File.pathSeparator));
        for (String entry : split) {
            if (!isEmpty(entry)) {
                if (new File(entry).isDirectory() && !entry.endsWith(File.separator)) {
                    entry += File.separator;
                }
                cp.add(entry);
            }
        }
    }

    String getScalaVersion() {
        String scala = getenv("OPENAPI_SCALA_VERSION");
        if (scala != null) {
            return scala;
        }
        String openapiHome = getOpenapiHome();
        File scala210 = new File(openapiHome, "launcher/target/scala-2.10");
        File scala211 = new File(openapiHome, "launcher/target/scala-2.11");
        checkState(!scala210.isDirectory() || !scala211.isDirectory(),
                "Presence of build for both scala versions (2.10 and 2.11) detected.\n" +
                        "Either clean one of them or set OPENAPI_SCALA_VERSION in your environment.");
        if (scala210.isDirectory()) {
            return "2.10";
        } else {
            checkState(scala211.isDirectory(), "Cannot find any build directories.");
            return "2.11";
        }
    }

    String getOpenapiHome() {
        String path = getenv(ENV_OPENAPI_HOME);
        checkState(path != null,
                "Openapi home not found; set it explicitly or use the OPENAPI_HOME environment variable.");
        return path;
    }

    String getenv(String key) {
        return firstNonEmpty(childEnv.get(key), System.getenv(key));
    }

    void setPropertiesFile(String path) {
        effectiveConfig = null;
        this.propertiesFile = path;
    }

    Map<String, String> getEffectiveConfig() throws IOException {
        if (effectiveConfig == null) {
            effectiveConfig = new HashMap<>(conf);
            Properties p = loadPropertiesFile();
            for (String key : p.stringPropertyNames()) {
                if (!effectiveConfig.containsKey(key)) {
                    effectiveConfig.put(key, p.getProperty(key));
                }
            }
        }
        return effectiveConfig;
    }

    /**
     * Loads the configuration file for the application, if it exists. This is either the
     * user-specified properties file, or the openapi-defaults.conf file under the Openapi configuration
     * directory.
     */
    private Properties loadPropertiesFile() throws IOException {
        Properties props = new Properties();
        File propsFile;
        if (propertiesFile != null) {
            propsFile = new File(propertiesFile);
            checkArgument(propsFile.isFile(), "Invalid properties file '%s'.", propertiesFile);
        } else {
            propsFile = new File(getConfDir(), DEFAULT_PROPERTIES_FILE);
        }

        if (propsFile.isFile()) {
            FileInputStream fd = null;
            try {
                fd = new FileInputStream(propsFile);
                props.load(new InputStreamReader(fd, "UTF-8"));
                for (Map.Entry<Object, Object> e : props.entrySet()) {
                    e.setValue(e.getValue().toString().trim());
                }
            } finally {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e) {
                        // Ignore.
                    }
                }
            }
        }

        return props;
    }

    private String findAssembly() {
        String openapiHome = getOpenapiHome();
        File libdir;
        if (new File(openapiHome, "RELEASE").isFile()) {
            libdir = new File(openapiHome, "applib");
            checkState(libdir.isDirectory(), "Library directory '%s' does not exist.",
                    libdir.getAbsolutePath());
        } else {
            libdir = new File(openapiHome, String.format("assembly/target/scala-%s", getScalaVersion()));
        }

        final Pattern re = Pattern.compile("assembly.*launcher.*\\.jar");
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && re.matcher(file.getName()).matches();
            }
        };
        File[] assemblies = libdir.listFiles(filter);
        checkState(assemblies != null && assemblies.length > 0, "No assemblies found in '%s'.", libdir);
        checkState(assemblies.length == 1, "Multiple assemblies found in '%s'.", libdir);
        return assemblies[0].getAbsolutePath();
    }

    private String getConfDir(String conf) {
        String result=null;
        if(conf.equals("OPENAPI_CLIENT_CONF_DIR"))
        {
            result= getClientConfDir();
        }
        else if(conf.equals("OPENAPI_WORKER_CONF_DIR"))
        {
            result= getWorkerConfDir();
        }
        else
        {
            result= getConfDir();
        }
        return result;
    }

    private String getConfDir() {
        String confDir = getenv("OPENAPI_CONF_DIR");
        return confDir != null ? confDir : join(File.separator, getOpenapiHome(), "conf");
    }

    private String getWorkerConfDir() {
        String confDir = getenv("OPENAPI_WORKER_CONF_DIR");
        return confDir != null ? confDir : join(File.separator, getOpenapiHome(), "conf/worker");
    }

    private String getClientConfDir() {
        String confDir = getenv("OPENAPI_CLIENT_CONF_DIR");
        return confDir != null ? confDir : join(File.separator, getOpenapiHome(), "conf/client");
    }

}
