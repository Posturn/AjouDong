package com.example.ajoudongfe;

import java.util.ArrayList;
import java.util.List;

public class VerifyObject {
    int templateSid;
    private List<RecipientForRequest> recipients;

    public VerifyObject(int templateSid, List<com.example.ajoudongfe.RecipientForRequest> recipients) {
        this.templateSid = templateSid;
        this.recipients = recipients;
    }

    public int getTemplateSid() {
        return templateSid;
    }
    public void setTemplateSid(int templateSid) {
        this.templateSid = templateSid;
    }
    public List<RecipientForRequest> getRecipients() {
        return recipients;
    }
    public void setRecipients(List<RecipientForRequest> recipients) {
        this.recipients = recipients;
    }
}
