package com.cliknfit.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.DataModelResultObj;
import com.cliknfit.pojo.WorkChildModel;
import com.cliknfit.pojo.WorkOutResult;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.ConnectionDetector;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import eu.janmuller.android.simplecropimage.CropImage;

import static android.R.attr.path;
import static com.cliknfit.R.id.capture8;
import static com.cliknfit.R.id.capture9;
import static com.cliknfit.util.AppPreference.getPreference;
import static com.cliknfit.util.Constants.UPDATEPROFILE;
import static com.cliknfit.util.Constants.otherheath;
import static com.cliknfit.util.Constants.otherinterest;
import static com.cliknfit.util.Constants.otherlocation;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.U;

public class UpdateProfile extends AppCompatActivity implements ApiResponse {


    private ImageView expandedImageView;
    private int mShortAnimationDuration;
    private boolean imagezoomflag = false;
    private Rect startBounds;
    private float startScaleFinal;
    private Animator mCurrentAnimator;

    ImageView profilePic;
    EditText etName;
    EditText speciality, tv_myAddress;
    TextView tvRating;
    TextView tvPoints;
    EditText etCurrentWait;
    EditText etTargetWait;
    EditText etBmi;
    EditText etBodyFat;
    ImageView capture1;
    ImageView capture2;
    ImageView capture3;
    ImageView capture4;
    ImageView capture5;
    ImageView capture6;
    ImageView capture7;
    ImageView capture8;
    ImageView capture9;
    EditText tvAboutUs;
    EditText tvAdditionalInfo;
    TextView viewProfile;
    TextView save;
    private String TAG = "";
    private static int picId = 0;

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int PERMISSIONS_REQUEST_STORAGE = 165;
    private static final int REQUEST_CAMERA = 789;
    private static final int SELECT_FILE = 987;
    private static final int LOCATION_CALLBACK = 110;
    public static final int REQUEST_CODE_CROP_IMAGE = 133;
    private static File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo_u";
    private LinearLayout ll_healthConditionProblem1, ll_healthConditionProblem2, ll_workOutLocationpreference1, ll_workOutInterest1, ll_workOutInterest2, ll_workOutLocationpreference2;
    private ArrayList<WorkChildModel> helthproblemList;
    private ArrayList<WorkChildModel> workOutInterestList;
    private ArrayList<WorkChildModel> workOutLocation;
    private EditText tv_city;
    private ArrayList<String> selectedHealthProblem = new ArrayList<>();
    private ArrayList<String> selectedworkOutInterest = new ArrayList<>();
    private ArrayList<String> selectedworkOutLocation = new ArrayList<>();
    private ImageView select1, select2, select3, select4, select5, select6, select7, select8, select9;
    private boolean select1flag, select2flag, select3flag, select4flag, select5flag, select6flag, select7flag, select8flag, select9flag;
    private ImageView select_multiple, select_deselect, delete;
    private boolean selectflag = false;
    private boolean selectallflag = false;
    private ArrayList<String> imgIdList;

    private ArrayList<String> selectedImage = new ArrayList<>();
    private EditText other_health, other_workout_location, other_workoutinterest;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        initViews();
        getWorkoutdata();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    @Override
    public void onBackPressed() {
        AppPreference.setPreference(UpdateProfile.this, Constants.PROFILEUPDATED, "1");
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AppPreference.setPreference(UpdateProfile.this, Constants.PROFILEUPDATED, "1");
            finish();
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getWorkoutdata() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.healthMastertask("healthmaster");
    }

    private void forimageppath(int selected) {
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME + "" + selected + ".jpg");
        } else {
            mFileTemp = new File(UpdateProfile.this.getFilesDir(), TEMP_PHOTO_FILE_NAME + "" + selected + ".jpg");
        }
    }

    private void backfromimage() {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(expandedImageView, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.SCALE_Y, startScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                profilePic.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                profilePic.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }


    private void initViews() {
        expandedImageView = (ImageView) findViewById(R.id.fullimage);
        profilePic = (ImageView) findViewById(R.id.img_profile_pic);
        save = (TextView) findViewById(R.id.save);
        tv_myAddress = (EditText) findViewById(R.id.tv_myAddress);
        tvAboutUs = (EditText) findViewById(R.id.tv_aboutUs);
        speciality = (EditText) findViewById(R.id.speciality);
        tv_city = (EditText) findViewById(R.id.tv_city);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvPoints = (TextView) findViewById(R.id.tv_points);

        other_health = (EditText) findViewById(R.id.other_health);
        other_workout_location = (EditText) findViewById(R.id.other_workout_location);
        other_workoutinterest = (EditText) findViewById(R.id.other_workoutinterest);

        other_health.setText(AppPreference.getPreference(this, Constants.otherheath));
        other_workout_location.setText(AppPreference.getPreference(this, Constants.otherlocation));
        other_workoutinterest.setText(AppPreference.getPreference(this, Constants.otherinterest));


        etName = (EditText) findViewById(R.id.tv_Name);
        etCurrentWait = (EditText) findViewById(R.id.et_currentWait);
        etTargetWait = (EditText) findViewById(R.id.et_targetWait);
        etBmi = (EditText) findViewById(R.id.et_bmi);
        etBodyFat = (EditText) findViewById(R.id.et_bodyFat);

        tvAdditionalInfo = (EditText) findViewById(R.id.tv_additionalInfo);

        Picasso.with(UpdateProfile.this)
                .load(Constants.PICBASE_URL + AppPreference.getPreference(this, Constants.PROFILEPIC))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(profilePic);

        etName.setText(getPreference(UpdateProfile.this, Constants.NAME));
        speciality.setText(getPreference(UpdateProfile.this, Constants.AGE));
        tv_city.setText(getPreference(UpdateProfile.this, Constants.CITY));
        tvRating.setText(getPreference(UpdateProfile.this, Constants.RATE));
        tvPoints.setText(getPreference(UpdateProfile.this, Constants.POINTS));
        tvAboutUs.setText(getPreference(UpdateProfile.this, Constants.ABOUT_ME));
        tvAdditionalInfo.setText(getPreference(UpdateProfile.this, Constants.ADITIONAL_INFO));

        etBmi.setText(getPreference(UpdateProfile.this, Constants.BMI));
        etCurrentWait.setText(getPreference(UpdateProfile.this, Constants.CURRENTWEIGHT));
        etBodyFat.setText(getPreference(UpdateProfile.this, Constants.FAT));
        etTargetWait.setText(getPreference(UpdateProfile.this, Constants.TARGETWEIGHT));

        tv_myAddress.setText(getPreference(UpdateProfile.this, Constants.GYMADDRESS));

        capture1 = (ImageView) findViewById(R.id.capture1);
        capture2 = (ImageView) findViewById(R.id.capture2);
        capture3 = (ImageView) findViewById(R.id.capture3);
        capture4 = (ImageView) findViewById(R.id.capture4);
        capture5 = (ImageView) findViewById(R.id.capture5);
        capture6 = (ImageView) findViewById(R.id.capture6);
        capture7 = (ImageView) findViewById(R.id.capture7);
        capture8 = (ImageView) findViewById(R.id.capture8);
        capture9 = (ImageView) findViewById(R.id.capture9);

        select1 = (ImageView) findViewById(R.id.select1);
        select2 = (ImageView) findViewById(R.id.select2);
        select3 = (ImageView) findViewById(R.id.select3);
        select4 = (ImageView) findViewById(R.id.select4);
        select5 = (ImageView) findViewById(R.id.select5);
        select6 = (ImageView) findViewById(R.id.select6);
        select7 = (ImageView) findViewById(R.id.select7);
        select8 = (ImageView) findViewById(R.id.select8);
        select9 = (ImageView) findViewById(R.id.select9);

        select_multiple = (ImageView) findViewById(R.id.select_multiple);
        select_deselect = (ImageView) findViewById(R.id.select_deselect);
        delete = (ImageView) findViewById(R.id.delete);


        ll_healthConditionProblem1 = (LinearLayout) findViewById(R.id.ll_healthConditionProblem1);
        ll_healthConditionProblem2 = (LinearLayout) findViewById(R.id.ll_healthConditionProblem2);
        ll_workOutInterest1 = (LinearLayout) findViewById(R.id.ll_workOutInterest1);
        ll_workOutInterest2 = (LinearLayout) findViewById(R.id.ll_workOutInterest2);
        ll_workOutLocationpreference1 = (LinearLayout) findViewById(R.id.ll_workOutLocationpreference1);
        ll_workOutLocationpreference2 = (LinearLayout) findViewById(R.id.ll_workOutLocationpreference2);

        clicks();
    }


    private boolean isValid() {
        if (!Validations.isFieldNotEmpty(tvAboutUs))
            return false;
        else if (!Validations.isFieldNotEmpty(speciality))
            return false;
        else if (!Validations.isFieldNotEmpty(etName))
            return false;
        else if (!Validations.isFieldNotEmpty(etCurrentWait))
            return false;
        else if (!Validations.isFieldNotEmpty(etTargetWait))
            return false;
        else if (!Validations.isFieldNotEmpty(etBmi))
            return false;
        else if (!Validations.isFieldNotEmpty(etBodyFat))
            return false;
        else if (!Validations.isFieldNotEmpty(tvAdditionalInfo))
            return false;

        else {
            return true;
        }
    }

    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        Picasso.with(this)
                .load(Constants.PICBASE_URL + AppPreference.getPreference(this, Constants.PROFILEPIC))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.activity_profile)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;


        startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backfromimage();
                imagezoomflag = false;
            }
        });
    }


    private void clicks() {

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(profilePic);
            }
        });


        select_multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    selectflag = true;
                    select1.setVisibility(View.VISIBLE);
                    select2.setVisibility(View.VISIBLE);
                    select3.setVisibility(View.VISIBLE);
                    select4.setVisibility(View.VISIBLE);
                    select5.setVisibility(View.VISIBLE);
                    select6.setVisibility(View.VISIBLE);
                    select7.setVisibility(View.VISIBLE);
                    select8.setVisibility(View.VISIBLE);
                    select9.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    select_deselect.setVisibility(View.VISIBLE);

                    select_multiple.setImageDrawable(getResources().getDrawable(R.drawable.multiple_dark));
                } else {
                    selectflag = false;
                    select_multiple.setImageDrawable(getResources().getDrawable(R.drawable.multiple));
                    select1.setVisibility(View.GONE);
                    select2.setVisibility(View.GONE);
                    select3.setVisibility(View.GONE);
                    select4.setVisibility(View.GONE);
                    select5.setVisibility(View.GONE);
                    select6.setVisibility(View.GONE);
                    select7.setVisibility(View.GONE);
                    select8.setVisibility(View.GONE);
                    select9.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    select_deselect.setVisibility(View.GONE);
                }
            }
        });


        select_deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectallflag) {
                    deselectall();
                    selectallflag = false;
                    select_deselect.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                } else {
                    selectall();
                    selectallflag = true;
                    select_deselect.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_dark));
                }

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    picId = 11;
                    forimageppath(picId);
                    checkimagepermission();
            }
        });


        capture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 1;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 1) {
                        if (select1flag) {
                            select1flag = false;
                            select1.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select1flag = true;
                            select1.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 2;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 2) {
                        if (select2flag) {
                            select2flag = false;
                            select2.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select2flag = true;
                            select2.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 3;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 3) {
                        if (select3flag) {
                            select3flag = false;
                            select3.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select3flag = true;
                            select3.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 4;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 4) {
                        if (select4flag) {
                            select4flag = false;
                            select4.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select4flag = true;
                            select4.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 5;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 5) {
                        if (select5flag) {
                            select5flag = false;
                            select5.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {

                            select5flag = true;
                            select5.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 6;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 6) {
                        if (select6flag) {
                            select6flag = false;
                            select6.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select6flag = true;
                            select6.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 7;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 7) {
                        if (select7flag) {
                            select7flag = false;
                            select7.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select7flag = true;
                            select7.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 8;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 8) {
                        if (select8flag) {
                            select8flag = false;
                            select8.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select8flag = true;
                            select8.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
                        }
                    }
                }
            }
        });

        capture9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectflag == false) {
                    picId = 9;
                    forimageppath(picId);
                    checkimagepermission();
                } else {
                    if (imgIdList.size() >= 9) {
                        if (select9flag) {
                            select9flag = false;
                            select9.setImageDrawable(getResources().getDrawable(R.drawable.select));
                        } else {
                            select9flag = true;
                            select9.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));

                        }
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    updateInfo();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage.clear();
                for (int i = 0; i < imgIdList.size(); i++) {
                    if (i == 0) {
                        if (select1flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 1) {
                        if (select2flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 2) {
                        if (select3flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 3) {
                        if (select4flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 4) {
                        if (select5flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 5) {
                        if (select6flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 6) {
                        if (select7flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 7) {
                        if (select8flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                    if (i == 8) {
                        if (select9flag) {
                            selectedImage.add(imgIdList.get(i));
                        }
                    }
                }

                if (selectedImage.size() > 0)
                    deleteApi();
            }
        });

    }

    private void deleteApi() {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.deleteImagetask(selectedImage.toString().substring(1, selectedImage.toString().length() - 1).replaceAll("\\s", "")
                , "delete");

    }

    private void deselectall() {
        if (imgIdList.size() >= 1) {
            select1flag = false;
            select1.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 2) {
            select2flag = false;
            select2.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 3) {
            select3flag = false;
            select3.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 4) {
            select4flag = false;
            select4.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 5) {
            select5flag = false;
            select5.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 6) {
            select6flag = false;
            select6.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 7) {
            select7flag = false;
            select7.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 8) {
            select8flag = false;
            select8.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
        if (imgIdList.size() >= 9) {
            select9flag = false;
            select9.setImageDrawable(getResources().getDrawable(R.drawable.select));
        }
    }

    private void selectall() {
        if (imgIdList.size() >= 1) {
            select1flag = true;
            select1.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 2) {
            select2flag = true;
            select2.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 3) {
            select3flag = true;
            select3.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 4) {
            select4flag = true;
            select4.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 5) {
            select5flag = true;
            select5.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 6) {
            select6flag = true;
            select6.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 7) {
            select7flag = true;
            select7.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 8) {
            select8flag = true;
            select8.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
        if (imgIdList.size() >= 9) {
            select9flag = true;
            select9.setImageDrawable(getResources().getDrawable(R.drawable.select_dark));
        }
    }

    private void updateInfo() {
        getselectedCheckBox();
        CommonAsyncTask ca = new CommonAsyncTask(UpdateProfile.this, this);

        ca.updateProfiletask(etName.getText().toString(), AppPreference.getimgPreference(UpdateProfile.this, Constants.USERID),
                selectedHealthProblem.toString().substring(1, selectedHealthProblem.toString().length() - 1).replaceAll("\\s", ""),
                selectedworkOutInterest.toString().substring(1, selectedworkOutInterest.toString().length() - 1).replaceAll("\\s", ""),
                selectedworkOutLocation.toString().substring(1, selectedworkOutLocation.toString().length() - 1).replaceAll("\\s", ""),
                etCurrentWait.getText().toString(), etTargetWait.getText().toString(), etBmi.getText().toString(), etBodyFat.getText().toString(),
                tvAdditionalInfo.getText().toString(), tvAboutUs.getText().toString(), speciality.getText().toString(), tv_city.getText().toString(),
                other_health.getText().toString(), other_workout_location.getText().toString(), other_workoutinterest.getText().toString(),
                "update");
    }

    private void getselectedCheckBox() {
        ArrayList<String> healthproblem = new ArrayList<>();
        ArrayList<String> workoutinterest = new ArrayList<>();
        ArrayList<String> workoutlocation = new ArrayList<>();

        for (int i = 0; i < ll_healthConditionProblem1.getChildCount(); i++) {
            View child = ll_healthConditionProblem1.getChildAt(i);
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isChecked()) {
                healthproblem.add(checkBox.getText().toString());
            }
        }
        for (int i = 0; i < ll_healthConditionProblem2.getChildCount(); i++) {
            View child = ll_healthConditionProblem2.getChildAt(i);
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isChecked()) {
                healthproblem.add(checkBox.getText().toString());
            }
        }
        for (int i = 0; i < ll_workOutInterest1.getChildCount(); i++) {
            View child = ll_workOutInterest1.getChildAt(i);
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isChecked()) {
                workoutinterest.add(checkBox.getText().toString());
            }
        }
        for (int i = 0; i < ll_workOutInterest2.getChildCount(); i++) {
            View child = ll_workOutInterest2.getChildAt(i);
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isChecked()) {
                workoutinterest.add(checkBox.getText().toString());
            }
        }
        for (int i = 0; i < ll_workOutLocationpreference1.getChildCount(); i++) {
            View child = ll_workOutLocationpreference1.getChildAt(i);
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isChecked()) {
                workoutlocation.add(checkBox.getText().toString());
            }
        }
        for (int i = 0; i < ll_workOutLocationpreference2.getChildCount(); i++) {
            View child = ll_workOutLocationpreference2.getChildAt(i);
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isChecked()) {
                workoutlocation.add(checkBox.getText().toString());
            }
        }


        for (int i = 0; i < helthproblemList.size(); i++) {
            WorkChildModel model = helthproblemList.get(i);
            for (String str : healthproblem) {
                if (str.equals(model.getTitle())) {
                    selectedHealthProblem.add(model.getId());
                }
            }
        }
        for (int i = 0; i < workOutInterestList.size(); i++) {
            WorkChildModel model = workOutInterestList.get(i);

            for (String str : workoutinterest) {
                if (str.equals(model.getTitle())) {
                    selectedworkOutInterest.add(model.getId());
                }
            }
        }
        for (int i = 0; i < workOutLocation.size(); i++) {
            WorkChildModel model = workOutLocation.get(i);

            for (String str : workoutlocation) {
                if (str.equals(model.getTitle())) {
                    selectedworkOutLocation.add(model.getId());
                }
            }
        }

        if(other_health.getText().toString().length()>0){
            selectedHealthProblem.add("8");
        }
        if(other_workoutinterest.getText().toString().length()>0){
            selectedworkOutInterest.add("11");
        }
        if(other_workout_location.getText().toString().length()>0){
            selectedworkOutLocation.add("9");
        }


        AppPreference.setPreference(this, otherheath, other_health.getText().toString());
        AppPreference.setPreference(this, otherinterest, other_workoutinterest.getText().toString());
        AppPreference.setPreference(this, otherlocation, other_workout_location.getText().toString());

    }

    private void checkimagepermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(UpdateProfile.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(UpdateProfile.this, PERMISSIONS, PERMISSIONS_REQUEST_STORAGE);
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateProfile.this);
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

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = FileProvider.getUriForFile(UpdateProfile.this, UpdateProfile.this.getPackageName() + ".provider", mFileTemp);
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

    private void upload(File file, String type) {
        String url;
        HashMap<String, Object> hm = new HashMap<>();
        if (type.equals("video")) {
            url = Constants.BASE_URL + "upload_video";
            hm.put("video", file);
            hm.put("user_id", getPreference(UpdateProfile.this, Constants.USERID));
        } else {
            if(picId==11){
                url = Constants.BASE_URL + "client/update_profile";
                hm.put("image", file);
                hm.put("id", AppPreference.getPreference(this, Constants.USERID));
            }else {
                url = Constants.BASE_URL + "upload_image";
                hm.put("image", file);
                hm.put("index", "" + picId);
                hm.put("user_id", getPreference(UpdateProfile.this, Constants.USERID));
                if (imgIdList.size() > picId) {
                    hm.put("image_id", "" + imgIdList.get(picId - 1));
                }
            }
        }


        ProgressDialog pd = new ProgressDialog(UpdateProfile.this);
        pd.setMessage("Loading ...");
        pd.setCancelable(false);
        AQuery aq = new AQuery(UpdateProfile.this);

        aq.progress(pd).ajax(url, hm, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONObject obj = new JSONObject(object);
                        if (!obj.getBoolean("error")) {
                            if(picId!=11) {
                                JSONObject data = obj.getJSONObject("data");
                                JSONArray jarray = data.getJSONArray("results");
                                imgIdList.clear();
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject imgobj = jarray.getJSONObject(i);
                                    imgIdList.add(String.valueOf(imgobj.getInt("id")));
                                }
                            }else{
                                JSONObject data = obj.getJSONObject("data");
                                JSONObject result = data.getJSONObject("results");
                                JSONObject client = result.getJSONObject("client");
                                AppPreference.setPreference(UpdateProfile.this, Constants.PROFILEPIC, client.getString("image"));
                            }
                            Alerts.okAlert(UpdateProfile.this,"Image updated successfully","");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_FILE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(UpdateProfile.this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        Intent intent = new Intent(UpdateProfile.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
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
                        InputStream inputStream = UpdateProfile.this.getContentResolver().openInputStream(data.getData());
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
                        String path = data.getStringExtra(CropImage.IMAGE_PATH);
                        if (path == null) {
                            return;
                        }
                        uri = Uri.parse(path);
                        Bitmap bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());

                        if (uri == null) {
                            uri = GetImageContent.getImageUri(bitmap, UpdateProfile.this);
                        }
                        File file = new File(GetImageContent.
                                getRealPathFromURI(uri, UpdateProfile.this));

                        if (file != null) {
                            if (picId == 11)
                                profilePic.setImageBitmap(bitmap);
                            if (picId == 1)
                                capture1.setImageBitmap(bitmap);
                            if (picId == 2)
                                capture2.setImageBitmap(bitmap);
                            if (picId == 3)
                                capture3.setImageBitmap(bitmap);
                            if (picId == 4)
                                capture4.setImageBitmap(bitmap);
                            if (picId == 5)
                                capture5.setImageBitmap(bitmap);
                            if (picId == 6)
                                capture6.setImageBitmap(bitmap);
                            if (picId == 7)
                                capture7.setImageBitmap(bitmap);
                            if (picId == 8)
                                capture8.setImageBitmap(bitmap);
                            if (picId == 9)
                                capture9.setImageBitmap(bitmap);

                            upload(file, "");

                        } else
                            Toast.makeText(UpdateProfile.this, "File Not selected properly", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void emptybox() {
        if (select1flag)
            capture1.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select2flag)
            capture2.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select3flag)
            capture3.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select4flag)
            capture1.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select5flag)
            capture5.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select6flag)
            capture6.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select7flag)
            capture7.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select8flag)
            capture8.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
        if (select9flag)
            capture9.setImageDrawable(getResources().getDrawable(R.drawable.addimage_icon));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getResponse(Object response, String service) {
        if (service.equals("delete")) {
            CommonStatus parentModel = (CommonStatus) response;
            if (!parentModel.isError()) {
                emptybox();
                deselectall();
                selectallflag = false;
                select_deselect.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                refreshimageList();
            }
            Toast.makeText(this, parentModel.getmessage(), Toast.LENGTH_SHORT).show();

        } else if (service.equals("healthmaster")) {
            CommonStatusResultObj parentModel = (CommonStatusResultObj) response;

            if (!parentModel.isError()) {
                DataModelResultObj dataModel1 = parentModel.getData();
                WorkOutResult dataModel = dataModel1.getResults();

                helthproblemList = dataModel.getHealth_problems();
                workOutInterestList = dataModel.getWorkout_interests();
                workOutLocation = dataModel.getWorkout_locations();

                String[] locList = getPreference(UpdateProfile.this, Constants.WORKOUTLOCATIONLIST).split(",");
                String[] interestList = getPreference(UpdateProfile.this, Constants.WORKOUTPROGRAMLIST).split(",");
                String[] healthList = getPreference(UpdateProfile.this, Constants.HEATHPROBLEM).split(",");
                String[] imgList = getPreference(UpdateProfile.this, Constants.IMGList).split(",");
                String[] IdList = getPreference(UpdateProfile.this, Constants.IMGIdList).split(",");
                imgIdList = new ArrayList<>(Arrays.asList(IdList));
                if (imgIdList.get(0).equals("")) {
                    imgIdList = new ArrayList<>();
                }
                setImage(imgList);

                for (int i = 0; i < helthproblemList.size(); i++) {
                    WorkChildModel model = helthproblemList.get(i);
                    CheckBox cb = new CheckBox(UpdateProfile.this);
                    cb.setText(model.getTitle());
                    cb.setTextColor(UpdateProfile.this.getResources().getColor(R.color.WHITE));
                    cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.WHITE)));
                    checkBoxChecked(cb, model.getTitle().replaceAll("\\s", ""), healthList);
                        if (i % 2 == 0)
                            ll_healthConditionProblem1.addView(cb);
                        else
                            ll_healthConditionProblem2.addView(cb);
                }
                for (int i = 0; i < workOutInterestList.size(); i++) {
                    WorkChildModel model = workOutInterestList.get(i);
                    CheckBox cb = new CheckBox(UpdateProfile.this);
                    cb.setText(model.getTitle());
                    cb.setTextColor(UpdateProfile.this.getResources().getColor(R.color.WHITE));
                    cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.WHITE)));
                    checkBoxChecked(cb, model.getTitle().replaceAll("\\s", ""), interestList);
                        if (i % 2 == 0)
                            ll_workOutInterest1.addView(cb);
                        else
                            ll_workOutInterest2.addView(cb);

                }
                for (int i = 0; i < workOutLocation.size(); i++) {
                    WorkChildModel model = workOutLocation.get(i);
                    CheckBox cb = new CheckBox(UpdateProfile.this);
                    cb.setText(model.getTitle());
                    cb.setTextColor(UpdateProfile.this.getResources().getColor(R.color.WHITE));
                    cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.WHITE)));
                    checkBoxChecked(cb, model.getTitle().replaceAll("\\s", ""), locList);

                        if (i % 2 == 0)
                            ll_workOutLocationpreference1.addView(cb);
                        else
                            ll_workOutLocationpreference2.addView(cb);

                }
            } else {
                Alerts.okAlert(UpdateProfile.this, parentModel.getMessage(), "");
            }
        } else {
            CommonStatusResultObj parentModel = (CommonStatusResultObj) response;
            if (!parentModel.isError()) {
                AppPreference.setPreference(UpdateProfile.this, Constants.PROFILEUPDATED, "1");
               // Toast.makeText(getApplicationContext(), parentModel.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } else
                Alerts.okAlert(UpdateProfile.this, parentModel.getMessage(), "");
        }
    }

    private void refreshimageList() {
        for (String str : selectedImage) {
            for (int i = 0; i < imgIdList.size(); i++) {
                if (str.equals(imgIdList.get(i))) {
                    if(imgIdList.size()==1)
                        imgIdList.clear();
                    else
                        imgIdList.remove(i);
                }
            }
        }
    }

    private void setImage(String[] imageList) {
        for (int i = 0; i < imageList.length; i++) {
            String imgmodel = imageList[i];
            if (!imgmodel.equals("")) {
                String imageid = imgmodel;


                if (i == 0) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture1);
                }
                if (i == 1) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture2);
                }
                if (i == 2) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture3);
                }
                if (i == 3) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture4);
                }
                if (i == 4) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture5);
                }
                if (i == 5) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture6);
                }
                if (i == 6) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture7);
                }
                if (i == 7) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture8);
                }
                if (i == 8) {
                    Picasso.with(UpdateProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture9);
                }
            }
        }
    }

    private void checkBoxChecked(CheckBox cb, String title, String[] locList) {
        for (int i = 0; i < locList.length; i++) {
            if (title.trim().equalsIgnoreCase(locList[i])) {
                cb.setChecked(true);
            }
        }
    }
}
