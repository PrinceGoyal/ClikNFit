package com.cliknfit.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.braintreepayments.api.Json;
import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.DataModel;
import com.cliknfit.pojo.DataModelResultObj;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.pojo.WorkOutResult;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.GetImageContent;
import com.cliknfit.util.RoundedImageView;
import com.cliknfit.util.Validations;
import com.facebook.internal.ImageDownloader;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.Field;

import static android.icu.util.MeasureUnit.MEGABYTE;
import static com.cliknfit.R.id.card_number;
import static com.cliknfit.R.id.card_type;
import static com.cliknfit.R.id.profile_pic;
import static com.cliknfit.R.id.userloc;
import static com.cliknfit.activity.Dashboard.overlay;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.o;

public class Register extends AppCompatActivity implements ApiResponse {

    private static final int MEGABYTE = 1024 * 1024;
    TextView facebook;
    TextView googleplus;
    EditText name;
    EditText password, dob, countrycode;
    EditText temail;
    EditText mobile;
    TextView signup, forgotpassword;
    ImageView back;
    private Context context;
    private String loginType = "phone", device_type = "android";
    private String fbId = "", gPlusId = "", devideId = "56416516";
    private String lati = "28.603771", longti = "77.356335";
    private int SOCIALCALLBACKFACEBOOK = 1365;
    private int SOCIALCALLBACKGOOGLE = 6123;
    private String profilePic = "";
    private String age = "";
    private String address1 = "";
    private String city = "";
    private Bitmap profilebitmap = null;
    private ProgressDialog pd;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        initViews();
    }

    private void getaddres() {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String address = null;
        String newaddress = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    Double.valueOf(lati), Double.valueOf(longti), 1);
            if (addresses != null && addresses.size() > 0) {
                        /*Address addresses = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality();*/

                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                if (address.contains("Unnamed")) {
                    address = addresses.get(0).getAddressLine(0);
                }
                city = addresses.get(0).getLocality();
                if (city == null || city.equals("")) {
                    city = addresses.get(1).getLocality();
                }
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                if (address == null)
                    newaddress = city + ", " + state;
                else if (city == null)
                    newaddress = address + ", " + state;
                else if (state == null)
                    newaddress = address + ", " + city;
                else if (address == null && state == null)
                    newaddress = city;
                else if (city == null && state == null)
                    newaddress = address;
                else
                    newaddress = address + ", " + city + ", " + state;

            }
        } catch (IOException e) {
            //  Log.e(TAG, "Impossible to connect to Geocoder", e);
        } finally {
            address1 = newaddress;
        }
    }


    private void initViews() {
        lati = getIntent().getStringExtra("lati");
        longti = getIntent().getStringExtra("longti");
        getaddres();
        facebook = (TextView) findViewById(R.id.facebook);
        googleplus = (TextView) findViewById(R.id.googleplus);
        temail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        dob = (EditText) findViewById(R.id.dob);
        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        countrycode = (EditText) findViewById(R.id.countrycode);
        signup = (TextView) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        back = (ImageView) findViewById(R.id.back);

        if (getIntent().hasExtra("loginType")) {
            loginType = getIntent().getStringExtra("loginType");
            password.setText(getIntent().getStringExtra("password"));
            temail.setText(getIntent().getStringExtra("email"));
            name.setText(getIntent().getStringExtra("name"));
            profilePic = getIntent().getStringExtra("image");

            password.setFocusable(false);
            temail.setFocusable(false);
            name.setFocusable(false);

            password.setClickable(false);
            temail.setClickable(false);
            name.setClickable(false);
            if (loginType.equals("facebook")) {
                fbId = getIntent().getStringExtra("password");
            } else if (loginType.equals("google")) {
                gPlusId = getIntent().getStringExtra("password");
            }

        }
        click();
        mobileType();
    }




    private void mobileType() {
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 1) {
                    mobile.setText("(" + charSequence);
                    mobile.setSelection(mobile.getText().length());
                }
                if (i == 3 && i2 == 1) {
                    mobile.setText(charSequence + ") ");
                    mobile.setSelection(mobile.getText().length());
                }
                if (i == 8 && i2 == 1) {
                    mobile.setText(charSequence + "-");
                    mobile.setSelection(mobile.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void click() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    if (getIntent().hasExtra("loginType")) {
                        registerhere();
                    } else
                        registermethod();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fbintent = new Intent(context, SocialLogin.class);
                fbintent.putExtra("socialname", "facebook");
                startActivityForResult(fbintent, SOCIALCALLBACKFACEBOOK);
            }
        });
        googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fbintent = new Intent(context, SocialLogin.class);
                fbintent.putExtra("socialname", "google");
                startActivityForResult(fbintent, SOCIALCALLBACKGOOGLE);
            }
        });
    }

    private void registerhere() {
        String url;
        String refreshedToken = AppPreference.getPreference(context, Constants.REGID);
        String phone = countrycode.getText().toString() + mobile.getText().toString().replace("(", "").replace(")", "").replace("-", "").replaceAll("\\s+", "");
        HashMap<String, Object> hm = new HashMap<>();


        url = Constants.BASE_URL + "client/signup";
        hm.put("dob", dob.getText().toString());
        hm.put("name", name.getText().toString());
        hm.put("email", temail.getText().toString());
        hm.put("password", password.getText().toString());
        hm.put("login_type", loginType);
        hm.put("facebook_id", fbId);
        hm.put("google_id", gPlusId);
        hm.put("phone", phone);
        hm.put("device_id", devideId);
        hm.put("device_token", refreshedToken);
        hm.put("device_type", device_type);
        hm.put("country_code", countrycode.getText().toString());
        hm.put("lat", lati);
        hm.put("lan", longti);
        hm.put("age", age);
        hm.put("address", address1);
        hm.put("display_phone", mobile.getText().toString());
        hm.put("city", city);
        hm.put("social_image", profilePic);



        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading ...");
        pd.setCancelable(false);
        AQuery aq = new AQuery(this);

        aq.progress(pd).ajax(url, hm, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject obj, AjaxStatus status) {
                if (obj != null) {
                    try {
                        if (!obj.getBoolean("error")) {
                            JSONObject data = obj.getJSONObject("data");
                            JSONObject result = data.getJSONObject("results");
                            JSONObject client = result.getJSONObject("client");

                            UserModel userModel = new UserModel();

                            userModel.setEmail(client.getString("email"));
                            userModel.setId(client.getInt("id"));
                            userModel.setUsername(client.getString("username"));
                            userModel.setDisplay_phone(client.getString("display_phone"));
                            userModel.setStatus(client.getInt("status"));
                            userModel.setIs_agree(client.getInt("is_agree"));
                            userModel.setCountry_code(client.getString("country_code"));
                            userModel.setDob(client.getString("dob"));
                            userModel.setImage(client.getString("image"));
                            userModel.setAddress(client.getString("address"));
                            userModel.setDisplay_point(client.getInt("display_point"));
                            userModel.setIs_online(client.getInt("rating"));
                            userModel.setAge(client.getInt("age"));
                            userModel.setCity(client.getString("city"));
                            userModel.setAbout_me(client.getString("about_me"));
                            userModel.setAdditional_info(client.getString("additional_info"));
                            userModel.setSpeciality(client.getString("speciality"));
                            userModel.setMotto(client.getString("motto"));

                            AppPreference.setUser(context, userModel);
                            AppPreference.setPreference(context, Constants.PASSWORD, password.getText().toString());
                            AppPreference.setPreference(context, Constants.LOGINTYPE, loginType);
                            AppPreference.setPreference(context, Constants.FBID, fbId);
                            AppPreference.setPreference(context, Constants.GPLUSID, gPlusId);
                            AppPreference.setPreference(context, Constants.DEVICEID, devideId);
                            AppPreference.setPreference(context, Constants.DEVICETYPE, device_type);
                            startActivity(new Intent(context, OtpVerification.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        } else {
                            Alerts.okAlert(context, obj.getString("message"), "");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private boolean isValid() {
        if (!Validations.isFieldNotEmpty(name))
            return false;
        else if (!Validations.isValidEmail(temail))
            return false;
        else if (!Validations.isFieldNotEmpty(password))
            return false;
        else if (!Validations.isValidMobile(mobile))
            return false;
        else {
            return true;
        }
    }

    private void calendar() {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int a = mYear - 18;
        Calendar maxcalendar = Calendar.getInstance();
        maxcalendar.set(Calendar.YEAR, a);
        maxcalendar.set(Calendar.MONTH, mMonth);
        maxcalendar.set(Calendar.DAY_OF_MONTH, mDay);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String m, y, d;
                        m = "" + (monthOfYear + 1);
                        y = "" + year;
                        d = "" + dayOfMonth;

                        if (monthOfYear < 9) {
                            m = "0" + m;
                        }
                        if (dayOfMonth < 10) {
                            d = "0" + dayOfMonth;
                        }
                        dob.setText(y+ "-" +m + "-" + d);
                        age = String.valueOf(mYear - year);
                        signup.requestFocus();
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(maxcalendar.getTimeInMillis());


        // c.add(Calendar.DATE, 280);
        dpd.setCancelable(true);
        dpd.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOCIALCALLBACKFACEBOOK) {
            if (data != null) {
                profilePic = data.getStringExtra("profilepic");
                temail.setText(data.getStringExtra("email"));
                fbId = data.getStringExtra("socialid");
                name.setText(data.getStringExtra("name"));
                if (!fbId.equals("")) {
                    password.setText(fbId);
                    loginType = "facebook";

                    password.setFocusable(false);
                    temail.setFocusable(false);
                    name.setFocusable(false);

                    password.setClickable(false);
                    temail.setClickable(false);
                    name.setClickable(false);
                }
            }
        }

        if (requestCode == SOCIALCALLBACKGOOGLE) {
            if (data != null) {
                profilePic = data.getStringExtra("profilepic");
                temail.setText(data.getStringExtra("email"));
                gPlusId = data.getStringExtra("socialid");
                name.setText(data.getStringExtra("name"));
                if (!gPlusId.equals("")) {
                    password.setText(gPlusId);
                    loginType = "google";
                    password.setFocusable(false);
                    temail.setFocusable(false);
                    name.setFocusable(false);

                    password.setClickable(false);
                    temail.setClickable(false);
                    name.setClickable(false);
                }
            }
        }
    }


    private void registermethod() {
        String refreshedToken = AppPreference.getPreference(context, Constants.REGID);
        CommonAsyncTask ca = new CommonAsyncTask(this);

        String phone = countrycode.getText().toString() + mobile.getText().toString().replace("(", "").replace(")", "").replace("-", "").replaceAll("\\s+", "");
        ca.registertask(dob.getText().toString(), name.getText().toString(),
                temail.getText().toString(),
                password.getText().toString(),
                loginType,
                fbId, gPlusId,
                phone, devideId, refreshedToken, device_type, countrycode.getText().toString(), lati, longti, profilePic, age,
                address1, mobile.getText().toString(), city,
                "reg"
        );
    }


    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parentModel = (CommonStatusResultObj) response;

        if (!parentModel.isError()) {
            DataModelResultObj dataModel = parentModel.getData();
            WorkOutResult userModel1 = dataModel.getResults();
            UserModel userModel = userModel1.getUser();

            AppPreference.setUser(context, userModel);
            AppPreference.setPreference(context, Constants.PASSWORD, password.getText().toString());
            AppPreference.setPreference(context, Constants.LOGINTYPE, loginType);
            AppPreference.setPreference(context, Constants.FBID, fbId);
            AppPreference.setPreference(context, Constants.GPLUSID, gPlusId);
            AppPreference.setPreference(context, Constants.DEVICEID, devideId);
            AppPreference.setPreference(context, Constants.DEVICETYPE, device_type);
            startActivity(new Intent(context, OtpVerification.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } else {
            Alerts.okAlert(context, parentModel.getMessage(), "");
        }

    }
}
