package com.cliknfit.interfaces;

import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.DataTrainerInfo;
import com.cliknfit.pojo.ModelBookingHistory;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.TimerScreenModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static android.R.attr.rating;
import static com.cliknfit.R.id.address;


/**
 * Created by Prince on 3/3/2017.
 */

public interface SOService {


    @FormUrlEncoded
    @POST("client/signin")
    Call<CommonStatusResultObj> login(@Field("email") String email,
                                      @Field("password") String password,
                                      @Field("login_type") String login_type,
                                      @Field("facebook_id") String facebook_id,
                                      @Field("google_id") String google_id,
                                      @Field("device_id") String device_id,
                                      @Field("device_token") String device_token,
                                      @Field("device_type") String device_type,
                                      @Field("lat") String lat,
                                      @Field("lan") String longti);


    @FormUrlEncoded
    @POST("client/signup")
    Call<CommonStatusResultObj> register(@Field("dob") String dob,
                                         @Field("name") String name,
                                         @Field("email") String email,
                                         @Field("password") String password,
                                         @Field("login_type") String login_type,
                                         @Field("facebook_id") String facebook_id,
                                         @Field("google_id") String google_id,
                                         @Field("phone") String phone,
                                         @Field("device_id") String device_id,
                                         @Field("device_token") String device_token,
                                         @Field("device_type") String device_type,
                                         @Field("country_code") String country_code,
                                         @Field("lat") String lat,
                                         @Field("lan") String longti,
                                         @Field("image") String image,
                                         @Field("age") String age,
                                         @Field("address") String address,
                                         @Field("display_phone") String display_phone,
                                         @Field("city") String city
    );


    @FormUrlEncoded
    @POST("calculate_price")
    Call<CommonStatusResultObj> reqBook(@Field("start_time") String starttime,
                                        @Field("end_time") String endtime,
                                        @Field("no_of_people") String no_of_people
    );

    @FormUrlEncoded
    @POST("requests/make_request")
    Call<CommonStatusResultObj> confirmBook(@Field("client_id") String dob,
                                            @Field("trainer_id") String name,
                                            @Field("start_time") String email,
                                            @Field("end_time") String password,
                                            @Field("actual_start_time") String login_type,
                                            @Field("actual_end_time") String facebook_id,
                                            @Field("workoutlocation_id") String google_id,
                                            @Field("location_tag") String phone,
                                            @Field("location_address") String device_id,
                                            @Field("no_of_people") String device_token,
                                            @Field("nonce") String nonce
    );


    @FormUrlEncoded
    @POST("update_phone_number")
    Call<CommonStatusResultObj> updateMobile(
            @Field("country_code") String country_code,
            @Field("new_phone") String new_phone,
            @Field("id") String id);

    @FormUrlEncoded
    @POST("delete_image")
    Call<CommonStatus> deleteImage(@Field("id") String id, @Field("image_id") String image_id);

    @FormUrlEncoded
    @POST("client/get_sessions")
    Call<ModelMySession> sessionList(@Field("id") String id);

    @FormUrlEncoded
    @POST("client/get_completed_sessions")
    Call<ModelBookingHistory> BookingHistory(@Field("id") String id);

    @Multipart
    @POST("upload_image")
    Call<CommonStatusResultObj> postImage(@Part MultipartBody.Part image, @Part("image") RequestBody name,
                                          @Part("user_id") String userId);


    @FormUrlEncoded
    @POST("otpverify")
    Call<CommonStatusResultObj> oTp(@Field("otp") String otp,
                                    @Field("phone") String phone);

    @FormUrlEncoded
    @POST("otp/resend")
    Call<CommonStatusResultObj> oTpResend(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("client/forget_password")
    Call<CommonStatusResultObj> forgetPass(@Field("email") String email,
                                           @Field("country_code") String country_code,
                                           @Field("phone") String phone);

    @FormUrlEncoded
    @POST("client/save_address")
    Call<CommonStatusResultObj> saveAddress(@Field("id") String id,
                                            @Field("address") String address);

    @FormUrlEncoded
    @POST("client/get_address")
    Call<CommonStatusResultObj> getAddress(@Field("id") String id
    );

    @FormUrlEncoded
    @POST("client/get_profile")
    Call<CommonStatusResultObj> profile(@Field("id") String id);

    @FormUrlEncoded
    @POST("client/start_timer")
    Call<CommonStatusResultObj> startTimer(@Field("request_id") String id);

    @FormUrlEncoded
    @POST("requests/reject_request")
    Call<CommonStatusResultObj> cancelrequest(@Field("request_id") String id);


    @FormUrlEncoded
    @POST("requests/add_hours")
    Call<CommonStatusResultObj> requestMoreHour(
            @Field("start_time") String email,
            @Field("request_id") String id,
            @Field("hour") String hours);


    @FormUrlEncoded
    @POST("client/complete_session")
    Call<CommonStatusResultObj> submitRate(@Field("comment") String comment,
                                           @Field("notes") String notes,
                                           @Field("rating") String rating,
                                           @Field("request_id") String requestId
    );

    @FormUrlEncoded
    @POST("trainer/get_profile")
    Call<CommonStatusResultObj> profileTrainer(@Field("id") String id);

    @FormUrlEncoded
    @POST("questions/index")
    Call<CommonStatus> qnaList(@Field("email") String email);

    @FormUrlEncoded
    @POST("trainers/index")
    Call<DataTrainerInfo> trainerList(@Field("client_id") String client_id,
                                      @Field("lat") String lat,
                                      @Field("lan") String lan);

    @FormUrlEncoded
    @POST("trainer/search")
    Call<DataTrainerInfo> trainerSearchList(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("requests/get_session_details")
    Call<TimerScreenModel> updateOnPush(@Field("request_id") String id);

    @FormUrlEncoded
    @POST("questions/submit_answer")
    Call<CommonStatus> qnaSubmit(@Field("user_id") String user_id,
                                 @Field("question_id") String question_id,
                                 @Field("answer_id") String answer_id);

    @FormUrlEncoded
    @POST("newsfeeds/index")
    Call<CommonStatus> newsFeedList(@Field("email") String email);

    @FormUrlEncoded
    @POST("get_health_master")
    Call<CommonStatusResultObj> healthmaster(@Field("email") String email);

    @FormUrlEncoded
    @POST("client/update_profile")
    Call<CommonStatusResultObj> updatePassword(@Field("id") String id,
                                               @Field("password") String password);

    @FormUrlEncoded
    @POST("client/update_profile")
    Call<CommonStatusResultObj> updateProfile(@Field("name") String name,
                                              @Field("id") String id,
                                              @Field("health_problems") String health_problems,
                                              @Field("workout_interests") String workout_interests,
                                              @Field("workout_locations") String workout_locations,
                                              @Field("current_weight") String current_weight,
                                              @Field("target_weight") String target_weight,
                                              @Field("bmi") String bmi,
                                              @Field("body_fat") String body_fat,
                                              @Field("additional_info") String additional_info,
                                              @Field("about_me") String about_me,
                                              @Field("speciality") String speciality,
                                              @Field("city") String city,
                                              @Field("health_problems_other_text") String health_problems_other_text,
                                              @Field("workout_locations_other_text") String workout_locations_other_text,
                                              @Field("workout_interests_other_text") String workout_interests_other_text
    );


    @FormUrlEncoded
    @POST("client/update_profile")
    Call<CommonStatusResultObj> agreeTnc(@Field("id") String id,
                                         @Field("is_agree") String isagree);


    /*@GET("sendOtp")
    Call<GetOtp> getOtp(@Query("mobile") String mobile, @Query("user_type") String type);

    @GET("appointment_preferences")
    Call<Preference> PreferenceList(@Query("user_id") String mobile);

    @GET("final_appointments")
    Call<GetAnotherList> AppointmentList(@Query("user_id") String mobile, @Query("user_type") String type);

    @FormUrlEncoded
    @POST("advocate_client_cases")
    Call<ViewCaseModel> ViewCaseListTask(@Field("advocate_id") String advocate_id);


    @FormUrlEncoded
    @POST("make_claim")
    Call<CheckStatus> claimTask(@Field("advocate_id") String advocate_id, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("delete_case")
    Call<CheckStatus> deleteCaseTask(@Field("case_id") String caseid, @Field("user_type") String usertype);

    @FormUrlEncoded
    @POST("notifications")
    Call<GetAnotherList> ViewNotificationTask(@Field("user_id") String advocate_id,
                                              @Field("offset") String offset,
                                              @Field("user_type") String type);

    @FormUrlEncoded
    @POST("latest_hearings")
    Call<GetAnotherList> hearingListTask(@Field("advocate_id") String advocate_id,
                                         @Field("case_id") String case_id);

    @FormUrlEncoded
    @POST("block_user")
    Call<CheckStatus> blockUserTask(@Field("advocate_id") String advocate_id, @Field("user_id") String user_id,
                                    @Field("status") String status);


    @FormUrlEncoded
    @POST("chat_list")
    Call<GetList> ChatList(@Field("user_id") String advocate_id, @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("add_client_cases")
    Call<CheckStatus> AddCaseTask(@Field("client_id") String client_id, @Field("case_id") String case_id,
                                  @Field("advocate_id") String advocate_id,
                                  @Field("description") String description,
                                  @Field("hearing_date") String hearing_date,
                                  @Field("result") String result,
                                  @Field("next_hearing") String next_hearing,
                                  @Field("case_title") String case_title);


    @FormUrlEncoded
    @POST("user_status")
    Call<CheckStatus> usercheckTask(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("update_client_cases")
    Call<CheckStatus> updateCaseTask(@Field("case_id") String case_id,
                                     @Field("case_title") String case_title,
                                     @Field("result") String result,
                                     @Field("description") String description,
                                     @Field("previous_date") String previous_date,
                                     @Field("previous_hearing_id") String previous_hearing_id,
                                     @Field("next_hearing") String next_hearing
    );

    @FormUrlEncoded
    @POST("mute_advocate")
    Call<CheckStatus> usercheckTask(@Field("advocate_id") String id, @Field("status") String status);

    @GET("packages")
    Call<GetAnotherList> packageListTask();

    @FormUrlEncoded
    @POST("subscribe_package")
    Call<CheckStatus> subscribe_packageTask(@Field("advocate_id") String id,
                                            @Field("month") String month,
                                            @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("userPayment")
    Call<ChecksumHash> userpaymentTask(@Field("ORDER_ID") String order_id,
                                       @Field("CUST_ID") String custm_id,
                                       @Field("INDUSTRY_TYPE_ID") String industry_type,
                                       @Field("CHANNEL_ID") String channel_id,
                                       @Field("TXN_AMOUNT") String txn_amount,
                                       @Field("EMAIL") String mail,
                                       @Field("MOBILE_NO") String mobile);

    @Multipart
    @POST("picture_upload")
    Call<UploadPic> postImage(@Part MultipartBody.Part image, @Part("image") RequestBody name,
                              @Part("user_id") Integer userId);


    @GET("verifyUser")
    Call<GetList> getVerify(@Query("mobile") String mobile, @Query("otp") String otp,
                            @Query("user_type") String type,
                            @Query("token") String token);

    @FormUrlEncoded
    @POST("register_advocate")
    Call<GetList> getRegister(@Field("court_name") String courtname,
                              @Field("token") String token,
                              @Field("name") String name,
                              @Field("dob") String dob,
                              @Field("sex") String sex,
                              @Field("address") String address,
                              @Field("city") String city,
                              @Field("pin") String pin,
                              @Field("mobile") String mobile,
                              @Field("ban") String ban,
                              @Field("en") String en,
                              @Field("aadhar") String aadhar,
                              @Field("experince") String experince,
                              @Field("office") String office,
                              @Field("court") String court,
                              @Field("state") String state,
                              @Field("cases") String cases,
                              @Field("email") String email,
                              @Field("other_case") String other_case,
                              @Field("language") String language,
                              @Field("bar_name") String barname,
                              @Field("appointment_fee") String appointment_fee,
                              @Field("call_fee") String call_fee,
                              @Field("emergency_call_fee") String emergency_call_fee);

    @GET("states")
    Call<GetList> getStates();

    @GET("courts")
    Call<GetList> getCourts();

    @FormUrlEncoded
    @POST("advocate_status")
    Call<CheckStatus> getstatusChanged(@Field("advocate_id") String userid,
                                       @Field("status") String ques_id);

    @FormUrlEncoded
    @POST("user_advocate_chat")
    Call<GetAnotherList> getChatData(@Field("user_id") String userid,
                                     @Field("advocate_id") String adv_id);

    @FormUrlEncoded
    @POST("chats")
    Call<GetAnotherList> postChatData(@Field("user_id") String userid,
                                      @Field("advocate_id") String adv_id,
                                      @Field("user_type") String usertype,
                                      @Field("message") String msg);

    @GET("cases")
    Call<GetList> getCases();

    @GET("hight_courts")
    Call<GetList> getDistrict();

    @GET("languages")
    Call<GetList> getLangs();

    @FormUrlEncoded
    @POST("advocate_clients")
    Call<ClientList> getClients(@Field("advocate_id") String advocate_id);

    @FormUrlEncoded
    @POST("questions")
    Call<GetList> getQuestions(@Field("offset") String offset,
                               @Field("user_type") String usertype,
                               @Field("user_id") String user_id);


    @GET("appointments")
    Call<GetList> getAppointmentRequest(@Query("user_id") String userid, @Query("user_type") String usertype);

    @GET("call_requests")
    Call<GetAnotherList> getCallRequest(@Query("advocate_id") String userid,
                                        @Query("user_type") String user_type);


    @FormUrlEncoded
    @POST("add_comments")
    Call<GetList> addcommentrequest(@Field("user_id") String userid,
                                    @Field("question_id") String ques_id,
                                    @Field("comment") String comment);

    @FormUrlEncoded
    @POST("final_calls")
    Call<GetAnotherList> finalgetCallRequest(@Field("user_id") String userid,
                                             @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("appointment_preference")
    Call<CheckStatus> addPreferencetask(@Field("appointment_id") String appointment_id,
                                        @Field("appointment_date") String appointment_date,
                                        @Field("appointment_time") String appointment_time);

    @FormUrlEncoded
    @POST("add_client")
    Call<CheckStatus> addClienttask(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("mobile") String mobile,
                                    @Field("advocate_id") String advocate_id,
                                    @Field("address") String address);


    @FormUrlEncoded
    @POST("update_advocate_profile")
    Call<UpdateprofileModel> updateProfile(@Field("name") String name,
                                           @Field("resident") String resident,
                                           @Field("office") String office,
                                           @Field("pin") String pin,
                                           @Field("experience") String experience,
                                           @Field("user_id") String user_id,
                                           @Field("other_case") String other_case,
                                           @Field("language") String language);

    @FormUrlEncoded
    @POST("send_mail")
    Call<CheckStatus> orderbook(@Field("user_id") String user_id,
                                @Field("description") String description,
                                @Field("type") String type,                //1 for orderbook, 2 for complaint
                                @Field("title") String title);
*/


    /*
    Convert Json to model use :

    http://www.jsonschema2pojo.org/

     Every method in this interface must have an HTTP annotation that provides the request method and relative URL.
    There are five built-in annotations available: @GET, @POST, @PUT, @DELETE, and @HEAD.



    In the second method definition, we added a query parameter for us to filter the data from the server.
    Retrofit has the @Query("key") annotation to use instead of hard-coding it in the endpoint.
    The key value represents the parameter name in the URL
    if we pass the value "android" as an argument to the getAnswers(String tags) method, the full URL will be:

    https://api.stackexchange.com/2.2/answers?order=desc&sort=activity&site=stackoverflow&tagged=android


   Parameters of the interface methods can have the following annotations:

   @Path	variable substitution for the API endpoint
   @Query	specifies the query key name with the value of the annotated parameter
   @Body	payload for the POST call
   @Header	specifies the header with the value of the annotated parameter




    */
}
