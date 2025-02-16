package com.csse3200.game.components.maingame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private static GdxGame game;

  public MainGameActions(GdxGame game) {
    MainGameActions.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  public static void exitToMainMenu() {
    logger.debug("Exiting to main menu");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }
}
