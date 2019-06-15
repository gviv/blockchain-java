package ui.controllers;

import java.io.File;
import blockchain.Blockchain;
import blockchain.utils.BCJsonUtils;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ui.animation.Animator.Transitions;
import ui.components.InfoBar.InfoType;
import ui.components.VisualBlock.State;
import ui.views.Screen;

/**
 * Cette classe représente le contrôleur de la vue TitleScreen.
 *
 * @author Guillaume Vivies
 *
 */
public class TitleScreenController extends Controller {

  /**
   * Le Pane correspondant au menu lorsqu'aucune blockchain n'est chargée.
   */
  @FXML
  private Pane blockchainNotLoadedView;

  /**
   * Le Pane correspondant au menu lorsqu'une blockchain est chargée.
   */
  @FXML
  private Pane blockchainLoadedView;

  /**
   * Initialise la visibilité des menus (affiche le menu de la blockchain non
   * chargée).
   *
   * @see Controller#init()
   */
  public void init() {
    blockchainNotLoadedView.setVisible(true);
    blockchainLoadedView.setVisible(false);
  }

  /**
   * Vérifie si la blockchain est chargée et affiche le bon menu en conséquence.
   *
   * @see Controller#show()
   */
  public void show() {
    ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT).hideBackButton();

    if (ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
        .isBlockchainLoaded()) {
      showBlockchainLoadedView();
    } else {
      showBlockchainNotLoadedView();
    }
  }

  /**
   * Affiche le menu de la blockchain chargée.
   */
  private void showBlockchainLoadedView() {
    if (!blockchainLoadedView.isVisible()) {
      blockchainLoadedView.setVisible(true);
      blockchainNotLoadedView.setVisible(false);
    }
  }

  /**
   * Affiche le menu de la blockchain non chargée.
   */
  private void showBlockchainNotLoadedView() {
    if (!blockchainNotLoadedView.isVisible()) {
      blockchainNotLoadedView.setVisible(true);
      blockchainLoadedView.setVisible(false);
    }
  }

  /**
   * Permet d'accéder à la vue de configuration de la blockchain.
   */
  @FXML
  private void goToBlockchainSettings() {
    ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT).setMainContent(Screen.BLOCKCHAIN_SETTINGS,
        Transitions.FADE_LEFT, 200d, 0d);
  }

  /**
   * Permet d'importer une blockchain depuis un fichier JSON.
   */
  @FXML
  private void importBlockchain() {
    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);

    // Configuration du FileChooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Importer Blockchain");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichier JSON", "*.json"));
    File selectedFile = fileChooser.showOpenDialog(rlc.getRoot().getScene().getWindow());

    if (selectedFile != null) {
      String filename = selectedFile.toString();

      ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .setBlockchainLoaded(false);
      ControllerManager.<LoadingScreenController>getController(Screen.LOADING_SCREEN)
          .setText("Importation de la blockchain...");
      rlc.setMainContent(Screen.LOADING_SCREEN, Transitions.FADE_FRONT, 200d, 0d);

      // Création du service qui va se charger de l'importation
      final Service<Blockchain> s = new Service<Blockchain>() {
        @Override
        protected Task<blockchain.Blockchain> createTask() {
          return new Task<blockchain.Blockchain>() {
            @Override
            public Blockchain call() {
              return BCJsonUtils.BCJsonReader(filename);
            }
          };
        }
      };

      // Si échec
      s.setOnFailed(e -> {
        rlc.showInfoBar(InfoType.ERROR, "Format de fichier non reconnu", 4000d, 300d);
        rlc.setMainContent(Screen.TITLE_SCREEN, Transitions.FADE_BACK, 200d, 0d);
      });

      // Si succès
      s.setOnSucceeded(e -> {
        ControllerManager.getApp().setBlockchain(s.getValue());
        ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
            .setBlockchainState(State.UNCHECKED);
        rlc.showInfoBar(InfoType.SUCCESS, "Importation terminée", 2000d, 300d);
        rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_FRONT, 200d, 0d);
      });

      // Départ du service
      s.start();
    }
  }

  /**
   * Permet de quitter le programme.
   */
  @FXML
  private void quit() {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Permet d'exporter la blockchain dans un fichier JSON.
   */
  @FXML
  private void exportBlockchain() {
    ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW).exportBlockchain();
  }

  /**
   * Permet d'accéder à l'overview de la blockchain.
   */
  @FXML
  private void goToBlockchainOverview() {
    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);
    rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_LEFT, 200d, 0d);
  }

}
