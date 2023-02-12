package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.hasibix.hasicraft.discordbot.models.client.utils.JwtUtils;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;

public class Pagination {
    public class Page {
        public @Nullable String text;
        public @Nullable Embed embed;

        public Page(@Nullable String text, @Nullable Embed embed) {
            if(text == null) {
                this.text = "";
            } else {
                this.text = text;
            }

            this.embed = embed;
        }
    }

    private @Nullable List<Page> pages;
    
    private static final Logger LOGGER = Logger.of(Pagination.class);
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Random random = new Random();
    
    private TimeUnit timeoutUnit = TimeUnit.SECONDS;
    private int timeout = 45;

    private Predicate<ButtonInteractionEvent> predicate;
    private boolean disableOnTimeout;
    private EventWaiter waiter;
    private long channelId;
    private JDA client;

    private int maxPage;
    private List<String> tokens;
    private long messageId = -1;
    private InteractionHook intMsg;
    private boolean isInteractionCommand = false;
    private int page = 0;
    private Emoji[] emojis = {
        Emoji.fromMarkdown("⏪"),
        Emoji.fromMarkdown("◀"),
        Emoji.fromMarkdown("▶"),
        Emoji.fromMarkdown("⏩")
    };

    public Pagination(JDA client) {
        this.pages = new ArrayList<Page>();
        this.maxPage = pages.size();
        this.tokens = new ArrayList<>();
        this.client = client;
        
        String[] tokens = new String[] {
            JwtUtils.createToken(key, Long.toString(channelId) + (short) random.nextInt()),
            JwtUtils.createToken(key, Long.toString(channelId) + (short) random.nextInt()),
            JwtUtils.createToken(key, Long.toString(channelId) + (short) random.nextInt()),
            JwtUtils.createToken(key, Long.toString(channelId) + (short) random.nextInt())
        };

        for (String i : tokens) {
            this.tokens.add(i);
        }
    }

    public Pagination setCallback(Predicate<ButtonInteractionEvent> predicate) {
        this.predicate = predicate;
        return this;
    }

    public Pagination setEmojis(Emoji... emojis) {
        this.emojis = emojis;
        return this;
    }

    public Pagination setDeleteOnTimeout(boolean behavior) {
        this.disableOnTimeout = behavior;
        return this;
    }

    public void addPage(@Nullable Page page) {
        if(page != null) {
            this.pages.add(page);
        }
    }

    public Pagination setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Pagination setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
        return this;
    }

    public Pagination setWaiter(EventWaiter waiter) {
        this.waiter = waiter;
        return this;
    }

    public void Reply(@Nonnull net.dv8tion.jda.api.entities.Message message, boolean mention) {
        Message response = new Message(pages.get(0).text != null ? pages.get(0).text : "");
        response.addEmbed(pages.get(0).embed);
        response.addActionRow(getActionRow());
        Response.Reply(response, message, mention, (m) -> {
            this.messageId = m.getIdLong();
            this.intMsg = null;
            this.isInteractionCommand = false;
        });
        doWait();
    }

    public void CommandReply(@Nonnull GenericCommandInteractionEvent event) {
        Message response = new Message(pages.get(0).text != null ? pages.get(0).text : "");
        response.addEmbed(pages.get(0).embed);
        response.addActionRow(getActionRow());
        Response.CommandReply(response, event, false, (m) -> {
            this.messageId = -1;
            this.intMsg = m;
            this.isInteractionCommand = true;
        });
    }

    public void Send(@Nonnull MessageChannel channel) {
        Message response = new Message(pages.get(0).text != null ? pages.get(0).text : "");
        response.addEmbed(pages.get(0).embed);
        response.addActionRow(getActionRow());
        Response.Send(response, channel, (m) -> {
            this.messageId = m.getIdLong();
            this.intMsg = null;
            this.isInteractionCommand = false;
        });
        doWait();
    }

    public ActionRow getActionRow() {
        return ActionRow.of(
                page == 0 ? Button.primary(tokens.get(0), emojis[0]).asDisabled() : Button.primary(tokens.get(0), emojis[0]).asEnabled(),
                page == 0 ? Button.primary(tokens.get(1), emojis[1]).asDisabled() : Button.primary(tokens.get(1), emojis[1]).asEnabled(),
                page == maxPage | maxPage == 1 ? Button.primary(tokens.get(2), emojis[2]).asDisabled() : Button.primary(tokens.get(2), emojis[2]).asEnabled(),
                page == maxPage | maxPage == 1 ? Button.primary(tokens.get(3), emojis[3]).asDisabled() : Button.primary(tokens.get(3), emojis[3]).asEnabled()
        );
    }

    private void doWait() {
        waiter.waitForEvent(ButtonInteractionEvent.class, predicate, this::switchPage, timeout, timeoutUnit, () -> {
            if (this.disableOnTimeout) {
                if(!this.isInteractionCommand) {
                    if (this.messageId == -1) {
                        LOGGER.error("The message is not set. Please send the pagination first!");
                        LOGGER.trace(new IllegalArgumentException());
                        return;
                    }

                    MessageChannel channel = client.getTextChannelById(channelId) != null ? client.getTextChannelById(channelId) : client.getPrivateChannelById(channelId);
                    if(channel == null) {
                        LOGGER.error("Channel does not exist for ID " + this.channelId);
                        LOGGER.trace(new IllegalStateException());
                        return;
                    }
                    channel.retrieveMessageById(this.messageId).queue(m -> {
                        m.editMessageComponents(ActionRow.of(
                            Button.primary(tokens.get(0), emojis[0]).asDisabled(),
                            Button.primary(tokens.get(1), emojis[1]).asDisabled(),
                            Button.primary(tokens.get(2), emojis[2]).asDisabled(),
                            Button.primary(tokens.get(3), emojis[3]).asDisabled()
                        ));
                    });

                } else if(this.isInteractionCommand) {
                    if (this.intMsg == null) {
                        LOGGER.error("The message is not set. Please send the pagination first!");
                        LOGGER.trace(new IllegalArgumentException());
                        return;
                    }

                    intMsg.editOriginalComponents(ActionRow.of(
                        Button.primary(tokens.get(0), emojis[0]).asDisabled(),
                        Button.primary(tokens.get(1), emojis[1]).asDisabled(),
                        Button.primary(tokens.get(2), emojis[2]).asDisabled(),
                        Button.primary(tokens.get(3), emojis[3]).asDisabled()
                    )).queue();
                }
            }
        });
    }

    private void switchPage(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        String jwt = event.getComponentId();
        String emoji = event.getButton().getId();
        
        if(!this.isInteractionCommand) {
            net.dv8tion.jda.api.entities.Message message = event.getMessage();

            if (!tokens.contains(jwt)) {
                MessageChannel channel = client.getTextChannelById(channelId) != null ? client.getTextChannelById(channelId) : client.getPrivateChannelById(channelId);
                if (channel != null) {
                    channel.deleteMessageById(messageId).queue();
                }
                return;
            }

            if (!predicate.test(event)) {
                return;
            }

            if (emoji.equals(tokens.get(0))) {
                page = 0;
                if (page < 0) {
                    page = maxPage - 1;
                }
            } else if (emoji.equals(tokens.get(1))) {
                page--;
                if (page < 0) {
                    page = maxPage - 1;
                }
            } else if (emoji.equals(tokens.get(2))) {
                page++;
                if (page >= maxPage) {
                    page = 0;
                }
            } else if (emoji.equals(tokens.get(3))) {
                page = maxPage;
            }

            Response.EditMsg(new Message(pages.get(page).text != null ? pages.get(page).text : "").addEmbed(pages.get(page).embed), message);
            doWait();
        } else if (this.isInteractionCommand) {
            if (!tokens.contains(jwt)) {
                intMsg.deleteOriginal();
                return;
            }

            if (!predicate.test(event)) {
                return;
            }

            if (emoji.equals(tokens.get(0))) {
                page = 0;
                if (page < 0) {
                    page = maxPage - 1;
                }
            } else if (emoji.equals(tokens.get(1))) {
                page--;
                if (page < 0) {
                    page = maxPage - 1;
                }
            } else if (emoji.equals(tokens.get(2))) {
                page++;
                if (page >= maxPage) {
                    page = 0;
                }
            } else if (emoji.equals(tokens.get(3))) {
                page = maxPage;
            }

            Response.EditCommandReply(new Message(pages.get(page).text != null ? pages.get(page).text : "").addEmbed(pages.get(page).embed), intMsg);
            doWait();
        }
    }
}