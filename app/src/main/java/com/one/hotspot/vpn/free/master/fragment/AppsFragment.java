package com.one.hotspot.vpn.free.master.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.one.hotspot.vpn.free.master.R;
import com.one.hotspot.vpn.free.master.adapter.AppsUsingAdapter;
import com.one.hotspot.vpn.free.master.model.AppModel;

import org.strongswan.android.logic.MainApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public class AppsFragment extends Fragment implements AppsUsingAdapter.CountChangeListener {

    View view;
    Button reset;
    RecyclerView recyclerView;
    TextView sActiveApp;
    ConstraintLayout mainlay;
    AppsUsingAdapter appsUsingAdapter = new AppsUsingAdapter(this);
    Activity mactivity;


    public AppsFragment() {
    }

    public AppsFragment(Activity activity) {

        this.mactivity=activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_apps, container, false);
        initViews();

        return view;
    }

    void initViews(){

        mainlay=view.findViewById(R.id.container);
        reset=view.findViewById(R.id.reset);
        sActiveApp=view.findViewById(R.id.sActiveApps);
        recyclerView=view.findViewById(R.id.rvAppsList);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appsUsingAdapter.activateAll();
            }
        });

        new AsyncTask<Void,Void,List<AppModel>>(){


            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(getContext(),R.style.AppCompatAlertDialogStyle);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected List<AppModel> doInBackground(Void... voids) {

                if(getActivity()!=null)
                  return  getApps(mactivity);
                else
                    return null;

            }

            @Override
            protected void onPostExecute(List <AppModel> appModels) {
                super.onPostExecute(appModels);
                if(appModels!=null){

                    Log.e("appmodel",""+appModels.size());
                    appsUsingAdapter.setApps(appModels);
                    recyclerView.setAdapter(appsUsingAdapter);
                    mainlay.setVisibility(View.VISIBLE);
                    String s = "Active apps ("+appModels.size()+"/"+appsUsingAdapter.getAppsUsingCount()+")";
                    sActiveApp.setText(s);

                }
                progressDialog.dismiss();
            }
        }.execute();

    }

    private List<AppModel> getApps(Activity activity){

        List<AppModel> apps = new ArrayList <>();

        Set<String> inactive = MainApplication.prefs.getStringSet("not_allowed_apps",null);

        List <PackageInfo> packageInfoList = activity.getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);

        Collections.sort(packageInfoList, new Comparator <PackageInfo>() {
            @Override
            public int compare(PackageInfo packageInfo, PackageInfo t1) {
                return packageInfo.applicationInfo.loadLabel(activity.getPackageManager()).toString().toLowerCase().compareTo(t1.applicationInfo.loadLabel(activity.getPackageManager()).toString().toLowerCase());
            }
        });

        Log.e("packages",""+packageInfoList.size());

        for(PackageInfo packageInfo: packageInfoList){
            if(packageInfo.requestedPermissions!=null) {
                for (String permissionInfo : packageInfo.requestedPermissions) {
                    if (TextUtils.equals(permissionInfo, Manifest.permission.INTERNET)) {
                        apps.add(new AppModel(packageInfo.applicationInfo.loadLabel(activity.getPackageManager()).toString(), packageInfo.packageName, packageInfo.applicationInfo.loadIcon(activity.getPackageManager())));
                        break;
                    }
                }
            }
        }

            for (AppModel app : apps) {
                if(inactive!=null) {
                    if (inactive.contains(app.getPackageName()))
                        app.setActive(false);
                }

            }

            return apps;


    }

    @Override
    public void onCountChange(int newCount) {

        String s = "Active apps ("+newCount+"/"+appsUsingAdapter.getAppsUsingCount()+")";
        sActiveApp.setText(s);
    }
}