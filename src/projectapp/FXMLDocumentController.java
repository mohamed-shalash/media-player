/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectapp;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author xps
 */
public class FXMLDocumentController implements Initializable {
    static int songselectednum=0; 
    static List<File> files;
    static Media mediaa;
    static MediaPlayer mediaview;
    static boolean play=false,selected=false,change=false;
    @FXML
    private ImageView con;
    @FXML
    private ImageView wait;
    @FXML
    private ListView list;
    @FXML
    private Slider sliderv;
    @FXML
    private Slider sliderm;
    @FXML
    private Label totalDuration;
    @FXML
    private Label currentDuration;
    @FXML
    private Label artistName;
    @FXML
    private Label albumName;
    private Stage stage;
    @FXML
    private void changing() {
        mediaview.seek(Duration.seconds(sliderm.getValue()));
    }
    @FXML
    private void volume() {
        mediaview.setVolume(sliderv.getValue()/100);
    }
    @FXML
    private void closebtn() {
        stage=Projectapp.getStage();
        stage.close();
    } 
    @FXML
    private void minimized() {
        stage=Projectapp.getStage();
        stage.setIconified(true);
    }  
    
    @FXML
    private void choosefile() {
        selected=true;
        change=true;
        FileChooser fc =new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\xps\\Downloads\\Music"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp3 files","*.mp3"));
        File file =fc.showOpenDialog(null);
        if (file !=null) {
            list.getItems().clear();
            list.getItems().add(file.getAbsolutePath());
        }else{
            System.out.println("file not valied");
        }
    }
    @FXML
    private void choosefiles() {
        change=true;
        selected=true;
        FileChooser fc =new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\xps\\Downloads\\Music"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp3 files","*.mp3"));
        files =fc.showOpenMultipleDialog(null);
        if (files !=null) {
            list.getItems().clear();
            for (int i = 0; i < files.size(); i++)
                list.getItems().add(files.get(i).getAbsolutePath());
        }else{
            System.out.println("file not valied");
        }
            
    }
    @FXML
    private void constop() {
        if(selected){
            if(change){
                artistName.setText(files.get(songselectednum).toURI().toString());
                String arr[]=files.get(songselectednum).toURI().toString().split("/");
                albumName.setText(arr[arr.length-1]);
                mediaa =new Media(files.get(songselectednum).toURI().toString());
                mediaview =new MediaPlayer(mediaa);
                change=false;
                mediaview.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    sliderm.setValue(newValue.toSeconds());
                    currentDuration.setText(((int)newValue.toMinutes()%60)+":"+((int)newValue.toSeconds()%60)+"");
                    }
                });
                mediaview.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration total =mediaa.getDuration();
                    sliderm.setMax(total.toSeconds());
                    totalDuration.setText(((int)total.toMinutes()%60)+":"+((int)total.toSeconds()%60)+"");
                    }});
                sliderv.setValue(mediaview.getVolume()*100);
                sliderv.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                        mediaview.setVolume(sliderv.getValue()/100);
                    }
                });
        
                }
            if(!play){
                mediaview.play();
                play =true;
            }
            else{
                mediaview.pause();
                play =false;
            }
            }
    }
    @FXML
    private void nextvedio() {
        if(selected ==true && songselectednum < files.size() ){
            mediaview.pause();
            play =false;
            change=true;
             ++songselectednum;
            }
    }
    @FXML
    private void previousvedio() {
        if(songselectednum > 0 && selected){
                 mediaview.pause();
                play =false;
                change=true;
                --songselectednum;
            }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
