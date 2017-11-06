package com.cliknfit.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.activity.UpdateProfile;
import com.cliknfit.activity.ZoomImage;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.BmIModel;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.ImagesModel;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.pojo.WorkChildModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.cliknfit.R.id.img;
import static com.cliknfit.R.id.norecord;
import static com.cliknfit.R.id.other_workoutinterest;
import static com.cliknfit.util.Constants.HEATHPROBLEM;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.i;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements ApiResponse {


    private ImageView expandedImageView;
    private int mShortAnimationDuration;
    private boolean imagezoomflag=false;
    private Rect startBounds;
    private float startScaleFinal;
    private Animator mCurrentAnimator;

    int max = 0;
    TextView tvName, tv_myaddress;
    TextView tvAge;
    TextView tvCity;
    TextView tvRating;
    TextView tvPoints;
    TextView tvWeight;
    TextView tvBmi;
    TextView tvFat;
    GraphView graph;
    ImageView capture1;
    ImageView capture2;
    ImageView capture3;
    ImageView capture4;
    ImageView capture5;
    ImageView capture6;
    ImageView capture7;
    ImageView capture8;
    ImageView capture9;
    TextView tvAditionalInfo;
    private View view;
    private TextView aboutUs;
    private LinearLayout weightLayout, bmiLayout, fatLayout;
    private BroadcastReceiver broadcastReceiver;
    private TagView tag_group_heathproblemProgram, tag_group_workoutProgram, tag_group_workoutlocation;
    private ImageView profilePic;
    private LinearLayout imgLayout2, imgLayout1, imgLayout3;
    private ArrayList<ImagesModel> imageList;
    private String imageUrl;
    private TextView tvReview;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        initviews();
        getProfileData();
        return view;
    }


    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        Picasso.with(getActivity())
                .load(Constants.PICBASE_URL +imageUrl)
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
        view.findViewById(R.id.activity_profile)
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





    @SuppressLint("NewApi")
    private void initviews() {
        expandedImageView = (ImageView)view. findViewById(R.id.fullimage);
        profilePic = (ImageView) view.findViewById(R.id.img_profile_pic);
        tvName = (TextView) view.findViewById(R.id.tv_Name);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        tvCity = (TextView) view.findViewById(R.id.tv_city);
        tvRating = (TextView) view.findViewById(R.id.tv_rating);
        tvPoints = (TextView) view.findViewById(R.id.tv_points);
        tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        tvBmi = (TextView) view.findViewById(R.id.tv_bmi);
        tvFat = (TextView) view.findViewById(R.id.tv_fat);
        tvReview = (TextView) view.findViewById(R.id.tv_review);

        tv_myaddress = (TextView) view.findViewById(R.id.tv_myaddress);

        weightLayout = (LinearLayout) view.findViewById(R.id.weightLayout);
        bmiLayout = (LinearLayout) view.findViewById(R.id.bmiLayout);
        fatLayout = (LinearLayout) view.findViewById(R.id.fatLayout);

        tag_group_heathproblemProgram = (TagView) view.findViewById(R.id.tag_group_heathproblemProgram);
        tag_group_workoutProgram = (TagView) view.findViewById(R.id.tag_group_workoutProgram);
        tag_group_workoutlocation = (TagView) view.findViewById(R.id.tag_group_workoutlocation);

        imgLayout1 = (LinearLayout) view.findViewById(R.id.imgLayout1);
        imgLayout2 = (LinearLayout) view.findViewById(R.id.imgLayout2);
        imgLayout3 = (LinearLayout) view.findViewById(R.id.imgLayout3);

        capture1 = (ImageView) view.findViewById(R.id.capture1);
        capture2 = (ImageView) view.findViewById(R.id.capture2);
        capture3 = (ImageView) view.findViewById(R.id.capture3);
        capture4 = (ImageView) view.findViewById(R.id.capture4);
        capture5 = (ImageView) view.findViewById(R.id.capture5);
        capture6 = (ImageView) view.findViewById(R.id.capture6);
        capture7 = (ImageView) view.findViewById(R.id.capture7);
        capture8 = (ImageView) view.findViewById(R.id.capture8);
        capture9 = (ImageView) view.findViewById(R.id.capture9);

        aboutUs = (TextView) view.findViewById(R.id.tv_aboutUs);
        tvAditionalInfo = (TextView) view.findViewById(R.id.tv_aditionalInfo);
        aboutUs.setMovementMethod(new ScrollingMovementMethod());
        aboutUs.setNestedScrollingEnabled(true);


    }

    private void getProfileData() {
        CommonAsyncTask ca = new CommonAsyncTask(getActivity(), this);
        ca.profileTask(AppPreference.getPreference(getActivity(), Constants.USERID), "profile"
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        String update = AppPreference.getPreference(getActivity(), Constants.PROFILEUPDATED);
        if (update.equals("1")) {
           goneimg();
            getProfileData();
            tag_group_heathproblemProgram.removeAll();
            tag_group_workoutlocation.removeAll();
            tag_group_workoutProgram.removeAll();
            AppPreference.setPreference(getActivity(), Constants.PROFILEUPDATED, "0");
       }
        // registerSmsReciever();
    }

    private void goneimg() {
        imgLayout1.setVisibility(View.GONE);
        imgLayout2.setVisibility(View.GONE);
        imgLayout3.setVisibility(View.GONE);

        capture1.setVisibility(View.INVISIBLE);
        capture2.setVisibility(View.INVISIBLE);
        capture3.setVisibility(View.INVISIBLE);
        capture4.setVisibility(View.INVISIBLE);
        capture5.setVisibility(View.INVISIBLE);
        capture6.setVisibility(View.INVISIBLE);
        capture7.setVisibility(View.INVISIBLE);
        capture8.setVisibility(View.INVISIBLE);
        capture9.setVisibility(View.INVISIBLE);
    }


    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj commonStatusResultObj = (CommonStatusResultObj) response;
        //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        final ArrayList<BmIModel> bmiList = commonStatusResultObj.getData().getResults().getBmi_history();
        if (imageList != null) {
            imageList.clear();
        }
        imageList = commonStatusResultObj.getData().getResults().getImages();

        click();
        /*set graph*/

        DataPoint[] datapoint = new DataPoint[bmiList.size()];
        if(bmiList.size()>0) {
            BmIModel bmimodel = bmiList.get(0);

            max = Integer.valueOf(bmimodel.getTarget_weight());
            AppPreference.setPreference(getActivity(), Constants.BMI, bmimodel.getBmi());
            AppPreference.setPreference(getActivity(), Constants.CURRENTWEIGHT, bmimodel.getCurrent_weight());
            AppPreference.setPreference(getActivity(), Constants.TARGETWEIGHT, bmimodel.getTarget_weight());
            AppPreference.setPreference(getActivity(), Constants.FAT, bmimodel.getBody_fat());
            tvWeight.setText(bmimodel.getCurrent_weight());
            tvBmi.setText(bmimodel.getBmi());
            tvFat.setText(bmimodel.getBody_fat());
        }


      /*  for (int i = bmiList.size(); i > 0; i--) {
            BmIModel bmimodel = bmiList.get(i - 1);
            if (i == bmiList.size() - 1) {
                max = Integer.valueOf(bmimodel.getTarget_weight());
                AppPreference.setPreference(getActivity(), Constants.BMI, bmimodel.getCurrent_weight());
                AppPreference.setPreference(getActivity(), Constants.CURRENTWEIGHT, bmimodel.getBmi());
                AppPreference.setPreference(getActivity(), Constants.TARGETWEIGHT, bmimodel.getTarget_weight());
                AppPreference.setPreference(getActivity(), Constants.FAT, bmimodel.getBody_fat());
                tvWeight.setText(bmimodel.getCurrent_weight());
                tvBmi.setText(bmimodel.getBmi());
                tvFat.setText(bmimodel.getBody_fat());
            }*/
         //   datapoint[i - 1] = new DataPoint(Integer.valueOf(bmimodel.getTarget_weight()), Integer.valueOf(bmimodel.getCurrent_weight()));
       // }
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(datapoint);
        setGraph(series, max);*/

       /* weightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPoint[] datapoint = new DataPoint[bmiList.size()];

                for (int i = bmiList.size(); i > 0; i--) {
                    BmIModel bmimodel = bmiList.get(i - 1);
                    datapoint[i - 1] = new DataPoint(Integer.valueOf(bmimodel.getTarget_weight()), Integer.valueOf(bmimodel.getCurrent_weight()));
                }
                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(datapoint);
                setGraph(series1, max);
            }
        });

        bmiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPoint[] datapoint = new DataPoint[bmiList.size()];

                for (int i = bmiList.size(); i > 0; i--) {
                    BmIModel bmimodel = bmiList.get(i - 1);
                    datapoint[i - 1] = new DataPoint(Integer.valueOf(bmimodel.getTarget_weight()), Integer.valueOf(bmimodel.getBmi()));
                }
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(datapoint);
                setGraph(series2, max);
            }
        });

        fatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPoint[] datapoint = new DataPoint[bmiList.size()];

                for (int i = bmiList.size(); i > 0; i--) {
                    BmIModel bmimodel = bmiList.get(i - 1);
                    datapoint[i - 1] = new DataPoint(Integer.valueOf(bmimodel.getTarget_weight()), Integer.valueOf(bmimodel.getBody_fat()));
                }
                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(datapoint);
                setGraph(series3, max);
            }
        });
*/
        ArrayList<String> imgList = new ArrayList<>();
        ArrayList<String> imgIdList = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            ImagesModel imgmodel = imageList.get(i);
            if (!imgmodel.getImage().equals("")) {
                String imageid = Constants.PICBASE_URL + "" + imgmodel.getImage();
                imgList.add(imageid);
                imgIdList.add(imgmodel.getId());

                if (i == 0) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture1);
                    capture1.setVisibility(View.VISIBLE);
                    imgLayout1.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture2);
                    capture2.setVisibility(View.VISIBLE);
                    imgLayout1.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture3);
                    capture3.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture4);
                    capture4.setVisibility(View.VISIBLE);
                    imgLayout2.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture5);
                    capture5.setVisibility(View.VISIBLE);
                }
                if (i == 5) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture6);
                    capture6.setVisibility(View.VISIBLE);
                }
                if (i == 6) {
                    imgLayout3.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture7);
                    capture7.setVisibility(View.VISIBLE);
                }
                if (i == 7) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture8);
                    capture8.setVisibility(View.VISIBLE);
                }
                if (i == 8) {
                    Picasso.with(getActivity())
                            .load(imageid)
                            .into(capture9);
                    capture9.setVisibility(View.VISIBLE);
                }
            }
        }
        AppPreference.setPreference(getActivity(), Constants.IMGList, imgList.toString().substring(1, imgList.toString().length() - 1).replaceAll("\\s", ""));
        AppPreference.setPreference(getActivity(), Constants.IMGIdList, imgIdList.toString().substring(1, imgIdList.toString().length() - 1).replaceAll("\\s", ""));

        UserModel userModel = commonStatusResultObj.getData().getResults().getClient();
        AppPreference.setUser(getActivity(), userModel);

        tv_myaddress.setText(userModel.getGym_address());
        tvName.setText(userModel.getName());
        tvAge.setText("" + userModel.getAge());
        tvCity.setText(userModel.getCity());

        tvRating.setText("" + commonStatusResultObj.getData().getResults().getRating());
        tvPoints.setText("" + commonStatusResultObj.getData().getResults().getPoint());
        AppPreference.setPreference(getActivity(), Constants.RATE, "" + commonStatusResultObj.getData().getResults().getRating());
        AppPreference.setPreference(getActivity(), Constants.POINTS, "" + commonStatusResultObj.getData().getResults().getPoint());

        aboutUs.setText(userModel.getAbout_me());
        tvAditionalInfo.setText(userModel.getAdditional_info());
        imageUrl=userModel.getImage();
        Picasso.with(getActivity())
                .load(Constants.PICBASE_URL + userModel.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(profilePic);

        ArrayList<WorkChildModel> healthproblemList = commonStatusResultObj.getData().getResults().getHealth_problems();
        ArrayList<WorkChildModel> workoutInterestList = commonStatusResultObj.getData().getResults().getWorkout_interests();
        ArrayList<WorkChildModel> workoutLocationList = commonStatusResultObj.getData().getResults().getWorkout_locations();


        ArrayList<String> selectedHealthProblem = new ArrayList<>();
        ArrayList<String> selectedworkOutInterest = new ArrayList<>();
        ArrayList<String> selectedworkOutLocation = new ArrayList<>();
        String otherheath="",otherinterest="",otherlocation="";

        tvReview.setText("Booked "+userModel.getBooked_times()+" Times");
        for (WorkChildModel model : healthproblemList) {
            setTag(tag_group_heathproblemProgram, model.getTitle());
            if (model.getId().equals("8")) {
                otherheath=model.getTitle();
            } else
            selectedHealthProblem.add(model.getTitle());
        }
        for (WorkChildModel model : workoutInterestList) {
            setTag(tag_group_workoutProgram, model.getTitle());

            if (model.getId().equals("11")) {
                otherinterest=model.getTitle();
            } else
                selectedworkOutInterest.add(model.getTitle());
        }
        for (WorkChildModel model : workoutLocationList) {
            setTag(tag_group_workoutlocation, model.getTitle());
            if (model.getId().equals("9")) {
                otherlocation=model.getTitle();
            } else
                selectedworkOutLocation.add(model.getTitle());
        }

        AppPreference.setPreference(getActivity(), Constants.otherheath, otherheath);
        AppPreference.setPreference(getActivity(), Constants.otherinterest, otherinterest);
        AppPreference.setPreference(getActivity(), Constants.otherlocation, otherlocation);

        AppPreference.setPreference(getActivity(), Constants.WORKOUTPROGRAMLIST, selectedworkOutInterest.toString().substring(1, selectedworkOutInterest.toString().length() - 1).replaceAll("\\s", ""));
        AppPreference.setPreference(getActivity(), Constants.WORKOUTLOCATIONLIST, selectedworkOutLocation.toString().substring(1, selectedworkOutLocation.toString().length() - 1).replaceAll("\\s", ""));
        AppPreference.setPreference(getActivity(), HEATHPROBLEM, selectedHealthProblem.toString().substring(1, selectedHealthProblem.toString().length() - 1).replaceAll("\\s", ""));


    }

    private void setGraph(LineGraphSeries<DataPoint> series, int max) {
        graph = (GraphView) view.findViewById(R.id.graph);
        series.setColor(getResources().getColor(R.color.weightcolor));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(4);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        series.setAnimated(true);
        series.setDrawAsPath(true);


        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR));

        graph.addSeries(series);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(max);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    private void setTag(TagView tag_group_workoutProgram, String title) {
        Tag tag1 = new Tag(title);
        tag1.layoutColor = getResources().getColor(R.color.WHITE);
        tag1.tagTextColor = getResources().getColor(R.color.BACKGROUND_COLOR);
        tag1.radius = 6f;

        tag_group_workoutProgram.addTag(tag1);
    }

    private void click() {
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(profilePic);
            }
        });



        if (imageList != null) {
            capture1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageList.size() >= 1) {
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
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
                        startActivity(new Intent(getActivity(), ZoomImage.class)
                                .putExtra("images", imageList)
                                .putExtra("position", 8)
                        );
                    }
                }
            });
        }
    }


}
