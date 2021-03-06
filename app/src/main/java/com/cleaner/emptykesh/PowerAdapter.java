package com.cleaner.emptykesh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.emptykesh.dto.PowerItem;

import java.util.List;

public class PowerAdapter extends RecyclerView.Adapter<PowerAdapter.MyViewHolder> {

    private List<PowerItem> apps;

    public PowerAdapter(List<PowerItem> apps) {
        this.apps = apps;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.powe_itemlist, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PowerItem app = apps.get(position);
        holder.size.setText(app.getText());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView size;

        public MyViewHolder(View view) {
            super(view);
            size = (TextView) view.findViewById(R.id.items);
        }
    }
}
