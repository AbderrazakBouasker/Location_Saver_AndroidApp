package com.example.friendslocationv1.ui.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.friendslocationv1.Config;
import com.example.friendslocationv1.JSONParser;
import com.example.friendslocationv1.Position;
import com.example.friendslocationv1.R;
import com.example.friendslocationv1.databinding.FragmentGalleryBinding;
import com.example.friendslocationv1.ui.home.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class GalleryFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private FragmentGalleryBinding binding;
    int FinePermissionCode = 1;
    Location currentlocation;
    FusedLocationProviderClient client;
    GoogleMap googleMap;
    Marker previousMarker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(this.requireContext());
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        binding.btnAddGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //download data


                GalleryFragment.Download download = new GalleryFragment.Download(GalleryFragment.this.getActivity());
                download.execute();
                if (previousMarker != null) {
                    previousMarker.remove();
                }
                binding.ptLatitudeGallery.setText(String.valueOf(currentlocation.getLatitude()));
                binding.ptLongitudeGallery.setText(String.valueOf(currentlocation.getLongitude()));
                binding.ptPseudoGallery.setText("My Location");


            }
        });

        binding.btnInitGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ptLatitudeGallery.setText(String.valueOf(currentlocation.getLatitude()));
                binding.ptLongitudeGallery.setText(String.valueOf(currentlocation.getLongitude()));
                binding.ptPseudoGallery.setText("My Location");
                if (previousMarker != null) {
                    previousMarker.remove();
                }
            }
        });

        return root;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FinePermissionCode);
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentlocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(GalleryFragment.this);
                    }
                }
            }
        });
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLastLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()))
                .title("My Location"));
        binding.ptLongitudeGallery.setText(String.valueOf(currentlocation.getLongitude()));
        binding.ptLatitudeGallery.setText(String.valueOf(currentlocation.getLatitude()));
        this.googleMap.setOnMapClickListener(this);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FinePermissionCode) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this.requireContext(),"Permission Denied need to accept it",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (previousMarker != null) {
            previousMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Selected Location");
        previousMarker = googleMap.addMarker(markerOptions);
        binding.ptLongitudeGallery.setText(String.valueOf(latLng.longitude));
        binding.ptLatitudeGallery.setText(String.valueOf(latLng.latitude));
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
            // show dialogue box
//            data.clear();
//            AlertDialog.Builder builder = new AlertDialog.Builder(con);
//            builder.setTitle("Downloading");
//            builder.setMessage("Please wait... :)");
//            alert = builder.create();
//            alert.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {//second thread
            //internet connection => execute service
            JSONParser parser = new JSONParser();

            HashMap<String,String> params = new HashMap<String,String>();
            String longitude = binding.ptLongitudeGallery.getText().toString();
            String latitude = binding.ptLatitudeGallery.getText().toString();
            String pseudo = binding.ptPseudoGallery.getText().toString();
            params.put("longitude",longitude);
            params.put("latitude",latitude);
            params.put("pseudo",pseudo);
            JSONObject response = parser.makeHttpRequest(Config.URL_ADD_POSITION,"POST",params);



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
            Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
        }

    }


}