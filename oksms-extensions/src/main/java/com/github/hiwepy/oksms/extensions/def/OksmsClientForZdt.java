package com.github.hiwepy.oksms.extensions.def;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.function.Function;

import org.pf4j.Extension;

import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.exception.OksmsException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Extension
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
	
	@Override
	public void send(OksmsPayload payload, final Function<String, Boolean> callback) throws OksmsException {
		
		try {

			// 1.创建RequestBody对象
			RequestBody formBody = new FormBody.Builder()
		            .add("username", getUid())
		 			.add("password", getPwd())
		 			.add("to", payload.getMobile()) // 短信接收号码，多个号码（用英文逗号隔开）视为一次群发操作
		 			.add("text", URLEncoder.encode(payload.getContent(), "GBK"))
		 			.add("msgtype", getMsgtype()) // 发送类型 1:普通短信 2:长短信(超过64字符)
		 			.add("source", getSource())
		            .build();
			// 2.创建Request对象，设置一个url地址,设置请求方式。
			Request request = new Request.Builder()
		             .url(getUrl())
		             .post(formBody)
		             .build();
			// 3.创建一个call对象,参数就是Request请求对象
		    Call call = okHttpClient.newCall(request);
		    // 4.请求加入调度，重写回调方法
	        call.enqueue(new Callback() {
	            // 请求失败执行的方法
	            @Override
	            public void onFailure(Call call, IOException e) {
	            	throw new OksmsException(e);
	            }
	            // 请求成功执行的方法
	            @Override
	            public void onResponse(Call call, Response response) throws IOException {
	            	if(response.isSuccessful()) {
	            		String data = response.body().string();
		                int ret = 99;
		                if (data != null) {
		    				ret = Integer.parseInt(data);
		    			} else {
		    				ret = -99;
		    			}
		    			if (ret > 0) {
		    				callback.apply(data);
		    			}
	            	}
	            }
	        });
		} catch (Exception e) {
			throw new OksmsException(e);
		}
	}
	
	@Override
	public String getSmsURL() {
		return DEFAULT_SMSURL;
	}
	
	public String getToken() {
		return token == null ? getProperties().getProperty(SMS_TOKEN_KEY) : token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsgtype() {
		return msgtype == null ? getProperties().getProperty(SMS_MSGTYPE_KEY,"1") : msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getSource() {
		return source == null ? getProperties().getProperty(SMS_SOURCE_KEY) : source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
