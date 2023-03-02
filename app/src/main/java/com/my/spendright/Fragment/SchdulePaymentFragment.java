package com.my.spendright.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.R;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.act.SchdulePaymentMethod;
import com.my.spendright.databinding.FragmentGivingBinding;
import com.my.spendright.databinding.FragmentSchdulePaymentBinding;

public class SchdulePaymentFragment extends Fragment {

    FragmentSchdulePaymentBinding binding;
    private HomeModal[] arrayList;
    private View root;
    public static ImageView navigation_drawer;
    private SchdulePaymentMethod.Qr_DetailsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schdule_payment, container, false);

        binding.addImgAccount.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SchdulePayment.class));
        });

        setUpUi();

        return binding.getRoot();
    }

    private void setUpUi() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Investment"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Giving"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Subscription"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new SchdulePaymentMethod.Qr_DetailsAdapter(getActivity(),getChildFragmentManager(), binding.tabLayout.getTabCount());

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public class Qr_DetailsAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public Qr_DetailsAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    InvestmentFragment recents = new InvestmentFragment();
                    return recents;

                case 1:
                    GivingFragment recents1 = new GivingFragment();
                    return recents1;

                case 2:
                    SubcriptionFragment recents11 = new SubcriptionFragment();
                    return recents11;

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
        }
    }
}