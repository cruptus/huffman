package huffman;

public class Feuille extends Element{

    private char caractere;

    public Feuille(int occurrence, char caractere){
        this.caractere = caractere;
        this.occurrence = occurrence;
    }

    public char getCaractere() {
        return caractere;
    }

    public void setCaractere(char caractere) {
        this.caractere = caractere;
    }

}