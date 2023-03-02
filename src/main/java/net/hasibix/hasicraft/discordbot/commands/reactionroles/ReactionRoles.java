package net.hasibix.hasicraft.discordbot.commands.reactionroles;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Response;

public class ReactionRoles {
    public static void register() {
        Command command = new Command(
            "reactionroles",
            new String[]{"rr"},
            "Reaction roles commands.",
            new Permission[]{Permission.ADMINISTRATOR},
            "Reaction Roles",
            new OptionData[]{
                new OptionData(
                    OptionType.STRING,
                    "command",
                    "Defines which reaction role command to execute",
                    true
                )
            },
            (client, event, args, logger) -> {
                if(args[0] == "add") {
                    Response.Reply(new Message("Test reaction role add command"), event.getMessage(), false);
                } else if(args[0] == "delete" | args[0] == "remove") {
                    Response.Reply(new Message("Test reaction role remove command"), event.getMessage(), false);
                } else if(args[0] == "list" | args[0] == "listall") {
                    Response.Reply(new Message("Test reaction role list command"), event.getMessage(), false);
                }
            },
            (client, event, args, logger) -> {
                if(args[0].getAsString() == "add") {
                    Response.CommandReply(new Message("Test reaction role add command"), event);
                } else if(args[0].getAsString() == "remove") {
                    Response.CommandReply(new Message("Test reaction role remove command"), event);
                } else if(args[0].getAsString() == "list") {
                    Response.CommandReply(new Message("Test reaction role list command"), event);
                }
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }

}
