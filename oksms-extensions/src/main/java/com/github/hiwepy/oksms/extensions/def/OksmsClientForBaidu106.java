package com.github.hiwepy.oksms.extensions.def;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.exception.PluginInvokeException;
import com.github.hiwepy.oksms.core.provider.SmsPropertiesProvider;

/**
 *  106短信验证接口（http://apistore.baidu.com/apiworks/servicedetail/1018.html）
 */
@OksmsExtension(name = "baidu-106", method = "GET", protocol = "http")
public class OksmsClientForBaidu106 extends OksmsClientPoint {
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String DEFAULT_SMSURL = "http://apis.baidu.com/kingtto_media/106sms/106sms";
	
	public OksmsClientForBaidu106(){
		super();
	}
	
	public OksmsClientForBaidu106(SmsPropertiesProvider propsProvider){
		super(propsProvider);
	}
	
	
	@Override
	protected void onPreHandle(HttpURLConnection conn) throws Exception {
		super.onPreHandle(conn);
        // 填入apikey到HTTP header
		//conn.setRequestProperty("apikey",  "您自己的apikey");
		conn.setRequestProperty("apikey",  getUid());
		conn.setRequestMethod("GET");
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
	public Object send(OksmsPayload payload) throws PluginInvokeException {
		try {
			Map<String, String> params = new HashMap<String, String>();
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
