package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

public class FertiliseCropTilesQuest extends Quest {

    private final int target;
    private int numberOfTilesFertilised;

    public FertiliseCropTilesQuest(String name, Reward reward, int numberOfTilesToFertilise) {
        super(name, reward);

        target = numberOfTilesToFertilise;
        numberOfTilesFertilised = 0;
    }

    /**
     * Creates a {@link FertiliseCropTilesQuest}.
     * @param name The name of the {@link FertiliseCropTilesQuest}
     * @param reward The {@link Reward} for completion of the {@link Quest}
     * @param expiryDuration The time limit to complete the {@link Quest} once accepted
     * @param numberOfTilesToFertilise The number of crop tiles to fertilise before completing the
     *                                 {@link Quest}
     */
    public FertiliseCropTilesQuest(String name, Reward reward, int expiryDuration, int numberOfTilesToFertilise) {
        super(name, reward, expiryDuration, false);

        target = numberOfTilesToFertilise;
        numberOfTilesFertilised = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(
                MissionManager.MissionEvent.FERTILISE_CROP.name(),
                this::updateState);
    }

    /**
     * Returns true if the number of crop tiles the player has fertilised is greater than or equal
     * to the target number of tiles.
     * @return True if the player has fertilised the target number of fertilised tiles.
     */
    @Override
    public boolean isCompleted() {
        return numberOfTilesFertilised >= target;
    }

    @Override
    public String getDescription() {
        return "Fertilising crop tiles will cause your plants to grow faster.\nApply fertiliser to "
                + target + " tiles and become a " + getName() + "!\n"
                + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        return numberOfTilesFertilised + " out of " + target + " crop tiles fertilised";
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfTilesFertilised = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfTilesFertilised;
    }

    /**
     * Increments the number of crop tiles the player has fertilised.
     */
    private void updateState() {
        if (++numberOfTilesFertilised >= target) {
            numberOfTilesFertilised = target;
        }
        notifyUpdate();
    }

    @Override
    protected void resetState() {
        numberOfTilesFertilised = 0;
    }

}
