package com.github.hiwepy.oksms.extensions;


import org.apache.commons.lang3.StringUtils;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

/**
 * https://github.com/pf4j/pf4j
 */
public class OksmsPlugin extends Plugin {

    public OksmsPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
    	 System.out.println("OksmsPlugin.start()");
         // for testing the development mode
         if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
         	System.out.println(StringUtils.upperCase("OksmsPlugin"));
         }
    }
    
    @Override
    public void stop() {
    	System.out.println("OksmsPlugin.stop()");
    }
   
}