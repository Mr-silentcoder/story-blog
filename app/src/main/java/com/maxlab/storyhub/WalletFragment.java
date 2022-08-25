package com.maxlab.storyhub;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxlab.storyhub.databinding.FragmentWalletBinding;


public class WalletFragment extends Fragment {


    public WalletFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;
    ProgressDialog dialog;
    private InterstitialAd mInterstitialAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading");
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(getActivity());

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
        dialog.show();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               user =  documentSnapshot.toObject(User.class);
               binding.currentCoins.setText(String.valueOf(user.getCoins()));
               dialog.dismiss();
            }
        });

        binding.sendRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                   dialog.show();
                if (user.getCoins() > 50000){
                    String uid = FirebaseAuth.getInstance().getUid();
                    String paypal = binding.payEmail.getText().toString();

                    WithdrawRequest request = new WithdrawRequest(uid, paypal, user.getName());
                    if(paypal.equals("")){
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Please enter a  email address", Toast.LENGTH_SHORT).show();
                    }else if(!Patterns.EMAIL_ADDRESS.matcher(paypal).matches()){
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    }else {
                        database.collection("withdraw")
                                .document(uid)
                                .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Request Send Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText(getContext(), "You Need More Coins to get Withdraw",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}