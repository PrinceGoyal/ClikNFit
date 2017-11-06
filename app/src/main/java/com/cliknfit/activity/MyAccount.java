package com.cliknfit.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.BlurTransform;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.GetImageContent;
import com.cliknfit.util.InternalStorageContentProvider;
import com.cliknfit.util.Validations;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.CropImage;

import static android.R.attr.bitmap;
import static android.R.attr.path;
import static android.R.attr.type;
import static com.cliknfit.R.id.capture1;
import static com.cliknfit.R.id.capture2;
import static com.cliknfit.R.id.capture3;
import static com.cliknfit.R.id.capture4;
import static com.cliknfit.R.id.capture5;
import static com.cliknfit.R.id.capture6;
import static com.cliknfit.R.id.capture7;
import static com.cliknfit.R.id.capture8;
import static com.cliknfit.R.id.capture9;
import static com.cliknfit.R.id.start;
import static com.cliknfit.util.AppPreference.getPreference;


public class MyAccount extends AppCompatActivity implements ApiResponse {
    ImageView banner, userImage;
    private EditText mobile, countrycode, email, dob;

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    private String TAG = "";
    private static final int PERMISSIONS_REQUEST_STORAGE = 1650;
    private static final int REQUEST_CAMERA = 7890;
    private static final int SELECT_FILE = 9870;
    private static final int LOCATION_CALLBACK = 1100;
    private Bitmap bitmap = null;
    public static final int REQUEST_CODE_CROP_IMAGE = 1330;
    String path = "";
    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    boolean mobileEditFlag = false;
    private TextView userName;
    private TextView edit_mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        forimageppath();
        initViews();
    }

    private void forimageppath() {
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(this.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

    private void initViews() {

        banner = (ImageView) findViewById(R.id.banner);
        userImage = (ImageView) findViewById(R.id.user_image);
        edit_mobile = (TextView) findViewById(R.id.edit_mobile);
        mobile = (EditText) findViewById(R.id.mobile);
        countrycode = (EditText) findViewById(R.id.countrycode);
        mobile.setEnabled(false);
        countrycode.setEnabled(false);

        email = (EditText) findViewById(R.id.email);
        dob = (EditText) findViewById(R.id.dob);
        userName = (TextView) findViewById(R.id.userName);
        userName.setText(AppPreference.getPreference(MyAccount.this, Constants.NAME));


        mobile.setText(getPreference(this, Constants.Mobile));
        countrycode.setText(getPreference(this, Constants.COUNTRYCODE));


        email.setText(getPreference(this, Constants.Email));
        dob.setText(getPreference(this, Constants.Dob));

        String imageid = Constants.PICBASE_URL + getPreference(this, Constants.PROFILEPIC);
        Picasso.with(this)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(new BlurTransform(this))
                .into(banner);
        Picasso.with(this)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(userImage);

        click();

    }

    private void click() {
        edit_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (!mobileEditFlag) {
                    mobileEditFlag = true;
                    mobile.setFocusable(true);
                    mobile.requestFocus();
                } else {
                    mobileEditFlag = false;*/
                if (Validations.isValidMobile(mobile) && Validations.isFieldNotEmpty(countrycode))
                    changeMobile();
                // }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkimagepermission();
            }
        });
    }

    private void changeMobile() {
        String phone=countrycode.getText().toString()+mobile.getText().toString().replace("(","").replace(")","").replace("-","").replaceAll("\\s+","");

        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.updateMobiletask(countrycode.getText().toString(), phone, getPreference(this, Constants.USERID), "updatemobile"
        );
    }

    private void checkimagepermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_STORAGE);
            } else {
                selectImage();
            }
        } else {
            selectImage();
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select!");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePicture();
                } else if (items[item].equals("Choose from Library")) {
                    openGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_FILE);
    }


    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", mFileTemp);
            } else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            Log.d("", "cannot take picture", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            switch (requestCode) {
                case SELECT_FILE:
                    Uri uri;
                    try {
                        uri = data.getData();
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        Log.e(TAG, "Error while creating temp file", e);
                    }
                    break;
                case REQUEST_CAMERA:
                    startCropImage();
                    break;
                case REQUEST_CODE_CROP_IMAGE:
                    try {
                        path = data.getStringExtra(CropImage.IMAGE_PATH);
                        if (path == null) {
                            return;
                        }
                        uri = Uri.parse(path);
                        bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());

                   /*  pronamelayout.setVisibility(View.GONE);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT).replace("\n", "").replace("\r", "");
*/
                        if (uri == null) {
                            uri = GetImageContent.getImageUri(bitmap, MyAccount.this);
                        }
                        File file = new File(GetImageContent.
                                getRealPathFromURI(uri, this));

                        if (file != null) {
                            userImage.setImageBitmap(bitmap);
                            banner.setImageBitmap(bitmap);

                            upload(file);

                        } else
                            Toast.makeText(MyAccount.this, "File Not selected properly", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void upload(File file) {
        String url;
        HashMap<String, Object> hm = new HashMap<>();

        url = Constants.BASE_URL + "client/update_profile";
        hm.put("image", file);
        hm.put("id", AppPreference.getPreference(this, Constants.USERID));

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading ...");
        pd.setCancelable(false);
        AQuery aq = new AQuery(this);

        aq.progress(pd).ajax(url, hm, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONObject obj = new JSONObject(object);
                        if (!obj.getBoolean("error")) {
                            JSONObject data = obj.getJSONObject("data");
                            JSONObject result = data.getJSONObject("results");
                            JSONObject client = result.getJSONObject("client");
                            AppPreference.setPreference(MyAccount.this, Constants.PROFILEPIC, client.getString("image"));
                            Alerts.okAlert(MyAccount.this, "Image updated successfully", "");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editprofile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            super.onBackPressed();
            return true;
        }
        if (id == R.id.editprofile) {
            edit_mobile.setVisibility(View.VISIBLE);
            mobile.setEnabled(true);
            countrycode.setEnabled(true);
            countrycode.requestFocus();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parent = (CommonStatusResultObj) response;
        if (!parent.isError()) {
            if (service.equals("updatemobile")) {
                if (!parent.isError()) {
                    startActivity(new Intent(MyAccount.this, OtpVerification.class)
                            .putExtra("cc", countrycode.getText().toString())
                            .putExtra("number", mobile.getText().toString())
                            .putExtra("otp", parent.getData().getResults().getOtp())
                    );
                }
            }
        }Alerts.okAlert(MyAccount.this,parent.getMessage(),"");
    }
}
