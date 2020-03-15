package com.thorschmidt.curso.organizze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thorschmidt.curso.organizze.R;
import com.thorschmidt.curso.organizze.databinding.ActivityIncomeBinding;
import com.thorschmidt.curso.organizze.helper.DateCustom;
import com.thorschmidt.curso.organizze.model.Transaction;

public class IncomeActivity extends AppCompatActivity {

    private ActivityIncomeBinding binding;
    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_income);

        // set the current date in the textbox
        binding.txtDate.setText(DateCustom.getCurrentDate());
    }

    public void saveIncome(View v){
        if(fieldsAreFilled()){
            transaction = new Transaction();
            transaction.setValue(Double.parseDouble(binding.lblTotalSpend.getText().toString()));
            transaction.setCategory(binding.txtCategory.getText().toString());
            transaction.setDate(binding.txtDate.getText().toString());
            transaction.setDescription(binding.txtDescription.getText().toString());
            transaction.setType(getString(R.string.incomeType));
            transaction.save();
            finish();
        }
    }

    private boolean fieldsAreFilled() {
        if (binding.txtCategory.getText().toString().trim().isEmpty() ||
                binding.txtDate.getText().toString().trim().isEmpty() ||
                binding.txtDescription.getText().toString().trim().isEmpty() ||
                binding.lblTotalSpend.getText().toString().trim().isEmpty()
        ){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
