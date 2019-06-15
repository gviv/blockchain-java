package ui.components;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import ui.animation.Animator;
import ui.animation.Animator.Transitions;
import ui.resources.icons.Icon;
import javafx.geometry.Pos;

/**
 * Cette classe représente un sélecteur de nombres.
 *
 * @author Guillaume Vivies
 *
 */
public class NumberPicker extends VBox {

  /**
   * Zone de texte contenant le nombre.
   */
  private TextField textField;

  /**
   * Bouton du haut.
   */
  private IconButton up;

  /**
   * Bouton du bas.
   */
  private IconButton down;

  /**
   * Valeur minimale.
   */
  private int minValue;

  /**
   * Valeur maximale.
   */
  private int maxValue;

  /**
   * Le NumberPickerAnimator prenant en charge l'animation.
   */
  private Animator.NumberPickerAnimator animator = new Animator.NumberPickerAnimator();

  /**
   * Constructeur de NumberPicker.
   */
  public NumberPicker() {
    // Création du TextField
    textField = new TextField();
    textField.getStyleClass().add("number-picker__text-field");
    textField.setMaxWidth(60d);

    textField.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.UP) {
        Animator.IconButtonAnimator.mousePressed(up);
        incrementValue();
      } else if (e.getCode() == KeyCode.DOWN) {
        Animator.IconButtonAnimator.mousePressed(down);
        decrementValue();
      }
    });

    textField.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.UP) {
        Animator.IconButtonAnimator.mouseReleased(up, true);
      } else if (e.getCode() == KeyCode.DOWN) {
        Animator.IconButtonAnimator.mouseReleased(down, true);
      }
    });

    // Création du bouton du haut
    SVGPath upArrow = new SVGPath();
    upArrow.setContent(Icon.UP_ARROW);
    up = new IconButton(upArrow);
    up.setMinWidth(65d);
    up.setMinHeight(35d);
    up.setOnMouseClicked(e -> incrementValue());

    // Création du bouton du bas
    SVGPath downArrow = new SVGPath();
    downArrow.setContent(Icon.DOWN_ARROW);
    down = new IconButton(downArrow);
    down.setMinWidth(65d);
    down.setMinHeight(35d);
    down.setOnMouseClicked(e -> decrementValue());

    // Initialisation aux valeurs par défaut
    setMinValue(1);
    setMaxValue(999);
    setValue(1);

    // Création du rectangle qui se fait passer pour un champ de texte
    Rectangle rect = new Rectangle(65d, 35d, Color.valueOf("#fafafa"));
    rect.setArcWidth(15d);
    rect.setArcHeight(15d);
    rect.setStroke(Color.valueOf("#cccccc"));
    rect.setStrokeWidth(1d);

    this.getChildren().addAll(up, new StackPane(rect, textField), down);
    this.setAlignment(Pos.CENTER);
  }

  /**
   * Getter minValue.
   *
   * @return minValue
   */
  public int getMinValue() {
    return minValue;
  }

  /**
   * Getter maxValue.
   *
   * @return maxValue
   */
  public int getMaxValue() {
    return maxValue;
  }

  /**
   * Récupère le nombre actuellement affiché.
   *
   * @return le nombre actuel ou -1 en cas d'erreur.
   */
  public int getValue() {
    int value = 0;

    try {
      value = Integer.parseInt(textField.getText());
    } catch (NumberFormatException e) {
      return -1;
    }

    return value;
  }

  /**
   * Setter minValue.
   *
   * @param v minValue
   */
  public void setMinValue(int v) {
    this.minValue = v;
  }

  /**
   * Setter maxValue.
   *
   * @param v maxValue
   */
  public void setMaxValue(int v) {
    this.maxValue = v;
  }

  /**
   * Permet de changer le nombre affiché.
   *
   * @param v le nouveau nombre à afficher.
   */
  public void setValue(int v) {
    this.textField.setText(String.valueOf(v));
  }

  /**
   * Réinitialise le NumberPicker en passant sa valeur actuelle au minimum.
   */
  public void reset() {
    this.setValue(this.minValue);
  }

  /**
   * Incrémente le nombre affiché.
   */
  private void incrementValue() {
    int nb = getValue();

    if (nb < maxValue) {
      if (nb < minValue) {
        animator.changeNumber(textField, Transitions.FADE_UP, 80d, e -> {
          setValue(minValue + 1);
        });
      } else {
        animator.changeNumber(textField, Transitions.FADE_UP, 80d, e -> {
          setValue(nb + 1);
        });
      }
    } else if (nb != maxValue) {
      animator.changeNumber(textField, Transitions.FADE_UP, 80d, e -> {
        setValue(maxValue);
      });
    }
  }

  /**
   * Décrémente le nombre affiché.
   */
  private void decrementValue() {
    int nb = getValue();

    if (nb > minValue) {
      if (nb > maxValue) {
        animator.changeNumber(textField, Transitions.FADE_DOWN, 80d, e -> {
          setValue(maxValue - 1);
        });
      } else {
        animator.changeNumber(textField, Transitions.FADE_DOWN, 80d, e -> {
          setValue(nb - 1);
        });
      }
    } else if (nb != minValue) {
      animator.changeNumber(textField, Transitions.FADE_DOWN, 80d, e -> {
        setValue(minValue);
      });
    }
  }

}
