package com.my.spendright.act.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.R;
import com.my.spendright.act.AddTrasactionScreen;
import com.my.spendright.act.UpdatedAccountInfoTrasaction;
import com.my.spendright.adapter.AccontTrasactionAdapter;
import com.my.spendright.adapter.SubCategoryAdapter;
import com.my.spendright.utils.Preference;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<PdfRenderer.Page> pages = new ArrayList<>();
    ArrayList<GetBudgetActTransaction.AccountDetail> modelList=new ArrayList<>();


    private AccontTrasactionAdapter mAdapter1;
    ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> modelListCategory=new ArrayList<>();

    public CustomPagerAdapter(Context context, ArrayList<GetBudgetActTransaction.AccountDetail> modelList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.modelList = modelList;
    }

    // Returns the number of pages to be displayed in the ViewPager.
    @Override
    public int getCount() {
        return modelList.size();
    }

    // Returns true if a particular object (page) is from a particular page
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // This method should create the page for the given position passed to it as an argument.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate the layout for the page
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        // Find and populate data into the page (i.e set the image)
       // ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        TextView txtEmty = (TextView) itemView.findViewById(R.id.txtEmty);
        TextView txtBuegetAc = (TextView) itemView.findViewById(R.id.txtBuegetAc);
        TextView txtTotalExpence = (TextView) itemView.findViewById(R.id.txtTotalExpence);
        TextView txtInCOme = (TextView) itemView.findViewById(R.id.txtInCOme);
        TextView txtCurrentBalnce = (TextView) itemView.findViewById(R.id.txtCurrentBalnce);
        TextView txtAccountName = (TextView) itemView.findViewById(R.id.txtAccountName);
        ImageView imgAddTrasaction = (ImageView) itemView.findViewById(R.id.imgAddTrasaction);
        RecyclerView recyclerCategory = (RecyclerView) itemView.findViewById(R.id.rcycleTransaction);

        txtTotalExpence.setText(modelList.get(position).getExpense());

        txtInCOme.setText(modelList.get(position).getIncome());

        txtCurrentBalnce.setText(modelList.get(position).getAvailableBalance());

        txtAccountName.setText(modelList.get(position).getHolderName());

        txtBuegetAc.setText(Preference.doubleToStringNoDecimal(Double.parseDouble(modelList.get(position).getCurrentBalance())));

        imgAddTrasaction.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, AddTrasactionScreen.class).putExtra("account_budget_id",modelList.get(position).getId()));
        });
        // Add the page to the container

        if(modelList.get(position).getTransaction().size() != 0)
        {
            txtEmty.setVisibility(View.GONE);
            recyclerCategory.setVisibility(View.VISIBLE);
            setAdapter(recyclerCategory, (ArrayList<GetBudgetActTransaction.AccountDetail.Transaction>) modelList.get(position).getTransaction());

        }else
        {
            recyclerCategory.setVisibility(View.GONE);
            txtEmty.setVisibility(View.VISIBLE);

        }


        container.addView(itemView);
        // Return the page
        return itemView;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void setAdapter(RecyclerView recyclerView, ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> categoryDetail) {

        mAdapter1 = new AccontTrasactionAdapter(mContext,categoryDetail);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter1);
        mAdapter1.SetOnItemClickListener(new AccontTrasactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetBudgetActTransaction.AccountDetail.Transaction model) {

                mContext.startActivity(new Intent(mContext, UpdatedAccountInfoTrasaction.class).putExtra("trasaction_id",model.getId()));

            }
        });
    }

}