package it.tommaso.planckbet.model;


import it.tommaso.planckbet.controller.CreaPartita;
import it.tommaso.planckbet.controller.FinePartita;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class CountDown extends Thread{
    private Semaphore iniziaCountdown;
    private Semaphore rilasciaVideo;
    private Semaphore generaVideo;
    private ArrayList<Button> buttons;
    private int time;
    private Label conta;
    private CreaPartita creaPartita;
    private ImageView immagine1;
    private ImageView immagine2;
    private FinePartita finePartita;
    private Semaphore bloccaScommesse;
    private boolean first;


    public  CountDown (Semaphore bloccaScommesse,ImageView immagine1, ImageView immagine2, CreaPartita creaPartita, Semaphore s1, Semaphore s2, Label conta, Semaphore generaVideo, ArrayList<Button> buttons, FinePartita finePartita) {
        super();
        this.bloccaScommesse = bloccaScommesse;
        iniziaCountdown = s1;
        this.immagine1 = immagine1;
        this.immagine2 = immagine2;
        rilasciaVideo = s2;
        this.creaPartita = creaPartita;
        first = true;
        this.generaVideo = generaVideo;
        this.finePartita = finePartita;
        this.conta = conta;
        this.buttons = buttons;
        time = 60;
        start();
    }
    private class Refresh extends TimerTask {
        private int time;
        private Label label;
        private ArrayList<Button> button;
        public Refresh (Label label,ArrayList<Button> button,int time) {
            this.label = label;
            this.button = button;
            this.time = time;
        }
        @Override
        public void run() {
            Platform.runLater(() -> {
                label.setText(Integer.toString(time));
            });
        }


    }
    private void modificaScommesse (boolean tipo) {
        for (Button button : buttons) {
            button.setDisable(tipo);
        }
    }
    public void run () {
        while (true) {
            try {
                iniziaCountdown.acquire();
                if (first) {
                    first = false;
                } else {
                    creaPartita.salvataggio();
                    finePartita.finePartita();
                    bloccaScommesse.acquire();
                }
                creaPartita.generaMatch();
                immagine1.setImage(new Image(creaPartita.generaImmagineSquadra1()));
                immagine2.setImage(new Image(creaPartita.generaImmagineSquadra2()));
                modificaScommesse(false);
                generaVideo.release();
                Timer action = new Timer();
                for (int i = 30; i >= 0; i--) {
                    action.schedule(new Refresh(conta,buttons,i), 1000*(30-i));
                }
                sleep(30000);
                modificaScommesse(true);
                bloccaScommesse.release();
                rilasciaVideo.release();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
