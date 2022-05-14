package com.teradata.openapi.framework.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.teradata.openapi.framework.launcher.CommandBuilderUtils.*;

/**
 * Command builder for internal Openapi classes.
 * <p>
 * This class handles building the command to launch all internal Openapi classes except for
 * OpenapiSubmit (which is handled by {@link OpenapiSubmitCommandBuilder} class.
 */
class OpenapiClassCommandBuilder extends AbstractCommandBuilder {

    private final String className;
    private final List<String> classArgs;

    OpenapiClassCommandBuilder(String className, List<String> classArgs) {
        this.className = className;
        this.classArgs = classArgs;
    }

    @Override
    public List<String> buildCommand(Map<String, String> env) throws IOException {
        List<String> javaOptsKeys = new ArrayList<String>();
        String memKey = null;
        String extraClassPath = null;
        String confKey = null;

        // Master, Worker, and HistoryServer use OPENAPI_DAEMON_JAVA_OPTS (and specific opts) +
        // OPENAPI_DAEMON_MEMORY.
        if (className.equals("com.teradata.openapi.master.deploy.Master")) {
            javaOptsKeys.add("OPENAPI_DAEMON_JAVA_OPTS");
            javaOptsKeys.add("OPENAPI_MASTER_OPTS");
            memKey = "OPENAPI_DAEMON_MEMORY";
            confKey="OPENAPI_CONF_DIR";
        } else if (className.equals("com.teradata.openapi.worker.deploy.Worker")) {
            javaOptsKeys.add("OPENAPI_DAEMON_JAVA_OPTS");
            javaOptsKeys.add("OPENAPI_WORKER_OPTS");
            memKey = "OPENAPI_DAEMON_MEMORY";
            confKey="OPENAPI_WORKER_CONF_DIR";
        } else if (className.equals("com.teradata.openapi.httpserver.jetty.Bootstrap")) {
            javaOptsKeys.add("OPENAPI_CLIENT_OPTS");
            memKey = "OPENAPI_CLIENT_MEMORY";
            confKey="OPENAPI_CLIENT_CONF_DIR";
        } else if (className.startsWith("org.apache.openapi.tools.")) {
            String openapiHome = getOpenapiHome();
            File toolsDir = new File(join(File.separator, openapiHome, "tools", "target",
                    "scala-" + getScalaVersion()));
            checkState(toolsDir.isDirectory(), "Cannot find tools build directory.");

            Pattern re = Pattern.compile("openapi-tools_.*\\.jar");
            for (File f : toolsDir.listFiles()) {
                if (re.matcher(f.getName()).matches()) {
                    extraClassPath = f.getAbsolutePath();
                    break;
                }
            }

            checkState(extraClassPath != null,
                    "Failed to find Openapi Tools Jar in %s.\n" +
                            "You need to run \"build/sbt tools/package\" before running %s.",
                    toolsDir.getAbsolutePath(), className);

            javaOptsKeys.add("OPENAPI_JAVA_OPTS");
        } else {
            javaOptsKeys.add("OPENAPI_JAVA_OPTS");
            memKey = "OPENAPI_DRIVER_MEMORY";
            confKey="OPENAPI_CONF_DIR";
        }

        List<String> cmd = buildJavaCommand(extraClassPath,confKey);
        for (String key : javaOptsKeys) {
            addOptionString(cmd, System.getenv(key));
        }

        String mem = firstNonEmpty(memKey != null ? System.getenv(memKey) : null, DEFAULT_MEM);
        cmd.add("-Xms" + mem);
        cmd.add("-Xmx" + mem);
        addPermGenSizeOpt(cmd);
        cmd.add(className);
        cmd.addAll(classArgs);
        return cmd;
    }

}
