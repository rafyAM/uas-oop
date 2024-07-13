module rafy.uas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens rafy.uas to javafx.fxml;
    opens rafy.uas.controller to javafx.fxml;
    opens rafy.uas.model to javafx.base;
    
    exports rafy.uas;
    exports rafy.uas.controller;
}
