package com.csse3200.game.components.inventory;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/* Credit: LibGDX TooltipManager
* */
public class InstantTooltipManager extends com.badlogic.gdx.scenes.scene2d.ui.TooltipManager {
    static private InstantTooltipManager instance;
    static private Files files;

    public boolean animations = true;
    /** The maximum width of a {@link TextTooltip}. The label will wrap if needed. Default is Integer.MAX_VALUE. */

    float time = initialTime;
    public Stage stage;
    @Override
    public void showAction (Tooltip tooltip) {
        float actionTime = animations ? (time > 0 ? 0.5f : 0.15f) : 0.1f;
        tooltip.getContainer().addAction(parallel(fadeIn(actionTime, fade), scaleTo(1, 1, actionTime, Interpolation.fade)));

    }
    @Override
    public void hideAction (Tooltip tooltip) {
        tooltip.getContainer()
                .addAction(sequence(parallel(scaleTo(0.05f, 0.05f, 0, Interpolation.fade)), removeActor()));
    }

}
