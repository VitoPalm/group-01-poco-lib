module poco.company.group01pocolib {
    requires javafx.controls;
    requires javafx.fxml;


    exports poco.company.group01pocolib;
    opens poco.company.group01pocolib to javafx.fxml;
}