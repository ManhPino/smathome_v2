package com.example.customnavigationdrawer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.customnavigationdrawer.MainActivity;
import com.example.customnavigationdrawer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private Switch swicth_lighting_fisrt,swicth_lighting_second;
    private TextView txt_temp, txt_hum;
    private ProgressBar progress_temp,progress_humi;
    public static int mValueTemperatureEsp8266;
    public static int  mValueHumidityEsp826;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        swicth_lighting_fisrt = view.findViewById(R.id.switch1);
        swicth_lighting_second = view.findViewById(R.id.switch2);
        txt_temp = view.findViewById(R.id.txt_temp);
        txt_hum = view.findViewById(R.id.txt_humi);
//        progress_temp = view.findViewById(R.id.progress_bar_temp);
//        progress_humi = view.findViewById(R.id.progress_bar_humi);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data_humi = database.getReference("humidity");
        DatabaseReference data_temp = database.getReference("temperature");
        DatabaseReference lighting_first = database.getReference("lighting_first");
        DatabaseReference lighting_second = database.getReference("lighting_second");

        swicth_lighting_fisrt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    lighting_first.setValue(1);
                }else{
                    lighting_first.setValue(0);
                }
            }
        });
        swicth_lighting_second.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    lighting_second.setValue(1);
                }else{
                    lighting_second.setValue(0);
                }
            }
        });

        data_temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mValueTemperatureEsp8266 = dataSnapshot.getValue(Integer.class);
                if(MainActivity.mTemperature != ""){
                    if(HomeFragment.mValueTemperatureEsp8266 >= Integer.parseInt(MainActivity.mTemperature) ){
                        SettingFragment.mRequestSeverFCM(mValueHumidityEsp826+"",mValueTemperatureEsp8266+"");
                    }
                }
                txt_temp.setText(mValueTemperatureEsp8266+"ºC");
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("BBB", "Failed to read value.", error.toException());
            }
        });
        data_humi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mValueHumidityEsp826 = snapshot.getValue(Integer.class);
                if(MainActivity.mHumdity != ""){
                    if( HomeFragment.mValueHumidityEsp826 >= Integer.parseInt(MainActivity.mHumdity)){
                        Log.d("ABC",MainActivity.mHumdity+HomeFragment.mValueHumidityEsp826+MainActivity.TOKEN_DEVICE);
                        SettingFragment.mRequestSeverFCM(mValueHumidityEsp826+"",mValueTemperatureEsp8266+"");
                    }
                }
                txt_hum.setText(mValueHumidityEsp826+"%");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}
