package com.github.hiwepy.oksms.extensions.def;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.exception.PluginInvokeException;
import com.github.hiwepy.oksms.core.provider.SmsPropertiesProvider;

/**
 * 企业短信通 JAVA HTTP接口 发送短信
 */
@OksmsExtension(name = "cnsms", method = "GET", protocol = "http")
public class OksmsClientForCn extends OksmsClientPoint {
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String DEFAULT_SMSURL = "http://api.cnsms.cn/?ac=send";
	
	public OksmsClientForCn(){
		super();
	}
	
	public OksmsClientForCn(SmsPropertiesProvider propsProvider){
		super(propsProvider);
	}
	
	@Override
	protected void onPreHandle(HttpURLConnection conn) throws Exception {
		super.onPreHandle(conn);
		conn.setRequestMethod("GET");
	}
	
	/**
	 *  企业短信通 JAVA HTTP接口 发送短信
	 *  
	 *  http://api.cnsms.cn/?ac=send&uid=账号&pwd=MD5位32密码&mobile=号码&content=内容
	 *  
	 *  返回状态 :
	 *  -------------------------------------------------------------------------------------------------------
	 *	100 发送成功
	 *	101 验证失败
	 *	102 短信不足
	 * 	103 操作失败
	 *	104 非法字符
	 *	105 内容过多
	 *	106 号码过多
	 *	107 频率过快
	 *	108 号码内容空
	 *	109 账号冻结
	 *	110 禁止频繁单条发送
	 *	111 系统暂定发送
	 *	112 号码不正确
	 *	120 系统升级
	 */
	@Override
	public Object send(OksmsPayload payload) throws PluginInvokeException {
		try {
			Map<String, String> params = new HashMap<String, String>();
			MessageDigest dist = MessageDigest.getInstance("MD5");
			params.put("uid", getUid());
			params.put("pwd", new String( dist.digest(getPwd().getBytes())) );
			params.put("mobile", payload.getMobile());
			params.put("content", URLEncoder.encode(payload.getContent(), "UTF-8"));
			String result = requestGet(getUrl(), params);
			if (result != null) {
				JSONObject jsonObject = JSONObject.parseObject(result);
				if("Success".equalsIgnoreCase(jsonObject.getString("returnstatus"))){
					return true;
				}else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public String getSmsURL() {
		return DEFAULT_SMSURL;
	}
	
}
