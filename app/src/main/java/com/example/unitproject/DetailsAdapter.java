package com.example.unitproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class DetailsAdapter extends FirestoreRecyclerAdapter<Details, DetailsAdapter.DetailsHolder> {

    private OnItemClickListener listener;

    public DetailsAdapter(@NonNull FirestoreRecyclerOptions<Details> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DetailsHolder detailsHolder, int position, @NonNull Details details) {
        detailsHolder.name.setText("Name : " + details.getName());
        detailsHolder.personal_no.setText("Personal No : " + details.getPersonal_number());
        detailsHolder.mobile.setText("Mobile : " + details.getMobile());
        detailsHolder.dob.setText("DOB : " + details.getDob());
        detailsHolder.address.setText("Address : " + details.getAddress());

    }

    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_details,parent,false);
        return new DetailsHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class DetailsHolder extends RecyclerView.ViewHolder {

        TextView name,personal_no,mobile,dob,address;

        public DetailsHolder(View itemview){

            super(itemview);
            name = itemview.findViewById(R.id.textview_name);
            personal_no = itemview.findViewById(R.id.textview_personal_no);
            mobile = itemview.findViewById(R.id.textview_mobile);
            dob = itemview.findViewById(R.id.textview_dob);
            address = itemview.findViewById(R.id.textview_address);

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
}
