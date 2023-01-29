package com.notwatermango.pedulibelanja.ui.dashboard;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.notwatermango.pedulibelanja.UpdateCartEvent;
import com.notwatermango.pedulibelanja.adapters.CartAdapter;
import com.notwatermango.pedulibelanja.databinding.FragmentDashboardBinding;
import com.notwatermango.pedulibelanja.model.Item;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.notwatermango.pedulibelanja.model.Daging;
import com.notwatermango.pedulibelanja.model.Sayur;
import com.notwatermango.pedulibelanja.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class DashboardFragment extends Fragment {
    RecyclerView recyclerView;
    public ArrayList<Item> cartList = new ArrayList<Item>();
    TextView totalPrice;
    private FragmentDashboardBinding binding;

    @Override
    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class)){
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        final AppCompatButton save_btn = getActivity().findViewById(R.id.simpan_button);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveList(cartList, "cartList");

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(UpdateCartEvent event){
        loadCart();
        Log.d("b", "onUpdateCart: Ok");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cartList.add(new Sayur(1, "Tomat", 1000.0, "Buah"));
        cartList.add(new Sayur(2, "Kangkung", 2000.0, "Sayuran Hijau"));
        cartList.add(new Sayur(3, "Bayam", 3000.0, "Sayuran Hijau"));
        cartList.add(new Daging(4, "Daging Sapi", 12000.0, "Daging Merah"));
        cartList.add(new Daging(5, "Sosis", 10000.0, "Daging Olahan"));

        recyclerView = root.findViewById(R.id.cart_recycle_view);

        totalPrice = root.findViewById(R.id.total_price);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),linearLayout.getOrientation()));


        loadCart();

        return root;
    }


    // should be invoked every cart item update
    public void loadCart() {
        double sum = 0;
        for (Item cart : cartList) {
            sum += cart.getTotalPrice();
        }
        CartAdapter cartAdapter = new CartAdapter(cartList, getActivity());
        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        totalPrice.setText(String.valueOf(currencyFormatter.format(sum)));
        if (sum > 20000) {
            totalPrice.setTextColor(Color.RED);
        } else {
            totalPrice.setTextColor(Color.BLACK);
        }
        recyclerView.setAdapter(cartAdapter);
    }

    private void saveList(ArrayList<Item> l1, String key1) {
        ArrayList<Item> l2 = new ArrayList<Item>();
        l1.forEach((item)->{
            if (item.getQuantity() != 0) l2.add(item);
        });

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor prefsEditor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(l2);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput("savedata.txt", getContext().MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


        prefsEditor.putString(key1, json);

        prefsEditor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

