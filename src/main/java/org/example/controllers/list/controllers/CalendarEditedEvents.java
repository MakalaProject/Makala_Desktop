package org.example.controllers.list.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class CalendarEditedEvents extends CalendarEvent {
    public static final EventType<CalendarEditedEvents> ENTRYCLICKED;
    protected CalendarEditedEvents(EventType<? extends CalendarEvent> eventType, Calendar calendar) {
        super(eventType, calendar);
    }


    static{
        ENTRYCLICKED = new EventType(MouseEvent.MOUSE_CLICKED, "ENTRY_CLICKED");
    }
}
