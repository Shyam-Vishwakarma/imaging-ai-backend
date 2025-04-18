package com.imaging.app.security;

import com.imaging.app.exception.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    public static String getAuthenticatedUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static void verifyUserAccess(String userId) {
        String authorizedUser = getAuthenticatedUserId();
        if (!authorizedUser.equals(userId)) {
            throw new AccessDeniedException(
                    "Provide userId is different from authorized in userId");
        }
    }
}
