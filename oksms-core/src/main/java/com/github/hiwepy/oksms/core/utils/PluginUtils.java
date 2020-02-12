package com.github.hiwepy.oksms.core.utils;

import java.util.List;

import org.pf4j.PluginManager;
import org.pf4j.util.StringUtils;

import com.github.hiwepy.oksms.core.OksmsClientPoint;
import com.github.hiwepy.oksms.core.annotation.OksmsExtension;

public class PluginUtils {

	public static <T> T getExtensionPoint(PluginManager pluginManager, Class<T> type, String pluginId, String extensionId) {
		if (StringUtils.isNotNullOrEmpty(pluginId) && StringUtils.isNotNullOrEmpty(extensionId)) {
			List<T> extensions = pluginManager.getExtensions(type, pluginId);
			for (T extension : extensions) {
				OksmsExtension em = extension.getClass().getAnnotation(OksmsExtension.class);
				if(StringUtils.isNotNullOrEmpty(em.name()) && em.name().equals(extensionId)) {
					return extension;
				}
			}
		}
		return null;
	}
	
	public static OksmsClientPoint getPluginInvocationLifecyclePoint(PluginManager pluginManager, String pluginId, String extensionId) {
		return getExtensionPoint(pluginManager, OksmsClientPoint.class, pluginId, extensionId);
	}
	
}
