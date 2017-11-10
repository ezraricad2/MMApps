package com.totalbp.mm.model;

/**
 * Created by Ezra
 */

public class ApprovalEnt {

    public ApprovalEnt() {
    }

    public String getNik() {
        return Pengubah;
    }

    public void setNik(String nik) {
        this.Pengubah = nik;
    }

    public String getKode_proyek() {
        return KodeProyek;
    }

    public void setKode_proyek(String kode_proyek) {
        this.KodeProyek = kode_proyek;
    }

    public String getForm_id() {
        return formID;
    }

    public void setForm_id(String form_id) {
        this.formID = form_id;
    }

    public String getReference_id() {
        return References_ID;
    }

    public void setReference_id(String reference_id) {
        this.References_ID = reference_id;
    }

    public String getMode() {
        return ModeReq;
    }

    public void setMode(String mode) {
        this.ModeReq = mode;
    }

    public String getIs_approve() {
        return Is_Approve;
    }

    public void setIs_approve(String is_approve) {
        this.Is_Approve = is_approve;
    }

    public String getApproval_number() {
        return Approval_Number;
    }

    public void setApproval_number(String approval_number) {
        this.Approval_Number = approval_number;
    }

    public String Pengubah;
    public String KodeProyek;
    public String formID;
    public String References_ID;
    public String ModeReq;
    public String Is_Approve;
    public String Approval_Number;

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String TokenID;

    public ApprovalEnt(String nik, String kode_proyek, String form_id, String reference_id, String mode, String is_approve, String approval_number, String TokenID) {
        this.Pengubah = nik;
        this.KodeProyek = kode_proyek;
        this.formID = form_id;
        this.References_ID = reference_id;
        this.ModeReq = mode;
        this.Is_Approve = is_approve;
        this.Approval_Number = approval_number;
        this.TokenID = TokenID;
    }
}

