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

    private final int WILLOW_LOGS = 1519;
    private final int WILLOW_LONGBOW_U = 58;

    private final int RUBY = 1603;
    private final int EMERALD = 1605;
    private final int DIAMOND = 1601;

    private final int MAPLE_LOGS = 1517;

    private final int YEW_LOGS = 1515;

    ArrayList<Integer> FLETCHING_LIST = new ArrayList<>();


    boolean banking = true, STOPPED = false, newStage = true;
    int animCounter = 0, stage = 0, type = 2;
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

            //Determine starting action
            if (ctx.players.local().ctx.inventory.count() <= 1){
                System.out.println("Empty invent or has chisel");
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        bankBooth.interact("Bank");
                        return ctx.bank.open();
                    }
                }, 50, 100);
//            if (ctx.players.local().ctx.inventory.select().id(KNIFE).count() <=0){
//                ctx.bank.withdraw(KNIFE, 1);
//            }
                if (type == 0) {
                    for (int i = 0; i < FLETCHING_LIST.size(); i++) {
                        if (ctx.bank.select().id(FLETCHING_LIST.get(i)).count() > 0) {
                            System.out.println("Has fletchable item: " + FLETCHING_LIST.get(i));
                            ctx.bank.withdraw(FLETCHING_LIST.get(i), Bank.Amount.ALL);
                            fletchType = FLETCHING_LIST.get(i);
                            break;
                        }
                    }
                }

                if (type == 2){
                    Condition.wait(new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            if (stage == 0){
                                ctx.bank.depositAllExcept(KNIFE, CHISEL);
                                return ctx.players.local().ctx.inventory.count() == 1;
                            }
                            if (stage == 1){
                                ctx.bank.depositInventory();
                                return ctx.players.local().ctx.inventory.count() == 0;
                            }

                            return false;
                        }
                    }, 50, 100);

                    Condition.wait(new Callable<Boolean>() {
                        public Boolean call() throws Exception {

                            if (stage == 0) {
                                ctx.bank.depositAllExcept(KNIFE, CHISEL);
                                return ctx.bank.withdraw(LOGS, Bank.Amount.ALL);
                            }
                            if (ctx.players.local().ctx.inventory.select().id(LOGS).count() == 0) {
                                System.out.println("Detecting no logs, increasing stage");
                                stage++;
                                System.out.println("stage = "+stage);
                                return true;
                            }


                            if (stage == 1) {
                                ctx.bank.depositAllExcept(ARROW_SHAFT, FEATHER);
                                ctx.bank.withdraw(ARROW_SHAFT, Bank.Amount.ALL);
                                ctx.bank.withdraw(FEATHER, Bank.Amount.ALL);
                                return ctx.players.local().ctx.inventory.count() == 2;
                            }
                            if (ctx.players.local().ctx.inventory.select().id(ARROW_SHAFT).count() == 0) {
                                System.out.println("Detecting no arrow shafts, increasing stage");
                                stage++;
                                System.out.println("stage = "+stage);
                                return true;
                            }


                            return false;
                        }
                    }, 50, 100);
                }

                ctx.bank.close();
                banking = false;
            }

            if (ctx.players.local().ctx.inventory.count() > 1){ //TODO: make a list of all logs/gems required to start up, if none of those are found then empty inventory
                //TODO: and grab whichever one if present in the bank
                System.out.println("Full inventory");
                for (int i=0;i<FLETCHING_LIST.size();i++){
                    if (ctx.players.local().ctx.inventory.select().id(FLETCHING_LIST.get(i)).count() > 0){
                        System.out.println("Has fletchable item: "+FLETCHING_LIST.get(i));
                        ctx.bank.depositAllExcept(KNIFE, CHISEL);
                        ctx.bank.withdraw(FLETCHING_LIST.get(i), Bank.Amount.ALL);
                        fletchType = FLETCHING_LIST.get(i);
                        break;
                    }
                }
            }
//            else {
//                Condition.wait(new Callable<Boolean>() {
//                    public Boolean call() throws Exception {
//                        bankBooth.interact("Bank");
//                        return ctx.bank.open();
//                    }
//                }, 50, 100);
//                ctx.bank.depositAllExcept(KNIFE, CHISEL);
//            for (int i=0;i<FLETCHING_LIST.size();i++){
//                if (ctx.bank.select().id(FLETCHING_LIST.get(i)).count() > 0) {
//                    System.out.println("Has fletchable item: "+FLETCHING_LIST.get(i));
//                    ctx.bank.withdraw(FLETCHING_LIST.get(i), Bank.Amount.ALL);
//                    fletchType = FLETCHING_LIST.get(i);
//                    break;
//                }
//            }
//                ctx.bank.close();
//            }
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
        GameObject bankBooth = ctx.objects.select().id(BANK_BOOTH).nearest().poll();
        GameObject exchangeBooth = ctx.objects.select().id(EXCHANGE_BOOTH).nearest().poll();

        if (ctx.players.local().ctx.inventory.select().id(fletchType).count() == 0) {
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
                        ctx.bank.withdraw(FLETCHING_LIST.get(i), Bank.Amount.ALL);
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

            if (type == 2){ //Bank
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        if (stage == 0){
                            ctx.bank.depositAllExcept(KNIFE);
                            if (ctx.players.local().ctx.inventory.count() == 0) ctx.bank.withdraw(KNIFE, 1);
                            return ctx.players.local().ctx.inventory.count() == 1;
                        }
                        if (stage == 1){
                            ctx.bank.depositInventory();
                            return ctx.players.local().ctx.inventory.count() == 0;
                        }

                        return false;
                    }
                }, 50, 100);

                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {

                        if (stage == 0) {
                            ctx.bank.depositAllExcept(KNIFE, CHISEL);
                            return ctx.bank.withdraw(LOGS, Bank.Amount.ALL);
                        }

                        if (stage == 1) {
                            ctx.bank.withdraw(ARROW_SHAFT, Bank.Amount.ALL);
                            ctx.bank.withdraw(FEATHER, Bank.Amount.ALL);
                            return ctx.players.local().ctx.inventory.count() == 2;
                        }
                        return false;
                    }
                }, 50, 100);
                if (stage == 0) {
                    if (ctx.players.local().ctx.inventory.select().id(LOGS).count() == 0) {
                        System.out.println("Detecting no logs, increasing stage");
                        stage++;
                    }
                }
                if (stage == 1){
                    if (ctx.players.local().ctx.inventory.select().id(ARROW_SHAFT).count() == 0 || ctx.players.local().ctx.inventory.select().id(FEATHER).count() == 0) {
                        System.out.println("Detecting no arrow shafts or feathers, increasing stage");
                        stage++;
                    }
                }
            }
            ctx.bank.close();
            banking = false;
        }
    }

    public void checkBanking(){
        if (type == 0) {
            if (ctx.players.local().ctx.inventory.select().id(fletchType).count() == 0) {
                banking = true;
            }
        }

        if (type == 2) {
            if (stage == 0) {
                if (ctx.players.local().ctx.inventory.select().id(LOGS).count() == 0) {
                    banking = true;
                }
            }
            if (stage == 1) {
                if (ctx.players.local().ctx.inventory.select().id(ARROW_SHAFT).count() == 0) {
                    banking = true;
                }
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

//                    Condition.wait(new Callable<Boolean>() {
//                        public Boolean call() throws Exception {
//                            return ctx.widgets.component(270, 15).click();
//                        }
//                    }, 50, 100);

//                        Condition.wait(new Callable<Boolean>() {
//                            public Boolean call() throws Exception {
//                                return ctx.widgets.component(270, 16).click();
//                            }
//                        }, 50, 100);
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
                        if (stage == 0){
//                            System.out.println("Stage = 0");
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

                                Item fletchItem = ctx.inventory.select().id(LOGS).poll();
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
                                        return ctx.widgets.component(270, 14).click();
                                    }
                                }, 50, 100);
                            }

//                            stage++;
                        }


                        if (stage == 1 ){
//                            System.out.println("Stage = 1");
                            if (ctx.players.local().animation() == -1 && animCounter > 110) {
                                animCounter = 0;
                                Item arrowShaft = ctx.inventory.select().id(LOGS).poll();
                                Condition.wait(new Callable<Boolean>() {
                                    public Boolean call() throws Exception {
                                        return arrowShaft.interact("Use");
                                    }
                                }, 50, 100);

                                Item feather = ctx.inventory.select().id(LOGS).poll();
                                Condition.wait(new Callable<Boolean>() {
                                    public Boolean call() throws Exception {
                                        return feather.interact("Use");
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