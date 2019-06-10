package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="Fletcher", description = "Fletch a bunch of fletchable things", properties = "author=PinkNanner; topic=999; client=4;")
public class Fletcher extends PollingScript<ClientContext> {

    //OBJECTS
    private final int BANK_BOOTH = 10060;
    private final int EXCHANGE_BOOTH = 10061;

    //ITEMS
    private final int KNIFE = 946;
    private final int CHISEL = 1755;

    private final int LOGS = 1511;

    private final int FEATHER = 314;
    private final int IRON_ARROW_TIPS = 40;
    private final int ARROW_SHAFT = 52;
    private final int HEADLESS_ARROW = 53;

    private final int WILLOW_LOGS = 1519;
    private final int WILLOW_LONGBOW_U = 58;

    private final int OAK_LOGS = 1521;
    private final int OAK_LONGBOW_U = 56;

    private final int RUBY = 1603;
    private final int EMERALD = 1605;
    private final int DIAMOND = 1601;

    private final int MAPLE_LOGS = 1517;

    private final int YEW_LOGS = 1515;

    ArrayList<Integer> FLETCHING_LIST = new ArrayList<>();

    boolean banking = true, STOPPED = false, newStage = true;
    int animCounter = 0, stage = 0, type = 2, fletchLevel = 1;
    int fletchItem0, fletchItem1;
    Random r = new Random();
    Integer fletchType = 0;
    Assets a = new Assets();

    public void start(){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                GUI gui = new GUI();
//                gui.start();
////                gui.setVisible(true);
//            }
//        });
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                FletchGui gui = new FletchGui();
//                gui.start();
////                gui.setVisible(true);
//            }
//        });
    setup();
        }

        public void setup(){
            a.resetCamera();

            //SETUP OBJECTS
            GameObject bankBooth = ctx.objects.select().id(BANK_BOOTH).nearest().poll();
            GameObject exchangeBooth = ctx.objects.select().id(EXCHANGE_BOOTH).nearest().poll();

            //SETUP FLETCHING LIST
            FLETCHING_LIST.add(RUBY);
            FLETCHING_LIST.add(EMERALD);
            FLETCHING_LIST.add(DIAMOND);
            FLETCHING_LIST.add(MAPLE_LOGS);
            FLETCHING_LIST.add(WILLOW_LOGS);
            FLETCHING_LIST.add(YEW_LOGS);

            setStage();

        }

    public void setStage(){
        if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 24) stage = 0;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 34) stage = 3;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 39) stage = 4;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 49) stage = 5;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 54) stage = 6;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 64) stage = 7;
    }



    public void poll() {
        if (STOPPED == false) {
            animCounter++;
//        System.out.println("animCounter = "+animCounter);
            if (ctx.players.local().animation() != -1) {
                animCounter = 0;
            }
            checkBanking();

            if (banking) {
                if (r.nextInt(12) > 7) { //Delay from finishing an inventory to banking
                    Condition.sleep(4500 + r.nextInt(4500));
                }
                bank(type);
            }
            if (!banking) fletch(type);
            if (animCounter > 500) STOPPED = true;
        }
    }









    public void bank(Integer type) {
//        System.out.println("Banking");
        banking = false;
        GameObject bankBooth = ctx.objects.select().id(BANK_BOOTH).nearest().poll();
        GameObject exchangeBooth = ctx.objects.select().id(EXCHANGE_BOOTH).nearest().poll();

//        if (ctx.players.local().ctx.inventory.select().id(fletchType).count() == 0) {
            if (r.nextInt(10) >= 6) {
                System.out.println("Doing long wait");
                Condition.sleep(4500 + r.nextInt(4500));
            } else Condition.sleep(500 + r.nextInt(500));
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    bankBooth.interact("Bank");
                    return ctx.bank.open();
                }
            }, 50, 100);
            if (type == 0) {
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        ctx.bank.depositAllExcept(KNIFE, CHISEL);
                        return ctx.players.local().ctx.inventory.count() == 1;
                    }
                }, 50, 100);
                for (int i = 0; i < FLETCHING_LIST.size(); i++) {
                    if (ctx.players.local().ctx.inventory.select().id(FLETCHING_LIST.get(i)).count() > 0) {
                        ctx.bank.depositAllExcept(KNIFE, CHISEL);
                        System.out.println("Has fletchable item: " + FLETCHING_LIST.get(i));
                        fletchType = FLETCHING_LIST.get(i);
                        break;
                    }
                }
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        ctx.bank.depositAllExcept(KNIFE, CHISEL);
                        return ctx.bank.withdraw(fletchType, Bank.Amount.ALL);
                    }
                }, 50, 100);
                if (ctx.players.local().ctx.inventory.select().id(fletchType).count() == 0) {
                    System.out.println("Detecting no fletchable materials, stopping");
                    stop();
                }
            }

            if (type == 1){

            }

            if (type == 2){ //Levelling guide bank commands
                        if (stage == 0){ //Cut logs into shafts
                            bankingOptions("LOGS", KNIFE, LOGS, 99);
                        }
                        else if (stage == 1){
                            bankingOptions("ATTACH", ARROW_SHAFT, FEATHER, 99);
                        }
                        else if (stage == 2){
                            bankingOptions("ATTACH", HEADLESS_ARROW, IRON_ARROW_TIPS, 25);
                        }
                        else if (stage == 3){
                            bankingOptions("LOGS", KNIFE, OAK_LOGS, 35);
                        }
                        else if (stage == 4){
                            bankingOptions("LOGS", KNIFE, WILLOW_LOGS, 40);
                        }
                        else if (stage == 5){
                            bankingOptions("LOGS", KNIFE, WILLOW_LOGS, 50);
                        }
                        else if (stage == 6){
                            bankingOptions("LOGS", KNIFE, MAPLE_LOGS, 55);
                        }
                        else if (stage == 7){
                            bankingOptions("LOGS", KNIFE, MAPLE_LOGS, 65);
                        }
            }
            if (banking == false) ctx.bank.close();
//            banking = false;
    }

    public void bankingOptions(String option, int item0, int item1, int maxLevel){
        if (option == "LOGS") {
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositAllExcept(KNIFE);
                    return ctx.players.local().ctx.inventory.count() <= 1;
                }
            }, 10, 100);

            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositAllExcept(KNIFE);
                    if (ctx.players.local().ctx.inventory.count() == 0) ctx.bank.withdraw(KNIFE, 1);
                    if (ctx.players.local().ctx.inventory.count() == 0) return false;
                    return ctx.bank.withdraw(item1, Bank.Amount.ALL);
                }
            }, 10, 100);
        }

        if (option == "GEMS") {
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositAllExcept(CHISEL);
                    return ctx.players.local().ctx.inventory.count() <= 1;
                }
            }, 10, 100);

            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositAllExcept(CHISEL);
                    if (ctx.players.local().ctx.inventory.count() == 0) ctx.bank.withdraw(CHISEL, 1);
                    if (ctx.players.local().ctx.inventory.count() == 0) return false;
                    return ctx.bank.withdraw(item1, Bank.Amount.ALL);
                }
            }, 50, 100);
        }

        if (option == "ATTACH"){
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositAllExcept(item0, item1);
                    return ctx.players.local().ctx.inventory.count() <= 1;
                }
            }, 10, 100);

            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositAllExcept(item0, item1);
                    ctx.bank.withdraw(item0, Bank.Amount.ALL);
                    ctx.bank.withdraw(item1, Bank.Amount.ALL);
                    return ctx.players.local().ctx.inventory.count() == 2;
                }
            }, 10, 100);

        }

        if (option == "STRING"){
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositInventory();
                    return ctx.players.local().ctx.inventory.count() == 0;
                }
            }, 10, 100);

            Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ctx.bank.depositInventory();
                    ctx.bank.withdraw(item0, 14);
                    ctx.bank.withdraw(item1, 14);
                    return ctx.players.local().ctx.inventory.isFull();
                }
            }, 10, 100);
        }
        fletchItem0 = item0;
        fletchItem1 = item1;
        if (ctx.players.local().ctx.inventory.select().id(item0).count() == 0 || ctx.players.local().ctx.inventory.select().id(item1).count() == 0 || ctx.skills.level(Constants.SKILLS_FLETCHING) >= maxLevel && type == 2) {
            System.out.println("Detecting no fletching items or at max level, increasing stage");
            stage++;
            banking = true;
            return;
        }
    }





    public void checkBanking(){
        if (type == 0) {
            if (ctx.players.local().ctx.inventory.select().id(fletchType).count() == 0) {
                banking = true;
            }
        }

        if (type == 2) {
            if (ctx.players.local().ctx.inventory.select().id(fletchItem0).count() == 0 || ctx.players.local().ctx.inventory.select().id(fletchItem1).count() == 0) {
                banking = true;
            }
        }

    }


    public void fletch(Integer type){
//        System.out.println("Fletching type = "+type);
                    if (type == 0){ //FOR LOGS AND GEMS
//                        System.out.println("Fletch type = 0");
                    if (ctx.players.local().animation() == -1 && animCounter > 25) {
                        animCounter = 0;
                        Item knife = ctx.inventory.select().id(KNIFE).poll();
                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                if (knife.valid() == false) return true;
                                return knife.interact("Use");
                            }
                        }, 50, 100);
                        Item chisel = ctx.inventory.select().id(CHISEL).poll();
                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                if (chisel.valid() == false) return true;
                                return chisel.interact("Use");
                            }
                        }, 50, 100);
                        Item fletchItem = ctx.inventory.select().id(fletchType).poll();
                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                return fletchItem.interact("Use");
                            }
                        }, 50, 100);

                        Component fletchingScreen = ctx.widgets.component(270, 0);

                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                return fletchingScreen.valid();
                            }
                        }, 50, 100);

                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                return ctx.widgets.component(270, 14).click();
                            }
                        }, 50, 100);
                    }
                    }
                    if (type == 1 ) { //FOR BOLTS + TIPS AND ARROWS
//                        System.out.println("Fletch type = 1");
                        if (ctx.players.local().animation() == -1 && animCounter > 25) { //Artificial delay TODO: Should only be used for combining bolt + tips or making arrows
                            if (r.nextInt(12) == 5) {
                                Condition.sleep(4500 + r.nextInt(4500));
                            }

                        }
                    }

                    if (type == 2) { //TRAINING GUIDE TO 65
                        //Use numbers to determine which stage of training player is on
//                        System.out.println("Fletch type = 1");


                        if (stage == 0){ // Logs into arrow shafts
                            fletchingOptions("LOGS", LOGS, 0);
                        }
                        if (stage == 1 ){ // Attach feathers to arrow shafts
                            fletchingOptions("ATTACH", ARROW_SHAFT, FEATHER);
                        }
                        if (stage == 2){
                            fletchingOptions("ATTACH", HEADLESS_ARROW, IRON_ARROW_TIPS);
                        }
                        if (stage == 3){ // Oak Logs into longbows unfinished
                            fletchingOptions("LOGS", OAK_LOGS, 2);
                        }
                        if (stage == 4){ // Willow Logs into shortbows unfinished
                            fletchingOptions("LOGS", WILLOW_LOGS, 1);
                        }
                        if (stage == 5){ // Willow Logs into longbows unfinished
                            fletchingOptions("LOGS", WILLOW_LOGS, 2);
                        }
                        if (stage == 6){ // Maple Logs into longbows unfinished
                            fletchingOptions("LOGS", MAPLE_LOGS, 1);
                        }
                        if (stage == 7){ // Maple Logs into longbows unfinished
                            fletchingOptions("LOGS", MAPLE_LOGS, 2);
                        }
                    }
    }

    public void fletchingOptions(String option, int item0, int item1) { //FOR "LOGS" option, item0 = the log type and item1 = the menu option
        if (option == "LOGS") { //to be used for gems as well
            if (ctx.players.local().animation() == -1 && animCounter > 25) {
                animCounter = 0;
                if (newStage) startNewStage();
                Item knife = ctx.inventory.select().id(KNIFE).poll();
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        if (knife.valid() == false) return true;
                        System.out.println("Using knife");
                        return knife.interact("Use");
                    }
                }, 50, 100);

                Item fletchItem = ctx.inventory.select().id(item0).poll();
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        System.out.println("Using logs");
                        return fletchItem.interact("Use");
                    }
                }, 50, 100);

                Component fletchingScreen = ctx.widgets.component(270, 0);

                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return fletchingScreen.valid();
                    }
                }, 50, 100);

                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return ctx.widgets.component(270, 14+item1).click();
                    }
                }, 50, 100);
            }
        }
        if (option == "ATTACH"){
            if (ctx.players.local().animation() == -1 && animCounter > 190) {
                if (r.nextInt(10) >= 7){
                    Condition.sleep(r.nextInt(4500));
                } else Condition.sleep(r.nextInt(200));
                animCounter = 0;
                Item firstItem = ctx.inventory.select().id(item0).poll();
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return firstItem.interact("Use");
                    }
                }, 50, 100);

                Item secondItem = ctx.inventory.select().id(item1).poll();
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return secondItem.interact("Use");
                    }
                }, 50, 100);

                Component fletchingScreen = ctx.widgets.component(270, 0);

                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return fletchingScreen.valid();
                    }
                }, 50, 100);

                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return ctx.widgets.component(270, 14).click();
                    }
                }, 50, 100);
            }
        }
    }

    public void startNewStage(){

            }

    public void stop(){
        System.out.println("Stopped");
        STOPPED = true;
        ctx.controller.stop();
    }

}

class GUI extends JFrame {
    private JPanel panelMain = new JPanel();
    private JComboBox comboBoxJobs = new JComboBox();
    private JButton startButton = new JButton();

    public GUI() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        comboBoxJobs.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    System.out.println("Triggering item = "+event.getItem());
                    // do something with object
                }
            }
        });
        panelMain.add(startButton);
        panelMain.add(comboBoxJobs);
    }

    public static void main(String[] args) {

    }
    public void start(){
        JFrame frame = new JFrame("Fletcher");
        frame.setContentPane(new GUI().panelMain);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panelMain);
        frame.pack();
        frame.setSize(450, 450);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}