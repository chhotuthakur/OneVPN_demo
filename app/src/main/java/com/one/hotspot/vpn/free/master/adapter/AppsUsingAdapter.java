package com.one.hotspot.vpn.free.master.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.one.hotspot.vpn.free.master.model.AppModel;
import com.one.hotspot.vpn.free.master.util.ContextViewHolder;
import com.one.hotspot.vpn.free.master.R;

import org.jetbrains.annotations.NotNull;
import org.strongswan.android.logic.MainApplication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;


public class AppsUsingAdapter extends RecyclerView.Adapter<AppsUsingAdapter.ViewHolder> {

   public static final String APPS_NOT_USING_KEY = "not_allowed_apps";


   public interface CountChangeListener {

      void onCountChange(int newCount);
   }


   private CountChangeListener countChangeListener;
   private List <AppModel> apps;
   private int appsUsingCount;

   public AppsUsingAdapter(CountChangeListener countChangeListener) {
      this.countChangeListener = countChangeListener;
   }

   public void setApps(@NotNull List <AppModel> apps) {
      this.apps = apps;

      for (AppModel app : apps)
         if (app.isActive())
            appsUsingCount++;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apps_using, parent, false));
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      AppModel app = apps.get(position);
      holder.icon.setImageDrawable(app.getIcon());
      holder.activate.setText(app.getName());
      holder.activate.setChecked(app.isActive());
   }

   @Override
   public int getItemCount() {
      return apps.size();
   }

   public int getAppsUsingCount() {
      return appsUsingCount;
   }

   public void activateAll() {
      for (AppModel app : apps)
         app.setActive(true);

      notifyDataSetChanged();
      MainApplication.prefs.edit().putStringSet(APPS_NOT_USING_KEY, null).apply();
      appsUsingCount = apps.size();
      countChangeListener.onCountChange(appsUsingCount);
   }

   class ViewHolder extends ContextViewHolder implements View.OnClickListener {

      ImageView icon;
      SwitchCompat activate;

      ViewHolder(@NonNull View itemView) {
         super(itemView);

         icon = itemView.findViewById(R.id.icon);
         activate = itemView.findViewById(R.id.activate);
         activate.setOnClickListener(this);
      }

      @Override
      public void onClick(View v) {

         Set <String> inactiveApps = MainApplication.prefs.getStringSet(APPS_NOT_USING_KEY, null);
         if (inactiveApps == null)
            inactiveApps = new HashSet <>();

         int pos = getAdapterPosition();
         AppModel app = apps.get(pos);

         if (((SwitchCompat) v).isChecked()) {
            app.setActive(true);
            inactiveApps.remove(app.getPackageName());
            countChangeListener.onCountChange(++appsUsingCount);
         } else {
            app.setActive(false);
            inactiveApps.add(app.getPackageName());
            countChangeListener.onCountChange(--appsUsingCount);
         }

         MainApplication.prefs.edit().putStringSet(APPS_NOT_USING_KEY, inactiveApps).apply();
         notifyItemChanged(pos);
      }
   }
}
