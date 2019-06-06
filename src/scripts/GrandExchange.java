package scripts;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Game.Crosshair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Grand Exchange Interface for rt4.
 *
 * @author Mooshe
 */
public class GrandExchange extends ClientAccessor {

    public static final String 	GE_CLERK 			= "Grand Exchange Clerk";
    public static final int		WIDGET 				= 465;
    public static final int		SLOT_OFFSET 			= 7;
    public static final int		PROGRESS_BAR 			= 21;
    public static final int		PROGRESS_TOTAL 			= 22;
    public static final int		BUY_COMPONENT 			= 0;
    public static final int		SELL_COMPONENT 			= 1;
    public static final int		INPUT_COMPONENT 		= 24;
    public static final int		LABEL_COMPONENT 		= 25;
    public static final int		QUANTITY_INPUT_COMPONENT 	= 32;
    public static final int		QUANTITY_COMPONENT 		= 49;
    public static final int		PRICE_INPUT_COMPONENT 		= 43;
    public static final int		PRICE_COMPONENT 		= 52;
    public static final int		CONFIRM_COMPONENT 		= 54;
    public static final int		COLLECT_COMPONENT 		= 6;
    public static final int		COLLECT_SUBCOMPONENT 		= 1;
    public static final int		CLOSE_COMPONENT 		= 2;
    public static final int		CLOSE_SUBCOMPONENT 		= 11;
    public static final int		SEARCH_WIDGET 			= 162;
    public static final int		SEARCH_COMPONENT 		= 33;
    public static final int		QUERY_COMPONENT 		= 38;
    public static final int		QUERY_SELECT_COMPONENT 		= 1;
    public static final int		SEARCH_LABEL_COMPONENT 		= 32;

    public GrandExchange(final ClientContext ctx) {
        super(ctx);
    }

    /**
     * Opens the grand exchange widget.
     *
     * @return true if it has successfully opened the grand exchange.
     */
    public boolean open() {
        if(opened())
            return true;

        ctx.npcs.select().name(GE_CLERK).nearest();
        if(ctx.npcs.isEmpty())
            return false;

        if(!ctx.npcs.peek().inViewport())
            ctx.camera.turnTo(ctx.npcs.peek());

        ctx.npcs.peek().interact(true, "Exchange",
                "Grand Exchange Clerk");
        return Condition.wait(new Callable<Boolean>() {
            public Boolean call() {
                while(ctx.players.local().inMotion());
                return opened();
            }
        }, 300, 3);
    }

    /**
     *
     * @return true if the grand exchange widget is open.
     */
    public boolean opened() {
        return ctx.widgets.widget(WIDGET).valid();
    }

    /**
     * Closes the grand exchange widget
     *
     * @return true if the grand exchange is no longer opened.
     */
    public boolean close() {
        if(!opened())
            return true;
        final Component close = ctx.widgets.component(WIDGET,
                CLOSE_COMPONENT).component(CLOSE_SUBCOMPONENT);
        return Condition.wait(new Callable<Boolean>() {
            public Boolean call() {
                if(ctx.game.crosshair() == Crosshair.NONE && close.valid()
                        && close.visible() && !close.click())
                    return false;
                return !opened();
            }
        }, 100, 20);
    }

    /**
     * Buys an item from the grand exchange.
     *
     * @param item The item id to search for
     * @param amount The amount of the item to buy
     * @param price The price to buy each item at
     * @return true if the item has been successfully listed
     */
    public boolean buy(final int item, final int amount, final int price) {
        if(!opened())
            return false;

        List<Component> avail = getVacantSlots();
        if(avail.isEmpty())
            return false;
        avail.get(Random.nextInt(0, avail.size())).component(BUY_COMPONENT)
                .click();
        if(!Condition.wait(new Callable<Boolean>() {
            public Boolean call() {
                return ctx.widgets.component(SEARCH_WIDGET, SEARCH_COMPONENT)
                        .visible();
            }
        }, 100, 25))
            return false;

        final CacheItemConfig cic = CacheItemConfig.load(item);
        if (!cic.valid())
            return false;

        Condition.sleep(500);
        ctx.input.send(cic.name.toLowerCase());

        if(!Condition.wait(new Callable<Boolean>() {
            public Boolean call() {
                return ctx.widgets.component(SEARCH_WIDGET, QUERY_COMPONENT)
                        .component(QUERY_SELECT_COMPONENT).visible();
            }
        }, 100, 30))
            return false;

        Component query = ctx.widgets.component(SEARCH_WIDGET, QUERY_COMPONENT);

        Component[] results = query.components();

        int itemID = cic.noted ? item - 1 : item;
        final CacheItemConfig cicn = CacheItemConfig.load(itemID);

        if(! (cicn.valid() && cicn.name.equals(cic.name)) )
            itemID = item;

        for (int i = 0, j = results.length; i < j; i++)
            if ((results[i].itemId() == itemID) && results[i - 2].click())
                break;

        if(!matchesTitle(cic.name))
            return false;


        return setQuantity(amount) && setPrice(price) && confirm();
    }

    /**
     * Sells the specified item to the grand exchange.
     *
     * @param item The inventory item to be sold
     * @param amount The stack size to sell in the grand exchange
     * @param price The price to list each item at
     * @return true if it has been successfully listed in the grand exchange
     */
    public boolean sell(final Item item, final int amount, final int price) {
        return opened() && item.id() != -1 && item.click() &&
                matchesTitle(item.name()) &&
                setQuantity(amount) && setPrice(price) && confirm();
    }

    /**
     * The amount of available slots within the Grand Exchange. Any
     * items occupied within a slot will not be counted, or if the slot
     * is disabled due to lack of membership.
     *
     * @return The amount of vacant slots.
     */
    public int getAvailableSlots() {
        return getVacantSlots().size();
    }

    /**
     * Checks whether or not the specified slot is vacant for use.
     *
     * @param slot The slot to check is vacant
     * @return true if the slot is vacant
     */
    public boolean isVacant(final int slot) {
        return !ctx.widgets.component(WIDGET, SLOT_OFFSET + slot)
                .component(PROGRESS_BAR).visible();
    }

    /**
     * Gets the progress from the selected slot.
     *
     * @param slot The slot to check the progress of
     * @return Will return the progress as a double (0.0 to 1.0). If the
     * slot is vacant or invalid, it will return -1.0.
     */
    public double getProgress(final int slot) {
        Component parent = getSlot(slot);
        Component progress = parent.component(PROGRESS_TOTAL);
        Component bar = parent.component(PROGRESS_BAR);
        if(!progress.visible() || !bar.visible())
            return -1.0;
        return ((double) progress.width()) / bar.width();
    }

    /**
     * Collects all items within the grand exchange that are available for
     * collection. All of the items will be deposited into the player's
     * inventory.
     *
     * @return true if it has successfully collected items to the inventory.
     */
    public boolean collectToInventory() {
        return collect(false);
    }

    /**
     * Collects all items within the grand exchange that are available for
     * collection. All of the items will be deposited into the bank.
     *
     * @return true if it has successfully collected items to the bank.
     */
    public boolean collectToBank() {
        return collect(true);
    }

    private boolean collect(final boolean toBank) {
        Component collect = ctx.widgets.component(WIDGET, COLLECT_COMPONENT)
                .component(COLLECT_SUBCOMPONENT);
        return collect.valid() && collect.visible() && collect
                .interact("Collect to "+(toBank ? "bank" : "inventory"));
    }

    private boolean setPrice(final int price) {
        return set(""+price, PRICE_INPUT_COMPONENT, PRICE_COMPONENT);
    }

    private boolean setQuantity(final int quantity) {
        return set(""+quantity, QUANTITY_INPUT_COMPONENT, QUANTITY_COMPONENT);
    }

    private boolean set(final String value, final int input, final int btn) {
        final Component comp = ctx.widgets.component(WIDGET, INPUT_COMPONENT);
        if(comp.component(input).text().replaceAll("[^\\d]", "").equals(value))
            return true;
        if(!comp.visible() || !comp.component(btn).click() || !Condition.wait(
                new Callable<Boolean>() {
                    public Boolean call() {
                        return ctx.widgets.component(SEARCH_WIDGET,
                                SEARCH_LABEL_COMPONENT).visible();
                    }
                }, 100, 25))
            return false;
        return ctx.input.sendln(value);
    }

    private boolean matchesTitle(final String title) {
        return Condition.wait(new Callable<Boolean>() {
            public Boolean call() {
                Component c = ctx.widgets.component(WIDGET, INPUT_COMPONENT)
                        .component(LABEL_COMPONENT);
                return c.valid() && c.visible() &&
                        c.text().equalsIgnoreCase(title);
            }
        }, 100, 25);
    }

    private List<Component> getVacantSlots() {
        List<Component> components = new ArrayList<Component>();
        for(int i = 0; i < 8; i++) {
            if(!getSlot(i).component(PROGRESS_BAR).visible())
                components.add(getSlot(i));
        }
        return components;
    }

    private boolean confirm() {
        Component confirm = ctx.widgets.component(WIDGET, INPUT_COMPONENT)
                .component(CONFIRM_COMPONENT);
        return confirm.valid() && confirm.visible() && confirm.click();
    }

    private Component getSlot(int slot) {
        if(slot < 0 || slot > 7)
            return ctx.widgets.nil().component(0);
        return ctx.widgets.component(WIDGET, SLOT_OFFSET + slot);
    }
}