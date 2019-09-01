package com.example.unitproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private Context mCtx;
    private List<Details> detailsList;

    public DetailsAdapter(Context mCtx, List<Details> detailsList) {
        this.mCtx = mCtx;
        this.detailsList = detailsList;

    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_details, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        Details details = detailsList.get(position);
        holder.textViewName.setText(details.getName());
        holder.textViewPersonal.setText(details.getPersonal_number());
        holder.textViewMobile.setText((int) details.getMobile());
        holder.textViewDob.setText("INR " + details.getDob());
        holder.textViewAddress.setText("Available Units: " + details.getAddress());


    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName, textViewPersonal, textViewMobile, textViewDob, textViewAddress;


        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textview_name);
            textViewPersonal = itemView.findViewById(R.id.textview_personal_no);
            textViewMobile = itemView.findViewById(R.id.textview_mobile);
            textViewDob = itemView.findViewById(R.id.textview_dob);
            textViewAddress = itemView.findViewById(R.id.textview_address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Details details = detailsList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx, ShowDetailsActivity.class);
            intent.putExtra("details", (Serializable) details);
            mCtx.startActivity(intent);
        }
    }
}
