package com.csse3200.game.ui.terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.csse3200.game.ui.terminal.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.services.ServiceLocator;

/**
 * State tracker for a debug terminal. Any commands to be actioned through the terminal input should
 * be added to the map of commands.
 */
public class Terminal extends Component {
  private static final Logger logger = LoggerFactory.getLogger(Terminal.class);
  private final Map<String, Command> commands;
  private String enteredMessage = "";
  private boolean isOpen = false;

  public Terminal() {
    this(new HashMap<>());
  }

  public Terminal(Map<String, Command> commands) {
    this.commands = commands;

    addCommand("debug", new DebugCommand());
    addCommand("setTime", new SetTimeCommand());
    addCommand("save", new SaveCommand());
    addCommand("load", new LoadCommand());
    addCommand("spawn", new SpawnCommand());
    addCommand("setDay", new SetDayCommand());
    addCommand("addItem", new AddItemCommand());
    addCommand("removeItem", new RemoveItemCommand());
    addCommand("addWeather", new AddWeatherCommand());
    addCommand("plant", new PlantCommand());
    addCommand("setTimeScale", new SetTimeScaleCommand());
    addCommand("dialogueScreen", new DialogueScreenCommand());
  }

  /** @return message entered by user */
  public String getEnteredMessage() {
    return enteredMessage;
  }

  /** @return console is open */
  public boolean isOpen() {
    return isOpen;
  }

  /**
   * Toggles between the terminal being open and closed.
   */
  public void toggleIsOpen() {
    if (isOpen) {
      this.setClosed();
    } else {
      this.setOpen();
    }
  }

  /**
   * Opens the terminal.
   */
  public void setOpen() {
    logger.debug("Opening terminal");
    if (ServiceLocator.getGameArea() != null) {
      if (ServiceLocator.getGameArea().getPlayer() != null) {
        ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class).stopRunning();
        ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class).stopMoving();
      }
      if (ServiceLocator.getGameArea().getTractor() != null) {
        ServiceLocator.getGameArea().getTractor().getComponent(TractorActions.class).stopMoving();
      }
    }
    isOpen = true;
  }

  /**
   * Closes the terminal and clears the stored message.
   */
  public void setClosed() {
    logger.debug("Closing terminal");
    isOpen = false;
    if (ServiceLocator.getGameArea() != null) {
      if (ServiceLocator.getGameArea().getPlayer() != null) {
        ServiceLocator.getGameArea().getPlayer().getComponent(KeyboardPlayerInputComponent.class).setWalkDirection(Vector2.Zero);
      }
      if (ServiceLocator.getGameArea().getTractor() != null) {
        ServiceLocator.getGameArea().getTractor().getComponent(KeyboardTractorInputComponent.class).setWalkDirection(Vector2.Zero);
      }
    }
    setEnteredMessage("");
  }

  /**
   * Adds a command to the list of valid terminal commands.
   *
   * @param name command name
   * @param command command
   */
  public void addCommand(String name, Command command) {
    logger.debug("Adding command: {}", name);
    if (commands.containsKey(name)) {
      logger.error("Command {} is already registered", name);
    }
    commands.put(name, command);
  }

  /**
   * Processes the completed message entered by the user. If the message corresponds to a valid
   * command, the command will be actioned.
   * @return true if command handled, false otherwise
   */
  public boolean processMessage() {
    logger.debug("Processing message");
    // strip leading and trailing whitespace
    String message = enteredMessage.trim();

    // separate command from args
    String[] sections = message.split(" ");
    String command = sections[0];

    ArrayList<String> args = new ArrayList<>(Arrays.asList(sections).subList(1, sections.length));

    if (commands.containsKey(command)) {
      setEnteredMessage("");
      return commands.get(command).action(args);
    }
    return false;
  }

  /**
   * Appends the character to the end of the entered message.
   *
   * @param character character to append
   */
  public void appendToMessage(char character) {
    logger.debug("Appending '{}' to message", character);
    enteredMessage = enteredMessage + character;
  }

  /** Removes the last character of the entered message. */
  public void handleBackspace() {
    logger.debug("Handling backspace");
    int messageLength = enteredMessage.length();
    if (messageLength != 0) {
      enteredMessage = enteredMessage.substring(0, messageLength - 1);
    }
  }

  /**
   * Sets the text shown on the terminal
   * @param text Text to show
   */
  public void setEnteredMessage(String text) {
    enteredMessage = text;
  }
}
