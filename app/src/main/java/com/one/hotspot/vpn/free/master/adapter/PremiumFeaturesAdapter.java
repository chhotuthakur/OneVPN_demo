
package com.one.hotspot.vpn.free.master.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.one.hotspot.vpn.free.master.model.PremiumFeature;
import com.one.hotspot.vpn.free.master.util.Logger;
import com.one.hotspot.vpn.free.master.R;

import java.util.List;

public class PremiumFeaturesAdapter extends PagerAdapter {

    private Context context;
    private List<PremiumFeature> featureList;

    public PremiumFeaturesAdapter(Context context, List<PremiumFeature> featureList) {
        this.context = context;
        this.featureList = featureList;
    }

    @Override
    public int getCount() {
        return featureList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.premium_feature_list_item, container, false);

        PremiumFeature feature = featureList.get(position);

        ImageView icon = itemView.findViewById(R.id.featureIcon);
        TextView title = itemView.findViewById(R.id.featureTitle);
        TextView description = itemView.findViewById(R.id.featureDescription);

        title.setText(feature.getTitle());
        description.setText(feature.getDescription());
        if (feature.getIconResourceId() !=null) {
            icon.setImageDrawable(ContextCompat.getDrawable(context, feature.getIconResourceId()));
        }

        container.addView(itemView);
        Logger.INSTANCE.d("PremiumActivity", "item------------------------------");
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
