package net.hasibix.hasicraft.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.hasibix.hasicraft.discordbot.models.client.responsebuilders.Pagination;

public class PaginationHandler {
    public List<Pagination> paginations;

    public PaginationHandler() {
        this.paginations = new ArrayList<Pagination>();
    }

    public void add(Pagination pagination) {
        this.paginations.add(pagination);
        pagination.setWaiter(new EventWaiter());
    }

}
