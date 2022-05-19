package it.tommaso.planckbet.controller;

import it.tommaso.planckbet.model.Partita;
import it.tommaso.planckbet.model.Scontro;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;

public final class CreaPartita implements Serializable {
    private String squadra1;
    private String squadra2;
    private String nomeFile;
    private Scontro match;
    private Partita storico;
    private ArrayList<Label> statistiche;
    private ArrayList<Label> scontriPrecedenti;
    public CreaPartita (Scontro match,ArrayList<Label> statistiche,ArrayList<Label> scontrPrecedenti,Partita storico) {
        super();
        this.match = match;
        this.statistiche = statistiche;
        this.storico = storico;
        this.scontriPrecedenti = scontrPrecedenti;
    }

    @Override
    public String toString() {
        return "CreaPartita{" +
                "squadra1='" + squadra1 + '\'' +
                ", squadra2='" + squadra2 + '\'' +
                ", nomeFile='" + nomeFile + '\'' +
                ", match=" + match +
                ", storico=" + storico +
                ", statistiche=" + statistiche +
                ", scontriPrecedenti=" + scontriPrecedenti +
                '}';
    }

    public void generaMatch () {
        try {
            generaSquadre();
            match.setCasa(squadra1);
            match.setTrasferta(squadra2);
            System.out.println(match);
            Partita tmp  = creaPartitaFile();
            System.out.println("uella" + tmp);
            if (tmp == null) {
                tmp = new Partita();

            } else {
                storico.setEspulsioni(tmp.getEspulsioni());
                storico.setStats(tmp.getStats());
                storico.setTotale(storico.getTotale());
                storico.setScontriPrecedenti(tmp.getScontriPrecedenti());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        generaStatistiche();
        generaPrecedenti();
    }

    private Partita creaPartitaFile() {
        try {
            FileInputStream f = new FileInputStream(new File(nomeFile));
            ObjectInputStream o = new ObjectInputStream(f);
            Partita save = (Partita) o.readObject();
            System.out.println("save "  + save);
            o.close();
            f.close();
            if (save == null) {
                throw new FileNotFoundException();
            } else return save;
        } catch (FileNotFoundException e) {
            File f = new File(this.nomeFile);
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Platform.exit();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("la classe inserita non è valida");
        }
        return null;
    }

   private void generaPrecedenti() {
        ArrayList<Scontro> sc = storico.getScontriPrecedenti();
        for (int i = 0; i < 5; i++) {
            try {
                String text = " ";
                Label s = scontriPrecedenti.get(i);
                try {
                    text = sc.get(sc.size() - i -1 ).toString();
                } catch (Exception e) {
                    s.setVisible(false);
                    continue;
                }
                String finalText = text;
                Platform.runLater(() -> {
                    s.setText(finalText);
                    s.setVisible(true);
                });
            } catch (Exception e) {
                return;
            }
        }
    }

    private int [] generaQuote() {
        int [] quote = new int[10];
        int totale = storico.getTotale();
        // vittoria casa

        return null;
    }

    public String generaImmagineSquadra1 () {
        return "file:src/it/tommaso/planckbet/model/image/" +squadra1 + ".png";
    }
    public String generaImmagineSquadra2 () {
        return "file:src/it/tommaso/planckbet/model/image/" +squadra2 + ".png";
    }
    private void generaStatistiche() {
        int[] valori = {storico.getStats().getVittorie(), storico.getStats().getSconfitte(), storico.getStats().getGoalSegnati(), storico.getStats().getGoalSubiti()};
        if (squadra1.compareTo(squadra2) < 0) {
            int tmp = valori[0];
            valori[0] = valori[1];
            valori[1] = tmp;
            tmp = valori[2];
            valori[2] = valori[3];
            valori[3] = tmp;
        }
        for (int i = 0; i < 4; i++) {
            try {
                String text = statistiche.get(i).getText().split(":")[0];
                text += ":   ";
                text += valori[i];
                if (i == 2) {
                    text += " / " + valori[i + 1];
                } else if (i == 3) {
                    text += " / " + valori[i - 1];
                }
                Label l = statistiche.get(i);
                String print = text;
                Platform.runLater(() -> {
                    l.setVisible(true);
                    l.setText(print);
                });
            } catch (Exception e) {
                System.out.println("errore "+ e.getMessage());
            }
        }
    }
    public void generaSquadre () {
        squadra1 = generaSquadra("null");
        squadra2 = generaSquadra(squadra1);
        nomeFile = generaFile();
    }

    private String generaFile() {
        return (squadra1.compareTo(squadra2) > 0 ? squadra1+squadra2 : squadra2+squadra1) + ".txt";
    }


    private String generaSquadra(String squadraNot) {
        String squadra;
        while (true) {
            int rand = (int) Math.floor(Math.random() * 5);
            switch (rand) {
                case 0:
                    squadra = "juventus";
                    break;
                case 1:
                    squadra = "inter";
                    break;
                case 2:
                    squadra = "roma";
                    break;
                case 3:
                    squadra = "napoli";
                    break;
                case 4:
                    squadra = "milan";
                    break;
                default: squadra = null;
            }
            if (!squadra.equals(squadraNot)) {
                return squadra;
            }
        }
    }

    public void salvataggio() {
        try {
            storico.setEspulsioni(storico.getEspulsioni() + (match.isEspulsione() ? 1 : 0));
            storico.setTotale(storico.getTotale() +1);
            if (squadra1.compareTo(squadra2) > 0 ) {
                aggiungiStatistiche(match.getGoalCasa(),match.getGoalTransferta());
            } else aggiungiStatistiche(match.getGoalTransferta(), match.getGoalCasa());
            storico.nuovoScontro(match);
            FileOutputStream f = new FileOutputStream(new File(nomeFile));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(storico);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void aggiungiStatistiche(int goalCasa, int goalTransferta) {
        storico.getStats().setGoalSegnati(storico.getStats().getGoalSegnati() + goalCasa);
        storico.getStats().setGoalSubiti(storico.getStats().getGoalSubiti() + goalTransferta);
        if (goalCasa > goalTransferta) {
            storico.getStats().setVittorie(storico.getStats().getVittorie()+ 1 );
        } else  if (goalTransferta > goalCasa) storico.getStats().setSconfitte(storico.getStats().getSconfitte()+ 1 );


    }
}
