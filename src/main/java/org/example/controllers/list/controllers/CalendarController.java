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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.controllers.client.subcontrollers.OrderPropertiesController;
import org.example.customCells.GiftListViewCell;
import org.example.exceptions.ProductDeleteException;
import org.example.interfaces.IListController;
import org.example.model.*;
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
    @FXML ComboBox<String> calendarType;
    Calendar calendar = new Calendar();
    CalendarSource calendarSource = new CalendarSource("source");
    EventHandler<MouseEvent> handler = MouseEvent::consume;
    ArrayList<Entry<CalendarDetailedActivity>> allActivities = new ArrayList<>();
    ArrayList<Entry<Order>> orderActivities = new ArrayList<>();
    ArrayList<Entry<Order>> shippingActivities = new ArrayList<>();
    ObservableList<Calendar> calendars = FXCollections.observableArrayList();
    ObservableList<Calendar> shippingCalendars = FXCollections.observableArrayList();
    ObservableList<Calendar> ordersCallendars = FXCollections.observableArrayList();
    ObservableList<String> calendarOptions = FXCollections.observableArrayList("Produccion", "Envios", "Ventas");
    ArrayList<CalendarActivity> activities = new ArrayList<>(Request.getJ("calendars/basic/filter-list", CalendarActivity[].class, false));
    ArrayList<Order> orders = new ArrayList<>(Request.getJ("orders/basics?sells=true", Order[].class, false ));
    ArrayList<Order> shippings = new ArrayList<>(Request.getJ("orders/basics?sells=false", Order[].class, false ));
    ArrayList<Employee> employees = new ArrayList<>();
    Map<Calendar, String> map = new HashMap<>();
    boolean activitiesShown = false;
    boolean ordersShown = false;
    boolean shippingShown = false;
    String identifier = "";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendarType.setItems(calendarOptions);
        calendarType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("Produccion")){
                    identifier = "Produccion";
                    updateButton.setVisible(true);
                    showNormalActivities();
                }
                else if(t1.equals("Envios")){
                    identifier = "Envios";
                    updateButton.setVisible(false);
                    showShippingActivities();
                }
                else if(t1.equals("Ventas")){
                    identifier = "Ventas";
                    updateButton.setVisible(false);
                    showOrderActivities();
                }
            }
        });
        calendarType.setValue("Produccion");
        diaDayView.setEntryDetailsCallback(new Callback<DateControl.EntryDetailsParameter, Boolean>() {
            @Override
            public Boolean call(DateControl.EntryDetailsParameter entryDetailsParameter) {
                if(identifier.equals("Produccion")) {
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
                }else {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/order.fxml"));
                    try {
                        Parent parent = fxmlLoader.load();
                        OrderPropertiesController dialogController = fxmlLoader.getController();
                        Entry<Order> orderEntry = (Entry<Order>) entryDetailsParameter.getEntry();
                        dialogController.setOrder(orderEntry.getUserObject());
                        Scene scene = new Scene(parent);
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(scene);
                        stage.showAndWait();
                        Order order1 = (Order) stage.getUserData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Se actualizo correctamente el calendario");
            alert.setHeaderText("Se actualizó correctamente el calendario");
            alert.setContentText("");
            Optional<ButtonType> result = alert.showAndWait();
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

    private void showNormalActivities(){
        if(!activitiesShown) {
            int id;
            for (CalendarActivity activity : activities) {
                Employee employee = (Employee) Request.find("users/employees", activity.getIdEmployee(), Employee.class);
                employees.add(employee);
                activity.setEmployee(employee);
            }
            LocalTime time = LocalTime.of(9, 0);
            activities.sort(Comparator.comparing(CalendarActivity::getIdEmployee));
            id = activities.get(0).getIdEmployee();
            for (CalendarActivity activity : activities) {
                CalendarDetailedActivity calendarDetailedActivity = (CalendarDetailedActivity) Request.find("calendars/general", activity.getIdActivity(), activity.getIdEmployee(), CalendarDetailedActivity.class);
                calendarDetailedActivity.setEmployee(activity.getEmployee());
                Entry<CalendarDetailedActivity> calendarActivityEntry = new Entry<>("Actividad de " + activity.getEmployee().getFirstName());
                if (id != activity.getIdEmployee()) {
                    time = LocalTime.of(9, 0);
                }
                calendarActivityEntry.setInterval(calendarDetailedActivity.getDate().toLocalDate(), time, calendarDetailedActivity.getDate().toLocalDate(), time.plusMinutes(18));
                time = time.plusMinutes(18);
                calendarActivityEntry.setUserObject(calendarDetailedActivity);
                allActivities.add(calendarActivityEntry);
                id = activity.getIdEmployee();
            }
            for (Calendar.Style s : Calendar.Style.values()) {
                calendar = new Calendar("Empleado");
                calendar.setStyle(s);
                calendars.add(calendar);
            }
            int counter = 0;
            List<Integer> ids = new ArrayList<>();
            boolean validation = true;
            //ArrayList<WorkDays> activeEmployees = Request.getJ("employee-work-days/list-criteria?date="+ , WorkDays[].class, false);
            int c = 0;
            for (Entry<CalendarDetailedActivity> e : allActivities) {
                for (Integer i : ids) {
                    if (i.equals(e.getUserObject().getIdEmployee())) {
                        validation = false;
                        break;
                    }
                }
                if (validation) {
                    List<Entry<CalendarDetailedActivity>> list = allActivities.stream().filter(p -> p.getUserObject().getIdEmployee().equals(e.getUserObject().getIdEmployee())).collect(Collectors.toList());
                    calendars.get(counter).addEntries(new ArrayList<>(list));
                    if (list.size() == 0) {
                        map.put(calendars.get(counter), "");
                    } else
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
            activitiesShown = true;
        }
        calendarSource.getCalendars().clear();
        calendarSource.getCalendars().addAll(calendars);
        diaDayView.getCalendarSources().clear();
        diaDayView.getCalendarSources().setAll(calendarSource);
    }

    private void showShippingActivities(){
        if(!shippingShown) {
            LocalTime time = LocalTime.of(9, 0);
            for (Order order : shippings) {
                Entry<Order> calendarShippingActivities = new Entry<>("Envio de venta " + order.getIdOrder());
                if(order.getShippingDate()== null) {
                    calendarShippingActivities.setInterval(order.getEstimatedShippingDate(), time, order.getEstimatedShippingDate(), time.plusMinutes(18));
                }else{
                    calendarShippingActivities.setInterval(order.getShippingDate().toLocalDate(), time, order.getShippingDate().toLocalDate(), time.plusMinutes(18));
                }
                time = time.plusMinutes(18);
                calendarShippingActivities.setUserObject(order);
                shippingActivities.add(calendarShippingActivities);
            }
            shippingShown = true;
        }
        Calendar c = new Calendar("Envios");
        c.setStyle(Calendar.Style.STYLE7);
        c.addEntries(new ArrayList<>(shippingActivities));
        calendarSource.getCalendars().clear();
        calendarSource.getCalendars().add(c);
        diaDayView.getCalendarSources().clear();
        diaDayView.getCalendarSources().setAll(calendarSource);
    }

    private void showOrderActivities(){
        if(!ordersShown) {
            LocalTime time = LocalTime.of(9, 0);
            for (Order order : shippings) {
                Entry<Order> calendarShippingActivities = new Entry<>("Venta " + order.getIdOrder());
                calendarShippingActivities.setInterval(order.getDate().toLocalDate(), time, order.getDate().toLocalDate(), time.plusMinutes(18));
                time = time.plusMinutes(18);
                calendarShippingActivities.setUserObject(order);
                orderActivities.add(calendarShippingActivities);
            }
            ordersShown = true;
        }
        Calendar c = new Calendar("Ventas");
        c.setStyle(Calendar.Style.STYLE6);
        c.addEntries(new ArrayList<>(orderActivities));
        calendarSource.getCalendars().clear();
        calendarSource.getCalendars().add(c);
        diaDayView.getCalendarSources().clear();
        diaDayView.getCalendarSources().setAll(calendarSource);
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