package net.hasibix.hasicraft.discordbot.models.client;

import javax.annotation.Nullable;

public class Configuration {
    public final String name;
    @Nullable public final Object value;

    public Configuration(String name, Object value) {
        this.name = name;
        this.value =  value;
    }

}
