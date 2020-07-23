package com.berrycent.electionpollacm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout email;
    private TextInputLayout password;
    private Button login;
    private TextView register;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get all element
        auth=FirebaseAuth.getInstance();
         register=findViewById(R.id.register);
        progressBar=findViewById(R.id.progressBar);
        email=(TextInputLayout)findViewById(R.id.email);
        password=(TextInputLayout)findViewById(R.id.password);
        login=findViewById(R.id.login);


        // ----------------onClick lisner to register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        //---------------------------------end--------------



//--------------------------------------------LOGIN BUTTON -------------------------------------------------------------------------
        login.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                String txt_email=email.getEditText().getText().toString();
                String txt_password=password.getEditText().getText().toString();


                // if any field is blank so we have to show the error
                if(TextUtils.isEmpty(txt_email)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(txt_password)){
                    password.setError("Password is Required");
                    return;
                }
                if(txt_password.length() < 6){
                    password.setError("Password is too small");
                    return;
                }
                //--------------------------------end----------------------------------

                progressBar.setVisibility(View.VISIBLE);


                // --------------authenticate the user --------------------------
                auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}