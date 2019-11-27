package com.thinkty.toolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button hydroButton;
    private Button todoButton;
    private Button musicButton;
    private Button happyButton;
    private Button alarmButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hydroButton = (Button) findViewById(R.id.hydroButton);
        todoButton = (Button) findViewById(R.id.todoButton);
        musicButton = (Button) findViewById(R.id.musicButton);
        happyButton = (Button) findViewById(R.id.happinessButton);
        alarmButton = (Button) findViewById(R.id.alarmButton);

        // Setting onClickListeners for buttons
        hydroButton.setOnTouchListener(onTouchListenerForButtons);
        todoButton.setOnTouchListener(onTouchListenerForButtons);
        musicButton.setOnTouchListener(onTouchListenerForButtons);
        happyButton.setOnTouchListener(onTouchListenerForButtons);
        alarmButton.setOnTouchListener(onTouchListenerForButtons);
    }


    /**
     * OnTouchListener method for the buttons in the main activity
     */
    View.OnTouchListener onTouchListenerForButtons = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Get which action is being done
            boolean result = false;
            switch (event.getAction()) {
                // Touch started
                case MotionEvent.ACTION_DOWN:
                    switch (v.getId()) {
                        case R.id.hydroButton:
                            hydroButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_selected_left));
                            break;

                        case R.id.todoButton:
                            todoButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_selected_right));
                            break;

                        case R.id.musicButton:
                            musicButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_selected_left));
                            break;

                        case R.id.happinessButton:
                            happyButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_selected_right));
                            break;

                        case R.id.alarmButton:
                            alarmButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_selected_left));
                            break;
                    } // switch
                    result = true;
                    break;

                // Touch completed
                case MotionEvent.ACTION_UP:
                    // Change to appropriate activities based on which button was clicked
                    Intent intent = null;
                    switch (v.getId()) {
                        case R.id.hydroButton:
                            hydroButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_left));
                            intent = new Intent(v.getContext(), HydroActivity.class);
                            break;

                        case R.id.todoButton:
                            todoButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_right));
                            intent = new Intent(v.getContext(), TodoActivity.class);
                            break;

                        case R.id.musicButton:
                            musicButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_left));
                            intent = new Intent(v.getContext(), MusicActivity.class);
                            break;

                        case R.id.happinessButton:
                            happyButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_right));
                            intent = new Intent(v.getContext(), HappinessActivity.class);
                            break;

                        case R.id.alarmButton:
                            alarmButton.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_box_border_left));
                            intent = new Intent(v.getContext(), AlarmActivity.class);
                            break;
                    } // switch

                    // Change activity
                    if (intent != null) {
                        startActivity(intent);
                    }
                    result = true;
                    break;

            } // switch

            return result;
        }
    };
}
