package com.piter.piterdiplomna3.fragments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.piter.piterdiplomna3.R;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    CalendarView calendar;
    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.f_fragment_calendar, container, false);
        //copy function hope it works! no?container
        initializeCalendar(view);
        return view;
    }

    public void initializeCalendar(View view) {
        calendar = (CalendarView) view.findViewById(R.id.calendar);
//        calendar.getDate();
        // sets whether to show the week number.
        Calendar c=Calendar.getInstance();
        calendar.setDate(c.getTimeInMillis());
        calendar.setShowWeekNumber(false);
        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);
//        //The background color for the selected week.
//        int w = 40, h = 50;
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
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getContext(), day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
