package ui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import ui.animation.Animator;
import ui.resources.icons.Icon;

/**
 * Cette classe représente une barre d'informations.
 *
 * @author Guillaume Vivies
 *
 */
public class InfoBar extends StackPane {

  /**
   * Hauteur de la barre.
   */
  private static final double HEIGHT = 100.0;

  /**
   * Le texte affiché sur la barre.
   */
  private Label text;

  /**
   * Stocke si la barre est visible.
   */
  private boolean visible;

  /**
   * Le SVG de l'icône de la croix.
   */
  private SVGPath cross;

  /**
   * Le SVG de l'icône du "tick"
   */
  private SVGPath tick;

  /**
   * Le SVG de l'icône d'information.
   */
  private SVGPath info;

  /**
   * Types d'informations pris en charge.
   */
  public static enum InfoType {
    DEFAULT, SUCCESS, ERROR
  };

  /**
   * Constructeur de InfoBar.
   */
  public InfoBar() {
    text = new Label();

    // Création de l'icône de la croix
    cross = new SVGPath();
    cross.setContent(Icon.CROSS);
    cross.setScaleX(2);
    cross.setScaleY(2);
    cross.fillProperty().bind(text.textFillProperty());

    // Création du l'icône du "tick"
    tick = new SVGPath();
    tick.setContent(Icon.TICK);
    tick.setScaleX(2);
    tick.setScaleY(2);
    tick.fillProperty().bind(text.textFillProperty());

    // Création de l'icône d'information
    info = new SVGPath();
    info.setContent(Icon.INFO);
    info.setScaleX(2);
    info.setScaleY(2);
    info.fillProperty().bind(text.textFillProperty());

    this.setMaxHeight(HEIGHT);
    text.setGraphicTextGap(20.0);

    this.getChildren().add(text);
    this.setInfoType(InfoType.DEFAULT);
    this.reset();
  }

  /**
   * Setter text.
   *
   * @param text le texte à afficher
   */
  public void setText(String text) {
    this.text.setText(text);
  }

  /**
   * Permet de changer le type d'information affiché par la barre.
   *
   * @param t le type d'information
   */
  public void setInfoType(InfoType t) {
    String str = t.toString().toLowerCase();

    switch (t) {
    case DEFAULT:
      text.setGraphic(info);
      break;
    case SUCCESS:
      text.setGraphic(tick);
      break;
    case ERROR:
      text.setGraphic(cross);
      break;
    }

    this.getStyleClass().setAll("info-bar", "info-bar--" + str);
    text.getStyleClass().setAll("info-bar__text", "info-bar__text--" + str);
  }

  /**
   * Permet de réinitialiser la barre.
   */
  private void reset() {
    this.text.setText("");
    this.setTranslateY(this.getMaxHeight());
  }

  /**
   * Permet de masquer la barre.
   *
   * @param delay le délai de l'animation
   */
  private void hide(double delay) {
    Animator.InfoBarAnimator.hide(this, 500d, delay, a -> {
      this.reset();
      this.visible = false;
    });
  }

  /**
   * Permet d'afficher la barre.
   *
   * @param hideAfter la durée après laquelle la barre va se masquer
   * @param delay     le délai de l'animation
   */
  public void show(double hideAfter, double delay) {
    if (!this.visible) {
      Animator.InfoBarAnimator.show(this, 500d, delay, a -> {
        this.hide(hideAfter);
      });
      this.visible = true;
    }
  }

}
