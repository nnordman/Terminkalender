package de.nnordman.beaf.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.nnordman.beaf.DataBaseHelper;
import de.nnordman.beaf.Model.EventSqlLite;
import de.nnordman.beaf.R;


public class event_Detail_Fragment extends Fragment {

    private MaterialEditText dateText;
    private MaterialEditText timeText;
    private MaterialEditText eventName;
    private MaterialEditText location;
    private MaterialEditText notes;
    private Calendar myCalendar;
    private Calendar myCalendarTime;
    private CheckBox alarm;

    private Button edit;
    private Button save;

    DataBaseHelper db;
    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_event__detail_, container, false);

        db = new DataBaseHelper(this.getActivity());

        timeText = (MaterialEditText) view.findViewById(R.id.timePicker);
        dateText = (MaterialEditText) view.findViewById(R.id.date);
        eventName = (MaterialEditText) view.findViewById(R.id.event_Name);
        location = (MaterialEditText) view.findViewById(R.id.location);
        notes = (MaterialEditText) view.findViewById(R.id.notes);
        edit = (Button) view.findViewById(R.id.editbtn);
        save = (Button) view.findViewById(R.id.savebtn);
        //alarm = (CheckBox) view.findViewById(R.id.alarm);

        Bundle bundle = getArguments();
        String msg = bundle.getString("ID");

        try {
            id = Integer.parseInt(msg);
        } catch (NumberFormatException nfe) {

        }
        EventSqlLite eventSqlLite = db.getEvent(id);

        eventName.setText(eventSqlLite.getEventNameSql());
        location.setText(eventSqlLite.getLocationSql());
        dateText.setText(eventSqlLite.getDateSql());
        timeText.setText(eventSqlLite.getTimeSql());
        notes.setText(eventSqlLite.getNotesSql());

        // Event bearbeiten
        // ----------------------------------------------------------------------------------------------------------
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName.setFocusableInTouchMode(true);
                eventName.setFocusable(true);
                location.setFocusableInTouchMode(true);
                location.setFocusable(true);
                notes.setFocusableInTouchMode(true);
                notes.setFocusable(true);

                edit.setVisibility(v.GONE);
                save.setVisibility(v.VISIBLE);


                // Time and DatePicker
        // ----------------------------------------------------------------------------------------------------------
                myCalendar = Calendar.getInstance();
                myCalendarTime = Calendar.getInstance();

                final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendarTime.set(Calendar.HOUR, hourOfDay);
                        myCalendarTime.set(Calendar.MINUTE, minute);
                        updateLabelTime();
                    }
                };
                timeText.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new TimePickerDialog(getContext(), time, myCalendarTime.get(Calendar.HOUR), myCalendarTime.get(Calendar.MINUTE), true).show();
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
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = (getActivity()).getSupportFragmentManager().beginTransaction();

                EventSqlLite eventSqlLite = new EventSqlLite(id, eventName.getText().toString()
                        , location.getText().toString(), dateText.getText().toString(), timeText.getText().toString(), notes.getText().toString());
                db.updateEvent(eventSqlLite);

                transaction.replace(R.id.content, new events_Fragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;
    }

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

    // Aktivieren der Notifikations
    public void showNotification() {
        //NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, hours, minutes, years, months, days);
        //System.out.println("Hours: " + hours + "Minutes: " + minutes + "Years: " + years + "Months: " + months + "Days: " + days);
    }

}
