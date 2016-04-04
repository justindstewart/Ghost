package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String COMPUTER_WINS = "Computer Wins!";
    private static final String USER_WINS = "User Wins!";
    private static final String WORD_FRAG = "Word Fragment";
    private static final String GAME_STATUS = "Game Status";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        Log.d("Debug", "Computer Turn");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text = (TextView) findViewById(R.id.ghostText);
        // Do computer turn stuff then make it the user's turn again
        int length = text.getText().length();

        if(length >= dictionary.MIN_WORD_LENGTH && dictionary.isWord(text.getText().toString())) {
            label.setText(COMPUTER_WINS);
            Toast toast = Toast.makeText(this, COMPUTER_WINS, Toast.LENGTH_LONG);
            toast.show();
            Log.d("Debug", "Word is greater than 4 and is word.");
        } else if (length == 0) {
            Log.d("Debug", "Word is 0, select random");
            String s = dictionary.getGoodWordStartingWith(text.getText().toString());
            text.append(s.substring(0, 1));
        } else {
            Log.d("Debug", "Computer turn, getAnyWordStartingWith Call");
            String s = dictionary.getGoodWordStartingWith(text.getText().toString());
            Log.d("Debug", "Computer turn, returned " + s);
            if (s == null) {
                label.setText(COMPUTER_WINS);
                Toast toast = Toast.makeText(this, COMPUTER_WINS, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Log.d("Debug", "Computer turn, append");
                text.append(s.substring(length, length + 1));
            }
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            TextView text = (TextView) findViewById(R.id.ghostText);
            char unicodeChar = (char)event.getUnicodeChar();
            Log.d("Debug", "" + unicodeChar);
            String s = text.getText().toString() + unicodeChar;
            if(dictionary.isWord(s)) {
                TextView label = (TextView) findViewById(R.id.gameStatus);
                label.setText("Is a word!");
            }
            Log.d("Debug", s);
            text.setText(s);
            computerTurn();
            return true;
        }
        else {
            return super.onKeyUp(keyCode, event);
        }
    }

    public void challengeWord (View view){
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(text.getText().length() >= dictionary.MIN_WORD_LENGTH && dictionary.isWord(text.getText().toString())) {
            label.setText(USER_WINS);
            Toast toast = Toast.makeText(this, USER_WINS, Toast.LENGTH_LONG);
            toast.show();
        } else {
            String s = dictionary.getGoodWordStartingWith(text.getText().toString());

            if(s != null) {
                label.setText(COMPUTER_WINS);
                Toast toast = Toast.makeText(this, COMPUTER_WINS, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, USER_WINS, Toast.LENGTH_LONG);
                toast.show();
                label.setText(USER_WINS);
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        TextView word_frag = (TextView) findViewById(R.id.gameStatus);
        savedInstanceState.putBoolean(GAME_STATUS, userTurn);
        savedInstanceState.putString(WORD_FRAG, word_frag.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        TextView word_frag = (TextView) findViewById(R.id.gameStatus);

        // Restore state members from saved instance
        userTurn = savedInstanceState.getBoolean(GAME_STATUS);
        word_frag.setText(savedInstanceState.getString(WORD_FRAG));
    }
}
