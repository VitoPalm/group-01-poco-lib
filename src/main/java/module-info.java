module poco.company.group01pocolib {
    requires javafx.controls;
    requires javafx.fxml;


    exports poco.company.group01pocolib.mvc;
    opens poco.company.group01pocolib.mvc to javafx.fxml;
}