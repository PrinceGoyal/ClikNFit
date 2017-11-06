package com.cliknfit.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.interfaces.SOService;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.DataModelResultObj;
import com.cliknfit.pojo.DataTrainerInfo;
import com.cliknfit.pojo.ModelBookingHistory;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.TimerScreenModel;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.pojo.WorkOutResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

import static com.cliknfit.R.id.address;
import static com.cliknfit.util.Constants.client_id;

/**
 * Created by Prince on 11/7/2016.
 */
public class CommonAsyncTask {
    private SOService mService;
    private ProgressDialog pd;
    private ProgressBar pb;
    private Context context;
    private ApiResponse listener;
    private int count;
    private String refresh = "";


    public CommonAsyncTask(Context context) {
        this.context = context;
        listener = (ApiResponse) context;
        pd = new ProgressDialog(context);
        pd.setTitle("Loading!");
        pd.setMessage("Please wait ... ");
        pd.setCancelable(false);
        mService = ApiUtils.getSOService();
    }


    public CommonAsyncTask(Context context, String home) {
        this.context = context;
        listener = (ApiResponse) context;
        mService = ApiUtils.getSOService();
    }

    public CommonAsyncTask(Context context, ProgressBar pb) {
        this.context = context;
        listener = (ApiResponse) context;
        this.pb = pb;
        mService = ApiUtils.getSOService();
    }


    public CommonAsyncTask(Context context, ApiResponse listener, ProgressBar pb, int count, String refresh) {
        this.context = context;
        this.listener = listener;
        this.pb = pb;
        this.count = count;
        this.refresh = refresh;
        mService = ApiUtils.getSOService();
    }

    public CommonAsyncTask(Context context, ApiResponse listener) {
        this.context = context;
        this.listener = listener;
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait ... ");
        pd.setCancelable(false);
        mService = ApiUtils.getSOService();
    }
    public void updatePasswordtask(String password,String id, final String str) {
        pd.show();
        mService.updatePassword(id,password).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void submitRatetask(String review, String notes, String rate,String requestId, final String str) {
        pd.show();
        mService.submitRate(review, notes, rate,requestId).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void updateOnPushtask(String id,final String str) {
      //  pd.show();
        mService.updateOnPush(id).enqueue(new Callback<TimerScreenModel>() {
            @Override
            public void onResponse(Call<TimerScreenModel> call, Response<TimerScreenModel> response) {
             //   pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TimerScreenModel> call, Throwable t) {
             //   pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void requestMoreHourTask(String starttime,String id,String hour,final String str) {
        pd.show();
        mService.requestMoreHour(starttime,id,hour).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void cancelTask(String id, final String str) {
        pd.show();
        mService.cancelrequest(id).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void startTimerTask(String id, final String str) {
        pd.show();
        mService.startTimer(id).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void registertask(String dob, String name, String email, String password, String login_type, String facebook_id, String google_id, String phone,
                             String device_id, String device_token, String device_type,String country_code, String lat, String longti,String image,String age,
                             String address,String display_phone,String city,
                             final String str) {
        pd.show();
        mService.register(dob, name, email, password, login_type, facebook_id, google_id, phone,
                device_id, device_token, device_type,country_code,lat, longti,image,age,address,display_phone,city).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }
    public void getAddressTask(String id,final String str) {
        pd.show();
        mService.getAddress(id).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void saveAddressTask(String id, String address, final String str) {
        pd.show();
        mService.saveAddress(id,address).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void confirmBooktask(String client_id, String trainer_id, String start_time, String end_time, String actual_start_time, String actual_end_time, String workoutlocation_id, String location_tag,
                             String location_address, String no_of_people,String nonce,final String str) {
        pd.show();
        mService.confirmBook(client_id, trainer_id, start_time, end_time, actual_start_time, actual_end_time, workoutlocation_id, location_tag,
                 location_address, no_of_people,nonce).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void reqBooktask(String start_time, String end_time,String no_of_people,final String str) {
        pd.show();
        mService.reqBook(start_time, end_time,no_of_people).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }




    public void deleteImagetask(String image_id,final String str) {
        pd.show();
        mService.deleteImage(AppPreference.getPreference(context,Constants.USERID),image_id).enqueue(new Callback<CommonStatus>() {
            @Override
            public void onResponse(Call<CommonStatus> call, Response<CommonStatus> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatus> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }
    public void resendOtpTask(String otp, String phone, final String str) {
        pd.show();
        mService.oTpResend(phone).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void otpTask(String otp, String phone, final String str) {
        pd.show();
        mService.oTp(otp, phone).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void updateProfiletask(String name,
                                  String id,
                                  String health_problems,
                                  String workout_interests,
                                  String workout_locations,
                                  String current_weight,
                                  String target_weight,
                                  String bmi,
                                  String body_fat,
                                  String additional_info,
                                  String about_me,
                                  String speciality,
                                  String city,String health,
                                  String location,String interest,
                                  final String str) {
        pd.show();
        mService.updateProfile(name, id, health_problems, workout_interests, workout_locations,
                current_weight, target_weight, bmi, body_fat, additional_info, about_me, speciality,city,health,
                location,interest)
                .enqueue(new Callback<CommonStatusResultObj>() {
                    @Override
                    public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                        pd.dismiss();
                        if (response.isSuccessful()) {
                            //  Log.d("Complaint", response.body().getStatus().toString());
                            listener.getResponse(response.body(), str);
                        } else {
                            int statusCode = response.code();
                            Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                        pd.dismiss();
                        Log.e("Service Fail reason", t.toString());
                    }
                });
    }



    public void agreeTncTask(final String str) {
        pd.show();
        mService.agreeTnc(AppPreference.getPreference(context,Constants.USERID),"1")
                .enqueue(new Callback<CommonStatusResultObj>() {
                    @Override
                    public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                        pd.dismiss();
                        if (response.isSuccessful()) {
                            //  Log.d("Complaint", response.body().getStatus().toString());
                            listener.getResponse(response.body(), str);
                        } else {
                            int statusCode = response.code();
                            Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                        pd.dismiss();
                        Log.e("Service Fail reason", t.toString());
                    }
                });
    }


    public void healthMastertask(final String str) {
        pd.show();
        mService.healthmaster("").enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void forgetPassTask(String countrycode,String email, String phone, final String str) {
        pd.show();
        mService.forgetPass(email,countrycode,phone).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void profileTask(String id,final String str) {
        pd.show();
        mService.profile(id).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    if(!response.body().isError())
                    listener.getResponse(response.body(), str);
                    else
                        Alerts.okAlert(context,"Server error","");
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void profiletrainerTask(String id,final String str) {
        pd.show();
        mService.profileTrainer(id).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void updateMobiletask(String countryCode,String phone,String id, final String str) {
        pd.show();
        mService.updateMobile(countryCode,phone,id).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }
    public void logintask(String email, String password, String login_type, String facebook_id, String google_id,
                          String device_id, String device_token, String device_type, String lat, String longti, final String str) {
        if(pd!=null)
        pd.show();
        mService.login(email, password, login_type, facebook_id, google_id,
                device_id, device_token, device_type, lat, longti).enqueue(new Callback<CommonStatusResultObj>() {
            @Override
            public void onResponse(Call<CommonStatusResultObj> call, Response<CommonStatusResultObj> response) {
                if(pd!=null)
                pd.dismiss();
                if (response.isSuccessful()) {
                      Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatusResultObj> call, Throwable t) {
                if(pd!=null)
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void getBookingHistorytask(String id, final String str) {
        pd.show();
        mService.BookingHistory(id).enqueue(new Callback<ModelBookingHistory>() {
            @Override
            public void onResponse(Call<ModelBookingHistory> call, Response<ModelBookingHistory> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelBookingHistory> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void getSessiontask(String id, final String str) {
        pd.show();
        mService.sessionList(id).enqueue(new Callback<ModelMySession>() {
            @Override
            public void onResponse(Call<ModelMySession> call, Response<ModelMySession> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelMySession> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void gettrainertask(String lat,String lon,final String str) {
        pd.show();
        mService.trainerList(AppPreference.getPreference(context,Constants.USERID),lat,lon).enqueue(new Callback<DataTrainerInfo>() {
            @Override
            public void onResponse(Call<DataTrainerInfo> call, Response<DataTrainerInfo> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataTrainerInfo> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void gettrainerSearch(String keyword,final String str) {
        pd.show();
        mService.trainerSearchList(keyword).enqueue(new Callback<DataTrainerInfo>() {
            @Override
            public void onResponse(Call<DataTrainerInfo> call, Response<DataTrainerInfo> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Log.d("json", response.body().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataTrainerInfo> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }

    public void qnaListtask(final String str) {
        pd.show();
        mService.qnaList("").enqueue(new Callback<CommonStatus>() {
            @Override
            public void onResponse(Call<CommonStatus> call, Response<CommonStatus> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatus> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }



    public void qnaSubmittask(String user_id,String question_id,String answer_id,final String str) {
        pd.show();
        mService.qnaSubmit(user_id,question_id,answer_id).enqueue(new Callback<CommonStatus>() {
            @Override
            public void onResponse(Call<CommonStatus> call, Response<CommonStatus> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatus> call, Throwable t) {
                pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }


    public void newsFeedtask(final String str) {
        pd.show();
        mService.newsFeedList("").enqueue(new Callback<CommonStatus>() {
            @Override
            public void onResponse(Call<CommonStatus> call, Response<CommonStatus> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    //  Log.d("Complaint", response.body().getStatus().toString());
                    listener.getResponse(response.body(), str);
                } else {
                    int statusCode = response.code();
                    Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonStatus> call, Throwable t) {
               pd.dismiss();
                Log.e("Service Fail reason", t.toString());
            }
        });
    }



}
