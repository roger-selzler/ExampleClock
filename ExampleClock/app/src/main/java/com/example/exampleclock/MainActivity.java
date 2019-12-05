package com.example.exampleclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ArrayList<Alarm> alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.ListOfAlarms), this.MODE_PRIVATE);
        ScrollView scrollView = new ScrollView(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        System.out.println("Starting main activity");

        String jsonString = sharedPreferences.getString(String.valueOf(R.string.ListOfAlarms),"");
        System.out.println(jsonString);
        if (!jsonString.equals("")){
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            LinearLayout l1=new LinearLayout(this);
            alarmList = new ArrayList<>();

            alarmList = gson.fromJson(jsonString, new TypeToken<ArrayList<Alarm>>() {}.getType());
            for (int i=0;i<alarmList.size();i++){
                final Alarm alarm = alarmList.get(i);
                l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                l1.setOrientation(LinearLayout.VERTICAL);
                l1.setGravity(Gravity.CENTER);
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER);
                textView.setText(alarm.time + " (" + alarm.getAlarmColor() + ")");
                textView.setTag(alarm.alarmId);
                switch (alarm.getAlarmColor()){
                    case "Blue":
                        textView.setTextColor(Color.BLUE);
                        break;
                    case "Green":
                        textView.setTextColor(Color.GREEN);
                        break;
                    case "Red":
                        textView.setTextColor(Color.RED);
                        break;
                    default:
                        break;
                }
                textView.setTextSize(18);
                textView.setTypeface(null,Typeface.BOLD);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView tv = (TextView) view;
                        for (int i=0;i<alarmList.size();i++){
                            Alarm al = alarmList.get(i);
                            if (al.alarmId.equals(tv.getTag().toString())){
                                alarmList.remove(i);
                                GsonBuilder builder = new GsonBuilder();
                                builder.setPrettyPrinting();
                                Gson gson = builder.create();
                                String jsonString = gson.toJson(alarmList);
                                System.out.println(jsonString);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(String.valueOf(R.string.ListOfAlarms),jsonString);
                                editor.commit();
                                finish();
                                startActivity(getIntent());
                            }
                        }
//                        Toast.makeText(getApplicationContext(),tv.getTag().toString(),Toast.LENGTH_LONG).show();
                    }
                });
                linearLayout.addView(textView);
            }
        }

        Button bt = new Button(this);
        bt.setText("Add Alarm");
        bt.setGravity(Gravity.CENTER);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),addAlarm.class);
                startActivityForResult(intent,RESULT_OK);
            }
        });
        LinearLayout l2=new LinearLayout(this);
        l2.addView(bt);
        l2.setGravity(Gravity.CENTER);
        linearLayout.addView(l2);
        scrollView.addView(linearLayout);
        setContentView(scrollView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
