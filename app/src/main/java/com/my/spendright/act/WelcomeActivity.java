package com.my.spendright.act;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.my.spendright.MainActivity;
import com.my.spendright.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{


    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Context mContext;
    private TextView tvSignIn, tvSkip;
    private RelativeLayout RRStart;

    Boolean[] checkBoxState;
    private static final String TAG = "fireBaseToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            // Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            //  Log.e(TAG, "printHashKey()", e);
        }


        mContext= WelcomeActivity.this;
        viewPager = findViewById(R.id.view_pager);
        tvSkip = findViewById(R.id.btn_next);
       // tvSignIn = findViewById(R.id.tvSignIn);
        dotsLayout = findViewById(R.id.layoutDots);
        RRStart = findViewById(R.id.RRStart);

       // RRStart = findViewById(R.id.RRStart);

        // btnSkip = (Button) findViewById(R.id.btn_skip);
        // btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[] {
                R.layout.intro_screen1,
                R.layout.intro_screen2,
                R.layout.intro_screen3,
                R.layout.intro_screen4
        };

        RRStart.setOnClickListener(v -> {

            startActivity(new Intent(WelcomeActivity.this,SelectAccount.class));
            finish();

        });
        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        tvSkip.setOnClickListener(this);

        /*RRStart.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this,SelectAccount.class));

        });*/
       // tvSignIn.setOnClickListener(this);

    }

    private void addBottomDots(int currentPage) {

        dotsLayout.removeAllViews();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(30,30);

        for (int i = 0; i < layouts.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setPadding(5,5,5,5);

            if(currentPage == i) {
                imageView.setImageResource(R.drawable.ovel_yellow);
            }else {
                imageView.setImageResource(R.drawable.ovel);

            }

            dotsLayout.addView(imageView);
        }

        if(currentPage == 3)
        {
            RRStart.setVisibility(View.VISIBLE);
        }else
        {
            RRStart.setVisibility(View.GONE);
        }

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        // startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            Intent intentSign = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intentSign);
            finish();
        }

    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            if(position == 3)
            {
                RRStart.setVisibility(View.VISIBLE);
            }else
            {
                RRStart.setVisibility(View.GONE);
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

}
