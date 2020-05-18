package com.example.ajoudongfe;

import java.util.ArrayList;
import java.util.List;

public class VerifyObject {
    int templateSid;
    private List<Object> recipents;

    public void appendRecipents(String address, List<String> parameter)
    {
        recipents.add(address);
        recipents.add(parameter);
    }

    public List<String> appendParameter(final String who_signup, final String verify_code)
    {
        List<String> parameter = new ArrayList<String>(){
            {
                add(who_signup);
                add(verify_code);
            }
        };
        return parameter;
    }
}
