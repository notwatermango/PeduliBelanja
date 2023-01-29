package com.notwatermango.pedulibelanja.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.notwatermango.pedulibelanja.R;
import com.notwatermango.pedulibelanja.model.Item;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    List<Item> todoList;
    Context context;

    public TodoAdapter(List<Item> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item,parent,false);

        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Item res = todoList.get(position);

        holder.title.setText(res.getNama());
        holder.quantity.setText(new StringBuilder().append(res.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder  {
        TextView title, quantity;

        public TodoViewHolder(@NonNull View todoView) {
            super(todoView);
            title = todoView.findViewById(R.id.todo_title);
            quantity = todoView.findViewById(R.id.todo_quantity);

        }
    }
}
