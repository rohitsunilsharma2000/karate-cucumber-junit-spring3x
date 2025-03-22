package com.example.karate_cucumber_junit_spring3x.opd;

import lombok.ToString;

import java.util.Date;
@ToString

public class Notification {
    private long notificationId;
    private String  message;
    private Date sentDate;
}
