package com.thorschmidt.curso.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.thorschmidt.curso.organizze.R;
import com.thorschmidt.curso.organizze.config.FirebaseConfig;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); //don't use setcontentview when extending introactivity

        setIntroFragmentSlides();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // if the user is already logged in, goes to app main screen
        if (FirebaseConfig.isUserLoggedIn()){
            //FirebaseConfig.signOut();
            startActivity(new Intent(this, PrincipalActivity.class));
        }
    }

    public void btnRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));

    }

    public void btnLogin(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void setIntroFragmentSlides() {

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_blue_light)
                .fragment(R.layout.intro_1)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_light)
                .fragment(R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_green_light)
                .fragment(R.layout.intro_3)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_red_light)
                .fragment(R.layout.intro_4)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_register)
                .canGoForward(false)
                .build());

    }
}
