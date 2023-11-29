module HarmonyHub{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens HarmonyHub to javafx.fxml;
    opens HarmonyHub.GUI.Controllers to javafx.fxml;
    exports HarmonyHub;

    opens HarmonyHub.Domain to javafx.base;
    exports HarmonyHub.GUI;
    exports HarmonyHub.GUI.Events;
    opens HarmonyHub.GUI.Events to javafx.fxml;
}