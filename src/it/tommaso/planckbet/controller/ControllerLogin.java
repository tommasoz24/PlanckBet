package it.tommaso.planckbet.controller;

import it.tommaso.planckbet.model.Utente;
import it.tommaso.planckbet.model.UtentiRegistrati;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

public class ControllerLogin {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private CheckBox nuovo;

    private UtentiRegistrati utentiRegistrati;

    public String processResults() {
        try {
            importaFile();
            return verificaLogin();
        } catch (Exception e) {
            return null;
        }
    }

    private void importaFile() {
        try {
            FileInputStream f = new FileInputStream(new File("utentiLogin.txt"));
            ObjectInputStream o = new ObjectInputStream(f);
            UtentiRegistrati save = (UtentiRegistrati) o.readObject();
            o.close();
            f.close();
            if (save == null) {
                throw new FileNotFoundException();
            } else utentiRegistrati = save;
        } catch (FileNotFoundException e) {
            File f = new File("utentiLogin.txt");
            try {
                f.createNewFile();
                utentiRegistrati = new UtentiRegistrati();
            } catch (IOException ex) {
                Platform.exit();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("la classe inserita non è valida");
        }
    }

    private String verificaLogin() {

        if (nuovo.isSelected()) {
            if (utentiRegistrati.aggiunta(username.getText(),password.getText())) {
                esportaFIle();
                return username.getText()+ ".txt";
            }
        } else {
            if (utentiRegistrati.verificaLogin(username.getText(),password.getText())) {
                return username.getText() + ".txt";
            }
        }
        return null;
    }

    private void esportaFIle() {
        try {
            FileOutputStream f = new FileOutputStream(new File("utentiLogin.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(utentiRegistrati);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
