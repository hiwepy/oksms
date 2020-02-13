/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.hiwepy.oksms.spring.boot;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.github.hiwepy.oksms.core.OksmsClient;
import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.annotation.Primary;
import com.github.hiwepy.oksms.core.exception.PluginInvokeException;

/**
 * TODO
 * @author 		： <a href="https://github.com/hiwepy">wandl</a>
 */
public class OksmsTemplate implements InitializingBean, DisposableBean {

	private OksmsProperties oksmsProperties; 
	private PluginManager pluginManager;
	private Map<String, OksmsClientPoint> clientMap = new ConcurrentHashMap<>();
	private OksmsClientPoint defaultClient;
	
	public OksmsTemplate(PluginManager pluginManager, OksmsProperties oksmsProperties) {
		this.pluginManager = pluginManager;
		this.oksmsProperties = oksmsProperties;
	}
	
	public OksmsClientPoint getDefaultClient() {
		return defaultClient;
	}
	
	public OksmsClientPoint getClient(String name) {
		return clientMap.get(name);
	}
	
	public Object send(String name, OksmsPayload payload) throws PluginInvokeException {
		
		OksmsClientPoint client = getClient(name);
		
		Properties properties = new Properties();
		properties.setProperty(OksmsClient.HTTP_READTIMEOUT, oksmsProperties.getReadTimeout() + "");
		properties.setProperty(OksmsClient.HTTP_CONNECTTIMEOUT, oksmsProperties.getConnectTimeout() + "");
		properties.setProperty(OksmsClient.HTTP_CHARSET, oksmsProperties.getCharset());
		client.initialize(properties);
		
		return client.send(payload);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

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
	    for (OksmsClientPoint client : extensions) {
	    	
	    	OksmsExtension m = client.getClass().getAnnotation(OksmsExtension.class);
	    	clientMap.put(m.name(), client);
	    	
	    	if(null != client.getClass().getAnnotation(Primary.class)) {
	    		defaultClient = client;
	    	}
	    	
		}
		
	}

	@Override
	public void destroy() throws Exception {
		
	}

	
}
