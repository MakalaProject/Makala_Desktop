module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.net.http;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires org.apache.httpcomponents.httpclient;
    requires cloudinary.http44;
    requires cloudinary.core;
    requires lombok;

    opens org.example;
    opens org.example.controllers;
    opens org.example.customCells;
    opens org.example.customDialogs;
    opens org.example.services;
    opens org.example.productsSubControllers;
    opens org.example.model;
    opens org.example.model.products;
    opens org.example.interfaces;
    opens org.example.model.Adress;

    exports org.example;
    exports org.example.controllers;
    exports org.example.customCells;
    exports org.example.customDialogs;
    exports org.example.services;
    exports org.example.productsSubControllers;
    exports org.example.model;
    exports org.example.model.products;
    exports org.example.model.Adress;
    exports org.example.interfaces;

}