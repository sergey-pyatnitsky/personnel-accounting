package com.personnel_accounting.email;

public interface EmailService {
    void sendSimpleEmail(String toAddress, String subject, String message);
}
