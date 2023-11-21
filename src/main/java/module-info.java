module group.socialapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens group.socialapp to javafx.fxml;
    opens group.socialapp.GUI to javafx.fxml;
    exports group.socialapp;

    opens group.socialapp.Domain to javafx.base;
    exports group.socialapp.GUI;
    exports group.socialapp.GUI.Controller;
    opens group.socialapp.GUI.Controller to javafx.fxml;
    exports group.socialapp.GUI.Events;
    opens group.socialapp.GUI.Events to javafx.fxml;
}