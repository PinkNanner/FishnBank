package scripts;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;

import java.util.ArrayList;

@Script.Manifest(name="Flipper", description = "Check player determined list of items and attempt to flip them, primarily for mass quantity flips.", properties = "author=PinkNanner; topic=999; client=4;")
public class Flipper extends PollingScript<ClientContext> {

    GrandExchange ge = new GrandExchange(this.ctx);
    Component PRICE_PER_ITEM = ctx.widgets.component(465, 24, 36);
    ArrayList<Integer> ITEM_LIST;


    public void start(){
        System.out.println("Starting FlipItems");
        ITEM_LIST = new ArrayList<>();
        fillItemList();
        checkItemList();

    }



    public void poll() {

    }



    public void fillItemList(){

        //RUNES
        ITEM_LIST.add(563); //LAW RUNE
        ITEM_LIST.add(561); //NATURE RUNE

        //ORES/BARS
        ITEM_LIST.add(440); //IRON ORE
        ITEM_LIST.add(444); //GOLD ORE
        ITEM_LIST.add(2357); //GOLD BAR
        ITEM_LIST.add(2351); //IRON BAR
    }

    public void checkItemList(){
        ge.open();

        for (int i=0;i<2;i++) {
//            ge.buy(ITEM_LIST.get(i), 1, Integer.getInteger(PRICE_PER_ITEM.text())*2);
            ge.buy(ITEM_LIST.get(i), 1, 500);
        }


        ge.collectToInventory();

        for (int i=0;i<2;i++) {
            Item sellItem = (ctx.inventory.select().id(ITEM_LIST.get(i)).poll());
            ge.sell(sellItem, 1, 1);
        }
    }

    public void stop(){
        System.out.println("Stopping FlipItems");
    }
}
