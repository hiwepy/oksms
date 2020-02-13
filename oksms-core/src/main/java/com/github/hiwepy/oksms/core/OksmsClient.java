package com.github.hiwepy.oksms.core;

import com.github.hiwepy.oksms.core.exception.PluginInvokeException;
import com.github.hiwepy.oksms.core.provider.SmsPropertiesProvider;

public interface OksmsClient  {

	public static final String SMS_URL_KEY = "sms.url";
	public static final String SMS_UID_KEY = "sms.uid";
	public static final String SMS_PWD_KEY = "sms.pwd";
	public static final String SMS_SENDTO = "sms.sendTo";
	public static final String SMS_CONTENT = "sms.content";
	
	/**
	 * 插件执行生命周期1: 在业务逻辑方法被调用之前做增强处理
	 * @param pluginId		: 消息路由指定的插件ID
	 * @param extensionId	： 消息路由指定的插件中扩展点实现对象ID
	 */
	void initialize(String pluginId, String extensionId);
	
	/**
	 * 插件执行生命周期2: 业务逻辑
	 * @param payload		: 消息对象
	 * @return				: 逻辑处理后的返回数据
	 * @throws PluginInvokeException  ： 如果执行有异常，则抛出该异常
	 */
	Object send(OksmsPayload payload) throws PluginInvokeException;
	
	/**
	 * 插件执行生命周期3: 主要用来处理程序中未处理的异常
	 * @param pluginId		: 消息路由指定的插件ID
	 * @param extensionId	： 消息路由指定的插件中扩展点实现对象ID
	 * @param cause			：异常对象
	 */
	void afterThrowing(String pluginId, String extensionId, Throwable cause);

	/**
	 * 插件执行生命周期4: 在业务逻辑方法正常完成后做增强
	 * @param pluginId		: 消息路由指定的插件ID
	 * @param extensionId	： 消息路由指定的插件中扩展点实现对象ID
	 */
	void afterReturning(String pluginId, String extensionId);
	
	SmsPropertiesProvider getPropsProvider();
	
}
