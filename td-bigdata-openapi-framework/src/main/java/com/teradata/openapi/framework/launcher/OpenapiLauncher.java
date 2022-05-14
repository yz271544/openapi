package com.teradata.openapi.framework.launcher;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.teradata.openapi.framework.launcher.CommandBuilderUtils.*;

/**
 * Launcher for Openapi applications.
 * <p>
 * Use this class to start Openapi applications programmatically. The class uses a builder pattern
 * to allow clients to configure the Openapi application and launch it as a child process.
 * </p>
 */
public class OpenapiLauncher {

  /** The Openapi master. */
  public static final String OPENAPI_MASTER = "openapi.master";

  /** Configuration key for the driver memory. */
  public static final String DRIVER_MEMORY = "openapi.driver.memory";
  /** Configuration key for the driver class path. */
  public static final String DRIVER_EXTRA_CLASSPATH = "openapi.driver.extraClassPath";
  /** Configuration key for the driver VM options. */
  public static final String DRIVER_EXTRA_JAVA_OPTIONS = "openapi.driver.extraJavaOptions";
  /** Configuration key for the driver native library path. */
  public static final String DRIVER_EXTRA_LIBRARY_PATH = "openapi.driver.extraLibraryPath";

  /** Configuration key for the executor memory. */
  public static final String EXECUTOR_MEMORY = "openapi.executor.memory";
  /** Configuration key for the executor class path. */
  public static final String EXECUTOR_EXTRA_CLASSPATH = "openapi.executor.extraClassPath";
  /** Configuration key for the executor VM options. */
  public static final String EXECUTOR_EXTRA_JAVA_OPTIONS = "openapi.executor.extraJavaOptions";
  /** Configuration key for the executor native library path. */
  public static final String EXECUTOR_EXTRA_LIBRARY_PATH = "openapi.executor.extraLibraryPath";
  /** Configuration key for the number of executor CPU cores. */
  public static final String EXECUTOR_CORES = "openapi.executor.cores";

  /** Logger name to use when launching a child process. */
  public static final String CHILD_PROCESS_LOGGER_NAME = "openapi.launcher.childProcLoggerName";

  /**
   * Maximum time (in ms) to wait for a child process to connect back to the launcher server
   * when using @link{#start()}.
   */
  public static final String CHILD_CONNECTION_TIMEOUT = "openapi.launcher.childConectionTimeout";

  /** Used internally to create unique logger names. */
  private static final AtomicInteger COUNTER = new AtomicInteger();

  static final Map<String, String> launcherConfig = new HashMap<String, String>();

  /**
   * Set a configuration value for the launcher library. These config values do not affect the
   * launched application, but rather the behavior of the launcher library itself when managing
   * applications.
   *
   * @since 1.6.0
   * @param name Config name.
   * @param value Config value.
   */
  public static void setConfig(String name, String value) {
    launcherConfig.put(name, value);
  }

  // Visible for testing.
  final OpenapiSubmitCommandBuilder builder;

  public OpenapiLauncher() {
    this(null);
  }

  /**
   * Creates a launcher that will set the given environment variables in the child.
   *
   * @param env Environment variables to set.
   */
  public OpenapiLauncher(Map<String, String> env) {
    this.builder = new OpenapiSubmitCommandBuilder();
    if (env != null) {
      this.builder.childEnv.putAll(env);
    }
  }

  /**
   * Set a custom JAVA_HOME for launching the Openapi application.
   *
   * @param javaHome Path to the JAVA_HOME to use.
   * @return This launcher.
   */
  public OpenapiLauncher setJavaHome(String javaHome) {
    checkNotNull(javaHome, "javaHome");
    builder.javaHome = javaHome;
    return this;
  }

  /**
   * Set a custom Openapi installation location for the application.
   *
   * @param openapiHome Path to the Openapi installation to use.
   * @return This launcher.
   */
  public OpenapiLauncher setOpenapiHome(String openapiHome) {
    checkNotNull(openapiHome, "openapiHome");
    builder.childEnv.put(ENV_OPENAPI_HOME, openapiHome);
    return this;
  }

  /**
   * Set a custom properties file with Openapi configuration for the application.
   *
   * @param path Path to custom properties file to use.
   * @return This launcher.
   */
  public OpenapiLauncher setPropertiesFile(String path) {
    checkNotNull(path, "path");
    builder.setPropertiesFile(path);
    return this;
  }

  /**
   * Set a single configuration value for the application.
   *
   * @param key Configuration key.
   * @param value The value to use.
   * @return This launcher.
   */
  public OpenapiLauncher setConf(String key, String value) {
    checkNotNull(key, "key");
    checkNotNull(value, "value");
    checkArgument(key.startsWith("openapi."), "'key' must start with 'openapi.'");
    builder.conf.put(key, value);
    return this;
  }

  /**
   * Set the application name.
   *
   * @param appName Application name.
   * @return This launcher.
   */
  public OpenapiLauncher setAppName(String appName) {
    checkNotNull(appName, "appName");
    builder.appName = appName;
    return this;
  }

  /**
   * Set the Openapi master for the application.
   *
   * @param master Openapi master.
   * @return This launcher.
   */
  public OpenapiLauncher setMaster(String master) {
    checkNotNull(master, "master");
    builder.master = master;
    return this;
  }

  /**
   * Set the deploy mode for the application.
   *
   * @param mode Deploy mode.
   * @return This launcher.
   */
  public OpenapiLauncher setDeployMode(String mode) {
    checkNotNull(mode, "mode");
    builder.deployMode = mode;
    return this;
  }

  /**
   * Set the main application resource. This should be the location of a jar file for Scala/Java
   * applications, or a python script for PyOpenapi applications.
   *
   * @param resource Path to the main application resource.
   * @return This launcher.
   */
  public OpenapiLauncher setAppResource(String resource) {
    checkNotNull(resource, "resource");
    builder.appResource = resource;
    return this;
  }

  /**
   * Sets the application class name for Java/Scala applications.
   *
   * @param mainClass Application's main class.
   * @return This launcher.
   */
  public OpenapiLauncher setMainClass(String mainClass) {
    checkNotNull(mainClass, "mainClass");
    builder.mainClass = mainClass;
    return this;
  }

  /**
   * Adds a no-value argument to the Openapi invocation. If the argument is known, this method
   * validates whether the argument is indeed a no-value argument, and throws an exception
   * otherwise.
   * <p>
   * Use this method with caution. It is possible to create an invalid Openapi command by passing
   * unknown arguments to this method, since those are allowed for forward compatibility.
   *
   * @since 1.5.0
   * @param arg Argument to add.
   * @return This launcher.
   */
  public OpenapiLauncher addOpenapiArg(String arg) {
    OpenapiSubmitOptionParser validator = new ArgumentValidator(false);
    validator.parse(Arrays.asList(arg));
    builder.openapiArgs.add(arg);
    return this;
  }

  /**
   * Adds an argument with a value to the Openapi invocation. If the argument name corresponds to
   * a known argument, the code validates that the argument actually expects a value, and throws
   * an exception otherwise.
   * <p>
   * It is safe to add arguments modified by other methods in this class (such as
   * {@link #setMaster(String)} - the last invocation will be the one to take effect.
   * <p>
   * Use this method with caution. It is possible to create an invalid Openapi command by passing
   * unknown arguments to this method, since those are allowed for forward compatibility.
   *
   * @since 1.5.0
   * @param name Name of argument to add.
   * @param value Value of the argument.
   * @return This launcher.
   */
  public OpenapiLauncher addOpenapiArg(String name, String value) {
    OpenapiSubmitOptionParser validator = new ArgumentValidator(true);
    if (validator.MASTER.equals(name)) {
      setMaster(value);
    } else if (validator.PROPERTIES_FILE.equals(name)) {
      setPropertiesFile(value);
    } else if (validator.CONF.equals(name)) {
      String[] vals = value.split("=", 2);
      setConf(vals[0], vals[1]);
    } else if (validator.CLASS.equals(name)) {
      setMainClass(value);
    } else if (validator.JARS.equals(name)) {
      builder.jars.clear();
      for (String jar : value.split(",")) {
        addJar(jar);
      }
    } else if (validator.FILES.equals(name)) {
      builder.files.clear();
      for (String file : value.split(",")) {
        addFile(file);
      }
    } else if (validator.PY_FILES.equals(name)) {
      builder.pyFiles.clear();
      for (String file : value.split(",")) {
        addPyFile(file);
      }
    } else {
      validator.parse(Arrays.asList(name, value));
      builder.openapiArgs.add(name);
      builder.openapiArgs.add(value);
    }
    return this;
  }

  /**
   * Adds command line arguments for the application.
   *
   * @param args Arguments to pass to the application's main class.
   * @return This launcher.
   */
  public OpenapiLauncher addAppArgs(String... args) {
    for (String arg : args) {
      checkNotNull(arg, "arg");
      builder.appArgs.add(arg);
    }
    return this;
  }

  /**
   * Adds a jar file to be submitted with the application.
   *
   * @param jar Path to the jar file.
   * @return This launcher.
   */
  public OpenapiLauncher addJar(String jar) {
    checkNotNull(jar, "jar");
    builder.jars.add(jar);
    return this;
  }

  /**
   * Adds a file to be submitted with the application.
   *
   * @param file Path to the file.
   * @return This launcher.
   */
  public OpenapiLauncher addFile(String file) {
    checkNotNull(file, "file");
    builder.files.add(file);
    return this;
  }

  /**
   * Adds a python file / zip / egg to be submitted with the application.
   *
   * @param file Path to the file.
   * @return This launcher.
   */
  public OpenapiLauncher addPyFile(String file) {
    checkNotNull(file, "file");
    builder.pyFiles.add(file);
    return this;
  }

  /**
   * Enables verbose reporting for OpenapiSubmit.
   *
   * @param verbose Whether to enable verbose output.
   * @return This launcher.
   */
  public OpenapiLauncher setVerbose(boolean verbose) {
    builder.verbose = verbose;
    return this;
  }

  /**
   * Launches a sub-process that will start the configured Openapi application.
   * <p>
   * The {@link #startApplication(OpenapiAppHandle.Listener...)} method is preferred when launching
   * Openapi, since it provides better control of the child application.
   *
   * @return A process handle for the Openapi app.
   */
  public Process launch() throws IOException {
    return createBuilder().start();
  }

  /**
   * Starts a Openapi application.
   * <p>
   * This method returns a handle that provides information about the running application and can
   * be used to do basic interaction with it.
   * <p>
   * The returned handle assumes that the application will instantiate a single OpenapiContext
   * during its lifetime. Once that context reports a final state (one that indicates the
   * OpenapiContext has stopped), the handle will not perform new state transitions, so anything
   * that happens after that cannot be monitored. If the underlying application is launched as
   * a child process, {@link OpenapiAppHandle#kill()} can still be used to kill the child process.
   * <p>
   * Currently, all applications are launched as child processes. The child's stdout and stderr
   * are merged and written to a logger (see <code>java.util.logging</code>). The logger's name
   * can be defined by setting {@link #CHILD_PROCESS_LOGGER_NAME} in the app's configuration. If
   * that option is not set, the code will try to derive a name from the application's name or
   * main class / script file. If those cannot be determined, an internal, unique name will be
   * used. In all cases, the logger name will start with "org.apache.openapi.launcher.app", to fit
   * more easily into the configuration of commonly-used logging systems.
   *
   * @since 1.6.0
   * @param listeners Listeners to add to the handle before the app is launched.
   * @return A handle for the launched application.
   */
  public OpenapiAppHandle startApplication(OpenapiAppHandle.Listener... listeners) throws IOException {
    ChildProcAppHandle handle = LauncherServer.newAppHandle();
    for (OpenapiAppHandle.Listener l : listeners) {
      handle.addListener(l);
    }

    String appName = builder.getEffectiveConfig().get(CHILD_PROCESS_LOGGER_NAME);
    if (appName == null) {
      if (builder.appName != null) {
        appName = builder.appName;
      } else if (builder.mainClass != null) {
        int dot = builder.mainClass.lastIndexOf(".");
        if (dot >= 0 && dot < builder.mainClass.length() - 1) {
          appName = builder.mainClass.substring(dot + 1, builder.mainClass.length());
        } else {
          appName = builder.mainClass;
        }
      } else if (builder.appResource != null) {
        appName = new File(builder.appResource).getName();
      } else {
        appName = String.valueOf(COUNTER.incrementAndGet());
      }
    }

    String loggerPrefix = getClass().getPackage().getName();
    String loggerName = String.format("%s.app.%s", loggerPrefix, appName);
    ProcessBuilder pb = createBuilder().redirectErrorStream(true);
    pb.environment().put(LauncherProtocol.ENV_LAUNCHER_PORT,
            String.valueOf(LauncherServer.getServerInstance().getPort()));
    pb.environment().put(LauncherProtocol.ENV_LAUNCHER_SECRET, handle.getSecret());
    try {
      handle.setChildProc(pb.start(), loggerName);
    } catch (IOException ioe) {
      handle.kill();
      throw ioe;
    }

    return handle;
  }

  private ProcessBuilder createBuilder() {
    List<String> cmd = new ArrayList<String>();
    String script = isWindows() ? "openapi-submit.cmd" : "openapi-submit";
    cmd.add(join(File.separator, builder.getOpenapiHome(), "bin", script));
    cmd.addAll(builder.buildOpenapiSubmitArgs());

    // Since the child process is a batch script, let's quote things so that special characters are
    // preserved, otherwise the batch interpreter will mess up the arguments. Batch scripts are
    // weird.
    if (isWindows()) {
      List<String> winCmd = new ArrayList<String>();
      for (String arg : cmd) {
        winCmd.add(quoteForBatchScript(arg));
      }
      cmd = winCmd;
    }

    ProcessBuilder pb = new ProcessBuilder(cmd.toArray(new String[cmd.size()]));
    for (Map.Entry<String, String> e : builder.childEnv.entrySet()) {
      pb.environment().put(e.getKey(), e.getValue());
    }
    return pb;
  }

  private static class ArgumentValidator extends OpenapiSubmitOptionParser {

    private final boolean hasValue;

    ArgumentValidator(boolean hasValue) {
      this.hasValue = hasValue;
    }

    @Override
    protected boolean handle(String opt, String value) {
      if (value == null && hasValue) {
        throw new IllegalArgumentException(String.format("'%s' does not expect a value.", opt));
      }
      return true;
    }

    @Override
    protected boolean handleUnknown(String opt) {
      // Do not fail on unknown arguments, to support future arguments added to OpenapiSubmit.
      return true;
    }

    protected void handleExtraArgs(List<String> extra) {
      // No op.
    }

  };

}
