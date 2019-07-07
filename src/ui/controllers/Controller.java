package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Cette classe abstraite repr�sente un contr�leur.
 *
 * @author Guillaume Vivies
 *
 */
public abstract class Controller {

  /**
   * Le Pane racine de la vue associ�e au contr�leur.
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
   * Permet d'initialiser un contr�leur.
   */
  public abstract void init();

  /**
   * M�thode optionnelle appel�e lors de l'affichage d'un �cran.
   */
  public void show() {
  }

}
