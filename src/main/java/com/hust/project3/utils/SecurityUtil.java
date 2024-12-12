package com.hust.project3.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
