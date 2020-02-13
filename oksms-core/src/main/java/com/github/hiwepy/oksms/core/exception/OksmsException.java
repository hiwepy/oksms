package com.github.hiwepy.oksms.core.exception;

import org.pf4j.util.StringUtils;

public class OksmsException  extends RuntimeException {

    public OksmsException() {
        super();
    }

    public OksmsException(String message) {
        super(message);
    }

    public OksmsException(Throwable cause) {
        super(cause);
    }

    public OksmsException(Throwable cause, String message, Object... args) {
        super(StringUtils.format(message, args), cause);
    }

    public OksmsException(String message, Object... args) {
        super(StringUtils.format(message, args));
    }

}
