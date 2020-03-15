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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.thorschmidt.curso.organizze.config.FirebaseConfig;
import com.thorschmidt.curso.organizze.databinding.ActivityLoginBinding;
import com.thorschmidt.curso.organizze.model.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_login);
        setContentView(binding.getRoot());

        setListeners();

        //change the title in the toolbar
        getSupportActionBar().setTitle("Login");
    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsAreFilled()){
                    usr = new User();
                    usr.setEmail(binding.txtEmail.getText().toString());
                    usr.setPass(binding.txtPass.getText().toString());
                    loginUser(usr);
                }
            }
        });
    }

    private void loginUser(User usr) {
        FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();
        auth.signInWithEmailAndPassword(
                usr.getEmail(),
                usr.getPass()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("user", "loginUser onComplete: Success!");
                    goToMainScreen();
                } else {
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidUserException e){
                        Toast.makeText(LoginActivity.this, "This user is not registered", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(LoginActivity.this, "Wrong user or password", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Toast.makeText(LoginActivity.this, "Error logging user!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void goToMainScreen() {
        startActivity(new Intent(this,PrincipalActivity.class));
        finish();
    }

    public void goToRegisterScreen(View v) {
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }

    private boolean fieldsAreFilled() {
        String email,pass;
        email = binding.txtEmail.getText().toString();
        pass = binding.txtPass.getText().toString();
        if (email.trim().isEmpty() || pass.trim().isEmpty()){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}