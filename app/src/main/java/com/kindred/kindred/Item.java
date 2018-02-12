package com.kindred.kindred;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by usman on 2/1/2018.
 */

public class Item {

    private ArrayList<String> Item_Name;
    private ArrayList<String> Item_Quantity;
    private ArrayList<String> Item_Price;
    private ArrayList<String> Item_Note;
    private int NumberOfItems;
    public Item(){
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public Item(ArrayList<String> Item_Name, ArrayList<String> Item_Quantity, ArrayList<String> Item_Price, ArrayList<String> Item_Note,int numberOfItems){
        this.Item_Name = Item_Name;
        this.Item_Quantity = Item_Quantity;
        this.Item_Price = Item_Price;
        this.Item_Note = Item_Note;
        this.NumberOfItems = numberOfItems;
    }
    @Exclude
    public Map<String, ArrayList<String>> toMap(){
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        int totalNumberOfItems = Item_Name.size();


        //Array of Item Names String
        ArrayList<String> ItemsVariablesName = new ArrayList<String>();
        String item1 = "item1";
        ItemsVariablesName.add(item1);
        String item2 = "item2";
        ItemsVariablesName.add(item2);
        String item3 = "item3";
        ItemsVariablesName.add(item3);
        String item4 = "item4";
        ItemsVariablesName.add(item4);
        String item5 = "item5";
        ItemsVariablesName.add(item5);

        //ArrayList of ArrayList for storing all fields of items in array
        // and all arrays in one array so that can be traversed
        ArrayList<ArrayList<String>> ItemFields = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList> inner = new ArrayList<ArrayList>();
        ArrayList<String> inner1 = new ArrayList<String>();
        inner.add(inner1);
        ArrayList<String> inner2 = new ArrayList<String>();
        inner.add(inner2);
        ArrayList<String> inner3 = new ArrayList<String>();
        inner.add(inner3);
        ArrayList<String> inner4 = new ArrayList<String>();
        inner.add(inner4);
        ArrayList<String> inner5 = new ArrayList<String>();
        inner.add(inner5);

        for(int j=0; j < totalNumberOfItems; j++)
        {
            inner.get(j).add(Item_Name.get(j));
            inner.get(j).add(Item_Quantity.get(j));
            inner.get(j).add(Item_Quantity.get(j));
            inner.get(j).add(Item_Note.get(j));
            ItemFields.add(j,inner.get(j));
            result.put(ItemsVariablesName.get(j), ItemFields.get(j));
        }

        return result;

    }


    public String namestoString(){
        String names = "";
        for(int i=0; i<Item_Name.size(); i++)
        {
            if(Item_Name.get(i).matches(""))
            {
                names += "null" + "\n";
            }
            else
            {
                names += Item_Name.get(i) + "\n";
            }
        }
        return names;
    }

    public String quantityToString(){
        String quantities = "";
        for(int i=0; i<Item_Quantity.size(); i++)
        {
            if(Item_Name.get(i).matches(""))
            {
                quantities += "null" + "\n";
            }
            else
            {
                quantities += Item_Quantity.get(i) + "\n";
            }
        }
        return quantities;
    }

    public String priceToString(){
        String prices = "";
        for(int i=0; i<Item_Price.size(); i++)
        {
            if(Item_Price.get(i).matches(""))
            {
                prices += "null" + "\n";
            }
            else
            {
                prices += Item_Price.get(i) + "\n";
            }
        }
        return prices;
    }

    public String noteToString(){
        String notes = "";
        for(int i=0; i<Item_Note.size(); i++)
        {
            if(Item_Note.get(i).matches(""))
            {
                notes += "null" + "\n";
            }
            else
            {
                notes += Item_Note.get(i) + "\n";
            }
        }
        return notes;
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
