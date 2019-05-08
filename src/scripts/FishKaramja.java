package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.DepositBox;
import org.powerbot.script.rt4.Npc;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="Karamja F2P Fishing", description = "", properties = "author=PinkNanner; topic=999; client=4;")
public class FishKaramja extends PollingScript<ClientContext> {
    Assets a = new Assets();
    Random r = new Random();
    String itemType;
    static Tile[] pathToBoatFromBank, pathToDockFromBoat;
    int sarimSailors[] = new int[]{3644, 3645, 3646};
    int fishingPonds = 1522;
    boolean atBankSpot = false, atFishingSpot = false, banking = false, fishing = false, starting = true;

    @Override
    public void start(){
        System.out.println("Starting...");
        pathToBoatFromBank = new Tile[]{new Tile(3044, 3235), new Tile(3035+r.nextInt(2), 3235+r.nextInt(2)), new Tile(3027+r.nextInt(2), 3230+r.nextInt(1)),
                new Tile(3027+r.nextInt(2), 3220+r.nextInt(2))};
        pathToDockFromBoat = new Tile[]{new Tile(2955+r.nextInt(2), 3146+r.nextInt(1)), new Tile(2946+r.nextInt(2), 3146+r.nextInt(1)),
                new Tile(2934+r.nextInt(1), 3146+r.nextInt(2)), new Tile(2922+r.nextInt(1), 3150+r.nextInt(2)), new Tile(2916+r.nextInt(1), 3160),
                new Tile(2922+r.nextInt(1), 3163), new Tile(2924+r.nextInt(1), 3173+r.nextInt(1)), new Tile(2924+r.nextInt(1), 3177+r.nextInt(1))};
        ctx.properties.setProperty("randomevents.disable", "true");
    }
    @Override

    public void poll() {
//        System.out.println("Polling for action...");
            if (starting) setStart();
            if (atBankSpot) {
                if (banking) {
                    depositToBank();
                } else travelToKaramja();
            }
            if (atFishingSpot) {
                if (fishing == true) {
                    fish();
                } else if (banking) travelToBank();
            }
    }

    public void travelToKaramja() { //Assumes you are at the deposit box
        System.out.println("Running travelToKaramja");
        if (ctx.players.local().ctx.inventory.select().id(995).poll().stackSize() > 60) {
            System.out.println("Has greater than 60 gold");
            a.resetCamera();
            a.travelTo(pathToBoatFromBank);
            a.newNpc(sarimSailors[r.nextInt(2)], "Pay-fare");
            System.out.println("Pay-fare complete and returned");
            a.clearChatMenus();
//            Condition.wait(new Callable<Boolean>() {
//                @Override
//                public Boolean call() throws Exception {
//                    System.out.println("Player location = "+ctx.players.local().tile());
//                    return ctx.players.local().tile() == new Tile(2956, 3143);
//                }
//            }, 500, 100);
//            a.newDoor(2082, "Cross");
            a.newDoor(2082, 2956, 3146, "Cross");
            a.travelTo(pathToDockFromBoat);
            atFishingSpot = true;
            fishing = true;
            atBankSpot = false;
        }
    }






    public void fish(){
//        System.out.println("Fishing for fish");
//        System.out.println("Animation = "+ctx.players.local().animation());
        if (ctx.players.local().ctx.inventory.select().count() < 28 && ctx.players.local().animation() == -1) {
            System.out.println("Animation is idle");
            Condition.sleep(500+r.nextInt(4750));
            if (ctx.players.local().animation() == -1){
                System.out.println("Animation is still idle");
//                a.newFishingSpot(fishingPonds, "Cage");
                final Npc fishingSpot = ctx.npcs.select().id(fishingPonds).nearest().poll();
                ctx.camera.turnTo(fishingSpot);
                Condition.sleep(50);
                fishingSpot.interact(itemType);
                Condition.sleep(50);
                System.out.println("Caging new fishing spot");
            }
        }
        if (ctx.players.local().ctx.inventory.select().count() > 27){
            Condition.sleep(150+r.nextInt(6500));
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
                fishing = false;
                banking = true;
                System.out.println("Fishing = false");
            }
        }
    }





    public void travelToBank(){
        System.out.println("Travelling to Bank");
        a.resetCamera();
        a.travelToReverse(pathToDockFromBoat);
        a.newNpc(3648, "Pay-Fare");
//        Condition.wait(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                return ctx.players.local().tile() == new Tile(3032, 3217);
//            }
//        }, 500, 100);
//        a.newDoor(2084, "Cross");
        a.newDoor(2084, 3029, 3217, "Cross");
        a.travelToReverse(pathToBoatFromBank);
        atBankSpot = true;
        atFishingSpot = false;
    }




    public void depositToBank(){
        if (ctx.players.local().inMotion() == false){
            a.newObject(26254, "Deposit");
            Component depositBox = ctx.widgets.component(192, 0);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
//                    ctx.depositBox.deposit(377, DepositBox.Amount.ALL);
//                    return ctx.depositBox.ctx.inventory.count() == 2; //DOES NOT WORK
                    ctx.depositBox.deposit(23129, DepositBox.Amount.ALL);
                     if (itemType == "Cage") return ctx.depositBox.deposit(377, DepositBox.Amount.ALL);
                     return ctx.depositBox.deposit(371, DepositBox.Amount.ALL);
                }
            }, 500, 100);
            System.out.println("Deposited items");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    ctx.depositBox.close();
                    return !depositBox.valid();
                }
            }, 500, 100);
            System.out.println("Closed deposit box");
            banking = false;
        }
//        if (ctx .players.local().ctx.inventory.count() == 2) banking = false;
    }
    public void setStart(){
        //DETECTING STARTING LOCATION
        for (int i=0;i<15;i++){ //Starting at docks
            for (int k=0;k<15;k++){
                if (ctx.players.local().tile().equals(new Tile(2920+i, 3170+k))){
                    atFishingSpot = true;
                    fishing = true;
                    atBankSpot = false;
                    System.out.println("Detected player at fishing spot, starting from there");
                    break;
                }
            }
        }
        for (int i=0;i<10;i++){ //Starting at depositbox
            for (int k=0;k<5;k++){
                if (ctx.players.local().tile().equals(new Tile(3041+i, 3234+k))){
                    atBankSpot = true;
                    if (ctx.players.local().ctx.inventory.count() < 28) banking = false;
                        else banking = true;
                    System.out.println("Detected player at deposit box, starting from there");
                    break;
                }
            }
        }
//        for (int i=0;i<2;i++){ //Starting at sarim bank
//            for (int k=0;k<6;k++){
//                if (ctx.players.local().tile().equals(new Tile(2924+i, 3175+k))){
//                    atFishingSpot = true;
//                    System.out.println("Detected player at fishing spot, starting from there");
//                    break;
//                }
//            }
//        }
        if (ctx.players.local().ctx.inventory.select().id(301).count() > 0) itemType = "Cage";
        else if (ctx.players.local().ctx.inventory.select().id(311).count() > 0) itemType = "Harpoon";
        System.out.println("Item Type = "+itemType);
        a.resetCamera();
        starting = false;
    }
    public void stop(){
        System.out.println("Stopped");
    }
}
