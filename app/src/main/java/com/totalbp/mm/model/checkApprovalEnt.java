package com.totalbp.mm.model;

/**
 * Created by Ezra.R on 20/10/2017.
 */
public class checkApprovalEnt {

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getKodeProyek() {
        return KodeProyek;
    }

    public void setKodeProyek(String kodeProyek) {
        KodeProyek = kodeProyek;
    }

    public String getKodezona() {
        return kodezona;
    }

    public void setKodezona(String kodezona) {
        this.kodezona = kodezona;
    }



    public String formID;
    public String Userid;
    public String KodeProyek;
    public String kodezona;

    public String getAppprovalNo() {
        return AppprovalNo;
    }

    public void setAppprovalNo(String appprovalNo) {
        AppprovalNo = appprovalNo;
    }

    public String AppprovalNo;

    public checkApprovalEnt(String formID, String userid, String kodeProyek, String kodezona, String AppprovalNo) {
        this.formID = formID;
        Userid = userid;
        KodeProyek = kodeProyek;
        this.kodezona = kodezona;
        this.AppprovalNo = AppprovalNo;
    }

    public checkApprovalEnt() {
    }

}

