package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jeremie on 30/12/2016.
 */
public class Fenetre extends JFrame{

    private Client client;
    private JPanel content;

    public Fenetre(){
        this.setTitle("Client Huffman");
        this.setResizable(false);
        this.setSize(300, 300);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        content = new JPanel();

        init();

        this.setContentPane(content);
        this.pack();

        this.client = new Client("localhost", 1919);
    }

    private void init(){
        content.setLayout(new BorderLayout(5,5));
        content.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

        JButton button_LIST = new JButton("Get Files");
        button_LIST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getAllFile();
            }
        });
        content.add(button_LIST, BorderLayout.NORTH);
        content.add(MySelectBox.getInstance(), BorderLayout.CENTER);
        JButton button_ADD = new JButton("Download File");
        button_ADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getFile(MySelectBox.itemSelect());
            }
        });
        content.add(button_ADD, BorderLayout.SOUTH);
    }


    public static void main(String[] args) {
        new Fenetre();
    }
}
