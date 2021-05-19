package org.example.controllers.list.controllers;

import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CustomEntry<T> extends Entry<T> implements EventHandler<MouseEvent> {

    public CustomEntry(String title){
        super(title);
    }

    public String getRealTitle(){
        return super.getTitle();

    }


    @Override
    public void handle(MouseEvent mouseEvent) {

    }
}
