package com.my.spendright.utils;

import com.my.spendright.ElectircalBill.Model.GetMerchatAcocunt;
import com.my.spendright.ElectircalBill.Model.GetService;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;
import com.my.spendright.Model.AccountTransactionDetails;
import com.my.spendright.Model.AddAcountModel;
import com.my.spendright.Model.ChangePasswordModel;
import com.my.spendright.Model.CreateGroupModel;
import com.my.spendright.Model.CurencyModel;
import com.my.spendright.Model.DeleteAccountTrasaction;
import com.my.spendright.Model.DeletePaymentModel;
import com.my.spendright.Model.ForGotPassword;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.Model.GetAllAccountModel;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetCountryModel;
import com.my.spendright.Model.GetMainGrpCategory;
import com.my.spendright.Model.GetMyAccountModel;
import com.my.spendright.Model.GetMyPercentageModel;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.GetSchdulePaymentDetails;
import com.my.spendright.Model.GetSchdulepAymentModel;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.GetVtpassLoginModel;
import com.my.spendright.Model.GetVtpassMode;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.Model.ReportModal;
import com.my.spendright.Model.SchdulepAymentModel;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.Model.UpdateSchdulepAymentModel;
import com.my.spendright.TvCabelBill.Model.GetMerchatAcocuntTv;
import com.my.spendright.airetime.model.GetAmountAirtimeModel;
import com.my.spendright.airetime.model.GetInternationalModel;
import com.my.spendright.airetime.model.GetOperatorModel;
import com.my.spendright.airetime.model.GetProductTypeModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    String Api_signup_one = "signup_screen_one";
    String Api_signup_two = "signup_screen_two";
    String Api_get_userInfo = "get_user_by_id";
    String Api_signup_three = "otp_verification";
    String Api_get_all_state = "states_get";

    String Api_signup_user ="signup";
    String Api_send_otp ="generate_otp";

    String Api_get_account ="get_account_by_id";

    String Api_create_budget_grp ="new_group";

    String Api_get_all_grps ="get_list_group";

    String Api_delete_budget_grp ="delete_group";

    String Api_add_budget_category ="add_category";

    String Api_get_budget_category ="get_categories_by_user_id";

    String Api_create_budget_category ="create_budget_category";

    String Api_all_budget_category ="get_budget_category_by_group_id";

    String Api_share_unblock ="bcat_unlock_share_status";

    String Api_update_budget_category ="update_budget_category_by_id";

    String Api_delete_budget_grp_category ="delete_budget_category_by_id";
    String Api_for_exiting_user ="recreate_monnify_account_if_not_exists";

    String Api_for_bank_list ="bank_list";


    String Api_add_beneficiary ="add_beneficiary";

    String Api_get_beneficiary ="get_beneficiary";

    String Api_check_beneficiary ="check_beneficiary";

    String Api_withdraw_to_bank ="transfer_wallet_to_bank_monnify";

    String Api_delete_beneficiary ="delete_beneficiary";

    String Api_monnify_commission ="get_monnify_commission";

    String Api_dashboard_category ="getMainCategory";

    String Api_withdraw_to_my_wallet ="transfer_budget_category_to_wallet";


    String Api_withdraw_my_wallet_to_another_wallet ="transfer_amount_to_another_users_main_wallet";

    String Api_suggestion_user ="suggestion_for_user";
    String Api_delete_budget_category ="delete_category";

    String Api_update_expense_category ="update_category";

    String Api_withdraw_wallet_to_wallet ="transfer_amount_from_main_wallet_to_another_users_main_wallet";


    String Api_withdraw_wallet_to_bank ="transfer_amount_from_main_wallet_to_bank_account";

    String Api_user_update_profile ="update_profile";

    String Api_delete_user_profile ="delete_profile_by_user";

    String Api_user_notification ="get_vtpass_history_notification";

    String Api_contact_us ="get_social_share";

    String Api_notification_counter ="get_notification_count";

    String Api_get_pie_chart_report ="get_vtpass_history_search_pie_chart";

    String Api_finger_print ="update_finger_print";


    String Api_clear_notification ="clear_notifications";

    String Api_waec = "vt_pass_eduction";

    String Api_pay_showmax = "vtpass_pay";

    String Api_jamb = "service-variations";

    String Api_pay_waec = "waec_registraton_api_payment";                     //"new_vtpass_api_pay";

    String Api_pay_jamb = "purchase_jamb_pin";   // pay

    String Api_verify_jamb = "verify_profile_id";   // merchant-verify



    String Api_signup = "signup";
    String Api_check_otp = "check_otp";
    String Api_resendOTP = "resendOTP";
    String Api_social_login = "social_login";
    String Api_login = "login";

    String Api_login_biomatric = "login_finger_prints_key";
    String Api_get_profile = "get_profile";
    String Api_update_profile = "update_profile";
    String Api_forgot_password = "forgot_password";
    String Api_get_country = "get_country";
    String Api_change_password = "change_password";
    String Api_get_account_category = "get_account_category";
    String Api_get_account_detail = "get_account_detail";
    String Api_account_detail_info = "account_detail_info";
    String Api_delete_account_info = "delete_account_info";
    String Api_add_account= "add_account";
    String Api_editAccount= "editAccount";
    String get_profile_data= "get_profile_data";  //
    String get_getAllAccount= "getAllAccount";
    String getMonthBugdet= "getMonthBugdet";
    String change_currency= "change_currency";
    String getPaymentCommission= "getPaymentCommission";
    String addVTpassLogin= "addVTpassLogin";
    String logoutvtpass= "logoutvtpass";
    String getVTpassLogin= "getVTpass";
    String add_schedule_payment= "add_schedule_payment";
    String updateSchedulePayment= "updateSchedulePayment";
    String get_schedule_payment= "get_schedule_payment";
    String getSchedulePaymentDetails= "getSchedulePaymentDetails";
    String deleteSchedulePayment= "deleteSchedulePayment";
    String add_account_info= "add_account_info";
    String edit_account_info= "edit_account_info";

    //SetBudget
    String Api_create_group= "create_group";
    String GetGrpCategory= "getMainCategory";
    String getPercentageCategory= "getPercentageCategory";
    String Api_add_category_group= "add_category_group";
    String editgroupCategory= "editgroupCategory";
    String Api_category_delete= "deletegroupCategory";
    String Api_get_group_expence= "get_group_expence";
    String Api_get_group_expence_by_month= "get_group_expence_by_month";
    String Api_delete_group= "delete_group";
    String Api_deleteAccount= "deleteAccount";
    String get_stateMent= "get_vtpass_history";

    String get_vtpass_book_payment= "get_vtpass_book_payment";
    String Api_add_vtpass_book_payment  = "add_vtpass_book_payment";
    String get_budget_by_transaction  = "get_budget_by_transaction";
    String get_vtpass_history_search  = "get_vtpass_history_search";


    String Api_balance = "balance";
    String Api_service_Api = "vtpass_services?identifier=data";
    String Api_service_electricity_bill = "vtpass_services?identifier=electricity-bill";
    String Api_service_tv_subscription = "vtpass_services?identifier=tv-subscription";
    String Api_service_tv_subscription_plan = "vtpass_service_variations?serviceID=dstv";
    String Api_service_data_plan = "vtpass_service_variations?";
    String Api_get_international_airtime_product_types = "vtpass_get_international_airtime_product_types?";
    String Api_get_international_airtime_operator = "vtpass_get_international_airtime_operators?";
    String Api_get_international_airtime_service_variations= "vtpass_service_variations?";
    String Api_service_airtime = "vtpass_services?identifier=airtime";
    String Api_get_international_airtime_countries = "vtpass_get_international_airtime_countries";
    String Api_service_data = "vtpass_services?identifier=data";
    String Api_merchant_verify = "merchant_verify";

    ////////////
    String Api_pay = "new_vtpass_api_pay";
    String Api_pay_airtime = "www_new_vtpass_pay_airtime";
    String Api_pay_tv = "new_vtpass_api_pay_tv";
    String Api_pay_tv_change = "vtpass_pay_tv_change";
    String Api_pay_broadband = "www_new_vtpass_pay_broadband";
    String Api_pay_airtime_forin = "new_vtpass_pay_airtime_forin";

    String Api_generate_token = "generateMonnifyToken";

    String Api_verify_kyc = "bnv_verificatoin";




    @FormUrlEncoded
    @POST(Api_get_account)
    Call<ResponseBody> Api_get_account(@FieldMap HashMap<String,String> hashMap);

    @FormUrlEncoded
    @POST(Api_signup_user)
    Call<ResponseBody> Api_signup_user(@FieldMap HashMap<String,String> hashMap);


    @FormUrlEncoded
    @POST(Api_send_otp)
    Call<ResponseBody> Api_send_otp(@Field("mobile") String mobile);



    @FormUrlEncoded
    @POST(Api_signup_one)
    Call<ResponseBody> Api_signup_one(
            @Field("user_name") String user_name,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("other_legal_name") String other_legal_name,
            @Field("email") String email,
            @Field("phone_number") String phone_number,
            @Field("country_code") String country_code

    );

    @FormUrlEncoded
    @POST(Api_signup_two)
    Call<ResponseBody> Api_signup_two(
            @Field("user_id") String id,
            @Field("referral_code") String referral_code,
            @Field("full_address") String full_address,
            @Field("state") String state,
            @Field("password") String password

    );


    @FormUrlEncoded
    @POST(Api_get_userInfo)
    Call<ResponseBody> Api_get_userInfo(
            @Field("user_id") String id);



    @GET(Api_get_all_state)
    Call<ResponseBody> Api_get_all_state();


    @FormUrlEncoded
    @POST(Api_signup_three)
    Call<ResponseBody> Api_signup_three(
            @Field("user_id") String user_id,
            @Field("otp") String otp
            );



    @FormUrlEncoded
    @POST(Api_signup)
    Call<ResponseBody> Api_signup(
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
            @Field("register_id") String register_id,
            @Field("country_code") String country_code,
            @Field("monnify_contractCode") String monnify_contractCode,
            @Field("monnify_accountReference") String monnify_accountReference,
            @Field("monnify_accountName") String monnify_accountName,
            @Field("monnify_currencyCode") String monnify_currencyCode,
            @Field("monnify_customerEmail") String monnify_customerEmail,
            @Field("monnify_customerName") String monnify_customerName,
                        @Field("batch_id") String batch_id,
            @Field("user_date_time") String user_date_time


    );

   @FormUrlEncoded
    @POST(Api_update_profile)
    Call<LoginModel> Api_update_profile(
            @Field("user_id") String user_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("city") String city,
            @Field("dob") String dob,
            @Field("country_residence") String country_residence,
            @Field("country_code") String country_code
    );

    @FormUrlEncoded
    @POST(Api_login)
    Call<LoginModel> Api_login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("register_id") String register_id
    );

    @FormUrlEncoded
    @POST(Api_login_biomatric)
    Call<LoginModel> Api_login_biomatric(
            @Field("finger_prints_key") String finger_prints_key
            );

    @FormUrlEncoded
    @POST(Api_get_profile)
    Call<ResponseBody> Api_get_profile(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(Api_check_otp)
    Call<ResponseBody> Api_check_otp(
            @Field("user_id") String user_id,
            @Field("otp") String otp,
            @Field("user_date_time") String user_date_time
    );

    @FormUrlEncoded
    @POST(Api_resendOTP)
    Call<ResponseBody> Api_resendOTP(
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("country_code") String country_code,
            @Field("user_date_time") String user_date_time
    );

  @FormUrlEncoded
    @POST(Api_forgot_password)
    Call<ForGotPassword> Api_forgot_password(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(Api_change_password)
    Call<ResponseBody> change_password(
            @Field("user_id") String user_id,
            @Field("password") String password
    );

    @POST(Api_get_account_category)
    Call<GetAccountCategory> Api_get_account_category();

    @POST(Api_get_country)
    Call<GetCountryModel> Api_get_country();

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
    @POST(Api_get_group_expence_by_month)
    Call<GetSetbudgetExpence> Api_get_group_expence_by_month(
            @Field("user_id") String user_id,
            @Field("month") String month
    );

    @FormUrlEncoded
    @POST(Api_delete_group)
    Call<ResponseBody> Api_delete_group(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(Api_deleteAccount)
    Call<ResponseBody> Api_deleteAccount(
            @Field("account_id") String account_id
    );

    @FormUrlEncoded
    @POST(Api_get_account_detail)
    Call<GetCategoryModelNew> Api_get_account_detail(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(Api_account_detail_info)
    Call<AccountTransactionDetails> Api_account_detail_info(
            @Field("transaction_id") String transaction_id
    );

    @FormUrlEncoded
    @POST(Api_delete_account_info)
    Call<DeleteAccountTrasaction> Api_delete_account_info(
            @Field("account_info_id") String account_info_id
    );

    @FormUrlEncoded
    @POST(edit_account_info)
    Call<SchdulepAymentModel> edit_account_info(
            @Field("account_info_id") String account_info_id,
            @Field("pay_name") String pay_name,
            @Field("transaction_amount") String transaction_amount,
            @Field("type") String type,
            @Field("category_id") String category_id,
            @Field("category_name") String category_name,
            @Field("date_time") String date_time,
            @Field("description") String description,
            @Field("emoji") String emoji
    );



    @POST(GetGrpCategory)
    Call<GetMainGrpCategory> GetGrpCategory();

    @FormUrlEncoded
    @POST(getPercentageCategory)
    Call<GetMyPercentageModel> getPercentageCategory(
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
            @Field("select_end__day_month_week") String select_end__day_month_week,
            @Field("month") String month
    );

    @FormUrlEncoded
    @POST(editgroupCategory)
    Call<AddCategoryModel> editgroupCategory(
            @Field("id") String id,
            @Field("user_id") String user_id,
            @Field("group_id") String group_id,
            @Field("category_name") String category_name,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST(Api_category_delete)
    Call<UpdateSchdulepAymentModel> Api_category_delete(
            @Field("group_cate_id") String group_cate_id
    );

    @FormUrlEncoded
    @POST(get_profile_data)
    Call<GetProfileModel> Api_get_profile_data(
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST(get_getAllAccount)
    Call<GetAllAccountModel> get_getAllAccount(
            @Field("user_id") String user_id
    );



    @FormUrlEncoded
    @POST(getMonthBugdet)
    Call<GetMyAccountModel> getMonthBugdet(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(change_currency)
    Call<CurencyModel> change_currency(
            @Field("currency_code") String currency_code
    );

    @FormUrlEncoded
    @POST(getPaymentCommission)
    Call<ResponseBody> getPaymentCommission(
            @Field("category") String category,
            @Field("serviceID") String serviceID
    );

    @FormUrlEncoded
    @POST(addVTpassLogin)
    Call<GetVtpassLoginModel> addVTpassLogin(
            @Field("user_id") String user_id,
            @Field("email") String email,
            @Field("password") String password,
            @Field("balance") String balance
    );

    @FormUrlEncoded
    @POST(logoutvtpass)
    Call<GetVtpassLoginModel> logoutvtpass(
            @Field("user_id") String user_id,
            @Field("check_status") String check_status
    );

    @FormUrlEncoded
    @POST(getVTpassLogin)
    Call<GetVtpassMode> getVTpassLogin(
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST(Api_add_vtpass_book_payment)
    Call<ResponseBody> Api_add_vtpass_book_payment(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("amount") String amount,
            @Field("service_id") String service_id,
            @Field("service_name") String service_name,
            @Field("type") String type,
            @Field("check_status") String check_status,
            @Field("transaction_date") String transaction_date,
            @Field("budget_account_id") String budget_account_id,
            @Field("transaction_time") String cat_id,
            @Field("description") String description,
            @Field("phone_number") String phone_number,
            @Field("payment_commision") String payment_commision,
            @Field("response") String response,
            @Field("discount") String discount,
            @Field("customer_name") String customerName,
            @Field("billers_code") String billers_code
    );

    @FormUrlEncoded
    @POST(Api_add_vtpass_book_payment)
    Call<ResponseBody> Api_add_vtpass_book_payment11(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("amount") String amount,
            @Field("service_id") String service_id,
            @Field("service_name") String service_name,
            @Field("type") String type,
            @Field("check_status") String check_status,
            @Field("transaction_date") String transaction_date,
            @Field("transaction_time") String transaction_time,
            @Field("budget_account_id") String budget_account_id,
            @Field("cat_id") String cat_id,
            @Field("description") String description,
            @Field("phone_number") String phone_number,
            @Field("payment_commision") String payment_commision,
            @Field("vtpass_type") String vtpass_type,
            @Field("vtpass_address") String vtpass_address,
            @Field("response") String response,
            @Field("discount") String discount,
            @Field("customer_name") String customerName,
            @Field("billers_code") String billers_code

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



    @FormUrlEncoded
    @POST(add_schedule_payment)
    Call<SchdulepAymentModel> add_schedule_payment(
            @Field("user_id") String user_id,
            @Field("amount") String amount,
            @Field("type_of_payment") String type_of_payment,
            @Field("category") String category,
            @Field("schedule_date") String schedule_date,
            @Field("account_id") String account_id
    );

    @FormUrlEncoded
    @POST(updateSchedulePayment)
    Call<UpdateSchdulepAymentModel> updateSchedulePayment(
            @Field("payment_id") String payment_id,
            @Field("user_id") String user_id,
            @Field("amount") String amount,
            @Field("type_of_payment") String type_of_payment,
            @Field("category") String category,
            @Field("schedule_date") String schedule_date,
            @Field("account_id") String account_id
    );

    @FormUrlEncoded
    @POST(get_schedule_payment)
    Call<GetSchdulepAymentModel> get_schedule_payment(
            @Field("user_id") String user_id,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST(getSchedulePaymentDetails)
    Call<GetSchdulePaymentDetails> getSchedulePaymentDetails(
            @Field("payment_id") String payment_id
    );

    @FormUrlEncoded
    @POST(deleteSchedulePayment)
    Call<DeletePaymentModel> deleteSchedulePayment(
            @Field("payment_id") String payment_id
    );

    @FormUrlEncoded
    @POST(get_budget_by_transaction)
    Call<ResponseBody> get_budget_by_transaction(
            @Field("user_id") String user_id,
            @Field("id") String id,
            @Field("start_date") String start_dated,
            @Field("end_date") String end_date,
            @Field("start_amount") String start_amount,
            @Field("end_amount") String end_amount,
            @Field("type") String transaction_type

    );





    @FormUrlEncoded
    @POST(get_vtpass_history_search)
    Call<ResponseBody> get_vtpass_history_search(
            @Field("user_id") String user_id,
            @Field("from_amount") String from_amount,
            @Field("to_amount") String to_amount,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("transaction_type") String transaction_type,
            @Field("account_budget_id") String account_budget_id,
            @Field("trx_type") String trx_type
    );


    @FormUrlEncoded
    @POST(add_account_info)
    Call<SchdulepAymentModel> add_account_info(
            @Field("user_id") String user_id   ,
            @Field("transaction_amount") String transaction_amount,
            @Field("type") String type,
            @Field("main_category_id") String main_category_id,
            @Field("account_budget_id") String account_budget_id,
            @Field("date_time") String date_time,
            @Field("description") String description,
            @Field("pay_name") String pay_name,
            @Field("main_category_name") String main_category_name,
            @Field("emoji") String emoji,
            @Field("trx_type") String trx_type

    );



    @FormUrlEncoded
    @POST(Api_social_login)
    Call<LoginModel> Api_social_login(
            @Field("social_id") String social_id,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("first_name") String first_name,
            @Field("type") String type,
            @Field("register_id") String register_id,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("image") String image
    );



    @FormUrlEncoded
    @POST(Api_editAccount)
    Call<AddAcountModel> Api_editAccount(
            @Field("id") String  id,
            @Field("account_id") String  account_id,
            @Field("user_id") String user_id,
            @Field("holder_name") String holder_name,
            @Field("current_balance") String current_balance
    );

    @FormUrlEncoded
    @POST("transaction_request_after_payment")
    Call<ResponseBody> Api_updateWallet(@FieldMap Map<String, String> paramHashMap);

    @GET("get_category")
    Call<ResponseBody> getHomeCatApi();


    @FormUrlEncoded
    @POST("updateBVNMonnifyAccount")
    Call<ResponseBody> ApiUpdateBVN(@FieldMap Map<String, String> paramHashMap);



// @HeaderMap Map<String,String> header)
    @GET(Api_balance)
    Call<GetVtsWalletBalnce> Api_balance();

    @GET(Api_service_Api)
    Call<GetService> Api_service_Api();

    @GET(Api_service_electricity_bill)
    Call<ResponseBody> Api_service_electricity_bill(@HeaderMap Map<String,String> header);

    @GET(Api_service_airtime)
    Call<ResponseBody> Api_service_airtime(@HeaderMap Map<String,String> header);

    @GET(Api_get_international_airtime_countries)
    Call<ResponseBody> Api_get_international_airtime_countries(@HeaderMap Map<String,String> header);

    @GET(Api_service_data)
    Call<ResponseBody> Api_service_data(@HeaderMap Map<String,String> header);

    /*@GET(Api_service_data)
    Call<ResponseBody> Api_service_data();*/

    @GET(Api_service_tv_subscription)
    Call<ResponseBody>   Api_service_tv_subscription(@HeaderMap Map<String,String>header);

    @GET(Api_service_tv_subscription_plan)
    Call<ResponseBody> Api_service_tv_subscription_plan(@HeaderMap Map<String,String> header,@Query("serviceID") String serviceID);

    @GET(Api_service_data_plan)
    Call<ResponseBody> Api_service_data_plan(@HeaderMap Map<String,String> header,@Query("serviceID") String serviceID);

    @GET(Api_get_international_airtime_product_types)
    Call<ResponseBody> Api_get_international_airtime_product_types(@HeaderMap Map<String,String> header,@Query("code") String code);

    @GET(Api_get_international_airtime_operator)
    Call<ResponseBody> Api_get_international_airtime_operator(@HeaderMap Map<String,String> header,@Query("code") String code, @Query("product_type_id") String product_type_id);

    @GET(Api_get_international_airtime_service_variations)
    Call<ResponseBody> Api_get_international_airtime_service_variations(
           @HeaderMap Map<String,String> header,
            @Query("serviceID") String serviceID,
            @Query("operator_id") String operator_id,
            @Query("product_type_id") String product_type_id);

    @FormUrlEncoded
    @POST(Api_merchant_verify)
    Call<ResponseBody> Api_merchant_verify(@HeaderMap Map<String,String> header,
            @Field("billersCode") String billersCode,
            @Field("serviceID") String serviceID,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST(Api_merchant_verify)
    Call<GetMerchatAcocuntTv> Api_merchant_verify_Tv(
            @HeaderMap Map<String,String> header,
            @Field("billersCode") String billersCode,
            @Field("serviceID") String serviceID
    );

    @FormUrlEncoded
    @POST(Api_pay)
    Call<ResponseBody> Api_pay(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST(Api_pay_tv)
    Call<ResponseBody> Api_pay_tv(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("amount") String amount,
            @Field("phone") String phone,
            @Field("subscription_type") String subscription_type
    );

    @FormUrlEncoded
    @POST(Api_pay_tv_change)
    Call<ResponseBody> Api_pay_tv_change(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone,
            @Field("subscription_type") String subscription_type,
            @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST(Api_pay_airtime)
    Call<ResponseBody> Api_pay_airtime(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("amount") String amount,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Api_pay_airtime_forin)
    Call<ResponseBody> Api_pay_airtime_forin(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone,
            @Field("operator_id") String operator_id,
            @Field("country_code") String country_code,
            @Field("product_type_id") String product_type_id,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(Api_pay_broadband)
    Call<ResponseBody> Api_pay_broadband(
            @HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST(Api_generate_token)
    Call<ResponseBody> Api_generate_token(
            @Field("type") String type);



    @FormUrlEncoded
    @POST(Api_verify_kyc)
    Call<ResponseBody> Api_verify_kyc(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_create_budget_grp)
    Call<ResponseBody> Api_create_budget_grp(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_get_all_grps)
    Call<ResponseBody> Api_get_all_grps(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_delete_budget_grp)
    Call<ResponseBody> Api_delete_budget_grp(@FieldMap Map<String, String> body);


    @FormUrlEncoded
    @POST(Api_add_budget_category)
    Call<ResponseBody> Api_add_budget_category(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_get_budget_category)
    Call<ResponseBody> Api_get_budget_category(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_create_budget_category)
    Call<ResponseBody> Api_create_budget_category(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_all_budget_category)
    Call<ResponseBody> Api_all_budget_category(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_share_unblock)
    Call<ResponseBody> Api_share_unblock(@FieldMap Map<String, String> body);


    @FormUrlEncoded
    @POST(Api_update_budget_category)
    Call<ResponseBody> Api_update_budget_category(@FieldMap Map<String, String> body);


    @FormUrlEncoded
    @POST(Api_delete_budget_grp_category)
    Call<ResponseBody> Api_delete_budget_grp_category(@FieldMap Map<String, String> body);




    @FormUrlEncoded
    @POST(Api_for_exiting_user)
    Call<ResponseBody> Api_for_exiting_user(@FieldMap Map<String, String> body);

    @GET(Api_for_bank_list)
    Call<ResponseBody> Api_for_bank_list();

    @FormUrlEncoded
    @POST(Api_add_beneficiary)
    Call<ResponseBody> Api_add_beneficiary(@FieldMap Map<String, String> body);



    @FormUrlEncoded
    @POST(Api_get_beneficiary)
    Call<ResponseBody> Api_get_beneficiary(@FieldMap Map<String, String> body);



    @FormUrlEncoded
    @POST(Api_check_beneficiary)
    Call<ResponseBody> Api_check_beneficiary(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_withdraw_to_bank)
    Call<ResponseBody> Api_withdraw_to_bank(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_delete_beneficiary)
    Call<ResponseBody> Api_delete_beneficiary(@FieldMap Map<String, String> body);


    @GET(Api_monnify_commission)
    Call<ResponseBody> Api_monnify_commission();

    @GET(Api_dashboard_category)
    Call<ResponseBody> Api_dashboard_category();

    @FormUrlEncoded
    @POST(Api_withdraw_to_my_wallet)
    Call<ResponseBody> Api_withdraw_to_my_wallet(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_withdraw_my_wallet_to_another_wallet)
    Call<ResponseBody> Api_withdraw_my_wallet_to_another_wallet(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_suggestion_user)
    Call<ResponseBody> Api_suggestion_user(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_delete_budget_category)
    Call<ResponseBody> Api_delete_budget_category(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_update_expense_category)
    Call<ResponseBody> Api_update_expense_category(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_withdraw_wallet_to_wallet)
    Call<ResponseBody> Api_withdraw_wallet_to_wallet(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST(Api_withdraw_wallet_to_bank)
    Call<ResponseBody> Api_withdraw_wallet_to_bank(@FieldMap Map<String, String> body);




    @FormUrlEncoded
    @POST(Api_user_update_profile)
    Call<ResponseBody> Api_user_update_profile(
            @Field("user_id") String user_id,
            @Field("last_name") String last_name,
            @Field("other_legal_name") String other_legal_name,
            @Field("mobile") String mobile,
            @Field("country_code") String country_code,
            @Field("full_address") String full_address,
            @Field("state") String state
    );


    @FormUrlEncoded
    @POST(Api_delete_user_profile)
    Call<ResponseBody> Api_delete_user_profile(
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Api_user_notification)
    Call<ResponseBody> Api_user_notification(
            @Field("user_id") String user_id);


    @GET(Api_contact_us)
    Call<ResponseBody> Api_contact_us();


    @FormUrlEncoded
    @POST(Api_notification_counter)
    Call<ResponseBody> Api_notification_counter(
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Api_get_pie_chart_report)
    Call<ResponseBody> Api_get_pie_chart_report(
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Api_finger_print)
    Call<ResponseBody> Api_finger_print(
            @Field("user_id") String user_id,
    @Field("finger_prints_key") String finger_prints_key);



    @FormUrlEncoded
    @POST(Api_clear_notification)
    Call<ResponseBody> Api_clearAll_user_notification(
            @Field("user_id") String user_id);







    @FormUrlEncoded
    @POST(Api_waec)
    Call<ResponseBody> ApiWAECRegistration(@HeaderMap Map<String,String> header,@Field("serviceID") String serviceID);



    @FormUrlEncoded
    @POST(Api_pay_showmax)
    Call<ResponseBody> Api_pay_showmax(@HeaderMap Map<String, String> header,
            @Field("user_id") String user_id,
           @Field("request_id") String request_id,
            @Field("serviceId") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variationCode") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );



    @GET(Api_jamb)
    Call<ResponseBody> ApiJAMB(@Query("serviceID") String serviceID);



   /* @FormUrlEncoded
    @POST(Api_pay_waec)
    Call<ResponseBody> Api_pay_waec(
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("quantity") String quantity,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );*/


    @FormUrlEncoded
    @POST(Api_pay_waec)
    Call<ResponseBody> Api_pay_waec(@HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceId") String serviceID,
            @Field("quantity") String quantity,
            @Field("variationCode") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );



    @FormUrlEncoded
    @POST(Api_pay_jamb)
    Call<ResponseBody> Api_pay_jamb(@HeaderMap Map<String,String> header,
            @Field("user_id") String user_id,
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variationCode") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );





    @FormUrlEncoded
    @POST(Api_verify_jamb)
    Call<ResponseBody> Api_verify_jamb(@HeaderMap Map<String,String> header,
            @Field("billersCode") String billersCode,
            @Field("serviceID") String serviceID,
            @Field("type") String type,
            @Field("user_id") String userId

    );






}
