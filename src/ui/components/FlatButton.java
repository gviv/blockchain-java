package ui.components;

import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import ui.animation.Animator;

/**
 * Cette classe représente un bouton au design "flat" et doté d'animations.
 *
 * @author Guillaume Vivies
 *
 */
public class FlatButton extends Button {

  /**
   * Couleur de base.
   */
  public static final Color BASE_COLOR = Color.valueOf("#efefef");

  /**
   * Couleur au survol.
   */
  public static final Color HOVERED_COLOR = Color.valueOf("#0060ff");

  /**
   * Rayon de courbure du bouton.
   */
  public static final CornerRadii BORDER_RADIUS = new CornerRadii(10d);

  /**
   * Animation lors du survol.
   */
  private Transition hoveredAnimation;

  /**
   * Animation lors de la sortie du survol.
   */
  private Transition hoveredOutAnimation;

  /**
   * Constructeur de FlatButton.
   */
  public FlatButton() {
    // Récupération des animations
    hoveredAnimation = Animator.FlatButtonAnimator.getHoveredAnimation(this, BASE_COLOR, HOVERED_COLOR, BORDER_RADIUS);
    hoveredOutAnimation = Animator.FlatButtonAnimator.getHoveredAnimation(this, HOVERED_COLOR, BASE_COLOR,
        BORDER_RADIUS);

    // Mise en place des couleurs initiales
    this.setBackground(new Background(new BackgroundFill(BASE_COLOR, BORDER_RADIUS, Insets.EMPTY)));
    this.setTextFill(HOVERED_COLOR);

    this.setMinHeight(30d);
    this.getStyleClass().setAll("flat-button");

    this.setOnMouseEntered(e -> hoveredAnimation.playFromStart());
    this.setOnMouseExited(e -> hoveredOutAnimation.playFromStart());
  }

}
