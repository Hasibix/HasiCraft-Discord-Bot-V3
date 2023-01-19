package net.hasibix.hasicraft.discordbot.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.Command;
import net.hasibix.hasicraft.discordbot.models.client.Config.ConfigObject;
import net.hasibix.hasicraft.discordbot.models.client.Logger;
import net.hasibix.hasicraft.discordbot.utils.ClassFinder;
import net.hasibix.hasicraft.discordbot.utils.EqualsArray;

public class CommandHandler extends ListenerAdapter {
    public ConfigObject config;
    String prefix;
    String errorEmoji;
    String successEmoji;
    String warningEmoji;
    JDA client;
    Logger logger;

    List<Command> commands;

    Method slashRun;

    public void Initialize(JDA client, String pathToConfig, Logger logger, ConfigObject config) {
        this.config = config;
        this.commands = new ArrayList<Command>();
        this.logger = logger;
        this.prefix = (String) config.get("prefix");
        this.errorEmoji = ":x:";
        this.successEmoji = ":white_check_mark:";
        this.warningEmoji = ":warning:";
        this.client = client;

        String packageName = "net.hasibix.hasicraft.discordbot.commands";

        try {
            Class<?>[] commandFiles = ClassFinder.getClassesFromPackage(packageName);
            for (Class<?> i : commandFiles) {
                Method method = i.getDeclaredMethod("register");
                method.invoke(null);
                slashRun = i.getDeclaredMethod("slashrun");
            }
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | IOException | NoSuchMethodException e) {
            this.logger.Error(e.toString());
        }
    }

    public void addCommand(Command command) {
        for (Command cmd : commands) {
            if (cmd.name.equals(command.name)) {
                this.logger.Error("Command with name " + cmd.name + " already exists");
                return;
            }
        }
        this.logger.Log("Added command: " + command.name);
        commands.add(command);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentDisplay();
        Object[] unmanagedArgs = message.split(" ");
        Member member = event.getMember();

        if (unmanagedArgs[0].toString().equals(prefix) && member != null) {
            Object[] args = new Object[unmanagedArgs.length - 2];
            System.arraycopy(unmanagedArgs, 2, args, 0, args.length);

            Boolean commandFound = false;
            
            for (Command i : commands) {
                if(unmanagedArgs[1].toString().equals(i.name) | (i.aliases.length > 0 && EqualsArray.Equals(unmanagedArgs[1], i.aliases))) {
                        Boolean hasPerms = false;
                        if(i.permissions.length > 0) {
                            if (member.hasPermission(i.permissions)) {
                                hasPerms = true;
                            } else {
                                hasPerms = false;
                            }
                        } else if (i.permissions.length == 0) {
                            hasPerms = true;
                        }
                        
                        if(hasPerms) {
                            i.run.accept(client, event, args);
                            commandFound = true;
                        } else {                            
                            event.getMessage().reply(errorEmoji + " | You don't have specified permissions to execute this command! | " + i.permissions).queue();
                            commandFound = true;
                        }
                        break;
                } else {
                    continue;
                }
            }

            if(!commandFound) {
                event.getMessage().reply(errorEmoji + " | Command not found!").queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        if(member != null) {
            String commandName = event.getName();
            Boolean commandFound = false;
            List<OptionMapping> options = event.getOptions();
            OptionMapping[] args = options.toArray(new OptionMapping[options.size()]);
            for (Command i : commands) {
                if (commandName.equals(i.name) | (i.aliases.length > 0 && EqualsArray.Equals(commandName,  i.aliases))) {
                    Boolean hasPerms = false;
                        if(i.permissions.length > 0) {
                            if (member.hasPermission(i.permissions)) {
                                hasPerms = true;
                            } else {
                                hasPerms = false;
                            }
                        } else if (i.permissions.length == 0) {
                            hasPerms = true;
                        }
                        
                        if(hasPerms) {
                            //i.slashrun.accept(client, event, args);
                            try {
                                slashRun.invoke(client, event, args);
                            } catch (Exception e) {
                                logger.Error("eror");
                            }
                            commandFound = true;
                        } else {                            
                            event.reply(errorEmoji + " | You don't have specified permissions to execute this command! | " + i.permissions).queue();
                            commandFound = true;
                        }
                        break;
                }
            }

            if(!commandFound) {
                event.reply(errorEmoji + " | Command not found!").queue();
            }
        }
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for (Command i : commands) {
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
