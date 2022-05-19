package it.tommaso.planckbet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UtentiRegistrati implements Serializable{
    private ArrayList<User> utenti;
    private class User implements Serializable {
        public String username;
        public String password;

        public String getPassword() {
            return password;
        }

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
    public UtentiRegistrati () {
        utenti = new ArrayList<User>();
    }
    public boolean aggiunta (String username,String password) {
        User tmp = new User(username, password);
        if (verificaUser(tmp)) {
            utenti.add(tmp);
            return  true;
        }
        return false;
    }

    private boolean verificaUser(User tmp) {
        for (User ver: utenti) {
            if (ver.username.equals(tmp.getUsername())) {
                return false;
            }
        }
        return true;
    }
    public boolean verificaLogin(String username, String password) {
        for (User ver: utenti) {
            if (ver.username.equals(username)) {
                if (password.equals(ver.password)) {
                    return  true;
                }
            }
        }
        return false;
    }
}
