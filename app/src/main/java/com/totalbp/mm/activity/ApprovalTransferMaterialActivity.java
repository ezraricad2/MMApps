package com.totalbp.mm.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.totalbp.mm.R;
import com.totalbp.mm.config.SessionManager;
import com.totalbp.mm.controller.MMController;
import com.totalbp.mm.interfaces.VolleyCallback;
import com.totalbp.mm.model.ApprovalEnt;
import com.totalbp.mm.model.CommonRequestMoFormEnt;
import com.totalbp.mm.model.ListItemRequestOutFullTable;
import com.totalbp.mm.model.ListItemTransferOut;
import com.totalbp.mm.model.checkApprovalEnt;
import com.totalbp.mm.utils.MProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ezra.R on 09/10/2017.
 */

public class ApprovalTransferMaterialActivity extends AppCompatActivity
{
    private SessionManager session;
    private MMController controller;
    public List<ListItemRequestOutFullTable> listItems = new ArrayList<>();

    private ActionMode actionMode;
    JSONObject item;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private TextView tvLabelNewMo, tvLabelFiler;
    private ImageView ivCourirPhoto;
    private EditText etFromTowerTransfer,etFromFloorTransfer,etFromAreaTransfer,
            etMaterialTransfer,etSenderTransfer,etToBeTransfer, etToTowerTransfer,
            etToFloorTransfer,etToAreaTransfer,etRecipientTransfer;
    private static final int request_data_from  = 20;
    private static final int request_data_from_2  = 21;
    private static final int request_data_to_confirmation = 22;
    private String idtransfer, idmaterial, approvalnumber, statusrequest, kodetransfer;
    private Button btnApproveTrans, btnRejectTrans, btnPostingTrans;
    private Dialog dialog;
    private LinearLayout llPostingTrans, llApprovalTrans;
    String today;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    Calendar myCalendar = new GregorianCalendar();

    public ApprovalTransferMaterialActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvaltransfer);

        controller = new MMController();
        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            idtransfer = (String) bd.get("IdTransfer");
            idmaterial = (String) bd.get("IdMaterial");
            approvalnumber = (String) bd.get("ApprovalNumber");
            statusrequest = (String) bd.get("StatusRequest");
            kodetransfer = (String) bd.get("KodeTransfer");
            Log.d("ASd", approvalnumber);
        }

        etFromTowerTransfer = (EditText) findViewById(R.id.etFromTowerTransfer);
        etFromFloorTransfer= (EditText) findViewById(R.id.etFromFloorTransfer);
        etFromAreaTransfer= (EditText) findViewById(R.id.etFromAreaTransfer);
        etMaterialTransfer= (EditText) findViewById(R.id.etMaterialTransfer);
        etSenderTransfer= (EditText) findViewById(R.id.etSenderTransfer);

        etToBeTransfer= (EditText) findViewById(R.id.etToBeTransfer);
        etToTowerTransfer= (EditText) findViewById(R.id.etToTowerTransfer);
        etToFloorTransfer= (EditText) findViewById(R.id.etToFloorTransfer);
        etToAreaTransfer= (EditText) findViewById(R.id.etToAreaTransfer);
        etRecipientTransfer= (EditText) findViewById(R.id.etRecipientTransfer);

        etFromTowerTransfer.setEnabled(false);
        etFromFloorTransfer.setEnabled(false);
        etFromAreaTransfer.setEnabled(false);
        etMaterialTransfer.setEnabled(false);
        etSenderTransfer.setEnabled(false);
        etToBeTransfer.setEnabled(false);
        etToTowerTransfer.setEnabled(false);
        etToFloorTransfer.setEnabled(false);
        etToAreaTransfer.setEnabled(false);
        etRecipientTransfer.setEnabled(false);

        btnApproveTrans = (Button) findViewById(R.id.btnApproveTrans);
        btnRejectTrans = (Button) findViewById(R.id.btnRejectTrans);
        btnPostingTrans = (Button) findViewById(R.id.btnPostingTrans);
        llPostingTrans = (LinearLayout) findViewById(R.id.llPostingTrans);
        llApprovalTrans = (LinearLayout) findViewById(R.id.llApprovalTrans);

        dialog = new Dialog(ApprovalTransferMaterialActivity.this);
        today = dateFormat.format(myCalendar.getTime());

        getSupportActionBar().setTitle("Confirmation Transfer Material");

        btnRejectTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApprove(kodetransfer, approvalnumber, "0");
            }
        });

        btnApproveTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApprove(kodetransfer, approvalnumber, "1");
            }
        });

        btnPostingTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPosting(kodetransfer, idtransfer);
            }
        });
//
            SetListView(idtransfer);
//        SetReadyStockMaterial(idmaterial);
//        getIdleStockQty(idmaterial);
           CheckAction(statusrequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);


        if( requestCode == request_data_from ) {
            if (data != null){
                //projectSelected.setText(data.getExtras().getString("projectname"));
                session.setKodeProyek(data.getExtras().getString("kodeProyek"));
                session.setNamaProyek(data.getExtras().getString("projectname"));

                //Toast.makeText(getApplicationContext(), "Sampai sini Kode Proyek"+data.getExtras().getString("kodeProyek"), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Sampai sini Kode2 Proyek"+session.getKodeProyek(), Toast.LENGTH_LONG).show();

                //setupViewPager(viewPager);
            }
        }

    }

    public void CheckAction(String statusrequest)
    {
        if(statusrequest.equals(""))
        {
            isCanPosting(kodetransfer, idtransfer);
        }
        else if(statusrequest.equals("Waiting for Approval"))
        {
            IsCanApprove(kodetransfer, approvalnumber, idtransfer);
        }
        else if(statusrequest.equals("ON PROGRESS"))
        {
            IsCanApprove(kodetransfer, approvalnumber, idtransfer);
        }
//        else if(statusrequest.equals("APPROVED"))
//        {
//
//        }
//        else if(statusrequest.equals("REJECTED"))
//        {
//
//        }

    }

    private void isCanPosting(String kodematerialout, String IdMo) {

        MProgressDialog.showProgressDialog(ApprovalTransferMaterialActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();
        ApprovalEnt approvalEnt = new ApprovalEnt();
        approvalEnt.setNik(session.getKeyNik());
        approvalEnt.setKode_proyek(session.getKodeProyek());
        approvalEnt.setForm_id("MA.MM.01");
        approvalEnt.setReference_id(kodematerialout);
        approvalEnt.setMode("is_can_posting");
        approvalEnt.setIs_approve("");
        approvalEnt.setApproval_number("0");
        approvalEnt.setTokenID(session.getUserToken());

        String jsonString = gsonHeader.toJson(approvalEnt);

        controller.ApprovalConnectionString(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {

                MProgressDialog.dismissProgressDialog();
                JsonArray jArray = new JsonParser().parse(output).getAsJsonArray();
                JsonObject jsonObject = jArray.get(0).getAsJsonObject();

                if(jsonObject.get("IsCanPosting").getAsString().equals("true"))
                {
                    llPostingTrans.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void IsCanApprove(String kodematerialout, String ApprovalNumber, String IdMo) {

        MProgressDialog.showProgressDialog(ApprovalTransferMaterialActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();
        ApprovalEnt approvalEnt = new ApprovalEnt();
        approvalEnt.setNik(session.getKeyNik());
        approvalEnt.setKode_proyek(session.getKodeProyek());
        approvalEnt.setForm_id("MA.MM.01");
        approvalEnt.setReference_id(kodematerialout);
        approvalEnt.setMode("");
        //approvalEnt.setIs_approve("");
        approvalEnt.setApproval_number(ApprovalNumber);
        approvalEnt.setTokenID(session.getUserToken());

        String jsonString = gsonHeader.toJson(approvalEnt);

        controller.ApprovalConnectionString(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {
                Log.d("Saapp", output);
                MProgressDialog.dismissProgressDialog();
                JsonArray jArray = new JsonParser().parse(output).getAsJsonArray();
                JsonObject jsonObject = jArray.get(0).getAsJsonObject();

                if(jsonObject.get("IsCanApprove").getAsString().equals("true"))
                {
                    llApprovalTrans.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void onApprove(String kodematerialout, String ApprovalNumber, String IsApprove) {

        MProgressDialog.showProgressDialog(ApprovalTransferMaterialActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();
        ApprovalEnt approvalEnt = new ApprovalEnt();
        approvalEnt.setNik(session.getKeyNik());
        approvalEnt.setKode_proyek(session.getKodeProyek());
        approvalEnt.setForm_id("MA.MM.01");
        approvalEnt.setReference_id(kodematerialout);
        approvalEnt.setMode("");
        approvalEnt.setIs_approve(IsApprove);
        approvalEnt.setApproval_number(ApprovalNumber);
        approvalEnt.setTokenID(session.getUserToken());

        String jsonString = gsonHeader.toJson(approvalEnt);

        controller.ApprovalConnectionString(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {

                Intent intent = new Intent();
                intent.putExtra("", "");
                setResult(request_data_to_confirmation, intent);
                finish();

            }
        });
    }

    private void onPosting(String kodematerialout, String IdMo) {

        MProgressDialog.showProgressDialog(ApprovalTransferMaterialActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();
        ApprovalEnt approvalEnt = new ApprovalEnt();
        approvalEnt.setNik(session.getKeyNik());
        approvalEnt.setKode_proyek(session.getKodeProyek());
        approvalEnt.setForm_id("MA.MM.01");
        approvalEnt.setReference_id(kodematerialout);
        approvalEnt.setMode("posting");
        approvalEnt.setIs_approve("");
        approvalEnt.setApproval_number("0");
        approvalEnt.setTokenID(session.getUserToken());

        String jsonString = gsonHeader.toJson(approvalEnt);

        controller.ApprovalConnectionString(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {


                Log.d("OmOm2", output);
                MProgressDialog.dismissProgressDialog();
                JsonArray jArray = new JsonParser().parse(output).getAsJsonArray();
                JsonObject jsonObject = jArray.get(0).getAsJsonObject();

                if(jsonObject.get("IsCanApprove").getAsString().equals("true"))
                {
                    Log.d("OmOm3", output);
                    updateRequestMO(jsonObject.get("Approval_Number").getAsString(), IdMo, kodematerialout);
                }

            }
        });
    }

    private void updateRequestMO(String ApprovalNumber, String IdMo, String KodeMaterialOut) {
        Log.d("OmOm4", ApprovalNumber);
        MProgressDialog.showProgressDialog(ApprovalTransferMaterialActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();

        CommonRequestMoFormEnt commonRequestMoFormEnt = new CommonRequestMoFormEnt();
        commonRequestMoFormEnt.setID(IdMo);
        commonRequestMoFormEnt.setKode_MO(KodeMaterialOut);
        commonRequestMoFormEnt.setKode_Proyek(session.getKodeProyek());
        commonRequestMoFormEnt.setKode_Item("");
        commonRequestMoFormEnt.setVolume("1");
        commonRequestMoFormEnt.setUnit("");
        commonRequestMoFormEnt.setWork_Code("");
        commonRequestMoFormEnt.setKode_Tower("");
        commonRequestMoFormEnt.setKode_Lantai("");
        commonRequestMoFormEnt.setKode_Zona("");
        commonRequestMoFormEnt.setCourierPhoto("");
        commonRequestMoFormEnt.setRefHeaderSPR("0");
        commonRequestMoFormEnt.setRefDetailSPR("0");
        commonRequestMoFormEnt.setStatusRequest("1");
        commonRequestMoFormEnt.setApprovalNo(ApprovalNumber);
        commonRequestMoFormEnt.setStatusAktif("True");
        commonRequestMoFormEnt.setPembuat(session.getUserName());
        commonRequestMoFormEnt.setNama_Pembuat(session.getUserName());
        commonRequestMoFormEnt.setWaktuBuat(today);
        commonRequestMoFormEnt.setPengubah(session.getKeyNik());
        commonRequestMoFormEnt.setNama_Pengubah(session.getUserName());
        commonRequestMoFormEnt.setWaktuUbah(today);
        commonRequestMoFormEnt.setComment("");
        commonRequestMoFormEnt.setTokenID(session.getUserToken());
        //commonRequestMoFormEnt.setModeReq("POSTING");
        commonRequestMoFormEnt.setFormId("MA.MM.01");
//
        String jsonString = gsonHeader.toJson(commonRequestMoFormEnt);
        Log.d("OmOm5", ApprovalNumber);
        controller.SaveGeneralObjectRequestMo(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {

                Toast toast = Toast.makeText(ApprovalTransferMaterialActivity.this,KodeMaterialOut+" Posting Success !", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Intent intent = new Intent();
                intent.putExtra("", "");
                setResult(request_data_to_confirmation, intent);
                finish();

            }
        });
    }

    public void updateStatusRequest(String action)
    {
        Gson gsonHeader = new Gson();
        checkApprovalEnt checkapprovalEnt = new checkApprovalEnt();
        checkapprovalEnt.setUserid(session.isNikUserLoggedIn());
        checkapprovalEnt.setKodeProyek(session.getKodeProyek());
        checkapprovalEnt.setAppprovalNo(approvalnumber);

        String jsonStringCheck = gsonHeader.toJson(checkapprovalEnt);

        if(action.equals("Approve"))
        {
            controller.SaveGeneralObjectPostApproval(getApplicationContext(), jsonStringCheck, new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray result) {
                    //Toast.makeText(getApplicationContext(),"CheckApproval result "+result,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSave(String output) {

                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("data", "");
                    Intent intent = new Intent(getApplicationContext(), RequestMaterialActivity.class );
                    intent.putExtras(sendBundle);
                    setResult(22,intent);
                    finish();

                    Toast.makeText(getApplicationContext(), "Submit Approval Success !" + output, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"CheckApproval "+output,Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ListPOActivity.this, CartQtyReceivedActivity.class);
                    //startActivity(intent);
                }
            });
        }
        else
        {
            controller.SaveGeneralObjectPostReject(getApplicationContext(), jsonStringCheck, new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray result) {
                    //Toast.makeText(getApplicationContext(),"CheckApproval result "+result,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSave(String output) {

                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("data", "");
                    Intent intent = new Intent(getApplicationContext(), RequestMaterialActivity.class );
                    intent.putExtras(sendBundle);
                    setResult(22,intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Submit Reject Success !" + output, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"CheckApproval "+output,Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ListPOActivity.this, CartQtyReceivedActivity.class);
                    //startActivity(intent);
                }
            });
        }
    }

    public void SetListView(String wherecond){

        session = new SessionManager(getApplicationContext());
        //listItems.clear();
        String whereCond = "";
        if (!wherecond.equals("")){
            whereCond = wherecond;
        }
        controller.InqGeneral2(getApplicationContext(),"SPM_GetListTransferMaterialOut_Param",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond"," AND TTrans.ID ="+wherecond,
                "@nik",session.getKeyNik(),
                "@formid","MA.MM.01",
                "@zonaid","",
                new VolleyCallback() {
                    @Override
                    public void onSave(String output) {

                    }

                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            Log.d("EZRAsss", result.toString());

                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    item = result.getJSONObject(i);

                                    etFromTowerTransfer.setText(item.getString("Nama_Tower"));
                                    etFromFloorTransfer.setText(item.getString("Nama_Lantai"));
                                    etFromAreaTransfer.setText(item.getString("Nama_Zona"));
                                    etMaterialTransfer.setText(item.getString("Deskripsi"));
                                    etSenderTransfer.setText(item.getString("Pembuat"));


                                    etToBeTransfer.setText(item.getString("Volume"));
                                    etToTowerTransfer.setText(item.getString("Nama_KeTower"));
                                    etToFloorTransfer.setText(item.getString("Nama_KeLantai"));
                                    etToAreaTransfer.setText(item.getString("Nama_KeZona"));
                                    etRecipientTransfer.setText(item.getString("ToQSPV"));

//                                    ListItemTransferOut listProjectEnt = new ListItemTransferOut(
//                                            item.getString("ID"), item.getString("Kode_Transfer"),
//                                            item.getString("Kode_Proyek"), item.getString("Kode_Item"),
//                                            item.getString("Volume"),item.getString("Unit"),
//                                            item.getString("FromQSPV"),item.getString("FromKode_Tower"),
//                                            item.getString("FromKode_Lantai"),item.getString("FromKode_Zona"),
//                                            item.getString("ToQSPV"),item.getString("ToKode_Tower"),
//                                            item.getString("ToKode_Lantai"),item.getString("ToKode_Zona"),
//                                            item.getString("CourierPhoto"),item.getString("StatusRequest"),
//                                            item.getString("ApprovalNo"),item.getString("StatusAktif"),
//                                            item.getString("Pembuat"),item.getString("WaktuBuat"),
//                                            item.getString("Pengubah"),item.getString("WaktuUbah"),
//                                            item.getString("Comment"),item.getString("FlagReturnGudang"),
//                                            item.getString("Deskripsi"),item.getString("Nama_Tower"),
//                                            item.getString("Nama_Lantai"),item.getString("Nama_Zona")
//                                    );

                                    //listItems.add(listProjectEnt);
                                }

                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void SetReadyStockMaterial(String wherecond){
        session = new SessionManager(getApplicationContext());
        //listItems.clear();

        controller.InqGeneral3(getApplicationContext(),"SPM_GetQtyReadyStockFromViewParam",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond"," WHERE Kode_Proyek = "+session.getKodeProyek()+ " AND KatalogID = "+wherecond,


                new VolleyCallback() {
                    @Override
                    public void onSave(String output) {

                    }

                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            Log.d("WOI2",result.toString());
                            if (result.length() > 0 || !result.toString().equals("[]")) {
                                for (int i = 0; i < result.length(); i++) {
                                    item = result.getJSONObject(i);
//                                    etReadyStock.setText(item.getString("LatestVol"));
                                }
                            }
                            else {
//                                etReadyStock.setText("0");
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getIdleStockQty(String material){
        Log.d("material", material);
        session = new SessionManager(getApplicationContext());
        controller.InqGeneral3(getApplicationContext(),"SPM_GetQtyIdleStockFromView_Param",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND Kode_Item = '"+material+"' AND FlagReturnGudang = 1 ",
                //"@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND Kode_Item = '"+material+"' AND ToKode_Tower = '"+tower+"' AND FlagReturnGudang = 1 ",
                //"@nik",String.valueOf(session.isLoggedIn()),
                //"@formid","MA.MM.01",
                //"@zonaid","",
                new VolleyCallback() {
                    @Override
                    public void onSave(String output) {

                    }

                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            Log.d("EZRA", result.toString());
                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    item = result.getJSONObject(i);
//                                    etIdleStock.setText(item.getString("vol"));
                                }

                            }
                            else
                            {
//                                etIdleStock.setText("0");
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
            //btnConnect.setVisibility(View.GONE);
            //h2.removeCallbacks(run);
        }
        else
        {
            isAvailable = false;
            ///h2.postDelayed(run, 0);
            //btnConnect.setVisibility(View.VISIBLE);
        }
        return isAvailable;
    }



    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            tvLabelNewMo.startAnimation(fab_close);
            tvLabelFiler.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            tvLabelNewMo.startAnimation(fab_open);
            tvLabelFiler.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }


}
