package com.my.spendright.act.ui.expense;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.AddBeneficiaryBottomSheet;
import com.my.spendright.act.ui.budget.model.BankListModel;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.ActivityFilterBinding;
import com.my.spendright.databinding.FragmentAddBeneficiaryBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilterBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "FilterBottomSheet";
    BottomSheetDialog dialog;
    ActivityFilterBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public FilterListener listener;

    SessionManager sessionManager;

    private String id = "";

    private int mYear, mMonth,mDay;
    String paymentdate="",ToDate="",frmDate="", FrmAmt="",ToAmt="",totalInCOmes="",totalExpences="",Transaction_Type="expense";

    ArrayList<String>stringArrayList;

    public FilterBottomSheet(String id) {
        this.id = id;
    }


    public FilterBottomSheet callBack(FilterListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.activity_filter, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return dialog;
    }

    private void initBinding() {

        stringArrayList = new ArrayList<>();

        stringArrayList.add("expense");
        stringArrayList.add("income");


        binding.RRBack.setOnClickListener(view -> dialog.dismiss());


        binding.llType.setOnClickListener(view -> showDropDownTypeList(view,binding.tvType,stringArrayList));


        binding.llFrmDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);

                            // paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

                            paymentdate = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            frmDate = paymentdate;

                            binding.txtFormDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

            //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();

        });

        binding.llToDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            //paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            paymentdate = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            ToDate = paymentdate;
                            binding.txtToDate.setText(paymentdate);
                        }
                    }, mYear, mMonth, mDay);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();

        });

        binding.RRFilter.setOnClickListener(v -> {
             validation();




        });


        binding.txtReset.setOnClickListener(v -> {


            binding.txtFormDate.setText("00-00-00");
            binding.txtToDate.setText("00-00-00");
            binding.edtFrmAmt.setText("00");
            binding.edtToAmt.setText("00");
          //  binding.txtAll.setText("All transactions");
            binding.RadbtnAll.setChecked(true);

            paymentdate="";
            ToDate="";
            frmDate="";
            FrmAmt="";
            ToAmt="";
            Transaction_Type="";

        });

    }

    private void validation() {
        FrmAmt = binding.edtFrmAmt.getText().toString();
        ToAmt = binding.edtToAmt.getText().toString();

        if(frmDate.equalsIgnoreCase("")) Toast.makeText(getActivity(), getString(R.string.select_from_date), Toast.LENGTH_SHORT).show();
       else if(ToDate.equalsIgnoreCase("")) Toast.makeText(getActivity(), getString(R.string.select_to_date), Toast.LENGTH_SHORT).show();
       else if(FrmAmt.equalsIgnoreCase("")) Toast.makeText(getActivity(), getString(R.string.select_from_amt), Toast.LENGTH_SHORT).show();
       else if(ToAmt.equalsIgnoreCase("")) Toast.makeText(getActivity(), getString(R.string.select_to_amt), Toast.LENGTH_SHORT).show();
       else if(Transaction_Type.equalsIgnoreCase("")) Toast.makeText(getActivity(), getString(R.string.select_type), Toast.LENGTH_SHORT).show();
        else {
            listener.onFilter(id,frmDate,ToDate,FrmAmt,ToAmt,Transaction_Type);
            dialog.dismiss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownTypeList(View v, TextView textView, List<String> stringList) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i));
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).equalsIgnoreCase(menuItem.getTitle().toString())) {
                    Transaction_Type = stringList.get(i);
                    textView.setText(menuItem.getTitle() + " " + "Transactions");
                    //  listener.onExpense(filterText);


                }
            }
            return true;
        });
        popupMenu.show();
    }

}