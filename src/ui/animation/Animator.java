package ui.animation;

import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import ui.components.IconButton;
import ui.components.InfoBar;
import ui.components.VisualBlock;
import ui.controllers.BlockchainOverviewController;
import ui.controllers.Controller;
import ui.controllers.ControllerManager;
import ui.views.Screen;

/**
 * Cette classe permet d'effectuer des animations et des transitions.
 *
 * @author Guillaume Vivies
 *
 */
public final class Animator {

  /**
   * Stocke les transitions disponibles.
   */
  public static enum Transitions {
    NONE, FADE_FRONT, FADE_BACK, FADE_UP, FADE_DOWN, FADE_LEFT, FADE_RIGHT
  }

  /**
   * Cette classe représente une transition simple entre deux Nodes.
   */
  public static class SimpleTransition {

    /**
     * L'éventuel contrôleur associé à l'écran à afficher.
     */
    private Controller controller;

    /**
     * L'éventuelle transition en attente.
     */
    private PendingTransition pendingTransition;

    /**
     * Stocke si une animation est en cours.
     */
    private boolean isBeingAnimated;

    /**
     * Setter controller
     *
     * @param controller
     */
    public void setController(Controller c) {
      this.controller = c;
    }

    /**
     * Cette classe représente une transition en attente.
     */
    private class PendingTransition {
      Node n1;
      Node n2;
      Transitions t;
      double duration;
      double delay;
      Consumer<Void> f;

      public PendingTransition(Node n1, Node n2, Transitions t, double duration, double delay, Consumer<Void> f) {
        this.n1 = n1;
        this.n2 = n2;
        this.t = t;
        this.duration = duration;
        this.delay = delay;
        this.f = f;
      }
    }

    /**
     * Prépare la transition entre n1 et n2 en prenant en compte une éventuelle
     * animation en attente.
     *
     * @param n1       le premier Node
     * @param n2       le deuxième Node
     * @param t        la transition
     * @param duration la durée de la transition
     * @param delay    le délai de la transition
     * @param c        la fonction à appeler entre l'animation de n1 et n2
     */
    public void contentTransition(Node n1, Node n2, Transitions t, double duration, double delay, Consumer<Void> c) {
      if (!isBeingAnimated) {
        if (delay > 0.0) {
          Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delay), v -> {
            processTransition(n1, n2, t, duration, c);
          }));
          timeline.play();
        } else {
          processTransition(n1, n2, t, duration, c);
        }
      } else {
        pendingTransition = new PendingTransition(n1, n2, t, duration, delay, c);
      }
    }

    /**
     * Effectue la transition entre n1 et n2.
     *
     * @param n1       le premier Node
     * @param n2       le deuxième Node
     * @param t        la transition
     * @param duration la durée de la transition
     * @param c        la fonction à appeler entre l'animation de n1 et n2
     */
    private void processTransition(Node n1, Node n2, Transitions t, double duration, Consumer<Void> c) {
      isBeingAnimated = true;

      switch (t) {
      case FADE_FRONT:
        fade(n1, n2, Duration.millis(duration), 'f', c);
        break;
      case FADE_BACK:
        fade(n1, n2, Duration.millis(duration), 'b', c);
        break;
      case FADE_UP:
        fade(n1, n2, Duration.millis(duration), 'u', c);
        break;
      case FADE_DOWN:
        fade(n1, n2, Duration.millis(duration), 'd', c);
        break;
      case FADE_LEFT:
        fade(n1, n2, Duration.millis(duration), 'l', c);
        break;
      case FADE_RIGHT:
        fade(n1, n2, Duration.millis(duration), 'r', c);
        break;
      case NONE:
      default:
        duration = 0d;
        c.accept(null);
        isBeingAnimated = false;
      }

      // Appel de la fonction d'affichage du nouvel écran après son apparition
      if (this.controller != null) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration), e -> {
          this.controller.show();
          this.controller = null;
        }));
        timeline.play();
      }
    }

    /**
     * Effectue une transition de type "fondu".
     *
     * @param n1        le premier Node
     * @param n2        le deuxième Node
     * @param d         la durée de la transition
     * @param direction le sens de la transition
     * @param c         la fonction à appeler entre l'animation de n1 et n2
     */
    private void fade(Node n1, Node n2, Duration d, char direction, Consumer<Void> c) {
      FadeTransition ft = new FadeTransition();
      ScaleTransition st = new ScaleTransition();
      TranslateTransition tt = new TranslateTransition();
      ParallelTransition pt = new ParallelTransition();
      ft.setDuration(d);
      ft.setNode(n1);
      st.setDuration(d);
      st.setNode(n1);
      tt.setDuration(d);
      tt.setNode(n1);

      ft.setInterpolator(Interpolator.EASE_OUT);
      st.setInterpolator(Interpolator.EASE_BOTH);
      tt.setInterpolator(Interpolator.EASE_BOTH);

      st.setFromX(1.0);
      st.setFromY(1.0);
      tt.setFromX(0.0);
      tt.setFromY(0.0);

      switch (direction) {
      case 'f':
        st.setToX(1.08);
        st.setToY(1.08);
        break;
      case 'b':
        st.setToX(0.92);
        st.setToY(0.92);
        break;
      case 'u':
        tt.setToY(10.0);
        break;
      case 'd':
        tt.setToY(-10.0);
        break;
      case 'l':
        tt.setToX(-15.0);
        break;
      case 'r':
        tt.setToX(15.0);
      }

      ft.setFromValue(1.0);
      ft.setToValue(0.0);

      pt.getChildren().setAll(ft, st, tt);
      pt.setOnFinished(v -> {
        fadeReversed(n1, n2, d, direction, c);
      });
      pt.playFromStart();
    }

    /**
     * Termine l'animation "fondu" démarrée par la fonction fade.
     *
     * @param n1        le premier Node
     * @param n2        le deuxième Node
     * @param d         la durée de la transition
     * @param direction le sens de la transition
     * @param c         la fonction à appeler entre l'animation de n1 et n2
     */
    private void fadeReversed(Node n1, Node n2, Duration d, char direction, Consumer<Void> c) {
      FadeTransition ft = new FadeTransition();
      ScaleTransition st = new ScaleTransition();
      TranslateTransition tt = new TranslateTransition();
      ParallelTransition pt = new ParallelTransition();
      ft.setDuration(d);
      ft.setNode(n2);
      st.setDuration(d);
      st.setNode(n2);
      tt.setDuration(d);
      tt.setNode(n2);

      ft.setInterpolator(Interpolator.EASE_IN);
      st.setInterpolator(Interpolator.EASE_BOTH);
      tt.setInterpolator(Interpolator.EASE_BOTH);

      st.setToX(1.0);
      st.setToY(1.0);
      tt.setToX(0.0);
      tt.setToY(0.0);
      st.setFromX(1.0);
      st.setFromY(1.0);
      tt.setFromX(0.0);
      tt.setFromY(0.0);

      switch (direction) {
      case 'f':
        st.setFromX(0.92);
        st.setFromY(0.92);
        break;
      case 'b':
        st.setFromX(1.08);
        st.setFromY(1.08);
        break;
      case 'u':
        tt.setFromY(-10.0);
        break;
      case 'd':
        tt.setFromY(10.0);
        break;
      case 'l':
        tt.setFromX(10.0);
        break;
      case 'r':
        tt.setFromX(-15.0);
      }

      ft.setFromValue(0.0);
      ft.setToValue(1.0);

      pt.getChildren().setAll(ft, st, tt);
      pt.setOnFinished(v -> {
        // On remet le noeud d'origine à sa place
        n1.setTranslateX(0.0);
        n1.setScaleX(1.0);
        n1.setScaleY(1.0);
        n1.setOpacity(1.0);

        // S'il y a une transition en attente, on la lance
        if (pendingTransition != null) {
          if (pendingTransition.delay > 0.0) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(pendingTransition.delay), f -> {
              processTransition(pendingTransition.n1, pendingTransition.n2, pendingTransition.t,
                  pendingTransition.duration, pendingTransition.f);
            }));
            timeline.play();
          } else {
            processTransition(pendingTransition.n1, pendingTransition.n2, pendingTransition.t,
                pendingTransition.duration, pendingTransition.f);
          }
          pendingTransition = null;
        } else {
          isBeingAnimated = false;
        }
      });
      pt.playFromStart();
      c.accept(null);
    }
  }

  /**
   * Cette classe permet d'animer un VisualBlock.
   */
  public static class VisualBlockAnimator {

    /**
     * Couche du dessus sur laquelle on va placer le bloc à animer.
     */
    private static AnchorPane topLayer = new AnchorPane();

    /**
     * Bloc animé qui se trouve sur topLayer.
     */
    private static VisualBlock animatedBlock;

    /**
     * Échelle du bloc quand il est survolé.
     */
    private static final double SCALE_HOVERED = 1.05;

    /**
     * Effectue l'animation au survol d'un bloc.
     *
     * @param vb       le bloc à animer
     * @param duration la durée de l'animation
     */
    public static void onHover(VisualBlock vb, double duration) {
      ScaleTransition st = new ScaleTransition(Duration.millis(duration), vb);
      st.setToX(SCALE_HOVERED);
      st.setToY(SCALE_HOVERED);
      st.play();
    }

    /**
     * Effectue l'animation à la sortie du survol d'un bloc.
     *
     * @param vb       le bloc à animer
     * @param duration la durée de l'animation
     */
    public static void onHoverOut(VisualBlock vb, double duration) {
      ScaleTransition st = new ScaleTransition(Duration.millis(duration), vb);
      st.setToX(1.0);
      st.setToY(1.0);
      st.play();
    }

    /**
     * Calcule le nombre de blocs visibles à l'écran.
     *
     * @return le nombre de blocs visibles
     */
    private static int computeNbBlocksVisibles() {
      FlowPane blockchain = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchain();
      ScrollPane sp = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchainContainer();

      int nbBlocsLigne = (int) (((blockchain.getWidth() - blockchain.getPadding().getLeft()
          - Math.abs(blockchain.getPadding().getRight() - blockchain.getHgap())))
          / (VisualBlock.WIDTH + blockchain.getHgap()));
      int nbBlocsColonne = ((int) (((sp.getHeight() - blockchain.getPadding().getTop()))
          / (VisualBlock.HEIGHT + blockchain.getVgap()))) + 2;

      return nbBlocsLigne * nbBlocsColonne;
    }

    /**
     * Détermine si un bloc est visible à l'écran.
     *
     * @param vb le bloc à tester
     * @return true si le bloc est visible, false sinon
     */
    public static boolean isVisible(VisualBlock vb) {
      ScrollPane sp = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchainContainer();
      Bounds boundsSp = sp.localToScene(sp.getLayoutBounds());
      Bounds boundsVb = vb.localToScene(vb.getLayoutBounds());

      return boundsVb.getMaxY() >= boundsSp.getMinY() && boundsVb.getMinY() <= boundsSp.getMaxY();
    }

    /**
     * Effectue l'animation lors du chargement initial de la blockchain. Cette
     * fonction ressemble à scaleAll mais l'intérêt était de pouvoir définir une
     * animation différente lors du chargement initial.
     *
     * @param duration      la durée de l'animation individuelle d'un bloc
     * @param totalDuration la durée totale de l'animation (en prenant en compte
     *                      tous les blocs)
     * @param delay         le délai de l'animation
     */
    public static void initialAnimation(double duration, double totalDuration, double delay) {
      FlowPane blockchain = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchain();

      /*
       * On doit compter le nombre de blocs visibles pour restreindre l'animation à
       * ces derniers mais le ScrollPane n'a pas encore de taille définie à ce moment
       * de l'exécution, on ne peut donc pas appeler computeNbBlocksVisibles(). On va
       * donc approximer ce nombre avec les informations que l'on possède (on fait
       * donc le même calcul que dans computeNbBlocksVisibles mais en utilisant la
       * taille de la scène au lieu de celle du ScrollPane).
       */
      Scene s = ControllerManager.<Controller>getController(Screen.ROOT_LAYOUT).getRoot().getScene();
      int nbBlocsLigne = (int) (((s.getWidth() - blockchain.getPadding().getLeft()
          - Math.abs(blockchain.getPadding().getRight() - blockchain.getHgap())))
          / (VisualBlock.WIDTH + blockchain.getHgap()));
      int nbBlocsColonne = ((int) (((s.getHeight() - blockchain.getPadding().getTop()))
          / (VisualBlock.HEIGHT + blockchain.getVgap()))) + 2;
      int nbBlocs = nbBlocsLigne * nbBlocsColonne;

      // On calcule le délai entre chaque animation afin que l'animation ait la même
      // durée quelque soit le nombre de blocs
      double delayBetweenBlocs = totalDuration / nbBlocs;

      // On anime les blocs visibles
      int i = 0;
      while (i < nbBlocs && i < blockchain.getChildren().size()) {
        VisualBlock v = (VisualBlock) blockchain.getChildren().get(i);
        FadeTransition ft = new FadeTransition(Duration.millis(duration), v);
        ScaleTransition st = new ScaleTransition(Duration.millis(duration), v);
        ParallelTransition pt = new ParallelTransition();
        ft.setFromValue(0);
        ft.setToValue(1);
        st.setFromX(0.9);
        st.setFromY(0.9);
        st.setToX(1);
        st.setToY(1);
        pt.getChildren().addAll(ft, st);
        pt.setDelay(Duration.millis(delay + (i * delayBetweenBlocs)));
        pt.play();
        ++i;
      }
    }

    /**
     * Effectue l'animation de zoom sur un bloc.
     *
     * @param vb       le bloc à animer
     * @param duration la durée de l'animation
     */
    public static void zoomIn(VisualBlock vb, double duration) {
      // On place le bloc à animer sur le AnchorPane pour qu'il soit au dessus de tout
      // le reste
      Pane root = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW).getRoot();

      double sceneWidth = ControllerManager.<Controller>getController(Screen.ROOT_LAYOUT).getRoot().getScene()
          .getWidth();
      double sceneHeight = ControllerManager.<Controller>getController(Screen.ROOT_LAYOUT).getRoot().getScene()
          .getHeight();

      double x = (sceneWidth / 2) - vb.localToScene(vb.getBoundsInLocal()).getMinX() - vb.getWidth() / 2;
      double y = (sceneHeight / 2) - vb.localToScene(vb.getBoundsInLocal()).getMinY() - vb.getHeight() / 2;

      // Duplication de vb dans animatedBlock
      animatedBlock = new VisualBlock(vb);
      animatedBlock.setLayoutX(vb.localToScene(vb.getBoundsInLocal()).getMinX());
      animatedBlock.setLayoutY(vb.localToScene(vb.getBoundsInLocal()).getMinY());

      // Ajout du bloc animé dans le topLayer
      topLayer.getChildren().setAll(animatedBlock);
      // Ajout du topLayer à la scène
      if (!root.getChildren().contains(topLayer))
        root.getChildren().add(1, topLayer);

      Duration d = Duration.millis(duration);
      ScaleTransition st = new ScaleTransition(d, animatedBlock);
      TranslateTransition tt = new TranslateTransition(d, animatedBlock);
      FadeTransition ft = new FadeTransition(d, animatedBlock);
      ParallelTransition pt = new ParallelTransition();

      double scale = sceneWidth / 70;

      ft.setFromValue(1.0);
      ft.setToValue(0.0);
      st.setToX(scale);
      st.setToY(scale);
      tt.setToX(x);
      tt.setToY(y);
      tt.setFromX(0.0);
      tt.setFromY(0.0);
      st.setFromX(1.0);
      st.setFromY(1.0);

      pt.getChildren().setAll(st, tt);

      pt.play();
      vb.setOpacity(0.0);
    }

    /**
     * Effectue l'animation de dézoom du bloc actuellement zoomé.
     *
     * @param duration la durée de l'animation
     */
    public static void zoomOut(double duration) {
      BlockchainOverviewController boc = ControllerManager
          .<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW);
      Pane root = boc.getRoot();
      VisualBlock blockClicked = boc.getBlockClicked();
      // Si le bloc n'est pas visible, on bouge le slider pour qu'il soit visible
      if (!isVisible(blockClicked)) {
        FlowPane blockchain = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
            .getVisualBlockchain();
        ScrollPane sp = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
            .getVisualBlockchainContainer();
        sp.setVvalue(blockClicked.localToParent(blockClicked.getBoundsInLocal()).getMinY() / blockchain.getHeight());
      }

      // Si le bloc actuellement sur le topLayer (animatedBlock) n'est pas le bloc
      // cliqué, on fait l'animation
      // du bloc réellement cliqué
      if (animatedBlock.getIndex() != blockClicked.getIndex()) {
        zoomIn(blockClicked, 1);
      }
      Duration d = Duration.millis(duration);
      ScaleTransition st = new ScaleTransition(d, animatedBlock);
      TranslateTransition tt = new TranslateTransition(d, animatedBlock);
      FadeTransition ft = new FadeTransition(d, animatedBlock);
      ParallelTransition pt = new ParallelTransition();

      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      st.setToX(1);
      st.setToY(1);
      tt.setToX(0);
      tt.setToY(0);

      pt.getChildren().setAll(st, tt);

      pt.setOnFinished(v -> {
        // Suppression du bloc animé du topLayer
        topLayer.getChildren().remove(0);
        // Suppression du topLayer de la scène
        root.getChildren().remove(1);
        boc.setBlockClicked(-1);
      });
      pt.play();
    }

    /**
     * Effectue l'animation d'agrandissement ou rétrécissement des blocs.
     *
     * @param direction     la direction voulue ('u' si up, 'd' si down)
     * @param duration      la durée de l'animation individuelle d'un bloc
     * @param totalDuration la durée totale de l'animation (en prenant en compte
     *                      tous les blocs)
     * @param delay         le délai de l'animation
     */
    private static void scaleAll(char direction, double duration, double totalDuration, double delay) {
      assert direction == 'u' || direction == 'd';

      FlowPane blockchain = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchain();
      VisualBlock blockClicked = ControllerManager
          .<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW).getBlockClicked();
      int indexBlockClicked = blockClicked == null ? -1 : blockClicked.getIndex();

      int nbBlocs = computeNbBlocksVisibles();

      // On calcule le délai entre chaque animation afin que l'animation ait la même
      // durée quel que soit le nombre de blocs
      double delayBetweenBlocs = totalDuration / nbBlocs;

      if (direction == 'u') {
        // On remet tous les blocs à leur taille originale
        blockchain.getChildren().forEach(b -> {
          b.setOpacity(1.0);
          b.setScaleX(1.0);
          b.setScaleY(1.0);
        });
      }

      // On cherche le premier bloc visible
      int i = 0;
      VisualBlock v = (VisualBlock) blockchain.getChildren().get(i);
      while (!isVisible(v)) {
        v = (VisualBlock) blockchain.getChildren().get(i);
        ++i;
      }
      if (i > 0)
        --i;

      // On anime les nbBlocs suivant à partir du premier visible
      int j = 0;
      while (j < nbBlocs && i < blockchain.getChildren().size()) {
        v = (VisualBlock) blockchain.getChildren().get(i);
        // Si v est le bloc cliqué, on ne l'anime pas
        if (i == indexBlockClicked) {
          v.setScaleX(1);
          v.setScaleY(1);
          v.setOpacity(1);
        } else {
          FadeTransition ft = new FadeTransition(Duration.millis(duration), v);
          ScaleTransition st = new ScaleTransition(Duration.millis(duration), v);
          ParallelTransition pt = new ParallelTransition();

          if (direction == 'u') {
            ft.setFromValue(0);
            ft.setToValue(1);
            st.setFromX(0.9);
            st.setFromY(0.9);
            st.setToX(1);
            st.setToY(1);
          } else if (direction == 'd') {
            ft.setFromValue(1);
            ft.setToValue(0);
            st.setFromX(1);
            st.setFromY(1);
            st.setToX(0.9);
            st.setToY(0.9);
          }

          pt.getChildren().addAll(ft, st);
          pt.setDelay(Duration.millis(delay + (j * delayBetweenBlocs)));
          pt.play();
        }
        ++i;
        ++j;
      }
    }

    /**
     * Demande à scaleAll d'agrandir les blocs.
     *
     * @param duration      la durée de l'animation individuelle d'un bloc
     * @param totalDuration la durée totale de l'animation (en prenant en compte
     *                      tous les blocs)
     * @param delay         le délai de l'animation
     */
    public static void scaleUpAll(double duration, double totalDuration, double delay) {
      scaleAll('u', duration, totalDuration, delay);
    }

    /**
     * Demande à scaleAll de rétrécir les blocs.
     *
     * @param duration      la durée de l'animation individuelle d'un bloc
     * @param totalDuration la durée totale de l'animation (en prenant en compte
     *                      tous les blocs)
     * @param delay         le délai de l'animation
     */
    public static void scaleDownAll(double duration, double totalDuration, double delay) {
      scaleAll('d', duration, totalDuration, delay);
    }

    /**
     * Permet d'animer le défilement vertical du ScrollPane pour se déplacer
     * jusqu'au bloc choisi.
     *
     * @param blockClicked le bloc qu'on veut afficher
     * @param duration     la durée de l'animation
     */
    public static void scroll(VisualBlock blockClicked, double duration) {
      FlowPane blockchain = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchain();
      ScrollPane sp = ControllerManager.<BlockchainOverviewController>getController(Screen.BLOCKCHAIN_OVERVIEW)
          .getVisualBlockchainContainer();
      Animation scrollAnimation = new Transition() {
        {
          setCycleDuration(Duration.millis(duration));
        }

        @Override
        protected void interpolate(double frac) {
          double startPosition = sp.getVvalue();
          double endPosition = blockClicked.localToParent(blockClicked.getBoundsInLocal()).getMinY()
              / blockchain.getHeight();
          double newPosition;

          if (endPosition > startPosition) {
            newPosition = endPosition * frac;
          } else {
            newPosition = endPosition / frac;
          }

          sp.setVvalue(newPosition);
        }
      };

      scrollAnimation.play();
    }
  }

  /**
   * Cette classe permet d'animer une InfoBar.
   */
  public static class InfoBarAnimator {

    /**
     * Animation à jouer.
     */
    private static TranslateTransition tt = new TranslateTransition();

    /**
     * Interpolateur utilisé pour l'animation.
     */
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.77, 0.1, 0.23, 0.9);

    /**
     * Effectue l'animation de l'apparition de l'infoBar.
     *
     * @param infoBar  l'infoBar à animer
     * @param duration la durée de l'animation
     * @param delay    le délai de l'animation
     * @param action   l'action à effectuer à la fin de l'animation
     */
    public static void show(InfoBar infoBar, double duration, double delay, EventHandler<ActionEvent> action) {
      double height = infoBar.getMaxHeight();
      tt.setDuration(Duration.millis(duration));
      tt.setNode(infoBar);
      tt.setInterpolator(INTERPOLATOR);
      tt.setFromY(height);
      tt.setToY(0);
      tt.setDelay(Duration.millis(delay));
      tt.setOnFinished(action);
      tt.playFromStart();
    }

    /**
     * Effectue l'animation de la disparition de l'infoBar.
     *
     * @param infoBar  l'infoBar à animer
     * @param duration la durée de l'animation
     * @param delay    le délai de l'animation
     * @param action   l'action à effectuer à la fin de l'animation
     */
    public static void hide(InfoBar infoBar, double duration, double delay, EventHandler<ActionEvent> action) {
      double height = infoBar.getMaxHeight();
      tt.setDuration(Duration.millis(duration));
      tt.setNode(infoBar);
      tt.setInterpolator(INTERPOLATOR);
      tt.setFromY(0);
      tt.setToY(height);
      tt.setDelay(Duration.millis(delay));
      tt.setOnFinished(action);
      tt.playFromStart();
    }
  }

  /**
   * Cette classe permet d'animer le bouton retour.
   */
  public static class BackButtonAnimator {

    /**
     * Durée de l'animation.
     */
    private static final double DURATION = 200d;

    /**
     * Effectue l'animation d'apparition du bouton.
     *
     * @param n le bouton à animer
     */
    public static void show(Node n) {
      FadeTransition ft = new FadeTransition(Duration.millis(DURATION), n);
      ScaleTransition st = new ScaleTransition(Duration.millis(DURATION), n);
      ParallelTransition pt = new ParallelTransition();

      ft.setInterpolator(Interpolator.EASE_OUT);
      st.setInterpolator(Interpolator.EASE_BOTH);

      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      st.setFromX(0.9);
      st.setFromY(0.9);
      st.setToX(1);
      st.setToY(1);

      pt.getChildren().setAll(ft, st);
      pt.play();
    }

    /**
     * Efefctue l'animation de disparition du bouton.
     *
     * @param n le bouton à animer
     * @param c l'action à effectuer à la fin de l'animation
     */
    public static void hide(Node n, Consumer<Void> c) {
      FadeTransition ft = new FadeTransition(Duration.millis(DURATION), n);
      ScaleTransition st = new ScaleTransition(Duration.millis(DURATION), n);
      ParallelTransition pt = new ParallelTransition();

      ft.setInterpolator(Interpolator.EASE_OUT);
      st.setInterpolator(Interpolator.EASE_BOTH);

      st.setFromX(1);
      st.setFromY(1);
      st.setToX(0.9);
      st.setToY(0.9);

      ft.setFromValue(1.0);
      ft.setToValue(0.0);

      pt.getChildren().setAll(ft, st);
      pt.setOnFinished(e -> c.accept(null));
      pt.play();
    }
  }

  /**
   * Cette classe permet d'animer un FlatButton.
   */
  public static class FlatButtonAnimator {

    /**
     * Durée de l'animation.
     */
    private static final double DURATION = 150d;

    /**
     * Permet de récupérer l'animation à effectuer au survol du bouton.
     *
     * @param b          le bouton à animer
     * @param startColor la couleur de départ
     * @param endColor   la couleur d'arrivée
     * @param radius     le rayon de courbure du bouton
     * @return l'animation correspondant au survol d'un bouton
     */
    public static Transition getHoveredAnimation(Button b, Color startColor, Color endColor, CornerRadii radius) {
      // On crée une transition de couleur sur un rectangle fictif pour animer
      // l'arrière-plan du bouton
      Shape s = new Rectangle();
      FillTransition tr1 = new FillTransition();
      tr1.setShape(s);
      tr1.setDuration(Duration.millis(DURATION));
      tr1.setFromValue(startColor);
      tr1.setToValue(endColor);

      tr1.setInterpolator(new Interpolator() {
        @Override
        protected double curve(double t) {
          // On applique la couleur du rectangle (calculée dans la FillTransition) à
          // l'arrière-plan du bouton
          b.setBackground(new Background(new BackgroundFill(s.getFill(), radius, Insets.EMPTY)));
          return t;
        }
      });

      // On crée une transition de couleur sur un rectangle fictif pour animer le
      // texte
      Shape s2 = new Rectangle();
      FillTransition tr2 = new FillTransition();
      tr2.setShape(s2);
      tr2.setDuration(Duration.millis(DURATION));
      tr2.setFromValue(endColor);
      tr2.setToValue(startColor);

      tr2.setInterpolator(new Interpolator() {
        @Override
        protected double curve(double t) {
          // On applique la couleur du rectangle au texte
          b.setTextFill(s2.getFill());
          return t;
        }
      });

      ParallelTransition pt = new ParallelTransition(tr1, tr2);

      pt.setOnFinished(e -> {
        // À la fin de l'animation on s'assure que la couleur finale est bien celle
        // voulue
        b.setBackground(new Background(new BackgroundFill(endColor, radius, Insets.EMPTY)));
        b.setTextFill(startColor);
      });

      return pt;
    }

  }

  /**
   * Cette classe permet d'animer un IconButton.
   */
  public static class IconButtonAnimator {

    /**
     * Échelle lors du survol.
     */
    private static final double SCALE_HOVERED = 1.3;

    /**
     * Échelle lors du clic.
     */
    private static final double SCALE_CLICKED = 1.2;

    /**
     * Durée de l'animation.
     */
    private static final double DURATION = 100d;

    /**
     * Effectue l'animation lors du survol d'un bouton.
     *
     * @param b le bouton à animer
     */
    public static void hovered(Button b) {
      ScaleTransition st = new ScaleTransition(Duration.millis(DURATION), b);
      st.setToX(SCALE_HOVERED);
      st.setToY(SCALE_HOVERED);
      st.play();
    }

    /**
     * Effectue l'animation lors de la sortie du survol d'un bouton.
     *
     * @param b le bouton à animer
     */
    public static void hoveredOut(Button b) {
      ScaleTransition st = new ScaleTransition(Duration.millis(DURATION), b);
      st.setToX(1.0);
      st.setToY(1.0);
      st.play();
    }

    /**
     * Effectue l'animation lors d'un clic sur un bouton.
     *
     * @param b le bouton à animer
     */
    public static void mousePressed(Button b) {
      ScaleTransition st = new ScaleTransition(Duration.millis(DURATION / 2), b);
      FillTransition ft = new FillTransition(Duration.millis(DURATION / 2), (Shape) b.getGraphic());
      ParallelTransition pt = new ParallelTransition();
      ft.setToValue(IconButton.CLICKED_COLOR);

      st.setToX(SCALE_CLICKED);
      st.setToY(SCALE_CLICKED);
      pt.getChildren().addAll(st, ft);
      pt.play();
    }

    /**
     * Effectue l'animation lors de la sortie du relâchement du clic sur un bouton.
     *
     * @param b          le bouton à animer
     * @param resetScale true si on veut remettre l'échelle à zéro, false sinon
     */
    public static void mouseReleased(Button b, boolean resetScale) {
      ScaleTransition st = new ScaleTransition(Duration.millis(DURATION / 2), b);
      FillTransition ft = new FillTransition(Duration.millis(DURATION * 3), (Shape) b.getGraphic());
      ParallelTransition pt = new ParallelTransition();
      ft.setToValue(IconButton.BASE_COLOR);

      if (resetScale) {
        st.setToX(1.0);
        st.setToY(1.0);
      } else {
        st.setToX(SCALE_HOVERED);
        st.setToY(SCALE_HOVERED);
      }
      pt.getChildren().addAll(st, ft);
      pt.play();
    }
  }

  /**
   * Cette classe permet d'animer un NumberPicker.
   */
  public static class NumberPickerAnimator {

    /**
     * L'animator utilisé pour l'animation.
     */
    private Animator.SimpleTransition animator = new Animator.SimpleTransition();

    /**
     * Effectue l'animation de changement de nombre.
     *
     * @param n        le Node à animer
     * @param t        la transition
     * @param duration la durée de l'animation
     * @param c        la fonction à appeler après l'animation de n
     */
    public void changeNumber(Node n, Transitions t, double duration, Consumer<Void> c) {
      animator.processTransition(n, n, t, duration, c);
    }

  }

  /**
   * Cette classe permet d'animer un LoadingSpinner.
   *
   * @author guill
   *
   */
  public static class LoadingSpinnerAnimator {

    /**
     * Longueur minimale du spinner (entre 0 et 360).
     */
    private static final double SPINNER_MIN_LENGTH = 10d;

    /**
     * Longueur maximale du spinner (entre 0 et 360).
     */
    private static final double SPINNER_MAX_LENGTH = 240d;

    /**
     * Permet de récupérer l'animation qui fait tourner le spinner.
     *
     * @param spinner   le spinner à animer
     * @param container le conteneur dans lequel se trouve le spinner
     * @param colors    les couleurs à appliquer (3 uniquement)
     * @return la Timeline de l'animation
     */
    public static Timeline getAnimationTimeline(Arc spinner, Pane container, Color[] colors) {
      assert colors.length == 3;

      Timeline timeline = new Timeline();
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.getKeyFrames().setAll(
          new KeyFrame(Duration.ZERO, new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(container.rotateProperty(), 0d)),
          new KeyFrame(Duration.millis(100), new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(spinner.strokeProperty(), colors[0]), new KeyValue(container.rotateProperty(), 50d)),
          new KeyFrame(Duration.millis(500), new KeyValue(spinner.lengthProperty(), SPINNER_MAX_LENGTH),
              new KeyValue(container.rotateProperty(), 500d)),
          new KeyFrame(Duration.millis(1000), new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(spinner.strokeProperty(), colors[0]), new KeyValue(container.rotateProperty(), 700d)),
          new KeyFrame(Duration.millis(1700), new KeyValue(container.rotateProperty(), 1080d),
              new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH)));

      timeline.getKeyFrames().addAll(
          new KeyFrame(Duration.millis(1800), new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(spinner.strokeProperty(), colors[1]), new KeyValue(container.rotateProperty(), 1130d)),
          new KeyFrame(Duration.millis(2200), new KeyValue(spinner.lengthProperty(), SPINNER_MAX_LENGTH),
              new KeyValue(container.rotateProperty(), 1580d)),
          new KeyFrame(Duration.millis(2700), new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(spinner.strokeProperty(), colors[1]), new KeyValue(container.rotateProperty(), 1780d)),
          new KeyFrame(Duration.millis(3400), new KeyValue(container.rotateProperty(), 2160d),
              new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH)));

      timeline.getKeyFrames().addAll(
          new KeyFrame(Duration.millis(3500), new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(spinner.strokeProperty(), colors[2]), new KeyValue(container.rotateProperty(), 2210d)),
          new KeyFrame(Duration.millis(3900), new KeyValue(spinner.lengthProperty(), SPINNER_MAX_LENGTH),
              new KeyValue(container.rotateProperty(), 2660d)),
          new KeyFrame(Duration.millis(4400), new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH),
              new KeyValue(spinner.strokeProperty(), colors[2]), new KeyValue(container.rotateProperty(), 2860d)),
          new KeyFrame(Duration.millis(5100), new KeyValue(container.rotateProperty(), 3240d),
              new KeyValue(spinner.lengthProperty(), SPINNER_MIN_LENGTH)));

      return timeline;
    }

  }

}
