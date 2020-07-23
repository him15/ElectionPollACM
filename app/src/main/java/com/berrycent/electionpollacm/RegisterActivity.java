package com.berrycent.electionpollacm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout name;
    private TextInputLayout contact;
    private TextInputLayout address;
    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout cpassword;
    private RadioGroup radioGroup;
    private RadioButton g1;
    private RadioButton g2;
    String gender="";
    // firebase database
    DatabaseReference databaseReference;

    private Button register;
    private ProgressBar progressBar;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // set the Title
        getSupportActionBar().setTitle("Signup Form");

        name=(TextInputLayout)findViewById(R.id.name);
        contact=(TextInputLayout)findViewById(R.id.contact);
        address=(TextInputLayout)findViewById(R.id.address);
        email=(TextInputLayout)findViewById(R.id.email);
        password=(TextInputLayout)findViewById(R.id.password);
        register=findViewById(R.id.register);
        cpassword=(TextInputLayout)findViewById(R.id.cpassword);
        g1=(RadioButton) findViewById(R.id.g1);
        g2=(RadioButton)findViewById(R.id.g2);
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        databaseReference=FirebaseDatabase.getInstance().getReference("User");

        // registering the details in the firebase
    //----------------------------------------REGISTER ONCLICK START-----------------------------------------
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting details from the register page

                final String txt_name=name.getEditText().getText().toString();
                final String txt_contact=contact.getEditText().getText().toString();
                final String txt_address=address.getEditText().getText().toString();
                final String txt_email=email.getEditText().getText().toString();
                String txt_password=password.getEditText().getText().toString();
                String txt_cpassword=cpassword.getEditText().getText().toString();
                if(g1.isSelected()){
                    gender="Male";
                }
                if(g2.isSelected()){
                    gender="female";
                }
                 //-------------------------END -------------------------------------------


              //----------------------------- if user is alreadyLogged in then send it to profile activity
                if(auth.getCurrentUser() != null){
                    startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));
                    finish();
                }
                //------------------end----------------------------------------


                // if any field is blank so we have to show the error
                if(TextUtils.isEmpty(txt_email)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(txt_password)){
                    password.setError("Password is Required");
                    return;
                }
                if(TextUtils.isEmpty(txt_address)){
                    address.setError("address is Required");
                    return;
                }
                if(TextUtils.isEmpty(txt_contact)){
                    contact.setError("contact is Required");
                    return;
                }
                if(TextUtils.isEmpty(txt_cpassword)){
                    cpassword.setError("Confirm the Enterd Password");
                    return;
                }
                if(txt_password.length() < 6){
                    password.setError("Password is too small");
                    return;
                }
                if(!txt_password.equals(txt_cpassword)){
                    password.setError("password doesn't Match");
                    return;
                }
                //--------------------------------end----------------------------------

                progressBar.setVisibility(View.VISIBLE);

               auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){

                           // Enter the details in database
                           Student information=new Student(txt_name,txt_address,txt_email,txt_contact,gender);
                           FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   Toast.makeText(RegisterActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                               }
                           });
                       }
                       else{
                           progressBar.setVisibility(View.GONE);
                           Toast.makeText(RegisterActivity.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });


            }
        });
//        ---------------------------------REGISTER ON CLICK END------------------------------------------------------
    }

        }