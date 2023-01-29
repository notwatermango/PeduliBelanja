package com.notwatermango.pedulibelanja.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.notwatermango.pedulibelanja.R;
import com.notwatermango.pedulibelanja.UpdateCartEvent;
import com.notwatermango.pedulibelanja.model.Item;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Item> cartList;
    Context context;

    public CartAdapter(List<Item> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Item res = cartList.get(position);

        holder.title.setText(res.getNama());
        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.price.setText(String.valueOf(currencyFormatter.format(res.getHarga())));
        holder.quantity.setText(new StringBuilder().append(res.getQuantity()));

        holder.plusBtn.setOnClickListener(v-> plusCartItem(holder,cartList.get(position)));

        holder.minBtn.setOnClickListener(v-> minusCartItem(holder,cartList.get(position)));

    }

    private void plusCartItem(CartViewHolder holder, Item cart){
        if(cart.getQuantity()<100){
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity()*cart.getHarga());

            holder.quantity.setText(new StringBuilder().append(cart.getQuantity()));

            // update now
            EventBus.getDefault().postSticky(new UpdateCartEvent());
        }
    }

    private void minusCartItem(CartViewHolder holder, Item cart){
        if(cart.getQuantity()>0){
            cart.setQuantity(cart.getQuantity()-1);
            cart.setTotalPrice(cart.getQuantity()*cart.getHarga());

            holder.quantity.setText(new StringBuilder().append(cart.getQuantity()));

            // update now
            EventBus.getDefault().postSticky(new UpdateCartEvent());
        }
    }




    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,price,quantity;
        ImageView minBtn, plusBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.item_img);
            title =itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.text_quantity);
            minBtn = itemView.findViewById(R.id.minus_btn);
            plusBtn = itemView.findViewById(R.id.plus_btn);
        }
    }

    public interface OnClickCartListener{
        void onClickCartListener(Item cart);

    }

}
