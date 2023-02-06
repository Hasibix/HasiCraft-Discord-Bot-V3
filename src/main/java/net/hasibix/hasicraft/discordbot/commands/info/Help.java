package net.hasibix.hasicraft.discordbot.commands.info;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Embed;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Pages;

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

                Pages pagination = new Pages();

                Embed embed = new Embed()
                    .setTitle(client.getSelfUser().getName() + "\'s command list:", null)
                    .setColor(Color.red)
                    .setDescription("```\n" + joined + "\n```");
                
                Pages.Page page = pagination.new Page(null, embed);

                pagination.AddPage(page);
                pagination.Reply(event.getMessage(), false);
            },
            (client, event, args) -> {
                List<String> commandNames = new ArrayList<String>();
                for (Command i : HasiBot.commandHandler.commands) {
                    commandNames.add(i.name);
                }
                String[] commandNameArray = commandNames.toArray(new String[commandNames.size()]);
                String joined = String.join(",\n", commandNameArray);

                Pages pagination = new Pages();

                Embed embed = new Embed()
                    .setTitle(client.getSelfUser().getName() + "\' command list:", null)
                    .setColor(Color.red)
                    .setDescription("```\n" + joined + "\n```");
                
                Pages.Page page = pagination.new Page(null, embed);

                pagination.AddPage(page);
                pagination.CommandReply(event);
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
