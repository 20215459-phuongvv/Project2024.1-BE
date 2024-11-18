package com.hust.project3.utils;

import java.util.ResourceBundle;

public class Constants {
    public static final String LOCAL_HOST = "http://localhost:8080";
    public static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    interface BORROW_LIMIT {
        int NORMAL_USER = 5;
        int VIP_USER = 8;
    }
}
