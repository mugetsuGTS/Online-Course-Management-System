package org.course_management_system.exception.util;

import org.course_management_system.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class ExceptionUtil {

    public static void handleDatabaseExceptions(Exception exception,
                                                Map<String, String> constraintNamesAndErrorCodes) throws RuntimeException {
        Throwable throwable = exception;
        do {
            String exceptionMessage = throwable.getMessage().toLowerCase();

            constraintNamesAndErrorCodes.forEach((constraintName, errorCode) -> {
                if (exceptionMessage.contains(constraintName)) {
                    throw new BusinessException(errorCode);
                }
            });

            throwable = throwable.getCause();
        } while (throwable != null);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected PersistenceException", exception);
    }
}
