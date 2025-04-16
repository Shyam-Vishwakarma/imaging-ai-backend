package com.imaging.app.security;

import com.imaging.app.exception.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    public static String getAuthenticatedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static void verifyUserAccess(String userId) {
        String authorizedUser = getAuthenticatedUsername();
        if (!authorizedUser.equals(userId)) {
            throw new AccessDeniedException(
                    "Provide userId is different from authorized in userId");
        }
        ;
    }
}
