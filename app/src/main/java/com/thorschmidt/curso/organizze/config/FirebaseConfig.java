package com.thorschmidt.curso.organizze.config;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thorschmidt.curso.organizze.helper.Base64Custom;
import com.thorschmidt.curso.organizze.model.User;


public class FirebaseConfig {

    /*
    Variables
    */

    private static FirebaseAuth auth;
    public static DatabaseReference dbRef;
    private static DatabaseReference userRef;
    private static DatabaseReference userTransactionsRef;
    public static User usr;

    /*
    Getters and Setters
    */

    public static DatabaseReference getUserTransactionsRef() {
        return userTransactionsRef;
    }

    public static void setUserTransactionsRef(DatabaseReference userTransactionsRef) {
        Log.i("transactions", "setUserTransactionsRef: " + userTransactionsRef.toString());
        FirebaseConfig.userTransactionsRef = userTransactionsRef;
    }

    public static User getUser() {
        return usr;
    }

    public static DatabaseReference getUserRef() {
        return userRef;
    }

    public static void setUserRef(DatabaseReference userRef) {
        FirebaseConfig.userRef = userRef;
    }

    /**
     * Get the instance of Firebase Auth
     * @return
     */
    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }


    /**
     * Get the instance of Firebase Database
     * @return
     */
    public static DatabaseReference getFirebaseDatabase() {
        if (dbRef == null){
            dbRef = FirebaseDatabase.getInstance().getReference();
        }
        return dbRef;
    }


    /**
     * Validates if the user is already logged in
     */
    public static boolean isUserLoggedIn(){
        auth = getFirebaseAuth();
        if(auth.getCurrentUser()!=null){
            return true;
        }
        return false;
    }


    public static void updateUserDataRef(){

        // get Firebase database reference
        DatabaseReference dbRef = FirebaseConfig.getFirebaseDatabase();

        // user ref
        setUserRef(dbRef.child("users")
                .child(Base64Custom.encrypt64(getUserEmail())));

    }

    /**
     * Gets the Current user email
     * @return
     */
    private static String getUserEmail() {
        String email;
        if (getUser()== null){
            // get User to use the email
            FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();
            email = auth.getCurrentUser().getEmail();
        }
        else{
            email = usr.getEmail();
        }
        return email;
    }

    public static void updateUserTransactionsDataRef(){

        // get Firebase database reference
        DatabaseReference dbRef = FirebaseConfig.getFirebaseDatabase();

        // user ref
        setUserTransactionsRef(dbRef.child("transactions")
                .child(Base64Custom.encrypt64(getUserEmail()))); //user's email

    }


    /**
     * Sign out the current logged user
     */
    public static void signOut(){
        auth = getFirebaseAuth();
        if(auth.getCurrentUser()!=null){
            auth.signOut();
        }
    }


}
