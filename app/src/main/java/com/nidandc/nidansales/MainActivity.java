package com.nidandc.nidansales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    private EditText username;
    private EditText password;
    private EditText register_username;
    private EditText register_password;
    private EditText register_name;
    private RelativeLayout registerView;
    private RelativeLayout signInView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        register_name=(EditText)findViewById(R.id.name);
        register_username=(EditText)findViewById(R.id.register_username);
        register_password=(EditText)findViewById(R.id.register_password);
        signInView=(RelativeLayout)findViewById(R.id.signInView);
        registerView=(RelativeLayout)findViewById(R.id.registerView);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            startNextActivity();
        }
    }

    private void startNextActivity()
    {
        Intent intent=new Intent(this,NavigationMain.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signIn(View view)
    {
        String user=username.getText().toString();
        String pass=password.getText().toString();
        if (user.length()==0)
        {
            DisplayToast.display(this,"Cannot leave username empty");
            return;
        }
        else if (pass.length()==0)
        {

            DisplayToast.display(this,"Cannot leave password empty");
            return;
        }
        mAuth.signInWithEmailAndPassword(user+"@nidandc.com",pass)
                .addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            DisplayToast.display(getApplicationContext(),task.getException().getMessage());
                        }
                        else
                        {
                            startNextActivity();
                        }
                    }
                });
    }

    public void showRegisterPage(View view)
    {
        signInView.setVisibility(View.INVISIBLE);
        registerView.setVisibility(View.VISIBLE);
    }

    public void registerNewUser(View view)
    {
        String user=register_username.getText().toString();
        String pass=register_password.getText().toString();
        final String name=register_name.getText().toString();
        if (user.length()==0||pass.length()==0||name.length()==0)
        {
            DisplayToast.display(this,"Cannot Leave a field empty");
            return;
        }
        DisplayToast.display(this,"Good");


        //ToDo:Register
        mAuth.createUserWithEmailAndPassword(user+"@nidandc.com",pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (!task.isSuccessful())
                {
                    DisplayToast.display(getApplicationContext(),task.getException().getMessage());
                }
                else
                {
                    DatabaseReference reference=database
                            .getReference("users/"+mAuth.getCurrentUser().getUid());
                    reference.child("Name").setValue(name);
                    startNextActivity();

                }
            }
        });





    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (registerView.getVisibility()==View.VISIBLE)
        {
            registerView.setVisibility(View.INVISIBLE);
            signInView.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
