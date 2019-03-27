package com.example.firestorecontactchat.callback;

import com.example.firestorecontactchat.model.UserItem;

import java.util.List;

public interface FirebaseCallback {

        default void startChatActivity(){

        }

        default void startFillDetailActivity(){

        }
        default void getUserList(List<UserItem> userList){

        }
}
