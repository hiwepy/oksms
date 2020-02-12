package com.github.hiwepy.oksms.core.provider.def;

import java.util.Properties;

import com.github.hiwepy.oksms.core.provider.SmsPropertiesProvider;

public class DefaultSmsPropertiesProvider implements SmsPropertiesProvider {

	/**
	 * 基于配置文件的默认配置
	 */
	protected Properties defaultProps = new Properties();
	protected Properties props = new Properties();
	
	@Override
	public Properties props() {
		props.clear();
		props.putAll(defaultProps);
		return props;
	}

	public Properties getProps() {
		return props;
	}

	@Override
	public void setProps(Properties props) {
		this.defaultProps = props;
	}

	public Properties getDefaultProps() {
		//系统配置参数
		defaultProps.putAll(System.getProperties());
		return defaultProps;
	}

	public void setDefaultProps(Properties defaultProps) {
		this.defaultProps = defaultProps;
	}
	
}
