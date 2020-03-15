package com.thorschmidt.curso.organizze.model;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.thorschmidt.curso.organizze.R;
import com.thorschmidt.curso.organizze.config.FirebaseConfig;
import com.thorschmidt.curso.organizze.helper.DateCustom;

public class Transaction {

    private String idTransaction, category, date, description, type;
    private double value;
    private User usr;

    public Transaction() {

    }

    public void delete() {
        usr = FirebaseConfig.getUser();

        // get Firebase database reference
        DatabaseReference dbRef = FirebaseConfig.getFirebaseDatabase();

        // set the object values to save the data
        dbRef.child("transactions")
                .child(usr.getIdUser()) //user's email
                .child(DateCustom.getYearFromStringDate(date)) // year
                .child(DateCustom.getMonthFromStringDate(date)) // month
                .child(idTransaction)
                .removeValue();

        // update the total values in the user's register
        // depending on the type of the transaction
        if (getType().equals("spend")){
            usr.setSpendTotal(usr.getSpendTotal() - getValue());
        }else if (getType().equals("income")){
            usr.setIncomeTotal(usr.getIncomeTotal() - getValue());
        }
        usr.save();
    }


    public void save() {
        usr = FirebaseConfig.getUser();

        // get Firebase database reference
        DatabaseReference dbRef = FirebaseConfig.getFirebaseDatabase();

        Log.i("user", "save: "+ usr.getIdUser());

        // set the object values to save the data
        dbRef.child("transactions")
                .child(usr.getIdUser()) //user's email
                .child(DateCustom.getYearFromStringDate(date)) // year
                .child(DateCustom.getMonthFromStringDate(date)) // month
                .push() //create auto id on Firebase for this register
                .setValue(this);

        // update the total values in the user's register
        // depending on the type of the transaction
        Log.i("save", "save: type - "+ type);
        Log.i("save", "save: R.string.spendType - "+ R.string.spendType);
        Log.i("save", "save: usr.getSpendTotal() - "+ usr.getSpendTotal());
        Log.i("save", "save: usr.getIncomeTotal() - "+ usr.getIncomeTotal());
        Log.i("save", "save: getValue() - "+ getValue());
        if (getType().equals("spend")){
            usr.setSpendTotal(usr.getSpendTotal() + getValue());
        }else if (getType().equals("income")){
            usr.setIncomeTotal(usr.getIncomeTotal() + getValue());
        }
        usr.save();
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
