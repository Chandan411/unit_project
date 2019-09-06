package com.example.unitproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.Serializable;
import java.util.List;

public class DetailsAdapter extends FirestoreRecyclerAdapter<Details, DetailsAdapter.DetailsHolder> {

    public DetailsAdapter(@NonNull FirestoreRecyclerOptions<Details> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DetailsHolder detailsHolder, int position, @NonNull Details details) {
        detailsHolder.name.setText(details.getName());
        detailsHolder.address.setText(details.getAddress());
    }

    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_details,parent,false);
        return new DetailsHolder(v);
    }

    class DetailsHolder extends RecyclerView.ViewHolder {

        TextView name,personal_no,mobile,dob,address;

        public DetailsHolder(View itemview){

            super(itemview);
            name = itemview.findViewById(R.id.textview_name);
            address = itemview.findViewById(R.id.textview_address);

        }
    }
}
