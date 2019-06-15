package ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Cette classe représente la zone dans laquelle vont se trouver les détails des
 * transactions.
 *
 * @author Guillaume Vivies
 *
 */
public class TransactionsPane extends VBox {

  /**
   * Constructeur de TransactionsPane.
   *
   * @param index     l'indice de la transaction
   * @param timestamp le timestamp de la transaction
   * @param sender    l'adresse de l'émetteur de la transaction
   * @param receiver  l'adresse du récepteur de la transaction
   * @param amount    le montant de la transaction
   * @param signature la signature de la transaction
   */
  public TransactionsPane(String index, String timestamp, String sender, String receiver, String amount,
      String signature) {
    // Affichage de l'indice de la transaction
    Label i = new Label("Transaction #");
    Label indexLabel = new Label(index);
    HBox hbox = new HBox(i, indexLabel);
    hbox.getStyleClass().add("transaction-index");

    // Création des labels
    Label t = new Label("TIMESTAMP");
    Label s = new Label("ÉMETTEUR");
    Label r = new Label("DESTINATAIRE");
    Label a = new Label("MONTANT");
    Label si = new Label("SIGNATURE");
    t.getStyleClass().add("block-overview__label");
    s.getStyleClass().add("block-overview__label");
    r.getStyleClass().add("block-overview__label");
    a.getStyleClass().add("block-overview__label");
    si.getStyleClass().add("block-overview__label");

    // Création des SelectableLabels affichant les informations
    double fontSize = 7.26;
    SelectableLabel labelTimestamp = new SelectableLabel(timestamp, fontSize);
    SelectableLabel labelSender = new SelectableLabel(sender, fontSize);
    SelectableLabel labelReceiver = new SelectableLabel(receiver, fontSize);
    SelectableLabel labelAmount = new SelectableLabel(amount, fontSize);
    SelectableLabel labelSignature = new SelectableLabel(signature, fontSize);

    // On met les labels dans une VBox
    VBox v = new VBox(t, s, r, a, si);
    v.setSpacing(1.0);
    v.setAlignment(Pos.CENTER_RIGHT);

    VBox labels = new VBox(labelTimestamp, labelSender, labelReceiver, labelAmount, labelSignature);
    labels.setSpacing(3.0);
    labels.setAlignment(Pos.CENTER_LEFT);
    labels.setFillWidth(false);

    HBox h = new HBox(v, labels);
    h.setSpacing(5.0);

    this.getChildren().addAll(hbox, h);
  }

}
