package com.github.hiwepy.oksms.core;

import java.util.HashMap;
import java.util.Map;

public class OksmsPayload {

	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 消息ID
	 */
	protected String uuid;
	/**
	 * 请求头
	 */
	protected Map<String, String> header = new HashMap<String, String>();
	/**
	 * 消息内容
	 */
	private String content;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
