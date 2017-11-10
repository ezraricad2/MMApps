package com.totalbp.mm.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.totalbp.mm.R;
import com.totalbp.mm.adapter.ListRequestItemAdapter;
import com.totalbp.mm.adapter.ListTransferItemAdapter;
import com.totalbp.mm.config.SessionManager;
import com.totalbp.mm.controller.MMController;
import com.totalbp.mm.interfaces.VolleyCallback;
import com.totalbp.mm.model.ListItemRequestOut;
import com.totalbp.mm.model.ListItemTransferOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by Ezra.R on 09/10/2017.
 */

public class TransferMaterialActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ListTransferItemAdapter.MessageAdapterListener, View.OnClickListener
{
    private SessionManager session;
    private MMController controller;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ListTransferItemAdapter adapter;
    public List<ListItemTransferOut> listItems = new ArrayList<>();

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    JSONObject item;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private TextView tvLabelNewMo, tvLabelFiler;

    private static final int request_data_from  = 20;
    private static final int request_data_from_2  = 21;

    private static final int request_data_to_confirmation  = 22;
    public Integer currentPage = 1;
    public Integer pageSize = 10;

    public Integer searchActive = 0;

    public TransferMaterialActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfermaterialout);

        controller = new MMController();
        session = new SessionManager(getApplicationContext());

        //BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        //findViewById(R.id.action_filter).setOnClickListener(this);
        //findViewById(R.id.action_sort).setOnClickListener(this);

        getSupportActionBar().setTitle("Material Transfer");
        //getSupportActionBar().setSubtitle(idik);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);

        tvLabelNewMo = (TextView)findViewById(R.id.tvLabelNewMo);
        tvLabelFiler = (TextView) findViewById(R.id.tvLabelFiler);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new ListTransferItemAdapter(this, listItems, this);
        //((ListRequestItemAdapter) adapter).setMode(Attributes.Mode.Single);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        actionModeCallback = new ActionModeCallback();

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if(isNetworkAvailable()){
                            SetListView("", currentPage.toString(), pageSize.toString());
                        }
                        else
                        {
                        }
                    }
                }
        );


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //JIKA BUKAN HASIL SEARCH MAKA ADD SCROOL AKTIF
                if(!searchActive.equals("1")){
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {

                        currentPage = currentPage + 1;
                        SetListView("", currentPage.toString(), pageSize.toString());
                        Log.d("onScroll", currentPage.toString());

                    }
                }
                else
                {

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);


        if( requestCode == request_data_from ) {
            if (data != null){
                listItems.clear();
                SetListView("", "1", "10");
            }
        }
        else if( requestCode == request_data_from_2 ) {
            if (data != null){

                listItems.clear();
                adapter.notifyDataSetChanged();
                searchActive = 1;

                //SetListView("", "1", "10");
                SetListViewSearch("", "1", "100", data.getExtras().getString("towerid"), data.getExtras().getString("floorid"), data.getExtras().getString("areaid"), data.getExtras().getString("materialid"));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            Intent intent2=new Intent(TransferMaterialActivity.this,TransferMaterialFilterActivity.class);
            intent2.putExtra("fromtransfermaterial","true");
            startActivityForResult(intent2, request_data_from_2);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SetListView(String wherecond, String currentpage, String pagesize){
        swipeRefreshLayout.setRefreshing(true);
        session = new SessionManager(getApplicationContext());
        //listItems.clear();
        String whereCond = "";
        if (!wherecond.equals("")){
            whereCond = wherecond;
        }
        controller.InqGeneral2(getApplicationContext(),"SPM_GetListTransferMaterialOut_Param",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage",currentpage,
                "@pagesize",pagesize,
                "@sortby","",
                "@wherecond"," AND TTrans.Kode_Proyek ="+session.getKodeProyek(),
                "@nik",String.valueOf(session.isLoggedIn()),
                "@formid","MA.MM.01",
                "@zonaid","",
                new VolleyCallback() {
                    @Override
                    public void onSave(String output) {

                    }

                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            Log.d("EZRA", result.toString());
                            swipeRefreshLayout.setRefreshing(false);
                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    item = result.getJSONObject(i);
                                    ListItemTransferOut listProjectEnt = new ListItemTransferOut(
                                            item.getString("ID"), item.getString("Kode_Transfer"),
                                            item.getString("Kode_Proyek"), item.getString("Kode_Item"),
                                            item.getString("Volume"),item.getString("Unit"),
                                            item.getString("FromQSPV"),item.getString("FromKode_Tower"),
                                            item.getString("FromKode_Lantai"),item.getString("FromKode_Zona"),
                                            item.getString("ToQSPV"),item.getString("ToKode_Tower"),
                                            item.getString("ToKode_Lantai"),item.getString("ToKode_Zona"),
                                            item.getString("CourierPhoto"),item.getString("StatusRequest"),
                                            item.getString("StatusAktif"),item.getString("ApprovalNo"),
                                            item.getString("Pembuat"),item.getString("WaktuBuat"),
                                            item.getString("Pengubah"),item.getString("WaktuUbah"),
                                            item.getString("Comment"),item.getString("FlagReturnGudang"),
                                            item.getString("Deskripsi"),item.getString("Nama_Tower"),
                                            item.getString("Nama_Lantai"),item.getString("Nama_Zona")
                                    );

                                    listItems.add(listProjectEnt);
                                }
                               // Log.d("EZRA2", item.toString());
                                adapter.notifyDataSetChanged();
                                //swipeRefreshLayout.setRefreshing(false);

                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void SetListViewSearch(String wherecond, String currentpage, String pagesize, String tower, String floor, String area, String material){
        swipeRefreshLayout.setRefreshing(true);
        session = new SessionManager(getApplicationContext());
        //listItems.clear();
        String whereCond = "";
        if (!wherecond.equals("")){
            whereCond = wherecond;
        }

        if(tower.equals("-"))
        {
            tower = "";
        }
        else
        {
            tower = " AND TTrans.FromKode_Tower = '"+tower+"' ";
        }

        if(floor.equals("-"))
        {
            floor = "";
        } else
        {
            floor = " AND TTrans.FromKode_Lantai = '"+floor+"' ";
        }

        if(area.equals("-"))
        {
            area = "";
        } else
        {
            area = " AND TTrans.FromKode_Zona = '"+area+"' ";
        }

        if(material.equals("-"))
        {
            material = "";
        } else
        {
            material = " AND TTrans.Kode_Item = '"+material+"' ";
        }

        controller.InqGeneral2(getApplicationContext(),"SPM_GetListTransferMaterialOut_Param",
                "@KodeProject",session.getKodeProyek(),
                "@currentpage",currentpage,
                "@pagesize",pagesize,
                "@sortby","",
                "@wherecond"," AND TTrans.Kode_Proyek = '"+session.getKodeProyek()+"' "+tower+floor+area+material,
                "@nik",String.valueOf(session.isLoggedIn()),
                "@formid","MA.MM.01",
                "@zonaid","",

                new VolleyCallback() {
                    @Override
                    public void onSave(String output) {

                    }

                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            Log.d("EZRA", result.toString());
                            swipeRefreshLayout.setRefreshing(false);
                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    item = result.getJSONObject(i);
                                    ListItemTransferOut listProjectEnt = new ListItemTransferOut(
                                            item.getString("ID"), item.getString("Kode_Transfer"),
                                            item.getString("Kode_Proyek"), item.getString("Kode_Item"),
                                            item.getString("Volume"),item.getString("Unit"),
                                            item.getString("FromQSPV"),item.getString("FromKode_Tower"),
                                            item.getString("FromKode_Lantai"),item.getString("FromKode_Zona"),
                                            item.getString("ToQSPV"),item.getString("ToKode_Tower"),
                                            item.getString("ToKode_Lantai"),item.getString("ToKode_Zona"),
                                            item.getString("CourierPhoto"),item.getString("StatusRequest"),
                                            item.getString("StatusAktif"),item.getString("ApprovalNo"),
                                            item.getString("Pembuat"),item.getString("WaktuBuat"),
                                            item.getString("Pengubah"),item.getString("WaktuUbah"),
                                            item.getString("Comment"),item.getString("FlagReturnGudang"),
                                            item.getString("Deskripsi"),item.getString("Nama_Tower"),
                                            item.getString("Nama_Lantai"),item.getString("Nama_Zona")
                                    );

                                    listItems.add(listProjectEnt);
                                }
                                // Log.d("EZRA2", item.toString());
                                adapter.notifyDataSetChanged();
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "CATCH ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.action_filter:
//                Log.d("fromrequestmaterial","noteyet");
//                break;
//            case R.id.action_sort:
//                Log.d("fromrequestmaterial","noteyet");
//                break;
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                Intent intent=new Intent(TransferMaterialActivity.this,TransferMaterialOutActivity.class);
                intent.putExtra("fromrequestmaterial","true");
                startActivityForResult(intent, request_data_from);
                break;
            case R.id.fab2:
                Toast.makeText(getApplicationContext(), "Menu Transfer Besi Coming Soon...", Toast.LENGTH_LONG).show();
                break;
        }
    }


    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            //fab2.startAnimation(fab_close);
            tvLabelNewMo.startAnimation(fab_close);
            //tvLabelFiler.startAnimation(fab_close);
            fab1.setClickable(false);
            //fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            //fab2.startAnimation(fab_open);
            tvLabelNewMo.startAnimation(fab_open);
            //tvLabelFiler.startAnimation(fab_open);
            fab1.setClickable(true);
            //fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
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

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //mode.getMenuInflater().inflate(R.menu.detilpo_item, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_filter:
                    // delete all the selected messages
                    //deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //adapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            //actionMode = getActivity().startSupportActionMode(actionModeCallback);
            actionMode = ((AppCompatActivity) getApplicationContext()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onRefresh() {
        if(isNetworkAvailable()){
            listItems.clear();
            adapter.notifyDataSetChanged();
            currentPage = 1;
            SetListView("", "1", "10");
        }
        else
        {

        }
    }

    @Override
    public void onIconClicked(int position) {

    }

    @Override
    public void onIconImportantClicked(int position) {

    }

    @Override
    public void onMessageRowClicked(int position) {
        if (adapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {

            // read the message which removes bold from the row
            ListItemTransferOut message = listItems.get(position);
            //message.setRead(true);
            listItems.set(position, message);
//            adapter.notifyDataSetChanged();

            Log.d("dadapaha", message.getID().toString());
            Intent intent = new Intent(this, ApprovalTransferMaterialActivity.class);
            intent.putExtra("IdTransfer", message.getID().toString());
            intent.putExtra("KodeTransfer", message.getKode_Transfer().toString());
            intent.putExtra("IdMaterial", message.getKode_Item().toString());
            intent.putExtra("ApprovalNumber", message.getApprovalNo().toString());
            intent.putExtra("StatusRequest", message.getStatusRequest().toString());

            //intent.putExtra("Deskripsi", message.getDeskripsi().toString());
            //intent.putExtra("IdScheduleNew", message.getIdScheduleNew().toString());
            startActivityForResult(intent, request_data_to_confirmation);

        }

    }

    @Override
    public void onRowLongClicked(int position) {

    }

}
