package net.hasibix.hasicraft.discordbot.models.client.builders;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.hasibix.hasicraft.discordbot.utils.Logger;

public class Command {

    @FunctionalInterface
    public interface CommondFunc<T, U, V, B> {
        void accept(T t, U u, V v, B b);
    }

    @Nonnull public final String name;
    @Nonnull public final String[] aliases;
    @Nonnull public final String description;
    @Nonnull public final Permission[] permissions;
    @Nonnull public final String category;
    @Nonnull public final OptionData[] argsSlash;
    @Nonnull public final CommondFunc<JDA, MessageReceivedEvent, Object[], Logger> run;
    @Nonnull public final CommondFunc<JDA, SlashCommandInteractionEvent, OptionMapping[], Logger> slashrun;

    public Command(@Nonnull String name, @Nonnull String[] aliases, @Nonnull String description, @Nonnull Permission[] permissions, @Nonnull String category, @Nonnull OptionData[] argsSlash, @Nonnull CommondFunc<JDA, MessageReceivedEvent, Object[], Logger> run, @Nonnull CommondFunc<JDA, SlashCommandInteractionEvent, OptionMapping[], Logger> slashrun) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.permissions = permissions;
        this.category = category;
        this.argsSlash = argsSlash;
        this.run = run;
        this.slashrun = slashrun;
    }

}