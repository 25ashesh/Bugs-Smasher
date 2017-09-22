/*Ashesh Subedi
  L20398950
  COSC 5340 Android Programming (Online)
  Summer 2016
  Homework #12
*/
package ashesh_solutions.hm12_subedi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * Created by Ashesh on 8/5/2016.
 */
public class MainThread extends Thread {

    private SurfaceHolder holder;
    private Handler handler;  // for running code in the UI thread
    private boolean isRunning = false;
    Context context;
    Paint paint;
    int touchx, touchy;    //x,y of touch event
    int tempscore,temphighscore=Assets.HighScore;
    boolean touched;      //true if touch occured
    boolean data_initialized;
    private static final Object lock = new Object();
    private Handler mHandler = new Handler();


    public MainThread(SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        this.context = context;
        handler = new Handler();
        data_initialized = false;
        touched = false;

    }

    public void setRunning(boolean b) {
        isRunning = b;
    }

    //set the touch event x,y location and flag indicating a touch has occured
    public void setXY(int x, int y) {
        synchronized (lock) {
            touchx = x;
            touchy = y;
            this.touched = true;
        }

    }

    @Override
    public void run() {
        while (isRunning) {
            //lock the canvas before drawing
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                //perform drawing operations on the canvas
                render(canvas);

                //after drawing, unlock the canvas and display it
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    // Loads graphics, etc. used in game
    private void loadData(Canvas canvas) {
        Bitmap bmp;
        int newWidth, newHeight;
        float scaleFactor;

        // Create a paint object for drawing vector graphics
        paint = new Paint();

        // Load score bar
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.statusbar);
        // Compute size of bitmap needed (suppose want height = 10% of screen height)
        newHeight = (int) (canvas.getHeight() * 0.07f);
        // Scale it to a new size
        Assets.statusbar = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), newHeight, false);
        // Delete the original
        bmp = null;

        // Load food bar
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass);
        // Compute size of bitmap needed (suppose want height = 10% of screen height)
        newHeight = (int) (canvas.getHeight() * 0.1f);
        // Scale it to a new size
        Assets.grass = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), newHeight, false);
        // Delete the original
        bmp = null;

        // Load roach1
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int) (canvas.getWidth() * 0.2f);
        // What was the scaling factor to get to this?
        scaleFactor = (float) newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int) (bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.roach = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;


        // Load roach2
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_moved);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int) (canvas.getWidth() * 0.2f);
        // What was the scaling factor to get to this?
        scaleFactor = (float) newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int) (bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.roach_moved = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;
        // Load the other bitmaps similarly
        // ...

        // Load roach3 (dead bug)
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_dead);
        // Compute size of bitmap needed (suppose want width = 20% of screen width)
        newWidth = (int) (canvas.getWidth() * 0.2f);
        // What was the scaling factor to get to this?
        scaleFactor = (float) newWidth / bmp.getWidth();
        // Compute the new height
        newHeight = (int) (bmp.getHeight() * scaleFactor);
        // Scale it to a new size
        Assets.roach_dead = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        // Delete the original
        bmp = null;

        // Create a bug
        //Bug[] abc = new Bug[4];
        //abc=Assets.bug;
        Assets.bug = new Bug();
        Assets.bug1 = new Bug();
        Assets.bug2 = new Bug();
        //Bug b = new Bug();
        //for(int a=0;a<4;a++){
        //   Assets.bug[a]=b;
        //}
        //Assets.bug = new Bug[3];
        //for (int h = 0; h < 3; ++h) { //for start
        //    Assets.bug[h] = new Bug();}
    }

    // Load specific background screen
    private void loadBackground(Canvas canvas, int resId) {
        // Load background
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.background = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), canvas.getHeight(), false);
        // Delete the original
        bmp = null;
    }

    // Load lifes
    private void loadLifes(Canvas canvas, int resId) {
        // Load background
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.lifes = Bitmap.createBitmap(bmp);
        // Delete the original
        bmp = null;
    }

    // Load finishscreen
    private void loadfinishsceen(Canvas canvas, int resId) {
        // Load background
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.finishscreen = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), canvas.getHeight(), false);
        // Delete the original
        bmp = null;
    }


    private void render(Canvas canvas) {
        int i, x, y;
        Bitmap bmp;
        int newWidth, newHeight;
        float scaleFactor;


        if (!data_initialized) {
            loadData(canvas);
            data_initialized = true;
        }

        switch (Assets.state) {
            case GettingReady:
                loadBackground(canvas, R.drawable.wood);
                // Draw the background screen

                canvas.drawBitmap(Assets.background, 0, 0, null);
                // Play a sound effect
                //** Assets.sp.play(Assets.sound_getready, 1, 1, 1, 0, 1);
                // Start a timer
                Assets.gameTimer = System.nanoTime() / 1000000000f;
                // Go to next state
                Assets.state = Assets.GameState.Starting;
                break;
            case Starting:
                // Draw the background screen
                canvas.drawBitmap(Assets.background, 0, 0, null);
                // Has 3 seconds elapsed?
                float currentTime = System.nanoTime() / 1000000000f;
                if (currentTime - Assets.gameTimer >= 3)
                    // Goto next state
                    Assets.state = Assets.GameState.Running;
                break;
            case Running:
                // Draw the background screen
                canvas.drawBitmap(Assets.background, 0, 0, null);
                // Draw the score bar at top of screen
                canvas.drawBitmap(Assets.statusbar, 0, 0, null);

                paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.LEFT);
                Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
                paint.setTypeface(typeface);
                paint.setTextSize(40);
                //String score = Integer.toString(Assets.score);
                canvas.drawText("Your Score : " + Assets.score, 0, canvas.getHeight() * 0.055f, paint);

                // Draw the foodbar at bottom of screen
                canvas.drawBitmap(Assets.grass, 0, canvas.getHeight() - Assets.grass.getHeight(), null);
                // Draw one circle for each life at top right corner of screen
                // Let circle radius be 5% of width of screen
                int radius = (int) (canvas.getWidth() * 0.04f);
                int spacing = 4; // spacing in between circles
                x = canvas.getWidth() - radius - spacing;    // coordinates for rightmost circle to draw
                y = radius + spacing;
                for (i = 0; i < Assets.livesLeft; i++) {
                    loadLifes(canvas, R.drawable.lifes);
                    canvas.drawBitmap(Assets.lifes, x, y, null);
                    x -= (radius * 2 + spacing);
                }
                //****************

                if (touched) {
                    // Set touch flag to false since we are processing this touch now
                    touched = false;
                    // See if this touch killed a bug
                    boolean bugKilled = Assets.bug.touched(canvas, touchx, touchy);
                    boolean bugKilled1 = Assets.bug1.touched(canvas, touchx, touchy);
                    boolean bugKilled2 = Assets.bug2.touched(canvas, touchx, touchy);
                    if (bugKilled || bugKilled1 || bugKilled2) {

                        if (bugKilled) {
                            Assets.sp.play(Assets.sound_squish, 1, 1, 1, 0, 1);
                            Assets.score += 1;
                        }

                        if (bugKilled1) {
                            Assets.sp.play(Assets.sound_squish1, 1, 1, 1, 0, 1);
                            Assets.score += 1;
                        }

                        if (bugKilled2) {
                            Assets.sp.play(Assets.sound_squish2, 1, 1, 1, 0, 1);
                            Assets.score += 1;
                        }

                    } else
                        Assets.sp.play(Assets.sound_thump, 1, 1, 1, 0, 1);
                    tempscore=Assets.score;

                }

                // Draw dead bugs on screen
                Assets.bug.drawDead(canvas);
                Assets.bug1.drawDead(canvas);
                Assets.bug2.drawDead(canvas);
                // Move bugs on screen
                Assets.bug.move(canvas);
                Assets.bug1.move(canvas);
                Assets.bug2.move(canvas);
                // Bring a dead bug to life?
                Assets.bug.birth(canvas);
                Assets.bug1.birth(canvas);
                Assets.bug2.birth(canvas);
                //****************

                // ADD MORE CODE HERE TO PLAY GAME

                if (Assets.score > Assets.HighScore) {
                    Assets.HighScore = Assets.score;
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(context, "New High Score !!! " + Assets.score + " point(s)!", Toast.LENGTH_SHORT).show();
                            Assets.sp.play(Assets.sound_highscore, 1, 1, 1, 0, 1);

                        }
                    });

                }

                // Are no lives left?
                if (Assets.livesLeft == 0)
                    // Goto next state
                    Assets.state = Assets.GameState.GameEnding;
                break;
            case GameEnding:

                // Show a game over message
                handler.post(new Runnable() {
                    public void run() {
                        if (tempscore > temphighscore) {
                            Toast.makeText(context, "Game Over! New High Score!!! " + Assets.score + " point(s)!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Game Over! You scored " + Assets.score + " point(s)!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                // Goto next state
                Assets.state = Assets.GameState.GameOver;
                break;
            case GameOver:
                // Fill the entire canvas' bitmap with 'black'
                //**loadfinishsceen(canvas, R.drawable.finishscreen);
                // Draw the background screen

                //**canvas.drawBitmap(Assets.finishscreen, 0, 0, null);
                //canvas.drawColor(Color.BLACK);

                //MainActivity a = new MainActivity();
                //a.onExit();
                DrawLastScreen(canvas);
                break;
        }
    }

    public void DrawLastScreen(Canvas canvas) {

        loadfinishsceen(canvas, R.drawable.finishscreen);
        canvas.drawBitmap(Assets.finishscreen, 0, 0, null);
        Finito();

    }

    public void Finito() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Please press the Back Button to Exit!", Toast.LENGTH_SHORT).show();
            }
        }, 3500);
    }

}
