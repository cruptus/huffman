package client;

import huffman.Huffman;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Jeremie on 07/11/2016.
 */
public class Client {

    private String host;
    private int port;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private boolean connect(){
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);

            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return socket.isConnected();
        } catch (Exception e) {
            System.out.println("Erreur Client : Impossible de se connecter au serveur");
            e.printStackTrace();
        }
        return false;
    }

    private void disconnect(){
        try {
            socket.close();
        }catch (Exception e){
            System.out.println("Erreur Client : Impossible de se deconnecter du serveur");
            e.printStackTrace();
        }
    }


    public void getAllFile(){
        try{
            if (!connect())
                return;

            out.write("LIST");
            out.newLine();
            out.flush();
            String response;
            ArrayList<String> items = new ArrayList<>();
            while ((response = in.readLine()) != null)
                items.add(response);

            MySelectBox.addItem(items);

        }catch (Exception e){
            e.printStackTrace();
        }
        disconnect();
    }

    public void getFile(String file){
        try{
            if(!connect())
                return;

            out.write("GET");
            out.newLine();
            out.write(file);
            out.newLine();
            out.flush();
            String path = "client_directory/decompress.huffman";
            File decompress = new File(path);
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream file_write = new BufferedOutputStream(new FileOutputStream(decompress, false));
            int len;
            byte[] data = new byte[1024];

            while ((len = in.read(data)) != -1){
                file_write.write(data, 0, len);
                file_write.flush();
            }
            file_write.close();
            in.close();

            Huffman huffman = new Huffman();
            BufferedWriter fichier = new BufferedWriter(new FileWriter(new File("client_directory/"+file), false));

            String tmp = huffman.decompressFile(path);

            fichier.write(tmp);
            fichier.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        disconnect();
    }
}
