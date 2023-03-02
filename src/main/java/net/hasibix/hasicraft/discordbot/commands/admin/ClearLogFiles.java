package net.hasibix.hasicraft.discordbot.commands.admin;

import java.io.File;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Response;
import net.hasibix.hasicraft.discordbot.utils.Config;
import net.hasibix.hasicraft.discordbot.utils.Logger;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message;

public class ClearLogFiles {
    public static void register() {
        Command command = new Command(
            "clear-log-files",
            new String[]{"clear-logs"},
            "Clears all log files.",
            new Permission[]{Permission.ADMINISTRATOR},
            "Admin",
            new OptionData[]{},
            (client, event, args, logger) -> {
                if(event.getAuthor().getId().equals(Config.get("ownerId"))) {
                    purgeDirectory(new File("logs/"), logger);
                    Response.Reply(new Message(HasiBot.commandHandler.successEmoji + " | Successfully cleared all log files!"), event.getMessage(), false);
                } else {
                    Response.Reply(new Message(HasiBot.commandHandler.errorEmoji + " | You need to be the owner of this bot to run this command!"), event.getMessage(), false);
                }
            },
            (client, event, args, logger) -> {
                if(event.getMember().getId().equals(event.getGuild().getOwnerId())) {
                    purgeDirectory(new File("logs/"), logger);
                    Response.CommandReply(new Message(HasiBot.commandHandler.successEmoji + " | Successfully cleared all log files!"), event, false);
                } else {
                    Response.CommandReply(new Message(HasiBot.commandHandler.errorEmoji + " | You need to be the owner of this server to run this command!"), event, false);
                }
            }
        );
        
        HasiBot.commandHandler.addCommand(command);
    }

    private static void purgeDirectory(File dir, Logger logger) {
        for (File file: dir.listFiles()) {
            if (file.isDirectory())
                purgeDirectory(file, logger);
            file.delete();
        }
        logger.warn("All logs has been cleared!");
    }
}
