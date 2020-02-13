package com.github.hiwepy.oksms.extensions.def;

import java.io.IOException;
import java.net.URLEncoder;
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
 *  106短信验证接口（http://apistore.baidu.com/apiworks/servicedetail/1018.html）
 */
@Extension
@OksmsExtension(name = "baidu-106", method = "GET", protocol = "http")
public class OksmsClientForBaidu106 extends OksmsClientPoint {
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String DEFAULT_SMSURL = "http://apis.baidu.com/kingtto_media/106sms/106sms";
	
	public OksmsClientForBaidu106(){
		super();
	}

	/**
	 *  JSON返回示例 :
	 *  -------------------------------------------------------------------------------------------------------
	 *	{
	 *		"returnstatus": "Success",---------- 返回状态值：成功返回Success 失败返回：Faild
	 *		"message": "ok",---------- 返回信息
	 *		"remainpoint": "0",---------- 运营商结算无意义，可不用解析
	 *		"taskID": "123456",---------- 返回本次任务的序列ID
	 *		"successCounts": "1"---------- 返回成功短信数     
	 *	}
	 *	
	 *	XML 返回示例：
	 *	-------------------------------------------------------------------------------------------------------
	 *	<?xml version="1.0" encoding="utf-8" ?>
	 *	<returnsms>
	 *		<returnstatus>status</returnstatus>---------- 返回状态值：成功返回Success 失败返回：Faild
	 *		<message>message</message>---------- 返回信息
	 *		<remainpoint> remainpoint</remainpoint>---------- 运营商结算无意义，可不用解析
	 *		<taskID>taskID</taskID>---------- 返回本次任务的序列ID
	 *		<successCounts>successCounts</successCounts>---------- 返回成功短信数
	 *	</returnsms>
	 *
	 */
	@Override
	public void send(OksmsPayload payload, final Function<String, Boolean> callback) throws OksmsException {
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", payload.getMobile());
			params.put("content", URLEncoder.encode(payload.getContent(), "UTF-8"));

			// 1.创建Request对象，设置一个url地址,设置请求方式。
			Request.Builder builder = new Request.Builder()
					.url(this.getGetHttpURL(this.getUrl(), params))
		            .get();
			// 填入apikey到 HTTP header
			builder.addHeader("apikey", getUid());
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
