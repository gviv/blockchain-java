package ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import ui.animation.Animator;

/**
 * Cette classe permet de représenter visuellement un bloc.
 *
 * @author Guillaume Vivies
 *
 */
public class VisualBlock extends Label {

  /**
   * Hauteur du bloc.
   */
  public static final double HEIGHT = 70d;

  /**
   * Largeur du bloc.
   */
  public static final double WIDTH = 70d;

  /**
   * États possible du bloc.
   */
  public static enum State {
    VALID, INVALID, UNCHECKED
  };

  /**
   * Indice du bloc.
   */
  private int index;

  /**
   * Constructeur de VisualBlock.
   *
   * @param index l'indice du bloc
   */
  public VisualBlock(int index) {
    super("#" + index);
    this.index = index;
    this.setPrefWidth(WIDTH);
    this.setPrefHeight(HEIGHT);
    this.setAlignment(Pos.CENTER);
    this.setState(State.UNCHECKED);
    this.setOnMouseEntered(e -> Animator.VisualBlockAnimator.onHover(this, 100d));
    this.setOnMouseExited(e -> Animator.VisualBlockAnimator.onHoverOut(this, 100d));
    this.setOnMousePressed(e -> Animator.VisualBlockAnimator.onHoverOut(this, 100d));
    this.setCache(true);
    this.setCacheShape(true);
  }

  /**
   * Constructeur de VisualBlock permettant la duplication d'un VisualBlock.
   *
   * @param vb le VisualBlock à dupliquer
   */
  public VisualBlock(VisualBlock vb) {
    super("#" + vb.getIndex());
    index = vb.index;
    this.setPrefWidth(WIDTH);
    this.setPrefHeight(HEIGHT);
    this.setAlignment(Pos.CENTER);
    this.getStyleClass().setAll(vb.getStyleClass());
    this.setCache(true);
    this.setCacheShape(true);
  }

  /**
   * Getter index.
   *
   * @return index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Permet de changer l'état du bloc.
   *
   * @param s le nouvel état
   */
  public void setState(State s) {
    this.getStyleClass().setAll("block", "block--" + s.toString().toLowerCase());
  }

}
