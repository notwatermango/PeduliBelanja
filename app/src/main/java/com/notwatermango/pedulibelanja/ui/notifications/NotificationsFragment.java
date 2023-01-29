package com.notwatermango.pedulibelanja.ui.notifications;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.notwatermango.pedulibelanja.R;
import com.notwatermango.pedulibelanja.UpdateCartEvent;
import com.notwatermango.pedulibelanja.adapters.TodoAdapter;
import com.notwatermango.pedulibelanja.databinding.FragmentNotificationsBinding;
import com.notwatermango.pedulibelanja.model.Item;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    public ArrayList<Item> todoList = new ArrayList<Item>();
    private FragmentNotificationsBinding binding;

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
        todoList = loadListFromFile();




    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(UpdateCartEvent event){
        populateList();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.item_recycle_view);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayout.getOrientation()));

        populateList();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void populateList() {
        todoList = loadListFromFile();
        TodoAdapter todoAdapter = new TodoAdapter(todoList, getActivity());
        recyclerView.setAdapter(todoAdapter);
    }

    private ArrayList<Item> loadListFromFile() {
        String ret = "";
        try {
            InputStream inputStream = getContext().openFileInput("savedata.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((readString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(readString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("read", "Cannot read savedata.txt");
        } catch (IOException e) {
            Log.e("read", "Cannot read savedata.txt");
        }

        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<Item>>() {
        }.getType();

        return gson.fromJson(ret, type);
    }

    private ArrayList<Item> loadList(String yourKey) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        Gson gson = new Gson();
        String json = prefs.getString(yourKey, null);

        Type type = new TypeToken<ArrayList<Item>>() {}.getType();

        return gson.fromJson(json, type);
    }
}