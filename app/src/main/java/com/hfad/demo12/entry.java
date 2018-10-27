package com.hfad.demo12;

/**
 * Created by HP on 30-05-2018.
 */
public class entry {
    int id,quantity;
    String item;
    String price,sp_des_price;
    String total_price,sp_des;


    public entry(){   }

    public entry(int id, String item, String price,int quantity, String sp_des ,String total_price){
        this.id = id;
        this.item = item;
        this.price = price;
        this.quantity=quantity;
        this.sp_des=sp_des;
        this.total_price = total_price;

    }

    public entry( String item, String price,int quantity, String sp_des, String total_price){
        this.item = item;
        this.price = price;
        this.quantity=quantity;
        this.sp_des=sp_des;
        this.total_price = total_price;
    }
    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getItem(){
        return this.item;
    }

    public void setItem(String item){
        this.item = item;
    }

    public  String getPrice(){
        return this.price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public int getQuantity(){ return this.quantity; }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getSp_des(){return this.sp_des;}

    public void setSp_des(String sp_des){
        this.sp_des = sp_des;
    }

    public  String getTotal_price(){
        return this.total_price;
    }

    public void setTotal_price(String total_price){
        this.total_price = total_price;
    }

}
