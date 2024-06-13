module group.gmae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens pac to javafx.fxml;
    exports pac;

}