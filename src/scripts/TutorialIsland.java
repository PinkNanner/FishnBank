package scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="TutorialIsland", description = "", properties = "author=PinkNanner; topic=999; client=4;")
public class TutorialIsland extends PollingScript<ClientContext> {

    public boolean room0 = false, room1 = false, room2 = false, room3 = false, room4 = false, room5 = false, room6 = false, room7 = false, room8 = false;
    public Random r = new Random();

    @Override
    public void start() {
        System.out.println("Started");
//        room0 = true;
//        room1 = true;
//        room2 = true;
//        room3 = true;
//        room4 = true;
//        room5 = true;
//        room6 = true;
//        room7 = true;
    }
    @Override
    public void poll() {
        System.out.println("Polling");
        if (!room0){
            rsGuide();
        }
        else if (!room1) {
            fishingGuide();
        }
        else if (!room2) {
            cookingGuide();
        }
        else if (!room3) {
            questGuide();
        }
        else if (!room4) {
            miningGuide();
        }
        else if (!room5) {
            combatGuide();
        }
        else if (!room6) {
            wealthGuide();
        }
        else if (!room7) {
            prayerGuide();
        }
        else if (!room8) {
            magicGuide();
        }
    }
    public void rsGuide(){
        System.out.println("RUNNING ROOM 0");
        resetCamera();
        randomizeCharacter();
        Npc rsGuide = ctx.npcs.select().id(3308).nearest().poll();
        while (ctx.players.local().inMotion() == false && ctx.chat.chatting() == false) rsGuide.interact("Talk-to");
        continueChat(6);
        clearMenus();
        ctx.chat.continueChat("I am an experienced player.");
        continueChat(2);
        clearMenus();
        Condition.sleep(500);

        System.out.println("RUNNING MENU");
        openOptions();
        Component audio0 = ctx.widgets.component(261, 1).component(3);
        audio0.click();
        Condition.sleep(200);
        Component audio1 = ctx.widgets.component(261, 27);
        audio1.click();
        Condition.sleep(200);
        Component audio2 = ctx.widgets.component(261, 33);
        audio2.click();
        Condition.sleep(200);
        Component audio3 = ctx.widgets.component(261, 39);
        audio3.click();
        Condition.sleep(200);
        travelTo(3096, 3107, 200);

        newDoor(9398, 3098, 3107);
        System.out.println("ROOM 0 COMPLETE");
        room0 = true;
        Condition.sleep(1000);
        fishingGuide();
    }
    public void randomizeCharacter(){
        Widget Character_Customization = ctx.widgets.widget(269);
        Component Change_Head = ctx.widgets.component(269, 113);
        Component Change_Jaw = ctx.widgets.component(269, 114);
        Component Change_Torso = ctx.widgets.component(269, 115);
        Component Change_Arms = ctx.widgets.component(269, 116);
        Component Change_Hands = ctx.widgets.component(269, 117);
        Component Change_Legs = ctx.widgets.component(269, 118);
        Component Change_Feet = ctx.widgets.component(269, 119);

        Component Change_Hair_Colour = ctx.widgets.component(269, 121);
        Component Change_Legs_Colour = ctx.widgets.component(269, 122);
        Component Change_Torso_Colour = ctx.widgets.component(269, 127);
        Component Change_Feet_Colour = ctx.widgets.component(269, 130);
        Component Change_Skin_Colour = ctx.widgets.component(269, 131);

        Component Female_Gender_Button = ctx.widgets.component(269, 139);
        Component Welcome_To_Runescape = ctx.widgets.component(269, 97);
        Component Accept_Character = ctx.widgets.component(269, 100);

        while(Welcome_To_Runescape.visible()) {
            Random r = new Random();
            int sleepFor;

            int growBreasts = r.nextInt(2);
            if (growBreasts == 1) Female_Gender_Button.click();
            //Condition.sleep();

            int cycleHead = r.nextInt(24);  //24 hairstyles
            for(int i = 0; i < cycleHead; i++) {
                Change_Head.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleJaw = r.nextInt(15);  //15 beards
            for(int i = 0; i < cycleJaw; i++) {
                Change_Jaw.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleTorso = r.nextInt(14);  //14 torsos
            for(int i = 0; i < cycleTorso; i++) {
                Change_Torso.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleLegs = r.nextInt(11);  //11 legs
            for(int i = 0; i < cycleLegs; i++) {
                Change_Legs.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleArms= r.nextInt(12);  //12 arms
            for(int i = 0; i < cycleArms; i++) {
                Change_Arms.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleHands = r.nextInt(2);  //2 hand sets
            for(int i = 0; i < cycleHands; i++) {
                Change_Hands.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleFeet = r.nextInt(2);  //2 feet
            for(int i = 0; i < cycleFeet; i++) {
                Change_Feet.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleHairColour = r.nextInt(25);  //25 colours
            for(int i = 0; i < cycleHairColour; i++) {
                Change_Hair_Colour.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleLegsColour = r.nextInt(15);  //15 colours
            for(int i = 0; i < cycleLegsColour; i++) {
                Change_Legs_Colour.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleTorsoColour = r.nextInt(14);  //14 colours
            for(int i = 0; i < cycleTorsoColour; i++) {
                Change_Torso_Colour.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleFeetColour = r.nextInt(3);  //3 colours
            for(int i = 0; i < cycleFeetColour; i++) {
                Change_Feet_Colour.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            int cycleSkinColour = r.nextInt(8);  //8 colours
            for(int i = 0; i < cycleSkinColour; i++) {
                Change_Skin_Colour.click();
                sleepFor = r.nextInt(100) + 50;
                Condition.sleep(sleepFor);
            }

            Accept_Character.click();
            Condition.sleep(1000);
        }
    }


    public void fishingGuide(){
        System.out.println("RUNNING ROOM 1");
        resetCamera();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        }, 200, 500);
        travelTo(3104, 3094, 500); //RUN TO TRAINING AREA
        newNpc(3306);
        continueChat(3, 100);
        clearMenus();
        Condition.sleep(100);
        openInventory();
        Condition.sleep(200);
        makeFire();
        Component stats = ctx.widgets.component(161, 59);
        stats.click();
        Condition.sleep(200);
        newNpc(3306);
        openInventory();
        Condition.sleep(200);
        clearMenus();
        ctx.chat.clickContinue();
        Condition.sleep(200);
        travelTo(3101, 3093, 500); //fishing spot
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.input.click(678, 263, 1);//FISH FIRST SHRIMP
                travelTo(3101, 3093, 500); //fishing spot
                Condition.sleep(500);
                Npc fishingSpot = ctx.npcs.select().id(3317).nearest().poll();
                ctx.camera.turnTo(fishingSpot);
                Condition.sleep(500);
                if (ctx.inventory.select().id(2514).count() == 0 && ctx.players.local().animation() == -1) fishingSpot.interact("Net");
                return ctx.inventory.select().id(2514).count() >= 1;
            }
        }, 500, 100);
        resetCamera();
        Condition.wait(new Callable<Boolean>() {    //COOK FIRST SHRIMPS
            @Override
            public Boolean call() throws Exception {
                if (ctx.players.local().animation() == -1) {
                    clearMenus();
                    GameObject fire0 = ctx.objects.select().id(26185).nearest().poll();
                    if (fire0.valid() == false) makeFire();
                    Item shrimp0 = ctx.inventory.select().id(2514).poll();
                    shrimp0.interact("Use");
                    Condition.sleep(200);
                    fire0.interact("Use");
                    Condition.sleep(200);
                    ctx.chat.clickContinue();
                    Condition.sleep(2000);
//                    ctx.movement.newTilePath(new Tile(r.nextInt(3) + 3100, r.nextInt(3) + 3093)).traverse();
                    travelTo(fire0.tile().x(), fire0.tile().y(), 200);
                    Condition.sleep(2000);
                }
                if (ctx.inventory.select().id(7954).count() > 0) return true;
                if (ctx.inventory.select().id(2514).count() < 1) return true;
                return ctx.inventory.select().id(2514).count() == 0;
            }
        });
        travelTo(3101, 3093, 100);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {                         //FISH SECOND SHRIMP
                Npc fishingSpot = ctx.npcs.select().id(3317).nearest().poll();
                ctx.camera.turnTo(fishingSpot);
                if (ctx.inventory.select().id(2514).count() == 0) fishingSpot.interact("Net");
                return ctx.inventory.select().id(2514).count() >= 1;
            }
        }, 500, 100);
        resetCamera();
        Condition.wait(new Callable<Boolean>() {    //COOK SECOND SHRIMPS
            @Override
            public Boolean call() throws Exception {
                ctx.input.click(479, 211, 1);
                GameObject fire0 = ctx.objects.select().id(26185).nearest().poll();
                if (fire0.valid() == false) makeFire();
                Item shrimp0 = ctx.inventory.select().id(2514).poll();
                shrimp0.interact("Use");
                Condition.sleep(200);
//                ctx.input.click(fire0.tile().x(), fire0.tile().y(), 1);
                fire0.interact("Use");
//                fire0.click();
                Condition.sleep(200);
                Condition.sleep(2000);
                ctx.chat.clickContinue();
                travelTo(fire0.tile().x(), fire0.tile().y(), 200);
                Condition.sleep(2000);
//                ctx.movement.newTilePath(new Tile(r.nextInt(3)+3100, r.nextInt(3)+3093)).traverse();
                return ctx.inventory.select().id(2514).count() == 0;
            }
        }, 500, 100);
        Condition.sleep(1200);
        ctx.movement.newTilePath(new Tile(3090, 3092)).traverse(); // EXIT FENCE
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject gate = ctx.objects.select().id(9470).nearest().poll();
                ctx.camera.turnTo(gate);
                ctx.camera.pitch(1);
                if (!ctx.players.local().inMotion()) gate.interact("Open");
                if (ctx.players.local().tile().equals(new Tile(3089, 3091))) return true;
                if (ctx.players.local().tile().equals(new Tile(3089, 3092))) return true;
                return ctx.players.local().tile().equals(new Tile(3089, 3092));
            }
        }, 500, 200);
        System.out.println("ROOM 1 COMPLETE");
        room1 = true;
        Condition.sleep(2000);
        cookingGuide();
    }
    public void makeFire(){
        final GameObject tree = ctx.objects.select().id(9730).nearest().poll();         //CUT TREE
        tree.interact("Chop");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Condition.sleep(2000);
                ctx.input.click(683, 262, 1);
                Condition.sleep(200);
                if (ctx.players.local().animation() == -1 && ctx.inventory.select().id(2511).count() == 0) tree.interact("Chop");
                return ctx.players.local().animation() == -1 && ctx.inventory.select().id(2511).count() >= 1;
            }
        }, 500, 500);
        clearMenus();
        Condition.wait(new Callable<Boolean>() {     //LIGHT LOGS
            @Override
            public Boolean call() throws Exception {
                ctx.input.click(260, 528, 1);
                Condition.sleep(200);
                Item tinder = ctx.inventory.select().id(590).poll();
                tinder.interact("Use");
                Condition.sleep(200);
                Item log = ctx.inventory.select().id(2511).poll();
                log.interact("Use");
                Condition.sleep(5000);
                clearMenus();
                ctx.movement.newTilePath(new Tile(r.nextInt(5)+3100, r.nextInt(5)+3093)).traverse();
                return ctx.inventory.select().id(2511).count() == 0;
            }
        }, 500, 30);
    }

    public void cookingGuide(){
        System.out.println("RUNNING ROOM 2");
        resetCamera();
        travelTo(3079, 3084, 500);
        newDoor(9709, 3078, 3084);
        newNpc(3305);
        openInventory();
        Condition.sleep(200);
        ctx.chat.clickContinue();
        continueChat(6);
        clearMenus();
        Item flour = ctx.inventory.select().id(2516).poll();
        Condition.sleep(200);
        Item water = ctx.inventory.select().id(1929).poll();
        Condition.sleep(200);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.chat.clickContinue();
                clearMenus();
                Condition.sleep(200);
                flour.interact("Use");
                Condition.sleep(200);
                water.interact("Use");
                Condition.sleep(200);
                return ctx.inventory.select().id(2307).count() > 0;
            }
        }, 200, 200);
        travelTo(3075, 3082, 100);
        cookBread();
        Component music = ctx.widgets.component(161, 48);
        music.click();
        Condition.sleep(200);
        openInventory();
        travelTo(3073, 3090, 200);
        Condition.sleep(200);
        newDoor(9710, 3072, 3090);
        System.out.println("ROOM 2 COMPLETE");
        room2 = true;
        Condition.sleep(5000);
        questGuide();

    }
    public void cookBread(){
        Condition.wait(new Callable<Boolean>() { // COOK BREAD
            @Override
            public Boolean call() throws Exception {
                GameObject oven = ctx.objects.select().id(9736).nearest().poll();
                Item dough = ctx.inventory.select().id(2307).poll();
                if (ctx.players.local().animation() == -1) {
                    dough.interact("Use");
                    Condition.sleep(200);
                    oven.interact("Use");
                    Condition.sleep(3000);
                    travelTo(3075, 3082, 100);
                    ctx.camera.turnTo(oven);
                }
                resetCamera();
                if (ctx.inventory.select().id(2309).count() > 0)
                if (ctx.inventory.select().id(2311).count() > 0) return true;
                return ctx.inventory.select().id(2309).count() > 0;
            }
        }, 500, 100);
        clearMenus();
    }

    public void questGuide(){
        System.out.println("STARTING ROOM 3");
        resetCamera();
        travelTo(3072, 3090, 100);
        Condition.sleep(1000);
        Component emote = ctx.widgets.component(161, 47);
        emote.click();
        Condition.sleep(900);
        Component emoteSelect = ctx.widgets.component(261, 33);
        emoteSelect.click();
        Condition.sleep(200);
        openOptions();
        Condition.sleep(1000);
        Component run = ctx.widgets.component(261, 78);
        run.click();
        Condition.sleep(200);
        travelTo(3076, 3106, 500);
        travelTo(3076, 3121, 500);
        travelTo(3086, 3126, 500); //TODO: Check if prospecting worked via chat windows, did not equip shield properly twice, did not finish talking to financial adviser once, did not open prayer menu once
        Condition.sleep(1200);
        newDoor(9716, 3086, 3125);
        resetCamera();
        travelTo(3086, 3120, 100);
        newNpc(3312);
        continueChat(1);
        Condition.sleep(800);
        Component quest = ctx.widgets.component(161, 60);
        quest.click();
        Condition.sleep(200);
        newNpc(3312);
        continueChat(5);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                travelTo(3088, 3120, 100);
                GameObject ladder = ctx.objects.select().id(9726).nearest().poll();
                ctx.camera.turnTo(ladder);
                Condition.sleep(500);
                ladder.interact("Climb-down");
                Condition.sleep(500);
                return ctx.players.local().tile().equals(new Tile(3088, 9520));
            }
        }, 500, 100);
        System.out.println("FINISHED ROOM 3");
        room3 = true;
        miningGuide();
    }

    public void miningGuide(){
        resetCamera();
        openInventory();
        travelTo(3080, 9507, 500);
        travelTo(3081, 9501, 100);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Npc miningGuide = ctx.npcs.select().id(3311).nearest().poll();
                miningGuide.interact("Talk-to");
                Condition.sleep(1200);
                if (!miningGuide.valid()) travelTo(3078+r.nextInt(6), 9500+r.nextInt(7), 100);
                return ctx.chat.canContinue();
            }
        }, 500, 100);
        continueChat(3);
        travelTo(3078, 9504, 100);
        resetCamera();
        prospectOre(10080, "tin");
        travelTo(3084, 9503, 100);
        prospectOre(10079, "copper");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Npc miningGuide = ctx.npcs.select().id(3311).nearest().poll();
                miningGuide.interact("Talk-to");
                Condition.sleep(1200);
                if (!miningGuide.valid()) travelTo(3078+r.nextInt(6), 9500+r.nextInt(7), 100);
                return ctx.chat.canContinue();
            }
        }, 500, 100);
        continueChat(3);
        clearMenus();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject tin = ctx.objects.select().id(10080).nearest().poll();
                travelTo(tin.tile().x()+1, tin.tile().y(), 100);
                Condition.sleep(1200);
                if (ctx.players.local().animation() == -1){
                    tin.interact("Mine");
                    System.out.println("Clicking tin, tin valid = "+tin.valid());
                }
                Condition.sleep(1200);
                return ctx.inventory.select().id(438).count() > 0;
            }
        }, 500, 100);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject copper = ctx.objects.select().id(10079).nearest().poll();
                travelTo(copper.tile().x(), copper.tile().y()+1, 100);
                Condition.sleep(1200);
                if (ctx.players.local().animation() == -1){
                    copper.interact("Mine");
                    System.out.println("Clicking copper, copper valid = "+copper.valid());
                }
                Condition.sleep(1200);
                return ctx.inventory.select().id(436).count() > 0;
            }
        }, 500, 100);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                travelTo(3079, 9497, 500);
                GameObject furnace = ctx.objects.select().id(10082).nearest().poll();
                Item copperOre = ctx.inventory.select().id(436).poll();
                copperOre.interact("Use");
                Condition.sleep(750);
                furnace.interact("Use");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == -1;
                    }
                }, 500, 100);
                return ctx.inventory.select().id(2349).count() > 0;
            }
        }, 500, 100);
        System.out.println("Has bronze bar");
        clearMenus();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Npc miningGuide = ctx.npcs.select().id(3311).nearest().poll();
                miningGuide.interact("Talk-to");
                Condition.sleep(1200);
                if (!ctx.players.local().inMotion() && !ctx.chat.canContinue()) travelTo(3078+r.nextInt(6), 9500+r.nextInt(7), 100);
                return ctx.chat.canContinue();
            }
        }, 500, 100);
        continueChat(3);
        clearMenus();
        travelTo(3083, 9498, 500);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject anvil = ctx.objects.select().id(2097).nearest().poll();
                anvil.interact("Smith");
                Condition.sleep(1200);
                Component smith = ctx.widgets.component(312, 2).component(2);
                smith.click();
                Condition.sleep(600);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == -1;
                    }
                }, 100, 100);
                return ctx.inventory.select().id(1205).count() > 0;
            }
        },500, 100);
        travelTo(3094, 9502, 100);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject gate = ctx.objects.select().id(9717).nearest().poll();
//                ctx.camera.turnTo(gate);
                ctx.camera.angle(275);
                Condition.sleep(700);
                ctx.camera.pitch(0);
                Condition.sleep(700);
                gate.interact("Open");
                Condition.sleep(700);
                if (ctx.players.local().tile().equals(new Tile(3095, 9503))) return true;
                return ctx.players.local().tile().equals(new Tile(3095, 9502));
            }
        }, 500, 100);
        System.out.println("FINISHED ROOM 4");
        room4 = true;
        resetCamera();
        combatGuide();
    }
    public void prospectOre(int id, String type){
        GameObject rock = ctx.objects.select().id(id).nearest().poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Component chatBox = ctx.widgets.component(229, 1);
                rock.interact("Prospect");
                Condition.sleep(5000);
                return chatBox.text().endsWith("rock contains "+type+".");
            }
        }, 500, 10000);

    }

    public void combatGuide(){
        resetCamera();
        travelTo(3107, 9508, 500);
        newNpc(3307);
        continueChat(3);
        Condition.sleep(200);
        Component equipment = ctx.widgets.component(161, 62);
        equipment.click();
        Condition.sleep(200);
        Component equiped = ctx.widgets.component(387, 18);
        equiped.click();
        Condition.sleep(700);
        equipItem(1205); //Dagger
        travelTo(3103, 9505, 100);
        Condition.sleep(200);
        openInventory();
        newNpc(3307);
        continueChat(1);
        clearMenus();
        equipItem(1277); //Sword
        equipItem(1171); //Shield
        Condition.sleep(1250);
        Component combat = ctx.widgets.component(161, 58);
        combat.click();
        Condition.sleep(250);
        openInventory();
        Condition.sleep(250);
        travelTo(3111, 9518, 200);
        Condition.sleep(250);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.camera.angle(86);
                Condition.sleep(750);
                ctx.camera.pitch(43);
                Condition.sleep(750);
                GameObject gate = ctx.objects.select().id(9719).nearest().poll();
                gate.interact("Open");
                if (ctx.players.local().tile().equals(new Tile(3110, 9518))) return true;
                return ctx.players.local().tile().equals(new Tile(3110, 9519));
            }
        }, 500, 100);
        resetCamera();
        attackRat(); //waits until player out of combat (rat is dead)
        clearMenus();
        Condition.sleep(1000);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.camera.angle(274);
                Condition.sleep(750);
                ctx.camera.pitch(41);
                Condition.sleep(750);
                GameObject gate = ctx.objects.select().id(9719).nearest().poll();
                gate.interact("Open");
                if (ctx.players.local().tile().equals(new Tile(3110, 9519))) return true;
                return ctx.players.local().tile().equals(new Tile(3111, 9518));
            }
        }, 500, 100);
        resetCamera();
        Condition.sleep(1550);
        travelTo(3105, 9505, 200);
        newNpc(3307);
        continueChat(4);
        equipItem(841); //Bow and arrows
        equipItem(882);
        travelTo(3107, 9512, 100);
        attackRat();
        travelTo(3111, 9525, 500);

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject ladder = ctx.objects.select().id(9727).nearest().poll();
                ctx.camera.pitch(73);
                Condition.sleep(750);
                ctx.camera.turnTo(ladder);
                Condition.sleep(750);
                ladder.interact("Climb-up");
                Condition.sleep(1500);
                return ctx.players.local().tile().equals(new Tile(3111, 3125));
            }
        }, 500, 100);
        System.out.println("ROOM 5 COMPLETE");
        room5 = true;
        wealthGuide();
    }

    public void equipItem(int id){
        Item item = ctx.inventory.select().id(id).poll();
        int itemCount = ctx.inventory.select().id(id).count();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                item.interact("Equip");
                item.interact("Wield");
                Condition.sleep(700);
                System.out.println("Contains "+item.name()+": "+ctx.equipment.contains(item)+", number of equipped items = "+ctx.equipment.count());
                return ctx.inventory.select().id(id).count() < itemCount;
            }
        }, 100, 5000);
    }

    public void attackRat(){
        final boolean[] ratDead = {false};
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (!ratDead[0] && !ctx.players.local().inCombat()) {
                    System.out.println("RAT DEAD = FALSE");
                    Npc rat = ctx.npcs.select().id(3313).select(new Filter<Npc>() {
                        @Override
                        public boolean accept(Npc npc) {
                            return !npc.inCombat();
                        }
                    }).nearest().poll();
                    rat.interact("Attack");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (rat.inCombat()){
                                System.out.println("RETURNING RAT IN COMBAT");
                                return true;
                            }
                            return ctx.players.local().inCombat();
                        }
                    }, 100, 100);
//                Condition.sleep(200);
                if (rat.inCombat() || ctx.players.local().inCombat()){
                    Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                if (ctx.combat.health() < 5) ctx.inventory.select().id(2309).poll().interact("Eat"); //doesnt work
                                System.out.println("rat healthPercent = " + rat.healthPercent());
                                System.out.println("RAT COMBAT = " + rat.inCombat() + ", RAT VALID = " + rat.valid() + ", RAT ANIMATION = " + rat.animation());

                                if (rat.healthPercent() <= 0 && ctx.players.local().inCombat() == false) { //RAT STAYS IN COMBAT WHILE DEAD
                                    ratDead[0] = true;
                                    System.out.println("RAT DEAD = TRUE, health percent = " + rat.healthPercent());
                                    System.out.println("RAT COMBAT = " + rat.inCombat() + ", RAT VALID = " + rat.valid() + ", RAT ANIMATION = " + rat.animation());
                                }
                                return ratDead[0];
                            }
                        }, 500, 1000);
                    }
                    Condition.sleep(1200);
                }
                return ratDead[0];
            }
        }, 100, 100);
    }

    public void wealthGuide(){
        resetCamera();
        travelTo(3122, 3118, 500);
        GameObject door = ctx.objects.select().id(1513).nearest().poll();
        door.interact("Open");
        Condition.sleep(500);
        travelTo(3122, 3123, 500);
        GameObject bank = ctx.objects.select().id(10083).nearest().poll();
        bank.interact("Use");
        Condition.sleep(700);
        continueChat(1);
        Condition.sleep(200);
        ctx.chat.continueChat("Yes.");
        Condition.sleep(1500);
        ctx.bank.close();
        GameObject poll = ctx.objects.select().id(26801).nearest().poll();
        ctx.camera.turnTo(poll);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Condition.sleep(500);
                poll.interact("Use");
                Condition.sleep(500);
                return ctx.chat.canContinue();
            }
        });
        continueChat(2);
        clearMenus();
        travelTo(3124, 3124, 100);
        newDoor(9721, 3125, 3124);
        newNpc(3310);
        continueChat(8);
        newDoor(9722, 3130, 3124);
        System.out.println("ROOM 6 COMPLETE");
        room6 = true;
        prayerGuide();
    }

    public void prayerGuide(){
        resetCamera();
        travelTo(3131, 3114, 500);
        travelTo(3129, 3107, 500);
        GameObject door = ctx.objects.select().id(1525).nearest().poll();
        door.interact("Open");
        Condition.sleep(500);
        travelTo(3126, 3104, 100);
        newNpc(3319);
        continueChat(2);
        Condition.sleep(200);
        Component pray = ctx.widgets.component(161, 63);
        pray.click();
        Condition.sleep(200);
        newNpc(3319);
        continueChat(4);
        Condition.sleep(800);
        Component friends = ctx.widgets.component(161, 44);
        friends.click();
        Condition.sleep(800);
        Component enemies = ctx.widgets.component(161, 43);
        enemies.click();
        newNpc(3319);
        continueChat(7);
        clearMenus();
        travelTo(3122, 3103, 500);
        newDoor(9723, 3122, 3102);
        System.out.println("ROOM 7 COMPLETE");
        room7 = true;
        magicGuide();
    }
    public void magicGuide(){
        resetCamera();
        openInventory();
        travelTo(3126, 3087, 500);
        travelTo(3141, 3085, 500);
        newNpc(3309);
        continueChat(2);
        Component spellBook = ctx.widgets.component(161, 64);
        spellBook.click();
        Condition.sleep(200);
        newNpc(3309);
        continueChat(1);
        clearMenus();
        travelTo(3138, 3091, 100);
        castWindStrike();
        travelTo(3141, 3085, 100);
        newNpc(3309);
        continueChat(1);
        ctx.chat.continueChat("Yes.");
        continueChat(1);
        ctx.chat.continueChat("No, I'm not planning to do that.");
        continueChat(3);
        clearMenus();
        continueChat(2);
        System.out.println("TUTORIAL ISLAND COMPLETE");
        room8 = true;
        stop();
    }
    public void castWindStrike(){
        Component windStrike = ctx.widgets.component(218, 2);
        windStrike.click();
        Condition.sleep(500);
        Npc chicken = ctx.npcs.select().id(3316).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();
        chicken.interact("Cast");
        Condition.sleep(1200);
    }

    public void newNpc(int id){
        Npc guy = ctx.npcs.select().id(id).nearest().poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
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
                if (!ctx.players.local().inMotion()) door.interact("Open");
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
    public void travelTo(int x, int y, int sleep){
        System.out.println("Travelling to: "+x+", "+y);
        ctx.movement.newTilePath(new Tile(x, y)).traverse();
        Condition.sleep(500);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().tile().equals(new Tile(x, y));
            }
        }, sleep, 100);
        ctx.input.click(267, 507, 1);
    }
//    public void travelTo(int x, int y, int sleep, int doorId){
//        System.out.println("Travelling to: "+x+", "+y);
//        ctx.movement.newTilePath(new Tile(x, y)).traverse();
//        Condition.wait(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                Condition.sleep(1000);
//                if (checkDoor(doorId) == true) newDoor(doorId);
//                ctx.movement.newTilePath(new Tile(x, y)).traverse();
//                return ctx.players.local().tile().equals(new Tile(x, y));
//            }
//        }, sleep, 10000);
//    }
    public boolean checkDoor(int doorId){
        GameObject door = ctx.objects.select().id(doorId).nearest().poll();
        if (ctx.players.local().tile().distanceTo(door.tile()) > 3) return false;
        return door.valid();
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
    @Override
    public void stop(){
        System.out.println("Tutorial island completed, stopping script.");
    }
}
