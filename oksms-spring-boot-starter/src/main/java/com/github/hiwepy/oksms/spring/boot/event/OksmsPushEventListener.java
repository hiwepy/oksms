package com.github.hiwepy.oksms.spring.boot.event;

import org.springframework.context.ApplicationListener;

import com.github.hiwepy.oksms.spring.boot.OksmsTemplate;

/**
 * 短消息推送事件监听
 */
public class OksmsPushEventListener implements ApplicationListener<OksmsPushEvent> {

	protected OksmsTemplate oksmsTemplate;
	
	public OksmsPushEventListener(OksmsTemplate oksmsTemplate) {
		this.oksmsTemplate = oksmsTemplate;
	}

	@Override
	public void onApplicationEvent(OksmsPushEvent event) {
		getOksmsTemplate().send(event.getPayload());
	}
	
	public OksmsTemplate getOksmsTemplate() {
		return oksmsTemplate;
	}
	
}