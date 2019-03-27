package com.example.firestorecontactchat.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.firestorecontactchat.R;
import com.example.firestorecontactchat.callback.FirebaseCallback;
import com.example.firestorecontactchat.utils.FireStoreUtil;

import java.util.ArrayList;

public class SetUserDetailActivity extends AppCompatActivity implements FirebaseCallback {
        RoundedImageView imgProfile;
        EditText edtUserName,edtAboutUser;
        Button btnFillDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_detail);

        imgProfile=findViewById(R.id.imgProfile);
        edtUserName=findViewById(R.id.edtUserName);
        edtAboutUser=findViewById(R.id.edtAboutUser);
        btnFillDetail=findViewById(R.id.btnFillDetail);

        btnFillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=edtUserName.getText().toString().trim();
                String aboutUser=edtAboutUser.getText().toString().trim();

                if(userName.isEmpty()){
                    edtUserName.setError("user name required");
                    edtUserName.requestFocus();
                    return;
                }
                if(aboutUser.isEmpty()){
                    edtAboutUser.setError("about user required");
                    edtAboutUser.requestFocus();
                    return;
                }
                FireStoreUtil.setUserDetail(userName,aboutUser,"",new ArrayList<>(),SetUserDetailActivity.this);
            }
        });
    }
    @Override
    public void startChatActivity() {
        Intent intent=new Intent(SetUserDetailActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
