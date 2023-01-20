package net.hasibix.hasicraft.discordbot.models.client.builders;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.hasibix.hasicraft.discordbot.handlers.CommandHandler;
import net.hasibix.hasicraft.discordbot.handlers.EventHandler;
import net.hasibix.hasicraft.discordbot.handlers.InteractionHandler;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config.ConfigObject;

public class HasiBot {

    public enum ActivityType {
        Playing,
        Watching,
        Listening,
        Streaming
    }

    public enum Intent {
        GUILD_MEMBERS,
        GUILD_BANS,
        GUILD_EMOJIS,
        GUILD_WEBHOOKS,
        GUILD_INVITES,
        GUILD_VOICE_STATES,
        GUILD_PRESENCES,
        GUILD_MESSAGES,
        GUILD_MESSAGE_REACTIONS,
        GUILD_MESSAGE_TYPING,
        DIRECT_MESSAGES,
        DIRECT_MESSAGE_REACTIONS,
        DIRECT_MESSAGE_TYPING,
        ALL_INTENTS
    }

    JDA bot;
    public ConfigObject config;
    public Logger logger;
    public static CommandHandler commandHandler;
    public static EventHandler eventHandler;
    public static InteractionHandler interactionHandler;

    static Collection<GatewayIntent> mapIntentsToGatewayIntents(@Nullable Intent[] intents) {

        boolean allIntents;

        if (intents == null) {
            return null;
        }

        @Nullable Collection<GatewayIntent> intentsReal = new ArrayList<>();

        for (Intent i : intents) {
            @Nullable GatewayIntent intentReal = null;
            if(i != Intent.ALL_INTENTS) {
                allIntents = false;
            } else {
                allIntents = true;
            }

            if(allIntents == false) {
                switch (i) {
                    case GUILD_MEMBERS:
                        intentReal = GatewayIntent.GUILD_MEMBERS;
                        break;
                    case GUILD_BANS:
                        intentReal = GatewayIntent.GUILD_BANS;
                        break;
                    case GUILD_EMOJIS:
                        intentReal = GatewayIntent.GUILD_EMOJIS;
                        break;
                    case GUILD_WEBHOOKS:
                        intentReal = GatewayIntent.GUILD_WEBHOOKS;
                        break;
                    case GUILD_INVITES:
                        intentReal = GatewayIntent.GUILD_INVITES;
                        break;
                    case GUILD_VOICE_STATES:
                        intentReal = GatewayIntent.GUILD_VOICE_STATES;
                        break;
                    case GUILD_PRESENCES:
                        intentReal = GatewayIntent.GUILD_PRESENCES;
                        break;
                    case GUILD_MESSAGES:
                        intentReal = GatewayIntent.GUILD_MESSAGES;
                        break;
                    case GUILD_MESSAGE_REACTIONS:
                        intentReal = GatewayIntent.GUILD_MESSAGE_REACTIONS;
                        break;
                    case GUILD_MESSAGE_TYPING:
                        intentReal = GatewayIntent.GUILD_MESSAGE_TYPING;
                        break;
                    case DIRECT_MESSAGES:
                        intentReal = GatewayIntent.DIRECT_MESSAGES;
                        break;
                    case DIRECT_MESSAGE_REACTIONS:
                        intentReal = GatewayIntent.DIRECT_MESSAGE_REACTIONS;
                        break;
                    case DIRECT_MESSAGE_TYPING:
                        intentReal = GatewayIntent.DIRECT_MESSAGE_TYPING;
                        break;
                    default:
                        break;
                }
                if (intentReal != null) {
                    intentsReal.add(intentReal);
                }
                break;
            } else if(allIntents == true) {
                intentsReal = new ArrayList<GatewayIntent>(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
                break;
            }
        }

        return intentsReal;
    }

    Collection<GatewayIntent> gatewayIntents;

    public HasiBot(@Nullable Intent[] intents, Logger logger, ConfigObject config) {
        this.logger = logger;
        this.config = config;
        this.gatewayIntents = mapIntentsToGatewayIntents(intents);
    }

    public void Login(String token) {
        if(token == null | token == "" | token == " ") {
            logger.FatalError("A Bot token cannot be null.");
            System.exit(1);
        }

        commandHandler = new CommandHandler();
        interactionHandler = new InteractionHandler();
        eventHandler = new EventHandler();

        try {
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.setEnabledIntents(gatewayIntents);
            this.bot = builder.build();

            commandHandler.Initialize(this.bot, "config.yml", this.logger, this.config);

            bot.addEventListener(commandHandler);
            bot.addEventListener(interactionHandler);
            bot.addEventListener(eventHandler);

            SelfUser botUser = this.bot.getSelfUser();
            String botTag = botUser.getAsTag();

            this.logger.Log("[Discord] Logged in as " + botTag);
        } catch (LoginException e) {
            this.logger.FatalError("LoginException occured!\n" + e);
        }
    }

    public void Logoff() {
        bot.shutdown();
        logger.Log("HasiBot: Logoff process has succeed.");
    }

    public void SetActivity(@Nonnull String text, @Nonnull ActivityType activityType, @Nullable String url) {
        switch (activityType) {
            case Playing:
                bot.getPresence().setActivity(Activity.playing(text));
                break;
            case Watching:
                bot.getPresence().setActivity(Activity.watching(text));
                break;
            case Listening:
                bot.getPresence().setActivity(Activity.listening(text));
                break;
            case Streaming:
                bot.getPresence().setActivity(Activity.streaming(text, url));
                break;
            default:
                break;
        }
    }

}
