package com.grupo06;

import com.grupo06.mvc.*;

/**
 * Entry point for the SpotifUM application.
 * Initializes the MVC components and starts the user interface.
 *
 * This class sets up the model, controller, and view, then runs the
 * welcome screen followed by the main application loop.
 *
 *
 * @author Grupo 06 (Francisco Barbosa, Pedro Morais, Simão Araújo)
 * @version 1.0
 */

public class SpotifUM {
    /**
     * Main method that launches the SpotifUM application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        SpotifUMModel model = new SpotifUMModel();
        SpotifUMController controller = new SpotifUMController(model);
        SpotifUMView view = new SpotifUMView(controller);
        view.runWelcome();
        view.run();
    }
}
