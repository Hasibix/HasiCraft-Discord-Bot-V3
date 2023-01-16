package net.hasibix.hasicraft.discordbot.models.client;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Command {

    public static Command[] commands;
    public interface CommondFunc<T, U, V> {
        void run(T t, U u, V v);
    }

    @Nonnull public final String name;
    @Nonnull public final String description;
    @Nonnull public final Permission[] permissions;
    @Nonnull public final String category;
    @Nonnull public final OptionData[] args;
    @Nonnull public final CommondFunc<JDA, MessageReceivedEvent, Object[]> run;
    @Nonnull public final CommondFunc<JDA, SlashCommandInteractionEvent, OptionMapping[]> slashrun;

    public Command(@Nonnull String name, @Nonnull String description, @Nonnull OptionData[] args, @Nonnull Permission[] permissions, @Nonnull String category, @Nonnull CommondFunc<JDA, MessageReceivedEvent, Object[]> run, @Nonnull CommondFunc<JDA, SlashCommandInteractionEvent, OptionMapping[]> slashrun) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.category = category;
        this.args = args;
        this.run = run;
        this.slashrun = slashrun;

        ArrayList<Command> commandsList = new ArrayList<Command>(Arrays.asList(commands));
        commandsList.add(this);
        commands = commandsList.toArray(new Command[commandsList.size()]);
    }

}