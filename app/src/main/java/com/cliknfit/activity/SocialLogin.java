package com.cliknfit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.util.Constants;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static android.R.attr.bitmap;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.f;

public class SocialLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private Button fb_login;
    private Button gpluslogin;
    private int GPLS_SIGN_IN = 100;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback;
    private String profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        facebooklogin();
        initializationForgplus();
        String socialname = getIntent().getStringExtra("socialname");
        if (socialname.equals("facebook")) {
            onFblogin();
        } else if (socialname.equals("google")) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, GPLS_SIGN_IN);
        }
    }

    private void initializationForgplus() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
  /*      PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("mas.picquick", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPLS_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void facebooklogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
    }

    private void onFblogin() {
        final String numericStr = "^[0-9]*$";
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        Log.i("LoginActivity",
                                                response.toString());
                                        try {
                                            String id = object.getString("id");
                                            try {
                                                URL profile_pic = new URL(
                                                        "http://graph.facebook.com/" + id + "/picture?type=large");
                                             /*   Bitmap profilePic = BitmapFactory.decodeStream(profile_pic.openConnection().getInputStream());

                                                File file = getfile(profilePic);*/
                                                profilepic = profile_pic.toString();

                                                Log.i("profile_pic",
                                                        profile_pic + "");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            String username = object.getString("name");
                                            String email = object.getString("email");
                                            if (id != null) {
                                                //   Toast.makeText(SocialLogin.this, "fb login" + id, Toast.LENGTH_SHORT).show();
                                                LoginManager.getInstance().logOut();
                                                Intent returnFromGalleryIntent = new Intent();
                                                returnFromGalleryIntent.putExtra("profilepic", profilepic);
                                                returnFromGalleryIntent.putExtra("email", email);
                                                returnFromGalleryIntent.putExtra("socialid", id);
                                                returnFromGalleryIntent.putExtra("name", username);
                                                setResult(RESULT_OK, returnFromGalleryIntent);
                                                finish();
                                            } else {
                                                LoginManager.getInstance().logOut();
                                                Intent returnFromGalleryIntent = new Intent();
                                                returnFromGalleryIntent.putExtra("profilepic", "");
                                                returnFromGalleryIntent.putExtra("email", "");
                                                returnFromGalleryIntent.putExtra("socialid", "");
                                                returnFromGalleryIntent.putExtra("name", "");
                                                setResult(RESULT_OK, returnFromGalleryIntent);
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            LoginManager.getInstance().logOut();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }


                    @Override
                    public void onCancel() {
                        Intent returnFromGalleryIntent = new Intent();
                        returnFromGalleryIntent.putExtra("profilepic", "");
                        returnFromGalleryIntent.putExtra("email", "");
                        returnFromGalleryIntent.putExtra("socialid", "");
                        returnFromGalleryIntent.putExtra("name", "");
                        setResult(RESULT_OK, returnFromGalleryIntent);
                        finish();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Intent returnFromGalleryIntent = new Intent();
                        returnFromGalleryIntent.putExtra("profilepic", "");
                        returnFromGalleryIntent.putExtra("email", "");
                        returnFromGalleryIntent.putExtra("socialid", "");
                        returnFromGalleryIntent.putExtra("name", "");
                        setResult(RESULT_OK, returnFromGalleryIntent);
                        finish();
                    }
                });
    }

    private File getfile(Bitmap profilePic) {
        //create a file to write bitmap data
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, Constants.IMAGEFOLDER);
        /* if specified not exist create new */
        if (!folder.exists()) {
            folder.mkdir();
        } else {
            if (folder.isDirectory()) {
                if (folder.list().length > 0) {
                    String[] children = folder.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(folder, children[i]).delete();
                    }
                } else
                    folder.delete();
            }
            folder.mkdir();
        }
        File imageFile = new File(folder, "clicknfitprofile.png");

        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap = profilePic;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        int statusCode = result.getStatus().getStatusCode();
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (!acct.getEmail().isEmpty()) {
                //   Toast.makeText(this, acct.getId().toLowerCase().toString(), Toast.LENGTH_SHORT).show();
                Intent returnFromGalleryIntent = new Intent();
                String profilepic = "https://plus.google.com/s2/photos/profile/" + acct.getId() + "?sz=300";
                returnFromGalleryIntent.putExtra("profilepic", profilepic);
                returnFromGalleryIntent.putExtra("email", acct.getEmail());
                returnFromGalleryIntent.putExtra("socialid", acct.getId());
                returnFromGalleryIntent.putExtra("name", acct.getDisplayName());
                setResult(RESULT_OK, returnFromGalleryIntent);
                finish();
            }
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            Intent returnFromGalleryIntent = new Intent();
            returnFromGalleryIntent.putExtra("profilepic", "");
            returnFromGalleryIntent.putExtra("email", "");
            returnFromGalleryIntent.putExtra("socialid", "");
            returnFromGalleryIntent.putExtra("name", "");
            setResult(RESULT_OK, returnFromGalleryIntent);
            finish();

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
