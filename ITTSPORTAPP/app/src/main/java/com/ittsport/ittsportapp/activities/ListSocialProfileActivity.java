package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.adapters.CardFragmentPagerAdapterSocialProfile;
import com.ittsport.ittsportapp.adapters.CardPagerAdapterSocialProfile;
import com.ittsport.ittsportapp.utils.ShadowTransformer;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import javax.annotation.Nonnull;

public class ListSocialProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private boolean mShowingFragments = false;
    private ViewPager mViewPager;
    private CardPagerAdapterSocialProfile mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapterSocialProfile mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private TabLayout tabLayout;
    private int LAUNCH_CREATE_SOCIAL_PROFILE_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_choose_social_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mCardAdapter = new CardPagerAdapterSocialProfile(this);
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        db.collection("perfilesSociales").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("estado", Estado.ACEPTADO).whereEqualTo("escuelaId", sharedPreferences.getEscuelaSeleccionada()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        PerfilSocial perfil = document.toObject(PerfilSocial.class);
                        perfil.setId(document.getId());
                        mCardAdapter.addCardItem(perfil);
                    }
                    mFragmentCardAdapter = new CardFragmentPagerAdapterSocialProfile(getSupportFragmentManager(),
                            dpToPixels(2, context));

                    mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                    mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
                    tabLayout.setupWithViewPager(mViewPager, true);

                    mViewPager.setAdapter(mCardAdapter);
                    mViewPager.setPageTransformer(false, mCardShadowTransformer);
                    mViewPager.setOffscreenPageLimit(3);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });
    }

    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CREATE_SOCIAL_PROFILE_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                PerfilSocial nuevo = (PerfilSocial) data.getSerializableExtra("perfilSocial");
                mCardAdapter.addCardItem(nuevo);
                mCardAdapter.notifyDataSetChanged();
            }
        }
    }

    public void goToNextActivity(){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Context context = ListSocialProfileActivity.this;
        Intent startNewSocialProfileActivityClass = new Intent(context, NewSocialProfileActivity.class);
        startActivityForResult(startNewSocialProfileActivityClass, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    public void onBackPressed() {

    }
}

