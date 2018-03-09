package de.nnordman.beaf.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.nnordman.beaf.AlarmEvents.AlarmReceiver;
import de.nnordman.beaf.AlarmEvents.NotificationScheduler;
import de.nnordman.beaf.DataBaseHelper;
import de.nnordman.beaf.Model.EventSqlLite;
import de.nnordman.beaf.R;


public class event_Anlegen_Fragment extends Fragment {

    private MaterialEditText dateText;
    private MaterialEditText timeText;
    private MaterialEditText eventName;
    private MaterialEditText location;
    private MaterialEditText notes;
    private CheckBox alarm;

    private Calendar myCalendar;
    private Calendar myCalendarTime;
    private Button save;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    List<EventSqlLite> data = new ArrayList<>();
    DataBaseHelper db;
    private int i = 0;
    int hours, minutes, years, months, days;

    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_event__anlegen_, container, false);

        myCalendar = Calendar.getInstance();
        myCalendarTime = Calendar.getInstance();
        db = new DataBaseHelper(this.getActivity());

        timeText = (MaterialEditText) view.findViewById(R.id.timePicker);
        dateText = (MaterialEditText) view.findViewById(R.id.date);
        eventName = (MaterialEditText) view.findViewById(R.id.event_Name);
        location = (MaterialEditText) view.findViewById(R.id.location);
        notes = (MaterialEditText) view.findViewById(R.id.notes);
        save = (Button) view.findViewById(R.id.saveEvent);
        //alarm = (CheckBox) view.findViewById(R.id.alarm);


        // Time and DatePicker
        // ----------------------------------------------------------------------------------------------------------
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarTime.set(Calendar.MINUTE, minute);

                hours = hourOfDay;
                minutes = minute;

                updateLabelTime();
            }
        };

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(getContext(), time, myCalendarTime.get(Calendar.HOUR_OF_DAY), myCalendarTime.get(Calendar.MINUTE), true).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                years = year;
                months = monthOfYear;
                days = dayOfMonth;

                updateLabel();
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // Prüfen ob der Name des Events eingetragen wurde sowie speichern des Events
        // ----------------------------------------------------------------------------------------------------------
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }

                EventSqlLite eventSqlLite = new EventSqlLite(i, eventName.getText().toString()
                        , location.getText().toString(), dateText.getText().toString(), timeText.getText().toString(), notes.getText().toString());
                db.addEvent(eventSqlLite);

                //Toast.makeText(getActivity(), R.string.event_created, Toast.LENGTH_SHORT).show();

                timeText.setText("");
                dateText.setText("");
                eventName.setText("");
                location.setText("");
                notes.setText("");

                /*if(alarm.isChecked()){
                    showNotification();
                    //Toast.makeText(getActivity(), "Alarm aktiviert",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getActivity(), "Alarm nicht aktiviert",Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        return view;
    }

    // ----------------------------------------------------------------------------------------------------------
    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        timeText.setText(sdf.format(myCalendarTime.getTime()));
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = eventName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            if (Locale.getDefault().getLanguage().contentEquals("en")) {
                eventName.setError("Required");
                valid = false;
            } else {
                eventName.setError("Erforderlich");
                valid = false;
            }
        } else {
            eventName.setError(null);
        }

        String date = dateText.getText().toString();
        if (TextUtils.isEmpty(date)) {
            if (Locale.getDefault().getLanguage().contentEquals("en")) {
                dateText.setError("Required");
                valid = false;
            } else {
                dateText.setError("Erforderlich");
                valid = false;
            }
        } else {
            dateText.setError(null);
        }

        return valid;
    }

    // Aktivieren der Notifikations
    public void showNotification() {
        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, hours, minutes, years, months, days);
        System.out.println("Hours: " + hours + "Minutes: " + minutes + "Years: " + years + "Months: " + months + "Days: " + days);
    }

}
