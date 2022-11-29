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
    protected DatabaseReference expensesRef;
    protected DatabaseReference personalRef;

    protected TextView totalBudgetAmountTextView;
    protected TextView analyticsTransportAmount;
    protected TextView analyticsFoodAmount;
    protected TextView analyticsHouseExpensesAmount;
    protected TextView analyticsEntertainmentAmount;
    protected TextView analyticsEducationAmount;
    protected TextView analyticsCharityAmount;
    protected TextView analyticsApparelAmount;
    protected TextView analyticsHealthAmount;
    protected TextView analyticsPersonalExpensesAmount;
    protected TextView analyticsOtherAmount;
    protected TextView monthSpentAmount;
    protected TextView monthRatioSpending;

    protected RelativeLayout linearLayoutTransport;
    protected RelativeLayout linearLayoutFood;
    protected RelativeLayout linearLayoutFoodHouse;
    protected RelativeLayout linearLayoutEntertainment;
    protected RelativeLayout linearLayoutEducation;
    protected RelativeLayout linearLayoutCharity;
    protected RelativeLayout linearLayoutApparel;
    protected RelativeLayout linearLayoutHealth;
    protected RelativeLayout linearLayoutPersonalExp;
    protected RelativeLayout linearLayoutOther;
    protected RelativeLayout linearLayoutAnalysis;

    protected AnyChartView anyChartView;

    protected TextView progress_ratio_transport;
    protected TextView progress_ratio_food;
    protected TextView progress_ratio_house;
    protected TextView progress_ratio_ent;
    protected TextView progress_ratio_edu;
    protected TextView progress_ratio_cha;
    protected TextView progress_ratio_app;
    protected TextView progress_ratio_hea;
    protected TextView progress_ratio_per;
    protected TextView progress_ratio_oth;

    protected ImageView status_Image_transport;
    protected ImageView status_Image_food;
    protected ImageView status_Image_house;
    protected ImageView status_Image_ent;
    protected ImageView status_Image_edu;
    protected ImageView status_Image_cha;
    protected ImageView status_Image_app;
    protected ImageView status_Image_hea;
    protected ImageView status_Image_per;
    protected ImageView status_Image_oth;
    protected ImageView monthRatioSpending_image;
}
