package ui.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import ui.Main;
import ui.views.Screen;

/**
 * Cette classe permet de regrouper et rendre accessible de n'importe où les
 * différents contrôleurs.
 *
 * @author Guillaume Vivies
 *
 */
public class ControllerManager {

  /**
   * Application principale.
   */
  private static Main app;

  /**
   * Stocke les contrôleurs.
   */
  private static Map<String, Controller> controllers = new HashMap<String, Controller>();

  /**
   * Constructeur de ControllerManager.
   *
   * @param app l'application principale
   */
  public ControllerManager(Main app) {
    ControllerManager.app = app;

    // Chargement des contrôleurs
    String name;
    for (Screen s : Screen.values()) {
      name = s.toString();
      controllers.put(name, loadFxml(toFilename(name)).getController());
    }
  }

  /**
   * Permet de récupérer le contrôleur associé à un Screen. On utilise un
   * générique afin de caster directement le contrôleur en contrôleur plus
   * spécialisé (évite de devoir caster lors des appels, qui sont déjà assez
   * longs).
   *
   * @param screen le Screen dont on veut récupérer le contrôleur
   * @return le contrôleur associé à screen
   */
  @SuppressWarnings("unchecked")
  public static <T> T getController(Screen screen) {
    return (T) controllers.get(screen.toString());
  }

  /**
   * Getter app.
   *
   * @return app
   */
  public static Main getApp() {
    return ControllerManager.app;
  }

  /**
   * Permet d'initialiser les contrôleurs.
   */
  public void initControllers() {
    controllers.forEach((k, c) -> c.init());
  }

  /**
   * Permet de charger le fichier FXML dont le nom est passé en paramètre.
   *
   * @param filename le nom du fichier à charger
   * @return le FXMLLoader associé au fichier
   */
  private FXMLLoader loadFxml(String filename) {
    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(Main.class.getResource(filename));
    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return loader;
  }

  /**
   * Permet de transformer une chaîne de caractère de la forme "ABC_DEF" en
   * "views/AbcDef.fxml".
   *
   * @param str la chaîne à transformer
   * @return la chaîne transformée
   */
  private String toFilename(String str) {
    String filename = "views/";
    String[] exploded = str.split("_");

    for (String s : exploded) {
      filename += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    filename += ".fxml";

    return filename;
  }

}
