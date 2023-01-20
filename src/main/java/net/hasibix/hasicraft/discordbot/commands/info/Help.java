package net.hasibix.hasicraft.discordbot.commands.info;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;

public class Help {
    public static void register() {
        Command command = new Command(
            "help",
            new String[]{},
            "Lists all commands",
            new Permission[]{},
            "Info",
            new OptionData[]{},
            (client, event, args) -> {
                event.getMessage().reply("Pong!").mentionRepliedUser(false).queue();
            },
            (client, event, args) -> {
                event.reply("Pong!").queue();
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
