package serveur;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jeremie on 30/12/2016.
 */
public class MyTextArea extends JTextArea{

    private static MyTextArea instance = null;

    private MyTextArea(){
        super();
        this.setPreferredSize(new Dimension(290, 290));
        this.setEditable(false);
        this.setVisible(true);
        this.setAutoscrolls(true);
    }

    public static MyTextArea getInstance(){
        if(instance == null)
            instance = new MyTextArea();
        return instance;
    }

    public static void setTexte(String texte){
        instance.setText(instance.getText() + texte + "\n");
    }
}
