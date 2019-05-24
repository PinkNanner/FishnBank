package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="Fishing Guild", description = "", properties = "author=PinkNanner; topic=999; client=4;")
public class FishingGuild extends PollingScript<ClientContext> {
    Assets a = new Assets();
    Random r = new Random();
    boolean atBank, starting = true, banking;
    static Tile[] pathToFishFromBank;
    int FISHING_ID = 1510;
    String action;



    @Override
    public void poll() {
        if (starting) setStart();

        if (!banking && !atBank){
            fish();
        } else if (banking && atBank){
            bank();
        }
    }


    public void fish(){
        System.out.println("Fishing");
        if (ctx.players.local().ctx.inventory.select().count() > 27){
            Condition.sleep(150+r.nextInt(1500));
            if (ctx.players.local().ctx.inventory.select().id(359).count() > 0){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        ctx.inventory.select().id(359).poll().interact("Drop");
                        return ctx.players.local().ctx.inventory.select().id(359).count() == 0;
                    }
                }, 500, 100);
            } else {
                a.resetCamera();
                banking = true;
//                if (ctx.players.local().tile().x() > 2850){
//                    a.travelTo(2848+r.nextInt(2), 3431+r.nextInt(2), 100);
//                }
                a.travelToReverse(pathToFishFromBank);
                atBank = true;
                System.out.println("banking = true");
            }
        } else {
            final Npc fishingSpot = ctx.npcs.select().id(FISHING_ID).nearest().poll();
//            if (fishingSpot.valid() == false){
//                a.travelTo(2850, 3429, 100);
//            } else
            Condition.sleep(r.nextInt(1750)+100);
//            a.travelTo(fishingSpot.tile().x(), fishingSpot.tile().y()+1, 50);
            if ((ctx.players.local().animation() == -1) && ctx.players.local().inMotion() == false){
                ctx.camera.turnTo(fishingSpot);
                fishingSpot.interact(action);
                Condition.sleep(r.nextInt(1750)+100);
//                if ((ctx.players.local().animation() == -1)){
//                    a.travelTo(2850, 3429, 100);
//                }
            }
        }
    }



    public void bank() {
        System.out.println("Banking");
//            Component depositBox = ctx.widgets.component(192, 0);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                a.newObject(10355, "Bank");
//                Condition.sleep(1200);
                return ctx.bank.depositAllExcept(301, 311);
//                    return ctx.players.local().ctx.inventory.count() == 1;
            }
        }, 500, 100);
        System.out.println("Deposited items");
        if (ctx.players.local().ctx.inventory.count() == 1){
            banking = false;
            a.travelTo(pathToFishFromBank);
            atBank = false;
        }
    }

    public void setStart(){
        System.out.println("Starting");
        ctx.properties.setProperty("randomevents.disable", "true");
        pathToFishFromBank = new Tile[]{new Tile(2586+r.nextInt(2), 3418+r.nextInt(3)), new Tile(2598+r.nextInt(1), 3420+r.nextInt(1)),
                new Tile(2601+r.nextInt(2), 3424+r.nextInt(1))}; // new Tile(2848+r.nextInt(2), 3431+r.nextInt(2))};

        starting = false;
        if (ctx.players.local().ctx.inventory.select().id(311).count() > 0){
            action = "Harpoon";
        } else action = "Cage";
        a.resetCamera();
        for (int i=0;i<10;i++){
            for (int k=0;k<10;k++){
                if (ctx.players.local().tile().equals(new Tile(2586+i, 3415+k))){
                    atBank = true;
                    banking = true;
                    System.out.println("Detected player at bank, starting from there");
                    return;
                }
            }
        }
    }
}
