package ui;

import blockchain.Blockchain;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.controllers.Controller;
import ui.controllers.ControllerManager;
import ui.views.Screen;

/**
 * Classe principale de l'application.
 *
 * @author Guillaume Vivies
 *
 */
public class Main extends Application {

  /**
   * La blockchain actuellement chargée.
   */
  private Blockchain blockchain;

  /**
   * Getter blockchain.
   *
   * @return blockchain
   */
  public Blockchain getBlockchain() {
    return this.blockchain;
  }

  /**
   * Setter blockchain.
   *
   * @param blockchain la blockchain
   */
  public void setBlockchain(Blockchain blockchain) {
    this.blockchain = blockchain;
  }

  @Override
  public void start(Stage primaryStage) {
    // Création du ControllerManager
    ControllerManager cm = new ControllerManager(this);

    // Création de la scène
    Scene s;
    s = new Scene(ControllerManager.<Controller>getController(Screen.ROOT_LAYOUT).getRoot());
    s.getStylesheets().add(Main.class.getResource("views/style.css").toExternalForm());

    // Configuration de la fenêtre principale
    primaryStage.setScene(s);
    primaryStage.setWidth(854);
    primaryStage.setHeight(540);
    primaryStage.setTitle("Projet Blockchain");
    primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/icon.png")));
    primaryStage.show();

    // Initialisation des contrôleurs
    cm.initControllers();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
