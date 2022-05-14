package com.teradata.openapi.sample;


import com.teradata.openapi.framework.util.pluginUtil.PluginEnv;

/**
 * Created by John on 2016/9/28.
 */
public class GetScalaVal {
    public static void main(String[] args) {
        String plugFrmtTargetPath = PluginEnv.plugFrmtTargetDir();

        System.out.println(plugFrmtTargetPath);
    }
}
