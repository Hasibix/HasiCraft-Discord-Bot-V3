package net.hasibix.hasicraft.discordbot.events;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hasibix.hasicraft.discordbot.Main;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;

public class Ready extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Main.client.SetActivity("hasi help | Play.HasiCraft.ml", HasiBot.ActivityType.Streaming, "https://twitch.tv/hasibixlive");
    }
}
