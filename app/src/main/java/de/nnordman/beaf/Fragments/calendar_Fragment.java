package de.nnordman.beaf.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.nnordman.beaf.DataBaseHelper;
import de.nnordman.beaf.R;
import de.nnordman.beaf.decorators.EventDecorator;
import de.nnordman.beaf.decorators.OneDayDecorator;

public class calendar_Fragment extends Fragment implements OnDateSelectedListener {

    ArrayList<CalendarDay> dates = new ArrayList<>();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            widget.addDecorator(new EventDecorator(Color.RED, dates));
        }
    };

    private AdView mAdView;

    MaterialCalendarView widget;
    TextView textView;
    DataBaseHelper db;
    final int SPEECHINTENT_REQ_CODE = 11;

    //-------------------------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_calendar_, container, false);
        db = new DataBaseHelper(this.getActivity());
        textView = (TextView) view.findViewById(R.id.textView);
        widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        widget.setOnDateChangedListener(this);

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

        widget.addDecorators(oneDayDecorator);

        // Dauer des Kalenders
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            Date dateMin = sdf.parse("01/01/17");
            Date dateMax = sdf.parse("31/12/20");
            widget.state().edit()
                    .setMinimumDate(dateMin)
                    .setMaximumDate(dateMax)
                    .commit();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        showAllSQLData();

        // ------------------------------------------------------------
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
/*                MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544/6300978111");
                mAdView = (AdView) view.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);*/


        //new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());


        return view;
    }

    //-------------------------------------------------------------------------------------------------------------
    // Ausgabe der Fokusierten Tage mit Informationen
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        Cursor res = db.getAllEventsData();
        String pruefen = "";
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        while (res.moveToNext()) {
            if (res.getString(3).equals(getSelectedDatesString())) {
                pruefen = res.getString(1);
            }
        }
        if (pruefen.length() == 0) {
            textView.setText("");
        } else {
            textView.setText(pruefen);
        }
        db.close();
    }

    //-------------------------------------------------------------------------------------------------------------
    // Markierten Tag ausgeben
    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }

        return FORMATTER.format(date.getDate());
    }

    //-------------------------------------------------------------------------------------------------------------
    // Anzeige der Events im Kalender
    public void showAllSQLData() {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    //-------------------------------------------------------------------------------------------------------------
    // AsyncTask für das laden der Events
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Loading dates...");

            Cursor res = db.getAllEventsData();
            if (res.getCount() == 0) {

            }

            while (res.moveToNext()) {

                Calendar calendar = Calendar.getInstance();
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                try {
                    Date test = sdf.parse(res.getString(3));
                    calendar.setTime(test);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);

            }
            res.close();
            db.close();
            handler.sendEmptyMessage(0);
            resp = "Dates geladen";

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }
}
