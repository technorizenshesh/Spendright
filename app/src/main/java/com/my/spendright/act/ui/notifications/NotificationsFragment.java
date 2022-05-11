package com.my.spendright.act.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.my.spendright.R;
import com.my.spendright.act.FIlterActivity;
import com.my.spendright.act.SettingActivity;
import com.my.spendright.databinding.FragmentNotificationsBinding;


public class NotificationsFragment extends Fragment {


    FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifications, container, false);

       binding.imgSetting.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), SettingActivity.class));
       });

       binding.imgFilter.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), FIlterActivity.class));
       });

       binding.imgGraph.setOnClickListener(v -> {
           binding.llgraph.setVisibility(View.GONE);
           binding.RRgraph.setVisibility(View.VISIBLE);
       });

       binding.imgGraph1.setOnClickListener(v -> {
           binding.llgraph.setVisibility(View.VISIBLE);
           binding.RRgraph.setVisibility(View.GONE);
       });

        return binding.getRoot();
    }
}