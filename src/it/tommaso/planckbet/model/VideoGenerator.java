package it.tommaso.planckbet.model;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;


public class VideoGenerator extends Thread {

    private MediaPlayer mediaPlayer;
    private MediaView video;
    private Semaphore printVideo;
    private Semaphore generaVideo;
    private Semaphore finePartita;
    private Semaphore cambioScena;
    private Semaphore operazioniINcorso;
    private ArrayList<MediaPlayer> azioni;
    private Label risultato;
    private Scontro match;
    private ArrayList<String> risultati;
    private int azione;


    public VideoGenerator(MediaView video, Semaphore printVideo, Label risultato, Semaphore generaVideo, Semaphore finePartita, Scontro match) {
        this.video = video;
        this.operazioniINcorso = new Semaphore(0);
        this.printVideo = printVideo;
        this.risultato = risultato;
        this.generaVideo = generaVideo;
        this.finePartita = finePartita;
        this.match = match;
        this.risultati = new ArrayList<String>();
        this.setPriority(MAX_PRIORITY);
        start();
    }

    private ArrayList<MediaPlayer> createVideo() {
        try {
            risultati = new ArrayList<String>();
            String nomeCasa = match.getCasa();
            String nomeTrasferta = match.getTrasferta();
            int azioni = 4 + (int) Math.floor(Math.random() * 3);
            ArrayList<MediaPlayer> media = new ArrayList<>();
            String cartella = generaCartella();
            int casa = 0;
            int trasferta = 0;
            int random = 0;
            boolean espulsione = false;
            ArrayList<Integer> azioniPassate = new ArrayList<Integer>();
            for (int i = 0; i < azioni; i++) {
                random = 0 ;
                boolean esci = true;
                while (esci) {
                    esci = false;
                    random = 1 + (int) Math.floor(Math.random() * 29);
                    for (int verifica : azioniPassate) {
                        if (verifica == random) {
                            esci = true;
                            break;
                        }
                    }
                }
                azioniPassate.add(random);
                Media action = null;
                try {
                    File f = new File(cartella + random +".mp4");
                    if (f.exists()) {
                        action = new Media(f.toURI().toString());
                    } else {
                        f = new File(cartella + random + "_" + nomeCasa + ".mp4");
                        if (f.exists()) {
                            action = new Media(f.toURI().toString());
                            casa++;
                        } else {
                            f = new File(cartella + random + "_" + nomeTrasferta + ".mp4");
                            if (f.exists()) {
                                action = new Media(f.toURI().toString());
                                trasferta++;
                            } else {
                                f = new File(cartella + random + "_espulsione.mp4");
                                if (f.exists()) {
                                    action = new Media(f.toURI().toString());
                                    match.setEspulsione(true);
                                }
                            }
                        }
                    }
                    if (action == null) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                risultati.add(casa + " - " + trasferta);
                MediaPlayer tmp = new MediaPlayer(action);
                System.out.println(action.getSource());
                media.add(tmp);
            }
            match.setGoal(casa > 0 && trasferta > 0);
            match.setVittoria(casa > trasferta);
            match.setGoalCasa(casa);
            match.setGoalTransferta(trasferta);
            return media;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    public String generaCartella() {
        return "C:\\Users\\ztomm\\Desktop\\PlanckBet\\video\\" + (match.getCasa().compareTo(match.getTrasferta()) < 0 ? match.getCasa() + match.getTrasferta() : match.getTrasferta() + match.getCasa()) + "\\";
    }

    @Override
    public void run() {
        while (true) {
            try {
                generaVideo.acquire();
                azioni = createVideo();
                System.out.println(azioni);
                printVideo.acquire();
                playVideo(azioni);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
    }


    private boolean playVideo(ArrayList<MediaPlayer> azioni) {
        azione = 0;
        Semaphore streamVideo = new Semaphore(1);
        Timer timer = new Timer();
        for (MediaPlayer media : azioni) {
            try {
                streamVideo.acquire();
                if (azione != 0) {
                    int a = azione-1;
                    Platform.runLater(() -> risultato.setText(risultati.get(a)));
                }
                TimerTask playTheScene = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            video.setVisible(true);
                            video.setFitWidth(media.getMedia().getHeight());
                            video.setFitHeight(media.getMedia().getWidth());

                            video.setMediaPlayer(media);
                            media.setAutoPlay(true);
                            media.play();
                            media.setOnError(() -> {
                                System.out.println("media error"+media.getError().toString());
                                streamVideo.release();
                            });
                            media.setOnEndOfMedia(streamVideo::release);
                        });
                        System.out.println(media.autoPlayProperty());
                        video.setFitHeight(media.getMedia().getHeight());
                    }
                };
                timer.schedule(playTheScene,
                        200);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            azione++;
        }
        try {
            streamVideo.acquire();
            video.setVisible(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                video.setVisible(false);
                finePartita.release();
            }
        }, (long) 1000);
        return true;

    }
}
