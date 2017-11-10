package com.totalbp.mm.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.totalbp.mm.R;
import com.totalbp.mm.config.SessionManager;
import com.totalbp.mm.controller.MMController;
import com.totalbp.mm.interfaces.VolleyCallback;
import com.totalbp.mm.model.CommonAreaFormEnt;
import com.totalbp.mm.model.CommonEnt;
import com.totalbp.mm.model.CommonFloorFormEnt;
import com.totalbp.mm.model.CommonMaterialFormEnt;
import com.totalbp.mm.model.CommonTowerFormEnt;
import com.totalbp.mm.model.CommonWorkFormEnt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ezra.R on 09/10/2017.
 */

public class TransferMaterialFilterActivity extends AppCompatActivity{

    private String fromtransfermaterial;

    private SearchableSpinner spinnerTower,spinnerFloor, spinnerZone, spinnerMaterial;
    private FloatingActionButton floatActButton;
    private String tower="-", floor="-", floortype="", area="-", material="-", tdsprid="", unit="";

    private ArrayList<CommonTowerFormEnt> ddlArrayListTower2 = new ArrayList<>();
    private ArrayList<CommonFloorFormEnt> ddlArrayListFloor = new ArrayList<>();
    private ArrayList<CommonAreaFormEnt> ddlArrayListArea = new ArrayList<>();
    private ArrayList<CommonWorkFormEnt> ddlArrayListWork = new ArrayList<>();
    private ArrayList<CommonMaterialFormEnt> ddlArrayListMaterial = new ArrayList<>();

    private SessionManager session;
    MMController controller;
    public ProgressDialog pDialog;

    private static final int request_data_from_2  = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfermaterialoutfilter);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            fromtransfermaterial = (String) bd.get("fromtransfermaterial");
            Log.d("fromtransfermaterial", fromtransfermaterial);
        }

        spinnerTower = (SearchableSpinner) findViewById(R.id.spinnerTower);
        spinnerFloor = (SearchableSpinner) findViewById(R.id.spinnerFloor);
        spinnerZone = (SearchableSpinner) findViewById(R.id.spinnerZone);
        spinnerMaterial = (SearchableSpinner) findViewById(R.id.spinnerMaterial);

        floatActButton = (FloatingActionButton) findViewById(R.id.action_header_search_btn);

        getSupportActionBar().setTitle("Filter Transfer Material");
        session = new SessionManager(getApplicationContext());

        controller = new MMController();
        pDialog = new ProgressDialog(this);
        initControls();

        floatActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tes", tower);
                Log.d("tes", floor);
                Log.d("tes", area);
                Log.d("tes", material);

                Intent intent = new Intent(getApplicationContext(), TransferMaterialActivity.class);
                intent.putExtra("towerid",tower);
                intent.putExtra("floorid",floor);
                intent.putExtra("areaid",area);
                intent.putExtra("materialid",material);
                setResult(request_data_from_2,intent);
                finish();
            }
        });

    }


    public void initControls(){

        getDataForDDL2(spinnerTower, "SPM_GetTower_Param", "Kode_Tower", "Nama_Tower", ddlArrayListTower2, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
        getDataForDDL3(spinnerFloor, "SPM_GetFloor_Param", "Kode_Lantai", "Nama_Lantai", ddlArrayListFloor, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", " Nama_Lantai ASC ", "@wherecond", " AND M_Zona.Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
        getDataForDDL4(spinnerZone, "SPM_GetArea_Param", "Kode_Lokasi", "Nama_Area", ddlArrayListArea, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND M_Zona.Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
        getDataForDDL6(spinnerMaterial, "SPT_SPR_Detail_Inq_MM_Param", "ID_M_Katalog", "Spec", ddlArrayListMaterial, "@TotalRecords", "", "@currentpage", "1", "@pagesize", "1000", "@sortby", "", "@wherecond", " AND b.Kode_Proyek ='" + session.getKodeProyek() + "' ", "@nik", String.valueOf(session.isLoggedIn()), "@formid", "MA.MM.01", "@zonaid","");
        spinnerTower.setTitle("Select Tower");
        spinnerTower.setPositiveButton("OK");
        spinnerTower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonTowerFormEnt commonEnt = (CommonTowerFormEnt) parent.getSelectedItem();
                tower = commonEnt.getValue();
                //if(!tower.equals("-")) {
                    //Log.d("towerChoose", tower);
                //}
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
                //if(!floor.equals("-")) {

                    //getDataForDDL5(spinnerWork, "SPM_GetWorkBasedSprAndTowerAndFloorMobile_Param", "Kode_Work", "Nama_Work", ddlArrayListWork, "@KodeProject", session.getKodeProyek(), "@currentpage", "1", "@pagesize", "10000", "@sortby", "", "@wherecond", " AND a.Kode_Proyek ='" + session.getKodeProyek() + "' AND d.Kode_Zona ='" + tower + "' AND c.Kode_Lokasi ='" + floor + "' ");
                    //Log.d("floorChoose", floor);
                    // Log.d("workChoose", work);
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerZone.setTitle("Select Area");
        spinnerZone.setPositiveButton("OK");
        spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final CommonAreaFormEnt commonEnt = (CommonAreaFormEnt) parent.getSelectedItem();
                area = commonEnt.getValue();
                //if(!area.equals("-")) {

                    //Log.d("areaChoose", area);
                //}
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
                Log.d("materialChoose", material);
                Log.d("materialChoose", tdsprid);
                Log.d("materialChoose", unit);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
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
        controller.InqGeneral2(getApplicationContext(), uniqueSPName, paramName1, paramVal1, paramName2, paramVal2, paramName3, paramVal3,paramName4, paramVal4,paramName5, paramVal5,paramName6, paramVal6,paramName7, paramVal7,paramName8, paramVal8,
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
                                "No Item Found","-",
                                "No Item Found","-",
                                "No Item Found","-",
                                "No Item Found","-",
                                "No Item Found","-"
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
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-"
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
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-"
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
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-"
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
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-",
                                        "No Item Found","-"
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
}
