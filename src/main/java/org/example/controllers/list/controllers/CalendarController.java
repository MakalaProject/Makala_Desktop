package org.example.controllers.list.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.page.DayPage;
import com.calendarfx.view.page.MonthPage;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.customCells.GiftListViewCell;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IListController;
import org.example.model.CalendarActivity;
import org.example.model.CalendarDetailedActivity;
import org.example.model.Employee;
import org.example.model.Gift;
import org.example.model.products.InsideProduct;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarController implements Initializable, IListController<CalendarActivity> {
    @FXML DetailedDayView diaDayView;
    @FXML MonthPage mesPage;
    @FXML FontAwesomeIconView updateButton;
    Calendar calendar = new Calendar();
    CalendarSource calendarSource = new CalendarSource("source");
    EventHandler<MouseEvent> handler = MouseEvent::consume;
    ArrayList<Entry<CalendarDetailedActivity>> allActivities = new ArrayList<>();
    ObservableList<Calendar> calendars = FXCollections.observableArrayList();

    Map<Calendar, String> map = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<CalendarActivity> activities = new ArrayList<>(Request.getJ("calendars/basic/filter-list", CalendarActivity[].class, false));
        ArrayList<Employee> employees = new ArrayList<>();
        int id;
        for(CalendarActivity activity : activities){
            Employee employee = (Employee) Request.find("users/employees",activity.getIdEmployee(), Employee.class);
            employees.add(employee);
            activity.setEmployee(employee);
        }
        LocalTime time = LocalTime.of(9,0);
        activities.sort(Comparator.comparing(CalendarActivity::getIdEmployee));
        id = activities.get(0).getIdEmployee();
        for (CalendarActivity activity: activities){
            CalendarDetailedActivity calendarDetailedActivity = (CalendarDetailedActivity) Request.find("calendars/general", activity.getIdActivity(), activity.getIdEmployee(), CalendarDetailedActivity.class);
            calendarDetailedActivity.setEmployee(activity.getEmployee());
            Entry<CalendarDetailedActivity> calendarActivityEntry = new Entry<>("Actividad de " + activity.getEmployee().getFirstName());
            if(id!= activity.getIdEmployee()){
                time = LocalTime.of(9,0);
            }
            calendarActivityEntry.setInterval(calendarDetailedActivity.getDate().toLocalDate(), time, calendarDetailedActivity.getDate().toLocalDate(), time.plusMinutes(calendarDetailedActivity.getTime()));
            time = time.plusMinutes(calendarDetailedActivity.getTime());
            calendarActivityEntry.setUserObject(calendarDetailedActivity);
            allActivities.add(calendarActivityEntry);
            id = activity.getIdEmployee();
        }
        for(Calendar.Style s : Calendar.Style.values()){
            calendar = new Calendar("Empleado");
            calendar.setStyle(s);
            calendars.add(calendar);
        }
        int counter = 0;
        List<Integer> ids = new ArrayList<>();
        boolean validation = true;
        for (Entry<CalendarDetailedActivity> e : allActivities){
            for(Integer i : ids){
                if(i.equals(e.getUserObject().getIdEmployee())){
                    validation = false;
                    break;
                }
            }
            if(validation) {
                List<Entry<CalendarDetailedActivity>> list = allActivities.stream().filter(p -> p.getUserObject().getIdEmployee().equals(e.getUserObject().getIdEmployee())).collect(Collectors.toList());
                calendars.get(counter).addEntries(new ArrayList<>(list));
                if(list.size() == 0){
                    map.put(calendars.get(counter), "");
                }else
                map.put(calendars.get(counter), list.get(0).getUserObject().getIdEmployee().toString());
                ids.add(e.getUserObject().getIdEmployee());
                counter++;
            }
            validation = true;
        }
        mesPage.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            LocalDate date = mesPage.getDate();
            diaDayView.setDate(date);
        });
        calendarSource.getCalendars().addAll(calendars);
        diaDayView.getCalendarSources().setAll(calendarSource);
        diaDayView.setEntryDetailsCallback(new Callback<DateControl.EntryDetailsParameter, Boolean>() {
            @Override
            public Boolean call(DateControl.EntryDetailsParameter entryDetailsParameter) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/calendar_activity_properties.fxml"));
                try {
                    ActivityPropertiesController loadDialog = new ActivityPropertiesController();
                    fxmlLoader.setController(loadDialog);
                    Parent parent = fxmlLoader.load();
                    loadDialog.setObject((Entry<CalendarDetailedActivity>) entryDetailsParameter.getEntry());
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                    Entry<CalendarDetailedActivity> object = (Entry<CalendarDetailedActivity>) stage.getUserData();
                    if (object != null) {
                        changeCalendar(object);
                        sortEntries();
                        filterEntries();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            for(Entry<CalendarDetailedActivity> entry : allActivities){
                try {
                    CalendarDetailedActivity response = (CalendarDetailedActivity) Request.putJ("calendars", entry.getUserObject());
                } catch (ProductDeleteException e) {
                    e.printStackTrace();
                }
            }
        });


        diaDayView.setEntryContextMenuCallback(new Callback<DateControl.EntryContextMenuParameter, ContextMenu>() {
            @Override
            public ContextMenu call(DateControl.EntryContextMenuParameter entryContextMenuParameter) {
                return null;
            }
        });
    }

    public void changeCalendar(Entry<CalendarDetailedActivity> object) {
        for(Calendar c : calendars){
            c.removeEntry(object);
            String value = map.get(c);
            if(value != null && value.equals(object.getUserObject().getIdEmployee().toString())){
                c.addEntry(object);
                break;
            }
        }
    }

    private void sortEntries() {
        for(Calendar c : calendars){
            String value = map.get(c);
            if(value != null){
                List<Entry<CalendarDetailedActivity>> toSort = new ArrayList<>();
                toSort = allActivities.stream().filter(p -> p.getUserObject().getIdEmployee().equals(Integer.parseInt(value))).collect(Collectors.toList());
                toSort.sort(Comparator.comparing(Entry<CalendarDetailedActivity>::getEndTime));
                int counter = 1;
                for(Entry<CalendarDetailedActivity> e : toSort){
                    e.getUserObject().setDailyNumber(counter);
                    counter++;
                }
            }
        }

    }

    private void filterEntries(){
        for(Calendar c : calendars){
            String value = map.get(c);
            if(value != null){
                List<Entry<CalendarDetailedActivity>> toSort = new ArrayList<>();
                toSort = allActivities.stream().filter(p -> p.getUserObject().getIdEmployee().equals(Integer.parseInt(value))).collect(Collectors.toList());
                toSort.sort(Comparator.comparing(Entry<CalendarDetailedActivity>::getStartDate));
                for(int counter = 0; counter < toSort.size(); counter++){
                    if(counter ==0){
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {

    }

    @Override
    public void updateView() {

    }

    @Override
    public void cleanForm() {

    }
}