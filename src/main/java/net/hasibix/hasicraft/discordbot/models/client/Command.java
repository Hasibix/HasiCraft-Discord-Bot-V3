package net.hasibix.hasicraft.discordbot.models.client;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Command {

    public interface CommondFunc<T, U, V> {
        void accept(T t, U u, V v);
    }

    @Nonnull public final String name;
    @Nonnull public final String[] aliases;
    @Nonnull public final String description;
    @Nonnull public final Permission[] permissions;
    @Nonnull public final String category;
    @Nonnull public final OptionData[] args;
    @Nonnull public final CommondFunc<JDA, MessageReceivedEvent, Object[]> run;
    @Nonnull public final CommondFunc<JDA, SlashCommandInteractionEvent, OptionMapping[]> slashrun;

    public Command(@Nonnull String name, @Nonnull String[] aliases, @Nonnull String description, @Nonnull Permission[] permissions, @Nonnull String category, @Nonnull OptionData[] args, @Nonnull CommondFunc<JDA, MessageReceivedEvent, Object[]> run, @Nonnull CommondFunc<JDA, SlashCommandInteractionEvent, OptionMapping[]> slashrun) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.permissions = permissions;
        this.category = category;
        this.args = args;
        this.run = run;
        this.slashrun = slashrun;
    }

}