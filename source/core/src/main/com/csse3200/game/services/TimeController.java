package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.services.GameTimeDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameTime;

/**
 * Repsonsible for controlling and updating classes dependent on the time
 */
public class TimeController {

    /** The current GameTime service */
    private GameTime timeSource;

    /** Clock UI dependent on time*/
    private GameTimeDisplay timeDisplay;

    /** The current hour in the game*/
    private int hour;

    /** if paused then 1 else 0 */
    private boolean paused;

    /** Time that the game was last paused at */
    private long pausedAt;

    /**
     * Constructor used to initalise important values and define initial states for the TimeController class
     *
     * @param timeSource: The current GameTime service for this instance
     */
    public TimeController(GameTime timeSource) {
        this.timeSource = timeSource;
        // initally the game is not paused
        this.hour = 0;
        this.paused = false;
    }

    /**
     * Logs the current time UI under this TimeController
     *
     * @param timeDisplay: The class that controls the clock and time display
     */
    public void setTimeDisplay(GameTimeDisplay timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    /**
     * @return the active time of the game in seconds
     */
    public int getTimeInSeconds() {
        return (int) timeSource.getActiveTime() / 1000;
    }

    /**
     * @return the time of the day in milliseconds
     */
    public int getTimeOfDay() {
        return (int) timeSource.getActiveTime() % 720000;
    }

    /**
     * @return the current hour of the game
     */
    public int getHour() {
        return (int) Math.floor(getTimeOfDay() / 30000);
    }

    /**
     *  Updates the clock and time display by calling the update() method in GameTimeDisplay
     */
    public void updateDisplay() {
        // If the game is paused there is no reason to update the UI
        if (paused == false) {
            // Each day is 12minutes so 720000 milliseconds is one day
            int timeInDay = (int) timeSource.getActiveTime() % 720000;
            // 30 seconds is each hour so 30000 is one hour
            this.hour = (int) Math.floor(timeInDay / 30000);

            timeDisplay.update(this.hour);
        }
    }

    /**
     * Pauses the game
     */
    public void pause() {
        // Wont pause if its already paused
        if (paused == false) {
            this.paused = true;
            this.pausedAt = timeSource.getTime();
        }
    }

    /**
     * Unpauses the game
     */
    public void unpause() {
        // Wont unpause if not paused
        if (paused == true) {
            this.paused = false;
            // Calculates the difference between the current time and the time that the game was paused at
            // to get the pause length.
            // Adds that to the pause offset in gameTime which will be subtracted from the total game time
            // to get active time.
            timeSource.addPauseOffset(timeSource.getTimeSince(this.pausedAt));
        }
    }

    // Sets the game time and display to a specific time
    public void setTime(int hour) {
        this.hour = hour;
        timeDisplay.update(this.hour);
    }
}