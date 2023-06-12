package com.biit.kafka.logger;


import org.springframework.http.HttpStatus;

public class LoggedException extends RuntimeException {
    private HttpStatus status;

    protected LoggedException(Class<?> clazz, String message, ExceptionType type, HttpStatus status) {
        super(message);
        this.status = status;
        final String className = clazz.getName();
        switch (type) {
            case INFO:
                KafkaProxyLogger.info(className, message);
                break;
            case WARNING:
                KafkaProxyLogger.warning(className, message);
                break;
            case SEVERE:
                KafkaProxyLogger.severe(className, message);
                break;
            default:
                KafkaProxyLogger.debug(className, message);
                break;
        }
    }

    protected LoggedException(Class<?> clazz, Throwable e, HttpStatus status) {
        this(clazz, e);
        this.status = status;
    }

    protected LoggedException(Class<?> clazz, Throwable e) {
        super(e);
        KafkaProxyLogger.errorMessage(clazz, e);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
