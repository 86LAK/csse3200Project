package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.inventory.InventoryDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InventoryDisplayManager {
    private final List<InventoryDisplay> inventoryDisplays;
    private final Stage stage;

    private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);

    /**
     * Initialise the Inventory Display Manager
     * @param stage to place display on
     */
    public InventoryDisplayManager(Stage stage) {
        this.inventoryDisplays = new ArrayList<>();
        this.stage = stage;
    }

    /**
     * Add an inventoryDisplay to the manager
     * @param inventoryDisplay display to be added
     */
    public void addInventoryDisplay(InventoryDisplay inventoryDisplay) {
        this.inventoryDisplays.add(inventoryDisplay);
    }

    /**
     * Remove an inventoryDisplay to the manager
     * @param inventoryDisplay display to be removed
     */
    public void removeInventoryDisplay(InventoryDisplay inventoryDisplay) {
        this.inventoryDisplays.remove(inventoryDisplay);
    }

    /**
     * Remove an inventoryDisplay to the manager
     */
    public List<InventoryDisplay> getInventoryDisplays() {
        return this.inventoryDisplays;
    }

    /**
     * Update the position of the displays
     */
    public void updateDisplays() {
        int displayCount = inventoryDisplays.size();

        if (displayCount == 1) {
            Window window = (Window) inventoryDisplays.get(0).getWindow();
            window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
        }
        else if (displayCount == 2){
            InventoryDisplay displayOne = inventoryDisplays.get(0);
            InventoryDisplay displayTwo = inventoryDisplays.get(1);

            if (displayOne.isOpen() && displayTwo.isOpen()) {
                float totalHeight = displayOne.getWindow().getHeight() + displayTwo.getWindow().getHeight();

                float yOne = (stage.getHeight() - totalHeight - 50) / 2;
                float yTwo = yOne + displayOne.getWindow().getHeight() + 50;

                displayOne.getWindow().setPosition(stage.getWidth() / 2 - displayOne.getWindow().getWidth() / 2, yOne);
                displayTwo.getWindow().setPosition(stage.getWidth() / 2 - displayTwo.getWindow().getWidth() / 2, yTwo);
            }
        }
    }
}