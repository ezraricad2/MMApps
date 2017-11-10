package com.totalbp.mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.totalbp.mm.activity.RequestMaterialActivity;
import com.totalbp.mm.activity.TransferMaterialActivity;
import com.totalbp.mm.config.SessionManager;
import com.orm.SugarContext;
import com.totalbp.mm.fragments.ProfilePictureDialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.totalbp.mm.config.AppConfig.urlProfileFromTBP;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Dialog dialog;
    private BottomNavigationView bottomNavigation;
    private View navHeader;
    private int mSelectedItem;
    private long lastBackPressTime = 0;
    private Toast toast;
    private TextView selectProject, projectSelected;
    private TextView tvTowerFloorZona, tvNamaPekerjaan, tvUserNameSideBar, tvEmailSideBar;
    private ImageView imgProfile;
    private ImageView btnTransfer, btnApproval, btnReport, btnRequest;
    private LinearLayout llRequest,llApproval, llTransfer,llReport;

    private static final int request_data_from_project_items  = 99;
    private SessionManager session;
    TextView projectName;
    public String idspk, kodespk, tower, floor, zona, namapekerjaan, namavendor, percentage;
    public  Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SugarContext.init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        selectProject = (TextView) findViewById(R.id.select_project);
        projectSelected = (TextView) findViewById(R.id.project_selected);
        dialog = new Dialog(MainActivity.this);

        Intent intent = getIntent();
        extras = intent.getExtras();
        if(extras!=null){
            if(extras.getString("nik")!=null)
            {

                session.setKodeProyek(extras.getString("projectid"));
                session.setNamaProyek(extras.getString("projectname"));

                session.createLoginSession(true,extras.getString("nik"),extras.getString("email"), "role", extras.getString("token"), extras.getString("nama"));
                Log.d("login_session_mm",extras.getString("nik")+extras.getString("email")+ "role"+ extras.getString("token")+ extras.getString("nama"));

                session.setCanView(extras.getString("toapprove"));
                session.setCanEdit(extras.getString("todelete"));
                session.setCanInsert(extras.getString("toedit"));
                session.setCanDelete(extras.getString("toinsert"));
                session.setCanApprove(extras.getString("toview"));

                Log.d("LOG.USERPRIVILEGE","To Approve : "+extras.getString("toapprove")+ " To Delete :" +extras.getString("todelete")+ " To Edit :"+ extras.getString("toedit")+ " To Insert :" + extras.getString("toinsert")+ " To View : "+ extras.getString("toview"));

            }
//            Toast.makeText(getApplicationContext(),extras.getString("nik"),Toast.LENGTH_SHORT).show();
        }
        else{
            Intent inent = new Intent("com.totalbp.cis.main_action");
            PackageManager pm = getPackageManager();
            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(inent, PackageManager.GET_RESOLVED_FILTER);
            if(resolveInfos.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Application Not Installed", Toast.LENGTH_SHORT).show();
                Log.i("NoneResolved", "No Activities");
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Please using CIS launcher to use the apps")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }}).show();
            } else {
                Log.i("Resolved!", "There are activities" + resolveInfos);
                startActivity(inent);
                this.finish();
            }
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        tvUserNameSideBar = (TextView) navHeader.findViewById(R.id.tv_UsernameSideBar);
        tvEmailSideBar = (TextView) navHeader.findViewById(R.id.tv_EmailSideBar);
        imgProfile = (ImageView) navHeader.findViewById(R.id.imageProfile);

        btnRequest = (ImageView) findViewById(R.id.btnRequest);
        btnApproval = (ImageView) findViewById(R.id.btnApproval);
        btnTransfer = (ImageView) findViewById(R.id.btnTransfer);
        btnReport = (ImageView) findViewById(R.id.btnReport);

        btnRequest.setColorFilter(Color.parseColor("#3F51B5"));
        btnApproval.setColorFilter(Color.parseColor("#3F51B5"));
        btnTransfer.setColorFilter(Color.parseColor("#3F51B5"));
        btnReport.setColorFilter(Color.parseColor("#3F51B5"));

        llRequest = (LinearLayout) findViewById(R.id.llRequest);
        llApproval = (LinearLayout) findViewById(R.id.llApproval);
        llTransfer = (LinearLayout) findViewById(R.id.llTransfer);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        // holder.imgProfile.setColorFilter(message.getColor());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (!session.getKodeProyek().equals("")) {
            projectSelected.setText(session.getNamaProyek());
        }

        tvUserNameSideBar.setText(session.getUserName());
        tvEmailSideBar.setText(session.getUserEmail());

        Glide.with(getApplicationContext()).load(urlProfileFromTBP+session.getUserDetails().get("nik")+".jpg")
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imgProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imgProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", urlProfileFromTBP+session.getUserDetails().get("nik")+".jpg");
                bundle.putInt("position", 0);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ProfilePictureDialogFragment newFragment = ProfilePictureDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });

        llRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RequestMaterialActivity.class);
                intent.putExtra("idpo","");
                startActivityForResult(intent, 10);
            }
        });

        llTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TransferMaterialActivity.class);
                intent.putExtra("idpo","");
                startActivityForResult(intent, 11);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);


        if( requestCode == request_data_from_project_items ) {
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


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("list_items.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.action_filter:



                break;
            case R.id.action_sort:



                break;
        }
        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< bottomNavigation.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigation.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

//        updateToolbarText(item.getTitle());

        if (frag != null) {
            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.replace(R.id.fragmentContainer, frag, frag.getTag());
            //ft.commit();

        }
    }

    /*fungsi percobaan untuk membuat project blank di layout fragment
    private void selectFragmentCustom(String value) {
        // update the main content by replacing fragments
        if (session.getKodeProyek().equals("") || projectName.getText().equals(getString(R.string.select_project_hint))){
            value = "blank";
        }
        Fragment fragment = null;
        switch (value)
        {
            case "blank":
                fragment = new ProjectBlankFragment();
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
    */


    //private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new FragmentWorkInstruction(), "Work Instruction");
//        adapter.addFragment(new FragmentDailyProgress(), "Daily Progress");

//        viewPager.setAdapter(adapter);
//    }




    @Override
    public void onBackPressed() {
        FragmentManager fm = this.getSupportFragmentManager();
//        getFragmentManager().popBackStack();
//        Toast.makeText(getApplicationContext(),"pressed",Toast.LENGTH_SHORT).show();
//        setDrawerState(true);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//        FragmentManager mgr = getSupportFragmentManager();
//
//        if (mgr.getBackStackEntryCount() > 0)
//            mgr.popBackStack();
//        else
//            setDrawerState(true);
//            super.onBackPressed();
        if (isTaskRoot() && fm.getBackStackEntryCount()==0){
            if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
                toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_SHORT);
                toast.show();
                this.lastBackPressTime = System.currentTimeMillis();
            } else {
                if (toast != null) {
                    toast.cancel();
                }
                super.onBackPressed();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void logoutUser() {
        session.RemoveSession();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
