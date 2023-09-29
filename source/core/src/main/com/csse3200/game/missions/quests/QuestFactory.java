package com.csse3200.game.missions.quests;

import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.*;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestFactory {

    public static final String firstContactQuestName = "First Contact";
    public static final String clearingYourMessQuestName = "Clearing Your Mess";
    public static final String sowingYourFirstSeedsQuestName = "Sowing Your First Seeds";
    public static final String reapingYourRewardsQuestName = "Reaping Your Rewards";
    public static final String makingFriendsQuestName = "Making Friends";
    public static final String fertilisingFiestaQuestName = "Fertilising Fiesta";
    public static final String aliensAttackQuestName = "Aliens Attack";
    public static final String actIMainQuestName = "An Agreement";
    public static final String shipRepairsQuestName = "Ship Repairs";
    public static final String homeSickQuestName = "Home Sick";
    public static final String bringingItAllTogether = "Bringing It All Together";
    public static final String actIIMainQuestName = "Making Contact";
    public static final String airAndAlgaeQuestName = "Air and Algae";
    public static final String stratosphericSentinel = "Stratospheric Sentinel";
    public static final String actIIIMainQuestName = "Weather the Storm";

    public static AutoQuest createFirstContactQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createClearingYourMessQuest());
        questsToActivate.add(createActIMainQuest());

        String dialogue = """
                WHAT HAVE YOU DONE!!!
                {WAIT}
                CLEAR UP THIS MESS AT ONCE, OR {COLOUR=red}THERE WILL BE CONSEQUENCES{COLOUR=white}!!!
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createShovel())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new AutoQuest(firstContactQuestName, reward, "Wake up after your crash landing.");
    }

    public static ClearDebrisQuest createClearingYourMessQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createSowingYourFirstSeedsQuest());

        String dialogue = """
                Good. But your {WAIT} "landing" {WAIT} has completely destroyed my crops!
                {WAIT}
                Take this hoe and these seeds and start replanting the crops you destroyed.
                {WAIT}
                You have 6 hours to plant 12 Cosmic Corn.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createHoe(), ItemFactory.createCosmicCobSeed())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new ClearDebrisQuest(clearingYourMessQuestName, reward, 15);
    }

    public static PlantInteractionQuest createSowingYourFirstSeedsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createReapingYourRewardsQuest());

        String dialogue = """
                Impressive. I see that you can follow basic instructions.
                {WAIT}
                *sigh*
                {WAIT}
                I apologise for my hostility.
                {WAIT}
                My name is ALIEN NPC. I am an engineer of the ALIEN SPECIES people.
                {WAIT}
                I think I might be able to help you out, and get you back to wherever you came from. However, your crash still destroyed all that I was working on. So I'm not going to help you until you show that you are willing to help me.
                {WAIT}
                I have a few more tasks for you to do, and then I might consider helping you out.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createScythe(), ItemFactory.createWateringcan())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(sowingYourFirstSeedsQuestName, reward, MissionManager.MissionEvent.PLANT_CROP,
                Set.of("Cosmic Cob"), 12);
    }

    public static PlantInteractionQuest createReapingYourRewardsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createMakingFriendsQuest());

        String dialogue = """
                Ahhh, well done. The Cosmic Cob is a favourite of my people.
                {WAIT}
                I thank you, truly.
                {WAIT}
                But there are more plants that were in my original collection before it was destroyed.
                {WAIT}
                Atomic Algae <TO BE ADDED>
                {WAIT}
                Also...
                {WAIT}
                You might find your harvested crops desirable by our local wildlife.
                {WAIT}
                Who knows, maybe you will be able to get something from them if you treat them nicely.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createCowFood()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new PlantInteractionQuest(reapingYourRewardsQuestName, reward, MissionManager.MissionEvent.HARVEST_CROP,
                Set.of("Cosmic Cob"), 12);
    }

    public static TameAnimalsQuest createMakingFriendsQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createFertilisingFiestaQuest());

        String dialogue = """
                So you've met our kind fauna.
                {WAIT}
                Good. You're starting to learn.
                {WAIT}
                You might have noticed that once you treat our wildlife kindly, they drop rewards for you.
                {WAIT}
                Why don't you try using them?
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(ItemFactory.createFertiliser())),
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));
        return new TameAnimalsQuest(makingFriendsQuestName, reward, 1);
    }

    public static FertiliseCropTilesQuest createFertilisingFiestaQuest() {
        List<Quest> questsToAdd = new ArrayList<>();
        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createAliensAttackQuest());

        String dialogue = """
                Well done! You seem to really be getting the hang of this.
                {WAIT}
                There is one more thing you must do, however, before I can truly trust you.
                {WAIT}
                Some of our wildlife does not take too kindly to aliens from outer space invading our plant.
                {WAIT}
                And to be honest, a lot are even pests for us locals.
                {WAIT}
                A few of these hostile creatures will be attacking soon.
                {WAIT}
                Take this WEAPON, and defend our crops.
                {WAIT}
                I've also given you a seed for a special type of plant, the Space Snapper, which will eat up these types of hostiles once grown to maturity.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        // TODO - Add weapon to defeat incoming enemies
                        ItemFactory.createSpaceSnapperSeed()
                )),
                new QuestReward(questsToAdd, questsToActivate),
                new TriggerHostilesReward(List.of(
                        // TODO - Add extra hostiles
                        NPCFactory.createOxygenEater(ServiceLocator.getGameArea().getPlayer()),
                        NPCFactory.createOxygenEater(ServiceLocator.getGameArea().getPlayer()),
                        NPCFactory.createOxygenEater(ServiceLocator.getGameArea().getPlayer())
                )),
                new DialogueReward(dialogue)
        ));
        return new FertiliseCropTilesQuest(fertilisingFiestaQuestName, reward, 12);
    }

    public static ManageHostilesQuest createAliensAttackQuest() {
        String dialogue = """
                Impressive.
                {WAIT}
                Well, I suppose you have shown me that you can be trusted.
                {WAIT}
                I give you these seeds as boons for your good work.
                {WAIT}
                The trusty Hammer Plant shall heal all creatures and plants around it.
                {WAIT}
                Aloe Vera yields a gel which can heal even the gravest of injuries.
                {WAIT}
                Seeing as though you have been obedi- generous in offering your help to reconstruct what was lost, I am ready to help you.
                {WAIT}
                Come speak with me when you are ready, and we can talk about repairing that ship of yours.
                """;

        MultiReward reward = new MultiReward(List.of(
                new ItemReward(List.of(
                        ItemFactory.createHammerPlantSeed(),
                        ItemFactory.createAloeVeraSeed()
                )),
                new DialogueReward(dialogue)
        ));
        return new ManageHostilesQuest(aliensAttackQuestName, reward, Set.of(EntityType.OxygenEater), 5);
    }

    public static MainQuest createActIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();

        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createActIIMainQuest());

        String dialogue = """
                Well done, human. You have shown me that you can be trusted, and that our cooperation may be mutually beneficial.
                {WAIT}
                Now, {WAIT=0.5} let's see if we can do something about that Ship of yours.
                I understand you likely did not intend to crash here - my guess is the recent {COLOUR=red}Solar Surge{COLOUR=white} is to blame?
                {WAIT}
                You begin to explain what happened, how you lost contact with the Mothership, how your controls failed and how you fell into an uncontrolled descent onto the planet's surface.
                {WAIT}
                I see... {WAIT=1} The storm has also devastated our planet, and is part of the reason I was sent here.
                {WAIT}
                As you continue to go over the details, a ravenous anxiety begins to claw at your chest as the possibility of you becoming stranded forever becomes a terrifying possibility.
                {WAIT}
                ... TO BE ADDED
                """;

        Set<String> requiredQuests = new HashSet<>();
        requiredQuests.add(clearingYourMessQuestName);
        requiredQuests.add(sowingYourFirstSeedsQuestName);
        requiredQuests.add(reapingYourRewardsQuestName);
        requiredQuests.add(makingFriendsQuestName);
        requiredQuests.add(fertilisingFiestaQuestName);
        requiredQuests.add(aliensAttackQuestName);

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(actIMainQuestName, reward, 5, requiredQuests, "gain ALIEN NPC's trust");
    }

    public static MainQuest createActIIMainQuest() {
        List<Quest> questsToAdd = new ArrayList<>();

        List<Quest> questsToActivate = new ArrayList<>();
        questsToActivate.add(createActIIIMainQuest());

        String dialogue = """
                With the final pieces of the ship in place, a faint static buzzes from the radio.
                {WAIT}
                You tune the radio into the Mothership's frequency, and send your SOS message, in hopes it will be heard...
                {WAIT=2}
                "Hello, is that you PLAYER NAME?"
                {WAIT}
                You eagerly reply...
                {WAIT=0.5}
                "Hello? {WAIT} We're struggling to hear what you are saying."
                {WAIT}
                "Listen, if you can hear us, we're in major trouble. Our sensors have been picking up a major increase in solar activity."
                {WAIT}
                "We predict in 15 days there will be a Solar Storm so intense that it could wipe out our entire fleet."
                {WAIT}
                "Our only chance is to land on your planet, but even then our life-support systems will likely fail."
                {WAIT}
                "We need you to do all you can to ensure the atmosphere is at least survivable for us when we land."
                {WAIT}
                Taken aback by all this information, you attempt to speak to them again, but just as you go to speak, the sky lights up with the power of the sun, and the radio cuts out...
                """;

        Set<String> requiredQuests = new HashSet<>();

        MultiReward reward = new MultiReward(List.of(
                new QuestReward(questsToAdd, questsToActivate),
                new DialogueReward(dialogue)
        ));

        return new MainQuest(actIIMainQuestName, reward, 10, requiredQuests, "make connection with the Mothership");
    }

    public static MainQuest createActIIIMainQuest() {
        Set<String> requiredQuests = new HashSet<>();
        MultiReward reward = new MultiReward(List.of());
        return new MainQuest(actIIIMainQuestName, reward, 15, requiredQuests, "weather the storm");
    }

    public static FertiliseCropTilesQuest createHaberHobbyist() {
        // To be decided
        ItemReward reward = new ItemReward(new ArrayList<>());
        return new FertiliseCropTilesQuest("Haber Hobbyist", reward, 24, 10);
    }

    public static FertiliseCropTilesQuest createFertiliserFanatic() {
        // To be decided
        ItemReward reward = new ItemReward(new ArrayList<>());
        return new FertiliseCropTilesQuest("Fertiliser Fanatic", reward, 48, 40);
    }

}
