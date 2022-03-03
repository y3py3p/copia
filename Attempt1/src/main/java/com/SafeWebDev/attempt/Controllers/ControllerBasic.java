package com.SafeWebDev.attempt.Controllers;

import com.SafeWebDev.attempt.Models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControllerBasic {

    List<Item> items = new ArrayList<Item>();

    public ControllerBasic() {
        items.add(new Item("Boxers Hombre", "XXL", "Desgastado, dado de sí y manchado", 10));
        items.add(new Item("Bragas Mujer", "XL", "Desgastado, sucio", 15));
        items.add(new Item("Calcetin Blanco", "L", "Con agujeros, olor a esmegma", 35));
        items.add(new Item("Sujetador Mujer", "92B", "Hecho mierda", 25));
    }

    @GetMapping("")
    public String homePage(Model model) {
        model.addAttribute("items", items);
        return "StartPage";
    }

    @GetMapping("/item")
    public String itemPage(Model model, @RequestParam int item) {
        model.addAttribute("item", items.get(item - 1));
        return "ItemPage";
    }

    @GetMapping("/usr")
    public String usrPage() {
        return "UsrPage";
    }

    @GetMapping("/login")
    public String logIn(){
        return "LogIn";
    }

    @GetMapping("/createAccount")
    public String createAccount(){
        return "CreateAccount";
    }
}