package net.hasibix.hasicraft.discordbot.commands.info;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.Command;

public class Ping {
    public static Command register() {
        Command command = new Command(
            "ping",
            new String[]{},
            "Ping command",
            new Permission[]{},
            "Info",
            new OptionData[]{},

            (jda, event, args) -> {
                event.getMessage().reply("Pong!");
            },
            (jda, event, args) -> {
                event.reply("Pong!");
            }
        );

        return command;
    }
}
