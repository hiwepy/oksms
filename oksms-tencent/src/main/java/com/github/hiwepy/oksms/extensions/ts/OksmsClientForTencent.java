package com.github.hiwepy.oksms.extensions.ts;

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
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
// 导入对应产品模块的client
import com.tencentcloudapi.cvm.v20170312.CvmClient;
// 导入要请求接口对应的request response类
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
/**
 * 	腾讯云短信
 * https://cloud.tencent.com/document/product/382/38778
 * https://github.com/TencentCloud/tencentcloud-sdk-java
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
			
			// 实例化一个认证对象，入参需要传入腾讯云账户 secretId，secretKey
            Credential cred = new Credential("secretId", "secretKey");
            
            // 实例化要请求产品(以cvm为例)的client对象
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
            clientProfile.setLanguage(Language.ZH_CN);

           /* HttpProfile httpProfile = new HttpProfile();
            httpProfile.setProxyHost("真实代理ip");
            httpProfile.setProxyHost("真实代理ip");
            httpProfile.setProxyHost("真实代理ip");
            httpProfile.setProxyPort("真实代理端口");*/
            
            CvmClient client = new CvmClient(cred, "ap-guangzhou", clientProfile);
            
            // 实例化一个请求对象
            DescribeZonesRequest req = new DescribeZonesRequest();
            
            // 通过client对象调用想要访问的接口，需要传入请求对象
            DescribeZonesResponse resp = client.DescribeZones(req);
            
            // 输出json格式的字符串回包
            System.out.println(DescribeZonesRequest.toJsonString(resp));
            
            
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
