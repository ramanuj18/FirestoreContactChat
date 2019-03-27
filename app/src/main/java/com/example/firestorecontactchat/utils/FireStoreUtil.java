package com.example.firestorecontactchat.utils;

import com.example.firestorecontactchat.callback.FirebaseCallback;
import com.example.firestorecontactchat.model.User;
import com.example.firestorecontactchat.model.UserItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

public class FireStoreUtil {
    static FirebaseFirestore firestoreInstance=FirebaseFirestore.getInstance();
    static DocumentReference currentDocRef;
    public static void initCurrentUserIfFirstTime(FirebaseCallback firebaseCallback){
        currentDocRef=firestoreInstance.document("users/"+FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        currentDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    firebaseCallback.startFillDetailActivity();
                }else {
                    firebaseCallback.startChatActivity();
                }
            }
        });
    }
    public static void setUserDetail(String name, String bio, String profilePath, List<String> tokenList,FirebaseCallback firebaseCallback){
        User user=new User(name,bio,profilePath,true,Calendar.getInstance().getTime(),tokenList);
        currentDocRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseCallback.startChatActivity();
            }
        });
    }
    public static ListenerRegistration getAllUsers(FirebaseCallback firebaseCallback){
        return firestoreInstance.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }
                        List<UserItem> userList=new ArrayList<>();
                        queryDocumentSnapshots.getDocuments().forEach(new Consumer<DocumentSnapshot>() {
                            @Override
                            public void accept(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot != null){
                                 //   if(!documentSnapshot.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                                        userList.add(new UserItem(documentSnapshot.toObject(User.class), documentSnapshot.getId()));
                                   // }
                                }
                            }
                        });
                        firebaseCallback.getUserList(userList);
                    }
                });
    }
    public static void removeListener(ListenerRegistration listenerRegistration){
        listenerRegistration.remove();
    }
}
