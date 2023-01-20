package net.hasibix.hasicraft.discordbot.commands.info;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.Command;
import net.hasibix.hasicraft.discordbot.models.client.HasiBot;

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
                event.getMessage().reply("Pong!").queue();
            },
            (client, event, args) -> {
                event.reply("Pong!").queue();
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
