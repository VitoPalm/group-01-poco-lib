package poco.company.group01pocolib.mvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

import java.io.IOException;

public class PocoLibApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL urlRisorsa = getClass().getResource("/poco/company/group01pocolib/Mockup.fxml");
        Parent root = FXMLLoader.load(urlRisorsa);
        stage.setScene(new Scene(root));
        stage.setTitle("Mockup");
        stage.show();
    }
}
