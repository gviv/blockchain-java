package ui.controllers;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.BlockchainGenerator;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import ui.animation.Animator.Transitions;
import ui.components.NumberPicker;
import ui.components.InfoBar.InfoType;
import ui.components.VisualBlock.State;
import ui.views.Screen;

/**
 * Cette classe représente le contrôleur de la vue BlockchainSettings.
 *
 * @author Guillaume Vivies
 *
 */
public class BlockchainSettingsController extends Controller {

  /**
   * Le NumberPicker du nombre de blocs.
   */
  @FXML
  private NumberPicker pickerNbBlocs;

  /**
   * Le NumberPicker de la difficulté.
   */
  @FXML
  private NumberPicker pickerDifficulty;

  /**
   * Le NumberPicker du nombre de transactions.
   */
  @FXML
  private NumberPicker pickerNbTransactions;

  /**
   * Change la valeur minimale du NumberPicker de la difficulté et la valeur
   * maximale du NumberPicker du nombre de transactions.
   *
   * @see Controller#init()
   */
  public void init() {
    pickerDifficulty.setMinValue(0);
    pickerNbTransactions.setMaxValue(10);
  }

  /**
   * Réinitialise les NumberPickers et configure le bouton retour.
   *
   * @see Controller#show()
   */
  public void show() {
    root.requestFocus();

    // Réinitialisation des NumberPickers
    pickerNbBlocs.reset();
    pickerDifficulty.reset();
    pickerNbTransactions.reset();

    // Configuration du bouton retour
    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);
    rlc.setOnBackButtonClick(v -> {
      rlc.setMainContent(Screen.TITLE_SCREEN, Transitions.FADE_RIGHT, 200d, 0d);
    });
    rlc.showBackButton("Accueil");
  }

  /**
   * Permet de générer une blockchain. Cette fonction devrait utiliser la fonction
   * de génération de blockchain
   * ({@link blockchain.BlockchainGenerator#generateBlockchain(int, int, int)})
   * mais cela n'est pas possible, pour les raisons mentionnées ci-dessous.
   */
  @FXML
  private void generateBlockchain() {
    // Récupération des valeurs des NumberPickers
    int nbBlocks = pickerNbBlocs.getValue();
    int difficulty = pickerDifficulty.getValue();
    int nbTransactions = pickerNbTransactions.getValue();

    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);

    // Création du service qui va se charger de la génération
    Service<Blockchain> s = new Service<Blockchain>() {
      @Override
      protected Task<Blockchain> createTask() {
        return new Task<Blockchain>() {
          @Override
          public Blockchain call() {
            /*
             * Ici, il faudrait faire appel à la méthode
             * BlockchainGenerator.generateBlockchain(int, int, int) pour générer la
             * blockchain mais cette méthode ne permet pas de s'arrêter si l'utilisateur le
             * décide. On recopie donc cette méthode, ce qui est "sale" mais cela permet de
             * ne pas polluer le code de la classe BlockchainGenerator avec des
             * considérations qui relèvent de l'interface.
             */
            Blockchain blockchain = new Blockchain(difficulty);
            for (int i = 0; i < nbBlocks - 1; ++i) {
              if (this.isCancelled())
                break;
              Block b = BlockchainGenerator.generateBlock(nbTransactions);
              blockchain.addBlock(b);
            }
            return blockchain;
          }
        };
      }
    };

    // Si échec
    s.setOnFailed(e -> {
      rlc.showInfoBar(InfoType.ERROR, "Une erreur est survenue", 3000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_SETTINGS, Transitions.FADE_BACK, 200d, 0d);
    });

    // Si succès
    s.setOnSucceeded(e -> {
      ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .setBlockchainLoaded(false);
      ControllerManager.getApp().setBlockchain(s.getValue());
      ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .setBlockchainState(State.VALID);
      rlc.showInfoBar(InfoType.SUCCESS, "Génération terminée", 2000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_FRONT, 200d, 0d);
    });

    // Si annulé
    s.setOnCancelled(e -> {
      rlc.showInfoBar(InfoType.DEFAULT, "Génération annulée", 2000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_SETTINGS, Transitions.FADE_BACK, 200d, 0d);
    });

    if (nbBlocks >= pickerNbBlocs.getMinValue() && nbBlocks <= pickerNbBlocs.getMaxValue()
        && difficulty >= pickerDifficulty.getMinValue() && difficulty <= pickerDifficulty.getMaxValue()
        && nbTransactions >= pickerNbTransactions.getMinValue()
        && nbTransactions <= pickerNbTransactions.getMaxValue()) {

      // Si les nombres entrés sont valides, on va pouvoir lancer la génération
      ControllerManager.<LoadingScreenController>getController(Screen.LOADING_SCREEN)
          .setText("Génération de la blockchain...");
      rlc.setMainContent(Screen.LOADING_SCREEN, Transitions.FADE_FRONT, 200d, 0d);

      // Annulation du service sur appui du bouton retour
      rlc.setOnBackButtonClick(v -> s.cancel());

      // Départ du service
      s.start();
    } else {
      // Sinon on affiche un message d'erreur
      rlc.showInfoBar(InfoType.ERROR, "Paramètres invalides", 2000d, 0d);
    }
  }

}
