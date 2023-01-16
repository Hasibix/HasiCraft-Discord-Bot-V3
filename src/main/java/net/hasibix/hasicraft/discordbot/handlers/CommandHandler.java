package net.hasibix.hasicraft.discordbot.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.Config;
import net.hasibix.hasicraft.discordbot.models.client.Command;
import net.hasibix.hasicraft.discordbot.models.client.Logger;

public class CommandHandler extends ListenerAdapter {
    Config config = new Config();
    String prefix;
    String errorEmoji;
    String successEmoji;
    String warningEmoji;
    JDA client;
    Logger logger;
    public Config Initialize(JDA client, String pathToConfig, Logger logger) {
        config.loadConfig(pathToConfig);

        this.logger = logger;

        this.prefix = (String) config.getKey("prefix").value;
        this.errorEmoji = (String) config.getKey("emoji").value; 
        this.successEmoji = (String) config.getKey("emoji").value;
        this.warningEmoji = (String) config.getKey("emoji").value;

        this.client = client;

        return config;
    } 

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentDisplay();
        Object[] unmanagedArgs = message.split(" ");

        if (unmanagedArgs[0].toString().equals(prefix)) {
            Object[] args = new Object[unmanagedArgs.length - 2];
            System.arraycopy(unmanagedArgs, 2, args, 0, args.length);

            Boolean commandExecuted = false;
            
            for (Command i : Command.commands) {
                if(unmanagedArgs[1].equals(i.name)) {
                    i.run.run(client, event, args);
                    commandExecuted = true;
                    break;
                }
            }

            if(!commandExecuted) {
                event.getChannel().sendMessage(errorEmoji + " | Command not found!").queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        Boolean commandExecuted = false;
        for (Command i : Command.commands) {
            if (commandName.equals(i.name)) {
                List<OptionMapping> options = event.getOptions();
                OptionMapping[] args = options.toArray(new OptionMapping[options.size()]);
                i.slashrun.run(client, event, args);
                commandExecuted = true;
                break;
            }
        }

        if(!commandExecuted) {
            event.reply(errorEmoji + " | Command not found!").queue();
        }
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for (Command i : Command.commands) {
            if (i.args.length > 0) {
                ArrayList<OptionData> optionList = new ArrayList<OptionData>(Arrays.asList(i.args));
                commandData.add(Commands.slash(i.name, i.description).addOptions(optionList));
            } else {
                commandData.add(Commands.slash(i.name, i.description));
            }
        }
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

}
