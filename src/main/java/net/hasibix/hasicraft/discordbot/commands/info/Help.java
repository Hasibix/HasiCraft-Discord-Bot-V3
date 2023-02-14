package net.hasibix.hasicraft.discordbot.commands.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Embed;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Pagination;
import net.hasibix.hasicraft.discordbot.utils.Config;

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
                Map<String, List<String>> commandzz = new HashMap<String, List<String>>();
                Map<Integer, String> categories = new HashMap<Integer, String>();
                for (Command i : HasiBot.commandHandler.commands) {
                    commandzz.putIfAbsent(i.category, new ArrayList<String>());
                    commandzz.get(i.category).add(i.name);
                }

                for(int i = 0; i < commandzz.keySet().size(); i++) {
                    categories.putIfAbsent(i, commandzz.keySet().toArray(new String[commandzz.keySet().size()])[i]);
                }

                Pagination pagination = new Pagination(client, true, true)
                    .setDeleteOnTimeout(true)
                    .setChannel(event.getChannel().getIdLong())
                    .setTimeout(45)
                    .setTimeoutUnit(TimeUnit.SECONDS)
                    .setCondition(e -> { return event.getMember().getId() != null; })
                    .setEmoji("FirstPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_first_page", String.class)))
                    .setEmoji("PreviousPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_previous_page", String.class)))
                    .setEmoji("CancelButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_cancel", String.class)))
                    .setEmoji("NextPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_next_page", String.class)))
                    .setEmoji("LastPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_last_page", String.class)));
                List<Pagination.Page> pages = new ArrayList<Pagination.Page>(commandzz.size());

                for (int i = 0; i < commandzz.size(); i++) {
                    String category = categories.get(i);
                    String[] commands = commandzz.get(category).toArray(new String[commandzz.get(category).size()]);
                    pages.add(
                        pagination.new Page(
                            null,
                            new Embed()
                                .setTitle(client.getSelfUser().getName() + "\'s command list:", null)
                                .setColor(Color.red)
                                .setDescription("**" + category.toUpperCase() +"**```\n" + String.join(",\n", commands) + "\n```")
                                .setFooter("Page "+ (i + 1) +"/" + commandzz.size(), null)
                        )
                    );
                }

                for (Pagination.Page i : pages) {
                    pagination.addPage(i);
                }
                
                pagination.Reply(event.getMessage(), false);
            },
            (client, event, args, logger) -> {
                Map<String, List<String>> commandzz = new HashMap<String, List<String>>();
                Map<Integer, String> categories = new HashMap<Integer, String>();
                for (Command i : HasiBot.commandHandler.commands) {
                    commandzz.putIfAbsent(i.category, new ArrayList<String>());
                    commandzz.get(i.category).add(i.name);
                }

                for(int i = 0; i < commandzz.keySet().size(); i++) {
                    categories.putIfAbsent(i, commandzz.keySet().toArray(new String[commandzz.keySet().size()])[i]);
                }

                Pagination pagination = new Pagination(client, true, true)
                    .setDeleteOnTimeout(true)
                    .setChannel(event.getChannel().getIdLong())
                    .setTimeout(45)
                    .setTimeoutUnit(TimeUnit.SECONDS)
                    .setCondition( e -> { return event.getMember().getIdLong() != 0; })
                    .setEmoji("FirstPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_first_page", String.class)))
                    .setEmoji("PreviousPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_previous_page", String.class)))
                    .setEmoji("CancelButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_cancel", String.class)))
                    .setEmoji("NextPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_next_page", String.class)))
                    .setEmoji("LastPageButton", Emoji.fromMarkdown(Config.get("emoji", "pagination_last_page", String.class)));
                List<Pagination.Page> pages = new ArrayList<Pagination.Page>(commandzz.size());

                for (int i = 0; i < commandzz.size(); i++) {
                    String category = categories.get(i);
                    String[] commands = commandzz.get(category).toArray(new String[commandzz.get(category).size()]);
                    pages.add(
                        pagination.new Page(
                            null,
                            new Embed()
                                .setTitle(client.getSelfUser().getName() + "\'s command list:", null)
                                .setColor(Color.red)
                                .setDescription("**" + category.toUpperCase() +"**```\n" + String.join(",\n", commands) + "\n```")
                                .setFooter("Page "+ (i + 1) +"/" + commandzz.size(), null)
                        )
                    );
                }

                for (Pagination.Page i : pages) {
                    pagination.addPage(i);
                }

                pagination.CommandReply(event);
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }
}
