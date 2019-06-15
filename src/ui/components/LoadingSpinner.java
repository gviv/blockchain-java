package ui.components;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import ui.animation.Animator;

/**
 * Cette classe représente un spinner de chargement.
 *
 * @author Guillaume Vivies
 *
 */
public class LoadingSpinner extends StackPane {

  /**
   * Le rayon du spinner.
   */
  private static final double SPINNER_RADIUS = 30d;

  /**
   * La largeur du spinner.
   */
  private static final double SPINNER_STROKE_WIDTH = 6d;

  /**
   * Les couleurs du spinner.
   */
  private static final Color[] colors = new Color[] { Color.valueOf("#7700ff"), Color.valueOf("#00cc03"),
      Color.valueOf("#ff6e00") };

  /**
   * L'arc de cercle.
   */
  private Arc spinner;

  /**
   * La Timeline de l'animation.
   */
  private Timeline timeline;

  /**
   * Constructeur de LoadingSpinner.
   */
  public LoadingSpinner() {
    spinner = new Arc(SPINNER_RADIUS + SPINNER_STROKE_WIDTH / 2, SPINNER_RADIUS + SPINNER_STROKE_WIDTH / 2,
        SPINNER_RADIUS, SPINNER_RADIUS, 0d, 15d);
    Rectangle rect = new Rectangle(SPINNER_RADIUS * 2 + SPINNER_STROKE_WIDTH,
        SPINNER_RADIUS * 2 + SPINNER_STROKE_WIDTH);
    rect.setFill(Color.TRANSPARENT);

    spinner.setStrokeWidth(SPINNER_STROKE_WIDTH);
    spinner.setStrokeLineCap(StrokeLineCap.ROUND);
    spinner.setStroke(colors[0]);
    spinner.setFill(Color.TRANSPARENT);

    this.getChildren().add(new Group(rect, spinner));
    timeline = Animator.LoadingSpinnerAnimator.getAnimationTimeline(spinner, this, colors);
    timeline.play();
  }

  /**
   * Lance l'animation du spinner.
   */
  public void spin() {
    timeline.stop();
    timeline.playFromStart();
  }

}
