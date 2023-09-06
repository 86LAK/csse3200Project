package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Screen;
public class ToolbarDisplay extends UIComponent {

    private Table table;
    private Window window;
    private boolean isOpen;

    private InventoryComponent inventory;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        isOpen = true;
        entity.getEvents().addListener("updateInventory", this::updateInventory);
        entity.getEvents().addListener("toggleInventory",this::toggleOpen);
        inventory = entity.getComponent(InventoryDisplay.class).getInventory();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        table = new Table(skin);
        table.defaults().size(64, 64);

        for (int i = 0; i < 10; i++) {

            Label label = new Label(String.valueOf(i) + " ", skin); //please please please work
            label.setColor(Color.WHITE);
            label.setAlignment(Align.topLeft);

            ItemSlot item = new ItemSlot(new Texture("images/itemFrame.png"), 0);
            item.add(label);
            table.add(item).pad(10, 10, 10, 10).fill();

            Label itemlabel = new Label(String.valueOf(i+1), skin.get("default", Label.LabelStyle.class));
            itemlabel.setAlignment(Align.topLeft);
            table.add(itemlabel);
        }

        // Create a window for the inventory using the skin
        window = new Window("", skin);
        window.pad(40, 5, 5, 5); // Add padding to with so that the text doesn't go offscreen
        window.add(table); //Add the table to the window
        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0); // Clip to the bottom of the window on the stage
        window.setVisible(true);
        // Add the window to the stage
        stage.addActor(window);
    }

    /**
     * Draw stage for render
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Toggle Toolbar to open state
     */
    public void toggleOpen(){
        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
        } else {
            window.setVisible(true);
            isOpen = true;
        }
    }

    /**
     * Updates the player's inventory toolbar on the ui.
     */
    public void updateInventory() {
        inventory = entity.getComponent(InventoryDisplay.class).getInventory();
        // refresh the ui as per the new inventory.
    }

    /**
     * Dispose of Toolbar
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    public void updateItemSlot(int slotNum) {

    }
}
