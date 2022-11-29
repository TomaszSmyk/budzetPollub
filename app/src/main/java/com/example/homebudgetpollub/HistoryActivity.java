package com.example.homebudgetpollub;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView historyTotalAmountSpent;
    RecyclerView recyclerView;
    private DatabaseReference expensesRef;
    private String onlineUserId = "";
    private Toolbar toolbar;
    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> myDataList;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        onlineUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        DatabaseReference personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        search = findViewById(R.id.search);
        historyTotalAmountSpent = findViewById(R.id.historyTotalAmountSpent);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        myDataList = new ArrayList<>();
        todayItemsAdapter = new TodayItemsAdapter(HistoryActivity.this, myDataList);
        recyclerView.setAdapter(todayItemsAdapter);

        search.setOnClickListener(view -> showDataPickerDialog());
    }

    private void showDataPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONDAY),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        int months = month + 1;
        String date = dayOfMonth + "-" + months + "-" + year;
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        Query query = expensesRef.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                int totalAmount = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataList.add(data);
                    totalAmount += data.getAmount();
                }

                todayItemsAdapter.notifyDataSetChanged();

                historyTotalAmountSpent.setText(date + " spent: $" + totalAmount);
                historyTotalAmountSpent.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "error", error.toException());
            }
        });
    }
}