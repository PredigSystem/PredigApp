package predigsystem.udl.org.predigsystem.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.DateWatcher;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;
import pl.rafman.scrollcalendar.data.CalendarDay;
import predigsystem.udl.org.predigsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment{


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ScrollCalendar scrollCalendar = (ScrollCalendar) getActivity().findViewById(R.id.scrollCalendar);
        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {
                // user clicked on a specific date on the calendar
            }
        });
        scrollCalendar.setDateWatcher(new DateWatcher() {
            @Override
            public int getStateForDate(int year, int month, int day) {
                //    CalendarDay.DEFAULT,
                //    CalendarDay.DISABLED,
                //    CalendarDay.TODAY,
                //    CalendarDay.UNAVAILABLE,
                //    CalendarDay.SELECTED,
                return CalendarDay.DEFAULT;
            }
        });
        scrollCalendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                // return false if you don't want to show later months
                return true;
            }
            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                // return false if you don't want to show previous months
                return true;
            }
        });
    }
}
