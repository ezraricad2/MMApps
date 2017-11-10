package com.totalbp.mm.config;

/**
 * Created by Ezra.R on 28/07/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.totalbp.mm.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "TBPPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NIK = "nik";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_ROLE = "role";

    public static final String KEY_TOKEN = "token";

    public static final String KEY_NAMA = "nama";

    public static final String KEY_KODEPROYEK = "kodeproyek";

    public static final String KEY_IDSPK = "IdSpk";
    public static final String KEY_TOWER = "Tower";
    public static final String KEY_FLOOR = "Floor";
    public static final String KEY_ZONA = "Zona";
    public static final String KEY_NAMAVENDORJKH = "NamaVendorJkh";
    public static final String KEY_NAMAPEKERJAAN = "NamaPekerjaan";
    public static final String KEY_PERCENTAGE = "Percentage";
    public static final String KEY_IDVENDOR = "IdVendor";
    public static final String KEY_KODESPK = "KodeSpk";
    public static final String KEY_DATEPICKER_ddMMYYYY = "tanggal";
    public static final String KEY_DATEPICKER_YYYYMMdd = "tanggal2";

    public String getKeyIdspk() {
        return pref.getString(KEY_IDSPK,"");
    }

    public String getKeyTower() {
        return pref.getString(KEY_TOWER,"");
    }

    public String getKeyFloor() {
        return pref.getString(KEY_FLOOR,"");
    }

    public String getKeyZona() {
        return pref.getString(KEY_ZONA,"");
    }

    public String getKeyNamavendorjkh() {
        return pref.getString(KEY_NAMAVENDORJKH,"");
    }

    public String getKeyNamapekerjaan() {
        return pref.getString(KEY_NAMAPEKERJAAN,"");
    }

    public String getKeyPercentage() {
        return pref.getString(KEY_PERCENTAGE,"");
    }

    public String getKeyIdvendor() {
        return pref.getString(KEY_IDVENDOR,"");
    }

    public String getKeyKodespk() {
        return pref.getString(KEY_KODESPK,"");
    }

    private static String TAG = SessionManager.class.getSimpleName();

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(boolean isLoggedIn, String nik, String email, String role, String token, String nama){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, isLoggedIn);

        // Storing name in pref
        editor.putString(KEY_NIK, nik);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_NAMA, nama);
        // commit changes
        editor.commit();
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NIK, pref.getString(KEY_NIK, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, ""));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, ""));

        // return user
        return user;
    }

    public void SpnDetilSelected(String IdSpn, String KodeSpnParam, String NoRevisi, String KodeProyek, String TglSpn, String KodeZona, String KodeVendor, String SuratJalan, String Keterangan, String NamaVendor, String StatusApproval){

        editor.putString("IdSpn", IdSpn);
        editor.putString("KodeSpnParam", KodeSpnParam);
        editor.putString("NoRevisi", NoRevisi);
        editor.putString("KodeProyekParam", KodeProyek);
        editor.putString("TglSpn", TglSpn);
        editor.putString("KodeZona", KodeZona);
        editor.putString("KodeVendor", KodeVendor);
        editor.putString("SuratJalan", SuratJalan);
        editor.putString("Keterangan", Keterangan);
        editor.putString("NamaVendor", NamaVendor);
        editor.putString("StatusApproval", StatusApproval);
        editor.commit();
    }

    //start user privilege
    public void setCanView(String setCanView){
        editor.putString("setCanView",setCanView);
        editor.commit();
    }

    public void setCanEdit(String setCanEdit){
        editor.putString("setCanEdit",setCanEdit);
        editor.commit();
    }

    public void setCanInsert(String setCanInsert){
        editor.putString("setCanInsert",setCanInsert);
        editor.commit();
    }

    public void setCanDelete(String setCanDelete){
        editor.putString("setCanDelete",setCanDelete);
        editor.commit();
    }

    public void setCanApprove(String setCanApprove){
        editor.putString("setCanApprove",setCanApprove);
        editor.commit();
    }

    public String getCanApprove(){
        return pref.getString("setCanApprove","");
    }
    public String getCanDelete(){
        return pref.getString("setCanDelete","");
    }
    public String getCanEdit(){
        return pref.getString("setCanEdit","");
    }
    public String getCanInsert(){
        return pref.getString("setCanInsert","");
    }
    public String getCanView(){
        return pref.getString("setCanView","");
    }
    //end

    public void SetPekerjaanDetil(String IdSpk, String Tower, String Floor, String Zona, String NamaVendorJkh, String NamaPekerjaan, String Percentage, String IdVendor, String KodeSpk){

        editor.putString(KEY_IDSPK, IdSpk);
        editor.putString(KEY_TOWER, Tower);
        editor.putString(KEY_FLOOR, Floor);
        editor.putString(KEY_ZONA, Zona);
        editor.putString(KEY_NAMAVENDORJKH, NamaVendorJkh);
        editor.putString(KEY_NAMAPEKERJAAN, NamaPekerjaan);
        editor.putString(KEY_PERCENTAGE, Percentage);
        editor.putString(KEY_IDVENDOR, IdVendor);
        editor.putString(KEY_KODESPK, KodeSpk);
        editor.commit();
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }



    public String getKodeProyek(){
        return pref.getString("ProyekID","");
    }

    public void setKodeProyek(String kodeProyek){
        editor.putString("ProyekID",kodeProyek);
        editor.commit();
        Log.d(TAG, "Kode proyek modified, kodeProyek: "+kodeProyek);
    }



    public void setIdPO(String IdPO){
        editor.putString("IdPO",IdPO);
        editor.commit();
        Log.d(TAG, "IdPO modified, IdPO: "+IdPO);
    }

    public void setKEY_DATEPICKER_YYYYMMdd(String Tanggal){
        editor.putString(KEY_DATEPICKER_YYYYMMdd,Tanggal);
        editor.commit();
        Log.d(TAG, "KEY_DATEPICKER modified, TglRencanaKirim: "+Tanggal);
    }

    public void setKEY_DATEPICKER_ddMMYYYY(String Tanggal){
        editor.putString(KEY_DATEPICKER_ddMMYYYY,Tanggal);
        editor.commit();
        Log.d(TAG, "KEY_DATEPICKER modified, TglRencanaKirim: "+Tanggal);
    }

    public String getKEY_DATEPICKER_YYYYMMdd(){
        return pref.getString(KEY_DATEPICKER_YYYYMMdd,"");
    }

    public String getKEY_DATEPICKER_ddMMYYYY(){
        return pref.getString(KEY_DATEPICKER_ddMMYYYY,"");
    }




    public String getIdPO(){
        return pref.getString("IdPO","");
    }

    public void setTglRencanaTerima(String TglRencanaTerima){
        editor.putString("TglRencanaTerima",TglRencanaTerima);
        editor.commit();
        Log.d(TAG, "TglRencanaTerima modified, TglRencanaTerima: "+TglRencanaTerima);
    }

    public void setTglRencanaKirim(String TglRencanaKirim){
        editor.putString("TglRencanaKirim",TglRencanaKirim);
        editor.commit();
        Log.d(TAG, "TglRencanaKirim modified, TglRencanaKirim: "+TglRencanaKirim);
    }

    public String getTglRencanaKirim(){
        return pref.getString("TglRencanaKirim","");
    }

    public String getTglRencanaTerima(){
        return pref.getString("TglRencanaTerima","");
    }

    public void setCodePO(String CodePO){
        editor.putString("CodePO",CodePO);
        editor.commit();
        Log.d(TAG, "CodePO modified, CodePO: "+CodePO);
    }

    public String getCodePO(){
        return pref.getString("CodePO","");
    }

    public void setRole(String roleName){
        editor.putString("RoleName",roleName);
        editor.commit();
        Log.d(TAG, "Role login session modified!");
    }

    public void setLogin(boolean isLoggedIn, String nik, String email) {

        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.putString(KEY_NIK,nik);
        editor.putString(KEY_EMAIL,email);
        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public String getKeyNik(){
        return pref.getString(KEY_NIK,"");
    }

    public String getRole(){
        return pref.getString("RoleName","");
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String isNikUserLoggedIn(){
        return  pref.getString(KEY_NIK,"");
    }

    public String getUserToken(){
        return pref.getString(KEY_TOKEN,"");
    }

    public String getUserName(){
        return pref.getString(KEY_NAMA,"");
    }

    public String getUserEmail(){
        return pref.getString(KEY_EMAIL,"");
    }

    public void RemoveSession (){

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public String getItemDesc(){
        return pref.getString("desc","");
    }

    public String getItemUrl(){
        return pref.getString("url","");
    }

    public String getItemSpec(){
        return pref.getString("spec","");
    }

    public void setItemAdded(String qty, String date){
        editor.putString("quantity",qty);
        editor.putString("date",date);
        editor.commit();
    }

    public String getItemQty(){
        return pref.getString("quantity","");
    }

    public void setItemInfo(String name, String desc, String url, String curr, String ordered, String received,
                            String spec, String unit, String keterangan,
                            String kode_transaksi, String kode_material,
                            String kodeanak_kodematerial,String keteranganpo,
                            String unit1, String unit2,
                            String volume1, String volume2,
                            String harga_satuan_org1, String harga_satuan_org2,
                            String harga_satuan_std1, String harga_satuan_std2,
                            String panjang_material, String koefisien_konversi){
        editor.putString("name",name);
        editor.putString("desc",desc);
        editor.putString("url",url);
        editor.putString("curr",curr);
        editor.putString("ordered",ordered);
        editor.putString("received",received);
        editor.putString("spec",spec);
        editor.putString("unit",unit);
        editor.putString("keterangan",keterangan);

        editor.putString("kode_transaksi",kode_transaksi);
        editor.putString("kode_material",kode_material);
        editor.putString("kodeanak_kodematerial",kodeanak_kodematerial);
        editor.putString("keteranganpo",keteranganpo);
        editor.putString("unit1",unit1);
        editor.putString("unit2",unit2);
        editor.putString("volume1",volume1);
        editor.putString("volume2",volume2);
        editor.putString("harga_satuan_org1",harga_satuan_org1);
        editor.putString("harga_satuan_org2",harga_satuan_org2);
        editor.putString("harga_satuan_std1",harga_satuan_std1);
        editor.putString("harga_satuan_std2",harga_satuan_std2);
        editor.putString("panjang_material",panjang_material);
        editor.putString("koefisien_konversi",koefisien_konversi);
        editor.commit();
    }

    public String getKodeTransaksiPo(){
        return pref.getString("kode_transaksi","");
    }
    public String getItemKodeMaterial(){
        return pref.getString("kode_material","");
    }
    public String getItemKodeAnakKodeMaterial(){
        return pref.getString("kodeanak_kodematerial","");
    }
    public String getItemKeteranganPo(){
        return pref.getString("keteranganpo","");
    }
    public String getItemUnit1(){
        return pref.getString("unit1","");
    }
    public String getItemUnit2(){
        return pref.getString("unit2","");
    }
    public String getItemVolume1(){
        return pref.getString("volume1","");
    }
    public String getItemVolume2(){
        return pref.getString("volume2","");
    }
    public String getItemHargaSatuanOrg1(){
        return pref.getString("harga_satuan_org1","");
    }
    public String getItemHargaSatuanOrg2(){
        return pref.getString("harga_satuan_org2","");
    }
    public String getItemHargaSatuanStd1(){
        return pref.getString("harga_satuan_std1","");
    }
    public String getItemHargaSatuanStd2(){
        return pref.getString("harga_satuan_std2","");
    }
    public String getItemPanjangMaterial(){
        return pref.getString("panjang_material","");
    }
    public String getItemKoefisienKonversi(){
        return pref.getString("koefisien_konversi","");
    }

    public String getItemReceivedStock(){
        return pref.getString("received","");
    }

    public String getItemCurrentStock(){
        return pref.getString("curr","");
    }

    public String getItemOrderedStock(){
        return pref.getString("ordered","");
    }

    public String getItemPlanDate(){
        return pref.getString("date","");
    }

    public String getItemName(){
        return pref.getString("name","");
    }

    public  String getItemUnit(){
        return  pref.getString("unit","");
    }

    public  String getItemKeterangan(){
        return  pref.getString("keterangan","");
    }

    public String getNamaProyek(){
        return pref.getString("NamaProyek","");
    }

    public void setNamaProyek(String namaProyek){
        editor.putString("NamaProyek",namaProyek);
        editor.commit();
        Log.d(TAG, "Kode proyek modified, namaProyek: "+namaProyek);
    }
    //======================================================================
    public void setAktifitasStatusPostParam(String jsonstring){
        editor.putString("AktifitasStatusPostParam", jsonstring);
        editor.commit();
        Log.d(TAG, "AktifitasStatusPostParam modif : "+jsonstring);
    }

    public String getAktifitasStatusPostParam(){
        return pref.getString("AktifitasStatusPostParam","");
    }
}
