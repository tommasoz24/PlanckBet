package it.tommaso.planckbet.model;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Schedina implements Serializable {
    private String scommessa;
    private Scontro match;
    private double quota;
    private String importo;
    private boolean vittoria;
    public Schedina(String scommessa, double quota, String importo, Scontro match) {
        this.scommessa = scommessa;
        this.quota = quota;
        this.match = match;
        this.importo = importo;
        this.vittoria = verificaVittoria();

    }

    public String getScommessa() {
        return scommessa;
    }

    public void setScommessa(String scommessa) {
        this.scommessa = scommessa;
    }

    public double getQuota() {
        return quota;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }

    public String getImporto() {
        return importo;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }
    public String toString () {
        String esito = vittoria ? " vincente" : " perdente ";
        return "partita giocata: " + match.getCasa() + " vs " + match.getTrasferta() + " scomessa : " + scommessa + " risultato finale: " + match.getGoalCasa() + " : " + match.getGoalTransferta() + "  esito:  " + esito;
    }

    public boolean verificaVittoria () {
        ArrayList<String> vincenti = schedineVincenti();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\t\tRisultato : "+ match.getGoalCasa() + " - " + match.getGoalTransferta());
            alert.setContentText( " \n\nSchedina giocata dall'utente: " + scommessa + "\nEsito:"  + (vincenti.contains(scommessa) ? " Vincente " : " Perdente"));
            alert.setTitle("Esito schedina");
            alert.show();
        });
        return vincenti.contains(scommessa);
    }

    private ArrayList<String> schedineVincenti() {
        ArrayList<String> vincenti = new ArrayList<String>();
        int casa = match.getGoalCasa();
        int transferta = match.getGoalTransferta();
        System.out.println(casa);
        System.out.println(transferta);
        // verifica piu giocate
        if (casa > transferta) {
            vincenti.add("1");
            vincenti.add("12");
            vincenti.add("1X");
        } else if (casa < transferta) {
            vincenti.add("2");
            vincenti.add("12");
            vincenti.add("1X");

        } else {
            vincenti.add("X");
            vincenti.add("1X");
            vincenti.add("X2");
        }
        if (casa > 0 && transferta > 0) {
            vincenti.add("GOAL");
        } else vincenti.add("NOGOAL");
        // verifica multigoal casa  1 -2 1 -3 1 4 2-3 2-4
        if (casa > 0) {
            if ( casa <= 2) {
                vincenti.add("MULTIGOAL CASA 1-2");
            }
            if ( casa <= 3) {
                vincenti.add("MULTIGOAL CASA 1-3");
            }
            if ( casa <= 4) {
                vincenti.add("MULTIGOAL CASA 1-4");
            }
            if (casa > 1 && casa <= 3) {
                    vincenti.add("MULTIGOAL CASA 2-3");
            }
            if (casa > 1 && casa < 4) {
                vincenti.add("MULTIGOAL CASA 2-4");
            }
        }
        if (transferta > 0) {
            if ( transferta <= 2) {
                vincenti.add("MULTIGOAL TRASFERTA 1-2");
            }
            if ( transferta <= 3) {
                vincenti.add("MULTIGOAL TRASFERTA 1-3");
            }
            if ( transferta <= 4) {
                vincenti.add("MULTIGOAL TRASFERTA 1-4");
            }
            if (transferta > 1 && transferta <= 3) {
                vincenti.add("MULTIGOAL TRASFERTA 2-3");
            }
            if (transferta > 1 && transferta < 4) {
                vincenti.add("MULTIGOAL TRASFERTA 2-4");
            }
        }
        int tot = casa + transferta;
        if (tot > 0) {
            if ( tot <= 2) {
                vincenti.add("MULTIGOAL 1-2");
            }
            if ( tot <= 3) {
                vincenti.add("MULTIGOAL 1-3");
            }
            if ( tot <= 4) {
                vincenti.add("MULTIGOAL 1-4");
            }
            if (tot > 1 && tot <= 3) {
                vincenti.add("MULTIGOAL 2-3");
            }
            if (tot > 1 && tot < 4) {
                vincenti.add("MULTIGOAL 2-4");
            }
        }
        if (tot > 1) {
            vincenti.add("OVER 1.5");
            if (tot > 2) {
                vincenti.add("OVER 2.5");
                if (tot > 3 ) {
                    vincenti.add("OVER 3.5");
                } else vincenti.add("UNDER 3.5");

            } else {
                vincenti.add("UNDER 2.5");
            }
        } else {
            vincenti.add("UNDER 1.5");
        }
        System.out.println("schedine vincenti");
        for (String timp: vincenti) {
            System.out.println(timp);
        }
        return vincenti;
    }
}
