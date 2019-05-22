package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="Pirates Treasure & Romeo & Juliet", description = "Start in Draynor village or at Redbeard Frank with 60 coins", properties = "author=PinkNanner; topic=999; client=4;")
public class PiratesTreasure extends PollingScript<ClientContext> {
    static Tile[] pathToPirateFromDraynor, pathToKaramja, pathToRum, pathToNanners, pathToCrate, pathToPirateFromShop, pathToFaladorFromSarim, pathToTreasure, pathToVarrockFromFally, pathToVarrokBerriesFromLummy, pathToVarrockFromBerries, pathToJulietFromRomeo;
    static Tile[] pathToLawrenceFromSquare, pathToApothecaryFromSquare, pathToJulietFromApothecary, pathToInnFromSquare, pathToFallyFromInn, pathToGrandExchangeFromSquare;
    int[] BANANA_TREE;
    boolean atRedbeardFrank = false, inLumbridge = false, doingRomeo = false, hasBerries = false, atBerries = false, atGrandExchange = false, treasureQuestComplete = false;
    Random r = new Random();

    @Override
    public void start() {
        System.out.println("Started");
        ctx.properties.setProperty("randomevents.disable", "true");
        atRedbeardFrank = true; //Remove later
        BANANA_TREE = new int[]{2073, 2074, 2075, 2076, 2077};
        pathToPirateFromDraynor = new Tile[]{new Tile(3087, 3247), new Tile(3081, 3258), new Tile(3074, 3269), new Tile(3072, 3277), new Tile(3063, 3265), new Tile(3056, 3254), new Tile(3051, 3247)};
        pathToKaramja = new Tile[]{new Tile(3042, 3241), new Tile(3034, 3235), new Tile(3028, 3223), new Tile(3028, 3217)};
        pathToRum = new Tile[]{new Tile(2944, 3146), new Tile(2932, 3149), new Tile(2928, 3143)};
        pathToNanners = new Tile[]{new Tile(2920, 3152), new Tile(2918, 3158)};
        pathToCrate = new Tile[]{new Tile(2916, 3160), new Tile(2924, 3151), new Tile(2934, 3149), new Tile(2939, 3152)};
        pathToPirateFromShop = new Tile[]{new Tile(3018, 3212), new Tile(3027, 3221), new Tile(3028, 3229), new Tile(3038, 3235), new Tile(3047, 3245), new Tile(3053, 3246)};
        pathToFaladorFromSarim = new Tile[]{new Tile(3040, 3253), new Tile(3033, 3263), new Tile(3022, 3271), new Tile(3011, 3279), new Tile(3007, 3292), new Tile(3006, 3304), new Tile(3007, 3317),
                new Tile(3007, 3330), new Tile(3006, 3344), new Tile(3000, 3357), new Tile(2994, 3367), new Tile(2982, 3371)};
        pathToTreasure = new Tile[]{new Tile(2994, 3372), new Tile(2999, 3383)};

        pathToVarrockFromFally = new Tile[]{new Tile(3040, 3253)}; //TODO THIS

        pathToVarrokBerriesFromLummy = new Tile[]{new Tile(3236, 3220), new Tile(3250+r.nextInt(2), 3225+r.nextInt(1)), new Tile(3259+r.nextInt(1), 3228+r.nextInt(1)),
                new Tile(3259, 3241), new Tile(3251+r.nextInt(1), 3251+r.nextInt(2)), new Tile(3249+r.nextInt(1), 3264+r.nextInt(2)),
                new Tile(3242+r.nextInt(2), 3275+r.nextInt(1)), new Tile(3238+r.nextInt(2), 3289+r.nextInt(1)), new Tile(3237+r.nextInt(1), 3304+r.nextInt(1)),
                new Tile(3247+r.nextInt(2), 3315+r.nextInt(2)), new Tile(3254, 3324+r.nextInt(2)), new Tile(3267, 3330), new Tile(3276, 3342+r.nextInt(1)),
                new Tile(3274, 3356), new Tile(3272, 3367)};
        pathToVarrockFromBerries = new Tile[]{new Tile(3280+r.nextInt(2), 3371+r.nextInt(2)), new Tile(3290+r.nextInt(1), 3381+r.nextInt(1)),
                new Tile(3289+r.nextInt(3), 3395+r.nextInt(1)), new Tile(3288+r.nextInt(1), 3409+r.nextInt(2)), new Tile(3280+r.nextInt(1), 3418+r.nextInt(1)),
                new Tile(3270+r.nextInt(1), 3428+r.nextInt(1)), new Tile(3258+r.nextInt(1), 3428+r.nextInt(1)), new Tile(3243, 3429),
                new Tile(3227+r.nextInt(1), 3428+r.nextInt(2)), new Tile(3216+r.nextInt(1), 3426+r.nextInt(1)), new Tile(3210+r.nextInt(2), 3422+r.nextInt(2))};
        pathToJulietFromRomeo = new Tile[]{new Tile(3212, 3422), new Tile(3206, 3427), new Tile(3193, 3430), new Tile(3179, 3430), new Tile(3167, 3433)};
        pathToLawrenceFromSquare = new Tile[]{new Tile(3211, 3424), new Tile(3217, 3426), new Tile(3227, 3429), new Tile(3235, 3441), new Tile(3245, 3446), new Tile(3245, 3461), new Tile(3250, 3467), new Tile(3255, 3473), new Tile(3255, 3482)};
        pathToApothecaryFromSquare = new Tile[]{new Tile(3209, 3421), new Tile(3199, 3409), new Tile(3191, 3403)};
        pathToJulietFromApothecary = new Tile[]{new Tile(3184, 3413), new Tile(3180, 3423), new Tile(3168, 3432), new Tile(3165, 3433)};
        pathToInnFromSquare = new Tile[]{new Tile(3211, 3418), new Tile(3211, 3407), new Tile(3215, 3395)};
        pathToFallyFromInn = new Tile[]{new Tile(3211, 3380), new Tile(3198, 3373), new Tile(3186, 3376), new Tile(3177, 3385), new Tile(3169, 3394), new Tile(3154, 3395), new Tile(3141, 3400), new Tile(3129, 3407),
                new Tile(3117, 3414), new Tile(3107, 3421), new Tile(3094, 3418), new Tile(3081, 3416), new Tile(3068, 3411), new Tile(3060, 3402), new Tile(3050, 3393), new Tile(3038, 3391),
                new Tile(3024, 3392), new Tile(3012, 3394), new Tile(2998, 3395), new Tile(2984, 3396), new Tile(2973, 3397), new Tile(2968, 3386), new Tile(2976, 3378), new Tile(2983, 3376)};
        pathToGrandExchangeFromSquare = new Tile[]{new Tile(3213, 3423), new Tile(3204, 3429), new Tile(3196, 3443), new Tile(3184, 3453), new Tile(3176, 3459), new Tile(3166, 3465), new Tile(3165, 3474),
                new Tile(3164, 3486)};
    }
    @Override
    public void poll() {
        resetCamera();
//        romeoQuest();
//        stop();
        if(treasureQuestComplete == false) {
            startQuest(); //TODO check quest log for which stage of quest player is on
        }
    }
    public void startQuest() {
        System.out.println("Starting pirates treasure quest");
//        if (!atRedbeardFrank) travelTo(pathToPirateFromDraynor);
//        newNpc(3643);
//        continueChat(1);
//        continueChat("I'm in search of treasure.");
//        continueChat(5);
//        continueChat("Ok, I will bring you some rum");
//        continueChat(6);
        travelTo(pathToKaramja);
        newNpc(3644);
        continueChat(2);
        continueChat("Yes please.");
        continueChat(2);
        newDoor(2082, 2956, 3146, "Cross");
        smuggleRum();
    }
    public void smuggleRum() {
        System.out.println("Starting smuggle rum segment");
        openInventory();
        travelTo(pathToRum);
        newNpc(1037);
        continueChat(1);
        continueChat("Yes please.");
        Condition.sleep(500);
        Component shop = ctx.widgets.component(300, 16).component(2);
        shop.interact("Buy 1");
        travelTo(pathToNanners);
        pickNanners();
        travelTo(pathToCrate);
        newNpc(3647);
        continueChat(1);
        continueChat("Could you offer me employment on your plantation?");
        continueChat(4);
        travelTo(2943, 3150, 100);
        GameObject crate = ctx.objects.select().nearest().id(2072).poll();
        crate.interact("Fill");
        Condition.sleep(1000);
        clearChatMenus();
        Item rum = ctx.inventory.select().id(431).poll();
        rum.interact("Use");
        Condition.sleep(500);
        crate.interact("Use");
        Condition.sleep(1000);
        travelTo(2939, 3152, 100);
        newNpc(3647);
        continueChat(2);
        travelTo(2938, 3147, 500);
        travelTo(2945, 3146, 500);
        travelTo(2953, 3146, 500);
        newNpc(3648);
        continueChat(1);
        continueChat("Can I journey on this ship?");
         continueChat(2);
        continueChat("Search away, I have nothing to hide.");
        continueChat(2);
        continueChat("Ok.");
        continueChat(2);
        newDoor(2084, 3029, 3217, "Cross");
        retrieveRum();
    }
    public void retrieveRum() {
        System.out.println("Starting retrieve rum segment");
        resetCamera();
        travelTo(3014, 3219, 500);
        travelTo(3016, 3228, 500, 1535);
        GroundItem shelves = ctx.groundItems.select().id(7957).nearest().poll();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception { //CANNOT Take another white apron if already carrying or wearing one
                shelves.interact("Take");
                Condition.sleep(1900);
                return ctx.inventory.select().id(1005).count() > 0;
            }
        }, 500, 100);
        equipItem(1005, "Wear");
        travelTo(3014, 3220, 500);
        travelTo(3014, 3217, 500, 1535);
        travelTo(3015, 3205, 500);
        newNpc(1026);
        continueChat(1);
        continueChat("Can I get a job here?");
        continueChat(4);
        travelTo(3012, 3204, 100);
//        travelTo(3010, 3207, 100, 2069); //TODO: Check X for in-front of door or not
        newDoor(2069, 3011, 3204);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                GameObject rumCrate = ctx.objects.select().id(2071).nearest().poll();
                rumCrate.interact("Search");
                return ctx.inventory.select().id(431).count() > 0;
            }
        }, 500, 100);

        Condition.sleep(500);
        travelTo(3011, 3204, 100);
        newDoor(2069, 3012, 3204);
        travelTo(pathToPirateFromShop);
        newNpc(3643);
        continueChat(6);
        clearChatMenus();
        clearChatMenus();
        continueChat(2);
        clearChatMenus();
        continueChat("Ok thanks, I'll go and get it.");
        continueChat(2);
        travelToInn();
    }

    public void travelToInn() {
        System.out.println("Starting travel to inn segment");
        Tile tempTile = ctx.players.local().tile();
        Component spellBook = ctx.widgets.component(161, 64);
        spellBook.click();
        Condition.sleep(1000);
        Component homeTeleport = ctx.widgets.component(218, 4);
        homeTeleport.click();
        Condition.sleep(500);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        });
        openInventory();
        if (ctx.players.local().tile() == tempTile) inLumbridge = false;
        else
 inLumbridge = true;
        if (inLumbridge == false) {
            travelTo(pathToFaladorFromSarim);
            getSpade();
            travelToReverse(pathToFallyFromInn);
            if (doingRomeo) { //TODO: Collect treasure first
                travelToReverse(pathToInnFromSquare);
                collectLogs(1);
//                romeoQuest();
                travelTo(pathToInnFromSquare);
            }
        } else if (inLumbridge){
            travelTo(pathToVarrokBerriesFromLummy);
            if (ctx.inventory.select().id(753).count() == 0 && doingRomeo) {
                collectBerries();
            }
            travelTo(pathToVarrockFromBerries);
//            collectLogs(1);
            if (doingRomeo) romeoQuest();
            travelTo(pathToInnFromSquare);
        }
        lootInn();
    }
    public void lootInn() {
        travelTo(3217, 3395, 100, 11775);
        travelTo(3226, 3394, 100);
        newDoor(11796 , 3230, 3394, "Climb-up");
        travelTo(3223, 3395, 100);
        travelTo(3219, 3395, 100, 11775);
        Item key = ctx.inventory.select().id(432).poll();
        key.interact("Use");
        Condition.sleep(500);
        GameObject chest = ctx.objects.select().nearest().id(2079).poll();
        chest.interact("Use");
        Condition.sleep(2000);
        travelTo(3222, 3395, 100);
        if (checkDoor(11775) == true){
            newDoor(11775, "Open");
        }
        travelTo(3230, 3394, 100);
        newDoor(11799, 3226, 3394, "Climb-down");
        travelTo(3216, 3395, 100);
        travelTo(3213, 3395, 100, 11775);
        travelTo(pathToFallyFromInn);
        getSpade();
    }
    public void getSpade(){
        System.out.println("Starting get spade segment");
        if (ctx.inventory.select().id(952).count() == 0) {
            travelTo(2982, 3371, 500);
            if (checkDoor(24050) == true){
                newDoor(24050, "Open");
            }
            travelTo(2982, 3368, 500);
            if (checkDoor(24051) == true){
                newDoor(24051, "Close");
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    GroundItem spade = ctx.groundItems.select().id(952).nearest().poll();
                    spade.interact("Take");
                    Condition.sleep(1000);
                    return ctx.inventory.select().id(952).count() > 0;
                }
            });
            travelTo(2984, 3370, 500);
            if (checkDoor(24050) == true){
                newDoor(24050, "Open");
            }
            travelTo(2982, 3375, 500);
        }
        getTreasure();
    }
    public void getTreasure(){
        travelTo(pathToTreasure);
        Item clue = ctx.inventory.select().id(433).poll();
        clue.interact("Read");
        Condition.sleep(1500);
        Component closeClue = ctx.widgets.component(222, 12);
        closeClue.click();
        Condition.sleep(1500);
        Item spade = ctx.inventory.select().id(952).poll();
        spade.interact("Dig");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 100, 1000);
        travelTo(2999, 3369, 100);
        Condition.sleep(10000);
        travelTo(2999, 3383, 100);
        Condition.sleep(500);
        spade.interact("Dig");
        Condition.sleep(3000);
        Component closeQuest = ctx.widgets.component(277, 15);
        closeQuest.click();
        treasureQuestComplete = true;
        travelTo(2994, 3370, 500);
        travelToReverse(pathToFaladorFromSarim);
        travelTo(3039, 3257, 500);
        travelTo(3048, 3249, 500);
//        travelToReverse(pathToPirateFromDraynor);

    }

    public void collectLogs(int cutCount){ //Collect i inventories and sell them
        boolean holdingAxe = false;
        if (!atGrandExchange) {
            travelTo(pathToGrandExchangeFromSquare);
            atGrandExchange = true;
        }

        //Prepare to cut logs
        openInventory();
        if (ctx.inventory.select().id(1351).count() > 0) equipItem(1351, "Wield");
        openEquipment();
        System.out.println("Main hand = "+ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id());
        if (ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id() == 1351) holdingAxe = true;
        Condition.sleep(200);
        openInventory();
        depositInventory(10060);
        if (!holdingAxe) {
            ctx.bank.withdraw(1351, 1);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(1351).count() > 0;
                }
            }, 500, 500);
        }
        ctx.bank.close();
        if (!holdingAxe) equipItem(1351, "Wield");

        //Cut down the logs
        Tile[] pathToTreesFromGE = new Tile[]{new Tile(3167, 3487), new Tile(3164, 3481), new Tile(3164, 3471), new Tile(3161, 3460), new Tile(3154, 3456)};
        travelTo(pathToTreesFromGE);
        for (int i = 0;i<cutCount;i++) {
            while (ctx.inventory.select().count() < 28) cutLogs();
            resetCamera();
            travelTo(3158, 3451, 100);
            travelToReverse(pathToTreesFromGE);
            depositInventory(10060);
            if (i != cutCount-1) {
                travelTo(pathToTreesFromGE);
            }
        }
        depositInventory(10060);

        //Sell the logs
        GameObject bankBooth = ctx.objects.select().nearest().id(10060).poll();
        bankBooth.interact("Bank");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.open();
            }
        }, 500, 1000);
        ctx.bank.withdrawModeNoted(true);
        int logCount = ctx.bank.select().id(1512).count(); //Logs have different id when noted selected in bank
        ctx.bank.withdraw(1511, logCount);
        Condition.sleep(1200);
        ctx.bank.close();
        travelTo(3165, 3487, 100);


        openGE();
        sellGE(1512, logCount, 1); //use 0 to sell all
        Condition.sleep(2500);
        closeGE();
        Condition.sleep(2500);
        collectGE(true);
        resetCamera();


        //Grab the quest items
        bankBooth.interact("Bank");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.open();
            }
        }, 100, 1000);
        ctx.bank.withdraw(753, 1);
        ctx.bank.withdraw(432, 1);
        ctx.bank.close();

        //Travel back to square
        travelToReverse(pathToGrandExchangeFromSquare);

    }
    public void cutLogs(){
        int[] TREE_ID = new int[]{1276, 1278};
        GameObject tree = ctx.objects.select().id(TREE_ID).within(6).nearest().poll();
        if (!tree.valid()){
            resetCamera();
            travelTo(3157, 3452, 100);
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ctx.camera.turnTo(tree);
                ctx.camera.pitch(1);
                Condition.sleep(150);
                if (ctx.players.local().animation() == -1 && ctx.players.local().inMotion() == false) tree.interact("Chop down");
                Condition.sleep(250);
                if (ctx.players.local().animation() == -1 && ctx.players.local().inMotion() == false) return true;
                return false;
            }
        }, 1200, 1000);
    }
    public void depositInventory(int id){
        GameObject bankBooth = ctx.objects.select().id(id).nearest().poll();
        bankBooth.interact("Bank");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.open();
            }
        }, 100, 1000);
        System.out.println("Inventory count = "+ctx.inventory.select().count());
        if (ctx.inventory.select().count() > 0) ctx.bank.depositInventory();
    }

    public void romeoQuest(){
//        System.out.println("Starting romeo quest");
//        findRomeo();
//        a.continueChat(1);
//        a.continueChat("Perhaps I could help to find her for you?");
//        a.continueChat(10);
//        a.continueChat("Yes, ok, I'll let her know.");
//        a.continueChat(5);
//        a.continueChat("Ok, thanks.");
//        a.travelTo(pathToJulietFromRomeo);
        navigateIntoJulietHouse();
        newNpc(5035);
        continueChat(7);
        clearChatMenus();
        navigateOutOfJulietHouse();
        travelToReverse(pathToJulietFromRomeo);
        findRomeo();
        continueChat(0);
        clearChatMenus();
        continueChat(15);
        clearChatMenus();
        continueChat(13);
        travelTo(pathToLawrenceFromSquare);
        newNpc(5038);
        continueChat(4);
        continueChatScene("continue with my sermon?");
        continueChat(10);
        clearChatMenus();
        travelToReverse(pathToLawrenceFromSquare);
        travelTo(pathToApothecaryFromSquare);
        travelTo(3196, 3402, 100, 11775);
        newNpc(5036);
        continueChat(1);
        continueChat("Talk about something else.");
        continueChat("Talk about Romeo & Juliet.");
        continueChat(6);
        clearChatMenus();
        continueChat(1);
        clearChatMenus();
        travelTo(3192, 3403, 100);
        travelTo(3187, 3403, 100, 11775);
        travelTo(pathToJulietFromApothecary);
        navigateIntoJulietHouse();
        newNpc(5035);
        continueChat(11);
        clearChatMenus();
        continueChat(17);
        navigateOutOfJulietHouse();
        travelToReverse(pathToJulietFromRomeo);
        findRomeo();
        continueChat(18);
        Component closeQuest = ctx.widgets.component(277, 15);
        closeQuest.click();
        Condition.sleep(500);
    }

    public void findRomeo(){
        Npc guy = ctx.npcs.select().id(5037).nearest().poll();
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
        }, 500, 1000);
        resetCamera();
    }
    public void navigateIntoJulietHouse(){
//        a.newDoor(11773, 3165, 3433);
//        a.travelTo(3162, 3433, 500, 11773);
        travelTo(3159, 3435, 500, 11773);
        newDoor(11797, 3155, 3436, "Climb-up");
//        a.travelTo(3157, 3431, 100);
//        a.travelTo(3158, 3428, 100, 11773);
        travelTo(3157, 3425, 100, 11773);
    }
    public void navigateOutOfJulietHouse(){
//        a.travelTo(3158, 3430, 100, 11773);
        travelTo(3155, 3435, 100, 11773);
        newDoor(11799, 3159, 3435, "Climb-down");
//        a.travelTo(3162, 3434, 100);
        travelTo(3168, 3433, 100, 11773);
    }



    @Override
    public void stop(){
    }




    public void collectBerries(){
        System.out.println("Collecting berries");
        hasBerries = Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                travelTo(3268+r.nextInt(3), 3367+r.nextInt(3), 100);
                Condition.sleep(500);
                GameObject berries = ctx.objects.select().id(23625).nearest().poll();
                berries.interact("Pick-from");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == -1;
                    }
                }, 100, 1000);
                return ctx.inventory.select().id(753).count() > 0;
            }
        }, 500, 1000);
        if (hasBerries) atBerries = true;
    }

    public void pickNanners(){
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final GameObject bananaTree = ctx.objects.select().id(BANANA_TREE).nearest().poll();
                bananaTree.interact("Pick");
                Condition.sleep(200);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == -1;
                    }
                });
                return ctx.inventory.select().id(1963).count() > 10;
            }
        }, 100, 2000);
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
