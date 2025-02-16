package com.csse3200.game.components.maingame.mainmenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;

  public MainMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("control", this::onControls);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("credits", this::onCredits);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    //Check ask if the player is sure they want to start a new game
    game.setScreen(GdxGame.ScreenType.INTRO);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load game");
    game.setScreen(GdxGame.ScreenType.LOAD_GAME);
  }

  /**
   * opens controls screen
   */
  private void onControls() {
    logger.info("Launching control screen");
    game.setScreen(GdxGame.ScreenType.CONTROLS);
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.SETTINGS);
  }

  /**
   * Swaps to the Credits screen.
   */
  private void onCredits() {
    logger.info("Play Credits");
    game.setScreen(GdxGame.ScreenType.ENDCREDITS);
  }
}
