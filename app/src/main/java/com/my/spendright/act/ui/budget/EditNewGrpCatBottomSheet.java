package com.my.spendright.act.ui.budget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.act.ui.settings.model.BudgetCategoryModel;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.FragmentAddNewGrpCateBinding;
import com.my.spendright.databinding.FragmentEditNewGrpCateBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditNewGrpCatBottomSheet  extends BottomSheetDialogFragment {
    public String TAG = "EditNewGrpCatBottomSheet";
    BottomSheetDialog dialog;
    FragmentEditNewGrpCateBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;

    SessionManager sessionManager;
    private String catId="";
    private String budDate="";

    private String bGrpId="";

    BudgetCategoryModel.Data data;

    ArrayList<IncomeExpenseCatModel.Category> arrayList;
    IncomeExpenseCatModel incomeExpenseCatModel;


    public EditNewGrpCatBottomSheet(BudgetCategoryModel.Data data) {
        this.data = data;
    }


    public EditNewGrpCatBottomSheet callBack(CreateVirtualListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_edit_new_grp_cate, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return dialog;
    }

    private void initBinding() {
        budDate = data.getBcatDueDate();
        binding.tvCategoryName.setText(data.getBcatCategoryName());
        binding.edtAmount.setText( data.getBcatBudgetAmount());
        //   binding.edtAmount.setText( Preference.doubleToStringNoDecimal(Double.parseDouble(data.getBcatBudgetAmount())));
        binding.tvDate.setText(budDate);
        arrayList = new ArrayList<>();

        sessionManager = new SessionManager(getActivity());


        binding.rlCategory.setOnClickListener(view -> {
            if(arrayList.size()>0) showDropDownCategory(view,binding.tvCategoryName,arrayList);

        });

        binding.RRAdd.setOnClickListener(view -> {
            validation();
        });

        binding.rlDate.setOnClickListener(view -> {
            final Calendar myCalendar = Calendar.getInstance();

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "dd-MMM-yyyy"; // your format yyyy-MM-dd"
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    budDate = sdf.format(myCalendar.getTime());
                    binding.tvDate.setText(budDate);

                }

            };
            DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
            // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            Log.e("-------",myCalendar.getTimeInMillis()+"");

            datePickerDialog.show();

        });


        if (sessionManager.isNetworkAvailable()) getAllBudgetCategories();
        else Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
    }

    private void validation() {
       /* if (catId.equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_select_category), Toast.LENGTH_LONG).show();*/
         if (binding.edtAmount.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_enter_amount), Toast.LENGTH_LONG).show();
        else if (budDate.equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_select_date), Toast.LENGTH_LONG).show();
        else  {


            if (sessionManager.isNetworkAvailable()) updateBudgetCategory();
            else Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }





    private void updateBudgetCategory() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("bcat_id",data.getBcatId());
        requestBody.put("bcat_budget_amount",binding.edtAmount.getText().toString() );
        requestBody.put("bcat_due_date",budDate);
        requestBody.put("bcat_user_id", sessionManager.getUserID());
      //  requestBody.put("bcat_group_id",bGrpId);

        Log.e(TAG,"update grp BudgetRequest=="+requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_update_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "update grp BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(),getString(R.string.group_created_successfully), Toast.LENGTH_SHORT).show();
                        listener.onVirtual("3","");
                        dialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownCategory(View v, TextView textView, List<IncomeExpenseCatModel.Category> stringList) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getCatName());
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).getCatName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    catId = stringList.get(i).getCatId();
                    textView.setText(menuItem.getTitle());
                    binding.edtAmount.setText(incomeExpenseCatModel.getAdminMinimumAmount());

                }
            }


            return true;
        });
        popupMenu.show();
    }



    private void getAllBudgetCategories() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cat_user_id", sessionManager.getUserID());
        requestBody.put("cat_type","EXPENSE");
        Log.e(TAG, "getAll category BudgetRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_get_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "getAll category BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        incomeExpenseCatModel = new Gson().fromJson(stringResponse, IncomeExpenseCatModel.class);
                        arrayList.clear();
                        arrayList.addAll(incomeExpenseCatModel.getCategories());
                       // catId = incomeExpenseCatModel.getCategories().get(0).getCatId();
                        //binding.tvCategoryName.setText(incomeExpenseCatModel.getCategories().get(0).getCatName());

                    } else {
                        arrayList.clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }



}