package com.SafeWebDev.attempt.Models;

import java.util.Map;

public class GeneralHolder {
    private ItemHolder itemholder=new ItemHolder();
    private UserHolder userholder=new UserHolder();

    public User getCurrentUser(){
        return userholder.getUsuarioActual();
    }
    public void setCurrentUser(User user){
        userholder.setCurrentUser(user);
    }
    public void addItem(Item item){
        itemholder.addItem(item);
    }
    public void addUsr(User user){
        userholder.addUser(user);
    }
    public User getUsrId(long id){
        return userholder.getUsrbyId(id);
    }
    public Item getItemId(long id){
        return itemholder.getById(id);
    }
    public Map<Long,Item> getItems(){
        return itemholder.getItems();
    }
}