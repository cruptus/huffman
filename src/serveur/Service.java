package serveur;

import huffman.Huffman;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Service extends Thread {
    private static String path = System.getProperty("user.dir")+ "/texte/";
    private Socket socket;

    private File[] files;
    private BufferedReader in;
    private BufferedWriter out;

    public Service(Socket socket){
        this.socket = socket;
        File folder = new File(path);
        files = folder.listFiles();
        MyTextArea.setTexte("Nouvelle connexion : "+socket.getLocalSocketAddress());

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        waitMessage();
    }

    private void sendListFile() {
        try {
            for (File file : files) {
                out.write(file.getName());
                out.newLine();
                out.flush();
            }
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendFile(){
        try{
            String name = in.readLine();
            for(File file : files){
                if(file.getName().equals(name)){
                    MyTextArea.setTexte("Fichier trouvé : "+name);
                    Huffman huffman = new Huffman(file.getAbsolutePath());
                    huffman.compressFile();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(
                            new FileInputStream(System.getProperty("user.dir")+"/compress.huffman"));
                    BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                    int len;
                    byte[] data = new byte[1024];
                    while ((len = bufferedInputStream.read(data)) >= 0) {
                        out.write(data, 0, len);
                        out.flush();
                    }
                    bufferedInputStream.close();
                    out.close();
                    return;
                }
            }
            MyTextArea.setTexte("Fichier inexistant : "+name);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void waitMessage(){
        try{
            String order = in.readLine();
            switch (order){
                case "LIST":
                    MyTextArea.setTexte("["+socket.getInetAddress()+"] - Envoie de la liste des fichiers");
                    this.sendListFile();
                    break;
                case "GET":
                    MyTextArea.setTexte("["+socket.getInetAddress()+"] - Envoie d'un fichier");
                    this.sendFile();
                    break;
                default:
                    MyTextArea.setTexte("["+socket.getInetAddress()+"] - Commande inconnu");
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}