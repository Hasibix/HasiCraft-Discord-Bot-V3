package net.hasibix.hasicraft.discordbot.commands.info;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Response;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message;

public class Ping {
    public static void register() {
        Command command = new Command(
            "ping",
            new String[]{},
            "Ping command",
            new Permission[]{},
            "Info",
            new OptionData[]{},
            (client, event, args) -> {
                Response.Reply(new Message("Pong!"), event.getMessage(), false);
            },
            (client, event, args) -> {
                Response.CommandReply(new Message("Pong!"), event, false);
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
