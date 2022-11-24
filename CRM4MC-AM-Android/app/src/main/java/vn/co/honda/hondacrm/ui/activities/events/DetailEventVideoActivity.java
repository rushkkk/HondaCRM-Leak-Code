package vn.co.honda.hondacrm.ui.activities.events;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import vn.co.honda.hondacrm.R;

public class DetailEventVideoActivity extends AppCompatActivity {

    public static final String url = "https://dangducminh.com/honda_civic.mp4";
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        videoView =  findViewById(R.id.videoView);

        // Tạo bộ điều khiển
        if (mediaController == null) {
            mediaController = new MediaController(DetailEventVideoActivity.this);
            // Neo vị trí của MediaController với VideoView.
            mediaController.setAnchorView(videoView);
            // Sét đặt bộ điều khiển cho VideoView.
            videoView.setMediaController(mediaController);
        }
        try {
            videoView.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
            videoView.start();
            videoView.setMediaController(mediaController);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoView.requestFocus();
        // Sự kiện khi file video sẵn sàng để chơi.

    }
    // Khi bạn xoay điện thoại, phương thức này sẽ được gọi
    // nó lưu trữ lại ví trí file video đang chơi.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Lưu lại vị trí file video đang chơi.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }
    // Sau khi điện thoại xoay chiều xong. Phương thức này được gọi,
    // bạn cần tái tạo lại ví trí file nhạc đang chơi.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Lấy lại ví trí video đã chơi.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }
}