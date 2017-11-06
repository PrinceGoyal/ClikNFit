package com.cliknfit.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.cliknfit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        init();
    }
    private void init() {
        videoView = (VideoView) findViewById(R.id.info_video);
        TextView skip = (TextView) findViewById(R.id.skip);
        String path = "android.resource://" + getPackageName() + "/"
                + R.raw.fitness;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                mp.setLooping(true);
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

}
