package com.github.hiwepy.oksms.extensions;

import java.util.List;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;

public class OksmsPluginTest {

	public static void main(String[] args) {
		
		System.setProperty("pf4j.mode", RuntimeMode.DEPLOYMENT.toString());
		System.setProperty("pf4j.pluginsDir", "plugins");
		
		if(RuntimeMode.DEPLOYMENT.compareTo(RuntimeMode.DEPLOYMENT) == 0) {
			//System.setProperty("pf4j.pluginsDir", System.getProperty("app.home","e:/root") + "/plugins");
		}
		
		/**
		 * 创建PluginManager对象,此处根据生产环境选择合适的实现，或者自定义实现
		 */
		PluginManager pluginManager = new DefaultPluginManager();
		// PluginManager pluginManager = new Pf4jPluginManager();
		// PluginManager pluginManager = new SpringPluginManager();
		// PluginManager pluginManager = new Pf4jJarPluginManager();
		// PluginManager pluginManager = new Pf4jJarPluginWhitSpringManager();

		/**
		 * 加载插件到JVM
		 */
	    pluginManager.loadPlugins();

	    /**
	     * 调用Plugin实现类的start()方法: 
	     */
	    pluginManager.startPlugins();
	    
	    List<PluginWrapper> list = pluginManager.getPlugins();
	    for (PluginWrapper pluginWrapper : list) {
			System.out.println(pluginWrapper.getPluginId());
		}
	    
	    List<OksmsClientPoint> extensions = pluginManager.getExtensions(OksmsClientPoint.class);
	    for (OksmsClientPoint point : extensions) {
	    	
	    	OksmsExtension m = point.getClass().getAnnotation(OksmsExtension.class);
	    	System.out.println(m.name());
	    	
		}
	    
	    /**
	     * 调用Plugin实现类的stop()方法
	     */
	    pluginManager.stopPlugins();
	    
	    System.out.println("=============");
		
	}
	
}
