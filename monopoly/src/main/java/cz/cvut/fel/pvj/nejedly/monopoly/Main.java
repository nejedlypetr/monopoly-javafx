package cz.cvut.fel.pvj.nejedly.monopoly;

import cz.cvut.fel.pvj.nejedly.monopoly.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.*;

public class Main extends Application {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * The main entry point for the application.
     * <p>This method initializes the logger and launches the application. After the
     * application has finished running, the logger is closed.</p>
     *
     * @param args the command-line arguments passed to the application
     * @throws IOException if there is an error configuring the logger
     */
    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = configureLogger(args);
        LOGGER.info("Starting application...");

        launch(args);

        LOGGER.info("Application finished.");
        fileHandler.close();
    }

    /**
     * {@inheritDoc}
     * <p>This method creates a new {@link GameController} instance and initializes
     * the game.</p>
     *
     * @param primaryStage the primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        GameController gameController = new GameController();
        gameController.initialize();
    }

    private static FileHandler configureLogger(String[] args) throws IOException {
        Level level = Level.OFF; // default log level
        if (args.length > 0) level = Level.parse(args[0]);

        FileHandler fileHandler =  new FileHandler("log.txt");
        fileHandler.setFormatter(new SimpleFormatter());
        fileHandler.setLevel(level);

        LogManager.getLogManager().getLogger("").setLevel(level); // set log level to all loggers
        LogManager.getLogManager().getLogger("").addHandler(fileHandler); // add handler to all loggers

        return fileHandler;
    }
}
