package com.floo.payment_service.model;

import lombok.Data;

@Data
public class Email {
    private String to;
    private String subject;
    private String text;
}
