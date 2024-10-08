package utils;

import entities.Crabby;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.net.*;

import static utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    //smp q precsamos de um p carregar definimos aqui e usamos o GetSpriteAtlas p isso
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    //public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTIONS_MENU = "options_background.png";
    public static final String SHARK_SPRITE = "shark_atlas.png";
    public static final String EVENTO_ESPECIAL = "tiago.png";


    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage img= null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+fileName);

        try {//load inputStream:
            img = ImageIO.read(is);

        } catch (IOException e) {
            System.out.println("error loading image!");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.out.println("error trying to close inputStream");
            }
        }
        return img;
    }

    //para multiple levels:
    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        //URL => location. URI => Recurso. como vimos em AR.
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        //sorting:
        for(int i=0; i<filesSorted.length; i++)
            for(int j =0; j<files.length; j++){
                if(files[j].getName().equals((i+1) + ".png"))
                    filesSorted[i] = files[j];
            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i=0; i<imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgs;
    }

    //TODO !
    public static ArrayList<Crabby> GetCrabsVA(){
        ArrayList<Crabby> list = new ArrayList<>();
        Random r = new Random();
        double a = r.nextGaussian();
        //fzr um while p continuar a gerar valores diferentes.

        list.add(new Crabby((float)a*Game.TILES_SIZE, (float)a*Game.TILES_SIZE));

        return list;
    }






}