package com.my.spendright.act;

import android.content.Context;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.my.spendright.Fragment.GivingFragment;
import com.my.spendright.Fragment.InvestmentFragment;
import com.my.spendright.Fragment.SubcriptionFragment;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.R;
import com.my.spendright.act.ui.home.HomeFragment;
import com.my.spendright.databinding.ActivitySchdulePaymentBinding;
import com.my.spendright.databinding.ActivitySchdulePaymentMethodBinding;

public class SchdulePaymentMethod extends AppCompatActivity {

    ActivitySchdulePaymentMethodBinding binding;

    private HomeModal[] arrayList;
    private View root;
    public static ImageView navigation_drawer;
    private Qr_DetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_schdule_payment_method);

        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });

        setUpUi();
    }

    private void setUpUi() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Investment"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Giving"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Subscription"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new Qr_DetailsAdapter(this,getSupportFragmentManager(), binding.tabLayout.getTabCount());

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

    public static class Qr_DetailsAdapter extends FragmentPagerAdapter {

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