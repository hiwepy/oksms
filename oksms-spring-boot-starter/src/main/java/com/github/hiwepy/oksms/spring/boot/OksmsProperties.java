package com.github.hiwepy.oksms.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(OksmsProperties.PREFIX)
public class OksmsProperties {

	public static final String PREFIX = "oksms";

	/** Whether Enable Oksms. */
	private boolean enabled = false;
	
	/**
	 * 默认连接超时时间，默认：5000（单位：毫秒）
	 */
	private int connectTimeout = 5000;
	/**
	 * 默认读取超时时间，默认：3000（单位：毫秒）
	 */
    private int readTimeout = 3000;
	/**
	 * 请求编码，默认：UTF-8
	 */
	private String charset = "UTF-8";
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}