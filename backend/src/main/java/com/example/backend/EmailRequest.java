package com.example.backend;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;

    public String getEmailcontent() {
        return emailContent;
    }

    public void setEmailcontent(String emailcontent) {
        this.emailContent = emailcontent;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}
