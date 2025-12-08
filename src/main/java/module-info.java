module poco.company.group01pocolib {
    requires javafx.controls;
    requires javafx.fxml;
    requires poco.company.group01pocolib;
    requires javafx.graphics;


    exports poco.company.group01pocolib;
    opens poco.company.group01pocolib to javafx.fxml;
}