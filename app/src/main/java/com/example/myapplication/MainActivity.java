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


    public void displayMessege (View view){
        EditText edit = findViewById(R.id.txt);
        String message = edit.getText().toString();
        Toast.makeText(MainActivity.this,message, Toast.LENGTH_LONG). show();
    }



//    public void button2 (View view){
//        Toast.makeText(MainActivity.this, "ME", Toast.LENGTH_LONG). show();
//    }
    public void buttonClick (View view){
        Button b = (Button) view;
        String label = b.getText().toString();
        Log.d("BUTTON", "buttonClick");
        int id = view.getId();
        Log.d("BUTTON", Integer.toString(id));
//        switch (id){
//            case "button":
//        }
        Toast.makeText(MainActivity.this,label, Toast.LENGTH_LONG). show();
    }
}
