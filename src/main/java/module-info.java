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
    opens org.example.controllers.create.controllers;
    opens org.example.controllers.list.controllers;
    opens org.example.controllers.parent.controllers;
    opens org.example.customCells;
    opens org.example.controllers.elements.controllers;
    opens org.example.services;
    opens org.example.controllers.products.subcontrollers;
    opens org.example.model;
    opens org.example.model.products;
    opens org.example.interfaces;
    opens org.example.model.Adress;
    opens org.example.controllers.gift.subcontrollers;
    opens org.example.exceptions;
    opens org.example.controllers.client.subcontrollers;


    exports org.example;
    exports org.example.controllers;
    exports org.example.customCells;
    exports org.example.controllers.elements.controllers;
    exports org.example.services;
    exports org.example.controllers.products.subcontrollers;
    exports org.example.model;
    exports org.example.model.products;
    exports org.example.model.Adress;
    exports org.example.interfaces;
    exports org.example.controllers.create.controllers;
    exports org.example.controllers.list.controllers;
    exports org.example.controllers.parent.controllers;
    exports org.example.controllers.gift.subcontrollers;
    exports org.example.exceptions;
    exports org.example.controllers.client.subcontrollers;
}