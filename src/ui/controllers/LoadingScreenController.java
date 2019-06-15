package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import ui.components.LoadingSpinner;
import ui.views.Screen;

/**
 * Cette classe représente le contrôleur de la vue LoadingScreen.
 *
 * @author Guillaume Vivies
 *
 */
public class LoadingScreenController extends Controller {

  /**
   * Le texte affiché.
   */
  @FXML
  private Text text;

  /**
   * Le "rond de chargement".
   */
  @FXML
  private LoadingSpinner loadingSpinner;

  /**
   * Permet de changer le texte affiché.
   *
   * @param text le nouveau texte à afficher
   */
  public void setText(String text) {
    this.text.setText(text);
  }

  /**
   * Initialise le texte affiché.
   *
   * @see Controller#init()
   */
  public void init() {
    setText("");
  }

  /**
   * Lance l'animation du spinner et affiche le bouton retour.
   *
   * @see Controller#show()
   */
  public void show() {
    loadingSpinner.spin();
    ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT).showBackButton("Annuler");
  }

}
