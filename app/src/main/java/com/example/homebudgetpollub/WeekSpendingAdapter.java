package com.example.homebudgetpollub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeekSpendingAdapter extends RecyclerView.Adapter<WeekSpendingAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Data> myDataList;

    public WeekSpendingAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout, parent, false);
        return new WeekSpendingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data data = myDataList.get(position);
        holder.item.setText("Category: " + data.getItem());
        holder.amount.setText("Amount: " + data.getAmount());
        holder.date.setText("Date: " + data.getDate());
        holder.note.setText("Note: " + data.getNotes());
        holder.imageView.setImageResource(ImagesProvider.provideItemsWithImages(data));
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item, amount, date, note;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            note = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
