package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.Random;
import java.util.concurrent.Callable;

public class Assets extends PollingScript<ClientContext> {
    Random r = new Random();

    public void start(){

    }
    public void poll(){

    }
    public void stop(){

    }

    public void newNpc(int id){
        Npc guy = ctx.npcs.select().nearest().id(id).poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                travelTo(guy.tile().x(), guy.tile().y(), 100);
                ctx.camera.turnTo(guy);
                Condition.sleep(500);
                guy.interact("Talk-to");
                Condition.sleep(500);
                return ctx.chat.canContinue();
            }
        }, 500, 1000);
        resetCamera();
    }
    public void newNpc(int id, String action){
        Npc guy = ctx.npcs.select().nearest().id(id).poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.camera.turnTo(guy);
                travelTo(guy.tile().x(), guy.tile().y(), 100);
                Condition.sleep(50);
                guy.interact(action);
                Condition.sleep(50);
                return ctx.chat.canContinue();
            }
        }, 500, 1000);
    }

    public void newFishingSpot(int id, String action){ //TODO: WHEN THIS RETURNS IT STOPS ALL ACTION
        final Npc fishingSpot = ctx.npcs.select().id(id).nearest().poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.camera.turnTo(fishingSpot);
                fishingSpot.interact(action);
                Condition.sleep(1000);
                return !(ctx.players.local().animation() == -1);
            }
        }, 500, 1000);
    }

    public void newObject(int id, String action) {
        final GameObject target = ctx.objects.select().id(id).nearest().poll();
        Condition.wait(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return target.interact(action);
//                return target.valid();
            }
        }, 500, 1000);
    }

    public void newDoor(int id, int x, int y){ //For doors that cause the player to move
        if (!ctx.players.local().tile().equals(new Tile(x, y))) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    GameObject door = ctx.objects.select().id(id).nearest().poll();
                    if (ctx.players.local().tile().distanceTo(door.tile()) > 5) return true;
                    ctx.camera.turnTo(door);
                    ctx.camera.pitch(1);
                    Condition.sleep(500);
                    if (!isPlayerAtTile(new Tile(x, y))) door.interact("Open");
                    Condition.sleep(500);
                    return ctx.players.local().tile().equals(new Tile(x, y));
                }
            }, 500, 100);
        } else {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    GameObject door = ctx.objects.select().id(id).nearest().poll();
                    if (ctx.players.local().tile().distanceTo(door.tile()) > 5) return true; //To prevent picking a door far away if the target door is already open
                    ctx.camera.turnTo(door);
                    ctx.camera.pitch(1);
                    Condition.sleep(500);
                    door.interact("Open");
                    Condition.sleep(500);
                    return !door.valid();
                }
            }, 500, 100);
        }
        resetCamera();
    }
    public void newDoor(int id, String action){ //For doors that don't cause the player to move
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject door = ctx.objects.select().id(id).nearest().poll();
                if (ctx.players.local().tile().distanceTo(door.tile()) > 5) return true; //To prevent picking a door far away if the target door is already open
//                ctx.camera.turnTo(door);
                ctx.camera.turnTo(door);
                Condition.sleep(500);
                door.interact(action);
                Condition.sleep(1500);
                return !door.valid();
            }
        }, 500, 100);
        resetCamera();
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
    public void newDoor(int id, int x, int y, String action, boolean soloDoor){
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject door = ctx.objects.select().id(id).nearest().poll();
                if (door.valid() == false) return true;
                System.out.println("DISTANCE TO DOOR = "+ctx.players.local().tile().distanceTo(door.tile()));
                if (ctx.players.local().tile().distanceTo(door.tile()) > 5 && soloDoor == false) return true;
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
    public boolean isPlayerAtTile(Tile t){
        if (ctx.players.local().tile().x() == t.x() && ctx.players.local().tile().y() == t.y()) return true;
        return false;
    }
    public boolean checkDoor(int doorId){
        GameObject door = ctx.objects.select().id(doorId).within(3).nearest().poll();
        System.out.println("checkDoor = "+door.valid());
        return door.valid();
    }

    public void continueChat(int times){
        for (int i=0;i<times;i++){
            Condition.sleep(200);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.chat.canContinue();
                }
            }, 200, 200);
            Condition.sleep(300);
            ctx.chat.clickContinue();
            System.out.println("Chat continued");
        }
        Condition.sleep(200);
        System.out.println("Continue chat ended");
    }
    public void continueChat(String text){
        Condition.sleep(200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.chatting();
            }
        }, 200, 200);
        Condition.sleep(300);
        ctx.chat.continueChat(text);
        System.out.println("Chat continued with string: "+text);
        Condition.sleep(200);
        System.out.println("Continue chat ended");
    }
    public void continueChatScene(String phrase){
        Condition.sleep(200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Component chatBox = ctx.widgets.component(231, 4);
                System.out.println("Chatbox = : "+chatBox.text());
//                System.out.println("Waiting for string: "+phrase);
                return chatBox.text().endsWith(phrase);
            }
        }, 200, 100);
        System.out.println("Chat scene ended");
    }
    public void travelTo(Tile[] t){ //TODO: if target distance is greater than, say, 30, return path as completed to allow for starting quests part-way through

        /*
                TODO: create an array of all the tiles between player and destination, make player click on those tiles randomly
                Try making the array have to be a certain distance away from player current position, if its right beside you it'll cause delays
                When player gets close to target in array create a new array (possibly using the next targetTile
         */

        for (int i=0;i<t.length;i++) {
            int finalI = i;
//            System.out.println("FINALI = "+finalI);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    if (ctx.players.local().ctx.movement.destination() != t[finalI]) ctx.movement.newTilePath(t[finalI]).traverse();
//                    System.out.println("DISTANCE FROM PLAYER TO TARGET = "+ctx.players.local().tile().distanceTo(t[finalI]));
                    if (ctx.players.local().tile().distanceTo(t[finalI]) < 5) { //TODO: Needs to be modified based on distance between previous tiles
//                        System.out.println("DISTANCE FROM PLAYER TO TARGET < 5, RETURNING TRUE");
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
//            System.out.println("FINALI = "+finalI);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (ctx.players.local().ctx.movement.destination() != t[finalI])  ctx.movement.newTilePath(t[finalI]).traverse();
//                    System.out.println("DISTANCE FROM PLAYER TO TARGET = "+ctx.players.local().tile().distanceTo(t[finalI]));
                    if (ctx.players.local().tile().distanceTo(t[finalI]) < 5) {
//                        System.out.println("DISTANCE FROM PLAYER TO TARGET < 5, RETURNING TRUE");
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
        if (ctx.players.local().ctx.movement.destination() != targetTile) ctx.movement.newTilePath(targetTile).traverse();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (ctx.players.local().ctx.movement.destination() != targetTile) ctx.movement.newTilePath(targetTile).traverse();
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
        if (ctx.players.local().ctx.movement.destination() != targetTile) ctx.movement.newTilePath(targetTile).traverse();
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
        Component lookNorth = ctx.widgets.component(161, 24);
        lookNorth.click();
        ctx.camera.pitch(99);
        Condition.sleep(1000);
    }
    public void clearChatMenus(){
        Condition.sleep(250);
        Component chatItem = ctx.widgets.component(193, 3);
        System.out.println("chatItem valid = "+chatItem.valid()+", says: "+chatItem.text());
        if (chatItem.valid()) {
            chatItem.click();
            Condition.sleep(20);
        }
        else {
            System.out.println("resetChat clicked continue");
            ctx.chat.clickContinue();
            Condition.sleep(20);
        }
//        ctx.input.click(267, 507, 1);
//        Condition.sleep(20);
//        ctx.input.click(267, 534, 1);
//        Condition.sleep(20);
//        ctx.input.click(267, 527, 1);
//        Condition.sleep(20);
    }
    public void waitForInteraction(GameObject object){ //

    }

    public void equipItem(int id, String action){
        Item item = ctx.inventory.select().id(id).poll();
        int itemCount = ctx.inventory.select().id(id).count();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                item.interact(action);
                Condition.sleep(700);
                System.out.println("Contains "+item.name()+": "+ctx.equipment.contains(item)+", number of equipped items = "+ctx.equipment.count());
                return ctx.inventory.select().id(id).count() < itemCount;
            }
        }, 100, 5000);
        Condition.sleep(200);
    }
    public void openInventory(){
        Component inventory = ctx.widgets.component(161, 61);
        inventory.click();
        Condition.sleep(200);
    }
    public void openEquipment(){
        Component equipment = ctx.widgets.component(161, 62);
        equipment.click();
        Condition.sleep(200);
    }
    public void openOptions(){
        Component inventory = ctx.widgets.component(161, 46);
        inventory.click();
        Condition.sleep(200);
    }
    public void openGE(){
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject geBooth = ctx.objects.select().id(10061).nearest().poll();
                Npc camGuy = ctx.npcs.select().id(2149).nearest().poll();
                ctx.camera.turnTo(camGuy);
                ctx.camera.pitch(1);
                Condition.sleep(500);
                if (!ctx.players.local().inMotion()) geBooth.interact("Exchange");
                Condition.sleep(500);
                Component geMain = ctx.widgets.component(465, 2).component(1);
                return geMain.valid();
            }
        }, 500, 500);
    }
    public void sellGE(int id, int amount, int price){ //Use 0 to sell all
        Item sellItem = ctx.inventory.select().id(id).poll();
        sellItem.click();
        Condition.sleep(500);
        if (amount < ctx.inventory.select().id(id).count() && amount != 0) {
            Component itemQuantity = ctx.widgets.component(465, 24).component(49);
            itemQuantity.click();
            Condition.sleep(500);
            ctx.input.sendln(String.valueOf(amount));
            Condition.sleep(500);
        }
        Component itemPrice = ctx.widgets.component(465, 24).component(52);
        itemPrice.click();
        Condition.sleep(500);
        ctx.input.sendln(String.valueOf(price));
        Condition.sleep(500);
        Component confirm = ctx.widgets.component(465, 24).component(54);
        confirm.click();
        Condition.sleep(500);
    }
    public void closeGE(){
        Component close = ctx.widgets.component(465, 2).component(11);
        close.click();
        Condition.sleep(500);
    }


    public void collectGE(boolean toBank){
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject geBooth = ctx.objects.select().id(10061).nearest().poll();
                if (!ctx.players.local().inMotion()) geBooth.interact("Collect");
                Npc camGuy = ctx.npcs.select().id(2149).nearest().poll();
                ctx.camera.turnTo(camGuy);
                ctx.camera.pitch(1);
                Condition.sleep(500);
                Component geCollectionBox = ctx.widgets.component(402, 2).component(1);
                return geCollectionBox.valid();
            }
        }, 500, 500);
        Component bank = ctx.widgets.component(402, 4).component(1);
        Component inventory = ctx.widgets.component(402, 3).component(1);
        if (toBank) bank.click();
        else inventory.click();
        Condition.sleep(500);
        Component close = ctx.widgets.component(402, 2).component(11);
        close.click();
        Condition.sleep(500);
    }
}
