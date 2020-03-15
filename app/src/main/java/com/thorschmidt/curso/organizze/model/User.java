package com.thorschmidt.curso.organizze.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.thorschmidt.curso.organizze.config.FirebaseConfig;
import com.thorschmidt.curso.organizze.helper.Base64Custom;

public class User {

    private String idUser, name, pass, email;
    private double spendTotal = 0.00, incomeTotal = 0.00;

    public Double getSpendTotal() {
        return spendTotal;
    }

    public void setSpendTotal(Double spendTotal) {
        this.spendTotal = spendTotal;
    }

    public Double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(Double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude //this reference excludes this info during the database register (setValue)
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude //this reference excludes this info during the database register (setValue)
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public User(String idUser, String name, String pass, String email, double spendTotal, double incomeTotal) {
        this.idUser = idUser;
        this.name = name;
        this.pass = pass;
        this.email = email;
        this.spendTotal = spendTotal;
        this.incomeTotal = incomeTotal;
    }

    public User(User usr) {
        this.idUser = Base64Custom.encrypt64(usr.email);
        this.name = usr.name;
        this.pass = usr.pass;
        this.email = usr.email;
        this.spendTotal = usr.spendTotal;
        this.incomeTotal = usr.incomeTotal;
    }

    public User(){

    }

    public void save(){
        DatabaseReference dbRef = FirebaseConfig.getFirebaseDatabase();
        dbRef.child("users")
                .child(this.idUser)
                .setValue(this);
    }



}
