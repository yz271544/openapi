package com.teradata.openapi.framework.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.teradata.openapi.framework.launcher.CommandBuilderUtils.*;

/**
 * Special command builder for handling a CLI invocation of OpenapiSubmit.
 * <p>
 * This builder adds command line parsing compatible with OpenapiSubmit. It handles setting
 * driver-side options and special parsing behavior needed for the special-casing certain internal
 * Openapi applications.
 * <p>
 * This class has also some special features to aid launching pyopenapi.
 */
class OpenapiSubmitCommandBuilder extends AbstractCommandBuilder {

  /**
   * Name of the app resource used to identify the PyOpenapi shell. The command line parser expects
   * the resource name to be the very first argument to openapi-submit in this case.
   *
   * NOTE: this cannot be "pyopenapi-shell" since that identifies the PyOpenapi shell to OpenapiSubmit
   * (see java_gateway.py), and can cause this code to enter into an infinite loop.
   */
  static final String PYOPENAPI_SHELL = "pyopenapi-shell-main";

  /**
   * This is the actual resource name that identifies the PyOpenapi shell to OpenapiSubmit.
   */
  static final String PYOPENAPI_SHELL_RESOURCE = "pyopenapi-shell";

  /**
   * Name of the app resource used to identify the OpenapiR shell. The command line parser expects
   * the resource name to be the very first argument to openapi-submit in this case.
   *
   * NOTE: this cannot be "openapir-shell" since that identifies the OpenapiR shell to OpenapiSubmit
   * (see openapiR.R), and can cause this code to enter into an infinite loop.
   */
  static final String OPENAPIR_SHELL = "openapir-shell-main";

  /**
   * This is the actual resource name that identifies the OpenapiR shell to OpenapiSubmit.
   */
  static final String OPENAPIR_SHELL_RESOURCE = "openapir-shell";

  /**
   * This map must match the class names for available special classes, since this modifies the way
   * command line parsing works. This maps the class name to the resource to use when calling
   * openapi-submit.
   */
  private static final Map<String, String> specialClasses = new HashMap<String, String>();
  static {
    specialClasses.put("org.apache.openapi.repl.Main", "openapi-shell");
    specialClasses.put("org.apache.openapi.sql.hive.thriftserver.OpenapiSQLCLIDriver",
            "openapi-internal");
    specialClasses.put("org.apache.openapi.sql.hive.thriftserver.HiveThriftServer2",
            "openapi-internal");
  }

  final List<String> openapiArgs;
  private final boolean printInfo;

  /**
   * Controls whether mixing openapi-submit arguments with app arguments is allowed. This is needed
   * to parse the command lines for things like bin/openapi-shell, which allows users to mix and
   * match arguments (e.g. "bin/openapi-shell OpenapiShellArg --master foo").
   */
  private boolean allowsMixedArguments;

  OpenapiSubmitCommandBuilder() {
    this.openapiArgs = new ArrayList<String>();
    this.printInfo = false;
  }

  OpenapiSubmitCommandBuilder(List<String> args) {
    this.openapiArgs = new ArrayList<String>();
    List<String> submitArgs = args;
    if (args.size() > 0 && args.get(0).equals(PYOPENAPI_SHELL)) {
      this.allowsMixedArguments = true;
      appResource = PYOPENAPI_SHELL_RESOURCE;
      submitArgs = args.subList(1, args.size());
    } else if (args.size() > 0 && args.get(0).equals(OPENAPIR_SHELL)) {
      this.allowsMixedArguments = true;
      appResource = OPENAPIR_SHELL_RESOURCE;
      submitArgs = args.subList(1, args.size());
    } else {
      this.allowsMixedArguments = false;
    }

    OptionParser parser = new OptionParser();
    parser.parse(submitArgs);
    this.printInfo = parser.infoRequested;
  }

  @Override
  public List<String> buildCommand(Map<String, String> env) throws IOException {
    if (PYOPENAPI_SHELL_RESOURCE.equals(appResource) && !printInfo) {
      return buildPyOpenapiShellCommand(env);
    } else if (OPENAPIR_SHELL_RESOURCE.equals(appResource) && !printInfo) {
      return buildOpenapiRCommand(env);
    } else {
      return buildOpenapiSubmitCommand(env);
    }
  }

  List<String> buildOpenapiSubmitArgs() {
    List<String> args = new ArrayList<String>();
    OpenapiSubmitOptionParser parser = new OpenapiSubmitOptionParser();

    if (verbose) {
      args.add(parser.VERBOSE);
    }

    if (master != null) {
      args.add(parser.MASTER);
      args.add(master);
    }

    if (deployMode != null) {
      args.add(parser.DEPLOY_MODE);
      args.add(deployMode);
    }

    if (appName != null) {
      args.add(parser.NAME);
      args.add(appName);
    }

    for (Map.Entry<String, String> e : conf.entrySet()) {
      args.add(parser.CONF);
      args.add(String.format("%s=%s", e.getKey(), e.getValue()));
    }

    if (propertiesFile != null) {
      args.add(parser.PROPERTIES_FILE);
      args.add(propertiesFile);
    }

    if (!jars.isEmpty()) {
      args.add(parser.JARS);
      args.add(join(",", jars));
    }

    if (!files.isEmpty()) {
      args.add(parser.FILES);
      args.add(join(",", files));
    }

    if (!pyFiles.isEmpty()) {
      args.add(parser.PY_FILES);
      args.add(join(",", pyFiles));
    }

    if (mainClass != null) {
      args.add(parser.CLASS);
      args.add(mainClass);
    }

    args.addAll(openapiArgs);
    if (appResource != null) {
      args.add(appResource);
    }
    args.addAll(appArgs);

    return args;
  }

  private List<String> buildOpenapiSubmitCommand(Map<String, String> env) throws IOException {
    // Load the properties file and check whether openapi-submit will be running the app's driver
    // or just launching a cluster app. When running the driver, the JVM's argument will be
    // modified to cover the driver's configuration.
    Map<String, String> config = getEffectiveConfig();
    boolean isClientMode = isClientMode(config);
    String extraClassPath = isClientMode ? config.get(OpenapiLauncher.DRIVER_EXTRA_CLASSPATH) : null;

    String conf="";
    List<String> cmd = buildJavaCommand(extraClassPath,conf);
    // Take Thrift Server as daemon
    if (isThriftServer(mainClass)) {
      addOptionString(cmd, System.getenv("OPENAPI_DAEMON_JAVA_OPTS"));
    }
    addOptionString(cmd, System.getenv("OPENAPI_SUBMIT_OPTS"));
    addOptionString(cmd, System.getenv("OPENAPI_JAVA_OPTS"));

    if (isClientMode) {
      // Figuring out where the memory value come from is a little tricky due to precedence.
      // Precedence is observed in the following order:
      // - explicit configuration (setConf()), which also covers --driver-memory cli argument.
      // - properties file.
      // - OPENAPI_DRIVER_MEMORY env variable
      // - OPENAPI_MEM env variable
      // - default value (1g)
      // Take Thrift Server as daemon
      String tsMemory =
              isThriftServer(mainClass) ? System.getenv("OPENAPI_DAEMON_MEMORY") : null;
      String memory = firstNonEmpty(tsMemory, config.get(OpenapiLauncher.DRIVER_MEMORY),
              System.getenv("OPENAPI_DRIVER_MEMORY"), System.getenv("OPENAPI_MEM"), DEFAULT_MEM);
      cmd.add("-Xms" + memory);
      cmd.add("-Xmx" + memory);
      addOptionString(cmd, config.get(OpenapiLauncher.DRIVER_EXTRA_JAVA_OPTIONS));
      mergeEnvPathList(env, getLibPathEnvName(),
              config.get(OpenapiLauncher.DRIVER_EXTRA_LIBRARY_PATH));
    }

    addPermGenSizeOpt(cmd);
    cmd.add("org.apache.openapi.deploy.OpenapiSubmit");
    cmd.addAll(buildOpenapiSubmitArgs());
    return cmd;
  }

  private List<String> buildPyOpenapiShellCommand(Map<String, String> env) throws IOException {
    // For backwards compatibility, if a script is specified in
    // the pyopenapi command line, then run it using openapi-submit.
    if (!appArgs.isEmpty() && appArgs.get(0).endsWith(".py")) {
      System.err.println(
              "WARNING: Running python applications through 'pyopenapi' is deprecated as of Openapi 1.0.\n" +
                      "Use ./bin/openapi-submit <python file>");
      appResource = appArgs.get(0);
      appArgs.remove(0);
      return buildCommand(env);
    }

    checkArgument(appArgs.isEmpty(), "pyopenapi does not support any application options.");

    // When launching the pyopenapi shell, the openapi-submit arguments should be stored in the
    // PYOPENAPI_SUBMIT_ARGS env variable.
    constructEnvVarArgs(env, "PYOPENAPI_SUBMIT_ARGS");

    // The executable is the PYOPENAPI_DRIVER_PYTHON env variable set by the pyopenapi script,
    // followed by PYOPENAPI_DRIVER_PYTHON_OPTS.
    List<String> pyargs = new ArrayList<String>();
    pyargs.add(firstNonEmpty(System.getenv("PYOPENAPI_DRIVER_PYTHON"), "python"));
    String pyOpts = System.getenv("PYOPENAPI_DRIVER_PYTHON_OPTS");
    if (!isEmpty(pyOpts)) {
      pyargs.addAll(parseOptionString(pyOpts));
    }

    return pyargs;
  }

  private List<String> buildOpenapiRCommand(Map<String, String> env) throws IOException {
    if (!appArgs.isEmpty() && appArgs.get(0).endsWith(".R")) {
      appResource = appArgs.get(0);
      appArgs.remove(0);
      return buildCommand(env);
    }
    // When launching the OpenapiR shell, store the openapi-submit arguments in the OPENAPIR_SUBMIT_ARGS
    // env variable.
    constructEnvVarArgs(env, "OPENAPIR_SUBMIT_ARGS");

    // Set shell.R as R_PROFILE_USER to load the OpenapiR package when the shell comes up.
    String openapiHome = System.getenv("OPENAPI_HOME");
    env.put("R_PROFILE_USER",
            join(File.separator, openapiHome, "R", "lib", "OpenapiR", "profile", "shell.R"));

    List<String> args = new ArrayList<String>();
    args.add(firstNonEmpty(System.getenv("OPENAPIR_DRIVER_R"), "R"));
    return args;
  }

  private void constructEnvVarArgs(
          Map<String, String> env,
          String submitArgsEnvVariable) throws IOException {
    mergeEnvPathList(env, getLibPathEnvName(),
            getEffectiveConfig().get(OpenapiLauncher.DRIVER_EXTRA_LIBRARY_PATH));

    StringBuilder submitArgs = new StringBuilder();
    for (String arg : buildOpenapiSubmitArgs()) {
      if (submitArgs.length() > 0) {
        submitArgs.append(" ");
      }
      submitArgs.append(quoteForCommandString(arg));
    }
    env.put(submitArgsEnvVariable, submitArgs.toString());
  }

  private boolean isClientMode(Map<String, String> userProps) {
    String userMaster = firstNonEmpty(master, userProps.get(OpenapiLauncher.OPENAPI_MASTER));
    // Default master is "local[*]", so assume client mode in that case.
    return userMaster == null ||
            "client".equals(deployMode) ||
            (!userMaster.equals("yarn-cluster") && deployMode == null);
  }

  /**
   * Return whether the given main class represents a thrift server.
   */
  private boolean isThriftServer(String mainClass) {
    return (mainClass != null &&
            mainClass.equals("org.apache.openapi.sql.hive.thriftserver.HiveThriftServer2"));
  }


  private class OptionParser extends OpenapiSubmitOptionParser {

    boolean infoRequested = false;

    @Override
    protected boolean handle(String opt, String value) {
      if (opt.equals(MASTER)) {
        master = value;
      } else if (opt.equals(DEPLOY_MODE)) {
        deployMode = value;
      } else if (opt.equals(PROPERTIES_FILE)) {
        propertiesFile = value;
      } else if (opt.equals(DRIVER_MEMORY)) {
        conf.put(OpenapiLauncher.DRIVER_MEMORY, value);
      } else if (opt.equals(DRIVER_JAVA_OPTIONS)) {
        conf.put(OpenapiLauncher.DRIVER_EXTRA_JAVA_OPTIONS, value);
      } else if (opt.equals(DRIVER_LIBRARY_PATH)) {
        conf.put(OpenapiLauncher.DRIVER_EXTRA_LIBRARY_PATH, value);
      } else if (opt.equals(DRIVER_CLASS_PATH)) {
        conf.put(OpenapiLauncher.DRIVER_EXTRA_CLASSPATH, value);
      } else if (opt.equals(CONF)) {
        String[] setConf = value.split("=", 2);
        checkArgument(setConf.length == 2, "Invalid argument to %s: %s", CONF, value);
        conf.put(setConf[0], setConf[1]);
      } else if (opt.equals(CLASS)) {
        // The special classes require some special command line handling, since they allow
        // mixing openapi-submit arguments with arguments that should be propagated to the shell
        // itself. Note that for this to work, the "--class" argument must come before any
        // non-openapi-submit arguments.
        mainClass = value;
        if (specialClasses.containsKey(value)) {
          allowsMixedArguments = true;
          appResource = specialClasses.get(value);
        }
      } else if (opt.equals(HELP) || opt.equals(USAGE_ERROR)) {
        infoRequested = true;
        openapiArgs.add(opt);
      } else if (opt.equals(VERSION)) {
        infoRequested = true;
        openapiArgs.add(opt);
      } else {
        openapiArgs.add(opt);
        if (value != null) {
          openapiArgs.add(value);
        }
      }
      return true;
    }

    @Override
    protected boolean handleUnknown(String opt) {
      // When mixing arguments, add unrecognized parameters directly to the user arguments list. In
      // normal mode, any unrecognized parameter triggers the end of command line parsing, and the
      // parameter itself will be interpreted by OpenapiSubmit as the application resource. The
      // remaining params will be appended to the list of OpenapiSubmit arguments.
      if (allowsMixedArguments) {
        appArgs.add(opt);
        return true;
      } else {
        checkArgument(!opt.startsWith("-"), "Unrecognized option: %s", opt);
        openapiArgs.add(opt);
        return false;
      }
    }

    @Override
    protected void handleExtraArgs(List<String> extra) {
      for (String arg : extra) {
        openapiArgs.add(arg);
      }
    }

  }

}
