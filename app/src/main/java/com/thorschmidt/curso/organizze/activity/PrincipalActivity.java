package com.thorschmidt.curso.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.thorschmidt.curso.organizze.R;
import com.thorschmidt.curso.organizze.adapter.TransactionAdapter;
import com.thorschmidt.curso.organizze.config.FirebaseConfig;
import com.thorschmidt.curso.organizze.databinding.ActivityPrincipalBinding;
import com.thorschmidt.curso.organizze.model.Transaction;
import com.thorschmidt.curso.organizze.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.thorschmidt.curso.organizze.config.FirebaseConfig.getUserRef;
import static com.thorschmidt.curso.organizze.config.FirebaseConfig.getUserTransactionsRef;

public class PrincipalActivity extends AppCompatActivity {

    /*
    Variables
     */

    private ActivityPrincipalBinding binding;
    private MaterialCalendarView calendarView;
    private CharSequence[] months = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "December"};
    private CharSequence[] weekDays = {"Son", "Mon", "Die", "Mit", "Don", "Fre", "Sam"};
    private ValueEventListener valueEventListenerUser;
    private ValueEventListener valueEventListenerTransactions;
    private RecyclerView recView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> traList = new ArrayList<>();
    private CalendarDay selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_principal);

        recView = findViewById(R.id.recyclerTransactions);

        setMenu();
        setCalendar();
        setAdapter();
        setRecycler();
    }



    private void setRecycler() {
        recView = findViewById(R.id.recyclerTransactions);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setHasFixedSize(true);
        recView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recView.setAdapter(transactionAdapter);
    }

    private void setAdapter() {
        // setting the adapter to the recyclerview
        transactionAdapter = new TransactionAdapter(traList, this);
        // setting the layout to the recyclerview
        recView.setLayoutManager(new LinearLayoutManager(this));
        // optimizing the recyclerview by setting a fixed size
        // so that it reuses already rendered items
        // to show information of new items, instead of creating lot of items
        recView.setHasFixedSize(true);
        // adds a division
        recView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        // setting the recyclerView
        recView.setAdapter(transactionAdapter);

        // sets the swipe action on the recycler view to delete an item
        setSwipe();
/*
        // set listeners
        recView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(
                                        MainActivity.this,
                                        listaFilmes.get(position).getTituloFilme(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(MainActivity.this, "Click longo", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

 */
    }

    /**
     * Set the Swipe action on the recycler view
     */
    private void setSwipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                // deactivate the drag and drop actions
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            // used to drag and drop functions
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // action to execute when the user moves the item to the sides
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeTransaction(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recView);

    }

    /**
     * On swiping, the selected transaction will be deleted from firebase
     * @param viewHolder
     */
    private void removeTransaction(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // set the alert dialog
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Do you want to delete this item?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Transaction tra = traList.get(viewHolder.getAdapterPosition());
                tra.delete();
                transactionAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                // put the item that was swiped back again into the list
                transactionAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // adds back the listener to the page
        this.updateUser();
        this.updateTransactions();
    }

    /**
     * Updates the static User object with the current credentials
     */
    public void updateUser(){
        // check if the user is logged in
        if (FirebaseConfig.isUserLoggedIn()) {

            FirebaseConfig.updateUserDataRef();

            // getting user's data into an User object
            // adding the result to an object, so this listener can be
            // stopped and resumed whenever we want.
            valueEventListenerUser = getUserRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseConfig.usr = new User(dataSnapshot.getValue(User.class));
                    setShowHeaderInfo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateTransactions() {
        // check if the user is logged in
        if (FirebaseConfig.isUserLoggedIn()) {

            FirebaseConfig.updateUserTransactionsDataRef();
            Log.i("transactions", "updateTransactions: "+ selectedDate.getMonth() +" "+ selectedDate.getYear());
            // getting user's data into an User object
            // adding the result to an object, so this listener can be
            // stopped and resumed whenever we want.
            valueEventListenerTransactions = getUserTransactionsRef()
                    .child(String.valueOf(selectedDate.getYear()))
                    .child(String.format("%02d",selectedDate.getMonth()))
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //FirebaseConfig.usr = new User(dataSnapshot.getValue(User.class));
                    //setShowHeaderInfo();
                    traList.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) { //using getChildren doesn't return the UUID of each transaction
                        Log.i("transactions", "updateTransactions onDataChange: "+ d.toString());
                        Transaction tra = d.getValue(Transaction.class);
                        if (tra != null) {
                            tra.setIdTransaction(d.getKey());
                        }
                        traList.add(tra);
                    }
                    // notify the adapter that the data was updated
                    // this is necessary because the initial adapter
                    // is an empty list
                    transactionAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Log.i("transactions", "updateTransactions: " + getUserTransactionsRef()
                    .child(String.valueOf(selectedDate.getYear()))
                    .child(String.valueOf(selectedDate.getMonth())));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // removes the event listener while the user is using other apps
        getUserRef().removeEventListener(valueEventListenerUser);
        getUserTransactionsRef().removeEventListener(valueEventListenerTransactions);
    }

    private void setMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseConfig.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void setShowHeaderInfo() {
        TextView lblTotalAmount = findViewById(R.id.lblTotalAmount);
        TextView lblGreetings = findViewById(R.id.lblGreetings);
        //shows the amount in the format 0.00
        DecimalFormat dcmFormat = new DecimalFormat("0.00");
        String totalAmount = dcmFormat.format(FirebaseConfig.usr.getIncomeTotal() - FirebaseConfig.usr.getSpendTotal());
        lblTotalAmount.setText("$"+ totalAmount);
        //shows the name of the user
        lblGreetings.setText("Welcome, "+ FirebaseConfig.usr.getName() +"!");
    }

    private void setCalendar() {
        selectedDate = CalendarDay.today();
        calendarView = findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2015,1,1))
                .setMaximumDate(CalendarDay.today());
        calendarView.setTitleMonths(months); // set the labels for the months
        calendarView.setWeekDayLabels(weekDays); // set the labels for the weekdays
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                selectedDate = date;
                getUserTransactionsRef().removeEventListener(valueEventListenerTransactions);
                updateTransactions();
            }
        });

        //selectedMonth = calendarView.getCurrentDate().getMonth();
        //selectedYear = calendarView.getCurrentDate().getYear();
    }

    public void addSpend(View view){
        binding.menu.close(true);
        startActivity(new Intent(this,SpendActivity.class));
    }
    public void addIncome(View view){
        binding.menu.close(true);
        startActivity(new Intent(this,IncomeActivity.class));
    }
}
