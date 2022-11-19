package com.example.homebudgetpollub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodaySpendingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView totalAmountSpentOn;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    DatabaseReference expensesRef;

    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> myDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_spending);

       toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Today spending");

       totalAmountSpentOn = findViewById(R.id.totalAmountSpentOn);
       progressBar = findViewById(R.id.progressBar);
       fab = findViewById(R.id.fab);
       loader = new ProgressDialog(this);

       mAuth = FirebaseAuth.getInstance();
       onlineUserId = mAuth.getCurrentUser().getUid();
       expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);


       recyclerView = findViewById(R.id.recyclerView);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       linearLayoutManager.setStackFromEnd(true);
       linearLayoutManager.setReverseLayout(true);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(linearLayoutManager);

       myDataList = new ArrayList<>();
       todayItemsAdapter = new TodayItemsAdapter(TodaySpendingActivity.this, myDataList);
       recyclerView.setAdapter(todayItemsAdapter);

       readItems();

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addItemSpentOn();
           }
       });

    }

    private void readItems() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                int totalAmount = 0;
                for ( DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataList.add(data);
                    totalAmount += data.getAmount();
                }

                todayItemsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                totalAmountSpentOn.setText("Total day's spending: $" + totalAmount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addItemSpentOn() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = view.findViewById(R.id.itemsSpinner);
        final EditText amount = view.findViewById(R.id.amount);
        final EditText note = view.findViewById(R.id.note);
        final Button save = view.findViewById(R.id.save);
        final Button cancel = view.findViewById(R.id.cancel);

        note.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountS = amount.getText().toString();
                String noteS = note.getText().toString();
                String itemS = itemSpinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(amountS)) {
                    amount.setError("Amount is required!");
                    return;
                }

                if(TextUtils.isEmpty(noteS)) {
                    note.setError("Note is required!");
                    return;
                }

                if("Select item".equals(itemS)) {
                    Toast.makeText(TodaySpendingActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                    return;
                }

                loader.setMessage("adding a budget item");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                String id = expensesRef.push().getKey();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);

                Data data = new Data(itemS, date, id, noteS, Integer.parseInt(amountS), months.getMonths());
                expensesRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(TodaySpendingActivity.this, "Budget item added successfuly", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TodaySpendingActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        loader.dismiss();
                    }
                });
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}