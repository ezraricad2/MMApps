package com.totalbp.mm.config;

/**
 * Created by Ezra.R on 28/07/2017.
 */

public class AppConfig {

//    public static String URL_LOGIN = "http://10.66.1.8:2017/API/Security/RequestToken";
//    public static String URL_LOGIN_OLD = "http://10.66.1.8:81/TBPService/Service.svc/rest/LoginRest";
//    public static final String URL_PAGING = "http://10.66.1.8:81/TBPService/Service.svc/rest/GetDDLRest";
//    public static final String URL_PAGING_RESTFULL = "http://10.66.1.8:81/TBPService/Service.svc/rest/GetPagingRestFull";
//    public static final String URL_IMAGE_PREFIX = "http://10.66.1.8/CISUploads";
//    public static final String URL_PAGING_REST = "http://10.66.1.8:81/TBPService/Service.svc/rest/GetPagingRest";
//    public static final String URL_GETLISTSPN = "http://10.66.1.8:81/TBPService/Service.svc/rest/GetListSpnDetails";
//    public static final String URL_CHECK_TOKEN = "http://10.66.1.8:2017/API/Security/CheckToken";
//    public static final String URL_PROGRESS_SAVE = "http://10.66.1.8:2017/API/QSPV/DailyWork/Progress/Update";
//    public static final String URL_POST_PICTURE = "http://10.66.1.8:2017/API/QSPV/DailyWork/Progress/PostPicture";
//    public static final String URL_UPLOAD = "http://10.66.1.8:2017/API/General/UploadFile";
//    public static final String URL_AKTIFITAS_SAVE = "http://10.66.1.8:2017/API/QSPV/DailyWork/IKP/Passing/Update";
//    public static final String urlProfileFromTBP = "http://10.66.1.8/assets/images/avatar/";

    public static String URL_LOGIN = "http://app.totalbp.com/REST/API/Security/RequestToken";
    public static String URL_LOGIN_OLD = "http://10.100.3.11/TBPService/Service.svc/rest/LoginRest";
    public static final String URL_PAGING = "http://10.100.3.11/TBPService/Service.svc/rest/GetDDLRest";
    public static final String URL_PAGING_RESTFULL = "http://10.100.3.11/TBPService/Service.svc/rest/GetPagingRestFull";
    public static final String URL_PAGING_RESTFULL_NEWDLL = "http://app.totalbp.com/REST/API/General/GetDataDDL";
    public static final String URL_IMAGE_PREFIX = "http://app.totalbp.com/CISUploads";
    public static final String URL_PAGING_REST = "http://10.100.3.11/TBPService/Service.svc/rest/GetPagingRest";
    public static final String URL_GETLISTSPN = "http://10.100.3.11/TBPService/Service.svc/rest/GetListSpnDetails";
    public static final String URL_CHECK_TOKEN = "http://app.totalbp.com/REST/API/Security/CheckToken";
    public static final String URL_CRUD_SPN = "http://app.totalbp.com/REST/API/Transaction/SPN/Save";
    public static final String URL_CHECK_APPROVAL = "http://app.totalbp.com/REST/API/General/GetApprovalPermission";
    public static final String URL_POST_APPROVAL = "http://app.totalbp.com/REST/API/General/Approve";
    public static final String URL_POST_REJECT = "http://app.totalbp.com/REST/API/General/Reject";
    public static final String urlProfileFromTBP = "http://10.66.1.8/assets/images/avatar/";
    public static final String URL_CRUD_MM = "http://app.totalbp.com/REST/API/Transaction/MaterialManagement/Mobile/Save";
    public static final String URL_CRUD_MM_TRANSFER = "http://app.totalbp.com/REST/API/Transaction/MaterialManagement/Mobile/SaveTransfer";
    public static final String URL_UPLOAD_GAMBAR = "http://app.totalbp.com/REST/API/General/UploadFile";
    public static final String URL_NEW_APPROVAL = "http://app.totalbp.com/REST/API/General/Approval/Core";
}
