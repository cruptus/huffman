package serveur;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jeremie on 07/11/2016.
 */
public class Serveur {

    private ServerSocket server;

    public Serveur(int port){
        try {
            server = new ServerSocket(port);
            MyTextArea.setTexte("Serveur start : "+server.getLocalSocketAddress());
            attenteConnexion();
        }catch (Exception e){
            MyTextArea.setTexte("Erreur Server : Connexion impossible");
            e.printStackTrace();
        }
    }

    public void attenteConnexion(){
        try {
            MyTextArea.setTexte("En attente de connexion");
            System.out.println();
            while (true) {
                Socket socket = server.accept();
                new Service(socket).run();
            }
        }catch (Exception e){
            try {
                server.close();
            }catch (IOException ioe){
                MyTextArea.setTexte("Erreur Server : Impossible de fermer le serveur");
                ioe.printStackTrace();
            }
            MyTextArea.setTexte("Erreur Server : Connexion client impossible");
            e.printStackTrace();
        }
    }
}