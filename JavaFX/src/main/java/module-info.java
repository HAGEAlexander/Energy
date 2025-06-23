module com.groupx.gui {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // HTTP client for REST calls
    requires java.net.http;

    // Jackson for JSON binding
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Allow FXMLLoader to instantiate @FXML controllers
    opens com.groupx.gui.controller to javafx.fxml;

    // Allow Jackson (and JavaFX Property support) to access model fields
    opens com.groupx.gui.model to com.fasterxml.jackson.databind, javafx.base;

    // Export the root package so the plugin/runtime can find your MainApp
    exports com.groupx.gui;
}
