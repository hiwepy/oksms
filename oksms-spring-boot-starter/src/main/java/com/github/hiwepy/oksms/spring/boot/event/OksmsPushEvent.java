package com.github.hiwepy.oksms.spring.boot.event;

import org.springframework.context.ApplicationEvent;

import com.github.hiwepy.oksms.core.OksmsPayload;

@SuppressWarnings("serial")
public class OksmsPushEvent extends ApplicationEvent {

	/**
	 * 当前事件绑定的数据对象
	 */
	protected OksmsPayload payload;

	public OksmsPushEvent(Object source, OksmsPayload payload) {
		super(source);
		this.payload = payload;
	}

	public OksmsPayload getPayload() {
		return payload;
	}
	
}
