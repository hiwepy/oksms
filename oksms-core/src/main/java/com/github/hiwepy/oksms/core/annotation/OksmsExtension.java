package com.github.hiwepy.oksms.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点注解：用于标注某个功能扩展点的信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited	
public @interface OksmsExtension {
	
	String name();
	
	/**•GET •POST •HEAD •OPTIONS •PUT •DELETE •TRACE */
	String method() default "GET";
	
	String protocol() default "http";

	String ver() default "1.0.0";
	
	String desc() default "";
	
}