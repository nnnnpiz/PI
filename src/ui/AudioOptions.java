package ui;

import gamestates.Gamestate;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {

    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;

    public AudioOptions(){
        createSoundButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX=(int)(309* Game.SCALE);
        int vY=(int)(278*Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int)(450*Game.SCALE);
        int musicY = (int)(140*Game.SCALE);
        int sfxY = (int)(186*Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public void update(){
        musicButton.update();
        sfxButton.update();

        volumeButton.update();
    }

    public void draw(Graphics g){
        //Sound buttons (mb e sfx)
        musicButton.draw(g);
        sfxButton.draw(g);

        //VOLUME SLIDER
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e){
        //SLIDER:
        if(volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }

    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if(isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if(isIn(e,volumeButton))
            volumeButton.setMousePressed(true);
    }


    public void mouseReleased(MouseEvent e) {
        if(isIn(e, musicButton)) {
            if(musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
        } else if(isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if(isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if(isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }

    //parecido ao state class mas o pauseOverlay nnao é uma state class ent faço de novo
    private boolean isIn(MouseEvent e, PauseButton b){ //usamos PauseButton p darmos check em tds os botoes
        return (b.getBounds().contains(e.getX(),e.getY()));
    }

}
