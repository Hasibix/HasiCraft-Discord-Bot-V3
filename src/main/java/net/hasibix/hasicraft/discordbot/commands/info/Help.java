package net.hasibix.hasicraft.discordbot.commands.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Embed;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Pagination;

public class Help {
    public static void register() {
        Command command = new Command(
            "help",
            new String[]{},
            "Lists all commands.",
            new Permission[]{},
            "Info",
            new OptionData[]{},
            (client, event, args, logger) -> {
                List<String> commandNames = new ArrayList<String>();
                for (Command i : HasiBot.commandHandler.commands) {
                    commandNames.add(i.name);
                }
                String[] commandNameArray = commandNames.toArray(new String[commandNames.size()]);
                String joined = String.join(",\n", commandNameArray);

                Pagination pagination = new Pagination(client);

                Embed embed = new Embed()
                    .setTitle(client.getSelfUser().getName() + "\'s command list:", null)
                    .setColor(Color.red)
                    .setDescription("```\n" + joined + "\n```");
                
                Pagination.Page page = pagination.new Page(null, embed);
                pagination.addPage(page);
                pagination.Reply(event.getMessage(), false);
            },
            (client, event, args, logger) -> {
                Map<String, List<String>> commandzz = new HashMap<String, List<String>>();
                for (Command i : HasiBot.commandHandler.commands) {
                    if(!commandzz.containsKey(i.category)) {
                        commandzz.put(i.category, new ArrayList<String>());
                    }
                    commandzz.get(i.category).add(i.name);
                }

                Pagination pagination = new Pagination(client);
                List<Pagination.Page> pages = new ArrayList<Pagination.Page>(commandzz.size());

                int pageNumber = 0;

                commandzz.forEach((key, value) -> {
                    String[] commands = value.toArray(new String[value.size()]);
                    pages.add(
                        pagination.new Page(
                            null,
                            new Embed()
                                .setTitle(client.getSelfUser().getName() + "\'s command list:", null)
                                .setColor(Color.red)
                                .setDescription("**" + key.toUpperCase() +"**```\n" + String.join(",\n", commands) + "\n```")
                                .setFooter("Page "+ commandzz +"/" + commandzz.size(), null)
                        )
                    );
                    
                });

                for (Pagination.Page i : pages) {
                    pagination.addPage(i);
                }

                pagination.CommandReply(event);
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
