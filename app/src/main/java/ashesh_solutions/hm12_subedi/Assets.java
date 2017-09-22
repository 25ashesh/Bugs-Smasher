/*Ashesh Subedi
  L20398950
  COSC 5340 Android Programming (Online)
  Summer 2016
  Homework #12
*/
package ashesh_solutions.hm12_subedi;

/**
 * Created by Ashesh on 8/3/2016.
 */
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.graphics.Bitmap;

public class Assets {
    // Global variables
    public static MediaPlayer mp;

    static Bitmap background;
    static Bitmap statusbar;
    static Bitmap grass;
    static Bitmap roach;
    static Bitmap roach_moved;
    static Bitmap roach_dead;
    static Bitmap lifes;
    static Bitmap finishscreen;

    //Game States
    enum GameState{
        GettingReady,
        Starting,
        Running,
        GameEnding,
        GameOver,
    }

    static GameState state;
    static float gameTimer;
    static int livesLeft;
    static int score, HighScore;
    static SoundPool sp;
    static int sound_getready;
    static int sound_squish;
    static int sound_squish1;
    static int sound_squish2;
    static int sound_thump;
    static int sound_background;
    static int sound_highscore;

    //static Bug[] bbug;//array of bugs;
    static Bug bug,bug1,bug2;//single bug
}