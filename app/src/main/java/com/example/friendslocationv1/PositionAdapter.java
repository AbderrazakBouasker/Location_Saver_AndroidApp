package com.example.friendslocationv1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder> {

    private List<Position> positionList;


    public PositionAdapter(List<Position> positionList) {
        this.positionList = positionList;
    }

    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.position_item, parent, false);
        return new PositionViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
        Position positionItem = positionList.get(position);
        holder.tvPseudo.setText("Pseudo: " + positionItem.getPseudo());
        holder.tvLatitude.setText("Latitude: " + positionItem.getLatitude());
        holder.tvLongitude.setText("Longitude: " + positionItem.getLongitude());
    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }

    static class PositionViewHolder extends RecyclerView.ViewHolder {
        TextView tvPseudo, tvLatitude, tvLongitude;
        Button btnDelete;

        public PositionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPseudo = itemView.findViewById(R.id.txt_pos_pseu);
            tvLatitude = itemView.findViewById(R.id.txt_pos_lati);
            tvLongitude = itemView.findViewById(R.id.txt_pos_long);
            btnDelete = itemView.findViewById(R.id.btn_pos_del);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}