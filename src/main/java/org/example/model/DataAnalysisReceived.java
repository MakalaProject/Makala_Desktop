package org.example.model;

import lombok.Data;
import org.example.interfaces.IListController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public abstract class DataAnalysisReceived<D> {
    private LocalDate date;
    private Integer amount;
    private boolean received = false;
    public abstract String getDescription();
    public abstract String getImagePath();
    public abstract String getClasification();
    public abstract void setObject(D object);
    public abstract Integer getId();
    public abstract String getRoute();
    public abstract D getObject();


}
