package huffman;

import sun.security.util.BitArray;

import java.io.*;
import java.util.*;

/**
 * Droite = 1
 * Gauche = 0
 */
public class Huffman {

    private Scanner file;
    private List<Element> list;
    private HashMap<Character, BitArray> dico;
    private BitArray message;
    private String name;

    public Huffman(String name){
        try {
            this.name = name;
            this.list = new ArrayList<>();
            this.file = new Scanner(new File(name));
            this.dico = new HashMap<>();
            lire();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Huffman(){
        this.name = null;
        this.list = new ArrayList<>();
        this.dico = new HashMap<>();
    }

    public HashMap<Character, BitArray> getDico(){return dico;}
    public BitArray getMessage(){return message;}

    /**
     * Lire le fichier + créer arbre + dictionnaire + message compressé
     */
    private void lire(){
        while (file.hasNextLine()) {
            for(char c : file.nextLine().toCharArray()) {
                if(!chercherList(c))
                    list.add(new Feuille(1, c));
            }
        }
        createNoeud();
        createArraylist();
        createMessage();
        file.close();
    }

    /**
     * Ajoute 1 au nombre d'occurence d'un caractère
     * @param c Charactère
     * @return Boolean
     */
    private boolean chercherList(char c){
        for (Element element : this.list) {
            if(((Feuille) element).getCaractere() == c){
                element.setOccurrence(element.getOccurrence()+1);
                return true;
            }
        }
        return false;
    }

    /**
     * Trie les elements
     */
    private void trier(){
        list.sort(new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                return o1.getOccurrence() < o2.getOccurrence() ? -1 : o1.getOccurrence() > o2.getOccurrence() ? 1 : 0;
            }
        });
    }

    /**
     * Créer l'arbre
     */
    private void createNoeud(){
        trier();
        Element droite = list.get(0);
        Element gauche = list.get(1);
        list.add(new Noeud(droite, gauche));
        list.remove(droite);
        list.remove(gauche);

        if(list.size() > 1)
            createNoeud();
    }

    /**
     * Parcours l'arbre pour créer le dictionnaire
     */
    private void createArraylist(){
        BitArray bitArray = new BitArray(1);
        Noeud noeud = (Noeud)list.get(0);
        bitArray.set(0, true);
        parcours(noeud.getDroite(), bitArray);

        bitArray.set(0, false);
        parcours(noeud.getGauche(), bitArray);
    }

    /**
     * Parcours les branches de l'arbre
     * @param element Element
     * @param code BitArray
     */
    private void parcours(Element element, BitArray code){
        if(element instanceof Feuille){
            Feuille feuille = (Feuille)element;
            dico.put(feuille.getCaractere(), code);
        }else{
            Noeud noeud = (Noeud) element;
            parcours(noeud.getDroite(), addBit(code, true));
            parcours(noeud.getGauche(), addBit(code, false));
        }
    }

    /**
     * Ajoute un bit au BitArray
     * @param code BitArray
     * @param value Boolean
     * @return BitArray
     */
    private BitArray addBit(BitArray code, Boolean value){
        BitArray bitArray = new BitArray(code.length()+1);
        for(int i = 0; i < code.length(); i++)
            bitArray.set(i, code.get(i));
        bitArray.set(code.length(), value);
        return bitArray;
    }

    /**
     * Créer le message compressé sous forme de bit
     */
    private void createMessage(){
        message = new BitArray(0);
        file.close();
        try {
            this.file = new Scanner(new File(name));
            while (file.hasNextLine()) {
                for(char c : file.nextLine().toCharArray()) {
                    message = addBitToMessage(dico.get(c));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Ajoute un BitArray au message compressé
     * @param value BitArray
     * @return bitArray BitArray
     */
    private BitArray addBitToMessage(BitArray value){
        BitArray bitArray = new BitArray(message.length()+value.length());
        for(int i = 0; i < message.length(); i++)
            bitArray.set(i, message.get(i));
        int j = 0;
        for(int i = message.length(); i < bitArray.length(); i++) {
            bitArray.set(i, value.get(j));
            j++;
        }
        return bitArray;
    }

    /**
     * Dechiffre le message
     * @param message BitArray
     */
    public String decriptMessage(BitArray message){
        Noeud element = (Noeud)list.get(0);
        Feuille feuille;
        String decrypt = "";
        for(int i = 0; i < message.length(); i++){
            if(message.get(i)){
                if(element.getDroite() instanceof Feuille){
                    feuille = (Feuille)element.getDroite();
                    decrypt += feuille.getCaractere();
                    element = (Noeud)list.get(0);
                }else
                    element = (Noeud)element.getDroite();
            }else{
                if(element.getGauche() instanceof Feuille){
                    feuille = (Feuille)element.getGauche();
                    decrypt += feuille.getCaractere();
                    element = (Noeud)list.get(0);
                } else
                    element = (Noeud)element.getGauche();
            }
        }
        return decrypt;
    }

    /**
     * Compresse un fichier
     */
    public void compressFile(){
        try {
            String name = "compress.huffman";

            // Ecriture du dictionnaire
            ObjectOutputStream compressDico = new ObjectOutputStream(new FileOutputStream(new File(name)));
            HashMap<Character, String> dictionnaire = new HashMap<>();
            dico.forEach((k, v) -> dictionnaire.put(k, v.toString()));
            compressDico.writeObject(dictionnaire);
            compressDico.close();

            // Balise delimitant le dictionnaire du texte
            BufferedWriter balise = new BufferedWriter(new FileWriter(name, true));
            balise.newLine();
            balise.write("|||");
            balise.newLine();
            balise.close();

            // Ecriture du message
            DataOutputStream message = new DataOutputStream(new FileOutputStream(name, true));
            for(int i = 0; i < this.message.length(); i++)
                message.writeBoolean(this.message.get(i));
            message.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Decompresse un fichier
     */
    public String decompressFile(String name){
        String tmp = "";
        try {
            ObjectInput file = new ObjectInputStream(new FileInputStream(name));
            HashMap<Character, String> dico = (HashMap<Character, String>) file.readObject();
            dico.forEach((k, v) -> this.dico.put(k, stringToBitArray(v)));
            file.close();

            RandomAccessFile randomAccessFile = new RandomAccessFile(name, "r");
            while (!randomAccessFile.readLine().equals("|||"));

            long longueur = (randomAccessFile.length() - randomAccessFile.getFilePointer());
            long init = randomAccessFile.getFilePointer();
            BitArray bitArray = new BitArray((int)longueur);

            for(long i = init; i < randomAccessFile.length(); i++){
                randomAccessFile.seek(i);
                bitArray.set((int)(i-init), randomAccessFile.readBoolean());
            }
            this.message = bitArray;
            randomAccessFile.close();
            BitArray key = new BitArray(0);

            for (int i = 0; i < message.length(); i++) {
                key = addBit(key, message.get(i));
                for (Map.Entry<Character, BitArray> e: this.dico.entrySet()) {
                    if(e.getValue().equals(key)) {
                        tmp += e.getKey();
                        key = new BitArray(0);
                    }
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return tmp;
    }


    /**
     * Convertie un String en BitArray
     * @param value String
     * @return BitArray
     */
    private BitArray stringToBitArray(String value){
        String[] tmp = value.split("");
        BitArray bitArray = new BitArray(value.length());
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals("1"))
                bitArray.set(i, true);
            else
                bitArray.set(i, false);
        }
        return bitArray;
    }
}
