module poco.company.group01pocolib {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;

    exports poco.company.group01pocolib.mvc.model;
    exports poco.company.group01pocolib.mvc.controller;
    exports poco.company.group01pocolib.db;
    exports poco.company.group01pocolib.db.omnisearch;
    exports poco.company.group01pocolib.exceptions;
    
    opens poco.company.group01pocolib.mvc.controller to javafx.fxml;
    opens poco.company.group01pocolib.mvc.model to javafx.fxml;
}