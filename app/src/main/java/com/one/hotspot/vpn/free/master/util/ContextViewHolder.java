package com.one.hotspot.vpn.free.master.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;


public class ContextViewHolder extends RecyclerView.ViewHolder {

   public ContextViewHolder(@NonNull View itemView) {
      super(itemView);
   }

   @NonNull
   public Context getContext() {
      return itemView.getContext();
   }

   @NonNull
   public Resources getResources() {
      return itemView.getResources();
   }

   @NonNull
   public String getString(@StringRes int resId) {
      return itemView.getResources().getString(resId);
   }

   @NonNull
   public String getString(@StringRes int resId, Object... formatArg) {
      return itemView.getResources().getString(resId, formatArg);
   }
}
