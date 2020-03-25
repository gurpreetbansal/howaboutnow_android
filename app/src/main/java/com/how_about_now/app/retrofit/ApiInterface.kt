package com.how_about_now.app.retrofit

import com.how_about_now.app.data.login_phase.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiInterface {
    @POST("index.php?p=signup")
    fun registerApi(@Body registerEntity: SignUpEntity): Call<SignUpWrapper>

    @POST("index.php?p=login")
    fun loginApi(@Body registerEntity: LoginEntity): Call<SignUpWrapper>

    @POST("index.php?p=forgot_password")
    fun forgotPasswordApi(@Body forgotPasswordEntity: ForgotPasswordEntity): Call<ForgotPasswordWrapper>


//
//    @POST("user/login")
//    fun loginApi(@Body loginEntity: LoginEntity): Call<SignUpWrapper>
//
//    @POST("mylikies")
//    fun myLikesApi(
//        @Body otpEntity: OtpEntity
//    ): Call<RegisterWrapper>
//
//
//    @POST("user/contact-us")
//    fun contactUsApi(@Body contactUsEntity: ContactUsEntity): Call<ConatctUsWrapper>
//
//    @POST("user/company-info")  //Slug = terms-n-cond; Slug = vision    lng: en or ar
//    fun companyInfoVisionApi(
//        @Body visionEntity: VisionEntity
//    ): Call<VisionWrapper>  //slug=vision&lng=en
//
//    @POST("user/company-info")  //Slug = terms-n-cond; Slug = vision    lng: en or ar
//    fun termsConditionsApi(
//        @Body visionEntity: VisionEntity
//    ): Call<TermsConditionsWrapper>  //slug=terms-n-cond&lng=en
//
//    @POST("user/edit-profile")
//    fun editProfileApi(@Body profileEntity: EditProfileEntity): Call<RegisterWrapper>
//
//    @POST("order/place-order/card-payment")
//    fun placeOrderApi(@Body checkoutEntity: CheckoutEntity): Call<CheckoutWrapper>
//
//    @POST("mosque/add-mosque")
//    fun addMosqueApi(@Body addMosqueEntity: AddMosqueEntity): Call<AddMosqueWrapper>
//
//    @POST("user/logout")
//    fun logoutApi(): Call<LogoutWrapper>
//
//    @POST("user/home")  //   Makkah/enorar
//    fun homeMarkerApi(
//        @Body markerEntity: MarkerEntity
//    ): Call<MarkerWrapper>
//
//    @POST("city")
//    fun getCityApi(
//        @Body cityEntity: CityEntity
//    ): Call<CityWrapper>
//
//    @POST("mosque/mosque-images")  //   Makkah/enorar
//    fun mosqueImagesApi(
//        @Body language: LanguageEntity
//    ): Call<GalleryWrapper>
//
//    @POST("order/get-feedback")  //   Makkah/enorar
//    fun getFeedbackApi(
//        @Body language: LanguageEntity
//    ): Call<GetFeedbackWrapper>
//
//    @POST("order/customer-feedback")
//    fun customerFeedbackApi(
//        @Body customerFeedbackEntity: CustomerFeedbackEntity
//    ): Call<CustomerFeedBackWrapper>
//
//    @POST("order/promo-codes")
//    fun promoCodeApi(
//        @Body promoCodeEntity: PromoCodeEntity
//    ): Call<PromoCodeWrapper>
//
//    @POST("user/product-list")
//    fun productListApi(
//        @Body profileEntity: ProductEntity
//    ): Call<ProductWrapper>
//
//    @POST("order/place-order/gpay")
//    fun googlePayApi(
//        @Body profileEntity: CheckoutEntity
//    ): Call<CheckoutWrapper>
//
//    @POST("order/apply-promo")
//    fun applyPromoApi(
//        @Body profileEntity: ApplyPromoEntity
//    ): Call<ApplyPromoWrapper>
//
//    @POST("order/order-history")
//    fun orderHistoryApi(
//        @Body language: OrderHistoryEntity
//    ): Call<CurrentOrderHistoryWrapper>
//
//    @POST("order/order-history")
//    fun pastOrderHistoryApi(
//        @Body language: OrderHistoryEntity
//    ): Call<PastOrderWrapper>
//
//    @POST("user/iban-list")
//    fun ibanAccountNumberListApi(
//        @Body language: LanguageEntity
//    ): Call<IbanNumberWrapper>
//
//    @Multipart
//    @POST("order/place-order/iban")
//    fun ibanOrderApi(
//        @Part("lng") lng: RequestBody,
//        @PartMap() partMap: Map<String, JsonObject>,
//        @Part iban_image: MultipartBody.Part
//    ): Call<IbanOrderWrapper>

}