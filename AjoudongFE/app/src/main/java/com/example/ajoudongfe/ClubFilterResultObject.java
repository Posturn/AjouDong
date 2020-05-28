package com.example.ajoudongfe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClubFilterResultObject {

    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("fields")
    @Expose
    private ClubObject fields;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public ClubObject getFields() {
        return fields;
    }

    public void setFields(ClubObject fields) {
        this.fields = fields;
    }

}