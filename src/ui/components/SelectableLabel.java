package ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 * Cette classe repr�sente un Label s�lectionnable.
 *
 * @author Guillaume Vivies
 *
 */
public class SelectableLabel extends TextField {

  /**
   * Taille des marges int�rieures.
   */
  private static final double PADDING = 2.0;

  /**
   * Constructeur de SelectableLabel.
   */
  public SelectableLabel() {
    this(8.24);
  }

  /**
   * Constructeur de SelectableLabel prenant un param�tre la taille de la police.
   * Cette taille doit correspondre � la taille r�elle de la police et ne sert pas
   * � changer la taille affich�e.
   *
   * @param fontSize la taille de la police
   */
  public SelectableLabel(double fontSize) {
    setEditable(false);
    getStyleClass().add("selectable-label");
    setPadding(new Insets(0d, PADDING, 0d, PADDING));
    setAlignment(Pos.CENTER);

    // On redimensionne le label en fonction de la largeur du texte qu'il contient
    textProperty().addListener(e -> setPrefWidth(getText().length() * fontSize + PADDING * 3));
  }

  /**
   * Constructeur de SelectableLabel initialisant le texte affich�.
   *
   * @param text le texte � afficher
   */
  public SelectableLabel(String text, double fontSize) {
    this(fontSize);
    if (text.equals(""))
      text = "     ";
    setText(text);
  }

}
