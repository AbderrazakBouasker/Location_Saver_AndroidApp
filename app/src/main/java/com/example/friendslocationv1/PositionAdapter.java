package com.example.friendslocationv1;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendslocationv1.ui.gallery.GalleryFragment;

import org.json.JSONObject;

import java.util.HashMap;
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

    class PositionViewHolder extends RecyclerView.ViewHolder {
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
                    Download download = new Download(itemView.getContext());
                    download.execute();
                }
            });
        }

        class Download extends AsyncTask {

            Context con;
            AlertDialog alert;
            String message;

            public Download(Context con) {
                this.con=con;
            }


            @Override
            protected void onPreExecute() {//ui thread (main thread)


            }

            @Override
            protected Object doInBackground(Object[] objects) {//second thread
                //internet connection => execute service
                JSONParser parser = new JSONParser();
                int position = getAdapterPosition();
                Position p = new Position();
                p = positionList.get(position);
                HashMap<String,String> params = new HashMap<String,String>();
                params.put("idposition", String.valueOf(p.idposition));
                JSONObject response = parser.makeHttpRequest(Config.URL_DELETE_POSITION,"POST",params);



                try{
                    int success = response.getInt("success");
                    message = response.getString("message");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {//ui thread (main thread)
                //hide dialogue box show result
//            ArrayAdapter<Position> positionAdapter = new ArrayAdapter<Position>(con, android.R.layout.simple_list_item_1, data);
//            binding.lvHomePos.setAdapter(positionAdapter);
//            alert.dismiss();
                int position = getAdapterPosition();
                positionList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
            }

        }

    }
}