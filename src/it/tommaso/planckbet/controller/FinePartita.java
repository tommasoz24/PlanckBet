package it.tommaso.planckbet.controller;

import it.tommaso.planckbet.model.Schedina;
import it.tommaso.planckbet.model.Scontro;
import it.tommaso.planckbet.model.Utente;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class FinePartita {
    private Utente utente;
    private Scontro match;
    private ArrayList<Label> statistiche;
    private ArrayList<String> scommesse;
    private Label conto;
    private Label risultato;
    private ArrayList<Label> scontriPrecedenti;
    private Label saldo;
    private Label nomeUtente;
    private VBox prova;
    public FinePartita (Utente utente, Scontro match,ArrayList<Label> statistiche,Label conto, ArrayList<String> scommesse,Label risultato,ArrayList<Label> scontriPrecedenti,Label nomeUtente) {
        this.utente = utente;
        this.match = match;
        this.statistiche = statistiche;
        this.scommesse = scommesse;
        this.scontriPrecedenti = scontriPrecedenti;
        this.risultato = risultato;
        this.conto = conto;
        this.nomeUtente = nomeUtente;
        this.prova = prova;
    }

    public void finePartita() {
        calcolaEaggiornaRisultato();
        inizializeStats();
        cleanOtherItem();
    }



    private void cleanOtherItem() {
        Platform.runLater(() -> {
            risultato.setText("0 - 0");
            scommesse.clear();
            for (Label st: scontriPrecedenti) {
                st.setText(" ");
                st.setVisible(false);
            }
        });
    }

    private void calcolaEaggiornaRisultato() {
        ArrayList<Schedina> schedinePassate = utente.getSchedinePassate();
        double tot = 0;
        for (String scommessa: scommesse) {
            Schedina s = creaSchedina(scommessa);
            if (s.verificaVittoria()) {
                tot+=s.getQuota() * Double.parseDouble(s.getImporto());
            }
            schedinePassate.add(s);
        }
        utente.setSchedinePassate(schedinePassate);
        utente.setConto(utente.getConto()+tot);
        Platform.runLater(() -> {
            conto.setText(utente.getNome());
            conto.setText(Double.toString(utente.getConto()));
        });
        utente.esportaDati();
    }

    private void inizializeStats() {
        for (Label st: statistiche) {
            Platform.runLater(() -> st.setText(st.getText().split(" : ")[0]));
        }
    }

    public Schedina creaSchedina (String schedina) {
       String[] valori = schedina.split(" : ");
       return new Schedina (valori[0], Double.parseDouble(valori[1]),valori[2],match);
    }

}
