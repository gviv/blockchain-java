package ui.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import ui.animation.Animator;

/**
 * Cette classe représente un FlatButton sans bordure et uniquement composé
 * d'une icône.
 *
 * @author Guillaume Vivies
 *
 */
public class IconButton extends FlatButton {

  /**
   * La couleur de base.
   */
  public static final Color BASE_COLOR = Color.valueOf("#dddddd");

  /**
   * La couleur lors d'un clic.
   */
  public static final Color CLICKED_COLOR = FlatButton.HOVERED_COLOR.deriveColor(0.0, 1.0, 1.0, 0.6);

  /**
   * Constructeur de IconButton.
   *
   * @param svg le SVG correspondant à l'icône voulue
   */
  public IconButton(SVGPath svg) {
    svg.setFill(BASE_COLOR);
    this.setGraphic(svg);
    this.getStyleClass().setAll("icon-button");
    this.setOnMouseEntered(e -> Animator.IconButtonAnimator.hovered(this));
    this.setOnMouseExited(e -> Animator.IconButtonAnimator.hoveredOut(this));
    this.setOnMousePressed(e -> {
      this.requestFocus();
      Animator.IconButtonAnimator.mousePressed(this);
    });
    this.setOnMouseReleased(e -> Animator.IconButtonAnimator.mouseReleased(this, false));
  }

}
