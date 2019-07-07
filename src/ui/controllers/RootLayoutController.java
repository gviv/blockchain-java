package ui.controllers;

import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.SVGPath;
import ui.animation.Animator;
import ui.animation.Animator.Transitions;
import ui.components.InfoBar;
import ui.components.InfoBar.InfoType;
import ui.resources.icons.Icon;
import ui.views.Screen;

/**
 * Cette classe repr�sente le contr�leur de la vue RootLayout.
 *
 * @author Guillaume Vivies
 *
 */
public class RootLayoutController extends Controller {

  /**
   * Le contenu principal affich�.
   */
  @FXML
  private BorderPane mainContent;

  /**
   * Le bouton retour.
   */
  @FXML
  private Button backButton;

  /**
   * La barre d'informations.
   */
  @FXML
  private InfoBar infoBar;

  /**
   * L'animator prenant en charge l'animation.
   */
  private Animator.SimpleTransition animator;

  /**
   * L'action � effectuer lors d'un clic sur le bouton retour.
   */
  private Consumer<Void> onBackButtonClick;

  /**
   * Permet de changer le contenu principal affich�.
   *
   * @param screen     l'�cran qu'on veut afficher
   * @param transition la transition
   * @param duration   la dur�e de la transition
   * @param delay      le d�lai de la transition
   */
  public void setMainContent(Screen screen, Transitions transition, double duration, double delay) {
    Controller c = ControllerManager.<Controller>getController(screen);

    animator.setController(c);
    animator.contentTransition(mainContent.getCenter(), c.getRoot(), transition, duration, delay, e -> {
      mainContent.setCenter(c.getRoot());
    });
  }

  /**
   * Setter onBackButtonClick.
   *
   * @param c onBackButtonClick
   */
  public void setOnBackButtonClick(Consumer<Void> c) {
    this.onBackButtonClick = c;
  }

  /**
   * M�thode appel�e automatiquement lors d'un clic sur le bouton retour.
   */
  @FXML
  private void onClick() {
    if (onBackButtonClick != null)
      onBackButtonClick.accept(null);
  }

  /**
   * Affiche l'�cran d'accueil et cr�e le bouton retour et l'animator.
   *
   * @see Controller#init()
   */
  public void init() {
    this.animator = new Animator.SimpleTransition();
    this.setMainContent(Screen.TITLE_SCREEN, Transitions.NONE, 0d, 0d);

    // Cr�ation du bouton retour
    SVGPath left = new SVGPath();
    left.setContent(Icon.LEFT_ARROW);
    left.fillProperty().bind(backButton.textFillProperty());
    backButton.setGraphicTextGap(7d);
    backButton.setGraphic(left);
    backButton.setVisible(false);
  }

  /**
   * Permet d'afficher le bouton retour s'il n'est pas d�j� affich�.
   *
   * @param text le nouveau texte � afficher sur le bouton retour
   */
  public void showBackButton(String text) {
    backButton.setText(text);
    if (!backButton.isVisible()) {
      Animator.BackButtonAnimator.show(backButton);
      backButton.setVisible(true);
    }
  }

  /**
   * Permet de masquer le bouton retour.
   */
  public void hideBackButton() {
    if (backButton.isVisible()) {
      Animator.BackButtonAnimator.hide(backButton, e -> backButton.setVisible(false));
    }
  }

  /**
   * Permet d'afficher la barre d'informations.
   *
   * @param infoType  le type de l'information
   * @param text      le texte � afficher
   * @param hideAfter la dur�e apr�s laquelle la barre va se masquer
   * @param delay     le d�lai de l'animation
   */
  public void showInfoBar(InfoType infoType, String text, double hideAfter, double delay) {
    infoBar.setInfoType(infoType);
    infoBar.setText(text);
    infoBar.show(hideAfter, delay);
  }

}
