package com.csse3200.game.components.npc;

import java.security.SecureRandom;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This the class for Tameable Component. These components should
 * only apply to animals. Tameable stats can be found in the NPCs.json file
 */
public class TamableComponent extends Component {
  private int numTimesFed; // tracker for how many times animal has been fed
  private int tamingThreshold; // tame threshold of the animal (found in NPCs.json)
  private double tamingProbability; // probability of taming the animal (found in NPC.json)
  private String favouriteFood; // animals favourite food (found in NPC.json)
  private boolean isTamed;
  private final Entity player;
  private SecureRandom random = new SecureRandom(); // https://rules.sonarsource.com/java/RSPEC-2119/
  private InventoryComponent playerInventory;
  private final Logger logger = LoggerFactory.getLogger(TamableComponent.class);

  /**
   * Constructor for the Tameable Component class
   * 
   * @param player            The main player/user entity
   * @param tamingThreshold   An integer that number of times the animal can be
   *                          fed to tame it.
   * @param tamingProbability A double that represents chances to tame the animal.
   * @param favouriteFood     The animals favourite food.
   */
  public TamableComponent(Entity player, int tamingThreshold, double tamingProbability,
      String favouriteFood) {
    this.numTimesFed = 0;
    this.tamingThreshold = tamingThreshold;
    this.tamingProbability = tamingProbability;
    this.favouriteFood = favouriteFood;
    this.isTamed = false;
    this.player = player;
    this.playerInventory = player.getComponent(InventoryComponent.class);
  }

  /**
   * Create the event and add it to the event hashmap so the function can be
   * called and the player can observe the effects.
   */
  @Override
  public void create() {
    entity.getEvents().addListener("feed", this::feedAnimal);
  }

  /**
   * Function to be called from the event handler.
   * This is the player's attempt to feed and tame the animal.
   * How the taming process works:
   * - The player can only feed the animal, if the player is holding the animal's
   *   favourite food.
   * - When fed, a random double decimal will be generated. If this
   *   decimal exceeds the animal's tame probability, the animal will then be tamed.
   *  - If not the player can continue feeding the animal.
   *  - When number of times fed exceeds the tame threshold, the animal will then automatically
   *    be tamed.
   *
   * Note:
   * This has dependencies on the players team and Items team as player must interact with animal
   * and item must be used to feed the animal.
   */
  private void feedAnimal() {
      EntityType animalType = this.getEntity().getType();
      String name = this.getEntity().getType().name();
      EffectSoundFile effect;
      switch(animalType) {
          case ASTROLOTL:
              effect = EffectSoundFile.ASTROLOTL_FEED;
              break;
          case CHICKEN:
              effect = EffectSoundFile.CHICKEN_FEED;
              break;
          default:
              effect = EffectSoundFile.COW_FEED;
      }
      logger.info("Fed " + name + "!");
      try {
          ServiceLocator.getSoundService().getEffectsMusicService().play(effect);
      } catch (Exception e) {
          logger.error("Failed to play animal sound", e);
      }

      if (isTamed) {
          // Handle Astrolotl adding oxygen
          if (animalType == EntityType.ASTROLOTL
                  && this.playerInventory.getHeldItem().getComponent(ItemComponent.class)
                  .getItemName().equals(favouriteFood)) {
              ServiceLocator.getPlanetOxygenService().addOxygen(75);
          }
          return;
      }

      //Ensures player is holding an item
      if (this.playerInventory.getHeldItem() == null) {
          return;
      }
      //If true, ensures that the player's held item has the ItemComponent class.
      if (this.playerInventory.getHeldItem().getComponent(ItemComponent.class) == null) {
          return;
      }
      // If so, we can check if player is holding the right item

      if (this.playerInventory.getHeldItem().getComponent(ItemComponent.class).getItemName().equals(favouriteFood)) {
          // Generate RNG number for taming
          double randomDecimal = generateRandomDecimal();

          // Try and tame the animal
          // Check how many times the player has tried to tame the animal
          // If player has already tried enough times, tame the animal (prevents frustration).
          // Use RNG to try and tame the animal
          if (numTimesFed == tamingThreshold || randomDecimal > tamingProbability) {
              logger.info("Tamed " + name + "!");
              try {
                  Thread.sleep(100); //Ensures that other audio finishes so this can ne played.
                  ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.TAMED_ANIMAL);
              } catch (Exception e) {
                  logger.error("Failed to play animal sound", e);
              }
              isTamed = true;
              ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.ANIMAL_TAMED.name());
              entity.getEvents().trigger("startTimedEffect", "tamed", 2f);
          }
          else {
              numTimesFed++;
              entity.getEvents().trigger("startTimedEffect", "fed", 2f);
          }
      }
  }


  /**
   * Function is used to generate a random double decimal when the animal has been
   * fed.
   * 
   * @return random double
   */
  private double generateRandomDecimal() {
    return this.random.nextDouble(); // https://rules.sonarsource.com/java/RSPEC-2119/
  }

  /**
   * Function is used to check to see if the animal has been tamed.
   * 
   * @return True if the animal has been tamed, else false.
   */
  public boolean isTamed() {
    return isTamed;
  }

  /**
   * Function is used to set the tame value of the animals.
   * This is used for testing purposes and will be deleted in later sprint.
   * 
   * @param value Boolean value that will be used to set to the istamed variable.
   */
  public void setTame(boolean value) {
    this.isTamed = value;
  }

  /**
   * Writes to the json boolean of tamed.
   * 
   * @param json which is a valid Json.
   */
  @Override
  public void write(Json json) {
    json.writeObjectStart(this.getClass().getSimpleName());
    json.writeValue("tamed", isTamed());
    json.writeObjectEnd();
  }

    @Override
    public void read(Json json, JsonValue jsonMap) {
      jsonMap = jsonMap.get("TamableComponent");
      isTamed = jsonMap.getBoolean("Tamed");
    }

    /**
     * Getter for favourite food
     *
     * @return favourite food
     */
    public String getFavouriteFood() {
      return favouriteFood;
    }
}
