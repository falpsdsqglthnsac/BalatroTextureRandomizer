import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
public class App{
    static volatile boolean pressed = false;
    static int indexOfName(ArrayList<GameObject> list,String name){
        for(GameObject o : list){
            if(o.name.equals(name)){
                return list.indexOf(o);
            }
        }
        return -1;
    }
    static int indexOfName(GameObject[] list,String name){
        for(int i = 0;i < list.length;i++){
            if(list[i].name.equals(name)){
                return i;
            }
        }
        return -1;
    }
    static BufferedImage scaleUp(BufferedImage img){
        BufferedImage output = new BufferedImage(img.getWidth()*2,img.getHeight()*2,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = output.createGraphics();
        gr.drawImage(img,0,0,output.getWidth(),output.getHeight(),0,0,img.getWidth(),img.getHeight(),null);
        return output;
    }
    static void writeImg(BufferedImage img,String seed,String path){
        try{
            ImageIO.write(img,"png",new File("TR" + seed + "/assets/1x/" + path));
            ImageIO.write(scaleUp(img),"png",new File("TR" + seed + "/assets/2x/" + path));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{
        JFrame frame = new JFrame("Balatro Texture Randomizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int w = 510;
        int h = 340;
        frame.setBounds(gd.getDisplayMode().getWidth()/2 - w/2,gd.getDisplayMode().getHeight()/2 - h/2,w,h);
        frame.setResizable(false);
        frame.getContentPane().setLayout(null);
        JButton button = new JButton("Randomize!");
        button.setFocusPainted(false);
        button.setBounds(w/2 - 50,h*2/3,100,25);
        frame.add(button);
        JTextField seedInputBox = new JTextField();
        seedInputBox.setBounds(w/2 - 100,h/2 - 25,200,30);
        frame.add(seedInputBox);
        JLabel seedLabel = new JLabel("Seed (optional):",SwingConstants.CENTER);
        seedLabel.setBounds(w/2 - 50,h/2 - 50,100,25);
        frame.add(seedLabel);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                pressed = true;
            }
        });
        seedInputBox.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    pressed = true;
                }
            }
        });
        JLabel seedStatus = new JLabel();
        JLabel randoMessage = new JLabel();
        JLabel tpMessage = new JLabel();
        JLabel doneMessage = new JLabel();
        frame.add(seedStatus);
        frame.add(randoMessage);
        frame.add(tpMessage);
        frame.add(doneMessage);
        frame.setVisible(true);
        while(!pressed){
            Thread.sleep(50);
        }
        button.setVisible(false);
        seedInputBox.setVisible(false);
        seedLabel.setVisible(false);
        String input = seedInputBox.getText();
        frame.remove(button);
        frame.remove(seedInputBox);
        frame.remove(seedLabel);
        long seed;
        try{
            seed = Long.parseUnsignedLong(input,36);
            seedStatus.setText("Seed selected: " + input.toLowerCase());
        }catch(NumberFormatException e){
            seed = new Random().nextLong();
            if(input.equals("")){
                seedStatus.setText("No seed specified. Random seed selected: " + Long.toUnsignedString(seed,36));
            }else{
                seedStatus.setText("Invalid seed specified. Random seed selected: " + Long.toUnsignedString(seed,36));
            }
        }
        String seedStr = Long.toUnsignedString(seed,36);
        Random rng = new Random(seed);
        seedStatus.setBounds(0,0,w,13);
        randoMessage.setText("Randomizing...");
        randoMessage.setBounds(0,14,w,13);
        File coords = new File("assets/coords.csv");
        Scanner sc = new Scanner(coords);
        Scanner rowSc = null;
        String line;
        ArrayList<GameObject> objectList = new ArrayList<GameObject>();
        GameObject[] objectReference = new GameObject[628];
        GameObject tempGO;
        for(int i = 0;i < objectReference.length;i++){
            line = sc.nextLine();
            rowSc = new Scanner(line);
            rowSc.useDelimiter(",");
            if(i < 300){
                tempGO = new GameObject(rowSc.next(),rowSc.nextInt(),rowSc.nextInt(),rowSc.nextInt(),rowSc.nextInt());
            }else{
                tempGO = new GameObject(rowSc.next(),rowSc.nextInt(),rowSc.nextInt());
            }
            objectReference[i] = tempGO;
            objectList.add(tempGO);
        }
        sc.close();
        rowSc.close();
        GameObject[] randomList = new GameObject[objectReference.length];
        String log = "";
        for(int i = 0;i < objectReference.length;i++){
            if(i < 248){// playing cards
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Half Joker")));
            }else if(i < 474){// jokers, decks, and consumables
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Small Blind")));
            }else if(i < 505){// blind chips
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Arcana Pack 1")));
            }else if(i < 538){// booster packs
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"White Stake")));
            }else if(i < 548){// stake chips
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Gold Seal")));
            }else if(i < 552){// seals
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Stone")));
            }else if(i < 560){// enhancements
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Soul Stone")));
            }else if(i < 569){// hoverers
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Uncommon Tag")));
            }else if(i < 594){// tags
                tempGO = objectList.get((int)(rng.nextFloat() * indexOfName(objectList,"Overstock")));
            }else{// vouchers
                tempGO = objectList.get((int)(rng.nextFloat() * objectList.size()));
            }
            randomList[i] = tempGO;
            objectList.remove(tempGO);
            log += objectReference[i].name + "," + tempGO.name + "\n";
        }
        tpMessage.setText("Making texture pack...");
        tpMessage.setBounds(0,27,w,13);
        new File("TR" + seedStr + "/assets/1x/collabs").mkdirs();
        new File("TR" + seedStr + "/assets/2x/collabs").mkdirs();
        File megaSpritesheet = new File("assets/megaspritesheet.png");
        BufferedImage allSprites = ImageIO.read(megaSpritesheet);
        BufferedImage outputImage = new BufferedImage(923,380,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = outputImage.createGraphics();
        for(int i = 0;i < 52;i++){
            gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),objectReference[i].x,objectReference[i].y,null);
        }
        writeImg(outputImage,seedStr,"8BitDeck.png");
        outputImage = new BufferedImage(923,380,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 52;i < 104;i++){
            gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),objectReference[i-52].x,objectReference[i-52].y,null);
        }
        writeImg(outputImage,seedStr,"8BitDeck_opt2.png");
        String[] friends = new String[]{
            "AU","TBoI","CL","D2","CR","BUG","VS","STS","PC","WF","DBD","FO",
            "DTD","SV","EG","XR","C7","R","TW","CYP","SK","DS","AC","STP"
        };
        for(int i = 104;i < 248;i++){
            outputImage = new BufferedImage(213,95,BufferedImage.TYPE_INT_ARGB);
            gr = outputImage.createGraphics();
            for(int j = 0;j < 3;j++){
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),j * 71,0,null);
                i++;
            }
            writeImg(outputImage,seedStr,"collabs/collab_" + friends[((i - 107)/3)%24] + "_" + ((i - 105)/72 + 1) + ".png");
            i--;
        }
        outputImage = new BufferedImage(714,1054,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 474;i < 505;i++){
            gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,714,34),0,(i - 474) * 34,null);
        }
        writeImg(outputImage,seedStr,"BlindChips.png");
        outputImage = new BufferedImage(284,855,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 505;i < 538;i++){
            if(i < 525){
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),(i - 505)%4 * 71,((i - 505)/4) * 95,null);
            }else if(i > 525){
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),(i - 506)%4 * 71,((i - 502)/4) * 95,null);
            }else{
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),0,5 * 95,null);
            }
        }
        writeImg(outputImage,seedStr,"boosters.png");
        outputImage = new BufferedImage(145,58,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 538;i < 548;i++){
            gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,29,29),(i - 538)%5 * 29,((i - 538)/5) * 29,null);
        }
        writeImg(outputImage,seedStr,"chips.png");
        String[] enhancers = new String[]{
            "Red Deck","","Gold Seal","Nebula Deck","Locked Deck","Stone","Gold",
            "Soul Stone","Bonus","Mult","Wild","Lucky","Glass","Steel",
            "Blue Deck","Yellow Deck","Green Deck","Black Deck","Plasma Deck","Gradient Deck","Ghost Deck",
            "Magic Deck","Checkered Deck","Erratic Deck","Abandoned Deck","Painted Deck","Undiscovered Joker Face","Undiscovered Consumable",
            "Challenge Deck","Streets Deck","Anaglyph Deck","Zodiac Deck","Purple Seal","Red Seal","Blue Seal"
        };
        outputImage = new BufferedImage(497,475,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 0;i < enhancers.length;i++){
            if(enhancers[i].equals("")){
                gr.drawImage(allSprites.getSubimage(2627,0,71,95),71,0,null);
            }else if(randomList[indexOfName(objectReference,enhancers[i])].altX > -1){
                gr.drawImage(allSprites.getSubimage(randomList[indexOfName(objectReference,enhancers[i])].altX,randomList[indexOfName(objectReference,enhancers[i])].altY,71,95),i%7 * 71,(i/7) * 95,null);
            }else{
                gr.drawImage(allSprites.getSubimage(randomList[indexOfName(objectReference,enhancers[i])].x,randomList[indexOfName(objectReference,enhancers[i])].y,71,95),i%7 * 71,(i/7) * 95,null);
            }
        }
        writeImg(outputImage,seedStr,"Enhancers.png");
        String[] jokers = new String[]{
            "Joker","Chaos the Clown","Jolly Joker","Zany Joker","Mad Joker","Crazy Joker","Droll Joker","Half Joker","Merry Andy","Stone Joker",
            "Juggler","Drunkard","Acrobat","Sock and Buskin","Mime","Credit Card","Greedy Joker","Lusty Joker","Wrathful Joker","Gluttonous Joker",
            "Troubadour","Banner","Mystic Summit","Marble Joker","Loyalty Card","Hack","Misprint","Steel Joker","Raised Fist","Golden Joker",
            "Blueprint","Glass Joker","Scary Face","Abstract Joker","Delayed Gratification","Golden Ticket","Pareidolia","Cartomancer","Even Steven","Odd Todd",
            "Scholar","Business Card","Supernova","Mr. Bones","Seeing Double","The Duo","The Trio","The Family","The Order","The Tribe",
            "8-Ball","Fibonacci","Joker Stencil","Space Joker","Matador","Ceremonial Dagger","Showman","Fortune Teller","Hit the Road","Swashbuckler",
            "Flower Pot","Ride the Bus","Shoot the Moon","Scholar 2","Smeared Joker","Oops! All 6s","Four Fingers","Gros Michel","Stuntman","Hanging Chad",
            "Driver's License","Invisible Joker","Astronomer","Burnt Joker","Dusk","Throwback","The Idol","Brainstorm","Satellite","Rough Gem",
            "Bloodstone","Arrowhead","Onyx Agate","Canio","Triboulet","Yorick","Chicot","Perkeo","Certificate","Bootstraps",
            "","","Hologram Face","Canio Face","Triboulet Face","Yorick Face","Chicot Face","Perkeo Face","Locked Joker","Undiscovered Joker",
            "Egg","Burglar","Blackboard","Runner","Ice Cream","DNA","Splash","Blue Joker","Sixth Sense","Constellation",
            "Hiker","Faceless Joker","Green Joker","Superposition","To Do List","Cavendish","Card Sharp","Red Card","Madness","Square Joker",
            "Séance","Riff-Raff","Vampire","Shortcut","Hologram","Vagabond","Baron","Cloud 9","Rocket","Obelisk",
            "Midas Mask","Luchador","Photograph","Gift Card","Turtle Bean","Erosion","Reserved Parking","Mail-In Rebate","To the Moon","Hallucination",
            "Sly Joker","Wily Joker","Clever Joker","Devious Joker","Crafty Joker","Lucky Cat","Baseball Card","Bull","Diet Cola","Trading Card",
            "Flash Card","Popcorn","Ramen","Seltzer","Spare Trousers","Campfire","Smiley Face","Ancient Joker","Walkie-Talkie","Castle"
        };
        outputImage = new BufferedImage(710,1520,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 0;i < jokers.length;i++){
            if(jokers[i].equals("")){
                continue;
            }else if(jokers[i].equals("Scholar 2")){
                gr.drawImage(allSprites.getSubimage(2059,570,71,95),213,570,null);
            }else if(randomList[indexOfName(objectReference,jokers[i])].altX > -1){
                gr.drawImage(allSprites.getSubimage(randomList[indexOfName(objectReference,jokers[i])].altX,randomList[indexOfName(objectReference,jokers[i])].altY,71,95),i%10 * 71,(i/10) * 95,null);
            }else{
                gr.drawImage(allSprites.getSubimage(randomList[indexOfName(objectReference,jokers[i])].x,randomList[indexOfName(objectReference,jokers[i])].y,71,95),i%10 * 71,(i/10) * 95,null);
            }
        }
        writeImg(outputImage,seedStr,"Jokers.png");
        outputImage = new BufferedImage(204,170,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 569;i < 594;i++){
            gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,34,34),(i == 593) ? 102 : (i - 569)%6 * 34,((i - 569)/6) * 34,null);
        }
        writeImg(outputImage,seedStr,"tags.png");
        outputImage = new BufferedImage(710,570,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 418;i < 474;i++){
            if(randomList[i].altX > -1){
                gr.drawImage(allSprites.getSubimage(randomList[i].altX,randomList[i].altY,71,95),(i - 418)%10 * 71,((i - 418)/10) * 95,null);
            }else{
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),(i - 418)%10 * 71,((i - 418)/10) * 95,null);
            }
        }
        writeImg(outputImage,seedStr,"Tarots.png");
        outputImage = new BufferedImage(639,380,BufferedImage.TYPE_INT_ARGB);
        gr = outputImage.createGraphics();
        for(int i = 594;i < 628;i++){
            if(i < 610){
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),(i - 594)%8 * 71,((i - 594)/8) * 95,null);
            }else{
                gr.drawImage(allSprites.getSubimage(randomList[i].x,randomList[i].y,71,95),(i - 610)%9 * 71,((i - 610)/9) * 95 + 190,null);
            }
        }
        writeImg(outputImage,seedStr,"Vouchers.png");
        File config = new File("assets/Template.lua");
        sc = new Scanner(config);
        String configString = "";
        while(sc.hasNext()){
            configString += sc.nextLine() + "\n";
        }
        configString = configString.replace("?seed",seedStr);
        FileWriter fw = new FileWriter("TR" + seedStr + "/TR" + seedStr + ".lua");
        fw.write(configString);
        fw.close();
        sc.close();
        fw = new FileWriter("TR" + seedStr + "/log.csv");
        fw.write(log);
        fw.close();
        doneMessage.setText("Done! Texture pack TR" + seedStr + " created. You may now close this window.");
        doneMessage.setBounds(0,40,w,13);
    }
}
class GameObject{
    String name;
    int x, y;
    int altX = -1;
    int altY = -1;
    GameObject(String n,int x,int y){
        name = n;
        this.x = x;
        this.y = y;
    }
    GameObject(String n,int x,int y,int x2,int y2){
        this(n,x,y);
        altX = x2;
        altY = y2;
    }
}