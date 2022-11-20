package com.example.homebudgetpollub;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeeklyAnalyticsActivity extends AnalyticsActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_analytics);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();

        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        settingsToolbar = findViewById(R.id.my_Feed_Toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setTitle("Week analytics");

        setAllFields();

    }

    private void setAllFields() {
        anyChartView = findViewById(R.id.anyChartView);

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);
        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHouseExpensesAmount = findViewById(R.id.analyticsHouseExpensesAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEducationAmount);
        analyticsCharityAmount = findViewById(R.id.analyticsCharityAmount);
        analyticsApparelAmount = findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalExpensesAmount = findViewById(R.id.analyticsPersonalExpensesAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);

        linearLayoutTransport = findViewById(R.id.linearLayoutTransport);
        linearLayoutFood = findViewById(R.id.linearLayoutFood);
        linearLayoutFoodHouse = findViewById(R.id.linearLayoutFoodHouse);
        linearLayoutEntertainment = findViewById(R.id.linearLayoutEntertainment);
        linearLayoutEducation = findViewById(R.id.linearLayoutEducation);
        linearLayoutCharity = findViewById(R.id.linearLayoutCharity);
        linearLayoutApparel = findViewById(R.id.linearLayoutApparel);
        linearLayoutHealth = findViewById(R.id.linearLayoutHealth);
        linearLayoutPersonalExp = findViewById(R.id.linearLayoutPersonalExp);
        linearLayoutOther = findViewById(R.id.linearLayoutOther);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);

        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_house = findViewById(R.id.progress_ratio_house);
        progress_ratio_ent = findViewById(R.id.progress_ratio_ent);
        progress_ratio_edu = findViewById(R.id.progress_ratio_edu);
        progress_ratio_cha = findViewById(R.id.progress_ratio_cha);
        progress_ratio_app = findViewById(R.id.progress_ratio_app);
        progress_ratio_hea = findViewById(R.id.progress_ratio_hea);
        progress_ratio_per = findViewById(R.id.progress_ratio_per);
        progress_ratio_oth = findViewById(R.id.progress_ratio_oth);

        status_Image_transport = findViewById(R.id.status_Image_transport);
        status_Image_food = findViewById(R.id.status_Image_food);
        status_Image_house = findViewById(R.id.status_Image_house);
        status_Image_ent = findViewById(R.id.status_Image_ent);
        status_Image_edu = findViewById(R.id.status_Image_edu);
        status_Image_cha = findViewById(R.id.status_Image_cha);
        status_Image_app = findViewById(R.id.status_Image_app);
        status_Image_hea = findViewById(R.id.status_Image_hea);
        status_Image_per = findViewById(R.id.status_Image_per);
        status_Image_oth = findViewById(R.id.status_Image_oth);

        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_image = findViewById(R.id.monthRatioSpending_image);

        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_image = findViewById(R.id.monthRatioSpending_image);

        getTotalWeekTransportExpenses();
        getTotalWeekFoodExpenses();
        getTotalWeekHouseExpenses();
        getTotalWeekEntertainmentExpenses();
        getTotalWeekEducationExpenses();
        getTotalWeekCharityExpenses();
        getTotalWeekApparelExpenses();
        getTotalWeekHealthExpenses();
        getTotalWeekPersonalExpenses();
        getTotalWeekOtherExpenses();

        getTotalWeekSpending();

        loadGraph();

        setStatusAndImageResource();

    }


    private void setStatusAndImageResource() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String[] names = getResources().getStringArray(R.array.items);
                    names = Arrays.copyOfRange(names, 1, names.length);
                    double[] totals = new double[names.length];
                    for (int i = 0; i<names.length; i++) {
                        String childName = "week" + names[i];
                        if(snapshot.hasChild(childName)){
                            totals[i] = Double.parseDouble(snapshot.child(childName).getValue().toString());
                        } else {
                            totals[i] = 0.0;
                        }
                    }
                    double totalSpentAmount;
                    if(snapshot.hasChild("week")){
                        totalSpentAmount =  Double.parseDouble(snapshot.child("week").getValue().toString());
                    } else {
                        totalSpentAmount = 0.0;
                    }

                    double[] ratios = new double[names.length];
                    for (int i = 0; i<names.length; i++) {
                        String childName = "week" + names[i] + "Ratio";
                        if(snapshot.hasChild(childName)){
                            ratios[i] = Double.parseDouble(snapshot.child(childName).getValue().toString());
                        } else {
                            ratios[i] = 0.0;
                        }
                    }
                    double totalSpentAmountRatio;
                    if(snapshot.hasChild("weekBudget")){
                        totalSpentAmountRatio =  Double.parseDouble(snapshot.child("weekBudget").getValue().toString());
                    } else {
                        totalSpentAmountRatio = 0.0;
                    }

                    double percent = (totalSpentAmount/totalSpentAmountRatio) * 100;
                    monthRatioSpending.setText(percent + "% used of: " + totalSpentAmountRatio + " Status:");
                    if(percent < 50) {
                        monthRatioSpending_image.setImageResource(R.drawable.green);
                    } else if (percent > 100) {
                        monthRatioSpending_image.setImageResource(R.drawable.red);
                    } else {
                        monthRatioSpending_image.setImageResource(R.drawable.yellow);
                    }

                    double[] percents = new double[names.length];
                    ImageView[] imageViews = {status_Image_transport, status_Image_food, status_Image_house, status_Image_ent,
                            status_Image_edu, status_Image_cha, status_Image_app, status_Image_hea, status_Image_per, status_Image_oth};
                    TextView[] txtViews = {progress_ratio_transport, progress_ratio_food, progress_ratio_house, progress_ratio_ent,
                            progress_ratio_edu, progress_ratio_cha, progress_ratio_app, progress_ratio_hea,
                            progress_ratio_per, progress_ratio_oth};
                    RelativeLayout[] relativeLayouts = {linearLayoutTransport,linearLayoutFood,linearLayoutFoodHouse,linearLayoutEntertainment,
                            linearLayoutEducation,linearLayoutCharity,linearLayoutApparel,linearLayoutHealth,linearLayoutPersonalExp,
                            linearLayoutOther};

                    for (int i = 0; i<names.length; i++) {
                        if(ratios[i] == 0){
                            relativeLayouts[i].setVisibility(View.GONE);
                            continue;
                        }
                        percents[i] = (totals[i] / ratios[i]) * 100;

                        txtViews[i].setText(percents[i] + "% used of " + ratios[i] + " Status:");
                        if(percents[i] < 50) {
                            imageViews[i].setImageResource(R.drawable.green);
                        } else if (percents[i] > 100) {
                            imageViews[i].setImageResource(R.drawable.red);
                        } else {
                            imageViews[i].setImageResource(R.drawable.yellow);
                        }
                    }

                } else {
                    Toast.makeText(WeeklyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    String[] names = getResources().getStringArray(R.array.items);
                    names = Arrays.copyOfRange(names, 1, names.length);
                    int[] totals = new int[names.length];
                    for (int i = 0; i<names.length; i++) {
                        String childName = "week" + names[i];
                        if(snapshot.hasChild(childName)){
                            totals[i] = Integer.parseInt(snapshot.child(childName).getValue().toString());
                        } else {
                            totals[i] = 0;
                        }
                        //adding data to chart
                        data.add(new ValueDataEntry(names[i], totals[i]));
                    }

                    pie.data(data);
                    pie.title("Week analytics");
                    pie.labels().position("outside");
                    pie.legend().title()
                            .text("Items spent on:")
                            .padding(0d, 0d, 10d, 0d);
                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);
                } else {
                    Toast.makeText(WeeklyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalWeekSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);
        Months months = Months.monthsBetween(epoch, now);

        Query query = expensesRef.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pTotal = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data data = ds.getValue(Data.class);
                        assert data != null;
                        pTotal += data.getAmount();
                    }
                    totalBudgetAmountTextView.setText("Total week's spending: $" + pTotal);
                    monthSpentAmount.setText("Total spent: $" + pTotal);
                } else {
                    totalBudgetAmountTextView.setText("You've not spent this week");
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPersonalRefForCategory(String categoryName, TextView textViewToSet) {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);
        Months months = Months.monthsBetween(epoch, now);

        String itemNweek = categoryName + weeks.getWeeks();
        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pTotal = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data data = ds.getValue(Data.class);
                        assert data != null;
                        pTotal += data.getAmount();
                    }
                    textViewToSet.setText("Spent: $" + pTotal);
                } else {
                    textViewToSet.setVisibility(View.GONE);
                }
                personalRef.child("week" + categoryName).setValue(pTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekTransportExpenses() {
        setPersonalRefForCategory("Transport", analyticsTransportAmount);
    }

    private void getTotalWeekFoodExpenses() {
        setPersonalRefForCategory("Food", analyticsFoodAmount);
    }

    private void getTotalWeekHouseExpenses() {
        setPersonalRefForCategory("House", analyticsHouseExpensesAmount);
    }

    private void getTotalWeekEntertainmentExpenses() {
        setPersonalRefForCategory("Entertainment", analyticsEntertainmentAmount);
    }

    private void getTotalWeekEducationExpenses() {
        setPersonalRefForCategory("Education", analyticsEducationAmount);
    }

    private void getTotalWeekCharityExpenses() {
        setPersonalRefForCategory("Charity", analyticsCharityAmount);
    }

    private void getTotalWeekApparelExpenses() {
        setPersonalRefForCategory("Apparel", analyticsApparelAmount);
    }

    private void getTotalWeekHealthExpenses() {
        setPersonalRefForCategory("Health", analyticsHealthAmount);
    }

    private void getTotalWeekPersonalExpenses() {
        setPersonalRefForCategory("Personal", analyticsPersonalExpensesAmount);
    }

    private void getTotalWeekOtherExpenses() {
        setPersonalRefForCategory("Other", analyticsOtherAmount);
    }



}