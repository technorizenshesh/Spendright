package com.my.spendright.act.ui.expense;

import static com.my.spendright.act.ui.expense.ExpenseFragment.sendToServer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.R;
import com.my.spendright.act.AddTrasactionScreen;
import com.my.spendright.act.UpdatedAccountInfoTrasaction;
import com.my.spendright.act.ui.budget.AddBeneficiaryBottomSheet;
import com.my.spendright.act.ui.budget.model.BankListModel;
import com.my.spendright.adapter.AccontTrasactionAdapter;
import com.my.spendright.utils.Preference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter implements FilterListener{

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<PdfRenderer.Page> pages = new ArrayList<>();
    ArrayList<GetBudgetActTransaction.AccountDetail> modelList=new ArrayList<>();


    String filterText;

    private AccontTrasactionAdapter mAdapter1;
    ArrayList<String>filterList;
    ExpenseListener listener;
    double totalIncome=0.0,totalExpense=0.0;

    public CustomPagerAdapter(Context context, ArrayList<GetBudgetActTransaction.AccountDetail> modelList,  ArrayList<String>filterList, ExpenseListener listener) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.modelList = modelList;
        this.filterList = filterList;
        this.listener = listener;
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
        ImageView payMe = (ImageView) itemView.findViewById(R.id.payMe);

        RecyclerView recyclerCategory = (RecyclerView) itemView.findViewById(R.id.rcycleTransaction);
        TextView tvFilter = (TextView) itemView.findViewById(R.id.tvFilter);



     //   txtTotalExpence.setText("₦"+modelList.get(position).getExpense());

     //   txtInCOme.setText("₦"+modelList.get(position).getIncome());

        txtCurrentBalnce.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(modelList.get(position).getAvailableBalance().replace(",",""))));
        txtAccountName.setText(modelList.get(position).getHolderName());

        txtBuegetAc.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(modelList.get(position).getCurrentBalance())));

        imgAddTrasaction.setOnClickListener(v -> {
            ExpenseFragment.pagerPosition = position;
            mContext.startActivity(new Intent(mContext, AddTrasactionScreen.class).putExtra("account_budget_id",modelList.get(position).getId()));
        });
        // Add the page to the container

        if(modelList.get(position).getTransaction().size() != 0)
        {
            txtEmty.setVisibility(View.GONE);
            recyclerCategory.setVisibility(View.VISIBLE);
            setAdapter(recyclerCategory, (ArrayList<GetBudgetActTransaction.AccountDetail.Transaction>) modelList.get(position).getTransaction(),txtInCOme,txtTotalExpence);

        }else
        {
            recyclerCategory.setVisibility(View.GONE);
            txtEmty.setVisibility(View.VISIBLE);

        }


        tvFilter.setOnClickListener(view -> showDropDownFilterList(view,tvFilter,filterList, (ArrayList<GetBudgetActTransaction.AccountDetail.Transaction>) modelList.get(position).getTransaction(),recyclerCategory,txtInCOme,txtTotalExpence));
       // setUpNavigationDrawer(drawer);

        payMe.setOnClickListener(v -> {
          callFilter(modelList.get(position).getId());

        });

        container.addView(itemView);
        // Return the page
        return itemView;
    }

    private void callFilter(String id) {
        new FilterBottomSheet(id).callBack(this::onFilter).show(((FragmentActivity)mContext).getSupportFragmentManager(),"");

    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void setAdapter(RecyclerView recyclerView, ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> categoryDetail,TextView textIncome,TextView textExpense) {
            totalIncome = 0.0;
            totalExpense = 0.0;
        for(int i = 0 ;i<categoryDetail.size();i++){
            if(categoryDetail.get(i).getType().equalsIgnoreCase("expense")){
                totalExpense = totalExpense + (Double.parseDouble(categoryDetail.get(i).getTransactionAmount())); /*+ Double.parseDouble(categoryDetail.get(i).getAdminFee()));*/
            }
            else {
                totalIncome = totalIncome + (Double.parseDouble(categoryDetail.get(i).getTransactionAmount())); /*+ Double.parseDouble(categoryDetail.get(i).getAdminFee()));*/

            }
        }
        textExpense.setText("₦"+Preference.doubleToStringNoDecimal(totalExpense));
        textIncome.setText("₦"+Preference.doubleToStringNoDecimal(totalIncome));



        mAdapter1 = new AccontTrasactionAdapter(mContext,categoryDetail);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter1);
        mAdapter1.SetOnItemClickListener(new AccontTrasactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetBudgetActTransaction.AccountDetail.Transaction model) {
                ExpenseFragment.pagerPosition = position;
                mContext.startActivity(new Intent(mContext, UpdatedAccountInfoTrasaction.class).putExtra("trasaction_id",model.getId()));

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownFilterList(View v, TextView textView, List<String> stringList,ArrayList<GetBudgetActTransaction.AccountDetail.Transaction>mainList, RecyclerView recyclerCategory,TextView textIncome, TextView textExpense) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i));
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).equalsIgnoreCase(menuItem.getTitle().toString())) {
                    filterText = stringList.get(i);
                    textView.setText(menuItem.getTitle() + " " + "Transactions");
                  //  listener.onExpense(filterText);

                    if(filterText.equalsIgnoreCase("Daily")) {
                        sendToServer.clear();
                        sendToServer.addAll(filterData( Preference.getCurrentDaily(),mainList));
                        setAdapter(recyclerCategory,sendToServer,textIncome,textExpense);
                    }
                   else if(filterText.equalsIgnoreCase("Weekly"))  {
                        sendToServer.clear();
                        sendToServer.addAll(filterData( Preference.getCurrentWeek(),mainList));
                       setAdapter(recyclerCategory,sendToServer,textIncome,textExpense );
                    }
                   else if(filterText.equalsIgnoreCase("Monthly"))  {
                        sendToServer.clear();
                        sendToServer.addAll(filterData( Preference.getCurrentMonth(),mainList));
                       setAdapter(recyclerCategory,sendToServer,textIncome,textExpense );
                    }
                   else if(filterText.equalsIgnoreCase("Yearly"))  {
                        sendToServer.clear();
                        sendToServer.addAll(filterData( Preference.getCurrentYear(),mainList));
                       setAdapter(recyclerCategory, sendToServer,textIncome,textExpense);
                    }
                   else {
                       sendToServer.clear();
                       sendToServer.addAll(mainList);
                       setAdapter(recyclerCategory,mainList,textIncome,textExpense);
                    }
                }
            }
            return true;
        });
        popupMenu.show();
    }

    public ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> filterData(ArrayList<String> filterList , ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> mainList){
        ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> list = new ArrayList<>();
        for(int i=0;i< mainList.size();i++){
            for (int j=0;j<filterList.size();j++){
                if(mainList.get(i).getDateTime().equalsIgnoreCase(filterList.get(j))) list.add((mainList.get(i))) ;

            }


        }

        return list;

    }


    @Override
    public void onFilter(String id, String stDate, String enDate, String stAmt, String enAmt, String transType) {
        listener.onExpense(id,stDate,enDate,stAmt,enAmt,transType);
    }
}