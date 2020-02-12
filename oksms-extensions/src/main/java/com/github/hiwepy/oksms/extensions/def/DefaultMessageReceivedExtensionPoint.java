package com.github.hiwepy.oksms.extensions.def;

import java.text.SimpleDateFormat;

import org.apache.commons.codec.binary.StringUtils;
import org.pf4j.Extension;
import org.springframework.amqp.core.Message;

import com.alibaba.fastjson.JSONObject;
import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.exception.PluginInvokeException;

import net.jeebiz.mbus.api.entities.MessageRoutingModel;
import net.jeebiz.mbus.plugin.sdk.Constant;
import net.jeebiz.mbus.plugin.sdk.annotation.ExtensionMapping;

@Extension
public class DefaultMessageReceivedExtensionPoint implements OksmsClientPoint {

	private final String appKey = "";
	private final String appSecret = "";
	private final String version = "1.0";
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void before(String pluginId, String extensionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object invoke(Object message, MessageRoutingModel routing) throws PluginInvokeException {
		// TODO Auto-generated method stub
		Message nativeMessage = (Message) message;
		
		String returnStr = StringUtils.newStringUtf8(nativeMessage.getBody());
        JSONObject jsStr = JSONObject.parseObject(returnStr);
		return null;
	}

	@Override
	public void afterThrowing(String pluginId, String extensionId, Throwable cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterReturning(String pluginId, String extensionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(String pluginId, String extensionId, Object message, Throwable cause) {
		// TODO Auto-generated method stub
		
	}

}
