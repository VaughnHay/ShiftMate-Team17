package com.example.shiftmateOPSC;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Users2> list;
    public HourAdapter(Context context, ArrayList<Users2> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users2 users2 = list.get(position);
        holder.category.setText(Users2.getCategory());
        holder.hours.setText(String.valueOf(Users2.getTotalHours()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView category, hours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.CategorytxtView);
            hours = itemView.findViewById(R.id.HourstxtView);
        }
    }

}
