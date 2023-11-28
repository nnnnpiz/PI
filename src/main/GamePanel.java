package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class GamePanel extends JPanel {
    private MouseInputs mouseInputs; //crio este atributo de class de forma a nao passar como MouseListener dois objetos diferentes
    private float xDelta=100, yDelta=100;
    //private Random random;
    private BufferedImage img; //usamos BufferedImage para desta forma termos numa so imagem o conjunto de sprites das animaçoes e assim podermos selecionar qual sprite usar com uma so imagem!


    public GamePanel(){

        mouseInputs = new MouseInputs(this);

        importImg();
        
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs); //clicks
        addMouseMotionListener(mouseInputs); //drags
    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        InputStream is2 = getClass().getResourceAsStream("/sprite2");

        try {//load inputStream:
            img = ImageIO.read(is2);
        } catch (IOException e) {
            System.out.println("error loading image!");
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280,800); //imgs de 32 pixeis por 32 => 1280/32 = 40 wide. 800/32 = 25 height
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void changeXDelta(int value){
        this.xDelta +=value;

    }

    public void changeYDelta(int value){
        this.yDelta +=value;

    }

    public void setRectPos(int x, int y){
        this.xDelta=x;
        this.yDelta=y;

    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img.getSubimage(0,0, 64, 40) ,0,0, 128, 80, null); //4º arg. ImageObserver usado p monitorizar o estado da imagem antes de ser FULLY drawn nao vai ser usado por questoes de simplicidade de codigo


    }


}
