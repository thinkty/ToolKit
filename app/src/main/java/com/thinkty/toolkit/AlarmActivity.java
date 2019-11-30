package com.thinkty.toolkit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private TimePicker timePicker;
    private ToggleButton alarmToggleButton;
    private static AlarmActivity instance;
    private SharedPreferences preference;

    public static AlarmActivity instance() {
        return instance;
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get preferences
        preference = getPreferences(Context.MODE_PRIVATE);

        if (preference.getBoolean("alarmIsEnabled", false)) {
            alarmToggleButton.setChecked(true);
        } else {
            alarmToggleButton.setChecked(false);
        }

        // Update the drawable for the alarm toggle button
        if (alarmToggleButton.isChecked()) {
            alarmToggleButton.setBackgroundDrawable(getDrawable(R.drawable.alarm_toggle_background_on));
        } else {
            alarmToggleButton.setBackgroundDrawable(getDrawable(R.drawable.alarm_toggle_background_off));
        }

        // Stop the ringtone
        if (AlarmReceiver.getRingtone() != null && AlarmReceiver.getRingtone().isPlaying()) {
            AlarmReceiver.getRingtone().stop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmToggleButton = (ToggleButton) findViewById(R.id.alarmToggle);

        // Get preferences
        preference = getPreferences(Context.MODE_PRIVATE);

        if (preference.getBoolean("alarmIsEnabled", false)) {
            alarmToggleButton.setChecked(true);
        } else {
            alarmToggleButton.setChecked(false);
        }

        // Set the clock to the set time
        String savedTime = preference.getString("alarmSetTime", null);
        if (savedTime != null) {
            timePicker.setHour(Integer.parseInt(savedTime.substring(0, savedTime.indexOf("/"))));
            timePicker.setMinute(Integer.parseInt(savedTime.substring(savedTime.indexOf("/") + 1)));
        }

        // Update the drawable for the alarm toggle button
        if (alarmToggleButton.isChecked()) {
            alarmToggleButton.setBackgroundDrawable(getDrawable(R.drawable.alarm_toggle_background_on));
        } else {
            alarmToggleButton.setBackgroundDrawable(getDrawable(R.drawable.alarm_toggle_background_off));
        }

        // Set an alarm on button click
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((ToggleButton) view).isChecked()) {
                    Log.d("AlarmActivity", "Alarm On");

                    // Edit the shared preference key-value data
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putBoolean("alarmIsEnabled", true);
                    editor.putString("alarmSetTime", timePicker.getHour() + "/" + timePicker.getMinute());
                    editor.apply();

                    // Changing the background drawable
                    alarmToggleButton.setBackgroundDrawable(getDrawable(R.drawable.alarm_toggle_background_on));

                    // Getting the setting time
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                    // Creating the intent to be activated at alarm notification click
                    alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0);

                    // Setting the alarm, and the intent on the Real Time Clock (also, wake up the device)
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                    Toast.makeText(getApplicationContext(), "Alarm enabled", Toast.LENGTH_SHORT).show();
                } else {

                    // Edit the shared preference key-value data
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putBoolean("alarmIsEnabled", false);
                    editor.putString("alarmSetTime", null);
                    editor.apply();

                    // Changing the background drawable
                    alarmToggleButton.setBackgroundDrawable(getDrawable(R.drawable.alarm_toggle_background_off));

                    // Canceling the intent
                    if (alarmManager != null) {
                        alarmIntent = PendingIntent.getService(getApplicationContext(), 0, new Intent(), 0);

                        if (alarmIntent == null) {
                            Log.e("AlarmActivity", "AlarmManager could not cancel alarm due to null pending intent");
                        } else {
                            alarmManager.cancel(alarmIntent);
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Alarm disabled", Toast.LENGTH_SHORT).show();
                    Log.d("AlarmActivity", "Alarm Off");
                }
            }
        });
    }
}
