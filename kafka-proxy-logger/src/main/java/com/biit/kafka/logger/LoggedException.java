package com.biit.kafka.logger;

/*-
 * #%L
 * Kafka Proxy (Logger)
 * %%
 * Copyright (C) 2023 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
