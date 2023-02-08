package net.hasibix.hasicraft.discordbot.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.models.client.builders.Command;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Message;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Response;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config;
import net.hasibix.hasicraft.discordbot.utils.ClassFinder;
import net.hasibix.hasicraft.discordbot.utils.EqualsArray;

public class CommandHandler extends ListenerAdapter {
    public Config config;
    private String prefix;
    public String errorEmoji;
    public String successEmoji;
    public String warningEmoji;
    private JDA client;
    private Logger logger;

    public List<Command> commands;

    public void Initialize(JDA client, String pathToConfig, Logger logger, Config config) {
        this.config = config;
        this.commands = new ArrayList<Command>();
        this.logger = logger;
        this.prefix = config.get("prefix", String.class);
        this.errorEmoji = config.get("emoji", "error", String.class);
        this.successEmoji = config.get("emoji", "success", String.class);
        this.warningEmoji = config.get("emoji", "warning", String.class);
        this.client = client;

        String packageName = "net.hasibix.hasicraft.discordbot.commands";

        try {
            Class<?>[] commandFiles = ClassFinder.getClassesFromPackage(packageName);
            for (Class<?> i : commandFiles) {
                Method method = i.getDeclaredMethod("register");
                method.invoke(null);
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
                            List<String> permList = new ArrayList<String>();
                            for (Permission perm : i.permissions) {
                                permList.add(perm.getName());
                            }
                            String[] permArray = permList.toArray(new String[permList.size()]);
                            String perms = String.join(",\n", permArray);
                            Message msg = new Message(errorEmoji + " | You don't have specified permissions to execute this command! [" + perms + "]");
                            Response.Reply(msg, event.getMessage(), false);
                            commandFound = true;
                        }
                        break;
                } else {
                    continue;
                }
            }

            if(!commandFound) {
                Message msg = new Message(errorEmoji + " | Command not found!");
                Response.Reply(msg, event.getMessage(), false);
                commandFound = true;
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
                            i.slashrun.accept(client, event, args);
                            commandFound = true;
                        } else {              
                            List<String> permList = new ArrayList<String>();
                            for (Permission perm : i.permissions) {
                                permList.add(perm.getName());
                            }
                            String[] permArray = permList.toArray(new String[permList.size()]);
                            String perms = String.join(",\n", permArray);
                            Message msg = new Message(errorEmoji + " | You don't have specified permissions to execute this command! [" + perms + "]");
                            Response.CommandReply(msg, event, false);
                            commandFound = true;
                        }
                        break;
                }
            }

            if(!commandFound) {
                Message msg = new Message(errorEmoji + " | Command not found!");
                Response.CommandReply(msg, event, false);
                commandFound = true;
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
