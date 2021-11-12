module Project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires java.naming;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swt;

    opens com to javafx.fxml;
    exports com;
}
