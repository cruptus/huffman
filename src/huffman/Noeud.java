package huffman;

public class Noeud extends Element {

    private Element droite, gauche;

    public Noeud(Element droite, Element gauche){
        this.droite = droite;
        this.gauche = gauche;
        this.occurrence = droite.getOccurrence()+gauche.getOccurrence();
    }

    public Element getGauche() {
        return gauche;
    }

    public void setGauche(Element gauche) {
        this.gauche = gauche;
    }

    public Element getDroite() {
        return droite;
    }

    public void setDroite(Element droite) {
        this.droite = droite;
    }
}