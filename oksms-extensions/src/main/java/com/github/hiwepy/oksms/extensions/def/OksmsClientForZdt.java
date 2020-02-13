package com.github.hiwepy.oksms.extensions.def;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.exception.PluginInvokeException;
import com.github.hiwepy.oksms.core.provider.SmsPropertiesProvider;

@OksmsExtension(name = "zdtsms", method = "GET", protocol = "http")
public class OksmsClientForZdt extends OksmsClientPoint {

	public static final String SMS_TOKEN_KEY = "sms.token";
	public static final String SMS_MSGTYPE_KEY = "sms.msgtype";
	public static final String SMS_SOURCE_KEY = "sms.source";
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String openurl = "http://open.zdt.zju.edu.cn:8080/UcOpen/api/sendSms/";
	protected String DEFAULT_SMSURL = "http://zdt-sms.zju.edu.cn:8086/smsInterface/sendsms";
	/**
	 * SMS服务商提供的appid对应的token
	 */ 
	protected String token;
	/**
	 * 发送类型 1:普通短信 2:长短信(超过64字符) 
	 */ 
	protected String msgtype;
	/**
	 * 调用SMS接口的来源，即请求方标记
	 */ 
	protected String source;
	
	public OksmsClientForZdt(){
		super();
	}
	
	public OksmsClientForZdt(SmsPropertiesProvider propsProvider){
		super(propsProvider);
	}
	
	@Override
	public Object send(OksmsPayload payload) throws PluginInvokeException {
		int ret = 99;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", getUid());
			params.put("password", getPwd());
			params.put("to", payload.getMobile()); // 短信接收号码，多个号码（用英文逗号隔开）视为一次群发操作
			params.put("text", URLEncoder.encode(payload.getContent(), "GBK"));
			params.put("msgtype", getMsgtype()); // 发送类型 1:普通短信 2:长短信(超过64字符)
			params.put("source", getSource());
			String result = requestGet(getUrl(), params);
			if (result != null) {
				ret = Integer.parseInt(result);
			} else {
				ret = -99;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		if (ret > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String getSmsURL() {
		return DEFAULT_SMSURL;
	}
	
	public String getToken() {
		return token == null ? getPropsProvider().props().getProperty(SMS_TOKEN_KEY) : token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsgtype() {
		return msgtype == null ? getPropsProvider().props().getProperty(SMS_MSGTYPE_KEY,"1") : msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getSource() {
		return source == null ? getPropsProvider().props().getProperty(SMS_SOURCE_KEY) : source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
