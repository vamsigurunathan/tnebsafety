package com.example.tnebsafety;

import com.google.gson.annotations.SerializedName;

public class responseoutput {
    @SerializedName("userid")
    private String userid;
    @SerializedName("username")
    private String username;
    @SerializedName("userrole")
    private String userrole;
    @SerializedName("secname")
    private String usersection;
    @SerializedName("cirname")
    private String usercircle;
    @SerializedName("usermaint")
    private String usermaintvalue;
    public String getUserid() {

        return userid;
    }
    public String getUsermaintvalue() {

        return usermaintvalue;
    }
    public String getUsername() {

        return username;
    }
    public String getUserrole() {

        return userrole;
    }
    public String getCirclename() {

        return usercircle;
    }
    public String getSectionname() {

        return usersection;
    }
}
