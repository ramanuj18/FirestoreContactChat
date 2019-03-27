package com.example.firestorecontactchat.views;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.firestorecontactchat.R;
import com.example.firestorecontactchat.adapter.ContactAdapter;
import com.example.firestorecontactchat.callback.FirebaseCallback;
import com.example.firestorecontactchat.model.UserItem;
import com.example.firestorecontactchat.utils.FireStoreUtil;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ContactActivity extends AppCompatActivity implements FirebaseCallback {
    ListenerRegistration userListListener;
    List<String> contactList;
    List<UserItem> registreredUserList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

       // Toast.makeText(this, ""+contactList.size(), Toast.LENGTH_SHORT).show();

       /* if(PhoneNumberUtils.compare("+917566361397","07566361397")){
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }*/

    }
    private List<String> getAllContacts(){
        List<String> contactList=new ArrayList<>();

        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                int hasPhoneNumber=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if(hasPhoneNumber>0){
                    String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor phoneCursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",new String[]{id},null);
                    if(phoneCursor.moveToNext()){
                        String phoneNumber=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactList.add(phoneNumber);
                    }
                    phoneCursor.close();
                }
            }
        }
        return contactList;
    }
    @Override
    protected void onStart() {
        super.onStart();
        contactList=getAllContacts();
        userListListener=FireStoreUtil.getAllUsers(this);

    }
    @Override
    public void getUserList(List<UserItem> userList) {
        registreredUserList=new ArrayList<>();
        Log.d("userList",""+userList.size());
        userList.forEach(new Consumer<UserItem>() {
          @Override
          public void accept(UserItem userItem) {
              String mob=userItem.getUserMobile();
              contactList.forEach(new Consumer<String>() {
                  @Override
                  public void accept(String s) {
                      if(PhoneNumberUtils.compare(mob,s)){
                          registreredUserList.add(userItem);
                      }
                  }
              });
          }
      });
      if(registreredUserList.size()!=0){
          ContactAdapter adapter=new ContactAdapter(registreredUserList);
          recyclerView.setAdapter(adapter);
      }
     //Toast.makeText(this, "useritemlist"+registreredUserList.size(), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        FireStoreUtil.removeListener(userListListener);
    }
}
