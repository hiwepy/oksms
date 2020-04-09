package com.github.hiwepy.oksms.extensions.aliyun;

import java.util.function.Function;

import org.pf4j.Extension;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.OksmsPayload;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;
import com.github.hiwepy.oksms.core.exception.OksmsException;

/**
 * 	腾讯云短信
 * https://cloud.tencent.com/document/product/382/38778
 */
@Extension
@OksmsExtension(name = "cnsms", method = "GET", protocol = "http")
public class OksmsClientForAliyun extends OksmsClientPoint {
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String DEFAULT_SMSURL = "sms.tencentcloudapi.com";
	
	public OksmsClientForAliyun(){
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
			
			DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<accessKeyId>", "<accessSecret>");
	        IAcsClient client = new DefaultAcsClient(profile);

	        CommonRequest request = new CommonRequest();
	        request.setMethod(MethodType.POST);
	        request.setDomain("dysmsapi.aliyuncs.com");
	        request.setVersion("2017-05-25");
	        request.setAction("QuerySendDetails");
	        request.putQueryParameter("RegionId", "cn-hangzhou");
            
	        CommonResponse response = client.getCommonResponse(request);
	        
	        System.err.println(response.getHttpStatus());
            System.out.println(response.getData());
            
            callback.apply(response.getData());
	            
		} catch (Exception e) {
			throw new OksmsException(e);
		}
	}
	
	@Override
	public String getSmsURL() {
		return DEFAULT_SMSURL;
	}
	
}
