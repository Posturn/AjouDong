package com.example.ajoudongfe;

import java.util.List;

public class UserListObject {
    private int response;
    private List<MemberInfoObject> content;

    public UserListObject(int response, List<MemberInfoObject> content) {
        this.response = response;
        this.content = content;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public List<MemberInfoObject> getContent() {
        return content;
    }

    public void setContent(List<MemberInfoObject> content) {
        this.content = content;
    }
}
