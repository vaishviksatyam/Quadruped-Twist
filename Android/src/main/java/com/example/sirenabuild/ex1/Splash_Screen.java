package com.example.sirenabuild.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(com.example.sirenabuild.ex1.R.style.splash_theme);
       try{
            Thread.sleep(1000);
        }
        catch (Exception e){
            Log.e("Error","opening",e);
        }
        super.onCreate(savedInstanceState);
        setContentView(com.example.sirenabuild.ex1.R.layout.activity_splash__screen);
        Intent myIntent = new Intent(Splash_Screen.this,MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}
