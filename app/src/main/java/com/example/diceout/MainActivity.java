package com.example.diceout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Field to hold the result text
    TextView rollResult;

    // Field to hold the score
    int score;

    // Field to hold random number generator
    Random rand;

    // Fields to hold the die value
    int die1, die2, die3;

    // ArrayList to hold all three dice values
    ArrayList<Integer> dice;

    // ArrayList to hold all three dice images
    ArrayList<ImageView> diceImageViews;

    // Field to hold the score text
    TextView scoreText;

    // Filed to hold the number of attempts left
    TextView attemptText;

    // Filed to hold number of tosses with 0 score
    int tosses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                rollDice(view);
            }
        });

        // Set initial score
        score = 0;

        // Set initial tosses
        tosses = 3;

        // Create greeting
        Toast.makeText(getApplicationContext(),"Welcome to DiceOut!",Toast.LENGTH_SHORT).show();

        // Link instances to widgets in the activity view
        rollResult = (TextView) findViewById(R.id.rollResult);
        scoreText = (TextView) findViewById(R.id.scoreText);
        attemptText = (TextView) findViewById(R.id.attemptText);

        // Initialize the random number generator
        rand = new Random();

        // Create ArrayList Container for the dice values
        dice = new ArrayList<Integer>();

        ImageView die1Image = (ImageView) findViewById(R.id.die1Image);
        ImageView die2Image = (ImageView) findViewById(R.id.die2Image);
        ImageView die3Image = (ImageView) findViewById(R.id.die3Image);

        diceImageViews = new ArrayList<ImageView>();
        diceImageViews.add(die1Image);
        diceImageViews.add(die2Image);
        diceImageViews.add(die3Image);
    }

    public void rollDice(View v){
        rollResult.setText("Clicked!");

        // Roll dice
        die1 = rand.nextInt(6) + 1;
        die2 = rand.nextInt(6) + 1;
        die3 = rand.nextInt(6) + 1;

        // Set dice values into an ArrayList
        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);

        if (tosses > 0) {
            for (int dieOfSet = 0; dieOfSet < 3; dieOfSet++){
                String imageName = "die_" + dice.get(dieOfSet) + ".png";
                try {
                    InputStream stream = getAssets().open(imageName);
                    Drawable d = Drawable.createFromStream(stream,null);
                    diceImageViews.get(dieOfSet).setImageDrawable(d);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            // Build message with the result
            String msg;

            if (die1 == die2 && die1 == die3) {
                // Triples
                int scoreDelta = die1 * 100;
                msg = "You rolled a triple " + die1 + "! You score " + scoreDelta + " points!";
                score += scoreDelta;
            } else if (die1 == die2 || die1 == die3 || die2 == die3 ) {
                // Doubles
                msg = "You rolled doubles for 50 points!";
                score += 50;
            } else {
                msg = "You didn't score this roll. Try again!";
                tosses--;
                attemptText.setText("You have " + tosses + " attempts left");

                if (tosses == 0) {
                    gameOver();
                }
            }

            // Update the app with the result message
            rollResult.setText(msg);
            scoreText.setText("Score : " + score);

        } else {
            gameOver();
        }

    }

    public void gameOver(){
        rollResult.setText("GAME OVER!");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You scored " + score)
                .setTitle("GAME OVER");

        builder.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
