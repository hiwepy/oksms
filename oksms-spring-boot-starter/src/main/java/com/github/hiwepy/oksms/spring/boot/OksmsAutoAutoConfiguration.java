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

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.RuntimeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.hiwepy.oksms.core.OksmsClientPoint;

import okhttp3.OkHttpClient;

@Configuration
@ConditionalOnClass({ OkHttpClient.class, PluginManager.class, OksmsClientPoint.class })
@ConditionalOnProperty(prefix = OksmsProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ OksmsProperties.class })
public class OksmsAutoAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public PluginManager pluginManager() {
		
		System.setProperty("pf4j.mode", RuntimeMode.DEPLOYMENT.toString());
		System.setProperty("pf4j.pluginsDir", "plugins");
		/**
		 * 创建PluginManager对象,此处根据生产环境选择合适的实现，或者自定义实现
		 */
		PluginManager pluginManager = new DefaultPluginManager();
		// PluginManager pluginManager = new Pf4jPluginManager();
		// PluginManager pluginManager = new SpringPluginManager();
		// PluginManager pluginManager = new Pf4jJarPluginManager();
		// PluginManager pluginManager = new Pf4jJarPluginWhitSpringManager();
		return pluginManager;
	}
	
	@Bean
	public OksmsTemplate oksmsTemplate(@Autowired(required = false) OkHttpClient okHttpClient, PluginManager pluginManager, OksmsProperties oksmsProperties) {
		return new OksmsTemplate(okHttpClient, pluginManager, oksmsProperties);
	}
	
}
