package com.csse3200.game.screens;

import com.csse3200.game.services.sound.SoundFile;
import com.csse3200.game.services.sound.SoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.intro.IntroDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.*;

import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(IntroScreen.class);

    /**
     * An array of paths to image textures needed for this screen
     */
    private static final String[] introScreenAssets = {"images/intro_background_v2.png",
            "images/intro_planet.png", "images/crash-animation/Cockpit_Bottom.png",
            "images/crash-animation/Cockpit_Top.png", "images/crash-animation/bright_light.png"};
    private final GdxGame game;
    private final Renderer renderer;

    public IntroScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising controls screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerSoundService(new SoundService());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);

        loadAssets();
        createUI();
    }


    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        unloadAssets();
        ServiceLocator.clear();
    }

    /**
     * Load all the image textures required for this screen into memory
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(introScreenAssets);
        ServiceLocator.getResourceService().loadAll();
        
        // Load sound effects
        List<SoundFile> effects = new ArrayList<>();
        effects.add(EffectSoundFile.SHIP_RATTLE);
        effects.add(EffectSoundFile.SHIP_CRASH);
        effects.add(EffectSoundFile.LEGO_BREAK);
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().loadSounds(effects);
        } catch (InvalidSoundFileException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove all the loaded image textures from the ResouceService, and thus game memory.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(introScreenAssets);
    }

    /**
     * Creates the intro screen's ui including components for rendering ui elements to the screen
     * and capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new IntroDisplay(game))
                .addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
