package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.content.Intent;
import android.net.Uri;
import android.widget.VideoView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.activity.VideoActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.ActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivityView extends ActivityView<VideoActivity> {

    @BindView(R.id.video_tutorial)
    VideoView videoView;

    public VideoActivityView(VideoActivity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
    }

    public void init() {
        if (getActivity() == null) return;
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro);
        videoView.setVideoURI(uri);
        videoView.setOnCompletionListener(getActivity());
        videoView.start();
    }

    public void goToAuthActivity() {
        if (getActivity() == null) return;
        Intent i = new Intent(getActivity(), AuthActivity.class);
        getActivity().startActivity(i);
        getActivity().finish();
    }

    public void pauseVideo() {
        videoView.pause();
    }

    public void startVideo() {
        videoView.start();
    }
}
