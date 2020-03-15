package com.thorschmidt.curso.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.thorschmidt.curso.organizze.config.FirebaseConfig;
import com.thorschmidt.curso.organizze.databinding.ActivityRegisterBinding;
import com.thorschmidt.curso.organizze.helper.Base64Custom;
import com.thorschmidt.curso.organizze.model.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();

        //change the title in the toolbar
        getSupportActionBar().setTitle("Register");

    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsAreFilled()){
                    usr = new User();
                    usr.setName(binding.txtName.getText().toString());
                    usr.setEmail(binding.txtEmail.getText().toString());
                    usr.setPass(binding.txtPass.getText().toString());
                    usr.setIdUser(Base64Custom.encrypt64(usr.getEmail()));
                    usr.save(); // save user data in the database
                    registerUser(usr);
                }
            }
        });
    }

    /**
     * Registers a User in the Firebase using FirebaseAuth
     * @param usr
     */
    public void registerUser(User usr) {
        FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                usr.getEmail(),
                usr.getPass()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish(); //closes this screen to validate the user in the intro_register (MainActivity) screen
                    Log.i("user", "registerUser onComplete: Success!");
                } else {
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        Toast.makeText(RegisterActivity.this, "You need a stronger password", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(RegisterActivity.this, "Provide a valid E-mail!", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthUserCollisionException e){
                        Toast.makeText(RegisterActivity.this, "User already registered!", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Toast.makeText(RegisterActivity.this, "Error registering user!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean fieldsAreFilled() {
        String name,email,pass;
        name = binding.txtName.getText().toString();
        email = binding.txtEmail.getText().toString();
        pass = binding.txtPass.getText().toString();
        if (name.trim().isEmpty() || email.trim().isEmpty() || pass.trim().isEmpty()){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void goToLoginScreen(View v) {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
