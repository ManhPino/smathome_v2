package com.example.customnavigationdrawer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.customnavigationdrawer.MainActivity;
import com.example.customnavigationdrawer.R;
import com.example.customnavigationdrawer.api.RetrofitConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    private EditText edt_humi,edt_temp,edt_timestart,edt_timeend,edt_dateend;
    private SwitchCompat switchCompat1,switchCompat2;
    private TextView txt_setting_humi,txt_setting_temp,txt_schedule_timedevice;
    private static boolean checkTime = false;
    public static LocalTime currentHM;
    public String mTimeStart,mTimeEnd ="";
    public CheckBox mCheckWater1, mCheckWater2,mCbxWaterHome1,mCbxWaterHome2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        initView(view);



        isCheckBetween();

        switchCompat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==false){
                    return;
                }else{
                    openDialog_settingNotify();
                }
            }
        });
        switchCompat2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==false){
                   turnOffWaterPump();
                }else{
                    openDialog_settingDevice();
                }
            }
        });
        return view;
    }
    private void initView(View view){

        txt_setting_humi = view.findViewById(R.id.txt_setting_humi);
        txt_setting_temp = view.findViewById(R.id.txt_setting_temp);

        txt_setting_humi.setText(MainActivity.mHumdity);
        txt_setting_temp.setText(MainActivity.mTemperature);

        txt_schedule_timedevice = view.findViewById(R.id.txt_schedule_timedevice);

        switchCompat1 = view.findViewById(R.id.switchCompat);
        switchCompat2 = view.findViewById(R.id.switchCompat2);

        switchCompat1.setChecked(false);
        switchCompat2.setChecked(false);

        mCbxWaterHome1 = view.findViewById(R.id.cbx_water_home1);
        mCbxWaterHome2 = view.findViewById(R.id.cbx_water_home2);
        mCbxWaterHome1.setChecked(false);
        mCbxWaterHome2.setChecked(false);
        mCbxWaterHome1.setEnabled(false);
        mCbxWaterHome2.setEnabled(false);
    }

    private void openDialog_settingDevice(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_settingdevice, null);
        edt_timestart = view.findViewById(R.id.timestart_device);
        edt_timeend = view.findViewById(R.id.timeend_device);

        mCheckWater1 = view.findViewById(R.id.cbx_water1);
        mCheckWater2 = view.findViewById(R.id.cbx_water2);

        builder.setView(view)
                .setTitle("Setting")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTimeStart = edt_timestart.getText().toString().trim();
                        mTimeEnd  = edt_timeend.getText().toString().trim();
                        txt_schedule_timedevice.setText(mTimeStart+" |"+mTimeEnd+" |");
                        if(mCheckWater1.isChecked() && mCheckWater2.isChecked()){
                            mCbxWaterHome1.setChecked(true);
                            mCbxWaterHome2.setChecked(true);
                        }else if(mCheckWater1.isChecked() && mCheckWater2.isChecked() ==false){
                            mCbxWaterHome1.setChecked(true);
                            mCbxWaterHome2.setChecked(false);
                        }else if(mCheckWater1.isChecked()==false && mCheckWater2.isChecked()){
                            mCbxWaterHome1.setChecked(false);
                            mCbxWaterHome2.setChecked(true);
                        }
                    }
                });
        builder.create().show();
    }
    public void isCheckBetween(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference timecurrent = database.getReference("timecurrent");

        timecurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(mTimeStart =="" && mTimeEnd ==""){
                        return;
                    }
                    if(mTimeStart != "" && mTimeEnd != ""){
                        LocalTime starts = LocalTime.parse(mTimeStart);
                        LocalTime ends= LocalTime.parse(mTimeEnd);
                        currentHM = LocalTime.parse(snapshot.getValue(String.class));
                        if(currentHM.isBefore(ends) && currentHM.isAfter(starts)) {
                            checkTime = true;
                        }else {
                            checkTime = false;
                        }
                        if(checkTime == true){
                            if(mCbxWaterHome1.isChecked() && mCbxWaterHome2.isChecked()){
                                turnOnWaterPump(true,true);
                            }
                            if(mCbxWaterHome1.isChecked() && mCbxWaterHome2.isChecked() == false){
                                turnOnWaterPump(true,false);
                            }
                            if(mCbxWaterHome1.isChecked() == false && mCbxWaterHome2.isChecked()){
                                turnOnWaterPump(false,true);
                            }
                        }else if(checkTime == false){
                            turnOffWaterPump();
                        }
                        Log.d("456","Current time:"+ currentHM +"Check time"+checkTime);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void turnOnWaterPump(boolean water1, boolean water2){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mWater1 = database.getReference("water_pumps_1");
        DatabaseReference mWater2 = database.getReference("water_pumps_2");
        if(water1 == true && water2 == true){
            mWater1.setValue(1);
            mWater2.setValue(1);
        }else if(water1 == true && water2 == false){
            mWater1.setValue(1);
            mWater2.setValue(0);
        }else if(water1 == false && water2 == true){
            mWater1.setValue(0);
            mWater2.setValue(1);
        }
    }
    public void turnOffWaterPump(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mWater1 = database.getReference("water_pumps_1");
        DatabaseReference mWater2 = database.getReference("water_pumps_2");
        mWater1.setValue(0);
        mWater2.setValue(0);
    }
    public static void mRequestSeverFCM(String humi,String temp){
        RetrofitConfig.retrofit.request_fcm(MainActivity.TOKEN_DEVICE,humi,temp).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("555",response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    private void openDialog_settingNotify(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cutsom_dialog_notification, null);

        edt_humi = view.findViewById(R.id.humi_setting_notification);
        edt_temp = view.findViewById(R.id.temp_setting_notification);

        builder.setView(view)
                .setTitle("Setting")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.mHumdity = edt_humi.getText().toString().trim();
                        MainActivity.mTemperature  = edt_temp.getText().toString().trim();
                        txt_setting_humi.setText(">"+MainActivity.mHumdity);
                        txt_setting_temp.setText(">"+MainActivity.mTemperature);
                    }
                });
        builder.create().show();
    }
}
