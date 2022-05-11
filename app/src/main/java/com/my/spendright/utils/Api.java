package com.my.spendright.utils;



import com.my.spendright.Model.AddAcountModel;
import com.my.spendright.Model.AddReportModal;
import com.my.spendright.Model.CreateGroupModel;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.Model.ReportModal;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String Api_signup = "signup";
    String Api_login = "login";
    String Api_get_account_category = "get_account_category";
    String Api_get_account_detail = "get_account_detail";
    String Api_add_account= "add_account";
    String get_profile_data= "get_profile_data";


    //SetBudget
    String Api_create_group= "create_group";
    String Api_add_category_group= "add_category_group";
    String Api_get_group_expence= "get_group_expence";
    String Api_delete_group= "delete_group";
    String get_stateMent= "get_vtpass_history";

    String get_vtpass_book_payment= "get_vtpass_book_payment";
    String Api_add_vtpass_book_payment  = "add_vtpass_book_payment";

    @FormUrlEncoded
    @POST(Api_signup)
    Call<LoginModel> Api_signup(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String mobile,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("city") String city,
            @Field("country_residence") String country_residence,
            @Field("dob") String dob,
            @Field("status") String status,
            @Field("register_id") String register_id
    );

    @FormUrlEncoded
    @POST(Api_login)
    Call<LoginModel> Api_login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST(Api_get_account_category)
    Call<GetAccountCategory> Api_get_account_category();

    @FormUrlEncoded
    @POST(Api_add_account)
    Call<AddAcountModel> Api_add_account(
            @Field("account_id") String account_id,
            @Field("user_id") String user_id,
            @Field("holder_name") String holder_name,
            @Field("current_balance") String current_balance
    );

    @FormUrlEncoded
    @POST(Api_get_group_expence)
    Call<GetSetbudgetExpence> Api_get_group_expence(
            @Field("user_id") String user_id,
            @Field("account_id") String account_id
    );

    @FormUrlEncoded
    @POST(Api_delete_group)
    Call<ResponseBody> Api_delete_group(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(Api_get_account_detail)
    Call<GetCategoryModelNew> Api_get_account_detail(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(Api_create_group)
    Call<CreateGroupModel> Api_create_group(
            @Field("user_id") String user_id,
            @Field("group_name") String group_name,
            @Field("group_image") String group_image,
            @Field("account_id") String account_id
    );

    @FormUrlEncoded
    @POST(Api_add_category_group)
    Call<AddCategoryModel> Api_add_category_group(
            @Field("user_id") String user_id,
            @Field("group_id") String group_id,
            @Field("account_id") String account_id,
            @Field("category_name") String category_name,
            @Field("amount") String amount,
            @Field("select_month_week") String select_month_week,
            @Field("select_end__day_month_week") String select_end__day_month_week
    );

    @FormUrlEncoded
    @POST(get_profile_data)
    Call<GetProfileModel> Api_get_profile_data(
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST(Api_add_vtpass_book_payment)
    Call<AddReportModal> Api_add_vtpass_book_payment(
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("amount") String amount,
            @Field("service_id") String service_id,
            @Field("service_name") String service_name,
            @Field("type") String type,
            @Field("check_status") String check_status,
            @Field("current_date") String current_date
    );

    @FormUrlEncoded
    @POST(get_vtpass_book_payment)
    Call<ReportModal> get_vtpass_book_payment(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(get_stateMent)
    Call<ReportModal> get_stateMent(
            @Field("user_id") String user_id,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date
    );


}
