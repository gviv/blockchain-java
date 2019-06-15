package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Cette classe abstraite représente un contrôleur.
 *
 * @author Guillaume Vivies
 *
 */
public abstract class Controller {

  /**
   * Le Pane racine de la vue associée au contrôleur.
   */
  @FXML
  protected Pane root;

  /**
   * Getter root.
   *
   * @return root
   */
  public Pane getRoot() {
    return root;
  }

  /**
   * Permet d'initialiser un contrôleur.
   */
  public abstract void init();

  /**
   * Méthode optionnelle appelée lors de l'affichage d'un écran.
   */
  public void show() {
  }

}
