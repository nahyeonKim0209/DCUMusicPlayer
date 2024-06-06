package kr.ac.cu.moai.dcumusicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button btnPlayPause;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String mp3file = intent.getStringExtra("mp3");

        ImageView ivCover = findViewById(R.id.ivCover);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDuration = findViewById(R.id.tvDuration);
        TextView tvArtist = findViewById(R.id.tvArtist);

        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            retriever.setDataSource(mp3file);
            byte[] b = retriever.getEmbeddedPicture();
            Bitmap cover = BitmapFactory.decodeByteArray(b, 0, b.length);
            ivCover.setImageBitmap(cover);

            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            tvTitle.setText(title);

            tvDuration.setText(ListViewMP3Adapter.getDuration(retriever));

            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            tvArtist.setText(artist);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(mp3file);
            mediaPlayer.prepare();
        }catch(IOException e){
            e.printStackTrace();
        }

        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnPlayPause.setOnClickListener(v -> {
            if(isPlaying){
                mediaPlayer.pause();
                btnPlayPause.setText(R.string.play);
            }else{
                mediaPlayer.start();
                btnPlayPause.setText(R.string.pause);
            }
            isPlaying = !isPlaying;
        });

    }

    @Override
    protected void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}