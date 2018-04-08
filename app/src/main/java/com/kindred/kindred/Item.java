package com.kindred.kindred;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by usman on 2/1/2018.
 */

public class Item {

    private ArrayList<ArrayList<String>> items;
    private ArrayList<String> Item_Name;
    private ArrayList<String> Item_Quantity;
    private ArrayList<String> Item_Price;
    private ArrayList<String> Item_Note;

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Item(ArrayList<String> Item_Name, ArrayList<String> Item_Quantity, ArrayList<String> Item_Price, ArrayList<String> Item_Note) {
        this.Item_Name = Item_Name;
        this.Item_Quantity = Item_Quantity;
        this.Item_Price = Item_Price;
        this.Item_Note = Item_Note;
    }

    @Exclude
    public Map<String, HashMap<String, String>> toMap() {
        HashMap<String, HashMap<String, String>> result = new HashMap<>();

        int totalNumberOfItems = Item_Name.size();

        int count = 1;
        for (int j = 0; j < totalNumberOfItems; j++) {
            HashMap<String, String> inner = new HashMap<>();
            inner.put("Item-Name", Item_Name.get(j));
            inner.put("Item-Quantity", Item_Quantity.get(j));
            inner.put("Item-Price", Item_Price.get(j));
            inner.put("Item-Note", Item_Note.get(j));
            result.put("Item" + count, inner);
            count++;
        }
        return result;
    }


    public ArrayList<ArrayList<String>> getItems() {
        return items;
    }

    public void setItems(ArrayList<ArrayList<String>> items) {
        this.items = items;
    }

    public ArrayList<String> getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(ArrayList<String> item_Name) {
        Item_Name = item_Name;
    }

    public ArrayList<String> getItem_Quantity() {
        return Item_Quantity;
    }

    public void setItem_Quantity(ArrayList<String> item_Quantity) {
        Item_Quantity = item_Quantity;
    }

    public ArrayList<String> getItem_Price() {
        return Item_Price;
    }

    public void setItem_Price(ArrayList<String> item_Price) {
        Item_Price = item_Price;
    }

    public ArrayList<String> getItem_Note() {
        return Item_Note;
    }

    public void setItem_Note(ArrayList<String> item_Note) {
        Item_Note = item_Note;
    }


}
