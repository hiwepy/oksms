package com.github.hiwepy.oksms.extensions.def;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.pf4j.Extension;

import com.alibaba.fastjson.JSONObject;
import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.exception.OksmsException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 	腾讯云短信
 * https://cloud.tencent.com/document/product/382/38778
 */
@Extension
@OksmsExtension(name = "cnsms", method = "GET", protocol = "http")
public class OksmsClientForTencent extends OksmsClientPoint {
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String DEFAULT_SMSURL = "sms.tencentcloudapi.com";
	
	public OksmsClientForTencent(){
		super();
	}
	
	/**
	 *  企业短信通 JAVA HTTP接口 发送短信
	 *  
	 *  https://sms.tencentcloudapi.com/?Action=SendSms
	 *  &PhoneNumberSet.0=+8618511122233
	 *  &PhoneNumberSet.1=+8618511122266
	 *  &TemplateID=1234
	 *  &Sign=腾讯云
	 *  &TemplateParamSet.0=12345
	 *  &SmsSdkAppid=1400006666
	 *  &SessionContext=test
	 *  &<公共请求参数>
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
	public void send(OksmsPayload payload, final Function<String, Boolean> callback) throws OksmsException {
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			
			MessageDigest dist = MessageDigest.getInstance("MD5");
			
			params.put("uid", getUid());
			params.put("pwd", new String( dist.digest(getPwd().getBytes())) );
			params.put("mobile", payload.getMobile());
			params.put("content", URLEncoder.encode(payload.getContent(), "UTF-8"));
			
			// 1.创建Request对象，设置一个url地址,设置请求方式。
			Request.Builder builder = new Request.Builder()
					.url(this.getGetHttpURL(this.getUrl(), params))
		            .get();
			for (Entry<String, String> entry : payload.getHeader().entrySet()) {
				builder.addHeader(entry.getKey(), entry.getValue());
			}
			// 2.创建一个call对象,参数就是Request请求对象
		    Call call = okHttpClient.newCall(builder.build());
		    // 3.请求加入调度，重写回调方法
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
		                if (data != null) {
		                	callback.apply(data);
		                	JSONObject jsonObject = JSONObject.parseObject(data);
		    				if("Success".equalsIgnoreCase(jsonObject.getString("returnstatus"))){
		    					
		    				}
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
	
}
