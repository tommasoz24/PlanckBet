package it.tommaso.planckbet.controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import it.tommaso.planckbet.model.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;


public class Controller {

    @FXML
    private Button logout;
    @FXML
    private Image imgCasa;
    @FXML
    private Button login;
    @FXML
    private Label giocata;
    @FXML
    private Label risultato;
    @FXML
    private ImageView immagineCasa;
    @FXML
    private ImageView immagineOspiti;
    @FXML
    private Label vittorieCasa;
    @FXML
    private Label vittorieOspiti;
    @FXML
    private Label casaGoal;
    @FXML
    private Label ospitiGoal;
    @FXML
    private Label scontro1;
    @FXML
    private Label scontro2;
    @FXML
    private Label scontro3;
    @FXML
    private Label scontro4;
    @FXML
    private MediaView game;
    @FXML
    private Button piuGiocate1;
    @FXML
    private Button piuGiocate2;
    @FXML
    private Button piuGiocate3;
    @FXML
    private Button piuGiocate4;
    @FXML
    private Button piuGiocate5;
    @FXML
    private Button piuGiocate6;
    @FXML
    private Button piuGiocate7;
    @FXML
    private Button under1;
    @FXML
    private Button over1;
    @FXML
    private Button under2;
    @FXML
    private Button over2;
    @FXML
    private Button under3;
    @FXML
    private Button over3;
    @FXML
    private Button piuGiocate8;
    @FXML
    private Button multiGoal1;
    @FXML
    private ComboBox<String> multiCasa;
    @FXML
    private ComboBox<String> multiTot;
    @FXML
    private ComboBox<String> multiOspiti;
    @FXML
    private Button multiGoal2;
    @FXML
    private Button multiGoal3;
    @FXML
    private Label timerPartita;
    @FXML
    private ImageView immagineUtente;
    @FXML
    private Label nomeUtente;
    @FXML
    private Button statistiche;
    @FXML
    private Label saldo;
    @FXML
    private Button ricarica;
    @FXML
    private TextField importo;
    @FXML
    private Button scommetti;
    @FXML
    private TabPane scomemesse;
    @FXML
    private VBox prova;

    // semafori necessari
    private Semaphore generaVideo;
    private Semaphore fineVideo;
    private Semaphore printVideo;
    private Semaphore checkSchedina;
    // Attributi condivisi tra le classi
    private Scontro match;
    private Partita storico;
    private Utente utente;
    private ArrayList<String> schedine;

    // classi Principali
    private CreaPartita creaPartita;
    private FinePartita finePartita;
    private VideoGenerator videoGenerator;
    private CountDown countDown;


    // Arraylist grafici
    private ArrayList<Label> labelStatistiche;
    private ArrayList<Label> scontriPrecedenti;
    private ArrayList<Button> bottoniPiuGiocate;
    private ArrayList<Button> bottoniUnderOver;
    private ArrayList<Button> bottoniMultiGoal;
    private ArrayList<Button> bottoniSpeciali;
    private ArrayList<Button> bottoniScommesse;


    public void initialize() {
        labelStatistiche = generaLabelStatistiche();
        scontriPrecedenti = generaLabelScontriPrecedenti();
        bottoniPiuGiocate = generaBottoniPiuGiocate();
        bottoniUnderOver = generaBottoniUnderOver();
        bottoniMultiGoal = generaBottoniMultiGoal();
        bottoniSpeciali = generaBottoniSpeciale();
        bottoniScommesse = generaBottoniTotale();
        match = new Scontro();
        utente = new Utente("Ospite.txt");
        aggiornaUtente();
        generaVideo = new Semaphore(0);
        fineVideo = new Semaphore(1);
        printVideo = new Semaphore(0);
        checkSchedina = new Semaphore(0);
        storico = new Partita();
        schedine = new ArrayList<String>();
        generaMultiGoal();
        creaPartita = new CreaPartita(match, labelStatistiche, scontriPrecedenti, storico);
        finePartita = new FinePartita(utente, match,labelStatistiche,saldo,schedine,risultato,scontriPrecedenti,nomeUtente);
        videoGenerator = new VideoGenerator(game, printVideo, risultato, generaVideo, fineVideo, match);
        countDown = new CountDown(checkSchedina,immagineCasa,immagineOspiti,creaPartita, fineVideo, printVideo, timerPartita, generaVideo, bottoniScommesse,finePartita);
    }




    private void generaMultiGoal() {
        String multiCasaText[] = {
                "1-2 : 1.80",
                "1-3 : 1.50",
                "1-4 : 1.10",
                "2-3 : 2.20",
                "2-4 : 2.00",
        };
        String multiOspite[] = {
                "1-2 : 1.80",
                "1-3 : 1.50",
                "1-4 : 1.10",
                "2-3 : 2.20",
                "2-4 : 2.00",
        };
        String multi[] = {
                "1-2 : 2.20",
                "1-3 : 1.60",
                "1-4 : 1.20",
                "2-3 : 2.10",
                "2-4 : 1.80",
        };
        multiCasa.setItems(FXCollections.observableArrayList(multiCasaText));
        multiOspiti.setItems(FXCollections.observableArrayList(multiOspite));
        multiTot.setItems(FXCollections.observableArrayList(multi));
    }

    private ArrayList<Label> generaLabelStatistiche() {
        ArrayList<Label> label = new ArrayList<Label>();
        label.add(vittorieCasa);
        label.add(vittorieOspiti);
        label.add(casaGoal);
        label.add(ospitiGoal);
        return label;
    }

    private ArrayList<Label> generaLabelScontriPrecedenti() {
        ArrayList<Label> label = new ArrayList<Label>();
        label.add(scontro1);
        label.add(scontro2);
        label.add(scontro3);
        label.add(scontro4);
        return label;
    }

    private ArrayList<Button> generaBottoniSpeciale() {
        return null;
    }

    private ArrayList<Button> generaBottoniMultiGoal() {
        ArrayList<Button> bottoni = new ArrayList<Button>();
        bottoni.add(multiGoal1);
        bottoni.add(multiGoal2);
        bottoni.add(multiGoal3);
        return bottoni;
    }

    private ArrayList<Button> generaBottoniUnderOver() {
        ArrayList<Button> bottoni = new ArrayList<Button>();
        bottoni.add(under1);
        bottoni.add(under2);
        bottoni.add(under3);
        bottoni.add(over1);
        bottoni.add(over2);
        bottoni.add(over3);
        return bottoni;
    }

    private ArrayList<Button> generaBottoniPiuGiocate() {
        ArrayList<Button> bottoni = new ArrayList<Button>();
        bottoni.add(piuGiocate1);
        bottoni.add(piuGiocate2);
        bottoni.add(piuGiocate3);
        bottoni.add(piuGiocate4);
        bottoni.add(piuGiocate5);
        bottoni.add(piuGiocate6);
        bottoni.add(piuGiocate7);
        bottoni.add(piuGiocate8);
        return bottoni;
    }


    private ArrayList<Button> generaBottoniTotale() {
        ArrayList<Button> bottoni = new ArrayList<Button>();
        bottoni.add(piuGiocate1);
        bottoni.add(piuGiocate2);
        bottoni.add(piuGiocate3);
        bottoni.add(piuGiocate4);
        bottoni.add(piuGiocate5);
        bottoni.add(piuGiocate6);
        bottoni.add(piuGiocate7);
        bottoni.add(piuGiocate8);
        bottoni.add(multiGoal1);
        bottoni.add(multiGoal2);
        bottoni.add(multiGoal3);
        bottoni.add(under1);
        bottoni.add(under2);
        bottoni.add(under3);
        bottoni.add(over1);
        bottoni.add(over2);
        bottoni.add(over3);
        return bottoni;
    }

    public void aggiungiScommessa(Event e) {
        Button tmp = (Button) e.getSource();
        String text = "MULTIGOAL ";
        if (tmp.getText().equals("CONFERMA")) {
            String tx = tmp.getId();
            if (tx.equals("multiGoal1")) {
                text+= "CASA ";
                text += multiCasa.getSelectionModel().getSelectedItem();
                multiCasa.getSelectionModel().clearSelection();
            } else  if (tx.equals("multiGoal2")) {
                text+= "TRASFERTA ";
                text +=  multiOspiti.getValue();
                multiOspiti.getSelectionModel().clearSelection();
            } else {
                text +=  multiTot.getValue();
                multiTot.getSelectionModel().clearSelection();
            }
        } else text = tmp.getText();
        giocata.setText(text);
        giocata.setVisible(true);

    }

    public void scommetti(Event e) {
        String text = " schedina giocata con successo";
        if (checkSchedina.availablePermits() != 0 ) {
            giocata.setText(" ");
            giocata.setVisible(false);
            text = "Le schedine sono chiuse";
        } else {
            try {
                double tot = Double.parseDouble(importo.getText());
                if (!giocata.isVisible()) {
                    text = " devi prima inserire selezionare una scommessa";
                } else {
                    if (tot < 1) {
                        text = "La quota minima che si può giocare è 1 €";
                    } else if (Double.parseDouble(saldo.getText()) < tot) {
                        text = "il saldo è insufficiente";
                    } else {
                        saldo.setText(Double.toString(Double.parseDouble(saldo.getText()) - tot));
                        utente.setConto(Double.parseDouble(saldo.getText()));
                        String add = giocata.getText() + " : " + tot;
                        schedine.add(add);
                        giocata.setText(" ");
                        giocata.setVisible(false);
                    }
                }

            } catch (Exception er) {
                text = "la schedina non è stata giocata";
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.setTitle("esito");
        alert.show();
        aggiornaUtente();

    }

    public void ricaricaProfilo(MouseEvent mouseEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(prova.getScene().getWindow());
        dialog.setTitle("Ricarica conto");
        dialog.setHeaderText("Dialogo per ricaricare il conto");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Platform.runLater(() -> {
            double ricarica = 0;
            fxmlLoader.setLocation(getClass().getResource("../view/ricaricaDialog.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());

            } catch (IOException e) {
                return;
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ControllerRicarica controller = fxmlLoader.getController();
                ricarica = controller.processResults();
            } else {
                return;
            }
            String text = "ricarica effeettuata con successo";
            if (ricarica > 0) {
                double conto = utente.getConto() + ricarica;
                utente.setConto(conto);
                saldo.setText(Double.toString(conto));
                utente.esportaDati();
            } else text = " ricarica non effettuata";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(text);
            alert.setTitle("Esito ricarica");
            alert.show();

        });

    }

    public void visualizzaStatistiche() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(utente.visualizzaStatistiche());
        alert.setTitle("Statistiche utente");
        alert.show();
    }

    public void aggiornaUtente() {
        nomeUtente.setText(utente.getNome());
        saldo.setText(Double.toString(utente.getConto()));
        utente.esportaDati();
    }

    public void loginPage(MouseEvent mouseEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(prova.getScene().getWindow());
        dialog.setTitle("LoginPage");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Platform.runLater(() -> {
            String nomeFIle;
            fxmlLoader.setLocation(getClass().getResource("../view/Login.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());

            } catch (IOException e) {
                return;
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ControllerLogin controller = fxmlLoader.getController();
                 nomeFIle = controller.processResults();
            } else {
                return;
            }
            if (nomeFIle != null) {
                utente.rigenera(nomeFIle);
                aggiornaUtente();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("utente gia esistente o credenziali inserite errate");
                alert.setTitle("Errore nel login");
                alert.show();
            }

        });

    }

    public void logout(MouseEvent mouseEvent) {
        if (utente.getNomeFIle().compareTo("Ospite.txt") != 0) {
            utente.rigenera("Ospite.txt");
            aggiornaUtente();
        }
    }
}
