/*Ashesh Subedi
  L20398950
  COSC 5340 Android Programming (Online)
  Summer 2016
  Homework #12
*/
package ashesh_solutions.hm12_subedi;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

/**
 * Created by Ashesh on 8/5/2016.
 */
public class MainView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder = null;
    Context context;
    private MainThread t = null;

    @SuppressWarnings("deprecation")
    public MainView (Context context) {
        super(context);
        // Save context
        this.context = context;
        // Retrieve the SurfaceHolder instance associated with this SurfaceView.
        holder = getHolder();
        // Initialize variables
        this.context = context;
        Assets.state = Assets.GameState.GettingReady;
        Assets.livesLeft = 3;
        // Load the sound effects
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Assets.sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            Assets.sp = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();

        }
        Assets.sound_getready = Assets.sp.load(context, R.raw.getready, 1);
        Assets.sound_squish = Assets.sp.load(context, R.raw.squish, 1);
        Assets.sound_squish1 = Assets.sp.load(context, R.raw.squish1, 1);
        Assets.sound_squish2 = Assets.sp.load(context, R.raw.squish2, 1);
        Assets.sound_background = Assets.sp.load(context, R.raw.background, 1);
        Assets.sound_thump = Assets.sp.load(context, R.raw.thump, 1);
        Assets.sound_highscore=Assets.sp.load(context,R.raw.highscore,1);

        // Specify this class (MainView) as the class that implements the three callback methods required by SurfaceHolder.Callback.
        holder.addCallback(this);
    }

    public void Pause ()
    {
        /*//write out high score to shared preferences
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("my_high_score",Assets.HighScore);
        editor.commit();

        //Retrieve the high score from the shared preferences
        Assets.HighScore=prefs.getInt("my_high_score",0);
        */
        //super.onPause();

        t.setRunning(false);
        while (true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void Resume ()
    {
        //super.onResume();
/*
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Assets.HighScore=prefs.getInt("my_high_score",0);
        */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x, y;
        int action = event.getAction();
        x = event.getX();
        y = event.getY();
//		if (action==MotionEvent.ACTION_MOVE) {
//		}
//		if (action==MotionEvent.ACTION_UP){
//		}
        if (action == MotionEvent.ACTION_DOWN) {
            if (t != null)
                t.setXY ((int)x, (int)y);
        }
        return true; // to indicate we have handled this event
    }

    @Override
    public void surfaceCreated (SurfaceHolder holder) {
        // Create and start a drawing thread whose Runnable object is defined by this class (MainView)
        if (t == null) {
            t = new MainThread(holder, context);
            t.setRunning(true);
            t.start();
            setFocusable(true); // make sure we get events
        }
    }
    // Neither of these two methods are used in this example, however, their definitions are required because SurfaceHolder.Callback was implemented
    @Override public void surfaceChanged(SurfaceHolder sh, int f, int w, int h) {}
    @Override public void surfaceDestroyed(SurfaceHolder sh) {}
}
