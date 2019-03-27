package com.example.firestorecontactchat.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firestorecontactchat.R;
import com.example.firestorecontactchat.callback.FirebaseCallback;
import com.example.firestorecontactchat.model.CountryData;
import com.example.firestorecontactchat.utils.FireStoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity  extends AppCompatActivity implements FirebaseCallback {

    EditText edtPhoneNumber,edtOtp;
    Button btnVerify,btnSingIn;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    String number;
    String verificationId;
    FirebaseAuth mAuth;
    Spinner spinnerCountryList;
    TextView txtCountryCode;
    String countryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varify_phone);

        edtPhoneNumber=findViewById(R.id.edtPhoneNumber);
        edtOtp=findViewById(R.id.edtOtp);
        btnVerify=findViewById(R.id.btnVerify);
        btnSingIn=findViewById(R.id.btnSignIn);
        progressBar=findViewById(R.id.progressBar);
        linearLayout=findViewById(R.id.linearLayout);
        txtCountryCode=findViewById(R.id.txtCountryCode);
        linearLayout.setVisibility(View.GONE);

        spinnerCountryList=findViewById(R.id.spinnerCountryList);
        spinnerCountryList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        spinnerCountryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryCode="+"+CountryData.countryAreaCodes[i];
                txtCountryCode.setText(countryCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAuth=FirebaseAuth.getInstance();
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=edtPhoneNumber.getText().toString().trim();
                String numWithCode=countryCode+number;

                if(number.isEmpty()|| number.length()<10){
                    edtPhoneNumber.setError("valid mobile number is required");
                    edtPhoneNumber.requestFocus();
                    return;
                }
                linearLayout.setVisibility(View.VISIBLE);
                sendVerificationCode(numWithCode);
            }
        });
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=edtOtp.getText().toString().trim();
                if(otp.isEmpty()||otp.length()<6){
                    edtOtp.setError("enter valid OTP");
                }else {
                    verifyCode(otp);
                }
            }
        });

    }
    private void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FireStoreUtil.initCurrentUserIfFirstTime(VerifyPhoneActivity.this);
                          /*  Intent intent=new Intent(VerifyPhoneActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                        }else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void sendVerificationCode(String mobileNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNumber,60,TimeUnit.SECONDS,TaskExecutors.MAIN_THREAD,mCallback);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                edtOtp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    @Override
    public void startChatActivity() {
        Intent intent=new Intent(VerifyPhoneActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void startFillDetailActivity() {
        Intent intent=new Intent(VerifyPhoneActivity.this,SetUserDetailActivity.class);
        startActivity(intent);
    }
}
