/*Ashesh Subedi
  L20398950
  COSC 5340 Android Programming (Online)
  Summer 2016
  Homework #12
*/
package ashesh_solutions.hm12_subedi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.preference.PreferenceManager;


public class initial extends AppCompatActivity implements View.OnClickListener{
    private Button highscorebutton,playbutton;
    private Handler mHandler = new Handler();
    SoundPool sp;
    int sound_getready;
    int num_sounds_loaded;
    boolean sounds_loaded;
    boolean quit;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        quit = false;

        highscorebutton = (Button) findViewById(R.id.btnHighScore);
        playbutton = (Button) findViewById(R.id.btnPlay);
        highscorebutton.setOnClickListener(this);
        playbutton.setOnClickListener(this);

        num_sounds_loaded = 0;
        sounds_loaded = false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool sounds = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        }

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int mySoundId, int status) {
                num_sounds_loaded++;
                if (num_sounds_loaded == 1)
                    sounds_loaded = true;
            }
        });

        sound_getready = sp.load(this, R.raw.getready, 1);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnHighScore:
                startActivity(new Intent(this,PrefsActivity.class));
                break;
            case R.id.btnPlay:
                //Load Game
                //Toast.makeText(this,"This is clicked", Toast.LENGTH_SHORT).show();
                if(sounds_loaded) {
                    sp.play(sound_getready, 1, 1, 0, 0, 1);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!quit)
                                StartGame();
                        }
                    },3000);

                }
                break;
        }
    }
    private void StartGame(){
        Intent gameintent = new Intent(initial.this,MainActivity.class);
        startActivity(gameintent);
        finish();
    }

    @Override
    protected void onResume() {

        super.onResume();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Assets.HighScore=prefs.getInt("my_high_score",0);
    }


    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close Application?")
                .setMessage("Confirm to exit the application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
