package com.teradata.openapi.framework.launcher;

import java.util.*;

import static com.teradata.openapi.framework.launcher.CommandBuilderUtils.*;

/**
 * Command line interface for the Openapi launcher. Used internally by Openapi scripts.
 */
class Main {

    /**
     * Usage: Main [class] [class args]
     * <p>
     * This CLI works in two different modes:
     * <ul>
     * <li>"openapi-submit": if <i>class</i> is "org.apache.openapi.deploy.OpenapiSubmit", the
     * {@link OpenapiLauncher} class is used to launch a Openapi application.</li>
     * <li>"openapi-class": if another class is provided, an internal Openapi class is run.</li>
     * </ul>
     * <p>
     * This class works in tandem with the "bin/openapi-class" script on Unix-like systems, and
     * "bin/openapi-class2.cmd" batch script on Windows to execute the final command.
     * <p>
     * On Unix-like systems, the output is a list of command arguments, separated by the NULL
     * character. On Windows, the output is a command line suitable for direct execution from the
     * script.
     */
    public static void main(String[] argsArray) throws Exception {
        checkArgument(argsArray.length > 0, "Not enough arguments: missing class name.");

        List<String> args = new ArrayList<String>(Arrays.asList(argsArray));
        String className = args.remove(0);

        boolean printLaunchCommand = !isEmpty(System.getenv("OPENAPI_PRINT_LAUNCH_COMMAND"));
        AbstractCommandBuilder builder;
        builder = new OpenapiClassCommandBuilder(className, args);

        Map<String, String> env = new HashMap<String, String>();
        List<String> cmd = builder.buildCommand(env);
        if (printLaunchCommand) {
            System.err.println("Openapi Command: " + join(" ", cmd));
            System.err.println("========================================");
        }

        if (isWindows()) {
            System.out.println(prepareWindowsCommand(cmd, env));
        } else {
            // In bash, use NULL as the arg separator since it cannot be used in an argument.
            List<String> bashCmd = prepareBashCommand(cmd, env);
            for (String c : bashCmd) {
                System.out.print(c);
                System.out.print('\0');
            }
        }
    }

    /**
     * Prepare a command line for execution from a Windows batch script.
     * <p>
     * The method quotes all arguments so that spaces are handled as expected. Quotes within arguments
     * are "double quoted" (which is batch for escaping a quote). This page has more details about
     * quoting and other batch script fun stuff: http://ss64.com/nt/syntax-esc.html
     */
    private static String prepareWindowsCommand(List<String> cmd, Map<String, String> childEnv) {
        StringBuilder cmdline = new StringBuilder();
        for (Map.Entry<String, String> e : childEnv.entrySet()) {
            cmdline.append(String.format("set %s=%s", e.getKey(), e.getValue()));
            cmdline.append(" && ");
        }
        for (String arg : cmd) {
            cmdline.append(quoteForBatchScript(arg));
            cmdline.append(" ");
        }
        return cmdline.toString();
    }

    /**
     * Prepare the command for execution from a bash script. The final command will have commands to
     * set up any needed environment variables needed by the child process.
     */
    private static List<String> prepareBashCommand(List<String> cmd, Map<String, String> childEnv) {
        if (childEnv.isEmpty()) {
            return cmd;
        }

        List<String> newCmd = new ArrayList<String>();
        newCmd.add("env");

        for (Map.Entry<String, String> e : childEnv.entrySet()) {
            newCmd.add(String.format("%s=%s", e.getKey(), e.getValue()));
        }
        newCmd.addAll(cmd);
        return newCmd;
    }

}
