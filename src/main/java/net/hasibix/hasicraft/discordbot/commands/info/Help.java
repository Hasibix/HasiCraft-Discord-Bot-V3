package net.hasibix.hasicraft.discordbot.commands.info;

import java.util.ArrayList;
import java.util.List;

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
                List<String> commandNames = new ArrayList<String>();
                for (Command i : HasiBot.commandHandler.commands) {
                    commandNames.add(i.name);
                }
                String[] commandNameArray = commandNames.toArray(new String[commandNames.size()]);
                String joined = String.join(",\n", commandNameArray);

                event.getMessage().reply(joined).mentionRepliedUser(false).queue();
            },
            (client, event, args) -> {
                List<String> commandNames = new ArrayList<String>();
                for (Command i : HasiBot.commandHandler.commands) {
                    commandNames.add(i.name);
                }
                String[] commandNameArray = commandNames.toArray(new String[commandNames.size()]);
                String joined = String.join(",\n", commandNameArray);

                event.reply(joined).queue();
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
