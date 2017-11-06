package com.cliknfit.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.ImagesModel;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.pojo.WorkChildModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.CustomVideoPlayer;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.tag;


public class TrainerProfile extends AppCompatActivity implements ApiResponse {

    private TextView tv_requesttrainer, trainerName, speciality, city, motto, fav, review, rating, points, aboutUs, aditionalInfo;
    private TagView tag_group_workoutProgram, tag_group_workoutlocation;

    private ImageView expandedImageView;
    private int mShortAnimationDuration;
    private boolean imagezoomflag=false;
    private Rect startBounds;
    private float startScaleFinal;
    private Animator mCurrentAnimator;



    ImageView capture1;
    ImageView capture2;
    ImageView capture3;
    ImageView capture4;
    ImageView capture5;
    ImageView capture6;
    ImageView capture7;
    ImageView capture8;
    ImageView capture9;
    private ImageView profilePic;
    private String trainerId = "";
    private String profileimage = "";
    private String gymAddress="";
    private VideoView videoView;    private LinearLayout imgLayout2,imgLayout1,imgLayout3;
    private ScrollView scrollView;
    private ImageView videoplaceholder;
    private ArrayList<ImagesModel> imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trainer_profile);
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
            initViews();
            getProfileData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        }



    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        Picasso.with(this)
                .load(Constants.PICBASE_URL + "" + profileimage)
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



    private void getProfileData() {
        CommonAsyncTask ca = new CommonAsyncTask(TrainerProfile.this, this);

        ca.profiletrainerTask(trainerId, "profile"
        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (imagezoomflag) {
                backfromimage();
                imagezoomflag = false;
            } else {
                finish();
                super.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (imagezoomflag) {
            backfromimage();
            imagezoomflag = false;
        } else {
            finish();
            super.onBackPressed();
        }
    }

    private void initViews() {
    
            trainerId = getIntent().getStringExtra("id");

        scrollView=(ScrollView)findViewById(R.id.scrollView);
        expandedImageView = (ImageView) findViewById(R.id.fullimage);
        profilePic = (ImageView) findViewById(R.id.img_profile_pic);
        videoplaceholder = (ImageView) findViewById(R.id.videoplaceholder);
        tv_requesttrainer = (TextView) findViewById(R.id.tv_requesttrainer);
        trainerName = (TextView) findViewById(R.id.tv_trainerName);
        speciality = (TextView) findViewById(R.id.tv_speciality);
        city = (TextView) findViewById(R.id.tv_city);
        motto = (TextView) findViewById(R.id.tv_motto);
        fav = (TextView) findViewById(R.id.tv_fav);
        review = (TextView) findViewById(R.id.tv_review);
        rating = (TextView) findViewById(R.id.tv_rating);
        points = (TextView) findViewById(R.id.tv_points);
        aboutUs = (TextView) findViewById(R.id.tv_aboutUs);
        aditionalInfo = (TextView) findViewById(R.id.tv_aditionalInfo);

        tag_group_workoutProgram = (TagView) findViewById(R.id.tag_group_workoutProgram);
        tag_group_workoutlocation = (TagView) findViewById(R.id.tag_group_workoutLocation);


        imgLayout1 = (LinearLayout) findViewById(R.id.imgLayout1);
        imgLayout2 = (LinearLayout) findViewById(R.id.imgLayout2);
        imgLayout3 = (LinearLayout) findViewById(R.id.imgLayout3);
        capture1 = (ImageView) findViewById(R.id.capture1);
        capture2 = (ImageView) findViewById(R.id.capture2);
        capture3 = (ImageView) findViewById(R.id.capture3);
        capture4 = (ImageView) findViewById(R.id.capture4);
        capture5 = (ImageView) findViewById(R.id.capture5);
        capture6 = (ImageView) findViewById(R.id.capture6);
        capture7 = (ImageView) findViewById(R.id.capture7);
        capture8 = (ImageView) findViewById(R.id.capture8);
        capture9 = (ImageView) findViewById(R.id.capture9);

        if(getIntent().hasExtra("book")){
            tv_requesttrainer.setVisibility(View.GONE);
        }


        click();
    }

    private void click() {
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(profilePic);
            }
        });


        tv_requesttrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrainerProfile.this, BookingInfo.class)
                        .putExtra("trainer_id", trainerId)
                        .putExtra("image", profileimage)
                        .putExtra("gymaddress", gymAddress)
                        .putExtra("name", trainerName.getText().toString())
                );
            }
        });
    }

    private void imageclick() {
        if (imageList != null) {
            capture1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 1) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 0)
                        );
                    }
                }
            });
            capture2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 2) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 1)
                        );
                    }

                }
            });
            capture3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 3) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 2)
                        );
                    }

                }
            });
            capture4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 4) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 3)
                        );
                    }

                }
            });
            capture5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 5) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 4)
                        );
                    }
                }
            });
            capture6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 6) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 5)
                        );
                    }
                }
            });
            capture7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 7) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 6)
                        );
                    }
                }
            });
            capture8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 8) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 7)
                        );
                    }
                }
            });
            capture9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 9) {
                        startActivity(new Intent(TrainerProfile.this, ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 8)
                        );
                    }
                }
            });
        }
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj commonStatusResultObj = (CommonStatusResultObj) response;
        //  Toast.makeText(TrainerProfile.this, "", Toast.LENGTH_SHORT).show();

        //commonStatusResultObj.getData().getResults().getBmi_history();
      
        imageList = commonStatusResultObj.getData().getResults().getImages();
        imageclick();
        ArrayList<String> imgList = new ArrayList<>();
        ArrayList<String> imgIdList = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {
            ImagesModel imgmodel = imageList.get(i);
            if (!imgmodel.getImage().equals("")) {
                String imageid = Constants.PICBASE_URL + "" + imgmodel.getImage();
                imgList.add(imageid);
                imgIdList.add(imgmodel.getId());

                if (i == 0) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture1);
                    capture1.setVisibility(View.VISIBLE);
                    imgLayout1.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture2);
                    capture2.setVisibility(View.VISIBLE);

                }
                if (i == 2) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture3);
                    capture3.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture4);
                    capture4.setVisibility(View.VISIBLE);
                    imgLayout2.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture5);
                    capture5.setVisibility(View.VISIBLE);
                }
                if (i == 5) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture6);
                    capture6.setVisibility(View.VISIBLE);
                }
                if (i == 6) {
                    imgLayout3.setVisibility(View.VISIBLE);
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture7);
                    capture7.setVisibility(View.VISIBLE);
                }
                if (i == 7) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture8);
                    capture8.setVisibility(View.VISIBLE);
                }
                if (i == 8) {
                    Picasso.with(TrainerProfile.this)
                            .load(imageid)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(capture9);
                    capture9.setVisibility(View.VISIBLE);
                }
            }
        }
        AppPreference.setPreference(TrainerProfile.this, Constants.IMGList, imgList.toString().substring(1, imgList.toString().length() - 1).replaceAll("\\s", ""));
        AppPreference.setPreference(TrainerProfile.this, Constants.IMGIdList, imgIdList.toString().substring(1, imgIdList.toString().length() - 1).replaceAll("\\s", ""));

        UserModel userModel = commonStatusResultObj.getData().getResults().getTrainer();

        gymAddress = userModel.getGym_address();



        profileimage = userModel.getImage();

        Picasso.with(TrainerProfile.this)
                .load(Constants.PICBASE_URL + "" + profileimage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(profilePic);


        trainerName.setText(userModel.getName());
        city.setText(userModel.getCity());

        rating.setText("" + commonStatusResultObj.getData().getResults().getRating());
        points.setText("" + commonStatusResultObj.getData().getResults().getPoint());
        AppPreference.setPreference(getApplicationContext(), Constants.RATE, "" + commonStatusResultObj.getData().getResults().getRating());
        AppPreference.setPreference(getApplicationContext(), Constants.POINTS, "" + commonStatusResultObj.getData().getResults().getPoint());
        //  tvWeight.setText(userModel.get());
        // tvBmi .setText(userModel.get);
        //  tvFat.setText(userModel.get);
        aboutUs.setText(userModel.getAbout_me());
        aditionalInfo.setText(userModel.getAdditional_info());


        speciality.setText(String.valueOf(userModel.getSpeciality()));
        motto.setText(userModel.getMotto());
        // tvFav.setText(userModel.getfa());
        //  tvResetText(userModel.getMotto());


        ArrayList<WorkChildModel> workoutInterestList = commonStatusResultObj.getData().getResults().getWorkout_programs();
        ArrayList<WorkChildModel> workoutLocationList = commonStatusResultObj.getData().getResults().getWorkout_locations();

        review.setText("Booked "+userModel.getBooked_times()+" Times");

        ArrayList<String> selectedworkOutInterest = new ArrayList<>();
        ArrayList<String> selectedworkOutLocation = new ArrayList<>();
        if (workoutInterestList.size() > 0) {
            for (WorkChildModel model : workoutInterestList) {
                setTag(tag_group_workoutProgram, model.getTitle());
                selectedworkOutInterest.add(model.getTitle());
            }
        }
        if(tag_group_workoutlocation==null){

            tag_group_workoutlocation = (TagView) findViewById(R.id.tag_group_workoutLocation);
        }

        if (workoutLocationList.size() > 0) {
            for (WorkChildModel model : workoutLocationList) {
                setTag(tag_group_workoutlocation, model.getTitle());
                selectedworkOutLocation.add(model.getTitle());
            }
        }
        if (commonStatusResultObj.getData().getResults().getVideo() == null) {
        } else {
            videoplaceholder.setVisibility(View.GONE);
            CustomVideoPlayer videoLayout = (CustomVideoPlayer)findViewById(R.id.videoview);
            videoLayout.setActivity(this,videoplaceholder);

            Uri videoUri = Uri.parse(Constants.PICBASE_URL + "" + commonStatusResultObj.getData().getResults().getVideo().getVideo());
            try {
                videoLayout.setVideoURI(videoUri);

                videoLayout.seekTo(5);
                videoLayout.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void setTag(TagView tag_group_workoutProgram, String title) {
        Tag tag1 = new Tag(title);
        tag1.layoutColor = getResources().getColor(R.color.WHITE);
        tag1.tagTextColor = getResources().getColor(R.color.BACKGROUND_COLOR);
        tag1.radius = 2f;

        tag_group_workoutProgram.addTag(tag1);
    }
}
