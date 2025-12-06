/**
 * @file Launcher.java
 * @brief This file contains the Launcher class which starts the PocoLib application.
 * @author Francesco Marino
 * @date 6 December 2025
 */
package poco.company.group01pocolib.mvc;

import javafx.application.Application;
/**
 * @class Launcher
 * @brief The Launcher class is responsible for starting the PocoLib application.
 */
public class Launcher {
    /**
     * @brief Main method to launch the PocoLib application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Application.launch(PocoLibApplication.class, args);
    }
}
