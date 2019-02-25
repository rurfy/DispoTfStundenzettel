package com.projects.christopherrichter.zeiterfassung;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView timer;
    private Button start;
    private boolean timerRuns = false;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private Handler handler;
    private int Seconds, Minutes;
    private Zeitplan zp;
    private String eMailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zp = new Zeitplan();

        timer = (Button) findViewById(R.id.bPausendauer);
        start = (Button) findViewById(R.id.bPause);

        handler = new Handler();

        final TextView tVStartDienst = (TextView) findViewById(R.id.tVStartDienst);
        final Button bStartDienst = (Button) findViewById(R.id.bStartDienst);
        bStartDienst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bStartDienst.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.dienstStart))) {

                    zp.setDienstBeginn(showPickers(zp.getDienstBeginn(), bStartDienst, tVStartDienst));
                } else {
                    zp.setDienstBeginn(getTimeStamp());
                    bStartDienst.setText(getFormattedTime(zp.getDienstBeginn()) + " Uhr");
                    tVStartDienst.setText(getFormattedDate(zp.getDienstBeginn()));
                    bStartDienst.setBackgroundResource(R.drawable.buttonstyle);
                }
            }
        });

        final Button bAbfahrt = (Button) findViewById(R.id.bAbfahrt);
        bAbfahrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bAbfahrt.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.abfahft))) {

                    zp.setAbfahrt(showTimePicker(zp.getAbfahrt(), bAbfahrt));
                } else {
                    zp.setAbfahrt(getTimeStamp());
                    bAbfahrt.setText(getFormattedTime(zp.getAbfahrt()) + " Uhr");
                    bAbfahrt.setBackgroundResource(R.drawable.buttonstyle);
                }
            }
        });

        final Button bAnkunft = (Button) findViewById(R.id.bAnkunft);
        bAnkunft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bAnkunft.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.ankunft))) {

                    zp.setAnkunft(showTimePicker(zp.getAnkunft(), bAnkunft));
                } else {
                    zp.setAnkunft(getTimeStamp());
                    bAnkunft.setText(getFormattedTime(zp.getAnkunft()) + " Uhr");
                    bAnkunft.setBackgroundResource(R.drawable.buttonstyle);
                }
            }
        });

        final TextView tVEndDienst = (TextView) findViewById(R.id.tVEndDienst);
        final Button bEndDienst = (Button) findViewById(R.id.bEndDienst);
        bEndDienst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bEndDienst.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.dienstEnde))) {

                    zp.setDienstEnde(showPickers(zp.getDienstEnde(), bEndDienst, tVEndDienst));
                } else {
                    zp.setDienstEnde(getTimeStamp());
                    bEndDienst.setText(getFormattedTime(zp.getDienstEnde()) + " Uhr");
                    tVEndDienst.setText(getFormattedDate(zp.getDienstEnde()));
                    bEndDienst.setBackgroundResource(R.drawable.buttonstyle);
                }
            }
        });

        final Button bPausenDauer = (Button) findViewById(R.id.bPausendauer);
        bPausenDauer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(bPausenDauer);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bPausenDauer.setBackgroundResource(R.drawable.buttonstyle);
                if (timerRuns) {
                    start.setText(R.string.pauseStart);
                    TimeBuff += MillisecondTime;
                    zp.setPause(timer.getText().toString());
                    handler.removeCallbacks(runnable);
                    timerRuns = false;
                } else

                {
                    start.setText(R.string.pauseStop);
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    timerRuns = true;
                }
            }

        });

        Button bSend = (Button) findViewById(R.id.bSend);
        bSend.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                if (zp.getDienstBeginn() == null || zp.getDienstEnde() == null || zp.getAbfahrt() == null || zp.getAnkunft() == null || zp.getPause() == null) {

                    builder.setMessage("Bitte tragen Sie zunächst alle Daten ein, bevor Sie sie absenden.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (bEndDienst.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.dienstEnde))) {
                                        bEndDienst.setBackgroundResource(R.drawable.buttonstyle_alert);
                                    }
                                    else bEndDienst.setBackgroundResource(R.drawable.buttonstyle);
                                    if (bStartDienst.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.dienstStart))) {
                                        bStartDienst.setBackgroundResource(R.drawable.buttonstyle_alert);
                                    }
                                    else bStartDienst.setBackgroundResource(R.drawable.buttonstyle);
                                    if (bAbfahrt.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.abfahft))) {
                                        bAbfahrt.setBackgroundResource(R.drawable.buttonstyle_alert);
                                    }
                                    else bAbfahrt.setBackgroundResource(R.drawable.buttonstyle);
                                    if (bAnkunft.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.ankunft))) {
                                        bAnkunft.setBackgroundResource(R.drawable.buttonstyle_alert);
                                    }
                                    else bAnkunft.setBackgroundResource(R.drawable.buttonstyle);
                                    if (bPausenDauer.getText().toString().equalsIgnoreCase(MainActivity.this.getString(R.string.pausenDauer))) {
                                        bPausenDauer.setBackgroundResource(R.drawable.buttonstyle_alert);
                                    }
                                    else bPausenDauer.setBackgroundResource(R.drawable.buttonstyle);


                                }
                            });
                } else {
                    builder.setMessage("Bitte überprüfen Sie noch einmal Ihre Daten, bevor Sie sie absenden und editieren Sie diese gegebenenfalls.")
                            .setPositiveButton("Absenden", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    PreferenceManager.setDefaultValues(MainActivity.this, R.xml.preferences, false);
                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                                    eMailText = "Hallo ihr Guten, \n" +
                                            "hiermit send ich euch meinen Schichtbericht. \n" +
                                            "Dienstbeginn: " + zp.getDienstBeginn().get(Calendar.DAY_OF_MONTH) + "." + zp.getDienstBeginn().get(Calendar.MONTH) + "." + zp.getDienstBeginn().get(Calendar.YEAR) + " " +
                                            zp.getDienstBeginn().get(Calendar.HOUR_OF_DAY) + ":" + zp.getDienstBeginn().get(Calendar.MINUTE) + " Uhr\n" +
                                            "Abfahrtszeit: " + zp.getAbfahrt().get(Calendar.HOUR_OF_DAY) + ":" + zp.getAbfahrt().get(Calendar.MINUTE) + " Uhr\n" +
                                            "Pausendauer: " + zp.getPause() +"\n" +
                                            "Ankunfszeit: " + zp.getAnkunft().get(Calendar.HOUR_OF_DAY) + ":" + zp.getAnkunft().get(Calendar.MINUTE) + " Uhr\n" +
                                            "Dienstende: " + zp.getDienstEnde().get(Calendar.DAY_OF_MONTH) + "." + zp.getDienstEnde().get(Calendar.MONTH) + "." + zp.getDienstEnde().get(Calendar.YEAR) + " " +
                                            zp.getDienstEnde().get(Calendar.HOUR_OF_DAY) + ":" + zp.getDienstEnde().get(Calendar.MINUTE) + " Uhr\n" + "\n" +
                                            "Mit freundlichen Grüßen" + "\n" +
                                            sharedPref.getString(SettingsActivity.KEY_PREF_USER_NAME, "");
                                    sendEmail(sharedPref);
                                }
                            })
                            .setNegativeButton("Zurück", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

    }

    protected void sendEmail(SharedPreferences sharedPref) {

        String[] TO = {sharedPref.getString(SettingsActivity.KEY_PREF_TARGET_MAIL, "")};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Schichtbericht");
        emailIntent.putExtra(Intent.EXTRA_TEXT, eMailText);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "Es scheint kein Email Programm installiert zu sein.", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFormattedDate(Calendar c) {

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    private String getFormattedTime(Calendar c) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    private String getFormattedMinutes(int minutes) {
        SimpleDateFormat df = new SimpleDateFormat("mm");
        String formattedDate = df.format(minutes);

        return formattedDate;
    }

    private Calendar getTimeStamp() {
        Calendar c = Calendar.getInstance();
        return c;
    }

    private Calendar showPickers(Calendar c, final Button b, final TextView tV) {

        final Calendar newC = c;
        DatePickerDialog dp = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                tV.setText(dayOfMonth + "." + monthOfYear + "." + year);
                newC.set(Calendar.YEAR, year);
                newC.set(Calendar.MONTH, monthOfYear);
                newC.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dp.show();

        TimePickerDialog tp = new TimePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                newC.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newC.set(Calendar.MINUTE, minute);
                b.setText(getFormattedTime(newC) + " Uhr");
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tp.show();

        return newC;
    }

    private Calendar showTimePicker(Calendar c, final Button b) {

        final Calendar newC = c;
        TimePickerDialog tp = new TimePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                newC.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newC.set(Calendar.MINUTE, minute);
                b.setText(getFormattedTime(newC) + " Uhr");
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tp.show();

        return newC;
    }

    private void showNumberPicker(final Button b) {

        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setView(dialogView);

        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 5;
                return "" + temp;
            }
        };
        numberPicker.setFormatter(formatter);
        numberPicker.setMaxValue(24);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            }
        });
        d.setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                b.setText(Integer.toString(numberPicker.getValue() * 5) + ":00 Minuten");
                zp.setPause(Integer.toString(numberPicker.getValue() * 5) + " Minuten");
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(600, 800);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + " Minuten");

            handler.postDelayed(this, 0);
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

        }

        return true;
    }
}