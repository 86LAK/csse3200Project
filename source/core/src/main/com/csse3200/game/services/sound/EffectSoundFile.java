package com.csse3200.game.services.sound;

public enum EffectSoundFile implements SoundFile {
    // TODO: Add enum declarations for all effect sound tracks
    TRACTOR_HONK("sounds/car-horn-6408.mp3"),
    TRACTOR_START_UP("sounds/tractor-start-up.wav"),
    SHOVEL("sounds/shovel.wav"),
    HOE("sounds/hoe.wav"),
    SCYTHE("sounds/hoe.wav"),
    WATERING_CAN("sounds/watering-can.wav"),
    FISHING_CAST("sounds/fishing-cast.wav"),
    FISHING_CATCH("sounds/applause.wav"),
    PLACE("sounds/place.wav"),
    GATE_INTERACT("sounds/gate-interact.wav"),
    IMPACT("sounds/Impact4.ogg"),
    INVENTORY_OPEN("sounds/open-bag-sound-effect.mp3"),
    ACID_BURN("sounds/weather/AcidBurn.wav"),
    BLIZZARD("sounds/weather/Blizzard.wav"),
    LIGHTNING_STRIKE("sounds/weather/LightningStrike.wav"),
    SOLAR_SURGE("sounds/weather/SolarSurge.wav"),
    STORM("sounds/weather/Storm.wav"),
    SURGE("sounds/weather/Surge.wav"),
    HOTKEY_SELECT("sounds/take-item-sound-effect.mp3"),
    GOD_DID("sounds/god-did.mp3");

    private final String filePath;

    EffectSoundFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
