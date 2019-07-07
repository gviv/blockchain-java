package ui.controllers;

import java.io.File;
import java.util.ArrayList;
import blockchain.Block;
import blockchain.Blockchain;
import blockchain.utils.BCJsonUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import ui.animation.Animator;
import ui.animation.Animator.Transitions;
import ui.components.VisualBlock;
import ui.components.InfoBar.InfoType;
import ui.components.VisualBlock.State;
import ui.resources.icons.Icon;
import ui.views.Screen;

/**
 * Cette classe repr�sente le contr�leur de la vue BlockchainOverview.
 *
 * @author Guillaume Vivies
 *
 */
public class BlockchainOverviewController extends Controller {

  /**
   * Le FlowPane contenant la blockchain visuelle.
   */
  @FXML
  private FlowPane visualBlockchain;

  /**
   * Le ScrollPane contenant visualBlockchain.
   */
  @FXML
  private ScrollPane visualBlockchainContainer;

  /**
   * La barre de recherche d'un bloc.
   */
  @FXML
  private TextField searchBar;

  /**
   * Le bouton de recherche d'un bloc.
   */
  @FXML
  private Button searchButton;

  /**
   * Le label indiquant l'�tat de la blockchain.
   */
  @FXML
  private Label labelBlockchainState;

  /**
   * Le bouton de v�rification de la blockchain.
   */
  @FXML
  private Button checkBlockchainButton;

  /**
   * Le Pane contenant labelBlockchainState et checkBlockchainButton.
   */
  @FXML
  private Pane checkBlockchainContainer;

  /**
   * Le SVG de l'ic�ne de la croix.
   */
  private SVGPath cross;

  /**
   * Le SVG de l'ic�ne du "tick".
   */
  private SVGPath tick;

  /**
   * Le SVG de l'ic�ne d'information.
   */
  private SVGPath info;

  /**
   * Stocke si la blockchain est charg�e.
   */
  private boolean blockchainLoaded;

  /**
   * L'�tat actuel de la blockchain.
   */
  private State blockchainState;

  /**
   * Le bloc actuellement cliqu�.
   */
  private VisualBlock blockClicked;

  /**
   * Getter visualBlockchain.
   *
   * @return visualBlockchain
   */
  public FlowPane getVisualBlockchain() {
    return visualBlockchain;
  }

  /**
   * Getter visualBlockchainContainer.
   *
   * @return visualBlockchainContainer
   */
  public ScrollPane getVisualBlockchainContainer() {
    return this.visualBlockchainContainer;
  }

  /**
   * Getter blockClicked.
   *
   * @return blockClicked
   */
  public VisualBlock getBlockClicked() {
    return this.blockClicked;
  }

  /**
   * Getter blockchainLoaded.
   *
   * @return blockchainLoaded
   */
  public boolean isBlockchainLoaded() {
    return blockchainLoaded;
  }

  /**
   * Setter blockchainLoaded.
   *
   * @param b blockchainLoaded
   */
  public void setBlockchainLoaded(boolean b) {
    this.blockchainLoaded = b;
  }

  /**
   * Setter blockClicked.
   *
   * @param index l'indice du bloc cliqu�
   */
  public void setBlockClicked(int index) {
    if (index >= 0 && index < visualBlockchain.getChildren().size()) {
      this.blockClicked = (VisualBlock) visualBlockchain.getChildren().get(index);
    } else {
      this.blockClicked = null;
    }
  }

  /**
   * Permet de changer l'�tat affich� de la blockchain.
   *
   * @param s le nouvel �tat
   */
  public void setBlockchainState(State s) {
    blockchainState = s;
    String str = s.toString().toLowerCase();

    switch (s) {
    case VALID:
      labelBlockchainState.setGraphic(tick);
      labelBlockchainState.setText("Blockchain valide");
      if (checkBlockchainContainer.getChildren().contains(checkBlockchainButton)) {
        checkBlockchainContainer.getChildren().remove(checkBlockchainButton);
      }
      break;
    case INVALID:
      labelBlockchainState.setGraphic(cross);
      labelBlockchainState.setText("Blockchain invalide");
      if (checkBlockchainContainer.getChildren().contains(checkBlockchainButton)) {
        checkBlockchainContainer.getChildren().remove(checkBlockchainButton);
      }
      break;
    case UNCHECKED:
      labelBlockchainState.setGraphic(info);
      labelBlockchainState.setText("Blockchain non v�rifi�e");
      if (!checkBlockchainContainer.getChildren().contains(checkBlockchainButton)) {
        checkBlockchainContainer.getChildren().add(1, checkBlockchainButton);
      }
      break;
    }

    labelBlockchainState.getStyleClass().setAll("info-text--" + str);
  }

  /**
   * Charge les ic�nes utilis�es et initialise l'�tat de la blockchain.
   *
   * @see Controller#init()
   */
  public void init() {
    // Cr�ation du bouton de recherche
    SVGPath magnifier = new SVGPath();
    magnifier.setContent(Icon.MAGNIFIER);
    magnifier.fillProperty().bind(searchButton.textFillProperty());
    searchButton.setGraphic(magnifier);

    // Chargement de l'ic�ne de la croix
    cross = new SVGPath();
    cross.setContent(Icon.CROSS);
    cross.fillProperty().bind(labelBlockchainState.textFillProperty());

    // Chargement de l'ic�ne du "tick"
    tick = new SVGPath();
    tick.setContent(Icon.TICK);
    tick.fillProperty().bind(labelBlockchainState.textFillProperty());

    // Chargement de l'ic�ne d'information
    info = new SVGPath();
    info.setContent(Icon.INFO);
    info.fillProperty().bind(labelBlockchainState.textFillProperty());

    blockchainLoaded = false;
    setBlockchainState(State.UNCHECKED);
  }

  /**
   * Affiche la blockchain.
   *
   * @see Controller#show()
   */
  public void show() {
    root.requestFocus();
    searchBar.setText("");

    // Configuration du bouton retour
    RootLayoutController c = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);
    c.setOnBackButtonClick(v -> {
      c.setMainContent(Screen.TITLE_SCREEN, Transitions.FADE_RIGHT, 200d, 0d);
    });
    c.showBackButton("Accueil");

    if (!blockchainLoaded) {
      // Si la blockchain n'est pas charg�e, on la charge
      visualBlockchainContainer.setVvalue(0.0);
      this.visualBlockchain.getChildren().clear();
      Blockchain blockchain = ControllerManager.getApp().getBlockchain();

      // On cr�e les VisualBlocks correspondant aux blocs de la blockchain
      for (int i = 0; i < blockchain.getNbBlocks(); ++i) {
        Block b = blockchain.getBlock(i);

        VisualBlock vb = new VisualBlock(b.getIndex());
        vb.setOnMouseReleased(e -> handleBlockClick(b, vb, 0d));

        this.visualBlockchain.getChildren().add(vb);
      }

      // On colore les blocs selon l'�tat de la blockchain
      this.visualBlockchain.getChildren().forEach(vb -> {
        ((VisualBlock) vb).setState(blockchainState);
      });

      Animator.VisualBlockAnimator.initialAnimation(200d, 1500d, 400d);
      blockchainLoaded = true;
    } else if (blockClicked != null) {
      // Sinon s'il y a un bloc cliqu�, on le d�zoome et on anime les autres
      Animator.VisualBlockAnimator.zoomOut(350d);
      Animator.VisualBlockAnimator.scaleUpAll(200d, 500d, 30d);
    } else {
      // Sinon �a signifie que la blockchain est charg�e mais qu'il n'y a pas de bloc
      // � d�zoomer
      visualBlockchainContainer.setVvalue(0.0);
      Animator.VisualBlockAnimator.initialAnimation(200d, 1200d, 200d);
    }
  }

  /**
   * Permet de prendre en charge le clic sur un bloc.
   *
   * @param b     le bloc de la blockchain
   * @param vb    le VisualBlock correspondant
   * @param delay le d�lai de l'animation
   */
  private void handleBlockClick(Block b, VisualBlock vb, double delay) {
    // Si pas de bloc d�j� cliqu�
    if (blockClicked == null) {
      Timeline timeline = new Timeline(new KeyFrame(Duration.ONE, e -> {
        // Le bloc cliqu� devient le VisualBlock pass� en param�tre
        this.blockClicked = vb;
        ControllerManager.<BlockOverviewController>getController(Screen.BLOCK_OVERVIEW).setBlock(b);

        // On anime les blocs
        Animator.VisualBlockAnimator.zoomIn(blockClicked, 400d);
        Animator.VisualBlockAnimator.scaleDownAll(150d, 500d, 0d);

        // On affiche la vue d�taill� sur le bloc
        ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT).setMainContent(Screen.BLOCK_OVERVIEW,
            Transitions.FADE_FRONT, 150d, 130d);
      }));
      timeline.setDelay(Duration.millis(delay));
      timeline.play();
    }
  }

  /**
   * Permet de lancer la recherche d'un bloc � partir de l'indice �crit dans
   * searchBar.
   */
  @FXML
  private void searchBlock() {
    Blockchain blockchain = ControllerManager.getApp().getBlockchain();
    int index = 0;

    // On r�cup�re l'indice
    try {
      index = Integer.parseInt(searchBar.getText());
    } catch (NumberFormatException e) {
      ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT).showInfoBar(InfoType.ERROR,
          "Indice invalide", 2000d, 0d);
      return;
    }

    if (index >= 0 && index < visualBlockchain.getChildren().size()) {
      // Si l'indice est valide, on va chercher le bloc correspondant
      VisualBlock blockSelected = (VisualBlock) visualBlockchain.getChildren().get(index);
      double scrollDuration = 0d;

      // Si le bloc choisi n'est pas visible, on fait d�filer la barre de d�filement
      if (!Animator.VisualBlockAnimator.isVisible(blockSelected)) {
        scrollDuration = 200d;
        Animator.VisualBlockAnimator.scroll(blockSelected, scrollDuration);
      }

      handleBlockClick(blockchain.getBlock(index), blockSelected, scrollDuration);
    } else {
      // Sinon affichage d'un message d'erreur
      ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT).showInfoBar(InfoType.ERROR,
          "Indice hors limites", 2000d, 0d);
    }
  }

  /**
   * Permet de v�rifier l'int�grit� de la blockchain. Cette fonction devrait
   * utiliser la fonction de v�rification de blockchain
   * ({@link blockchain.Blockchain#isValid()}) mais cela n'est pas possible, pour
   * les raisons mentionn�es ci-dessous.
   */
  @FXML
  private void checkBlockchain() {
    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);

    // Cr�ation du service qui va se charger de la v�rification
    Service<Boolean> s = new Service<Boolean>() {
      @Override
      protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
          @Override
          public Boolean call() {
            /*
             * Ici, il faudrait faire appel � la m�thode Blockchain.isValid() pour v�rifier
             * la validit� de la blockchain mais cette m�thode ne permet pas de s'arr�ter si
             * l'utilisateur le d�cide et ne permet pas non plus de savoir quel bloc est
             * valide ou non. On recopie donc cette m�thode, ce qui est "sale" mais cela
             * permet de ne pas polluer le code de la classe Blockchain avec des
             * consid�rations qui rel�vent de l'interface.
             */
            Blockchain blockchain = ControllerManager.getApp().getBlockchain();
            ArrayList<Block> blocks = blockchain.getBlocks();

            // V�rification que le premier bloc est le bloc Genesis
            if (!blocks.get(0).getPreviousHash().equals("0")) {
              ((VisualBlock) visualBlockchain.getChildren().get(0)).setState(VisualBlock.State.INVALID);
              return false;
            }
            // V�rification du merkle root
            if (!blocks.get(0).isValidMerkleRoot()) {
              ((VisualBlock) visualBlockchain.getChildren().get(0)).setState(VisualBlock.State.INVALID);
              return false;
            }
            // V�rification du hash
            if (!blocks.get(0).isValidHash()) {
              ((VisualBlock) visualBlockchain.getChildren().get(0)).setState(VisualBlock.State.INVALID);
              return false;
            }
            ((VisualBlock) visualBlockchain.getChildren().get(0)).setState(VisualBlock.State.VALID);

            // V�rification des autres blocs
            for (int i = 1; i < blockchain.getNbBlocks(); ++i) {
              if (this.isCancelled())
                break;

              // V�rification du merkle root
              if (!blocks.get(i).isValidMerkleRoot()) {
                ((VisualBlock) visualBlockchain.getChildren().get(i)).setState(VisualBlock.State.INVALID);
                return false;
              }

              // V�rification du hash
              if (!blocks.get(i).isValidHash()) {
                ((VisualBlock) visualBlockchain.getChildren().get(i)).setState(VisualBlock.State.INVALID);
                return false;
              }

              // V�rification du hash pr�c�dent
              if (!blocks.get(i).getPreviousHash().equals(blocks.get(i - 1).getHash())) {
                ((VisualBlock) visualBlockchain.getChildren().get(i)).setState(VisualBlock.State.INVALID);
                return false;
              }

              // V�rification des transactions
              if (!blocks.get(i).checkTransactions()) {
                ((VisualBlock) visualBlockchain.getChildren().get(i)).setState(VisualBlock.State.INVALID);
                return false;
              }
              ((VisualBlock) visualBlockchain.getChildren().get(i)).setState(VisualBlock.State.VALID);
            }
            return true;
          }
        };
      }
    };

    // Si �chec
    s.setOnFailed(e -> {
      rlc.showInfoBar(InfoType.ERROR, "Une erreur est survenue", 3000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_BACK, 200d, 0d);
    });

    // Si succ�s
    s.setOnSucceeded(e -> {
      // R�cup�ration de la validit� de la blockchain
      boolean valid = s.getValue();

      if (valid) {
        rlc.showInfoBar(InfoType.SUCCESS, "Blockchain valide", 3000d, 300d);
        setBlockchainState(State.VALID);
      } else {
        rlc.showInfoBar(InfoType.ERROR, "Blockchain invalide", 3000d, 300d);
        setBlockchainState(State.INVALID);
      }

      rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_FRONT, 200d, 0d);
    });

    // Si annul�
    s.setOnCancelled(e -> {
      rlc.showInfoBar(InfoType.DEFAULT, "V�rification annul�e", 2000d, 300d);
      rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_BACK, 200d, 0d);
    });

    if (blockchainState == State.UNCHECKED) {
      // Si la blockchain n'est pas v�rifi�e, on peut lancer sa v�rification
      ControllerManager.<LoadingScreenController>getController(Screen.LOADING_SCREEN)
          .setText("V�rification de la blockchain...");
      rlc.setMainContent(Screen.LOADING_SCREEN, Transitions.FADE_FRONT, 200d, 0d);

      // Annulation du service sur appui du bouton retour
      rlc.setOnBackButtonClick(v -> s.cancel());

      // On retarde le d�part du service de quelques dixi�mes de seconde pour avoir le
      // temps de voir l'animation
      Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300d), v -> s.start()));
      timeline.play();
    }
  }

  /**
   * Permet d'exporter la blockchain dans un fichier JSON.
   */
  @FXML
  public void exportBlockchain() {
    RootLayoutController rlc = ControllerManager.<RootLayoutController>getController(Screen.ROOT_LAYOUT);

    // Configuration du FileChooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Exporter Blockchain");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichier JSON", "*.json"));
    File selectedFile = fileChooser.showSaveDialog(rlc.getRoot().getScene().getWindow());

    Blockchain blockchain = ControllerManager.getApp().getBlockchain();

    if (selectedFile != null) {
      String filename = selectedFile.toString();

      ControllerManager.<LoadingScreenController>getController(Screen.LOADING_SCREEN)
          .setText("Exportation de la blockchain...");
      rlc.setMainContent(Screen.LOADING_SCREEN, Transitions.FADE_FRONT, 200d, 0d);

      // Cr�ation du service qui va se charger de l'exportation
      final Service<Void> s = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
          return new Task<Void>() {
            @Override
            public Void call() {
              BCJsonUtils.BCJsonWriter(blockchain, filename);
              return null;
            }
          };
        }
      };

      // Si �chec
      s.setOnFailed(e -> {
        rlc.showInfoBar(InfoType.ERROR, "Une erreur est survenue", 3000d, 300d);
        rlc.setMainContent(Screen.BLOCK_OVERVIEW, Transitions.FADE_BACK, 200d, 0d);
      });

      // Si succ�s
      s.setOnSucceeded(e -> {
        rlc.showInfoBar(InfoType.SUCCESS, "Exportation termin�e", 3000d, 300d);
        rlc.setMainContent(Screen.BLOCKCHAIN_OVERVIEW, Transitions.FADE_FRONT, 200d, 0d);
      });

      // D�part du service
      s.start();
    }
  }

}
