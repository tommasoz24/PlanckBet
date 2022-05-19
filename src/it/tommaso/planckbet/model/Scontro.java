package it.tommaso.planckbet.model;

import java.io.Serializable;

public class Scontro implements Serializable {
    private String casa;
    private String trasferta;
    private boolean espulsione;
    private boolean goal;
    private boolean vittoria;
    private int goalCasa;
    private int goalTransferta;

    public String getTrasferta() {
        return trasferta;
    }

    public void setTrasferta(String trasferta) {
        this.trasferta = trasferta;
    }

    public boolean isEspulsione() {
        return espulsione;
    }

    public void setEspulsione(boolean espulsione) {
        this.espulsione = espulsione;
    }

    public boolean isGoal() {
        return goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }

    public boolean isVittoria() {
        return vittoria;
    }

    public void setVittoria(boolean vittoria) {
        this.vittoria = vittoria;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public int getGoalCasa() {
        return goalCasa;
    }

    public void setGoalCasa(int goalCasa) {
        this.goalCasa = goalCasa;
    }

    public int getGoalTransferta() {
        return goalTransferta;
    }

    public void setGoalTransferta(int goalTransferta) {
        this.goalTransferta = goalTransferta;
    }

    public Scontro() {
        super();
    }

    public String toString () {
        return casa + "  " + goalCasa + " : " + goalTransferta + "  " +trasferta;
    }
}
