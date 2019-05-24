package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
//    Button button2;
//    Button button3;
//    Button button4;
//    Button button5;
//    EditText edit;
//
//    public void A1() {
//        button3 = (Button) findViewById(R.id.button3);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent E = new Intent(MainActivity.this, Movielist.class);
//
//                startActivity(E);
//            }
//
//        });
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick (View view) {
        Button b = (Button) view;
        String label = b.getText().toString().toLowerCase();
        Log.d("BUTTON", "buttonClick");
        switch (label) {
            case "movies":
                Intent E = new Intent(MainActivity.this, Movielist.class);

                startActivity(E);

        }
    }

    public void displayMessege (View view){
        EditText edit = findViewById(R.id.txt);
        String message = edit.getText().toString();
        Toast.makeText(MainActivity.this,message, Toast.LENGTH_LONG). show();
    }


}

