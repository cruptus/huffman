package serveur;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jeremie on 30/12/2016.
 */
public class Fenetre extends JFrame{

    private Serveur serveur;

    public Fenetre(){
        this.setTitle("Serveur Huffman");
        this.setResizable(false);
        this.setSize(300, 300);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(5,5));
        content.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));


        content.add(MyTextArea.getInstance(), BorderLayout.CENTER);
        this.setContentPane(content);
        this.pack();

        serveur = new Serveur(1919);
    }

    public static void main(String[] args) {
        new Fenetre();
    }
}
