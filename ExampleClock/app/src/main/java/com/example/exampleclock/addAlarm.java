package com.example.exampleclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class addAlarm extends AppCompatActivity {
    TimePicker timepicker;
    RadioButton [] colorAlarm ;
    RadioGroup rg;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.ListOfAlarms), this.MODE_PRIVATE);

        System.out.print(sharedPreferences.getString(String.valueOf(R.string.ListOfAlarms),""));

        LinearLayout ll1 = new LinearLayout(this);
        ll1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        ll1.setOrientation(LinearLayout.VERTICAL);
        ll1.setGravity(Gravity.CENTER);

        timepicker= new TimePicker(this);
        timepicker.setIs24HourView(true);
        ll1.addView(timepicker);

        colorAlarm = new RadioButton[3];
        for (int i=0;i<colorAlarm.length;i++){
            colorAlarm[i]=new RadioButton(this);
        }
        rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        rg.setGravity(Gravity.CENTER);

        colorAlarm[0].setText("Blue");
        colorAlarm[1].setText("Red");
        colorAlarm[2].setText("Green");

        for (int i=0;i<colorAlarm.length;i++){
            rg.addView(colorAlarm[i]);
        }
        ((RadioButton) rg.getChildAt(0)).setChecked(true);
        ll1.addView(rg);

        Button btnAdd = new Button(this);
        btnAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        btnAdd.setText("Add Alarm");
        ll1.addView(btnAdd);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Alarm> alarmList = new ArrayList<>(); // Used to store and retrieve from shared preferences.

                int hour = timepicker.getHour();
                int minute = timepicker.getMinute();

                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = findViewById(id);

                String alarmTime = hour + ":" + minute;
                String alarmColor = rb.getText().toString();
                Alarm alarm = new Alarm(alarmTime, alarmColor);

                Toast.makeText(view.getContext(), "Timepicker value is: " + alarm.getTime() + ", color: "+alarm.getAlarmColor(),Toast.LENGTH_LONG).show();

                String jsonAlarmsString = sharedPreferences.getString(String.valueOf(R.string.ListOfAlarms), "");

                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();
                if (!jsonAlarmsString.equals("")){
                    alarmList = gson.fromJson(jsonAlarmsString, new TypeToken<ArrayList<Alarm>>() {}.getType());
                }
                alarmList.add(alarm);

                for (int i=0;i<alarmList.size();i++){
                    Alarm al = alarmList.get(i);
                    System.out.println("Timepicker value is: " + al.getTime() + ", color: "+al.getAlarmColor());
                }
                String jsonString = gson.toJson(alarmList);
                System.out.println(jsonString);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(String.valueOf(R.string.ListOfAlarms),jsonString);
                editor.commit();
                finish();
            }
        });

        linearLayout.addView(ll1);
        setContentView(linearLayout);

    }
}
