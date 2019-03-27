package com.example.firestorecontactchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firestorecontactchat.R;
import com.example.firestorecontactchat.model.User;
import com.example.firestorecontactchat.model.UserItem;
import com.example.firestorecontactchat.views.RoundedImageView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    List<UserItem> userList;
    Context context;

    public ContactAdapter(List<UserItem> userList){
        this.userList=userList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_contact_item,viewGroup,false);
        context=viewGroup.getContext();
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user=userList.get(i).getUser();
        viewHolder.txtName.setText(user.getName());
        if(!user.getBio().equals("")) {
            viewHolder.txtBio.setText(user.getBio());
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundedImageView;
        TextView txtName,txtBio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView=itemView.findViewById(R.id.imageView_profile_picture);
            txtName=itemView.findViewById(R.id.textView_name);
            txtBio=itemView.findViewById(R.id.textView_bio);
        }
    }
}
