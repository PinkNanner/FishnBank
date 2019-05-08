import java.util.ArrayList;
import java.util.List;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.ItemQuery;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.Widget;

/**
 * Player Trading Interface.
 * 
 * @author Mooshe
 */
public class Trade extends ItemQuery<Item> {
	
	public static final String WAIT_TEXT = "Other player has accepted.";
	public static final int
			WIDGET_SECOND = 334,
			ACCEPT_SECOND = 49,
			DECLINE_SECOND = 54,
			WAITING_FOR_SECOND = 14,
			
			WIDGET = 335,
			CLOSE_BUTTON = 14,
			ACCEPT_FIRST = 21,
			DECLINE_FIRST = 23,
			OUR_INVENTORY = 26,
			THEIR_INVENTORY = 29,
			WAITING_FOR_PLAYER = 33,
			DEPOSIT_INVENTORY = 40, 
			DEPOSIT_POUCH = 41,
			OTHER_VALUE = 47,
			
			MONEY_WIDGET = 1469,
			MONEY_COMPONENT = 2;
	
	public Trade(ClientContext ctx) {
		super(ctx);
	}
	
	/**
	 * Trades with the specified {@link Player}.
	 * 
	 * @param player The player to trade with.
	 * @return {@code true} if the player has been interacted with, otherwise,
	 * {@code false}.
	 */
	public boolean tradeWith(Player player) {
		return !opened() && player.valid() && 
				player.interact("Trade with", player.name());
	}
	
	/**
	 * Determines whether or not the Trading interface is opened.
	 * 
	 * @return {@code true} if opened, {@code false} otherwise.
	 */
	public boolean opened() {
		return firstOpened() || secondOpened();
	}
	
	/**
	 * Accepts the trade, whether it be on the first or second trading screen.
	 * If on the first screen, it will require to be invoked once more in order
	 * to accept on the second trading screen.
	 * 
	 * @return {@code true} if the accept button has been properly clicked,
	 * otherwise, {@code false}.
	 */
	public boolean accept() {
		return opened() && click(firstOpened() ? ACCEPT_FIRST : ACCEPT_SECOND);
	}
	
	/**
	 * Declines the trade, whether it be on the first or second trading screen.
	 * 
	 * @return {@code} true if the decline button has been properly clicked,
	 * otherwise, {@code false}.
	 */
	public boolean decline() {
		return opened() && click(firstOpened() ? DECLINE_FIRST:DECLINE_SECOND);
	}
	
	/**
	 * Offers the item up for trade.
	 * 
	 * @param item The {@link Item} instance.
	 * @return {@code true} if the item has been successfully offered,
	 * otherwise, {@code false}.
	 */
	public boolean offer(Item item) {
		return firstOpened() && item.valid() && item.interact("Offer");
	}
	
	/**
	 * Offers money up for trade.
	 * 
	 * @param amount The amount you would like to trade.
	 * @return {@code true} if the request to transfer money has been sent,
	 * otherwise, {@code false}.
	 */
	public boolean offerMoney(int amount) {
		return firstOpened() && getWidget().component(DEPOSIT_POUCH).click() &&
				Condition.wait(new Condition.Check() {
			public boolean poll() {
				return ctx.widgets.component(MONEY_WIDGET,
						MONEY_COMPONENT).visible();
			}
		}, 100, 10) && ctx.input.sendln(""+amount);
	}
	
	/**
	 * Offers our inventory in the trading screen.
	 * 
	 * @return {@code true} if the inventory button has been properly clicked,
	 * otherwise, {@code false}.
	 */
	public boolean offerInventory() {
		return firstOpened() && ctx.widgets.component(WIDGET,
				DEPOSIT_INVENTORY).click();
	}

	/**
	 * Determines if the other player has accepted.
	 * 
	 * @return {@code true} if the player has accepted, otherwise,
	 * {@code false}.
	 */
	public boolean hasPlayerAccepted() {
		return opened() && getWidget().component(firstOpened()
					? WAITING_FOR_PLAYER : WAITING_FOR_SECOND)
						.text().equals(WAIT_TEXT);
	}
	
	/**
	 * Gets the market value of the other player's offer on the first trading
	 * screen.
	 * 
	 * @return The Market Value of the other player's items. Only works on the
	 * first trading screen, otherwise, will return {@code 0}.
	 */
	public int marketValue() {
		int value = 0;
		if(!firstOpened())
			return value;
		try {
			String s = ctx.widgets.component(WIDGET, OTHER_VALUE).text()
					.split(" ")[0];
			value = Integer.parseInt(s.replace(",", ""));
		} catch(Exception e) {
			
		}
		return value;
	}
	
	@Override
	public Item nil() {
		return new Item(ctx, -1, -1, null);
	}

	@Override
	protected List<Item> get() {
		List<Item> list = new ArrayList<Item>();
		if(!firstOpened())
			return list;
		Component[] inv = ctx.widgets.component(WIDGET, THEIR_INVENTORY)
				.components();
		for(int i = 0; i < 28; i++) {
			if(inv[i].itemId() < 0)
				continue;
			list.add(new Item(ctx, inv[i].itemId(),
					inv[i].itemStackSize(), inv[i]));
		}
		return list;
	}
	
	private boolean firstOpened() {
		return ctx.widgets.component(WIDGET, ACCEPT_FIRST).visible();
	}
	
	private boolean secondOpened() {
		return ctx.widgets.component(WIDGET_SECOND, ACCEPT_SECOND).visible();
	}
	
	private boolean click(int component) {
		 return getWidget().component(component).click();
	}
	
	private Widget getWidget() {
		return ctx.widgets.widget(secondOpened() ? WIDGET_SECOND : WIDGET);
	}
}
