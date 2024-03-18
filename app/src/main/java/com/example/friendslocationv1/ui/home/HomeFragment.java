package com.example.friendslocationv1.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendslocationv1.Config;
import com.example.friendslocationv1.JSONParser;
import com.example.friendslocationv1.Position;
import com.example.friendslocationv1.PositionAdapter;
import com.example.friendslocationv1.R;
import com.example.friendslocationv1.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<Position> data = new ArrayList<Position>();
    private RecyclerView recyclerView;
    private PositionAdapter positionAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnHomeDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //download data


                Download download = new Download(HomeFragment.this.getActivity());
                download.execute();

//                recyclerView = root.findViewById(R.id.recyclerView);
//                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//                positionAdapter = new PositionAdapter(data);
//                recyclerView.setAdapter(positionAdapter);


            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class Download extends AsyncTask {

        Context con;
        AlertDialog alert;

        public Download(Context con) {
            this.con=con;
        }


        @Override
        protected void onPreExecute() {//ui thread (main thread)
            // show dialogue box
            data.clear();
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("Downloading");
            builder.setMessage("Please wait... :)");
            alert = builder.create();
            alert.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {//second thread
            //internet connection => execute service
            JSONObject response = new JSONObject();
            try {
            JSONParser parser = new JSONParser();
            response = parser.makeHttpRequest(Config.URL_GET_ALL,"GET",null);
            }catch (Exception e){
                System.out.println("connection problem");
            }

            //add
//            HashMap<String,String> params = new HashMap<String,String>();
//            params.put("longitude","123");
//            params.put("latitude","123");
//            params.put("pseudo","foulen");
//            JSONObject response = parser.makeHttpRequest(Config.URL_ADD_POSITION,"POST",params);


            try{
                int success = response.getInt("success");
                if(success == 0){
                    String message = response.getString("message");
                }else {
                    JSONArray table = response.getJSONArray("positions");
                    for (int i = 0; i < table.length(); i++) {
                        JSONObject line = table.getJSONObject(i);
                        int id = line.getInt("idposition");
                        String longitude = line.getString("longitude");
                        String latitude = line.getString("latitude");
                        String pseudo = line.getString("pseudo");
                        Position position = new Position(id,longitude,latitude,pseudo);
                        data.add(position);

                    }
                }
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
            recyclerView = requireView().findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            positionAdapter = new PositionAdapter(data);
            recyclerView.setAdapter(positionAdapter);
            alert.dismiss();
        }

    }

}