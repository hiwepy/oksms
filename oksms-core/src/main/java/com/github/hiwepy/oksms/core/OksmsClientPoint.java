package com.github.hiwepy.oksms.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.pf4j.ExtensionPoint;

import okhttp3.OkHttpClient;

public abstract class OksmsClientPoint implements OksmsClient, ExtensionPoint {

	/**
	 * SMS服务商调用地址
	 */ 
	protected String url;
	/**
	 * SMS服务商提供的uid
	 */ 
	protected String uid;
	/**
	 * SMS服务商提供的pwd
	 */ 
	protected String pwd;
	
	protected OkHttpClient okHttpClient;
	
	protected Properties properties;
	
	public OksmsClientPoint(){}
	
	@Override
	public void initialize(Properties properties) {
		this.properties = properties;
		// 默认连接超时时间，默认：5000（单位：毫秒）
		int connectTimeout = Integer.parseInt(properties.getProperty(HTTP_CONNECTTIMEOUT, "5000"));
		// 默认读取超时时间，默认：3000（单位：毫秒）
		int readTimeout = Integer.parseInt(properties.getProperty(HTTP_READTIMEOUT, "3000"));
		// 请求编码，默认：UTF-8
		// String charset = properties.getProperty(HTTP_CHARSET, "UTF-8");
		if(okHttpClient == null) {
			// 1.创建OkHttpClient对象
			okHttpClient = new OkHttpClient().newBuilder()
					.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
					//.hostnameVerifier(okhttpHostnameVerifier)
					//.followRedirects(properties.isFollowRedirects())
					//.followSslRedirects(properties.isFollowSslRedirects())
					.pingInterval(1, TimeUnit.MILLISECONDS)
					.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
					.retryOnConnectionFailure(true)
					//.sslSocketFactory(trustedSSLSocketFactory, trustManager)
					.writeTimeout(3, TimeUnit.SECONDS)
					// Application Interceptors、Network Interceptors : https://segmentfault.com/a/1190000013164260
					//.addNetworkInterceptor(loggingInterceptor)
					//.addInterceptor(headerInterceptor)
					.build();
		}
	}
	
	protected String getGetHttpURL(String httpUrl,Map<String, String> httpArg) {
		StringBuffer paramsBuffer = new StringBuffer();
		if (httpArg != null && httpArg.size() > 0) {
			Set<String> set = httpArg.keySet();
			Iterator<String> it = set.iterator();
			int count = 0;
			while (it.hasNext()) {
				String key = it.next();
				String value = httpArg.get(key);
				if (count > 0) {
					paramsBuffer.append("&");
				}
				//内容转码由调用者实现
				//paramsBuffer.append(key + "=" + URLEncoder.encode(value, "UTF-8"));
				paramsBuffer.append(key + "=" + value);
				count++;
			}
		}
		String params = paramsBuffer.toString();
		if (params != null && !"".equals(params.trim())) {
			if (httpUrl.indexOf("?") == -1) {
				httpUrl = httpUrl + "?" + params;
			} else if (httpUrl.indexOf("?") == httpUrl.length() - 1) {
				httpUrl = httpUrl + params;
			} else {
				httpUrl = httpUrl + "&" + params;
			}
		}
		return httpUrl;
	}
	
	public void setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	public abstract String getSmsURL();
	
	public String getUrl() {
		String tmp_url = getProperties().getProperty(SMS_URL_KEY) ;
		return tmp_url == null ? ( url == null ? getSmsURL() : url ) : tmp_url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUid() {
		return uid == null ? getProperties().getProperty(SMS_UID_KEY) : uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd == null ? getProperties().getProperty(SMS_PWD_KEY) : pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
}
