package client;

import javax.swing.*;
import java.util.ArrayList;

public class MySelectBox extends JComboBox<String> {

    private static MySelectBox instance = null;

    private MySelectBox(){
        super();

    }

    public static MySelectBox getInstance(){
        if(instance == null)
            instance = new MySelectBox();
        return instance;
    }

    public static String itemSelect(){
        return (String) instance.getSelectedItem();
    }

    public static void addItem(ArrayList<String> items){
        instance.removeAllItems();
        for (String item : items) {
            instance.addItem(item);
        }
    }
}
