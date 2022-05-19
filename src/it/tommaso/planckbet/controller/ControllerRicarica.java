package it.tommaso.planckbet.controller;

import javafx.scene.control.TextField;

import java.time.LocalDate;

public class ControllerRicarica {
    public TextField ricarica;
    public double processResults() {
        try {
            Double ricarica = Double.parseDouble(this.ricarica.getText());
            if (ricarica > 0) {
                return ricarica;
            } else throw new Exception();
        } catch (Exception e) {
            return -1;
        }
    }
}
