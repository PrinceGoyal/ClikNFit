package com.cliknfit.util;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gaurav
 */
public class Validations {
    // validating emailuser id
    public static boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        if (email.equals("") || email.length() <= 0) {
            editText.setError(Constants.REQUIRED_FIELD);
            editText.requestFocus();
            return false;
        } else {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches())
                return true;
            else {
                editText.setError(Constants.INVALID_EMAIL);
                editText.requestFocus();
                return false;
            }
        }
    }

    // validating username
    public static boolean isValidUsername(String username) {
        String USERNAME_PATTERN = "^[a-z_-]{3,15}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String PASSWORD_PATTERN = "^[a-zA-Z0-9. ]{6,25}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // validating username
    public static boolean isValidMobile(EditText editText) {
        String str = editText.getText().toString();
        if (str.length() > 13)
            return true;
        else {
            editText.setError(Constants.INVALID_MOBILE);
            editText.requestFocus();
            return false;
        }
    }
    public static boolean isStringNotEmpty(String uname) {
        if (uname.equals("") || uname.length() <= 0) {
            return false;
        } else
            return true;
    }

    public static boolean isFieldNotEmpty(EditText editText) {
        String uname = editText.getText().toString().trim();
        if (uname.equals("") || uname.length() <= 0) {
            editText.setError(Constants.REQUIRED_FIELD);
            editText.requestFocus();
            return false;
        } else
            return true;
    }

    public static boolean isFieldNotSelect(EditText editText) {
        String uname = editText.getText().toString().trim();
        if (uname.equalsIgnoreCase("select") || uname.length() <= 0) {
            editText.requestFocus();
            return false;
        } else
            return true;
    }


    public static String SelectedRadioGroupValue(RadioGroup radioGroup) {

        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);

        if(idx>=0){
            return  radioButton.getText().toString();
        }else {
            return "";
        }
    }

    public static boolean isPassMatch(EditText pass, EditText repass) {
        String txtpass = pass.getText().toString().trim();
        String txtrepass = repass.getText().toString().trim();
        if (!txtpass.equals(txtrepass)) {
            repass.setError(Constants.REQUIRED_FIELD);
            repass.requestFocus();
            return false;
        } else
            return true;
    }

    public static boolean isTextViewFieldNotEmpty(TextView editText) {
        String uname = editText.getText().toString().trim();
        if (uname.equals("") || uname.length() <= 0) {
            return false;
        } else
            return true;
    }

    public static boolean isRadioButtonEmpty(Context context, RadioGroup radioGroup) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);

        if(radioGroup.getCheckedRadioButtonId()>=0){
            return true;
        }else {
            radioGroup.requestFocus();
            Toast.makeText(context, Constants.REQUIRED_FIELD, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean isValidYoutube(EditText editText) {
        String url = editText.getText().toString().trim();
        if (url == null || url.isEmpty()) {
            editText.setError(Constants.INVALIDE_LINK);
            editText.requestFocus();
            return false;
        } else if (URLUtil.isValidUrl(url)) {
            Uri uri = Uri.parse(url);
            if ("www.youtube.com".equals(uri.getHost()) || "www.youtu.be".equals(uri.getHost())) {
                return true;
            } else if (url.contains("https://youtu.be/") || url.contains("https://www.youtube.com/watch?v=")) {
                return true;
            } else {
                editText.setError(Constants.INVALIDE_LINK);
                editText.requestFocus();
                return false;
            }
        } else {
            editText.setError(Constants.INVALIDE_LINK);
            editText.requestFocus();
            return false;
        }
    }


}
//https://www.youtube.com/watch?v