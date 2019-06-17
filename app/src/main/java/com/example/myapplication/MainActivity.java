package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private SharedPreferences mSharedPreferences;




    Button b1;
    Button east;
    Button west;
    Button south;
    Button north;
    Button roadbtn;
    Button mapbtn;





    //Button b1;
    EditText ed1,ed2,ed4;
    TextView tx1;
    int counter = 3;

    String theUser;
    String thePass;

    private static final String TAG = "";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;




    //Menu menu;

    public void init() {
        b1 = (Button) findViewById(R.id.btn);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent welcome = new Intent(MainActivity.this, TeamActivity.class);

                startActivity(welcome);
            }

        });
    }





    /********************************* mine ***************************/
    public void east1() {
        east = (Button) findViewById(R.id.east);
        east.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent E = new Intent(MainActivity.this, East.class);

                startActivity(E);
            }

        });
    }
    public void road() {
        roadbtn = (Button) findViewById(R.id.camers);
        roadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(MainActivity.this, traffic_camera.class);

                startActivity(b);
            }

        });
    }
    /**** map  intent ***/
    public void mapss() {
        mapbtn = (Button) findViewById(R.id.loc);
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(MainActivity.this, TrafficMap.class);

                startActivity(m);
            }

        });
        /***************************************mine**********************/

    }





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /***************************************mine**********************/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        init();
        east1();
        road();
        mapss();
        /***************************************mine**********************/







        mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        mSharedPreferencesHelper = new SharedPreferencesHelper(mSharedPreferences);


        b1 = (Button)findViewById(R.id.btn);
        ed1 = (EditText)findViewById(R.id.names);
        ed2 = (EditText)findViewById(R.id.email);
        ed4 = (EditText)findViewById(R.id.password);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstValidate()) {



                    if (ed1.getText().toString().equals("captain") &&
                            ed2.getText().toString().equals("banana")) {
                        Toast.makeText(getApplicationContext(),
                                "Redirecting...", Toast.LENGTH_SHORT).show();

                        SharedPreferences settings = getSharedPreferences("myPrefs", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        //   editor.putBoolean("silentMode", mSilentMode);
                        String capUser = "abdifatah.mohamed@seattlecolleges.edu";
                        String capPass = "tangerine";
                        editor.putString("capUser", capUser);
                        editor.putString("capPass", capPass);
                        editor.commit();


                        // goToTeam();


                        signIn();

                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        // tx1.setVisibility(View.VISIBLE);
                        // tx1.setBackgroundColor(Color.RED);
                        counter--;
                        // tx1.setText(Integer.toString(counter));

                        if (counter == 0) {
                            b1.setEnabled(false);
                        }
                    }

                }
            }
        });


    }


    private boolean firstValidate() {

        String username = ed1.getText().toString();
        String password = ed2.getText().toString();

        final EditText emailValidate = (EditText) findViewById(R.id.email);

        final TextView textView = (TextView) findViewById(R.id.text);

        String email = emailValidate.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter User", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void signIn() {

        mAuth.signInWithEmailAndPassword("abdifatah.mohamed@seattlecolleges.edu","tangerine")
                .addOnCompleteListener(this, new
                        OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                Log.d("FIREBASE", "signIn:onComplete:" +
                                        task.isSuccessful());
                                if (task.isSuccessful()) {
                                    // update profile
                                    FirebaseUser user =
                                            FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new
                                            UserProfileChangeRequest.Builder()
                                            .setDisplayName("Captain")
                                            .build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new
                                                                           OnCompleteListener<Void>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull
                                                                                                              Task<Void> task) {
                                                                                   if (task.isSuccessful()) {
                                                                                       Log.d("FIREBASE", "User profile updated.");
                                                                                       // Go to FirebaseActivity
                                                                                       startActivity(new
                                                                                               Intent(MainActivity.this, TeamActivity.class));
                                                                                   }
                                                                               }
                                                                           });
                                } else {
                                    Log.d("FIREBASE", "sign-in failed");
                                    Toast.makeText(MainActivity.this, "Sign In Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }







    /***************************************mine**********************/
    /***************************************mine**********************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }
    /***************************************mine**********************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon1:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /*
    public void east(View view) {
        Toast.makeText(MainActivity.this,"H E L L O  E A S T", Toast.LENGTH_LONG).show();
        // Do something in response to button
    }
    */
    public void south(View view) {

        Toast.makeText(MainActivity.this,"H E L L O  S O U T H", Toast.LENGTH_LONG).show();

        // Do something in response to button
    }
    /*
    public void west(View view) {
        Toast.makeText(MainActivity.this,"H E L L O  W E S T ", Toast.LENGTH_LONG).show();
        // Do something in response to button
    }
    */
    public void north(View view) {

        Toast.makeText(MainActivity.this,"H E L L O  N O R T H", Toast.LENGTH_LONG).show();

        // Do something in response to button
    }


}