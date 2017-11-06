package com.cliknfit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cliknfit.pojo.UserModel;

/**
 * Created by Prince on 11/7/2016.
 */
public class AppPreference {



    public static void setUser(Context context, UserModel userModel) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.Email, userModel.getEmail());
            editor.putString(Constants.USERID, String.valueOf(userModel.getId()));
            editor.putString(Constants.USERNAME, userModel.getUsername());
            editor.putString(Constants.Mobile, userModel.getDisplay_phone());
            editor.putString(Constants.UPDATEDAT, userModel.getUpdated_at());
            editor.putString(Constants.CREATEDAT, userModel.getCreated_at());
            editor.putString(Constants.STATUS, String.valueOf(userModel.getStatus()));
            editor.putString(Constants.AGREE, String.valueOf(userModel.getIs_agree()));
            editor.putString(Constants.COUNTRYCODE, String.valueOf(userModel.getCountry_code()));
            editor.putString(Constants.Dob, String.valueOf(userModel.getDob()));

            editor.putString(Constants.GYMADDRESS, userModel.getGym_address());
            editor.putString(Constants.NAME, userModel.getName());
            editor.putString(Constants.PROFILEPIC, userModel.getImage());
            editor.putString(Constants.ADDRESS, userModel.getAddress());
            editor.putString(Constants.POINTS, ""+userModel.getDisplay_point());
            editor.putString(Constants.RATE, String.valueOf(userModel.getRating()));
            editor.putString(Constants.ISONLINE, String.valueOf(userModel.getIs_online()));
            editor.putString(Constants.AGE, String.valueOf(userModel.getAge()));
            editor.putString(Constants.CITY, userModel.getCity());
            editor.putString(Constants.ABOUT_ME, userModel.getAbout_me());
            editor.putString(Constants.ADITIONAL_INFO, userModel.getAdditional_info());
            editor.putString(Constants.SPECIALITY, userModel.getSpeciality());
            editor.putString(Constants.MOTTO, userModel.getMotto());

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }





    public static void setPreference(Context context, String key, String value) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getPreference(Context context, String key) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString(key, Constants.BLANCK);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Constants.BLANCK;
    }



    public static void setBooleanPreference(Context context, String key, Boolean value) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Boolean getBooleanPreference(Context context, String key) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getBoolean(key, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public static void setIntegerPreference(Context context, String key, Integer value) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Integer getIntegerPreference(Context context, String key) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getInt(key, 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getimgPreference(Context context, String key) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString(key, "0");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Constants.BLANCK;
    }
    public static boolean clearPreference(Context context) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            return true;
    }

    public static void setCartCount(Context context, int count) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("cart_count", count);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static Integer getCartCount(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getInt("cart_count", 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}
