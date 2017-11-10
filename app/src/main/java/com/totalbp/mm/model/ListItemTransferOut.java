package com.totalbp.mm.model;

/**
 * Created by Ezra
 */

public class ListItemTransferOut {

    public ListItemTransferOut() {
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKode_Transfer() {
        return Kode_Transfer;
    }

    public void setKode_Transfer(String kode_Transfer) {
        Kode_Transfer = kode_Transfer;
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

    public String getFromQSPV() {
        return FromQSPV;
    }

    public void setFromQSPV(String fromQSPV) {
        FromQSPV = fromQSPV;
    }

    public String getFromKode_Tower() {
        return FromKode_Tower;
    }

    public void setFromKode_Tower(String fromKode_Tower) {
        FromKode_Tower = fromKode_Tower;
    }

    public String getFromKode_Lantai() {
        return FromKode_Lantai;
    }

    public void setFromKode_Lantai(String fromKode_Lantai) {
        FromKode_Lantai = fromKode_Lantai;
    }

    public String getFromKode_Zona() {
        return FromKode_Zona;
    }

    public void setFromKode_Zona(String fromKode_Zona) {
        FromKode_Zona = fromKode_Zona;
    }

    public String getToQSPV() {
        return ToQSPV;
    }

    public void setToQSPV(String toQSPV) {
        ToQSPV = toQSPV;
    }

    public String getToKode_Tower() {
        return ToKode_Tower;
    }

    public void setToKode_Tower(String toKode_Tower) {
        ToKode_Tower = toKode_Tower;
    }

    public String getToKode_Lantai() {
        return ToKode_Lantai;
    }

    public void setToKode_Lantai(String toKode_Lantai) {
        ToKode_Lantai = toKode_Lantai;
    }

    public String getToKode_Zona() {
        return ToKode_Zona;
    }

    public void setToKode_Zona(String toKode_Zona) {
        ToKode_Zona = toKode_Zona;
    }

    public String getCourierPhoto() {
        return CourierPhoto;
    }

    public void setCourierPhoto(String courierPhoto) {
        CourierPhoto = courierPhoto;
    }

    public String getStatusRequest() {
        return StatusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        StatusRequest = statusRequest;
    }

    public String getStatusAktif() {
        return StatusAktif;
    }

    public void setStatusAktif(String statusAktif) {
        StatusAktif = statusAktif;
    }

    public String getApprovalNo() {
        return ApprovalNo;
    }

    public void setApprovalNo(String approvalNo) {
        ApprovalNo = approvalNo;
    }

    public String getPembuat() {
        return Pembuat;
    }

    public void setPembuat(String pembuat) {
        Pembuat = pembuat;
    }

    public String getWaktuBuat() {
        return WaktuBuat;
    }

    public void setWaktuBuat(String waktuBuat) {
        WaktuBuat = waktuBuat;
    }

    public String getPengubah() {
        return Pengubah;
    }

    public void setPengubah(String pengubah) {
        Pengubah = pengubah;
    }

    public String getWaktuUbah() {
        return WaktuUbah;
    }

    public void setWaktuUbah(String waktuUbah) {
        WaktuUbah = waktuUbah;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getFlagReturnGudang() {
        return FlagReturnGudang;
    }

    public void setFlagReturnGudang(String flagReturnGudang) {
        FlagReturnGudang = flagReturnGudang;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public String getNama_Tower() {
        return Nama_Tower;
    }

    public void setNama_Tower(String nama_Tower) {
        Nama_Tower = nama_Tower;
    }

    public String getNama_Lantai() {
        return Nama_Lantai;
    }

    public void setNama_Lantai(String nama_Lantai) {
        Nama_Lantai = nama_Lantai;
    }

    public String getNama_Zona() {
        return Nama_Zona;
    }

    public void setNama_Zona(String nama_Zona) {
        Nama_Zona = nama_Zona;
    }

    public String ID;
    public String Kode_Transfer;
    public String Kode_Proyek;
    public String Kode_Item;
    public String Volume;
    public String Unit;
    public String FromQSPV;
    public String FromKode_Tower;
    public String FromKode_Lantai;
    public String FromKode_Zona;
    public String ToQSPV;
    public String ToKode_Tower;
    public String ToKode_Lantai;
    public String ToKode_Zona;
    public String CourierPhoto;
    public String StatusRequest;
    public String StatusAktif;
    public String ApprovalNo;
    public String Pembuat;
    public String WaktuBuat;
    public String Pengubah;
    public String WaktuUbah;
    public String Comment;
    public String FlagReturnGudang;
    public String Deskripsi;
    public String Nama_Tower;
    public String Nama_Lantai;
    public String Nama_Zona;

    public ListItemTransferOut(String ID, String kode_Transfer, String kode_Proyek, String kode_Item, String volume, String unit, String fromQSPV, String fromKode_Tower, String fromKode_Lantai, String fromKode_Zona, String toQSPV, String toKode_Tower, String toKode_Lantai, String toKode_Zona, String courierPhoto, String statusRequest, String statusAktif, String approvalNo, String pembuat, String waktuBuat, String pengubah, String waktuUbah, String comment, String flagReturnGudang, String deskripsi, String nama_Tower, String nama_Lantai, String nama_Zona) {
        this.ID = ID;
        Kode_Transfer = kode_Transfer;
        Kode_Proyek = kode_Proyek;
        Kode_Item = kode_Item;
        Volume = volume;
        Unit = unit;
        FromQSPV = fromQSPV;
        FromKode_Tower = fromKode_Tower;
        FromKode_Lantai = fromKode_Lantai;
        FromKode_Zona = fromKode_Zona;
        ToQSPV = toQSPV;
        ToKode_Tower = toKode_Tower;
        ToKode_Lantai = toKode_Lantai;
        ToKode_Zona = toKode_Zona;
        CourierPhoto = courierPhoto;
        StatusRequest = statusRequest;
        StatusAktif = statusAktif;
        ApprovalNo = approvalNo;
        Pembuat = pembuat;
        WaktuBuat = waktuBuat;
        Pengubah = pengubah;
        WaktuUbah = waktuUbah;
        Comment = comment;
        FlagReturnGudang = flagReturnGudang;
        Deskripsi = deskripsi;
        Nama_Tower = nama_Tower;
        Nama_Lantai = nama_Lantai;
        Nama_Zona = nama_Zona;
    }
}

