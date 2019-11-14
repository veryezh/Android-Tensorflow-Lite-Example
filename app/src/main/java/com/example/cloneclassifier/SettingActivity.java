package com.example.cloneclassifier;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingActivity extends Activity {

    RadioButton radioButton1, radioButton2;
    RadioGroup radioGroup;

    SharedPreferences pref;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        ed = pref.edit();

        String modelName = pref.getString("modelName", "");
        switch (modelName) {
            case "TeaLeaves":
                radioButton1.toggle();
                break;
            case "Animals":
                radioButton2.toggle();
                break;
            default:
                break;
        }
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radioButton1:
                    ed.clear();
                    ed.putString("modelName", "TeaLeaves");
                    ed.commit();
                    break;
                case R.id.radioButton2:
                    ed.clear();
                    ed.putString("modelName", "Animals");
                    ed.commit();
                    break;
                default:
                    break;
            }
        }
    };
}
