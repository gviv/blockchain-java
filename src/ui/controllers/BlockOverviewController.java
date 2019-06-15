package ui.controllers;

import blockchain.Block;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import ui.animation.Animator;
import ui.animation.Animator.Transitions;
import ui.components.IconButton;
import ui.components.SelectableLabel;
import ui.components.TransactionsPane;
import ui.resources.icons.Icon;
import ui.views.Screen;

/**
 * Cette classe représente le contrôleur de la vue BlockOverview.
 *
 * @author Guillaume Vivies
 *
 */
public class BlockOverviewController extends Controller {

  /**
   * Le Pane contenant les boutons gauche/droite et blockDetails.
   */
  @FXML
  private BorderPane borderPane;

  /**
   * Le Pane affichant les détails du bloc.
   */
  @FXML
  private VBox blockDetails;

  /**
   * Le Label contenant l'indice du bloc.
   */
  @FXML
  private Label labelId;

  /**
   * Le SelectableLabel contenant le hash du bloc.
   */
  @FXML
  private SelectableLabel labelHash;

  /**
   * Le SelectableLabel contenant le hash du bloc précédent.
   */
  @FXML
  private SelectableLabel labelPreviousHash;

  /**
   * Le SelectableLabel contenant le timestamp.
   */
  @FXML
  private SelectableLabel labelTimestamp;

  /**
   * Le SelectableLabel contenant le Merkle Root.
   */
  @FXML
  private SelectableLabel labelMerkleRoot;

  /**
   * Le SelectableLabel contenant la nonce.
   */
  @FXML
  private SelectableLabel labelNonce;

  /**
   * Le SelectableLabel contenant le nombre de transactions.
   */
  @FXML
  private SelectableLabel labelNbTransactions;

  /**
   * La ListView contenant les transactions.
   */
  @FXML
  private ListView<TransactionsPane> transactionsPaneContainer;

  /**
   * Le bouton permettant de passer au bloc précédent.
   */
  private IconButton leftButton;

  /**
   * Le bouton permettant de passer au bloc suivant.
   */
  private IconButton rightButton;

  /**
   * Le bloc de la blockchain actuellement affiché.
   */
  private Block block;

  /**
   * L'animator prenant en charge l'animation.
   */
  private Animator.SimpleTransition animator;

  /**
   * Permet de changer le bloc affiché en actualisant les champs correspondants.
   *
   * @param block le nouveau bloc à afficher
   */
  public void setBlock(Block block) {
    // On met à jour les champs
    this.block = block;
    this.labelId.setText(String.valueOf(block.getIndex()));
    this.labelTimestamp.setText(String.valueOf(block.getTimestamp()));
    this.labelHash.setText(String.valueOf(block.getHash()));
    this.labelPreviousHash.setText(String.valueOf(block.getPreviousHash()));
    this.labelMerkleRoot.setText(String.valueOf(block.getMerkleRoot()));
    this.labelNonce.setText(String.valueOf(block.getNonce()));
    this.labelNbTransactions.setText(String.valueOf(block.getNbTransactions()));

    // Affichage des transactions
    ObservableList<TransactionsPane> transactions = FXCollections.observableArrayList();
    block.getTransactions().forEach(t -> {
      String index = String.valueOf(t.getIndex());
      String timestamp = t.getTimestamp();
      String sender = t.getSender();
      String receiver = t.getReceiver();
      String amount = String.valueOf(t.getAmount());
      String signature = t.getSignature();
      transactions.add(new TransactionsPane(index, timestamp, sender, receiver, amount, signature));
    });
    transactionsPaneContainer.setItems(transactions);
  }

  /**
   * Crée les boutons gauche/droite et redimensionne le transactionsPaneContainer.
   *
   * @see Controller#init()
   */
  public void init() {
    this.animator = new Animator.SimpleTransition();

    // Création du bouton de gauche
    SVGPath leftArrow = new SVGPath();
    leftArrow.setContent(Icon.LEFT_ARROW);
    leftArrow.setScaleX(3.0);
    leftArrow.setScaleY(3.0);
    leftButton = new IconButton(leftArrow);
    leftButton.setOnAction(e -> showPreviousBlock());
    leftButton.setPrefWidth(50d);
    leftButton.prefHeightProperty().bind(borderPane.heightProperty());
    BorderPane.setAlignment(leftButton, Pos.CENTER);
    borderPane.setLeft(leftButton);

    // Création du bouton de droite
    SVGPath rightArrow = new SVGPath();
    rightArrow.setContent(Icon.RIGHT_ARROW);
    rightArrow.setScaleX(3.0);
    rightArrow.setScaleY(3.0);
    rightButton = new IconButton(rightArrow);
    rightButton.setOnAction(e -> showNextBlock());
    rightButton.setPrefWidth(50d);
    rightButton.prefHeightProperty().bind(borderPane.heightProperty());
    BorderPane.setAlignment(rightButton, Pos.CENTER);
    borderPane.setRight(rightButton);

    // Redimensionnement du transactionsPaneContainer pour qu'il occupe tout
    // l'espace horizontal
    root.widthProperty().addListener(e -> {
      double width = ((Pane) borderPane.getCenter()).getWidth();
      if (width == 0.0)
        width = 740.0;
      transactionsPaneContainer.setPrefWidth(width - 39);
    });
  }

  /**
   * Configure le bouton retour.
   *
   * @see Controller#show()
   */
  public void show() {
    root.requestFocus();
    // Configuration du bouton retour
    RootLayoutController c = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);
    c.setOnBackButtonClick(v -> {
      c.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_BACK, 200d, 0d);
    });
    c.showBackButton("Retour");
  }

  /**
   * Permet d'afficher les détails du bloc précédent.
   */
  private void showPreviousBlock() {
    int newIndex = block.getIndex() - 1;

    showOtherBlock(newIndex, Transitions.FADE_RIGHT);
  }

  /**
   * Permet d'afficher les détails du bloc suivant.
   */
  private void showNextBlock() {
    int newIndex = block.getIndex() + 1;

    showOtherBlock(newIndex, Transitions.FADE_LEFT);
  }

  /**
   * Permet d'afficher les détails du bloc dont l'indice est passé en paramètre.
   *
   * @param index      l'indice du bloc dont on veut afficher les détails
   * @param transition la transition
   */
  private void showOtherBlock(int index, Transitions transition) {
    // Si l'indice est valide
    if (index >= 0 && index < ControllerManager.getApp().getBlockchain().getNbBlocks()) {
      animator.contentTransition(blockDetails, blockDetails, transition, 150d, 0d, e -> {
        // On affiche le nouveau bloc
        setBlock(ControllerManager.getApp().getBlockchain().getBlock(index));
        // On informe le BlockchainOverviewController que le bloc cliqué a changé
        ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
            .setBlockClicked(index);
      });
    }
  }

}
