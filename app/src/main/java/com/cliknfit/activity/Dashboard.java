package com.cliknfit.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterNavigation;
import com.cliknfit.adapter.AdapterNewsFeed;
import com.cliknfit.fragment.BookingHistory;
import com.cliknfit.fragment.Events;
import com.cliknfit.fragment.MySession;
import com.cliknfit.fragment.MyWallet;
import com.cliknfit.fragment.NewsFeeds;
import com.cliknfit.fragment.Profile;
import com.cliknfit.fragment.Rewards;
import com.cliknfit.fragment.Setting;
import com.cliknfit.fragment.TopTrainner;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.DataModel;
import com.cliknfit.pojo.DataTrainerInfo;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.pojo.NavItem;
import com.cliknfit.pojo.QNAModel;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.ConnectionDetector;
import com.cliknfit.util.Constants;
import com.cliknfit.util.GeocodingLocation;
import com.cliknfit.util.RoundedImageView;
import com.cliknfit.util.Validations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paypal.android.sdk.onetouch.core.metadata.ah;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity
        implements Adapterinterface, GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ApiResponse {

    private FrameLayout content_frame;
    private LinearLayout eventLayout;
    private LinearLayout trainerLayout;
    private LinearLayout newsLayout, homeLayout;
    private LinearLayout rewardsLayout;
    private RecyclerView navigationList;
    private ArrayList<NavItem> navigationDataList;
    private AdapterNavigation nav_adapter;
    private DrawerLayout drawer;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 320;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 521;
    private GoogleMap mMap;
    private EditText et_search;
    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_CONTACTS};
    private static final int PERMISSION_ALL = 2;
    private int homecount = 0;
    private double latitute, longtitute;
    private LinearLayout searchlayout;
    private Menu menu;
    private Toolbar toolbar;
    private View v;
    private TextView home_txt, news_txt;
    private ImageView news_icon, home_icon;
    private FrameLayout currentloc;
    private boolean homeVisibleFlag = false;
    private RelativeLayout trainerInfo;
    private TextView viewProfile;
    private TextView trainer_name;
    private TextView speciality;
    private TextView motto;
    private TextView tv_rating;
    private ImageView trainer_profile_pic;
    private ArrayList<UserModel> trainerList;
    private TextView price, searchDone;
    private ImageView userloc;
    private RelativeLayout searchFullLayout;
    private EditText et_search2;
    private TextView book_now;
    private RelativeLayout newsfeedlayout;
    private ImageView ad_image;
    private TextView ad_title;
    private ArrayList<QNAModel> dataList;
    private AdapterNewsFeed adapterNewsFeed;
    private RecyclerView mRecyclerViewNewsFeed;
    private int newsFeedId = 0;
    private ImageView checknoti;
    private BroadcastReceiver broadcastReceiver;
    private String addesc,adimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BOOK A TRAINER");

        setupDrawer(toolbar);
        initailizemap();
        setgoogleapi();
        searchlocation();
        footer();
        newsfeedlayout();
        addclick();

    }

    private void addclick() {
        ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, NewsFeedDetail.class)
                        .putExtra("title",ad_title.getText().toString())
                        .putExtra("image",adimg)
                        .putExtra("description",addesc));
            }
        });
    }

    private void newsfeedlayout() {
        newsfeedlayout = (RelativeLayout) findViewById(R.id.newsfeedlayout);
        ad_image = (ImageView) findViewById(R.id.ad_image);
        checknoti = (ImageView) findViewById(R.id.checknoti);
        ad_title = (TextView) findViewById(R.id.ad_title);
        mRecyclerViewNewsFeed = (RecyclerView) findViewById(R.id.mRecyclerViewNewsFeed);
        mRecyclerViewNewsFeed.setLayoutManager(new GridLayoutManager(this, 2));
        dataList = new ArrayList<>();
        adapterNewsFeed = new AdapterNewsFeed(this, dataList);
        mRecyclerViewNewsFeed.setAdapter(adapterNewsFeed);

        addNewsList();

    }

    private void addNewsList() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.newsFeedtask("newsfeed");
    }

    private void trainerList() {
        //trainers/index
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.gettrainertask(String.valueOf(latitute), String.valueOf(longtitute), "trainerlist");
    }


    private void footer() {
        userloc = (ImageView) findViewById(R.id.userloc);

        searchFullLayout = (RelativeLayout) findViewById(R.id.searchFullLayout);
        trainerInfo = (RelativeLayout) findViewById(R.id.trainerInfo);

        et_search2 = (EditText) findViewById(R.id.et_search2);
        searchDone = (TextView) findViewById(R.id.searchDone);
        viewProfile = (TextView) findViewById(R.id.viewPro);
        book_now = (TextView) findViewById(R.id.book_now);
        trainer_name = (TextView) findViewById(R.id.trainer_name);
        speciality = (TextView) findViewById(R.id.speciality);
        motto = (TextView) findViewById(R.id.motto);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        price = (TextView) findViewById(R.id.price);

        home_txt = (TextView) findViewById(R.id.home_txt);
        news_txt = (TextView) findViewById(R.id.news_txt);
        trainer_profile_pic = (ImageView) findViewById(R.id.trainer_profile_pic);
        news_icon = (ImageView) findViewById(R.id.news_icon);
        home_icon = (ImageView) findViewById(R.id.home_icon);

        news_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_news_feeds));

        currentloc = (FrameLayout) findViewById(R.id.currentloc);


        eventLayout = (LinearLayout) findViewById(R.id.eventLayout);
        rewardsLayout = (LinearLayout) findViewById(R.id.rewardsLayout);
        trainerLayout = (LinearLayout) findViewById(R.id.trainerLayout);
        newsLayout = (LinearLayout) findViewById(R.id.newsLayout);
        homeLayout = (LinearLayout) findViewById(R.id.homeLayout);


        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragments();
                homevisible();
            }
        });
        eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addfragment(new Events(), "EVENTS");
            }
        });
        rewardsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addfragment(new Rewards(), "REWARDS");
            }
        });
        trainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addfragment(new TopTrainner(), "TOP TRAINER");
            }
        });
        newsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNewsFeed();
                if (newsFeedId != 0 && dataList.size() > 0) {
                    if (newsFeedId == dataList.get(0).getId()) {
                        checknoti.setVisibility(View.GONE);
                    }
                }

                searchlayout.setVisibility(View.GONE);
                currentloc.setVisibility(View.GONE);
                homeVisibleFlag = false;
                newsfeedlayout.setVisibility(View.VISIBLE);
                setTitle("NEWSFEEDS");
                menu.findItem(R.id.search).setVisible(false);

            }
        });
        currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else
                    statusCheck();

            }
        });

        trainerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slideToTop(currentloc);
                slideToBottom(trainerInfo);
               /* trainerInfo.setVisibility(View.GONE);
                currentloc.setVisibility(View.VISIBLE);*/
            }
        });
        searchFullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFullLayout.setVisibility(View.GONE);
            }
        });
        searchDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations.isFieldNotEmpty(et_search2)) {
                    searchFullLayout.setVisibility(View.GONE);
                    hideSoftKeyboard();
                    startActivity(new Intent(Dashboard.this, TrainerList.class)
                            .putExtra("keyword", et_search2.getText().toString())
                            .putExtra("selectedAddress", et_search.getText().toString())
                    );
                }
            }
        });
        et_search2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Validations.isFieldNotEmpty(et_search2)) {
                        searchFullLayout.setVisibility(View.GONE);
                        hideSoftKeyboard();
                        startActivity(new Intent(Dashboard.this, TrainerList.class)
                                .putExtra("keyword", et_search2.getText().toString())
                                .putExtra("selectedAddress", et_search.getText().toString())
                        );
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void homevisible() {
        homeVisibleFlag = true;
        toolbar.getMenu().clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        menu.findItem(R.id.search).setVisible(true);
        setTitle("BOOK A TRAINER");
        searchlayout.setVisibility(View.VISIBLE);
        currentloc.setVisibility(View.VISIBLE);
        newsfeedlayout.setVisibility(View.GONE);
        selectedHome();
    }

    private void selectedHome() {
        home_icon.setImageDrawable(getResources().getDrawable(R.drawable.home_colored));
        news_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_news_feeds));
        home_txt.setTextColor(getResources().getColor(R.color.BUTTON_PINK_COLOR));
        news_txt.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR));
    }


    private void selectedNewsFeed() {
        news_icon.setImageDrawable(getResources().getDrawable(R.drawable.news_feed_color));
        home_icon.setImageDrawable(getResources().getDrawable(R.drawable.home));
        news_txt.setTextColor(getResources().getColor(R.color.BUTTON_PINK_COLOR));
        home_txt.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return true;
    }

    private void initailizemap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                currentloc.setVisibility(View.VISIBLE);
                trainerInfo.setVisibility(View.GONE);

                et_search2.setText("");
                searchFullLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.refresh:
                et_search2.setText("");
                searchFullLayout.setVisibility(View.GONE);
                if (!homeVisibleFlag) {
                    addNewsList();
                } else {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else
                        statusCheck();
                }
                break;
            case R.id.editprofile:
                startActivity(new Intent(this, UpdateProfile.class));
                break;
            case R.id.addwallet:
                startActivity(new Intent(this, PaymentMethods.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void searchlocation() {
        searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
        et_search = (EditText) findViewById(R.id.search);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(MapsActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                                    /*.setBoundsBias(mLastLocation)*/
                                    .build(Dashboard.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng lng = place.getLatLng();

                moveToLocation(lng.latitude, lng.longitude, 15);
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(lng,
                        Dashboard.this, new GeocoderHandler());

                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            et_search.setText(locationAddress);
            AppPreference.setPreference(Dashboard.this,Constants.CurrentAddress,locationAddress);
        }
    }

    private void moveToLocation(double latitude, double longitude, int zoom) {
        // mMap.addMarker(marker);
        Log.e("", "Error: Status = ");
        LatLng newLatLng = new LatLng(latitude, longitude);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(newLatLng);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, zoom));
        // Zoom in, animating the camera.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(newLatLng)      // Sets the center of the map to Mountain View
                .zoom(zoom)                   // Sets the zoom
                //.bearing(90)                // Sets the orientation of the camera to east
                .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void setgoogleapi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    // popout fragments
    public synchronized void popFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


    // add fragments
    public void addfragment(Fragment fragment, String tag) {
        newsfeedlayout.setVisibility(View.GONE);
        searchlayout.setVisibility(View.GONE);
        currentloc.setVisibility(View.GONE);
        trainerInfo.setVisibility(View.GONE);
        et_search2.setText("");
        searchFullLayout.setVisibility(View.GONE);
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment frag = fragmentManager.findFragmentByTag(tag);
            // if (frag == null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
         /*   transaction.setCustomAnimations(R.animator.slide_up,
                    R.animator.slide_down,
                    R.animator.slide_up,
                    R.animator.slide_down);*/
            transaction.add(R.id.content_frame, fragment);
            transaction.addToBackStack(null).commit();
            fragmentManager.executePendingTransactions();
            setTitle(tag);
            content_frame.setVisibility(View.VISIBLE);
            homeVisibleFlag = false;
            //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupDrawer(Toolbar toolbar) {
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationList = (RecyclerView) findViewById(R.id.navigationList);
        navigationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        navigationDataList = new ArrayList<>();
        nav_adapter = new AdapterNavigation(this, this, navigationDataList);
        navigationList.setAdapter(nav_adapter);
        addNavItems();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerFcmReciever();
        profiledata();
    }



    private void registerFcmReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cliknfit");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String mod = intent.getStringExtra("model");
               final ModelMySessionResults model=new ModelMySessionResults();
                try {
                    JSONObject json = new JSONObject(mod);
                    model.setIs_trainer_started(json.getInt("is_trainer_started"));
                    model.setIs_client_started(json.getInt("is_client_started"));
                    model.setStatus(json.getInt("status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (model.getIs_trainer_started()==0){
                    toolbar.getMenu().clear();
                    addfragment(new MySession(), "MY SESSION");
                }

            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }


    private void profiledata() {
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = mNavigationView.getHeaderView(0);
        ImageView banner = (ImageView) header.findViewById(R.id.banner);
        TextView user_name = (TextView) header.findViewById(R.id.user_name);
        ImageView mImageView = (ImageView) header.findViewById(R.id.img_profile_pic);

        user_name.setText(AppPreference.getPreference(this, Constants.NAME));
        Picasso.with(this).load(Constants.PICBASE_URL + AppPreference.getPreference(this, Constants.PROFILEPIC))
                .placeholder(R.drawable.placeholder)
                .into(banner);
        Picasso.with(this).load(Constants.PICBASE_URL + AppPreference.getPreference(this, Constants.PROFILEPIC))
                .placeholder(R.drawable.placeholder)
                .into(mImageView);

        setmarker(null, Constants.PICBASE_URL + AppPreference.getPreference(this, Constants.PROFILEPIC));
    }

    private void addNavItems() {
        navigationDataList.add(new NavItem("Home", R.drawable.nav_home));
        navigationDataList.add(new NavItem("My Session", R.drawable.ic_session));
        navigationDataList.add(new NavItem("Booking History", R.drawable.ic_booking_history));
        navigationDataList.add(new NavItem("My Profile", R.drawable.ic_profile));
        navigationDataList.add(new NavItem("My Wallet", R.drawable.ic_wallet));
        navigationDataList.add(new NavItem("Settings", R.drawable.ic_settings));
        navigationDataList.add(new NavItem("Logout", R.drawable.ic_logout));


        nav_adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //  super.onBackPressed();
        }
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (homeVisibleFlag) {
            super.onBackPressed();
            finish();
        } else if (count > 0) {
            content_frame.setVisibility(View.GONE);
            homevisible();
        } else
            homevisible();

    }


    @Override
    public void sendcallback(int position, String string) {
        if (string.equals("nav")) {
            if (position == 0) {
                popFragments();
                homevisible();
            } else if (position == 1) {
                toolbar.getMenu().clear();
                addfragment(new MySession(), "My SESSION");
            } else if (position == 2) {
                toolbar.getMenu().clear();
                addfragment(new BookingHistory(), "BOOKING HISTORY");
            } else if (position == 3) {
                toolbar.getMenu().clear();
                addfragment(new Profile(), "PROFILE");
                toolbar.getMenu().clear();
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.editprofile, menu);
            } else if (position == 4) {
                toolbar.getMenu().clear();
                addfragment(new MyWallet(), "MY WALLET");
                toolbar.getMenu().clear();
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.wallet_menu, menu);
            } else if (position == 5) {
                toolbar.getMenu().clear();
                addfragment(new Setting(), "SETTING");
            } else if (position == 6) {
                logoutAlert("Are you sure want to logout","Alert");
            }
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    public void logoutAlert(String message, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View dialog = LayoutInflater.from(this).inflate(R.layout.ok_alert_layout, null, false);
        TextView tvtitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        View canceldevider = dialog.findViewById(R.id.canceldevider);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        canceldevider.setVisibility(View.VISIBLE);
        tv_cancel.setVisibility(View.VISIBLE);

        tvtitle.setText(title);
        tvMessage.setText(message);
        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regid = AppPreference.getPreference(Dashboard.this, Constants.REGID);
                String deviceId = AppPreference.getPreference(Dashboard.this, Constants.DEVICEID);
                AppPreference.clearPreference(Dashboard.this);
                AppPreference.setPreference(Dashboard.this, Constants.REGID, regid);
                AppPreference.setPreference(Dashboard.this, Constants.DEVICEID, deviceId);
                startActivity(new Intent(Dashboard.this, Login.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                alertDialog2.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }
    @Override
    public void sendcallbackforpreference(int position, String string, String date, String time) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (homecount == 0) {
            statusCheck();
            homecount++;
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    Log.e("", "");
                    latitute = mLastLocation.getLatitude();
                    longtitute = mLastLocation.getLongitude();

                    LatLng lastloc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastloc, 15));

                    trainerList();

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_ACCESS_COARSE_LOCATION);
                }
            }
        }
    }

    public static Bitmap overlay(Bitmap firstImage, Bitmap secondImage) {
        Bitmap bmOverlay = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 32f, 10f, null);
        return bmOverlay;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
       //unregisterReceiver(broadcastReceiver);
        mGoogleApiClient.disconnect();
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(true);


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                // if (mainpage) {
                final LatLng centerlatLng = mMap.getCameraPosition().target;
                //  cameracout++;
                // if (cameracout > 2) {
                if (centerlatLng.latitude != 0.0) {

                    GeocodingLocation locationAddress = new GeocodingLocation();
                    locationAddress.getAddressFromLocation(centerlatLng,
                            Dashboard.this, new GeocoderHandler());

                }
            }
        });
        mMap.setOnMarkerClickListener(this);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < trainerList.size(); i++) {
            if (marker.equals(trainerList.get(i).getMarker())) {
                final UserModel trainerModel = trainerList.get(i);
                trainer_name.setText(trainerModel.getName());
                price.setText("$" + trainerModel.getPer_hr_rate() + "/hour");
                speciality.setText(trainerModel.getSpeciality());
                tv_rating.setText("" + trainerModel.getRating());
                motto.setText(trainerModel.getMotto());
                String imageid = Constants.PICBASE_URL + "" + trainerModel.getThumb_image();
                Picasso.with(Dashboard.this)
                        .load(imageid)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(trainer_profile_pic);


                slideToBottom(currentloc);
                slideToTop(trainerInfo);
                /*trainerInfo.setVisibility(View.VISIBLE);
                currentloc.setVisibility(View.GONE);*/


                book_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slideToTop(currentloc);
                        slideToBottom(trainerInfo);
                        startActivity(new Intent(Dashboard.this, BookingInfo.class)
                                .putExtra("trainer_id", trainerModel.getId())
                                .putExtra("image", trainerModel.getImage())
                                .putExtra("gymaddress", trainerModel.getGym_address())
                                .putExtra("name", trainerModel.getName())
                                .putExtra("selectedAddress", et_search.getText().toString())
                        );

                    }
                });

                viewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slideToTop(currentloc);
                        slideToBottom(trainerInfo);

                        startActivity(new Intent(Dashboard.this, TrainerProfile.class)
                                .putExtra("id", "" + trainerModel.getId())
                                .putExtra("selectedAddress", et_search.getText().toString())

                        );
                    }
                });


            }
        }
        return true;
    }

    @Override
    public void getResponse(Object response, String service) {
        if (service.equals("newsfeed")) {
            CommonStatus parentModel = (CommonStatus) response;
            if (!parentModel.isError()) {
                if (service.equals("newsfeed")) {
                    DataModel dataModel = parentModel.getData();
                    dataList.clear();
                    int count = 0;
                    for (QNAModel model : dataModel.getQna()) {
                        if (count == 0) {
                            AppPreference.setIntegerPreference(Dashboard.this, Constants.newsFeedId, model.getId());
                        }
                        count++;
                        if (model.getIs_banner() == 1) {
                            ad_title.setText(model.getTitle());
                            addesc=model.getDescription();
                            adimg = Constants.PICBASE_URL + "" + model.getImage();
                            Picasso.with(Dashboard.this)
                                    .load(adimg)
                                    .into(ad_image);
                        } else
                            dataList.add(model);
                    }
                    adapterNewsFeed.notifyDataSetChanged();
                }
            }
        }
        if (service.equals("trainerlist")) {
            mMap.clear();
            if (trainerList != null)
                if (trainerList.size() > 0)
                    trainerList.clear();
            DataTrainerInfo parentmodel = (DataTrainerInfo) response;
            if (!parentmodel.isError()) {
            if(parentmodel.getData().getOther()!=null) {
                if(parentmodel.getData().getOther().getLatest_newsfeed()!=null) {
                    newsFeedId = parentmodel.getData().getOther().getLatest_newsfeed().getId();
                    if (newsFeedId == AppPreference.getIntegerPreference(Dashboard.this, Constants.newsFeedId)) {
                        checknoti.setVisibility(View.GONE);
                    } else {
                        checknoti.setVisibility(View.VISIBLE);
                    }
                }
            }
                if (parentmodel.getData().getResults().size() > 0) {
                    trainerList = parentmodel.getData().getResults();
                    addhostpitalmarkeronmap();
                }
            }
        }
    }

    private void addhostpitalmarkeronmap() {
        mMap.clear();
        if (mMap != null) {
            for (int i = 0; i < trainerList.size(); i++) {

                Marker marker = mMap.addMarker(new MarkerOptions().position
                        (new LatLng(Double.parseDouble(trainerList.get(i).getLat())
                                , Double.parseDouble(trainerList.get(i).getLan())))
                );
                setmarker(marker, Constants.PICBASE_URL + trainerList.get(i).getThumb_image());
                /*.*/

                trainerList.get(i).setMarker(marker);
//}
                Log.e("add marker", "" + i);
            }
        }
    }

    private void setmarker(final Marker marker, String url) {
        Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.e("", "");
                Drawable dplaceholder;
                if (marker == null)
                    dplaceholder = getResources().getDrawable(R.drawable.centermarker);
                else
                    dplaceholder = getResources().getDrawable(R.drawable.marker_placeholder);

                Bitmap bitmapplaceholder = ((BitmapDrawable) dplaceholder).getBitmap();
                Bitmap trainerbitmap = RoundedImageView.getRoundedCroppedBitmap(bitmap, 155);

                if (marker == null) {
                    Log.d("", "");
                    userloc.setImageBitmap(overlay(bitmapplaceholder, trainerbitmap));
                } else {
                    Log.e("", "");
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(overlay(bitmapplaceholder, trainerbitmap)));
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Drawable dplaceholder;
                if (marker == null)
                    dplaceholder = getResources().getDrawable(R.drawable.centermarker);
                else
                    dplaceholder = getResources().getDrawable(R.drawable.marker_placeholder);

                Drawable dcircle = getResources().getDrawable(R.drawable.placeholder);
                Bitmap bitmapplaceholder = ((BitmapDrawable) dplaceholder).getBitmap();
                Bitmap bitmapcircle = ((BitmapDrawable) dcircle).getBitmap();

                Bitmap trainerbitmap = RoundedImageView.getRoundedCroppedBitmap(bitmapcircle, 155);
                if (marker == null) {
                    userloc.setImageBitmap(overlay(bitmapplaceholder, trainerbitmap));
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(overlay(bitmapplaceholder, trainerbitmap)));
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Drawable dplaceholder;
                if (marker == null)
                    dplaceholder = getResources().getDrawable(R.drawable.centermarker);
                else
                    dplaceholder = getResources().getDrawable(R.drawable.marker_placeholder);

                Drawable dcircle = getResources().getDrawable(R.drawable.placeholder);
                Bitmap bitmapplaceholder = ((BitmapDrawable) dplaceholder).getBitmap();
                Bitmap bitmapcircle = ((BitmapDrawable) dcircle).getBitmap();

                Bitmap trainerbitmap = RoundedImageView.getRoundedCroppedBitmap(bitmapcircle, 155);
                if (marker == null) {
                    Log.d("", "");
                    userloc.setImageBitmap(overlay(bitmapplaceholder, trainerbitmap));
                } else {
                    Log.d("", "");
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(overlay(bitmapplaceholder, trainerbitmap)));
                }
            }
        };
        Picasso.with(Dashboard.this)
                .load(url)
                .into(mTarget);

    }


    public void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight() + 500, 0);
        animate.setDuration(500);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public void slideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight() + 500);
        animate.setDuration(300);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
}
