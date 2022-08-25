package com.maxlab.storyhub;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.OnAdMetadataChangedListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxlab.storyhub.SpinWheel.LuckyWheelView;
import com.maxlab.storyhub.SpinWheel.model.LuckyItem;
import com.maxlab.storyhub.databinding.FragmentSpinnerBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.google.firebase.firestore.FieldValue.increment;


public class SpinnerFragment extends Fragment {


    public SpinnerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentSpinnerBinding binding;
    private long coin;
    List<LuckyItem> data = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpinnerBinding.inflate(inflater , container, false);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(getActivity());

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;

                    }
                });



          database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user =  documentSnapshot.toObject(User.class);

            }
        });

        final LuckyWheelView luckyWheelView = binding.luckyWheel;
        binding.spinner.setEnabled(true);
        binding.spinner.setAlpha(1f);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "0";
        luckyItem1.color = Color.parseColor("#e3e029");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "10";
        luckyItem2.color = Color.parseColor("#F14F00");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "20";
        luckyItem3.color = Color.parseColor("#E1036E");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "5";
        luckyItem4.color = ContextCompat.getColor(getContext(), R.color.Spinwell140);
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "25";
        luckyItem5.color = Color.parseColor("#03A2E1");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "50";
        luckyItem6.color = Color.parseColor("#03E150");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "15";
        luckyItem7.color = Color.parseColor("#E10318");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "100";
        luckyItem8.color = ContextCompat.getColor(getContext(), R.color.Spinwell140);
        data.add(luckyItem8);

         luckyWheelView.setData(data);
         luckyWheelView.setRound(getRandomRound());

     binding.spinner.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             int index = getRandomIndex();
             luckyWheelView.startLuckyWheelWithTargetIndex(index);
             binding.spinner.setEnabled(false);
             binding.spinner.setAlpha(.5f);

         }
     });

     luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
         @Override
         public void LuckyRoundItemSelected(int index) {
             if (index ==1 ){
                 coin = 0;
             } if (index ==2 ){
                 coin = 10;
             } if (index ==3 ){
                 coin = 20;
             } if (index ==4 ){
                 coin = 5;
             } if (index ==5){
                 coin = 25;
             } if (index ==6 ){
                 coin = 50;
             } if (index ==7 ){
                 coin = 15;
             } if (index ==8 ){
                 coin = 100; }

             InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                     new InterstitialAdLoadCallback() {
                         @Override
                         public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                             mInterstitialAd = interstitialAd;

                         }

                         @Override
                         public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                             // Handle the error
                             mInterstitialAd = null;
                             
                         }
                     });
             long currentCoins = user.getCoins();
             long points = currentCoins + coin;
             binding.spinner.setEnabled(true);
             binding.spinner.setAlpha(1f);

             if (mInterstitialAd != null) {
                 mInterstitialAd.show(getActivity());
                 Toast.makeText(getContext(), String.valueOf("+ " + coin +" Coins"), Toast.LENGTH_SHORT).show();
                 database.collection("users")
                         .document(FirebaseAuth.getInstance().getUid())
                         .update("coins", points);
             } else {
                 Toast.makeText(getContext(), "The interstitial ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
             }

         }
     });



        return binding.getRoot();
    }
    private int getRandomIndex() {
        int[] ind = new int[] {1,2,3,4,5,7};
        int rand = new Random().nextInt(ind.length);
        return ind[rand];
    }
    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

}