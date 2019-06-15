package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

//LOGS
    private final int LOGS = 1511;
    private final int ACHEY = 1511;
    private final int OAK_LOGS = 1521;
    private final int WILLOW_LOGS = 1519;
    private final int MAPLE_LOGS = 1517;
    private final int YEW_LOGS = 1515;

    private final int FEATHER = 314;
    private final int IRON_ARROW_TIPS = 40;
    private final int ARROW_SHAFT = 52;
    private final int HEADLESS_ARROW = 53;


    private final int WILLOW_LONGBOW_U = 58;


    private final int OAK_LONGBOW_U = 56;

//GEMS
//
    private final int OPAL = 1809;
    private final int JADE = 1611;
    private final int PEARL = 411;
    private final int TOPAZ = 1613;
    private final int SAPPHIRE = 1607;
    private final int EMERALD = 1605;
    private final int RUBY = 1603;
    private final int DIAMOND = 1601;
    private final int DRAGONSTONE = 1615;
    private final int ONYX = 1615;









    static ArrayList<Integer> FLETCHING_LIST = new ArrayList<>();

    GuiApp1 ui = new GuiApp1();
    boolean banking = true, STOPPED = false, newStage = true, PAUSED = true, firstStart = true, hasBanked;
    Boolean ruby, emerald, diamond, sapphire, opal, amethyst, jade, pearl, topaz, dragonstone, onyx;
    ArrayList<Boolean> gemArray = new ArrayList();
    int animCounter = 0, stage = 0, type = 1;
    int fletchItem0, fletchItem1;
    Random r = new Random();
    Integer fletchType = 0;

    public void start(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
        resetCamera();
        setup();
        }


        public void setup(){

        gemArray.add(ruby);gemArray.add(emerald);
        gemArray.add(diamond);gemArray.add(sapphire);
        gemArray.add(opal);gemArray.add(amethyst);
        gemArray.add(jade);gemArray.add(pearl);
        gemArray.add(topaz);gemArray.add(dragonstone);
        gemArray.add(onyx);

            //SETUP FLETCHING LIST
            FLETCHING_LIST.add(RUBY);
            FLETCHING_LIST.add(EMERALD);
            FLETCHING_LIST.add(DIAMOND);
            FLETCHING_LIST.add(MAPLE_LOGS);
            FLETCHING_LIST.add(WILLOW_LOGS);
            FLETCHING_LIST.add(YEW_LOGS);



            //GET INFO FROM GUI SETTINGS
            ui.getGems();






        }

    public void setStage(){
        if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 24) stage = 0;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 34) stage = 3;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 39) stage = 4;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 49) stage = 5;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 54) stage = 6;
        else if (ctx.skills.level(Constants.SKILLS_FLETCHING) <= 64) stage = 7;


        type = ui.getMode();
        System.out.println("Type = "+type);
        setPAUSED(false);
    }
    public void setPAUSED(boolean b){
        PAUSED = b;
    }



    public void poll() {
        if (STOPPED == false) {
            if (ui.getStarted() && firstStart) {
                System.out.println("UI started == true");
                setStage();
                firstStart = false;
            } else if (!ui.getStarted() && !firstStart){
                setPAUSED(true);
                firstStart = true;
                System.out.println("UI started == false, pausing");
            }
            if (PAUSED == false) {
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
                if (opal) bankingOptions("GEMS", CHISEL, OPAL, 99, true);
                if (jade) bankingOptions("GEMS", CHISEL, JADE, 99, true);
                if (pearl) bankingOptions("GEMS", CHISEL, PEARL, 99, true);
                if (topaz) bankingOptions("GEMS", CHISEL, TOPAZ, 99, true);
                if (sapphire) bankingOptions("GEMS", CHISEL, SAPPHIRE, 99, true);
                if (emerald) bankingOptions("GEMS", CHISEL, EMERALD, 99, true);
                if (ruby) bankingOptions("GEMS", CHISEL, RUBY, 99, true);
                if (diamond) bankingOptions("GEMS", CHISEL, DIAMOND, 99, true);
                if (dragonstone) bankingOptions("GEMS", CHISEL, DRAGONSTONE, 99, true);
                if (onyx) bankingOptions("GEMS", CHISEL, ONYX, 99, true);



            }

            if (type == 2){ //Levelling guide bank commands
                        if (stage == 0){ //Cut logs into shafts
                            bankingOptions("LOGS", KNIFE, LOGS, 8, true);
                        }
                        else if (stage == 1){
                            bankingOptions("ATTACH", ARROW_SHAFT, FEATHER, 99, true);
                        }
                        else if (stage == 2){
                            bankingOptions("ATTACH", HEADLESS_ARROW, IRON_ARROW_TIPS, 25, true);
                        }
                        else if (stage == 3){
                            bankingOptions("LOGS", KNIFE, OAK_LOGS, 35, true);
                        }
                        else if (stage == 4){
                            bankingOptions("LOGS", KNIFE, WILLOW_LOGS, 40, false);
                        }
                        else if (stage == 5){
                            bankingOptions("LOGS", KNIFE, WILLOW_LOGS, 50, true);
                        }
                        else if (stage == 6){
                            bankingOptions("LOGS", KNIFE, MAPLE_LOGS, 55, false);
                        }
                        else if (stage == 7){
                            bankingOptions("LOGS", KNIFE, MAPLE_LOGS, 65, true);
                        }
            }
            hasBanked = false;
            if (banking == false) ctx.bank.close();
//            banking = false;
    }

    public void bankingOptions(String option, int item0, int item1, int maxLevel, boolean useAll){
        if (hasBanked == false) {
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

            if (option == "ATTACH") {
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

            if (option == "STRING") {
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
            hasBanked = true;
            if (type == 2) {
                if (useAll) {
                    if ((ctx.players.local().ctx.inventory.select().id(item0).count() == 0 || ctx.players.local().ctx.inventory.select().id(item1).count() == 0) && ctx.skills.level(Constants.SKILLS_FLETCHING) >= maxLevel) {
                        System.out.println("Detecting no fletching items or at max level, increasing stage");
                        stage++;
                        banking = true;
                        if (stage > 7) stop();
                        if (ctx.skills.level(Constants.SKILLS_FLETCHING) >= 65) stop();
                        return;
                    }
                } else if (!useAll) {
                    if (ctx.players.local().ctx.inventory.select().id(item0).count() == 0 || ctx.players.local().ctx.inventory.select().id(item1).count() == 0 || ctx.skills.level(Constants.SKILLS_FLETCHING) >= maxLevel) {
                        System.out.println("Detecting no fletching items or at max level, increasing stage");
                        stage++;
                        banking = true;
                        if (stage > 7) stop();
                        if (ctx.skills.level(Constants.SKILLS_FLETCHING) >= 65) stop();
                        return;
                    }
                }
                if ((ctx.players.local().ctx.inventory.select().id(item0).count() == 0 || ctx.players.local().ctx.inventory.select().id(item1).count() == 0) && ctx.skills.level(Constants.SKILLS_FLETCHING) < maxLevel) {
                    stop();
                }
            } else if (ctx.players.local().ctx.inventory.select().id(item0).count() == 0 || ctx.players.local().ctx.inventory.select().id(item1).count() == 0) {
                System.out.println("Detecting no fletching items, stopping");
                stop();
                return;
            }
        }
    }





    public void checkBanking(){
        if (type == 0) {
            if (ctx.players.local().ctx.inventory.select().id(fletchType).count() == 0) {
                banking = true;
            }
        }
        if (type == 1){
            if (ctx.players.local().ctx.inventory.select().id(fletchItem0).count() == 0 || ctx.players.local().ctx.inventory.select().id(fletchItem1).count() == 0) {
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

//                      fletchingOptions("GEMS", RUBY, 0);
                      fletchingOptions("GEMS", DIAMOND, 0);
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
        if (option == "LOGS") {
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
        if (option == "GEMS") { //to be used for gems as well
            if (ctx.players.local().animation() == -1 && animCounter > 25) {
                animCounter = 0;
                if (newStage) startNewStage();
                Item chisel = ctx.inventory.select().id(CHISEL).poll();
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        if (chisel.valid() == false) return true;
                        System.out.println("Using knife");
                        return chisel.interact("Use");
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
    public void resetCamera(){
        Component lookNorth = ctx.widgets.component(161, 24);
        lookNorth.click();
        ctx.camera.pitch(99);
        Condition.sleep(1000);
    }

    public void startNewStage(){

            }

    public void stop(){
        System.out.println("Stopped");
        STOPPED = true;
        ctx.controller.stop();
    }

}

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
class GuiApp1 {
    private boolean started = false;
    int type = 1;

    //Note: Typically the main method will be in a
//separate class. As this is a simple one class
//example it's all in the one class.
    public static void main(String[] args) {
        new GuiApp1();
    }
    public GuiApp1()
    {

        JFrame guiFrame = new JFrame();
//make sure the program exits when the frame closes
        guiFrame.setTitle("Fletcher");
        guiFrame.setSize(300,250);
//This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
//Options for the JComboBox
        String[] fletchingOptions = {"Gems to Bolt Tips", "Logs", "Combine Items", "Training Mode"};
//Options for the JList
//        String[] vegOptions = {"Asparagus", "Beans", "Broccoli", "Cabbage"
//                , "Carrot", "Celery", "Cucumber", "Leek", "Mushroom"
//                , "Pepper", "Radish", "Shallot", "Spinach", "Swede"
//                , "Turnip"};
        String[] gemSettings = {""};
        String[] logSettings = {""};
        String[] combineSettings = {""};
        String[] trainingSettings = {""};
//The first JPanel contains a JLabel and JCombobox
        final JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Options:");
        JLabel settingsLbl = new JLabel("Settings:");
        JComboBox optionsCombo = new JComboBox(fletchingOptions);
        comboPanel.add(comboLbl);
        comboPanel.add(optionsCombo);
        comboPanel.add(settingsLbl);
//Create the second JPanel. Add a JLabel and JList
//Create JCheckBox's for all the options
        final JPanel gemPanel = new JPanel();
        gemPanel.setVisible(true);

        final JPanel logPanel = new JPanel();
        logPanel.setVisible(false);

        final JPanel combinePanel = new JPanel();
        combinePanel.setVisible(false);

        final JPanel trainingPanel = new JPanel();
        trainingPanel.setVisible(false);


        JCheckBox rubyBox = new JCheckBox("Ruby");
        JCheckBox diamondBox = new JCheckBox("Diamond");
        JCheckBox sapphireBox = new JCheckBox("Sapphire");
        JCheckBox emeraldBox = new JCheckBox("Emerald");
        JCheckBox opalBox = new JCheckBox("Opal");
        JCheckBox jadeBox = new JCheckBox("Jade");
        JCheckBox topazBox = new JCheckBox("Topaz");
        JCheckBox dragonBox = new JCheckBox("Dragonstone");
        JCheckBox onyxBox = new JCheckBox("Onyx");
        JCheckBox pearlBox = new JCheckBox("Pearl");
//
//

//
//
        JLabel descriptionLabel = new JLabel("Cuts all selected gem types into bolt tips,\n in order from lowest to highest.\n " +
                "Will stop if you run out of materials\n or do not have the skill requirements.");
//
//
        gemPanel.add(opalBox);

        gemPanel.add(jadeBox);
        gemPanel.add(pearlBox);
        gemPanel.add(topazBox);
        gemPanel.add(sapphireBox);
        gemPanel.add(emeraldBox);
        gemPanel.add(rubyBox);
        gemPanel.add(diamondBox);
        gemPanel.add(dragonBox);
        gemPanel.add(onyxBox);
        gemPanel.add(descriptionLabel);


        JButton startBut = new JButton( "Start");
//The ActionListener class is used to handle the
//event that happens when the user clicks the button.
//As there is not a lot that needs to happen we can
//define an anonymous inner class to make the code simpler.


        optionsCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ComboBox = "+optionsCombo.getSelectedItem() +" "+optionsCombo.getSelectedIndex());
                if (optionsCombo.getSelectedIndex() == 0){
                    gemPanel.setVisible(true);
                    logPanel.setVisible(false);
                    combinePanel.setVisible(false);
                    trainingPanel.setVisible(false);
                }
                if (optionsCombo.getSelectedIndex() == 1){
                    gemPanel.setVisible(false);
                    logPanel.setVisible(true);
                    combinePanel.setVisible(false);
                    trainingPanel.setVisible(false);
                }
                if (optionsCombo.getSelectedIndex() == 2){
                    gemPanel.setVisible(false);
                    logPanel.setVisible(false);
                    combinePanel.setVisible(true);
                    trainingPanel.setVisible(false);
                }
                if (optionsCombo.getSelectedIndex() == 3){
                    gemPanel.setVisible(false);
                    logPanel.setVisible(false);
                    combinePanel.setVisible(false);
                    trainingPanel.setVisible(true);
                    type = 2;
                }

            }
        });



        startBut.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if (!started) {
                    setStarted(true);
                    startBut.setText("Stop");
                } else if (started) {
                    setStarted(false);
                    startBut.setText("Start");
                }
                System.out.println("Started = "+started);
            }
        });

//The JFrame uses the BorderLayout layout manager.
//Put the two JPanels and JButton in different areas.
        guiFrame.add(comboPanel, BorderLayout.NORTH);
        guiFrame.add(gemPanel, BorderLayout.CENTER);
        guiFrame.add(startBut,BorderLayout.SOUTH);
//        guiFrame.add(stopBut,BorderLayout.SOUTH);
//make sure the JFrame is visible
        guiFrame.setVisible(true);
    }
    public static void getGems(){
        System.out.println("Getting gems");
    }
    public boolean getStarted(){
        return started;
    }
    public void setStarted(boolean b){
        started = b;
    }
    public int getMode(){
        return type;
    }
}