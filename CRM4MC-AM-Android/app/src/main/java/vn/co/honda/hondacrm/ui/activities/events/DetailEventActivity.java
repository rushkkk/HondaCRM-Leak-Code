package vn.co.honda.hondacrm.ui.activities.events;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;

public class DetailEventActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    public static final String url = "https://dangducminh.com/honda_civic.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        setTitleHeader(getString(R.string.label_title_event));

        //surfaceView = findViewById(R.id.img_detail_event_bottom);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(DetailEventActivity.this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(DetailEventActivity.this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            Log.d("hhh", "surfaceCreated: ");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        realeaseMediaPlayer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realeaseMediaPlayer();
    }

    private void realeaseMediaPlayer() {
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}
