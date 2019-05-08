package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="AfterTutorial", description = "", properties = "author=PinkNanner; topic=999; client=4;")
public class AfterTutorial extends PollingScript<ClientContext> {
    boolean inVarrock = false, inLumbridge = true, atBerries = false, hasBerries, onQuest = false;
    static Random r = new Random();
    static Tile[] pathToBerries, pathToVarrock;
    @Override
    public void start() {
        System.out.println("Started");
        pathToBerries = new Tile[]{new Tile(3236, 3220), new Tile(3250+r.nextInt(2), 3225+r.nextInt(1)), new Tile(3259+r.nextInt(1), 3239+r.nextInt(2)),
                new Tile(3250+r.nextInt(4), 3252+r.nextInt(2)), new Tile(3249+r.nextInt(1), 3267+r.nextInt(2)), new Tile(3239+r.nextInt(1), 3279+r.nextInt(2)),
                new Tile(3237+r.nextInt(2), 3295+r.nextInt(1)), new Tile(3243+r.nextInt(2), 3309+r.nextInt(1)), new Tile(3251+r.nextInt(1), 3320+r.nextInt(1)),
                new Tile(3261+r.nextInt(2), 3330+r.nextInt(2)), new Tile(3272, 3340+r.nextInt(3)), new Tile(3275, 3353+r.nextInt(2)), new Tile(3270, 3364+r.nextInt(3)),
                new Tile(3269, 3369)};

        pathToVarrock = new Tile[]{new Tile(3280+r.nextInt(2), 3371+r.nextInt(2)), new Tile(3290+r.nextInt(1), 3381+r.nextInt(1)),
                new Tile(3289+r.nextInt(3), 3395+r.nextInt(2)), new Tile(3285+r.nextInt(3), 3411+r.nextInt(2)), new Tile(3278+r.nextInt(2), 3424+r.nextInt(1)),
                new Tile(3263+r.nextInt(3), 3426+r.nextInt(3)), new Tile(3251+r.nextInt(3), 3428+r.nextInt(2)), new Tile(3239+r.nextInt(2), 3429+r.nextInt(2)),
                new Tile(3225+r.nextInt(2), 3428+r.nextInt(2)), new Tile(3211+r.nextInt(4), 3424+r.nextInt(1)), new Tile(3211+r.nextInt(4), 3424+r.nextInt(1))};
//        onQuest = true;
//        inVarrock = true;
//        inLumbridge = false;
    }

    @Override
    public void poll() {
        if (inLumbridge) {
            travelToBerries();
        }
        if (atBerries && !hasBerries){
            collectBerries();
        }
        if (atBerries && hasBerries){
            travelToVarrock();
        }
        if (inVarrock && onQuest){
            romeoQuest();
        }

    }
    public void romeoQuest(){
        System.out.println("Starting romeo quest");
        newNpc(5037);
        continueChat(1);
        ctx.chat.continueChat("Perhaps I could help to find her for you?");
        continueChat(10);
        ctx.chat.continueChat("Yes, ok, I'll let her know.");
        continueChat(5);
        ctx.chat.continueChat("Ok, thanks.");



    }

    public void travelToVarrock(){
        resetCamera();
        for (int i=0;i<pathToBerries.length;i++){
            System.out.println("Travelling to: Varrock, i = "+i);
            travelTo(pathToVarrock[i].x(), pathToVarrock[i].y(), 500);
            Condition.sleep(100);
            if (ctx.players.local().inMotion() == false && !ctx.players.local().tile().equals(pathToVarrock[i])){
                pathToVarrock = new Tile[]{new Tile(3280+r.nextInt(2), 3371+r.nextInt(2)), new Tile(3290+r.nextInt(1), 3381+r.nextInt(1)),
                        new Tile(3289+r.nextInt(3), 3395+r.nextInt(2)), new Tile(3285+r.nextInt(3), 3411+r.nextInt(2)), new Tile(3278+r.nextInt(2), 3424+r.nextInt(1)),
                        new Tile(3263+r.nextInt(3), 3426+r.nextInt(3)), new Tile(3251+r.nextInt(3), 3428+r.nextInt(2)), new Tile(3239+r.nextInt(2), 3429+r.nextInt(2)),
                        new Tile(3225+r.nextInt(2), 3428+r.nextInt(2)), new Tile(3211+r.nextInt(4), 3424+r.nextInt(1)), new Tile(3211+r.nextInt(4), 3424+r.nextInt(1))};
                i=-1;
            }
        }
        atBerries = false;
        inVarrock = true;
        onQuest = true;
    }

    public void travelToBerries(){
        resetCamera();
        for (int i=0;i<pathToBerries.length;i++){
            System.out.println("Travelling to: Berries, i = "+i);
            travelTo(pathToBerries[i].x(), pathToBerries[i].y(), 500);
            if (ctx.players.local().inMotion() == false && !ctx.players.local().tile().equals(pathToBerries[i])){
                pathToBerries = new Tile[]{new Tile(3236, 3221), new Tile(3250+r.nextInt(2), 3225+r.nextInt(1)), new Tile(3259+r.nextInt(1), 3239+r.nextInt(2)),
                        new Tile(3250+r.nextInt(5), 3252+r.nextInt(3)), new Tile(3249+r.nextInt(2), 3267+r.nextInt(3)), new Tile(3239+r.nextInt(2), 3279+r.nextInt(3)),
                        new Tile(3237+r.nextInt(3), 3295+r.nextInt(2)), new Tile(3243+r.nextInt(3), 3309+r.nextInt(2)), new Tile(3251+r.nextInt(2), 3320+r.nextInt(2)),
                        new Tile(3264+r.nextInt(3), 3330+r.nextInt(3)), new Tile(3272, 3340+r.nextInt(43)), new Tile(3275, 3353+r.nextInt(3)), new Tile(3270, 3364+r.nextInt(4)),
                        new Tile(3269, 3369)};
                i=-1;
            }
        }
        inLumbridge = false;
        atBerries = true;
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
                }, 100, 100);
                return ctx.inventory.select().id(753).count() > 0;
            }
        }, 500, 100);
        if (hasBerries) atBerries = true;
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
    public void travelTo(int x, int y, int sleep){
        System.out.println("Travelling to: "+x+", "+y);
        ctx.movement.newTilePath(new Tile(x, y)).traverse();
        Condition.sleep(500);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        }, sleep, 100);
        ctx.input.click(267, 507, 1);
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
        System.out.println("Stopped");
    }
}