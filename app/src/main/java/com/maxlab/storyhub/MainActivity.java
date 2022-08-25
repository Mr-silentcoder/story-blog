package com.maxlab.storyhub;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.maxlab.storyhub.databinding.ActivityMainBinding;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();
        binding.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.userContent, new ProfileFragment());
                binding.userContent.setVisibility(View.VISIBLE);
                binding.userContent2.setVisibility(View.INVISIBLE);
                binding.instraBtn.setVisibility(View.VISIBLE);
                binding.instraClose.setVisibility(View.INVISIBLE);
                binding.userProfile.setVisibility(View.INVISIBLE);
                binding.closeBtn.setVisibility(View.VISIBLE);
                transaction.commit();
            }
        });

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.userContent.setVisibility(View.INVISIBLE);
                binding.userProfile.setVisibility(View.VISIBLE);
                binding.closeBtn.setVisibility(View.INVISIBLE);
            }
        });


        binding.instraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.userContent2, new instructions());
                binding.userContent2.setVisibility(View.VISIBLE);
                binding.userContent.setVisibility(View.INVISIBLE);
                binding.userProfile.setVisibility(View.VISIBLE);
                binding.closeBtn.setVisibility(View.INVISIBLE);
                binding.instraBtn.setVisibility(View.INVISIBLE);
                binding.instraClose.setVisibility(View.VISIBLE);
                transaction1.commit();
            }
        });

        binding.instraClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.userContent2.setVisibility(View.INVISIBLE);
                binding.instraBtn.setVisibility(View.VISIBLE);
                binding.instraClose.setVisibility(View.INVISIBLE);
            }
        });



        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i){
                    case 0:
                        transaction.replace(R.id.content, new HomeFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.content, new SpinnerFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.content, new WalletFragment());
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.content, new VideoFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });

        
    }

}