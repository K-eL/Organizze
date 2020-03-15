package com.thorschmidt.curso.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thorschmidt.curso.organizze.R;
import com.thorschmidt.curso.organizze.model.Transaction;

import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>  {

    private List<Transaction> traList;
    private Context context;

    public TransactionAdapter(List<Transaction> traList, Context context){
        this.traList = traList;
        this.context = context;
    }

    // create layout from xml
    // res/layout/ new > 'layout resource file'
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // convert the layout xml into a View object
        // parent is the list
        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_transaction_list, parent, false);

        return new MyViewHolder(itemList);
    }

    // show each element created in onCreate
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // get the item from the list given the position of the holder in the view
        Transaction tra = traList.get(position);
        // set the parameters from the list's item on the called holder
        holder.transactionDescription.setText(tra.getDescription());
        holder.transactionCategory.setText(tra.getCategory());
        holder.transactionValue.setText(String.valueOf(tra.getValue()));
        if (tra.getType().equals("spend")){
            holder.transactionValue.setTextColor(context.getResources().getColor(R.color.colorAccentSpend));
            holder.transactionValue.setText("-"+ tra.getValue());
        }else if (tra.getType().equals("income")){
            holder.transactionValue.setTextColor(context.getResources().getColor(R.color.colorAccentIncome));
        }
    }

    @Override
    public int getItemCount() {
        return traList.size();
    }

    // serve para configurar a exibição das informações dentro do recyclerview
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView transactionDescription, transactionCategory, transactionValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionDescription = itemView.findViewById(R.id.lblTransactionDescription);
            transactionCategory = itemView.findViewById(R.id.lblTransactionCategory);
            transactionValue = itemView.findViewById(R.id.lblTransactionValue);
        }
    }
}
