package com.totalbp.mm;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.totalbp.mm.config.AppConfig;
import com.totalbp.mm.config.SessionManager;
import com.totalbp.mm.controller.MMController;
import com.totalbp.mm.helper.NetworkHelper;
import com.totalbp.mm.interfaces.VolleyCallback;
import com.totalbp.mm.utils.MProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Ezra.R on 28/07/2017.
 */


public class LoginActivity extends AppCompatActivity {

    private EditText edtTextNik, edtTextPassword;
    private Button btnLogin;
    private SessionManager session;
    private TextInputLayout txtInputLayoutNik,txtInputLayoutPassword;
    public static final int RequestPermissionCode = 1;
    private ProgressDialog pDialog;
    private NetworkHelper networkHelper;
    RequestQueue requestQueue;
    String nikparam, passwordparam;
    MMController controller;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE  = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        controller = new MMController();
        txtInputLayoutNik = (TextInputLayout) findViewById(R.id.txtInputLayoutNik);
        txtInputLayoutPassword = (TextInputLayout) findViewById(R.id.txtInputLayoutPassword);

        edtTextNik = (EditText) findViewById(R.id.nik);
        edtTextPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.nik_sign_in_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            //Toast.makeText(getApplicationContext(), "CATCH s"+session.isLoggedIn(), Toast.LENGTH_LONG).show();
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    // SQLite database handler
//                    db = new UserHandler(getApplicationContext());

                    // Session manager
                    session = new SessionManager(getApplicationContext());
                    nikparam = edtTextNik.getText().toString().trim();
                    passwordparam = edtTextPassword.getText().toString().trim();
                    submitForm();
                }
                else{
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    else{
                        // SQLite database handler
//                        db = new UserHandler(getApplicationContext());
                        // Session manager
                        session = new SessionManager(getApplicationContext());
                        nikparam = edtTextNik.getText().toString().trim();
                        passwordparam = edtTextPassword.getText().toString().trim();
                        submitForm();
                    }
                }




            }
        });

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {

                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {


                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadExternalPermission && WriteExternalPermission) {
//                        Toast toast=Toast.makeText(getApplicationContext(), "Permission Granted",Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                        toast.show();
                    }
                    else {
//                        Toast toast=Toast.makeText(getApplicationContext(), "Permission Denied",Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                        toast.show();
                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int FourPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FivePermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FivePermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void submitForm() {
        if (!validateNik()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        checkLoginRest(nikparam, passwordparam);
        /*
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            new RegisterBackProcess().execute();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(NewLoginActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Your internet not available");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        */
    }

    private boolean validateNik() {
        if (edtTextNik.getText().toString().trim().isEmpty()) {
            txtInputLayoutNik.setError("Nik Required");
            requestFocus(edtTextNik);
            return false;
        } else {
            txtInputLayoutNik.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (edtTextPassword.getText().toString().trim().isEmpty()) {
            txtInputLayoutPassword.setError("Password Required");
            requestFocus(edtTextPassword);
            return false;
        } else {
            txtInputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void checkRole(final String nik, final String proyek, final String type){
        controller.InqGeneral(getApplicationContext(), "CheckRoleMobileProcurement", "NIK", nik,
                "KodeProyek", proyek, "Type", "1", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            Log.d("TesLoginParam", result.toString());
                            if (result.length() > 0) {
                                session.setRole(result.getJSONObject(0).getString("Role_Name"));
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onSave(String output) {

                    }
                });
    }


    private void checkLoginRest(final String nik, final String password){

        //pDialog.setMessage("Logging in ...");
        //showDialog();
        MProgressDialog.showProgressDialog(LoginActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });

        JSONObject request = new JSONObject();
        try {
            request.put("NIK", nik);
            request.put("Password", password);
            request.put("IPAddress", "192.168.1.1");
            request.put("Referer", "");
            request.put("UserAgent","android");
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_LOGIN, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TesLoginParamLogin", response.toString());
                            MProgressDialog.dismissProgressDialog();
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c.getTime());

                            String nik2 = nik;
                            String email = "user@totalbp.com";
                            String token = response.getString("VarOutput");

                            String created_at = formattedDate;
                            String nama = null;

                            session.createLoginSession(true,nik,email, "role", token, nama);

                            if (session.getRole().equals("")){
                                for (int i=1;i<5;i++){
                                    if (session.getRole().equals("")){
                                        checkRole(nik,"1283",String.valueOf(i));
                                    }
                                }
                            }

                            //Log.d("Ezra Tok", response.getString("VarOutput"));
                            checkLoginOld(nik2,password);
                            // Launch main activity
                            //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            //intent.putExtra("UserName",nik2);
                            //startActivity(intent);



                            //session.setLogin(false,name,token);
//                            checkRole(nik,"1283","1");


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MProgressDialog.dismissProgressDialog();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),
                                    getApplicationContext().getString(R.string.error_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            //errorText.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof NetworkError) {
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                });
        requestQueue.add(strReq);
    }

    private void checkLoginOld(final String nik, final String password){
        //harus pake yg old supaya dapet nama lengk and email
        MProgressDialog.showProgressDialog(LoginActivity.this, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MProgressDialog.dismissProgressDialog();
            }
        });
        JSONObject request = new JSONObject();
        try {
            request.put("User", nik);
            request.put("Pass", password);
            request.put("TokenID","1234567");
            request.put("ADDomain","10.100.2.19");
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_LOGIN_OLD, request ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TesLoginParamOldLogin", response.toString());
                            MProgressDialog.dismissProgressDialog();
                            String name = nik;
                            String email = response.getString("Email");
                            String nama_lengkap = response.getString("NamaLengkap");
                            // user successfully logged in
                            // Create login session
                            //session.setLogin(true,name,session.getUserToken(), email,nama_lengkap);
                            session.createLoginSession(true,name,email, "role", session.getUserToken(), nama_lengkap);
                            // Inserting row in users table
                            // Launch main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            intent.putExtra("UserName",name);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MProgressDialog.dismissProgressDialog();


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),
                                    getApplicationContext().getString(R.string.error_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                        //Toast.makeText(getApplicationContext(), "Error Response" + error.toString(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(strReq);
    }

}