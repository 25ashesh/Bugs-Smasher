/*Ashesh Subedi
  L20398950
  COSC 5340 Android Programming (Online)
  Summer 2016
  Homework #12
*/
package ashesh_solutions.hm12_subedi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
MainView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        v = new MainView(this);

        setContentView(v);

    }

    @Override
    protected void onPause () {


        super.onPause();
        if (Assets.mp != null) {
            Assets.mp.pause();
            Assets.mp.release();
            Assets.mp = null;
        }
        v.Pause();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("my_high_score",Assets.HighScore);
        editor.commit();

        //Retrieve the high score from the shared preferences
        Assets.HighScore=prefs.getInt("my_high_score",0);
    }

    @Override
    protected void onResume () {
        super.onResume();


        Assets.mp=null;
        Assets.mp= MediaPlayer.create(this,R.raw.background);
        if(Assets.mp!=null){
            Assets.mp.setLooping(true);
            Assets.mp.start();
        }

        v.Resume();
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(false);
        startActivity(new Intent(this, initial.class));
        finish();

    }
}
