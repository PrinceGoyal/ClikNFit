package com.cliknfit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cliknfit.R;
import com.cliknfit.pojo.ImagesModel;
import com.cliknfit.util.Constants;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;


public class ZoomImage extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    private ArrayList<ImagesModel> imagearray;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        imagearray = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);

        initViews();

    }

    private void initViews() {
        SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.imagearray);

        for (int i = 0; i < imagearray.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(Constants.PICBASE_URL + "" + imagearray.get(i).getImage())
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setCurrentPosition(position);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(null);
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);

        //mDemoSlider.addOnPageChangeListener(this);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
