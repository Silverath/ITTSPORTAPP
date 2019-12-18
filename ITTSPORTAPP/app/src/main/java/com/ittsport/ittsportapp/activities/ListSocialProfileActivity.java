package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.CardFragmentPagerAdapter;

public class ListSocialProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_social_profile);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);

        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        int dip = 16;
        Resources r = getResources();
        float px = dpToPixels(dip, this);
        viewPager.setPageMargin( (int) px);
    }

    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}

