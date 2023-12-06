package com.haydenhuffman.soundoffbandtracker.util;

import com.haydenhuffman.soundoffbandtracker.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static Boolean isUserIdMatch(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();
            return currentUser.getUserId().equals(userId);
        }
        return false;
    }

}
