package com.totalbp.mm.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.totalbp.mm.R;
import com.totalbp.mm.config.AppConfig;
import com.totalbp.mm.config.SessionManager;
import com.totalbp.mm.controller.MMController;
import com.totalbp.mm.interfaces.VolleyCallback;
import com.totalbp.mm.model.ApprovalEnt;
import com.totalbp.mm.model.CommonAreaFormEnt;
import com.totalbp.mm.model.CommonEnt;
import com.totalbp.mm.model.CommonFloorFormEnt;
import com.totalbp.mm.model.CommonMaterialFormEnt;
import com.totalbp.mm.model.CommonRecipientEnt;
import com.totalbp.mm.model.CommonRequestMoFormEnt;
import com.totalbp.mm.model.CommonTowerFormEnt;
import com.totalbp.mm.model.CommonWorkFormEnt;
import com.totalbp.mm.model.ListItemTransferOutFullTableEnt;
import com.totalbp.mm.utils.MProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.totalbp.mm.config.AppConfig.URL_IMAGE_PREFIX;
import static com.totalbp.mm.utils.ImageDecoder.decodeSampledBitmapFromFile;
import static com.totalbp.mm.utils.ImageDecoder.getStringImage;

/**
 * Created by Ezra.R on 09/10/2017.
 */

public class TransferMaterialOutActivity extends AppCompatActivity{

    private String fromrequestmaterial;

    private SearchableSpinner spinnerTower, spinnerFloor, spinnerArea, spinnerMaterial;
    private SearchableSpinner spinnerTower2, spinnerFloor2, spinnerArea2, spinnerMaterial2, spinnerRecipient;
    private FloatingActionButton floatActButton;
    private Button btnTransfer;
    private String tower = "", floor = "", floortype = "", area = "", material = "", tdsprid = "", today = "", unit = "";
    private String tower2 = "", floor2 = "", floortype2 = "", area2 = "", recipient = "";
    private String savePath;
    private int spnvol, tmovol, ttransvol;
    private String latestvol, sitemanagername, volmo, voltrans;
    private ArrayList<CommonEnt> ddlArrayListTower = new ArrayList<>();
    private ArrayList<CommonTowerFormEnt> ddlArrayListTower2 = new ArrayList<>();
    private ArrayList<CommonFloorFormEnt> ddlArrayListFloor = new ArrayList<>();
    private ArrayList<CommonAreaFormEnt> ddlArrayListArea = new ArrayList<>();
    private ArrayList<CommonWorkFormEnt> ddlArrayListWork = new ArrayList<>();
    private ArrayList<CommonMaterialFormEnt> ddlArrayListMaterial = new ArrayList<>();
    private ArrayList<CommonRecipientEnt> ddlArrayListRecipient = new ArrayList<>();
    private SessionManager session;
    MMController controller;
    public ProgressDialog pDialog;
    private EditText etSender, etCurrentStock, etSiteManager, etQtyTransfer, etSiteManager2;
    private TextView tvUnggahFoto;
    JSONObject item, itemstrukturorganisasi, itemtransfer;
    private ImageView imageHolder;
    private final int requestCode = 30;
    private int year, month, day;
    Uri photoURI;
    public String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private CheckBox cbWarehouse;
    private LinearLayout llWarehouse, llRecipient;
    private Dialog dialogapproval;


    //Calendar myCalendar;
    Calendar myCalendar = new GregorianCalendar();
    public String id_daily_log_temp;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final int request_data_from  = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfermaterialoutform);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            fromrequestmaterial = (String) bd.get("fromrequestmaterial");
            Log.d("fromrequestmaterial", fromrequestmaterial);
        }

        today = dateFormat.format(myCalendar.getTime());

        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        day = myCalendar.get(Calendar.DAY_OF_MONTH);

        spinnerTower = (SearchableSpinner) findViewById(R.id.spinnerTower);
        spinnerFloor = (SearchableSpinner) findViewById(R.id.spinnerFloor);
        spinnerArea = (SearchableSpinner) findViewById(R.id.spinnerArea);


        spinnerTower2 = (SearchableSpinner) findViewById(R.id.spinnerTower2);
        spinnerFloor2 = (SearchableSpinner) findViewById(R.id.spinnerFloor2);
        spinnerArea2 = (SearchableSpinner) findViewById(R.id.spinnerArea2);
        spinnerRecipient = (SearchableSpinner) findViewById(R.id.spinnerRecipient);

        spinnerMaterial = (SearchableSpinner) findViewById(R.id.spinnerMaterial);
        etSender = (EditText) findViewById(R.id.etSender);
        etCurrentStock = (EditText) findViewById(R.id.etCurrentStock);
        etQtyTransfer = (EditText) findViewById(R.id.etQtyTransfer);
        etSiteManager = (EditText) findViewById(R.id.etManagerSender);
        etSiteManager2 = (EditText) findViewById(R.id.etSiteManager2);

        cbWarehouse = (CheckBox) findViewById(R.id.cbWarehouse);
        llWarehouse = (LinearLayout) findViewById(R.id.llWarehouse);
        llRecipient = (LinearLayout) findViewById(R.id.llRecipient);

        //etSprQty = (EditText) findViewById(R.id.etSprQty);
//        etVolumeRequest = (EditText) findViewById(R.id.etVolumeRequest);
//        tvUnggahFoto = (TextView) findViewById(R.id.tvUnggahFoto);
//        imageHolder = (ImageView)findViewById(R.id.ivItem);

        floatActButton = (FloatingActionButton) findViewById(R.id.action_header_search_btn);
        btnTransfer=(Button) findViewById(R.id.btnTransfer);

        getSupportActionBar().setTitle("Transfer Material Out");
        session = new SessionManager(getApplicationContext());

        controller = new MMController();
        pDialog = new ProgressDialog(this);

        etSender.setText(session.getUserName());
        etSender.setEnabled(false);
        etCurrentStock.setEnabled(false);

        dialogapproval = new Dialog(TransferMaterialOutActivity.this);

        initControls();

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbWarehouse.isChecked()) {
                    saveTransferMO("warehouse");
                }
                else
                {
                    saveTransferMO("");
                }
            }
        });

        cbWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbWarehouse.isChecked())
                {
                    cbWarehouse.setChecked(true);
                    llRecipient.setVisibility(View.GONE);
                    llWarehouse.setVisibility(View.GONE);
                }
                else
                {
                    cbWarehouse.setChecked(false);
                    llWarehouse.setVisibility(View.GONE);
                    llRecipient.setVisibility(View.VISIBLE);
                }
            }
        });

        getSiteManagetByNik(session.getKeyNik(), "SM1");


    }

    public void getQtyMo(String material){
        Log.d("material", material);
        session = new SessionManager(getApplicationContext());
        controller.InqGeneral3(getApplicationContext(),"SPM_GetQtyMo",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond"," AND Kode_Proyek = '"+session.getKodeProyek()+"' AND Kode_Item = '"+material+"' AND Nama_Pembuat = '"+session.getKeyNik()+"' GROUP BY Kode_Item",
                //"@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND KatalogID = '"+material+"' +"' AND Requester = '"+session.getUserName()+"' ",
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
                            Log.d("EZRAmo", result.toString());
                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    item = result.getJSONObject(i);
                                    volmo = item.getString("QtyMo");
                                    //Log.d("sinivol1", " :"+volmo);
                                    getQtyTrans(material);
                                }

                            }
                            else
                            {
                                volmo = "";
                                Log.d("sinivol2", " :"+volmo);
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getQtyTrans(String material){

        session = new SessionManager(getApplicationContext());
        controller.InqGeneral3(getApplicationContext(),"SPM_GetQtyTrans",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond"," AND Kode_Proyek = '"+session.getKodeProyek()+"' AND Kode_Item = '"+material+"' AND (FromQSPV = '"+session.getKeyNik()+"' OR ToQSPV = '"+session.getKeyNik()+"') AND FlagReturnGudang != 1 GROUP BY Kode_Item",
                //"@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND KatalogID = '"+material+"' +"' AND Requester = '"+session.getUserName()+"' ",
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
                            Log.d("EZRAtrans", result.toString());
                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    itemtransfer = result.getJSONObject(i);
                                    voltrans = itemtransfer.getString("QtyTrans");

                                    float maxvoltrans;

                                    Log.d("sinivolmo", " :"+volmo);
                                    Log.d("sinivoltrans", " :"+voltrans);
                                    if( (volmo != null) && (voltrans != null) ) {
                                        float volumemo = Float.parseFloat(volmo);
                                        float volumetrans = Float.parseFloat(voltrans);
                                        maxvoltrans = volumetrans - volumemo;
                                    }
                                    else
                                    {

                                        maxvoltrans = 0;
                                    }
                                    etCurrentStock.setText(String.valueOf(maxvoltrans));
                                }
                                //etReadyStock.setText(latestvol);
                            }
                            else
                            {
                                voltrans = "";
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //MainActivity activity = (MainActivity)getActivity();

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
//            photoFile = getOutputMediaFile(); //untuk sementara
//            try {
//                photoFile = createImageFile();
//
//            } catch (IOException ex) {
//                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
//                toast.show();
//            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
//                Log.d("path",mCurrentPhotoPath);
//                photoURI = Uri.fromFile(photoFile);

                photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.totalbp.mm.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
//                takePictureIntent.putExtra("id_daily_log",id_daily_log);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private  File createImageFile() throws IOException { //bener2 simpen filenya di storage directory
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                return null;
            }
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void displayDialog(String id_daily_log) {
        UploadPicture(id_daily_log);
        //materialDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getBaseContext(), "Photo Taken",Toast.LENGTH_SHORT).show();
//            displayDialog(data.getStringExtra("id_daily_log"));
            displayDialog(id_daily_log_temp);

            final Bitmap selectedImage = decodeSampledBitmapFromFile(mCurrentPhotoPath,500, 250);
            String encodedImage = getStringImage(selectedImage);

            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            // Set the image view
            //CameraimgView = (ImageView)materialDialog.findViewById(R.id.mImageView);
            //CameraimgView.setImageURI(photoURI);
            imageHolder.setImageBitmap(decodedByte);

        }
        /*if(this.requestCode == requestCode && resultCode == RESULT_OK){

            Bitmap bitmap = (Bitmap)data.getExtras().get("data");

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true);

            imageHolder.setImageBitmap(resized);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d("encoded", encoded);

            UploadFileToServer(encoded, "tesezraimage", "s");
        }
        */
    }

    private void UploadPicture(String id_daily_log){
        final Bitmap selectedImage = decodeSampledBitmapFromFile(mCurrentPhotoPath,500, 500);
        String filename="DP" +"_"+ mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/")+1);
        String encodedImage = getStringImage(selectedImage);
//        Log.d("dailylogid",id_daily_log);
//        long id = dailyProgressImageEnt.save();
//        Log.d("encoded",encodedImage);
        UploadFileToServer(encodedImage,filename, id_daily_log);

    }

    public void UploadFileToServer(String base64encode, String filename, final String id_hse_assessment){
        showDialog();
        RequestQueue requestQueue;
        session = new SessionManager(getApplicationContext());
        JSONObject request = new JSONObject();

        try {
            request.put("Pengubah", session.getUserDetails().get("nik"));
            request.put("FileName", filename);
            request.put("TokenID", session.getUserToken());
            request.put("FormID", "AR.03.01");
            request.put("KodeProyek",session.getKodeProyek()); //Dummy
            request.put("Base64String",base64encode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        http://10.100.223.51:20173/API/TQA/Upload
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_UPLOAD_GAMBAR,request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("IsSucceed").equals("true")){
                               // Toast.makeText(getApplicationContext(), response.getString("Message"), Toast.LENGTH_LONG).show();
//                                String savePath = response.getString("VarOutput");
                                String pathDatabase = response.getString("VarOutput"); //hashcode
                                String yearstring = String.valueOf(year);
                                String monthstring = "0"+String.valueOf(month+1);
                                savePath = URL_IMAGE_PREFIX + "/GeneralFile/"+yearstring.substring(yearstring.length()-2)+"/"+monthstring.substring(monthstring.length()-2)+"/" + response.getString("Message") + ".jpg";

                                Log.d("savePath",savePath);
                                hideDialog();

                            }
                            else{
                                Toast.makeText(getApplicationContext(), response.getString("Message "+"Please Re-Login"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error Response " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

    private void saveTransferMO(String isWarehouse) {

        showDialog();

        Gson gsonHeader = new Gson();
        ListItemTransferOutFullTableEnt commonRequestMoFormEnt = new ListItemTransferOutFullTableEnt();
        commonRequestMoFormEnt.setID("0");
        commonRequestMoFormEnt.setKode_Transfer(session.getKodeProyek());
        commonRequestMoFormEnt.setKode_Proyek(session.getKodeProyek());
        commonRequestMoFormEnt.setKode_Item(material);
        commonRequestMoFormEnt.setVolume(etQtyTransfer.getText().toString());
        commonRequestMoFormEnt.setUnit(unit);
        commonRequestMoFormEnt.setFromQSPV(session.getKeyNik());
        commonRequestMoFormEnt.setFromKode_Tower(tower);
        commonRequestMoFormEnt.setFromKode_Lantai(floor);
        commonRequestMoFormEnt.setFromKode_Zona(area);


        if(isWarehouse.equals("warehouse")) {
            commonRequestMoFormEnt.setToKode_Tower(tower);
            commonRequestMoFormEnt.setToKode_Lantai(floor);
            commonRequestMoFormEnt.setToKode_Zona(area);
            commonRequestMoFormEnt.setFlagReturnGudang("1");
            commonRequestMoFormEnt.setToQSPV(session.getKeyNik());
        }
        else
        {
            commonRequestMoFormEnt.setToKode_Tower(tower2);
            commonRequestMoFormEnt.setToKode_Lantai(floor2);
            commonRequestMoFormEnt.setToKode_Zona(area2);
            commonRequestMoFormEnt.setFlagReturnGudang("0");
            commonRequestMoFormEnt.setToQSPV(recipient);
        }
        commonRequestMoFormEnt.setCourierPhoto("");
        commonRequestMoFormEnt.setStatusRequest("1");
        commonRequestMoFormEnt.setApprovalNo("0");
        commonRequestMoFormEnt.setStatusAktif("True");
        commonRequestMoFormEnt.setPembuat(session.getKeyNik());
        commonRequestMoFormEnt.setWaktuBuat(today);
        commonRequestMoFormEnt.setPengubah(session.getKeyNik());
        commonRequestMoFormEnt.setWaktuUbah(today);
        commonRequestMoFormEnt.setComment("0");

        commonRequestMoFormEnt.setTokenID(session.getUserToken());
        commonRequestMoFormEnt.setModeReq("POSTING");
        commonRequestMoFormEnt.setFormId("MA.MM.01");
        String jsonString = gsonHeader.toJson(commonRequestMoFormEnt);

        controller.SaveGeneralObjectTransferMo(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {

                if(!output.equals("")) {

                    hideDialog();
                    String[] split = output.split("#");
                    String firstString = split[0];
                    String firstSecondstring = split[1];
                    Log.d("firstString", "firstString" + firstString);
                    isCanPosting(firstString, firstSecondstring);

//                hideDialog();
//                Toast.makeText(getApplicationContext(), "Save Transfer MO "+output+" Success!", Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent();
//                intent.putExtra("", "");
//                setResult(request_data_from, intent);
//                finish();
//
//                Log.d("FinalInputSuccessPos",output.toString());
                }
                else
                {
                    Toast toast = Toast.makeText(TransferMaterialOutActivity.this,"Save Failed!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    Intent intent = new Intent();
                    intent.putExtra("", "");
                    setResult(request_data_from, intent);
                    finish();
                }

            }
        });

    }


    private void isCanPosting(String kodematerialout, String IdMo) {

//        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                MProgressDialog.dismissProgressDialog();
//            }
//        });

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
                    dialogapproval = new Dialog(TransferMaterialOutActivity.this);
                    dialogapproval.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogapproval.setContentView(R.layout.general_dialog);
                    dialogapproval.setCanceledOnTouchOutside(false);

                    TextView dTittle = (TextView) dialogapproval.findViewById(R.id.dTittle);
                    TextView dContent = (TextView) dialogapproval.findViewById(R.id.dContent);
                    Button dBtnCancel = (Button) dialogapproval.findViewById(R.id.dBtnCancel);
                    Button dBtnSubmit = (Button) dialogapproval.findViewById(R.id.dBtnSubmit);

                    dTittle.setText("Material Out");
                    dContent.setText("Do you want Post Mo number "+kodematerialout+" ?");

                    dBtnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPosting(kodematerialout, IdMo);
                        }
                    });


                    dBtnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogapproval.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("", "");
                            setResult(request_data_from, intent);
                            finish();
                        }
                    });

                    dialogapproval.show();


                }
                else
                {
                    Toast toast = Toast.makeText(TransferMaterialOutActivity.this,"Request Material Save Success!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    Intent intent = new Intent();
                    intent.putExtra("", "");
                    setResult(request_data_from, intent);
                    finish();
                }

            }
        });
    }

    private void onPosting(String kodematerialout, String IdMo) {

        MProgressDialog.showProgressDialog(TransferMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        //Log.d("OmOm", kodematerialout+"|"+IdMo);
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
//                else
//                {
//                    Toast toast = Toast.makeText(RequestMaterialOutActivity.this,"You already posting !", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//
//                    Intent intent = new Intent();
//                    intent.putExtra("", "");
//                    setResult(request_data_from, intent);
//                    finish();
//                }

            }
        });
    }

    private void updateRequestMO(String ApprovalNumber, String IdMo, String KodeMaterialOut) {
        Log.d("OmOm4", ApprovalNumber);
        MProgressDialog.showProgressDialog(TransferMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();

        ListItemTransferOutFullTableEnt commonRequestMoFormEnt = new ListItemTransferOutFullTableEnt();
        commonRequestMoFormEnt.setID(IdMo);
        commonRequestMoFormEnt.setKode_Transfer(KodeMaterialOut);
        commonRequestMoFormEnt.setKode_Proyek(session.getKodeProyek());
        commonRequestMoFormEnt.setKode_Item(material);
        commonRequestMoFormEnt.setVolume(etQtyTransfer.getText().toString());
        commonRequestMoFormEnt.setUnit(unit);
        commonRequestMoFormEnt.setFromQSPV(session.getKeyNik());
        commonRequestMoFormEnt.setFromKode_Tower(tower);
        commonRequestMoFormEnt.setFromKode_Lantai(floor);
        commonRequestMoFormEnt.setFromKode_Zona(area);

            commonRequestMoFormEnt.setToKode_Tower(tower2);
            commonRequestMoFormEnt.setToKode_Lantai(floor2);
            commonRequestMoFormEnt.setToKode_Zona(area2);
            commonRequestMoFormEnt.setFlagReturnGudang("0");
            commonRequestMoFormEnt.setToQSPV(recipient);

        commonRequestMoFormEnt.setCourierPhoto("");
        commonRequestMoFormEnt.setStatusRequest("1");
        commonRequestMoFormEnt.setApprovalNo(ApprovalNumber);
        commonRequestMoFormEnt.setStatusAktif("True");
        commonRequestMoFormEnt.setPembuat(session.getKeyNik());
        commonRequestMoFormEnt.setWaktuBuat(today);
        commonRequestMoFormEnt.setPengubah(session.getKeyNik());
        commonRequestMoFormEnt.setWaktuUbah(today);
        commonRequestMoFormEnt.setComment("0");

        commonRequestMoFormEnt.setTokenID(session.getUserToken());
//        commonRequestMoFormEnt.setModeReq("POSTING");
        commonRequestMoFormEnt.setFormId("MA.MM.01");

        String jsonString = gsonHeader.toJson(commonRequestMoFormEnt);
        Log.d("OmOm5", ApprovalNumber);
        controller.SaveGeneralObjectTransferMo(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {
                Log.d("OmOm6", output);
                Toast toast = Toast.makeText(TransferMaterialOutActivity.this,KodeMaterialOut+" Posting Success !", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Intent intent = new Intent();
                intent.putExtra("", "");
                setResult(request_data_from, intent);
                finish();

            }
        });
    }

    public void initControls(){

        getDataForDDL2(spinnerTower, "SPM_GetTower_Param", "Kode_Tower", "Nama_Tower", ddlArrayListTower2, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
        getDataForDDL2(spinnerTower2, "SPM_GetTower_Param", "Kode_Tower", "Nama_Tower", ddlArrayListTower2, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");

        spinnerTower.setTitle("Select Tower");
        spinnerTower.setPositiveButton("OK");
        spinnerTower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonTowerFormEnt commonEnt = (CommonTowerFormEnt) parent.getSelectedItem();
                tower = commonEnt.getValue();
                if(!tower.equals("-")) {
                    getDataForDDL3(spinnerFloor, "SPM_GetFloor_Param", "Kode_Lantai", "Nama_Lantai", ddlArrayListFloor, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", " Nama_Lantai ASC ", "@wherecond", " AND M_Zona.Kode_Proyek ='" + session.getKodeProyek() + "' AND M_Zona.Kode_Zona ='" + tower + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    Log.d("towerChoose", tower);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTower2.setTitle("Select Tower");
        spinnerTower2.setPositiveButton("OK");
        spinnerTower2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonTowerFormEnt commonEnt = (CommonTowerFormEnt) parent.getSelectedItem();
                tower2 = commonEnt.getValue();
                if(!tower2.equals("-")) {
                    getDataForDDL3(spinnerFloor2, "SPM_GetFloor_Param", "Kode_Lantai", "Nama_Lantai", ddlArrayListFloor, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", " Nama_Lantai ASC ", "@wherecond", " AND M_Zona.Kode_Proyek ='" + session.getKodeProyek() + "' AND M_Zona.Kode_Zona ='" + tower2 + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    Log.d("towerChoose", tower2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFloor.setTitle("Select Floor");
        spinnerFloor.setPositiveButton("OK");
        spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonFloorFormEnt commonEnt = (CommonFloorFormEnt) parent.getSelectedItem();
                floor = commonEnt.getValue();
                floortype = commonEnt.getValue2();
                //work = commonEnt.getValue();
                if(!floor.equals("-")) {
                    getDataForDDL4(spinnerArea, "SPM_GetArea_Param", "Kode_Lokasi", "Nama_Area", ddlArrayListArea, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND M_Zona.Kode_Proyek ='" + session.getKodeProyek() + "' AND M_Zona.Kode_Zona ='" + tower + "' AND M_Area.Kode_Lokasi ='" + floortype+floor + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");

                    //getDataForDDL6(spinnerMaterial, "SPT_SPR_Detail_Inq_MM_Param", "ID_M_Katalog", "Spec", ddlArrayListMaterial, "@TotalRecords", "", "@currentpage", "1", "@pagesize", "1000", "@sortby", "", "@wherecond", " AND b.Kode_Proyek ='" + session.getKodeProyek() + "' AND f.UserCode ='"+work+"' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    getDataForDDL6(spinnerMaterial, "SPT_SPR_Detail_Inq_MM_Param", "ID_M_Katalog", "Spec", ddlArrayListMaterial, "@TotalRecords", "", "@currentpage", "1", "@pagesize", "1000", "@sortby", "", "@wherecond", " AND b.Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFloor2.setTitle("Select Floor");
        spinnerFloor2.setPositiveButton("OK");
        spinnerFloor2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonFloorFormEnt commonEnt = (CommonFloorFormEnt) parent.getSelectedItem();
                floor2 = commonEnt.getValue();
                floortype2 = commonEnt.getValue2();
                //work = commonEnt.getValue();
                if(!floor2.equals("-")) {
                    getDataForDDL4(spinnerArea2, "SPM_GetArea_Param", "Kode_Lokasi", "Nama_Area", ddlArrayListArea, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND M_Zona.Kode_Proyek ='" + session.getKodeProyek() + "' AND M_Zona.Kode_Zona ='" + tower2 + "' AND M_Area.Kode_Lokasi ='" + floortype2+floor2 + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerArea.setTitle("Select Area");
        spinnerArea.setPositiveButton("OK");
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonAreaFormEnt commonEnt = (CommonAreaFormEnt) parent.getSelectedItem();
                area = commonEnt.getValue();
                if(!area.equals("-")) {
                    //getDataForDDL5(spinnerWork, "SPM_GetWork_Param", "Kode_Pekerjaan", "Nama_Pekerjaan", ddlArrayListWork, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", "", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    Log.d("areaChoose", area);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerArea2.setTitle("Select Area");
        spinnerArea2.setPositiveButton("OK");
        spinnerArea2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonAreaFormEnt commonEnt = (CommonAreaFormEnt) parent.getSelectedItem();
                area2 = commonEnt.getValue();
                if(!area2.equals("-")) {
                    //getDataForDDL5(spinnerWork, "SPM_GetWork_Param", "Kode_Pekerjaan", "Nama_Pekerjaan", ddlArrayListWork, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", "", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    getDataForDDL8(spinnerRecipient, "TBP_ORGANIZATION_Inq", "Nik", "Nama", ddlArrayListRecipient, "@KdPry", session.getKodeProyek());
//                    "@KodeProject",session.getKodeProyek(),
//                            "@currentpage","1",
//                            "@pagesize","10",
//                            "@sortby","",
//                            "@wherecond",nik,
                    Log.d("areaChoose", area2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMaterial.setTitle("Select Material");
        spinnerMaterial.setPositiveButton("OK");
        spinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonMaterialFormEnt commonEnt = (CommonMaterialFormEnt) parent.getSelectedItem();
                material = commonEnt.getValue();
                tdsprid = commonEnt.getValue4();
                unit = commonEnt.getValue3();
                if(!material.equals("-")) {

                    Log.d("materialChoose", material);
                    Log.d("materialChoose", tdsprid);
                    Log.d("materialChoose", unit);
                    //getReadyStockQty(material);
                    getQtyMo(material);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRecipient.setTitle("Select Recipient");
        spinnerRecipient.setPositiveButton("OK");
        spinnerRecipient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonRecipientEnt commonEnt = (CommonRecipientEnt) parent.getSelectedItem();
                recipient = commonEnt.getValue();
                if(!recipient.equals("-")) {

                    getSiteManagetByNik(recipient,"SM2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void showDialog() {
        pDialog.setMessage("Loading ...");
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void getDataForDDL(final SearchableSpinner spinner, final String uniqueSPName,
                              final String paramName, final String paramVal,
                              final ArrayList<CommonEnt> ddlArrayList,
                              final String paramName1, final String paramVal1,
                              final String paramName2, final String paramVal2,
                              final String paramName3, final String paramVal3,
                              final String paramName4, final String paramVal4,
                              final String paramName5, final String paramVal5,
                              final String paramName6, final String paramVal6,
                              final String paramName7, final String paramVal7,
                              final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    if (result.length() > 0) {

                        CommonEnt itemEntsinit = new CommonEnt(
                                "-","-",
                                "-","-",
                                "-","-",
                                "-","-",
                                "-","-"
                        );
                        ddlArrayList.add(itemEntsinit);

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject item = result.getJSONObject(i);

                            CommonEnt itemEnts = new CommonEnt(item.getString(paramVal), item.getString(paramName),
                                    item.getString(paramName), item.getString(paramName),
                                    item.getString(paramName), item.getString(paramName),
                                    item.getString(paramName), item.getString(paramName),
                                    item.getString(paramName), item.getString(paramName)
                            );
                            ddlArrayList.add(itemEnts);
                        }
                        hideDialog();
                        ArrayAdapter<CommonEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                        spinner.setAdapter(adapterDDLGlobal);
                    }
                    else{
                        CommonEnt itemEnts = new CommonEnt(
                                "No Item Found","",
                                "No Item Found","",
                                "No Item Found","",
                                "No Item Found","",
                                "No Item Found",""
                        );
                        ddlArrayList.add(itemEnts);
                        ArrayAdapter<CommonEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                        spinner.setAdapter(adapterDDLGlobal);
                    }
                    hideDialog();
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onSave(String output) {

            }
        });
    }

    public void getDataForDDL2(final SearchableSpinner spinner, final String uniqueSPName,
                              final String paramName, final String paramVal,
                              final ArrayList<CommonTowerFormEnt> ddlArrayList,
                              final String paramName1, final String paramVal1,
                              final String paramName2, final String paramVal2,
                              final String paramName3, final String paramVal3,
                              final String paramName4, final String paramVal4,
                               final String paramName5, final String paramVal5,
                               final String paramName6, final String paramVal6,
                               final String paramName7, final String paramVal7,
                               final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {

                                CommonTowerFormEnt itemEntsinit = new CommonTowerFormEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );
                                ddlArrayList.add(itemEntsinit);

                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    CommonTowerFormEnt itemEnts = new CommonTowerFormEnt(item.getString(paramVal), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName)
                                    );
                                    ddlArrayList.add(itemEnts);
                                }
                                hideDialog();
                                ArrayAdapter<CommonTowerFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonTowerFormEnt itemEnts = new CommonTowerFormEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonTowerFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }

    public void getReadyStockQty(String material){
        Log.d("material", material);
        session = new SessionManager(getApplicationContext());
        controller.InqGeneral3(getApplicationContext(),"SPM_GetQtyReadyStockFromViewParam",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND KatalogID = '"+material+"' ",
                //"@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND KatalogID = '"+material+"' +"' AND Requester = '"+session.getUserName()+"' ",
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
                                    latestvol = item.getString("LatestVol");
                                }
                                etCurrentStock.setText(latestvol);
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getSiteManagetByNik(String nik, String sm){

        session = new SessionManager(getApplicationContext());
        controller.InqGeneral3(getApplicationContext(),"SPM_GetHirarkiUser",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage","1",
                "@pagesize","10",
                "@sortby","",
                "@wherecond",nik,
                //"@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND KatalogID = '"+material+"' +"' AND Requester = '"+session.getUserName()+"' ",
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
                                    itemstrukturorganisasi = result.getJSONObject(i);
                                    sitemanagername = itemstrukturorganisasi.getString("Nama");
                                }
                                if(sm.equals("SM1")) {
                                    etSiteManager.setText(sitemanagername);
                                }
                                else
                                {
                                    etSiteManager2.setText(sitemanagername);
                                }
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void getDataForDDL3(final SearchableSpinner spinner, final String uniqueSPName,
                               final String paramName, final String paramVal,
                               final ArrayList<CommonFloorFormEnt> ddlArrayList,
                               final String paramName1, final String paramVal1,
                               final String paramName2, final String paramVal2,
                               final String paramName3, final String paramVal3,
                               final String paramName4, final String paramVal4,
                               final String paramName5, final String paramVal5,
                               final String paramName6, final String paramVal6,
                               final String paramName7, final String paramVal7,
                               final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {

                                CommonFloorFormEnt itemEntsinit = new CommonFloorFormEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );
                                ddlArrayList.add(itemEntsinit);

                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    CommonFloorFormEnt itemEnts = new CommonFloorFormEnt(item.getString(paramVal), item.getString(paramName),
                                            item.getString("Tipe_Lokasi"), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName)
                                    );
                                    ddlArrayList.add(itemEnts);
                                }
                                hideDialog();
                                ArrayAdapter<CommonFloorFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonFloorFormEnt itemEnts = new CommonFloorFormEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonFloorFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }

    public void getDataForDDL4(final SearchableSpinner spinner, final String uniqueSPName,
                               final String paramName, final String paramVal,
                               final ArrayList<CommonAreaFormEnt> ddlArrayList,
                               final String paramName1, final String paramVal1,
                               final String paramName2, final String paramVal2,
                               final String paramName3, final String paramVal3,
                               final String paramName4, final String paramVal4,
                               final String paramName5, final String paramVal5,
                               final String paramName6, final String paramVal6,
                               final String paramName7, final String paramVal7,
                               final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {
                                CommonAreaFormEnt itemEntsinit = new CommonAreaFormEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );

                                ddlArrayList.add(itemEntsinit);
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    CommonAreaFormEnt itemEnts = new CommonAreaFormEnt(item.getString(paramVal), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName)
                                    );
                                    ddlArrayList.add(itemEnts);
                                }
                                hideDialog();
                                ArrayAdapter<CommonAreaFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonAreaFormEnt itemEnts = new CommonAreaFormEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonAreaFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }

    public void getDataForDDL5(final SearchableSpinner spinner, final String uniqueSPName,
                               final String paramName, final String paramVal,
                               final ArrayList<CommonWorkFormEnt> ddlArrayList,
                               final String paramName1, final String paramVal1,
                               final String paramName2, final String paramVal2,
                               final String paramName3, final String paramVal3,
                               final String paramName4, final String paramVal4,
                               final String paramName5, final String paramVal5,
                               final String paramName6, final String paramVal6,
                               final String paramName7, final String paramVal7,
                               final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {
                                CommonWorkFormEnt itemEntsinit = new CommonWorkFormEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );

                                ddlArrayList.add(itemEntsinit);
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    CommonWorkFormEnt itemEnts = new CommonWorkFormEnt(item.getString(paramVal), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName)
                                    );
                                    ddlArrayList.add(itemEnts);
                                }
                                hideDialog();
                                ArrayAdapter<CommonWorkFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonWorkFormEnt itemEnts = new CommonWorkFormEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonWorkFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }

    public void getDataForDDL6(final SearchableSpinner spinner, final String uniqueSPName,
                               final String paramName, final String paramVal,
                               final ArrayList<CommonMaterialFormEnt> ddlArrayList,
                               final String paramName1, final String paramVal1,
                               final String paramName2, final String paramVal2,
                               final String paramName3, final String paramVal3,
                               final String paramName4, final String paramVal4,
                               final String paramName5, final String paramVal5,
                               final String paramName6, final String paramVal6,
                               final String paramName7, final String paramVal7,
                               final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {
                                CommonMaterialFormEnt itemEntsinit = new CommonMaterialFormEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );

                                ddlArrayList.add(itemEntsinit);
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    CommonMaterialFormEnt itemEnts = new CommonMaterialFormEnt(item.getString(paramVal), item.getString(paramName),
                                            item.getString("Kd_Anak_Kd_Material"), item.getString("Unit"),
                                            item.getString("ID"), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName)
                                    );
                                    ddlArrayList.add(itemEnts);
                                    //sprqty = item.getString("Volume");
                                    //etSprQty.setText(sprqty);
                                    //Log.d("sprqty", sprqty);
                                }
                                hideDialog();
                                ArrayAdapter<CommonMaterialFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonMaterialFormEnt itemEnts = new CommonMaterialFormEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonMaterialFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }

    public void getDataForDDL7(final SearchableSpinner spinner, final String uniqueSPName,
                               final String paramName, final String paramVal,
                               final ArrayList<CommonMaterialFormEnt> ddlArrayList,
                               final String paramName1, final String paramVal1,
                               final String paramName2, final String paramVal2,
                               final String paramName3, final String paramVal3,
                               final String paramName4, final String paramVal4,
                               final String paramName5, final String paramVal5,
                               final String paramName6, final String paramVal6,
                               final String paramName7, final String paramVal7,
                               final String paramName8, final String paramVal8){
        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {
                                CommonMaterialFormEnt itemEntsinit = new CommonMaterialFormEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );

                                ddlArrayList.add(itemEntsinit);
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    CommonMaterialFormEnt itemEnts = new CommonMaterialFormEnt(item.getString(paramVal), item.getString(paramName),
                                            item.getString("Kd_Anak_Kd_Material"), item.getString("Unit"),
                                            item.getString("ID"), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName),
                                            item.getString(paramName), item.getString(paramName)
                                    );
                                    ddlArrayList.add(itemEnts);
                                    //sprqty = item.getString("Volume");
                                    //etSprQty.setText(sprqty);
                                    //Log.d("sprqty", sprqty);
                                }
                                hideDialog();
                                ArrayAdapter<CommonMaterialFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonMaterialFormEnt itemEnts = new CommonMaterialFormEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonMaterialFormEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }

    public void getDataForDDL8(final SearchableSpinner spinner, final String uniqueSPName,
                               final String paramName, final String paramVal,
                               final ArrayList<CommonRecipientEnt> ddlArrayList,
                               final String paramName1, final String paramVal1){

        final String paramName2=null; final String paramVal2=null;
        final String paramName3="moderecipient"; final String paramVal3=null;
        final String paramName4=null; final String paramVal4=null;
        final String paramName5=null; final String paramVal5=null;

        ddlArrayList.clear();
        showDialog();
        controller.InqGeneral3(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            if (result.length() > 0) {
                                CommonRecipientEnt itemEntsinit = new CommonRecipientEnt(
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-",
                                        "-","-"
                                );

                                ddlArrayList.add(itemEntsinit);
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);
                                    if( (item.getString("Role_Level").equals("4")) || item.getString("Role_Level").equals("5")) {
                                        CommonRecipientEnt itemEnts = new CommonRecipientEnt(item.getString(paramVal), item.getString(paramName),
                                                item.getString("Role_Description"), item.getString("Role_Level"),
                                                item.getString(paramName), item.getString(paramName),
                                                item.getString(paramName), item.getString(paramName),
                                                item.getString(paramName), item.getString(paramName)
                                        );
                                        ddlArrayList.add(itemEnts);
                                    }
                                    //sprqty = item.getString("Volume");
                                    //etSprQty.setText(sprqty);
                                    //Log.d("sprqty", sprqty);
                                }
                                hideDialog();
                                ArrayAdapter<CommonRecipientEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            else{
                                CommonRecipientEnt itemEnts = new CommonRecipientEnt(
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found","",
                                        "No Item Found",""
                                );
                                ddlArrayList.add(itemEnts);
                                ArrayAdapter<CommonRecipientEnt> adapterDDLGlobal = new ArrayAdapter<>(getBaseContext(),
                                        android.R.layout.simple_spinner_dropdown_item, ddlArrayList);
                                spinner.setAdapter(adapterDDLGlobal);
                            }
                            hideDialog();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onSave(String output) {

                    }
                });
    }
}
