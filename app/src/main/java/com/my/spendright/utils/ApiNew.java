package com.my.spendright.utils;


import com.my.spendright.Broadband.model.PayBroadbandModel;
import com.my.spendright.ElectircalBill.Model.GetMerchatAcocunt;
import com.my.spendright.ElectircalBill.Model.GetService;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;
import com.my.spendright.ElectircalBill.Model.PayFinalModel;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.TvCabelBill.Model.CofirmPaymentTvSubsChangeModel;
import com.my.spendright.TvCabelBill.Model.GetMerchatAcocuntTv;
import com.my.spendright.TvCabelBill.Model.PayAcocuntTvModel;
import com.my.spendright.airetime.model.GetAmountAirtimeModel;
import com.my.spendright.airetime.model.GetInternationalModel;
import com.my.spendright.airetime.model.GetOperatorModel;
import com.my.spendright.airetime.model.GetProductTypeModel;
import com.my.spendright.airetime.model.PayAirtimeModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiNew {

    String Api_balance = "balance";
    String Api_service_Api = "services?identifier=data";
    String Api_service_electricity_bill = "services?identifier=electricity-bill";
    String Api_service_tv_subscription = "services?identifier=tv-subscription";
    String Api_service_tv_subscription_plan = "service-variations?serviceID=dstv";
    String Api_service_data_plan = "service-variations?";
    String Api_get_international_airtime_product_types = "get-international-airtime-product-types?";
    String Api_get_international_airtime_operator = "get-international-airtime-operators?";
    String Api_get_international_airtime_service_variations= "service-variations?";
    String Api_service_airtime = "services?identifier=airtime";
    String Api_get_international_airtime_countries = "get-international-airtime-countries";
    String Api_service_data = "services?identifier=data";
    String Api_merchant_verify = "merchant-verify";

    ////////////
    String Api_pay = "pay";
    String Api_pay_airtime = "pay";
    String Api_pay_tv = "pay";
    String Api_pay_tv_change = "pay";
    String Api_pay_broadband = "pay";
    String Api_pay_airtime_forin = "pay";

    @GET(Api_balance)
    Call<GetVtsWalletBalnce> Api_balance();

    @GET(Api_service_Api)
    Call<GetService> Api_service_Api();

    @GET(Api_service_electricity_bill)
    Call<GetServiceElectricialModel> Api_service_electricity_bill();

    @GET(Api_service_airtime)
    Call<GetServiceElectricialModel> Api_service_airtime();

@GET(Api_get_international_airtime_countries)
    Call<GetInternationalModel> Api_get_international_airtime_countries();

    @GET(Api_service_data)
    Call<GetServiceElectricialModel> Api_service_data();

    @GET(Api_service_tv_subscription)
    Call<GetServiceElectricialModel> Api_service_tv_subscription();

    @GET(Api_service_tv_subscription_plan)
    Call<TvSuscriptionServiceModel> Api_service_tv_subscription_plan(@Query("serviceID") String serviceID);

    @GET(Api_service_data_plan)
    Call<TvSuscriptionServiceModel> Api_service_data_plan(@Query("serviceID") String serviceID);

   @GET(Api_get_international_airtime_product_types)
    Call<GetProductTypeModel> Api_get_international_airtime_product_types(@Query("code") String code);

   @GET(Api_get_international_airtime_operator)
    Call<GetOperatorModel> Api_get_international_airtime_operator(@Query("code") String code, @Query("product_type_id") String product_type_id);

   @GET(Api_get_international_airtime_service_variations)
    Call<GetAmountAirtimeModel> Api_get_international_airtime_service_variations(
            @Query("serviceID") String serviceID,
            @Query("operator_id") String operator_id,
            @Query("product_type_id") String product_type_id);

    @FormUrlEncoded
    @POST(Api_merchant_verify)
    Call<GetMerchatAcocunt> Api_merchant_verify(
            @Field("billersCode") String billersCode,
            @Field("serviceID") String serviceID,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST(Api_merchant_verify)
    Call<GetMerchatAcocuntTv> Api_merchant_verify_Tv(
            @Field("billersCode") String billersCode,
            @Field("serviceID") String serviceID
    );

    @FormUrlEncoded
    @POST(Api_pay)
    Call<PayFinalModel> Api_pay(
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST(Api_pay_tv)
    Call<PayAcocuntTvModel> Api_pay_tv(
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("amount") String amount,
            @Field("phone") String phone,
            @Field("subscription_type") String subscription_type
    );

    @FormUrlEncoded
    @POST(Api_pay_tv_change)
    Call<CofirmPaymentTvSubsChangeModel> Api_pay_tv_change(
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
    Call<PayAirtimeModel> Api_pay_airtime(
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("amount") String amount,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST(Api_pay_airtime_forin)
    Call<PayAirtimeModel> Api_pay_airtime_forin(
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
    Call<PayBroadbandModel> Api_pay_broadband(
            @Field("request_id") String request_id,
            @Field("serviceID") String serviceID,
            @Field("billersCode") String billersCode,
            @Field("variation_code") String variation_code,
            @Field("amount") String amount,
            @Field("phone") String phone
    );

}
