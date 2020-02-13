package com.github.hiwepy.oksms.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pf4j.ExtensionPoint;

import com.github.hiwepy.oksms.core.provider.SmsPropertiesProvider;

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
	
	protected SmsPropertiesProvider propsProvider;

	public OksmsClientPoint(){}
	
	public OksmsClientPoint(SmsPropertiesProvider propsProvider){
		this.propsProvider = propsProvider;
	}
	
	@Override
	public void initialize(String pluginId, String extensionId) {
		
	}

	@Override
	public void afterThrowing(String pluginId, String extensionId, Throwable cause) {
		
	}

	@Override
	public void afterReturning(String pluginId, String extensionId) {
		
	}
	
	protected void onPreHandle(HttpURLConnection conn) throws Exception{
		
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(3000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setRequestProperty("Charset", "UTF-8"); // 设置编码
		
	}
	
	protected String requestGet(String httpUrl, Map<String, String> httpArg) {
		if (httpUrl == null || "".equals(httpUrl.trim())) {
			return null;
		}
		BufferedReader reader = null;
		StringBuffer result = new StringBuffer();
		try {
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
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			this.onPreHandle(conn);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String strRead = null;
				int row = 0;
				while ((strRead = reader.readLine()) != null) {
					//多行数据，且当前行读取到数据，则在上一行之后添加换行符
					if(row > 0 && strRead != null && strRead.length() > 0){
						result.append("\r\n");
					}
					result.append(strRead);
					row ++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		return result.toString();
	}
	
	public SmsPropertiesProvider getPropsProvider() {
		return propsProvider;
	}

	public void setPropsProvider(SmsPropertiesProvider propsProvider) {
		this.propsProvider = propsProvider;
	}

	public abstract String getSmsURL();
	
	public String getUrl() {
		String tmp_url = getPropsProvider().props().getProperty(SMS_URL_KEY) ;
		return tmp_url == null ? ( url == null ? getSmsURL() : url ) : tmp_url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUid() {
		return uid == null ? getPropsProvider().props().getProperty(SMS_UID_KEY) : uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd == null ? getPropsProvider().props().getProperty(SMS_PWD_KEY) : pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
