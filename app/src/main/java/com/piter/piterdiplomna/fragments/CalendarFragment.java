package com.piter.piterdiplomna.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piter.piterdiplomna.R;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    private static final String TAG = "TAG CalendarFragment";
    ExtendedCalendarView calendar;

    public CalendarFragment() {
        // Required empty public constructor
        Log.d(TAG, "CalendarFragment: konstruktor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.f_fragment_calendar, container, false);
        //copy function hope it works! no?container
        Log.d(TAG, "onCreateView: shy 2 times bro?");
//        initializeCalendar(view);//no need for it
        return view;
    }

    public void initializeCalendar(View view) {
        calendar = (ExtendedCalendarView) view.findViewById(R.id.calendar);
////        getActivity().getContentResolver().onU
////        ContentResolver musicResolver = getActivity().getContentResolver();
////        getActivity().deleteDatabase("Calendar");
////        Log.d(TAG, "initializeCalendar: iztri se bazata");
////        calendar.getDate();
////        calendar.
//        // Adding events
//        ContentValues values = new ContentValues();
//        values.put(CalendarProvider.COLOR, Event.COLOR_RED);
//        values.put(CalendarProvider.DESCRIPTION, "Sample Description");
//        values.put(CalendarProvider.LOCATION, "Sample Location");
//        values.put(CalendarProvider.EVENT, "Test Event");
//
//        Calendar cal = Calendar.getInstance();
//        cal.setFirstDayOfWeek(Calendar.MONDAY);
//        TimeZone tz = TimeZone.getDefault();
//
//        //Day julianStartDay = new Day(getActivity(), 25, 2014, 04);
//        cal.set(2017, 00, 25, 13, 0);
//        Log.d(TAG, "initializeCalendar: day1"+cal.getTime().toString());
//        int julianDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        Log.d(TAG, "initializeCalendar: julianDay="+julianDay);
//
//        values.put(CalendarProvider.START, cal.getTimeInMillis());
//        values.put(CalendarProvider.START_DAY, julianDay);
//
//        cal.set(2017, 00, 29, 13, 0);
//        Log.d(TAG, "initializeCalendar: day2"+cal.getTime().toString());
//        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(),TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//
//        values.put(CalendarProvider.END, cal.getTimeInMillis());
//        values.put(CalendarProvider.END_DAY, endDayJulian);
//
////        String empty[];
//        CalendarProvider tozi = new CalendarProvider();
//        tozi.onCreate();
//        Log.d(TAG, "initializeCalendar: dali se napravi DB?");
//        Uri uri = getActivity().getContentResolver().insert(CalendarProvider.CONTENT_URI,values);
        // sets whether to show the week number.
//        Calendar c=Calendar.getInstance();
//        c.getDisplayNames
//        calendar.setDate(c.getTimeInMillis());
//        calendar.setShowWeekNumber(false);
        // sets the first day of week accord/ing to Calendar.
        // here we set Monday as the first day of the Calendar
//        calendar.setFirstDayOfWeek(2);
//        //The background color for the selected week.
//        int w = 40, h = 50;
//        Uri uri = getContentResolver().
//
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
//        Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
//
//        Canvas canvas = new Canvas(bmp);
//        Canvas f = new Canvas();
//        f.setBitmap(canvas);
//
//        calendar.draw(canvas);
//        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
//        //sets the color for the dates of an unfocused month.
//        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
//
//        //sets the color for the separator line between weeks.
//        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
//
//        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
//        calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            //show the selected date as a toast
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
//                Toast.makeText(getContext(), day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
