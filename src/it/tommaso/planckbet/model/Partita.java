package it.tommaso.planckbet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Partita implements Serializable {
    private static final long serialVersionUID = 1113799434508676095L;
    private ArrayList<Scontro> scontriPrecedenti;
    private int espulsioni;
    private int totale;
    private Statistiche stats;
    public Partita () {
        scontriPrecedenti = new ArrayList<Scontro>();
        espulsioni = 0;
        totale = 0;
        stats = new Statistiche();

    }
    public ArrayList<Scontro> getScontriPrecedenti() {
        return scontriPrecedenti;
    }

    public void setScontriPrecedenti(ArrayList<Scontro> scontriPrecedenti) {
        this.scontriPrecedenti = scontriPrecedenti;
    }

    public int getEspulsioni() {
        return espulsioni;
    }

    public void setEspulsioni(int espulsioni) {
        this.espulsioni = espulsioni;
    }

    public int getTotale() {
        return totale;
    }

    public void setTotale(int totale) {
        this.totale = totale;
    }

    public Statistiche getStats() {
        return stats;
    }

    public void setStats(Statistiche stats) {
        this.stats = stats;
    }


    public class Statistiche implements Serializable{
        private int vittorie;
        private int sconfitte;
        private int goalSegnati;
        private int goalSubiti;
        public void inizialize() {
            vittorie = 0;
            sconfitte = 0;
            goalSegnati = 0;
            goalSubiti = 0;
        }
        public Statistiche() {
            inizialize();
        }

        public int getVittorie() {
            return vittorie;
        }

        public void setVittorie(int vittorie) {
            this.vittorie = vittorie;
        }

        public int getSconfitte() {
            return sconfitte;
        }

        public void setSconfitte(int sconfitte) {
            this.sconfitte = sconfitte;
        }

        public int getGoalSegnati() {
            return goalSegnati;
        }

        public void setGoalSegnati(int goalSegnati) {
            this.goalSegnati = goalSegnati;
        }

        public int getGoalSubiti() {
            return goalSubiti;
        }

        public void setGoalSubiti(int goalSubiti) {
            this.goalSubiti = goalSubiti;
        }

    }
    public void nuovoScontro(Scontro scontro) {
        scontriPrecedenti.add(scontro);
    }

    @Override
    public String toString() {
        return "Partita{" +
                "scontriPrecedenti=" + scontriPrecedenti +
                ", espulsioni=" + espulsioni +
                ", totale=" + totale +
                ", stats=" + stats +
                '}';
    }
}
