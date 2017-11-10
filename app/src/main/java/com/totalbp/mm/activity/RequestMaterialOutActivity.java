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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.totalbp.mm.R;
import com.totalbp.mm.config.AppConfig;
import com.totalbp.mm.config.SessionManager;
import com.totalbp.mm.controller.MMController;
import com.totalbp.mm.fragments.ProfilePictureDialogFragment;
import com.totalbp.mm.interfaces.VolleyCallback;
import com.totalbp.mm.model.ApprovalEnt;
import com.totalbp.mm.model.CommonAreaFormEnt;
import com.totalbp.mm.model.CommonEnt;
import com.totalbp.mm.model.CommonFloorEnt;
import com.totalbp.mm.model.CommonFloorFormEnt;
import com.totalbp.mm.model.CommonItemEnt;
import com.totalbp.mm.model.CommonMaterialFormEnt;
import com.totalbp.mm.model.CommonRequestMoFormEnt;
import com.totalbp.mm.model.CommonTowerFormEnt;
import com.totalbp.mm.model.CommonWorkFormEnt;
import com.totalbp.mm.model.CommonZoneEnt;
import com.totalbp.mm.model.ListItemRequestOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.totalbp.mm.config.AppConfig.URL_IMAGE_PREFIX;
import static com.totalbp.mm.config.AppConfig.urlProfileFromTBP;
import static com.totalbp.mm.utils.ImageDecoder.decodeSampledBitmapFromFile;
import static com.totalbp.mm.utils.ImageDecoder.getStringImage;

import com.totalbp.mm.utils.MProgressDialog;

/**
 * Created by Ezra.R on 09/10/2017.
 */

public class RequestMaterialOutActivity extends AppCompatActivity{

    private String fromrequestmaterial;

    private SearchableSpinner spinnerSpr, spinnerTower, spinnerFloor, spinnerArea, spinnerWork, spinnerMaterial;
    private FloatingActionButton floatActButton;
    private Button btnSave;
    private String spr = "", tower = "", floor = "", floortype = "", area = "", work = "", material = "", sprqty = "", tdsprid = "", today = "", unit = "";
    private String savePath;
    private int spnvol, tmovol, ttransvol;
    private String latestvol, idlveol;
    private ArrayList<CommonEnt> ddlArrayListTower = new ArrayList<>();
    private ArrayList<CommonTowerFormEnt> ddlArrayListTower2 = new ArrayList<>();
    private ArrayList<CommonFloorFormEnt> ddlArrayListFloor = new ArrayList<>();
    private ArrayList<CommonAreaFormEnt> ddlArrayListArea = new ArrayList<>();
    private ArrayList<CommonWorkFormEnt> ddlArrayListWork = new ArrayList<>();
    private ArrayList<CommonMaterialFormEnt> ddlArrayListMaterial = new ArrayList<>();
    private SessionManager session;
    MMController controller;
    public ProgressDialog pDialog;
    private EditText etRequestBy, etReadyStock, etIdleStock, etVolumeRequest;
    private TextView tvUnggahFoto;
    JSONObject item;
    private ImageView imageHolder, ivCourierPhoto;
    private final int requestCode = 30;
    private int year, month, day;
    Uri photoURI;
    public String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Dialog dialog;


    //Calendar myCalendar;
    Calendar myCalendar = new GregorianCalendar();
    public String id_daily_log_temp;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private static final int request_data_from  = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestmaterialoutform);

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

        spinnerSpr = (SearchableSpinner) findViewById(R.id.spinnerSpr);
        spinnerTower = (SearchableSpinner) findViewById(R.id.spinnerTower);
        spinnerFloor = (SearchableSpinner) findViewById(R.id.spinnerFloor);
        spinnerArea = (SearchableSpinner) findViewById(R.id.spinnerArea);
        spinnerWork= (SearchableSpinner) findViewById(R.id.spinnerWork);
        spinnerMaterial = (SearchableSpinner) findViewById(R.id.spinnerMaterial);
        etRequestBy = (EditText) findViewById(R.id.etRequestBy);
        etReadyStock = (EditText) findViewById(R.id.etReadyStock);
        etIdleStock = (EditText) findViewById(R.id.etIdleStock);
        //etSprQty = (EditText) findViewById(R.id.etSprQty);
        etVolumeRequest = (EditText) findViewById(R.id.etVolumeRequest);
        tvUnggahFoto = (TextView) findViewById(R.id.tvUnggahFoto);
        imageHolder = (ImageView)findViewById(R.id.ivItem);
        ivCourierPhoto = (ImageView) findViewById(R.id.ivCourierPhoto);

        floatActButton = (FloatingActionButton) findViewById(R.id.action_header_search_btn);
        btnSave=(Button) findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Request Material Out");
        session = new SessionManager(getApplicationContext());

        controller = new MMController();
        pDialog = new ProgressDialog(this);

        ivCourierPhoto.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        etRequestBy.setText(session.getUserName());
        etRequestBy.setEnabled(false);
        etReadyStock.setEnabled(false);
        etIdleStock.setEnabled(false);


        dialog = new Dialog(RequestMaterialOutActivity.this);

        initControls();

        tvUnggahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(photoCaptureIntent, requestCode);

                dispatchTakePictureIntent();
            }
        });

        ivCourierPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(photoCaptureIntent, requestCode);

                dispatchTakePictureIntent();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Jika edittext tidak kosong
                String et_ReadyStock = etReadyStock.getText().toString().trim();
                String et_VolumeRequest = etVolumeRequest.getText().toString().trim();
                String et_IdleStock = etIdleStock.getText().toString().trim();

                if(et_ReadyStock.length() != 0)
                {
                        if(et_IdleStock.length() != 0)
                        {
                            if(et_VolumeRequest.length() != 0)
                            {
                                float volumerequest = Float.parseFloat(etVolumeRequest.getText().toString());
                                float volumereadystock = Float.parseFloat(etReadyStock.getText().toString());
                                float volumeidlestock = Float.parseFloat(etIdleStock.getText().toString());
                                if (volumerequest <= volumereadystock)
                                {
                                    saveRequestMO(etVolumeRequest.getText().toString(),"0");
                                }
                                else
                                {

                                    if (volumerequest <= (volumereadystock+volumeidlestock))
                                    {
                                        float qtyfinal1 = volumerequest-volumereadystock;
                                        saveRequestMO(etReadyStock.getText().toString(),String.valueOf(qtyfinal1));
                                    }
                                    else {
                                        Toast toast = Toast.makeText(RequestMaterialOutActivity.this, "Volume Request must less than Ready Stock or Ready Stock + Idle Stock!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();

                                        etVolumeRequest.setText("");
                                        etVolumeRequest.requestFocus();
                                    }
                                }
                            }
                            else {
                                Toast toast = Toast.makeText(RequestMaterialOutActivity.this, "Volume Request required!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                                etVolumeRequest.requestFocus();
                            }
                        }
                        else
                        {
                            Toast toast = Toast.makeText(RequestMaterialOutActivity.this, "Idle Stock required!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                }
                else {

                    Toast toast = Toast.makeText(RequestMaterialOutActivity.this, "Ready Stock required!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                                MProgressDialog.dismissProgressDialog();

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

    private void saveRequestMO(String qty, String qty2) {

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        Gson gsonHeader = new Gson();
        CommonRequestMoFormEnt commonRequestMoFormEnt = new CommonRequestMoFormEnt();
        commonRequestMoFormEnt.setID("0");
        commonRequestMoFormEnt.setKode_MO(session.getKodeProyek());
        commonRequestMoFormEnt.setKode_Proyek(session.getKodeProyek());
        commonRequestMoFormEnt.setKode_Item(material);
        commonRequestMoFormEnt.setVolume(qty);
        commonRequestMoFormEnt.setUnit(unit);
        commonRequestMoFormEnt.setWork_Code(work);
        commonRequestMoFormEnt.setKode_Tower(tower);
        commonRequestMoFormEnt.setKode_Lantai(floor);
        commonRequestMoFormEnt.setKode_Zona(area);
        commonRequestMoFormEnt.setCourierPhoto(savePath);
        commonRequestMoFormEnt.setRefHeaderSPR("0");
        commonRequestMoFormEnt.setRefDetailSPR("0");
        commonRequestMoFormEnt.setStatusRequest("1");
        commonRequestMoFormEnt.setApprovalNo("");
        commonRequestMoFormEnt.setStatusAktif("True");
        commonRequestMoFormEnt.setPembuat(session.getUserName());
        commonRequestMoFormEnt.setNama_Pembuat(session.getKeyNik());
        commonRequestMoFormEnt.setWaktuBuat(today);
        commonRequestMoFormEnt.setPengubah(session.getKeyNik());
        commonRequestMoFormEnt.setNama_Pengubah(session.getKeyNik());
        commonRequestMoFormEnt.setWaktuUbah(today);
        commonRequestMoFormEnt.setComment("");
        commonRequestMoFormEnt.setTokenID(session.getUserToken());
        commonRequestMoFormEnt.setVolumeIdle(qty);
        //commonRequestMoFormEnt.setModeReq("POSTING");
        commonRequestMoFormEnt.setFormId("MA.MM.01");
        String jsonString = gsonHeader.toJson(commonRequestMoFormEnt);

        controller.SaveGeneralObjectRequestMo(getApplicationContext(),jsonString,new VolleyCallback(){
            @Override
            public void onSuccess(JSONArray result) {
                // MProgressDialog.dismissProgressDialog();
            }

            @Override
            public void onSave(String output) {
                if(!output.equals("")) {
                    Log.d("verrr", "verrr" + output);
                    MProgressDialog.dismissProgressDialog();

                    String[] split = output.split("#");
                    String firstString = split[0];
                    String firstSecondstring = split[1];

                    isCanPosting(firstString, firstSecondstring);
                }
                else
                {
                    Toast toast = Toast.makeText(RequestMaterialOutActivity.this,"Save Failed!", Toast.LENGTH_LONG);
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
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
                    dialog = new Dialog(RequestMaterialOutActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.general_dialog);
                    dialog.setCanceledOnTouchOutside(false);

                    TextView dTittle = (TextView) dialog.findViewById(R.id.dTittle);
                    TextView dContent = (TextView) dialog.findViewById(R.id.dContent);
                    Button dBtnCancel = (Button) dialog.findViewById(R.id.dBtnCancel);
                    Button dBtnSubmit = (Button) dialog.findViewById(R.id.dBtnSubmit);

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
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("", "");
                            setResult(request_data_from, intent);
                            finish();
                        }
                    });

                    dialog.show();


                }
                else
                {
                    Toast toast = Toast.makeText(RequestMaterialOutActivity.this,"Request Material Save Success!", Toast.LENGTH_LONG);
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
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

    private void IsCanApprove(String kodematerialout, String ApprovalNumber, String IdMo) {

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
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

//                Log.d("OmOm2", output);
//                MProgressDialog.dismissProgressDialog();
//                JsonArray jArray = new JsonParser().parse(output).getAsJsonArray();
//                JsonObject jsonObject = jArray.get(0).getAsJsonObject();

                //updateRequestMO(jsonObject.get("Approval_Number").getAsString(), IdMo);
            }
        });
    }

    private void updateRequestMO(String ApprovalNumber, String IdMo, String KodeMaterialOut) {
        Log.d("OmOm4", ApprovalNumber);
        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
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

                Toast toast = Toast.makeText(RequestMaterialOutActivity.this,KodeMaterialOut+" Posting Success !", Toast.LENGTH_LONG);
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

        //getDataForDDL(spinnerSpr,"SPM_GetThSprMobile_Param","ID","Kode_Spr",ddlArrayListTower,"@KodeProject",session.getKodeProyek(),"@currentpage","1","@pagesize","10000","@sortby","","@wherecond"," AND Kode_Proyek ='"+session.getKodeProyek()+"' ");
        //spinnerSpr.setTitle("Select SPR");
        //spinnerSpr.setPositiveButton("OK");
        //spinnerSpr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                final CommonEnt commonEnt = (CommonEnt) parent.getSelectedItem();
//                spr = commonEnt.getValue();
//                Log.d("sprChoose", spr);

//                if(!spr.equals("-")) {
                    getDataForDDL2(spinnerTower, "SPM_GetTower_Param", "Kode_Tower", "Nama_Tower", ddlArrayListTower2, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    //
                    //getReadyStockQty(spr);
                //}
            //}
            //@Override
            //public void onNothingSelected(AdapterView<?> parent) {
            //}
        //});

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
                    //getDataForDDL5(spinnerWork, "SPM_GetWorkBasedSprAndTowerAndFloorMobile_Param", "Kode_Work", "Nama_Work", ddlArrayListWork, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND a.Kode_Proyek ='" + session.getKodeProyek() + "' AND d.Kode_Zona ='" + tower + "' AND c.Kode_Lokasi ='" + floor + "' ");
                    Log.d("floorChoose", floor);
                   // Log.d("workChoose", work);
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
                    getDataForDDL5(spinnerWork, "SPM_GetWork_Param", "Kode_Pekerjaan", "Nama_Pekerjaan", ddlArrayListWork, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND KodeProject = '"+session.getKodeProyek()+"' AND TowerCode = '"+tower+"' AND FloorCode = '"+floor+"' AND M_Area.ID = '"+area+"' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    Log.d("areaChoose", area);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWork.setTitle("Select Work");
        spinnerWork.setPositiveButton("OK");
        spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonWorkFormEnt commonEnt = (CommonWorkFormEnt) parent.getSelectedItem();
                work = commonEnt.getValue();
                if(!work.equals("-")) {
                    //getDataForDDL6(spinnerMaterial, "SPT_SPR_Detail_Inq_MM_Param", "ID_M_Katalog", "Spec", ddlArrayListMaterial, "@TotalRecords", "", "@currentpage", "1", "@pagesize", "1000", "@sortby", "", "@wherecond", " AND b.Kode_Proyek ='" + session.getKodeProyek() + "' AND f.UserCode ='"+work+"' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    getDataForDDL6(spinnerMaterial, "SPT_SPR_Detail_Inq_MM_Param", "ID_M_Katalog", "Spec", ddlArrayListMaterial, "@TotalRecords", "", "@currentpage", "1", "@pagesize", "1000", "@sortby", "", "@wherecond", " AND b.Kode_Proyek ='" + session.getKodeProyek() + "' AND b.Pembuat ='" + session.getKeyNik() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
                    Log.d("workChoose", work);
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
                    getReadyStockQty(material);
                    getIdleStockQty(material);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //getDataForDDL3(spinnerZone,"SPM_GetArea_Param","Kode_Area","Nama_Area",ddlArrayListTower2,"@KodeProject",session.getKodeProyek(),"@currentpage","1","@pagesize","100","@sortby","","@wherecond"," AND Kode_Proyek ='"+session.getKodeProyek()+"' GROUP BY ID, Deskripsi");
        //getDataForDDL4(spinnerMaterial,"SPM_GetMaterial_Param","Kode_Item","Nama_Item",ddlArrayListTower3,"@KodeProject",session.getKodeProyek(),"@currentpage","1","@pagesize","200","@sortby","","@wherecond"," GROUP BY ID, Deskripsi");
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                        MProgressDialog.dismissProgressDialog();
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
                    MProgressDialog.dismissProgressDialog();
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                                MProgressDialog.dismissProgressDialog();
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
                            MProgressDialog.dismissProgressDialog();
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
                "@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND KatalogID = '"+material+"' AND Requester = '"+session.getKeyNik()+"' ",
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
                                etReadyStock.setText(latestvol);
                            }
                            else
                            {
                                etReadyStock.setText("0");
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
                "@wherecond"," WHERE Kode_Proyek = '"+session.getKodeProyek()+"' AND Kode_Item = '"+material+"' AND ToKode_Tower = '"+tower+"' AND FlagReturnGudang = 1 ",
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
                                    idlveol = item.getString("vol");
                                }
                                etIdleStock.setText(idlveol);
                            }
                            else
                            {
                                etIdleStock.setText("0");
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                                MProgressDialog.dismissProgressDialog();
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
                            MProgressDialog.dismissProgressDialog();
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                                MProgressDialog.dismissProgressDialog();
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
                            MProgressDialog.dismissProgressDialog();
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                                MProgressDialog.dismissProgressDialog();
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
                            MProgressDialog.dismissProgressDialog();
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

        MProgressDialog.showProgressDialog(RequestMaterialOutActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

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
                                MProgressDialog.dismissProgressDialog();
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
                            MProgressDialog.dismissProgressDialog();
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
