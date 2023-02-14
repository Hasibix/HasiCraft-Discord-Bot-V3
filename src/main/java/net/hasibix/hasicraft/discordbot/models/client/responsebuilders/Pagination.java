package net.hasibix.hasicraft.discordbot.models.client.responsebuilders;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.utils.JwtUtils;
import net.hasibix.hasicraft.discordbot.utils.Logger;

public class Pagination {
    public static final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Random random = new Random();
    private static final Logger logger = Logger.of(Pagination.class);
    public class Page {
        @Nullable public String content;
        @Nullable public Embed embed;
        
        public Page(@Nullable String content, @Nullable Embed embed) {
            this.embed = embed;
            this.content = content != null ? content : "";
        }
    }
    private class Msg {
        @Nullable long messageId;
        @Nullable InteractionHook interactionHook;
        @Nullable public boolean isInteractionMessage;

        public Msg() {}

        public void setMessage(Object msgOrId) {
            if(msgOrId instanceof Long) {
                this.messageId = (long) msgOrId;
                this.interactionHook = null;
                this.isInteractionMessage = false;
            } else if(msgOrId instanceof InteractionHook) {
                this.messageId = -1;
                this.interactionHook = (InteractionHook) msgOrId;
                this.isInteractionMessage = true;
            } else {
                this.messageId = -1;
                this.interactionHook = null;
                this.isInteractionMessage = false;
            }
        }

        public Object getMessage() {
            return !this.isInteractionMessage ? this.messageId : this.interactionHook;
        }
    }
    public List<Page> pages;
    private JDA client;
    private EventWaiter waiter;
    private boolean useSelectMenu = true;
    private boolean useButtons = true;
    private Map<String, String> tokens = new HashMap<String, String>();
    private int currentPage = 0;
    private int maxPage = 0;
    private Map<String, Emoji> emojis = new HashMap<String, Emoji>();
    private boolean disableOnTimeout = true;
    private long channelId = -1;
    private long timeout = 45;
    private TimeUnit timeoutUnit = TimeUnit.SECONDS;
    private Msg message;
    private Predicate<GenericComponentInteractionCreateEvent> condition;

    public Pagination(JDA client, boolean useSelectMenu, boolean useButtons) {
        this.client = client;
        this.message = new Msg();
        this.pages = new ArrayList<Page>();

        if(!useButtons && !useSelectMenu) {
            logger.error("Cannot use nothing in a pagination! Use atleast one. Buttons or select menu or both to true. Both can't but false!");
            return;
        }
        
        this.emojis.putIfAbsent("FirstPageButton", Emoji.fromMarkdown(":rewind:"));
        this.emojis.putIfAbsent("PreviousPageButton", Emoji.fromMarkdown(":arrow_backward:"));
        this.emojis.putIfAbsent("CancelButton", Emoji.fromMarkdown(":x:"));
        this.emojis.putIfAbsent("NextPageButton", Emoji.fromMarkdown(":arrow_forward:"));
        this.emojis.putIfAbsent("LastPageButton", Emoji.fromMarkdown(":fast_forward:"));

        this.useSelectMenu = useSelectMenu;
        this.useButtons = useButtons;

        if(useSelectMenu) {
            this.tokens.putIfAbsent("SelectPageMenu", JwtUtils.createToken(jwtKey, Long.toString(client.getSelfUser().getIdLong()) + (short) random.nextInt()));
        }
        
        if(useButtons) {
            this.tokens.putIfAbsent("FirstPageButton", JwtUtils.createToken(jwtKey, Long.toString(client.getSelfUser().getIdLong()) + (short) random.nextInt()));
            this.tokens.putIfAbsent("PreviousPageButton",JwtUtils.createToken(jwtKey, Long.toString(client.getSelfUser().getIdLong()) + (short) random.nextInt()));
            this.tokens.putIfAbsent("CancelButton",JwtUtils.createToken(jwtKey, Long.toString(client.getSelfUser().getIdLong()) + (short) random.nextInt()));
            this.tokens.putIfAbsent("NextPageButton", JwtUtils.createToken(jwtKey, Long.toString(client.getSelfUser().getIdLong()) + (short) random.nextInt()));
            this.tokens.putIfAbsent("LastPageButton", JwtUtils.createToken(jwtKey, Long.toString(client.getSelfUser().getIdLong()) + (short) random.nextInt()));
        }
        HasiBot.paginationHandler.add(this);
    }

    public Pagination addPage(Page page) {
        if(page != null) {
            this.pages.add(page);
            this.maxPage++;
        }
        return this;
    }

    public Pagination useSelectMenu(boolean bool) {
        this.useSelectMenu = bool;
        return this;
    }

    public Pagination useButtons(boolean bool) {
        this.useButtons = bool;
        return this;
    }

    public Pagination setChannel(long channelId) {
        this.channelId = channelId;
        return this;
    }

    public Pagination setWaiter(EventWaiter waiter) {
        this.waiter = waiter;
        return this;
    }

    public Pagination setCondition(Predicate<GenericComponentInteractionCreateEvent> condition) {
        this.condition = condition;
        return this;
    }

    public Pagination setDeleteOnTimeout(boolean behavior) {
        this.disableOnTimeout = behavior;
        return this;
    }

    public Pagination setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Pagination setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
        return this;
    }

    public Pagination setEmoji(String emojiType, Emoji emoji) {
        this.emojis.remove(emojiType);
        this.emojis.put(emojiType, emoji);
        return this;
    }

    public void Reply(@Nonnull net.dv8tion.jda.api.entities.Message message, boolean mention) {
        Message response = new Message(pages.get(0).content != null ? pages.get(0).content : "");
        response.addEmbed(pages.get(0).embed);
        if(this.useSelectMenu) {
            response.addActionRow(getActionRowSelectMenu(false));
        }
        if(this.useButtons) {
            response.addActionRow(getActionRowButtons());
        }
        Response.Reply(response, message, mention, (m) -> {
            this.message.setMessage((Long) m.getIdLong());
        });
        startWaiter();
    }

    public void CommandReply(@Nonnull GenericCommandInteractionEvent event) {
        Message response = new Message(pages.get(0).content != null ? pages.get(0).content : "");
        response.addEmbed(pages.get(0).embed);
        if(this.useSelectMenu) {
            response.addActionRow(getActionRowSelectMenu(false));
        }
        if(this.useButtons) {
            response.addActionRow(getActionRowButtons());
        }
        Response.CommandReply(response, event, false, (m) -> {
            this.message.setMessage((InteractionHook) m);
        });
        startWaiter();
    }

    public void Send(@Nonnull MessageChannel channel) {
        Message response = new Message(pages.get(0).content != null ? pages.get(0).content : "");
        response.addEmbed(pages.get(0).embed);
        if(this.useSelectMenu) {
            response.addActionRow(getActionRowSelectMenu(false));
        }
        if(this.useButtons) {
            response.addActionRow(getActionRowButtons());
        }
        Response.Send(response, channel, (m) -> {
            this.message.setMessage((Long) m.getIdLong());
        });
        startWaiter();
    }

    public ActionRow getActionRowButtons() {
        return ActionRow.of(
            this.currentPage == 0 ? Button.primary(tokens.get("FirstPageButton"), emojis.get("FirstPageButton")).asDisabled() : Button.primary(tokens.get("FirstPageButton"), emojis.get("FirstPageButton")).asEnabled(),
            this.currentPage == 0  ? Button.primary(tokens.get("PreviousPageButton"), emojis.get("PreviousPageButton")).asDisabled() : Button.primary(tokens.get("PreviousPageButton"), emojis.get("PreviousPageButton")).asEnabled(),
            Button.danger(tokens.get("CancelButton"), emojis.get("CancelButton")),
            this.currentPage == (this.maxPage - 1) ? Button.primary(tokens.get("NextPageButton"), emojis.get("NextPageButton")).asDisabled() : Button.primary(tokens.get("NextPageButton"), emojis.get("NextPageButton")).asEnabled(),
            this.currentPage == (this.maxPage - 1)  ? Button.primary(tokens.get("LastPageButton"), emojis.get("LastPageButton")).asDisabled() : Button.primary(tokens.get("LastPageButton"), emojis.get("LastPageButton")).asEnabled()
        );
    }

    public ActionRow getActionRowSelectMenu(boolean disabled) {
        SelectMenu.Builder smB  = SelectMenu.create(tokens.get("SelectPageMenu")).setPlaceholder("Select a page")
        .setMinValues(1)
        .setMaxValues(1);

        for (int i = 0; i < pages.size(); i++) {
            smB = smB.addOption(String.format("Page %s", (i + 1)), String.format("page_%s", i));
        }
        
        return ActionRow.of(
            smB.setDisabled(disabled).build()
        );
    }

    public void startWaiter() {
        waiter.waitForEvent(GenericComponentInteractionCreateEvent.class, condition, this::switchPage, this.timeout, this.timeoutUnit, () -> {
            if (this.disableOnTimeout) {
                cancelPagination();
            }
        });
    }

    private void cancelPagination() {
        if(!this.message.isInteractionMessage) {
            if ((long) this.message.getMessage() == -1) {
                logger.error("The message is not set. Please send the pagination first!");
                logger.trace(new IllegalArgumentException());
                return;
            }

            MessageChannel channel = client.getTextChannelById(this.channelId) != null ? client.getTextChannelById(this.channelId) : client.getPrivateChannelById(this.channelId);
            if(channel == null) {
                logger.error("Channel does not exist for ID " + this.channelId);
                logger.trace(new IllegalStateException());
                return;
            }
            
            if(this.useButtons && this.useSelectMenu) {
                channel.retrieveMessageById((long) this.message.getMessage()).queue(m -> {
                    m.editMessageComponents(
                    getActionRowSelectMenu(true),    
                    ActionRow.of(
                            Button.primary(tokens.get("FirstPageButton"), emojis.get("FirstPageButton")).asDisabled(),
                            Button.primary(tokens.get("PreviousPageButton"), emojis.get("PreviousPageButton")).asDisabled(),
                            Button.danger(tokens.get("CancelButton"), emojis.get("CancelButton")).asDisabled(),
                            Button.primary(tokens.get("NextPageButton"), emojis.get("NextPageButton")).asDisabled(),
                            Button.primary(tokens.get("LastPageButton"), emojis.get("LastPageButton")).asDisabled()
                        )
                    ).queue();
                });
            } else if(this.useButtons && !this.useSelectMenu) {
                channel.retrieveMessageById((long) this.message.getMessage()).queue(m -> {
                    m.editMessageComponents(
                    ActionRow.of(
                            Button.primary(tokens.get("FirstPageButton"), emojis.get("FirstPageButton")).asDisabled(),
                            Button.primary(tokens.get("PreviousPageButton"), emojis.get("PreviousPageButton")).asDisabled(),
                            Button.danger(tokens.get("CancelButton"), emojis.get("CancelButton")).asDisabled(),
                            Button.primary(tokens.get("NextPageButton"), emojis.get("NextPageButton")).asDisabled(),
                            Button.primary(tokens.get("LastPageButton"), emojis.get("LastPageButton")).asDisabled()
                        )
                    ).queue();
                });
            } else if(!this.useButtons && this.useSelectMenu) {
                channel.retrieveMessageById((long) this.message.getMessage()).queue(m -> {
                    m.editMessageComponents(
                        getActionRowSelectMenu(true)
                    ).queue();
                });
            }

        } else if(this.message.isInteractionMessage) {
            if ((long) this.message.getMessage() == -1) {
                logger.error("The message is not set. Please send the pagination first!");
                logger.trace(new IllegalArgumentException());
                return;
            }

            MessageChannel channel = client.getTextChannelById(this.channelId) != null ? client.getTextChannelById(this.channelId) : client.getPrivateChannelById(this.channelId);
            if(channel == null) {
                logger.error("Channel does not exist for ID " + this.channelId);
                logger.trace(new IllegalStateException());
                return;
            }
            
            if(this.useButtons && this.useSelectMenu) {
                InteractionHook.class.cast(this.message.getMessage()).editOriginalComponents(
                    getActionRowSelectMenu(true),    
                    ActionRow.of(
                        Button.primary(tokens.get("FirstPageButton"), emojis.get("FirstPageButton")).asDisabled(),
                        Button.primary(tokens.get("PreviousPageButton"), emojis.get("PreviousPageButton")).asDisabled(),
                        Button.danger(tokens.get("CancelButton"), emojis.get("CancelButton")).asDisabled(),
                        Button.primary(tokens.get("NextPageButton"), emojis.get("NextPageButton")).asDisabled(),
                        Button.primary(tokens.get("LastPageButton"), emojis.get("LastPageButton")).asDisabled()
                    )
                ).queue();
            } else if(this.useButtons && !this.useSelectMenu) {
                InteractionHook.class.cast(this.message.getMessage()).editOriginalComponents(
                    ActionRow.of(
                            Button.primary(tokens.get("FirstPageButton"), emojis.get("FirstPageButton")).asDisabled(),
                            Button.primary(tokens.get("PreviousPageButton"), emojis.get("PreviousPageButton")).asDisabled(),
                            Button.danger(tokens.get("CancelButton"), emojis.get("CancelButton")).asDisabled(),
                            Button.primary(tokens.get("NextPageButton"), emojis.get("NextPageButton")).asDisabled(),
                            Button.primary(tokens.get("LastPageButton"), emojis.get("LastPageButton")).asDisabled()
                        )
                ).queue();
            } else if(!this.useButtons && this.useSelectMenu) {
                InteractionHook.class.cast(this.message.getMessage()).editOriginalComponents(
                    getActionRowSelectMenu(true)
                ).queue();
            }
        }
    }

    private void switchPage(GenericComponentInteractionCreateEvent event) {
        event.deferEdit().queue();
        String jwt = event.getComponentId();
        if(!this.message.isInteractionMessage) {
            net.dv8tion.jda.api.entities.Message message = event.getMessage();

            if (!tokens.containsValue(jwt)) {
                MessageChannel channel = client.getTextChannelById(channelId) != null ? client.getTextChannelById(channelId) : client.getPrivateChannelById(channelId);
                if (channel != null) {
                    channel.deleteMessageById((long) this.message.getMessage()).queue();
                }
                return;
            }

            if (!condition.test(event)) {
                return;
            }

            if (jwt.equals(tokens.get("FirstPageButton"))) {
                currentPage = 0;
                if (currentPage < 0) {
                    currentPage = 0;
                }
            } else if (jwt.equals(tokens.get("PreviousPageButton"))) {
                currentPage--;
                if (currentPage < 0) {
                    currentPage = 0;
                }
            } else if (jwt.equals(tokens.get("NextPageButton"))) {
                currentPage++;
                if (currentPage > (maxPage - 1)) {
                    currentPage = (maxPage - 1);
                }
            } else if (jwt.equals(tokens.get("LastPageButton"))) {
                currentPage = (maxPage - 1);
            } else if (jwt.equals(tokens.get("CancelButton"))) {
                cancelPagination();
                return;
            } else if (jwt.equals(tokens.get("SelectPageMenu"))) {
                List<SelectOption> selections = SelectMenuInteractionEvent.class.cast(event).getSelectedOptions();
                currentPage = Integer.valueOf(selections.get(0).getValue().split("_")[1]);
            }

            Message msg = new Message(pages.get(currentPage).content != null ? pages.get(currentPage).content : "").addEmbed(pages.get(currentPage).embed);

            if(this.useSelectMenu) {
                msg.addActionRow(getActionRowSelectMenu(false));
            }

            if(this.useButtons) {
                msg.addActionRow(getActionRowButtons());
            }

            Response.EditMsg(msg, message);
            startWaiter();
        } else if (this.message.isInteractionMessage) {
            if (!tokens.containsValue(jwt)) {
                InteractionHook.class.cast(message.getMessage()).deleteOriginal();
                return;
            }

            if (!condition.test(event)) {
                return;
            }
            
            if (jwt.equals(tokens.get("FirstPageButton"))) {
                currentPage = 0;
            } else if (jwt.equals(tokens.get("PreviousPageButton"))) {
                currentPage--;
                if (currentPage < 0) {
                    currentPage = 0;
                }
            } else if (jwt.equals(tokens.get("NextPageButton"))) {
                currentPage++;
                if (currentPage > (maxPage - 1)) {
                    currentPage = (maxPage - 1);
                }
            } else if (jwt.equals(tokens.get("LastPageButton"))) {
                currentPage = (maxPage - 1);
            } else if (jwt.equals(tokens.get("CancelButton"))) {
                cancelPagination();
                return;
            } else if (jwt.equals(tokens.get("SelectPageMenu"))) {
                List<SelectOption> selections = SelectMenuInteractionEvent.class.cast(event).getSelectedOptions();
                currentPage = Integer.valueOf(selections.get(0).getValue().split("_")[1]);
            }
            Message msg = new Message(pages.get(currentPage).content != null ? pages.get(currentPage).content : "").addEmbed(pages.get(currentPage).embed);

            if(this.useSelectMenu) {
                msg.addActionRow(getActionRowSelectMenu(false));
            }

            if(this.useButtons) {
                msg.addActionRow(getActionRowButtons());
            }

            Response.EditCommandReply(msg, (InteractionHook) this.message.getMessage());
            startWaiter();
        }
    }
}