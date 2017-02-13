/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class Buzzer extends Application {
    private final String mp3 = "buzz.mp3";

    private Media media;
    private MediaPlayer player;
    public Buzzer() {
        media = new Media(Paths.get(mp3).toUri().toString());
        player = new MediaPlayer(media);
    }
    
    public void buzz(){
        player.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
       
    }
    
    
    
}
