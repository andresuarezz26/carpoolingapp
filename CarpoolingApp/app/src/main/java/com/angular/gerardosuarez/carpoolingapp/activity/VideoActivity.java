package com.angular.gerardosuarez.carpoolingapp.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.VideoView;

import com.angular.gerardosuarez.carpoolingapp.R;

public class VideoActivity extends BaseActivity implements MediaPlayer.OnCompletionListener {
    private VideoView videoview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();
    }

    private void init() {
        videoview = (VideoView) findViewById(R.id.video_tutorial);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        videoview.setVideoURI(uri);
        videoview.setOnCompletionListener(this);
        videoview.start();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Intent i = new Intent(this, AuthActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoview.start();
    }
}
