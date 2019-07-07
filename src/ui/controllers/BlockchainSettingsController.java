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
 * Cette classe repr�sente le contr�leur de la vue BlockchainSettings.
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
   * Le NumberPicker de la difficult�.
   */
  @FXML
  private NumberPicker pickerDifficulty;

  /**
   * Le NumberPicker du nombre de transactions.
   */
  @FXML
  private NumberPicker pickerNbTransactions;

  /**
   * Change la valeur minimale du NumberPicker de la difficult� et la valeur
   * maximale du NumberPicker du nombre de transactions.
   *
   * @see Controller#init()
   */
  public void init() {
    pickerDifficulty.setMinValue(0);
    pickerNbTransactions.setMaxValue(10);
  }

  /**
   * R�initialise les NumberPickers et configure le bouton retour.
   *
   * @see Controller#show()
   */
  public void show() {
    root.requestFocus();

    // R�initialisation des NumberPickers
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
   * Permet de g�n�rer une blockchain. Cette fonction devrait utiliser la fonction
   * de g�n�ration de blockchain
   * ({@link blockchain.BlockchainGenerator#generateBlockchain(int, int, int)})
   * mais cela n'est pas possible, pour les raisons mentionn�es ci-dessous.
   */
  @FXML
  private void generateBlockchain() {
    // R�cup�ration des valeurs des NumberPickers
    int nbBlocks = pickerNbBlocs.getValue();
    int difficulty = pickerDifficulty.getValue();
    int nbTransactions = pickerNbTransactions.getValue();

    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);

    // Cr�ation du service qui va se charger de la g�n�ration
    Service<Blockchain> s = new Service<Blockchain>() {
      @Override
      protected Task<Blockchain> createTask() {
        return new Task<Blockchain>() {
          @Override
          public Blockchain call() {
            /*
             * Ici, il faudrait faire appel � la m�thode
             * BlockchainGenerator.generateBlockchain(int, int, int) pour g�n�rer la
             * blockchain mais cette m�thode ne permet pas de s'arr�ter si l'utilisateur le
             * d�cide. On recopie donc cette m�thode, ce qui est "sale" mais cela permet de
             * ne pas polluer le code de la classe BlockchainGenerator avec des
             * consid�rations qui rel�vent de l'interface.
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

    // Si �chec
    s.setOnFailed(e -> {
      rlc.showInfoBar(InfoType.ERROR, "Une erreur est survenue", 3000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_SETTINGS, Transitions.FADE_BACK, 200d, 0d);
    });

    // Si succ�s
    s.setOnSucceeded(e -> {
      ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .setBlockchainLoaded(false);
      ControllerManager.getApp().setBlockchain(s.getValue());
      ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .setBlockchainState(State.VALID);
      rlc.showInfoBar(InfoType.SUCCESS, "G�n�ration termin�e", 2000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_FRONT, 200d, 0d);
    });

    // Si annul�
    s.setOnCancelled(e -> {
      rlc.showInfoBar(InfoType.DEFAULT, "G�n�ration annul�e", 2000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_SETTINGS, Transitions.FADE_BACK, 200d, 0d);
    });

    if (nbBlocks >= pickerNbBlocs.getMinValue() && nbBlocks <= pickerNbBlocs.getMaxValue()
        && difficulty >= pickerDifficulty.getMinValue() && difficulty <= pickerDifficulty.getMaxValue()
        && nbTransactions >= pickerNbTransactions.getMinValue()
        && nbTransactions <= pickerNbTransactions.getMaxValue()) {

      // Si les nombres entr�s sont valides, on va pouvoir lancer la g�n�ration
      ControllerManager.<LoadingScreenController>getController(Screen.LOADING_SCREEN)
          .setText("G�n�ration de la blockchain...");
      rlc.setMainContent(Screen.LOADING_SCREEN, Transitions.FADE_FRONT, 200d, 0d);

      // Annulation du service sur appui du bouton retour
      rlc.setOnBackButtonClick(v -> s.cancel());

      // D�part du service
      s.start();
    } else {
      // Sinon on affiche un message d'erreur
      rlc.showInfoBar(InfoType.ERROR, "Param�tres invalides", 2000d, 0d);
    }
  }

}
