package it.tommaso.planckbet.model;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;

public class Utente implements Serializable {
    private static final long serialVersionUID = 1113799434508676095L;
    private String nome;
    private double conto;
    private ArrayList <Schedina> schedinePassate;
    private String nomeFIle;

    public Utente (String nomeFIle) {
        this.nome = nomeFIle.split(".txt")[0];
        this.nomeFIle = nomeFIle;
        this.conto = 100;
        this.schedinePassate = new ArrayList<Schedina>();
        if(!nomeFIle.equals("Ospite.txt")) {
            caricaDati();
        }
    }

    public String getNomeFIle() {
        return nomeFIle;
    }

    public void setNomeFIle(String nomeFIle) {
        this.nomeFIle = nomeFIle;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getConto() {
        return conto;
    }

    public void setConto(double conto) {
        this.conto = conto;
    }

    public ArrayList<Schedina> getSchedinePassate() {
        return schedinePassate;
    }

    public void setSchedinePassate(ArrayList<Schedina> schedinePassate) {
        this.schedinePassate = schedinePassate;
    }

    public void stampaSchedinePassate () {

    }
    public String  risultati () {
        int vittorie = 0,sconfitte = 0;
        for (Schedina sc : schedinePassate) {
            if (sc.verificaVittoria()) {
                vittorie++;
            } else sconfitte++;
        }
        return "Utente :" + nome + "schedine giocate: " + (vittorie + sconfitte) + "  vittorie: " + vittorie + " sconfitte" + sconfitte;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", conto=" + conto +
                ", schedinePassate=" + schedinePassate +
                ", nomeFIle='" + nomeFIle + '\'' +
                '}';
    }
    public void rigenera(String nomeFIle) {
        this.nome = nomeFIle.split(".txt")[0];
        this.nomeFIle = nomeFIle;
        this.conto = 100;
        this.schedinePassate = new ArrayList<Schedina>();
        if(!nomeFIle.equals("Ospite.txt")) {
            caricaDati();
        }
    }
    private void caricaDati() {
        Utente tmp = leggiDaFile();
            if (tmp == null) {
                esportaDati();
                return;
            }

            nome = tmp.getNome();
            conto = tmp.getConto();
            schedinePassate = tmp.getSchedinePassate();
    }

    public Utente leggiDaFile() {
        try {
            FileInputStream f = new FileInputStream(new File(nomeFIle));
            ObjectInputStream o = new ObjectInputStream(f);
            Utente save = (Utente) o.readObject();
            o.close();
            f.close();
            if (save == null) {
                throw new FileNotFoundException();
            } else return save;
        } catch (FileNotFoundException e) {
            File f = new File(this.nomeFIle);
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
    public void esportaDati() {
        try {
            FileOutputStream f = new FileOutputStream(new File(nomeFIle));
            ObjectOutputStream o = new ObjectOutputStream(f);
            Utente t = this;
            o.writeObject(t);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void aggiungiSchedine (Schedina schedina) {
        schedinePassate.add(schedina);
        if (schedina.verificaVittoria()) {
            conto+= (double) schedina.getQuota() * Double.parseDouble(schedina.getImporto());
        }
    }
    public boolean  aggiornaConto (double scala) {
        if (conto - scala > 0) {
            conto-= scala;
            return true;
        }
        return false;
    }
    public void ricarica (double ricarica) {
        conto += ricarica;
    }
    public String visualizzaStatistiche () {
        if (schedinePassate.size() == 0) {
            return " nessuna scommessa effettuata";
        }
        StringBuilder stampa = new StringBuilder(" -- Tutte le schedine effettuate -- \n");
        for ( Schedina schedina: schedinePassate) {
            stampa.append(schedina).append(" \n");
        }
        return stampa.toString();
    }
}
