package ui.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import ui.Main;
import ui.views.Screen;

/**
 * Cette classe permet de regrouper et rendre accessible de n'importe o� les
 * diff�rents contr�leurs.
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
   * Stocke les contr�leurs.
   */
  private static Map<String, Controller> controllers = new HashMap<String, Controller>();

  /**
   * Constructeur de ControllerManager.
   *
   * @param app l'application principale
   */
  public ControllerManager(Main app) {
    ControllerManager.app = app;

    // Chargement des contr�leurs
    String name;
    for (Screen s : Screen.values()) {
      name = s.toString();
      controllers.put(name, loadFxml(toFilename(name)).getController());
    }
  }

  /**
   * Permet de r�cup�rer le contr�leur associ� � un Screen. On utilise un
   * g�n�rique afin de caster directement le contr�leur en contr�leur plus
   * sp�cialis� (�vite de devoir caster lors des appels, qui sont d�j� assez
   * longs).
   *
   * @param screen le Screen dont on veut r�cup�rer le contr�leur
   * @return le contr�leur associ� � screen
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
   * Permet d'initialiser les contr�leurs.
   */
  public void initControllers() {
    controllers.forEach((k, c) -> c.init());
  }

  /**
   * Permet de charger le fichier FXML dont le nom est pass� en param�tre.
   *
   * @param filename le nom du fichier � charger
   * @return le FXMLLoader associ� au fichier
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
   * Permet de transformer une cha�ne de caract�re de la forme "ABC_DEF" en
   * "views/AbcDef.fxml".
   *
   * @param str la cha�ne � transformer
   * @return la cha�ne transform�e
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
