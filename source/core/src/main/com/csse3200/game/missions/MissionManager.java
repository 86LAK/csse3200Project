package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MissionManager {

	/**
	 * An enum storing all possible events that the {@link MissionManager}'s {@link EventHandler} should listen to and
	 * trigger. To add a listener to the {@link MissionManager}, create a new {@link MissionEvent} enum value, and add
	 * a listener for the {@link #name()} of the enum value.
	 */
	public enum MissionEvent {
		PLANT_CROP,
		FERTILISE_CROP
	}

	private static final Achievement[] achievements = new Achievement[]{};
	private static final ArrayList<Quest> quests = new ArrayList<>();
	private static final EventHandler events = new EventHandler();

	/**
	 * Creates the mission manager, registered all game achievements and adds a listener for hourly updates
	 */
	public MissionManager() {
		for (Achievement mission : achievements) {
			mission.registerMission(events);
		}
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateHour);
	}

	/**
	 * Adds a quest to the list of active quests in the game.  Also registers this quest in the game
	 * @param quest quest to be added and registered
	 */
	public void addQuest(Quest quest) {
		quests.add(quest);
		quest.registerMission(events);
	}

	/**
	 * Method that is called every in-game hour which loops through the active quests and updates their expiry time
	 */
	private void updateHour() {
		for (Quest quest : quests) {
			quest.updateExpiry(1);
			if (quest.isExpired()) {
				// Game over
			}
		}
	}

}
