package scripts;


import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="FishShrimp", description = "Fill inventory then bank", properties = "author=PinkNanner; topic=999; client=4;")
public class FishShrimp extends PollingScript<ClientContext> {
    final static int FISHING_ID[] = {1524, 1525, 1526};
    final static int BANKER_ID[] = {394, 395};
    int inventories, inventMax;
    String action;
    static Random r = new Random();
//    final static Area AREA_BANK = new Area(); Causes SEVERE ERROR ???
    static Tile[] tileToBank;
    static Tile[] tileToFish;
    public static boolean fullInvent = false, inBank = false, inPain = false;

    @Override
    public void start(){
        System.out.println("Started");
        tileToBank = new Tile[]{new Tile(r.nextInt(3)+3092,r.nextInt(4)+3241)};
        tileToFish = new Tile[]{new Tile(r.nextInt(4)+3088, r.nextInt(3)+3229)};
        ctx.properties.setProperty("randomevents.disable", "true");
        if (ctx.players.local().ctx.inventory.select().count() < 28){
            fullInvent = false;
            travelTo(tileToFish);
        }
        if (ctx.players.local().ctx.inventory.select().count() == 28){
            fullInvent = true;
            travelTo(tileToFish);
        }
        for (int i=0;i<4;i++){
            for (int k=0;k<6;k++){
                if (ctx.players.local().tile().equals(new Tile(3092+i, 3240+k))){
                    inBank = true;
                    System.out.println("Detected player in bank, starting from there");
                    break;
                }
            }
        }
        if (ctx.players.local().ctx.inventory.select().id(307).count() > 0){
            action = "Bait";
        } else action = "Small Net";
        inventories = 0;
        inventMax = 100;
    }

    @Override
    public void poll() {
//        while (inventories < inventMax){
            fishForever();
//        }

    }
    public void fishForever(){
        if (!inPain) {
            if (!fullInvent) {
                fish();
            } else {
                bank();
            }
        } else {
            flee();
        }
        if (ctx.players.local().inCombat()) inPain = true;
    }

    public void fish(){
        final Npc fishingSpot = ctx.npcs.select().id(FISHING_ID).nearest().poll();
        if ((ctx.players.local().animation() == -1) && !inBank){
            Condition.sleep(r.nextInt(4750)+1750);
            if ((ctx.players.local().animation() == -1)){
                ctx.camera.turnTo(fishingSpot);
                Condition.sleep(300);
                fishingSpot.interact(action);
            }
        } else if ((ctx.players.local().animation() == -1) && inBank){
            ctx.movement.newTilePath(new Tile(r.nextInt(4)+3088, r.nextInt(3)+3227)).traverse(); //to fish
            Condition.sleep(250);
            if (ctx.players.local().inMotion()) inBank = false;
            resetCamera();
//            pathToFish.traverse();
            Condition.sleep(r.nextInt(4750)+1750);
        }
        if ((ctx.players.local().ctx.inventory.select().count() == 28)) {
            Condition.sleep(r.nextInt(4750)+1750);
            fullInvent = true;
            System.out.println("Inventory is full");
        }
    }
    public void flee(){
        System.out.println("Fleeing");
        ctx.movement.newTilePath(new Tile(r.nextInt(3)+3092,r.nextInt(4)+3241)).traverse(); // to bank
        inBank = true;
        Condition.sleep(r.nextInt(60000)+67500);
        resetCamera();
        inPain = false;
//        pathToFish.traverse();
    }


    public void bank(){
//        System.out.println("inBank = "+inBank);
        if (ctx.players.local().inMotion() == false && !inBank){
            resetCamera();
            ctx.movement.newTilePath(new Tile(r.nextInt(3)+3092,r.nextInt(4)+3241)).traverse(); // to bank
//            pathToBank.traverse();
            Condition.sleep(250);
            if (ctx.players.local().inMotion()) inBank = true;
            Condition.sleep(r.nextInt(4750)+1750);
//            System.out.println("inBank = true");
        }
        else if (ctx.players.local().inMotion() == false && inBank){
            final Npc banker = ctx.npcs.select().id(BANKER_ID).nearest().poll();
            final GameObject bankBooth = ctx.objects.select().id(6947).nearest().poll();
            if (bankBooth.valid() == false) travelTo((r.nextInt(3)+3092),(r.nextInt(4)+3241), 100);
            bankBooth.interact("Bank");
            ctx.bank.depositAllExcept(303, 307, 313);
            inventories++;
            if (ctx.players.local().ctx.inventory.select().count() <= 2) fullInvent = false;
        }

    }
    public void newNpc(int id){
        Npc guy = ctx.npcs.select().id(id).nearest().poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (!guy.valid()){
                    travelTo(3209+r.nextInt(7), 3423+r.nextInt(8), 500);
                }
                ctx.camera.turnTo(guy);
                Condition.sleep(500);
                guy.interact("Talk-to");
                Condition.sleep(500);
                return ctx.chat.canContinue();
            }
        });
        resetCamera();
    }

    public void newDoor(int id, int x, int y){ //creates a new door and opens it
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject door = ctx.objects.select().id(id).nearest().poll();
                ctx.camera.turnTo(door);
                ctx.camera.pitch(1);
                Condition.sleep(500);
                door.interact("Open");
                Condition.sleep(500);
                return ctx.players.local().tile().equals(new Tile(x, y));
            }
        }, 500, 100);
        resetCamera();
    }

    public void continueChat(int times){
        for (int i=0;i<times;i++){
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    ctx.chat.clickContinue();
                    return ctx.chat.canContinue();
                }
            }, 100, 100);
            ctx.input.click(267, 507, 1);
            Condition.sleep(100);
            ctx.chat.clickContinue();
        }
        Condition.sleep(200);
    }
    public void continueChat(int times, int sleep){
        for (int i=0;i<times;i++){
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    ctx.chat.clickContinue();
                    return ctx.chat.canContinue();
                }
            }, 100, 100);
            ctx.input.click(267, 507, 1);
            ctx.chat.clickContinue();
            Condition.sleep(100);
            Condition.sleep(sleep);
        }
        Condition.sleep(300);
    }
    public void travelTo(Tile[] t){ //TODO: if target distance is greater than, say, 30, return path as completed to allow for starting quests part-way through

        /*
                TODO: create an array of all the tiles between player and destination, make player click on those tiles randomly
                Try making the array have to be a certain distance away from player current position, if its right beside you it'll cause delays
                When player gets close to target in array create a new array (possibly using the next targetTile
         */

        for (int i=0;i<t.length;i++) {
            int finalI = i;
            System.out.println("FINALI = "+finalI);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    ctx.movement.newTilePath(t[finalI]).traverse();
                    System.out.println("DISTANCE FROM PLAYER TO TARGET = "+ctx.players.local().tile().distanceTo(t[finalI]));
                    if (ctx.players.local().tile().distanceTo(t[finalI]) < 5) { //TODO: Needs to be modified based on distance between previous tiles
                        System.out.println("DISTANCE FROM PLAYER TO TARGET < 5, RETURNING TRUE");
                        return true;
                    }
                    if (isPlayerAtTile(t[finalI])) return  true;
                    return ctx.players.local().tile().equals(t[finalI]);
                }
            }, 100, 100);
        }
    }
    public void travelToReverse(Tile[] t){
        for (int i=t.length;i>-1;i--) {
            int finalI = i;
            System.out.println("FINALI = "+finalI);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    ctx.movement.newTilePath(t[finalI]).traverse();
                    System.out.println("DISTANCE FROM PLAYER TO TARGET = "+ctx.players.local().tile().distanceTo(t[finalI]));
                    if (ctx.players.local().tile().distanceTo(t[finalI]) < 5) {
                        System.out.println("DISTANCE FROM PLAYER TO TARGET < 5, RETURNING TRUE");
                        return true;
                    }
                    if (isPlayerAtTile(t[finalI])) return  true;
                    return ctx.players.local().tile().equals(t[finalI]);
                }
            }, 100, 100);
        }
    }
    public void travelTo(int x, int y, int sleep){
        System.out.println("Travelling to: "+x+", "+y);
        Tile targetTile = new Tile(x, y);
        ctx.movement.newTilePath(targetTile).traverse();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.movement.newTilePath(targetTile).traverse();
//                System.out.println("player tile = "+ctx.players.local().tile()+", target tile = "+targetTile+", distance between = "+ctx.players.local().tile().distanceTo(targetTile));
                if (isPlayerAtTile(targetTile)) return true;
                if (ctx.players.local().tile().distanceTo(targetTile) <= 0) return true;
                return ctx.players.local().tile().equals(targetTile);
            }
        }, sleep, 100);
    }
    public void travelTo(int x, int y, int sleep, int doorId){
        System.out.println("Travelling to: "+x+", "+y);
        Tile targetTile = new Tile(x, y);
        ctx.movement.newTilePath(targetTile).traverse();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (checkDoor(doorId) == true){
                    System.out.println("Waiting for newDoor");
                    newDoor(doorId, "Open");
                }
                ctx.movement.newTilePath(targetTile).traverse();
                System.out.println("player tile = "+ctx.players.local().tile()+", target tile = "+targetTile+", distance between = "+ctx.players.local().tile().distanceTo(targetTile));
                if (isPlayerAtTile(targetTile)) return true;
//                if (ctx.players.local().tile().distanceTo(targetTile) <= 2) return true;
                return ctx.players.local().tile().equals(targetTile);
            }
        }, sleep, 100);
    }
    public void resetCamera(){
        Component lookNorth = ctx.widgets.component(548, 7);
        lookNorth.click();
        ctx.camera.pitch(99);
        Condition.sleep(1000);
    }
    public void clearMenus(){
        ctx.input.click(267, 507, 1);
        Condition.sleep(20);
        ctx.input.click(267, 534, 1);
        Condition.sleep(20);
        ctx.input.click(267, 527, 1);
        Condition.sleep(20);
    }
    public void openInventory(){
        Component inventory = ctx.widgets.component(161, 61);
        inventory.click();
        Condition.sleep(200);
    }
    public void openOptions(){
        Component inventory = ctx.widgets.component(161, 46);
        inventory.click();
        Condition.sleep(200);
    }
    public boolean checkDoor(int doorId){
        GameObject door = ctx.objects.select().id(doorId).within(3).nearest().poll();
        System.out.println("checkDoor = "+door.valid());
        return door.valid();
    }
    public boolean isPlayerAtTile(Tile t){
        if (ctx.players.local().tile().x() == t.x() && ctx.players.local().tile().y() == t.y()) return true;
        return false;
    }
    public void newDoor(int id, int x, int y, String action){
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject door = ctx.objects.select().id(id).nearest().poll();
                if (door.valid() == false) return true;
                System.out.println("DISTANCE TO DOOR = "+ctx.players.local().tile().distanceTo(door.tile()));
                if (ctx.players.local().tile().distanceTo(door.tile()) > 5) return true;
                ctx.camera.turnTo(door);
                ctx.camera.pitch(1);
                Condition.sleep(500);
                if (!isPlayerAtTile(new Tile(x, y))) door.interact(action);
                Condition.sleep(500);
                return ctx.players.local().tile().equals(new Tile(x, y));
            }
        }, 500, 100);
        resetCamera();
    }
    public void newDoor(int id, String action){ //For doors that don't cause the player to move
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject door = ctx.objects.select().id(id).nearest().poll();
                if (ctx.players.local().tile().distanceTo(door.tile()) > 5) return true; //To prevent picking a door far away if the target door is already open
//                ctx.camera.turnTo(door);
                ctx.camera.angle(r.nextInt(360));
                ctx.camera.pitch(r.nextInt(45));
                Condition.sleep(500);
                door.interact(action);
                Condition.sleep(500);
                return !door.valid();
            }
        }, 500, 100);
        resetCamera();
    }
    @Override
    public void stop(){
        System.out.println("Stopped");
    }
}
