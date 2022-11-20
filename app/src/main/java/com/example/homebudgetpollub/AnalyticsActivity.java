package com.example.homebudgetpollub;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AnalyticsActivity extends AppCompatActivity {
    protected Toolbar settingsToolbar;

    protected FirebaseAuth mAuth;
    protected String onlineUserId = "";
    protected DatabaseReference expensesRef, personalRef;

    protected TextView totalBudgetAmountTextView, analyticsTransportAmount,analyticsFoodAmount,analyticsHouseExpensesAmount,
            analyticsEntertainmentAmount,analyticsEducationAmount,analyticsCharityAmount,analyticsApparelAmount,
            analyticsHealthAmount,analyticsPersonalExpensesAmount,analyticsOtherAmount, monthSpentAmount, monthRatioSpending;

    protected RelativeLayout linearLayoutTransport,linearLayoutFood,linearLayoutFoodHouse,linearLayoutEntertainment,
            linearLayoutEducation,linearLayoutCharity,linearLayoutApparel,linearLayoutHealth,linearLayoutPersonalExp,
            linearLayoutOther,linearLayoutAnalysis;

    protected AnyChartView anyChartView;

    protected TextView progress_ratio_transport, progress_ratio_food, progress_ratio_house, progress_ratio_ent,
            progress_ratio_edu, progress_ratio_cha, progress_ratio_app, progress_ratio_hea,
            progress_ratio_per, progress_ratio_oth;

    protected ImageView status_Image_transport, status_Image_food, status_Image_house, status_Image_ent,
            status_Image_edu, status_Image_cha, status_Image_app, status_Image_hea, status_Image_per, status_Image_oth, monthRatioSpending_image;
}
