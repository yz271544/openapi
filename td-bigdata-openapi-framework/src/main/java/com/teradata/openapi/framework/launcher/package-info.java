package com.teradata.openapi.framework.launcher;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

/**
 * Library for launching Openapi applications.
 * 
 * <p>
 * This library allows applications to launch Openapi programmatically. There's only one entry point to the library - the
 * {@link com.teradata.openapi.framework.launcher.OpenapiLauncher} class.
 * </p>
 * 
 * <p>
 * The
 * {@link com.teradata.openapi.framework.launcher.OpenapiLauncher#startApplication(com.teradata.openapi.framework.launcher.OpenapiAppHandle.Listener...)}
 * can be used to start Openapi and provide a handle to monitor and control the running application:
 * </p>
 * 
 * <pre>
 * {@code
 *   import org.apache.openapi.launcher.OpenapiAppHandle;
 *   import org.apache.openapi.launcher.OpenapiLauncher;
 * 
 *   public class MyLauncher {
 *     public static void main(String[] args) throws Exception {
 *       OpenapiAppHandle handle = new OpenapiLauncher()
 *         .setAppResource("/my/app.jar")
 *         .setMainClass("my.openapi.app.Main")
 *         .setMaster("local")
 *         .setConf(OpenapiLauncher.DRIVER_MEMORY, "2g")
 *         .startApplication();
 *       // Use handle API to monitor / control application.
 *     }
 *   }
 * }
 * </pre>
 * 
 * <p>
 * It's also possible to launch a raw child process, using the {@link com.teradata.openapi.framework.launcher.OpenapiLauncher#launch()}
 * method:
 * </p>
 * 
 * <pre>
 * {@code
 *   import org.apache.openapi.launcher.OpenapiLauncher;
 * 
 *   public class MyLauncher {
 *     public static void main(String[] args) throws Exception {
 *       Process openapi = new OpenapiLauncher()
 *         .setAppResource("/my/app.jar")
 *         .setMainClass("my.openapi.app.Main")
 *         .setMaster("local")
 *         .setConf(OpenapiLauncher.DRIVER_MEMORY, "2g")
 *         .launch();
 *       openapi.waitFor();
 *     }
 *   }
 * }
 * </pre>
 * 
 * <p>
 * This method requires the calling code to manually manage the child process, including its output streams (to avoid possible deadlocks).
 * It's recommended that
 * {@link com.teradata.openapi.framework.launcher.OpenapiLauncher#startApplication(com.teradata.openapi.framework.launcher.OpenapiAppHandle.Listener...)}
 * be used instead.
 * </p>
 */
