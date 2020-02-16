package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.CardFragmentPagerAdapter;
import com.ittsport.ittsportapp.utils.CardPagerAdapter;
import com.ittsport.ittsportapp.utils.ShadowTransformer;

public class ListSocialProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private boolean mShowingFragments = false;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_choose_social_profile);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_list_profile);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        FloatingActionButton newSocialProfile = (FloatingActionButton) findViewById(R.id.btn_nuevo_social_profile);

        mCardAdapter = new CardPagerAdapter();
        db.collection("perfilesSociales").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        PerfilSocial perfil = document.toObject(PerfilSocial.class);
                        mCardAdapter.addCardItem(perfil);
                    }
                    mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                            dpToPixels(2, context));

                    mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                    mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
                    tabLayout.setupWithViewPager(mViewPager, true);

                    mViewPager.setAdapter(mCardAdapter);
                    mViewPager.setPageTransformer(false, mCardShadowTransformer);
                    mViewPager.setOffscreenPageLimit(3);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });


        newSocialProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextActivity();
            }
        });

        //Toast.makeText(ListSocialProfileActivity.this, "Click", Toast.LENGTH_LONG);
    }

    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public void goToNextActivity(){
        Context context = ListSocialProfileActivity.this;
        Intent startNewSocialProfileActivityClass = new Intent(context, NewSocialProfileActivity.class);
        startActivity(startNewSocialProfileActivityClass);
    }


}

