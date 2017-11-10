package com.totalbp.mm.model;

/**
 * Created by Ezra
 */

public class CommonRequestMoFormEnt {


    public CommonRequestMoFormEnt(String ID, String kode_MO, String kode_Proyek, String kode_Item, String volume, String unit, String work_Code, String kode_Tower, String kode_Lantai, String kode_Zona, String courierPhoto, String refHeaderSPR, String refDetailSPR, String statusRequest, String approvalNo, String statusAktif, String pembuat, String Tanggal_Dibuat, String pengubah, String Tanggal_Diubah, String comment, String TokenID, String Nama_Pembuat, String Nama_Pengubah, String ModeReq, String FormId, String VolumeIdle) {
        this.ID = ID;
        Kode_MO = kode_MO;
        Kode_Proyek = kode_Proyek;
        Kode_Item = kode_Item;
        Volume = volume;
        Unit = unit;
        Work_Code = work_Code;
        Kode_Tower = kode_Tower;
        Kode_Lantai = kode_Lantai;
        Kode_Zona = kode_Zona;
        CourierPhoto = courierPhoto;
        RefHeaderSPR = refHeaderSPR;
        RefDetailSPR = refDetailSPR;
        StatusRequest = statusRequest;
        ApprovalNo = approvalNo;
        StatusAktif = statusAktif;
        Pembuat = pembuat;
        this.Tanggal_Dibuat = Tanggal_Dibuat;
        Pengubah = pengubah;
        this.Tanggal_Diubah = Tanggal_Diubah;
        Comment = comment;
        this.TokenID = TokenID;
        this.Nama_Pembuat = Nama_Pembuat;
        this.Nama_Pengubah = Nama_Pengubah;
        this.ModeReq = ModeReq;
        this.FormId = FormId;
        this.VolumeIdle = VolumeIdle;
    }

    public String ID;
    public String Kode_MO;
    public String Kode_Proyek;
    public String Kode_Item;
    public String Volume;
    public String Unit;
    public String Work_Code;
    public String Kode_Tower;
    public String Kode_Lantai;
    public String Kode_Zona;
    public String CourierPhoto;
    public String RefHeaderSPR;
    public String RefDetailSPR;
    public String StatusRequest;
    public String ApprovalNo;
    public String StatusAktif;
    public String Pembuat;
    public String Tanggal_Dibuat;
    public String Pengubah;
    public String Tanggal_Diubah;
    public String Comment;


    public String getVolumeIdle() {
        return VolumeIdle;
    }

    public void setVolumeIdle(String volumeIdle) {
        VolumeIdle = volumeIdle;
    }

    public String VolumeIdle;

    public String getNama_Pembuat() {
        return Nama_Pembuat;
    }

    public void setNama_Pembuat(String nama_Pembuat) {
        Nama_Pembuat = nama_Pembuat;
    }

    public String getNama_Pengubah() {
        return Nama_Pengubah;
    }

    public void setNama_Pengubah(String nama_Pengubah) {
        Nama_Pengubah = nama_Pengubah;
    }

    public String Nama_Pembuat;
    public String Nama_Pengubah;

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String TokenID;

    public String getModeReq() {
        return ModeReq;
    }

    public void setModeReq(String modeReq) {
        ModeReq = modeReq;
    }

    public String getFormId() {
        return FormId;
    }

    public void setFormId(String formId) {
        FormId = formId;
    }

    public String ModeReq;

    public String FormId;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKode_MO() {
        return Kode_MO;
    }

    public void setKode_MO(String kode_MO) {
        Kode_MO = kode_MO;
    }

    public String getKode_Proyek() {
        return Kode_Proyek;
    }

    public void setKode_Proyek(String kode_Proyek) {
        Kode_Proyek = kode_Proyek;
    }

    public String getKode_Item() {
        return Kode_Item;
    }

    public void setKode_Item(String kode_Item) {
        Kode_Item = kode_Item;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getWork_Code() {
        return Work_Code;
    }

    public void setWork_Code(String work_Code) {
        Work_Code = work_Code;
    }

    public String getKode_Tower() {
        return Kode_Tower;
    }

    public void setKode_Tower(String kode_Tower) {
        Kode_Tower = kode_Tower;
    }

    public String getKode_Lantai() {
        return Kode_Lantai;
    }

    public void setKode_Lantai(String kode_Lantai) {
        Kode_Lantai = kode_Lantai;
    }

    public String getKode_Zona() {
        return Kode_Zona;
    }

    public void setKode_Zona(String kode_Zona) {
        Kode_Zona = kode_Zona;
    }

    public String getCourierPhoto() {
        return CourierPhoto;
    }

    public void setCourierPhoto(String courierPhoto) {
        CourierPhoto = courierPhoto;
    }

    public String getRefHeaderSPR() {
        return RefHeaderSPR;
    }

    public void setRefHeaderSPR(String refHeaderSPR) {
        RefHeaderSPR = refHeaderSPR;
    }

    public String getRefDetailSPR() {
        return RefDetailSPR;
    }

    public void setRefDetailSPR(String refDetailSPR) {
        RefDetailSPR = refDetailSPR;
    }

    public String getStatusRequest() {
        return StatusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        StatusRequest = statusRequest;
    }

    public String getApprovalNo() {
        return ApprovalNo;
    }

    public void setApprovalNo(String approvalNo) {
        ApprovalNo = approvalNo;
    }

    public String getStatusAktif() {
        return StatusAktif;
    }

    public void setStatusAktif(String statusAktif) {
        StatusAktif = statusAktif;
    }

    public String getPembuat() {
        return Pembuat;
    }

    public void setPembuat(String pembuat) {
        Pembuat = pembuat;
    }

    public String getWaktuBuat() {
        return Tanggal_Dibuat;
    }

    public void setWaktuBuat(String waktuBuat) {
        Tanggal_Dibuat = waktuBuat;
    }

    public String getPengubah() {
        return Pengubah;
    }

    public void setPengubah(String pengubah) {
        Pengubah = pengubah;
    }

    public String getWaktuUbah() {
        return Tanggal_Diubah;
    }

    public void setWaktuUbah(String waktuUbah) {
        Tanggal_Diubah = waktuUbah;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public CommonRequestMoFormEnt() {
    }

}

