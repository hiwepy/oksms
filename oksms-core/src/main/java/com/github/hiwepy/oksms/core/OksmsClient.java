package com.github.hiwepy.oksms.core;

import java.util.Properties;
import java.util.function.Function;

import com.github.hiwepy.oksms.core.exception.OksmsException;

public interface OksmsClient  {

	public static final String HTTP_CONNECTTIMEOUT = "http.connectTimeout";
	public static final String HTTP_READTIMEOUT = "http.readTimeout";
	public static final String HTTP_CHARSET = "http.charset";
	
	public static final String SMS_URL_KEY = "sms.url";
	public static final String SMS_UID_KEY = "sms.uid";
	public static final String SMS_PWD_KEY = "sms.pwd";
	public static final String SMS_SENDTO = "sms.sendTo";
	public static final String SMS_CONTENT = "sms.content";
	
	/**
	 * 插件执行生命周期1: 在业务逻辑方法被调用之前做增强处理
	 * @param properties 	: 配置对象
	 */
	void initialize(Properties properties);
	
	/**
	 * 插件执行生命周期2: 业务逻辑
	 * @param payload		: 消息对象
	 * @param callback		: 回调函数
	 * @return				: 逻辑处理后的返回数据
	 * @throws OksmsException  ： 如果执行有异常，则抛出该异常
	 */
	void send(OksmsPayload payload, Function<String, Boolean> callback) throws OksmsException;
	
}
