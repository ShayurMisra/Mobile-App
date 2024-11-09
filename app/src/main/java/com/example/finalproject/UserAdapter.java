package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserModel> userList; // Use UserModel instead of User

    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserModel user = userList.get(position); // Use UserModel
        holder.fullNameTextView.setText(user.getFullName());
        holder.emailTextView.setText(user.getUserEmail());
        holder.userTypeTextView.setText(user.getIsUser() ? "User" : "Admin");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView fullNameTextView, emailTextView, userTypeTextView;

        public UserViewHolder(View view) {
            super(view);
            fullNameTextView = view.findViewById(R.id.fullName);
            emailTextView = view.findViewById(R.id.email);
            userTypeTextView = view.findViewById(R.id.userType);
        }
    }
}
