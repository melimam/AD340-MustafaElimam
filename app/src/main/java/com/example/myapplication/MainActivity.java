package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buttonClick (View view){
        Button b = (Button) view;
        String label = b.getText().toString().toLowerCase();
    Log.d("BUTTON", "buttonClick");
        switch (label){
            case "movies":
                Intent E = new Intent(MainActivity.this, Movielist.class);

                startActivity(E);

        }
        Toast.makeText(MainActivity.this,label, Toast.LENGTH_LONG). show();
    }
}

