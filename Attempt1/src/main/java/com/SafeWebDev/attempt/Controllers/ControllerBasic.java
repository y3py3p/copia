package com.SafeWebDev.attempt.Controllers;

import com.SafeWebDev.attempt.Models.*;
import com.SafeWebDev.attempt.Models.Services.*;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.regex.Pattern;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;

@Controller
public class ControllerBasic {

    @Autowired
    private CuponService cuponService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager entityManager;



    User currentUser;


    /*public ControllerBasic() {  //initializing the default products
        generalHolder.addItem(new Item("Boxers Hombre", "XXL", "Desgastado, dado de sí y manchado", 10));
        generalHolder.addItem(new Item("Bragas Mujer", "XL", "Desgastado, sucio", 15));
        generalHolder.addItem(new Item("Calcetin Blanco", "L", "Con agujeros, olor a esmegma", 35));
        generalHolder.addItem(new Item("Sujetador Mujer", "92B", "Hecho mierda", 25));
        generalHolder.addUsr(new User("hola","deez"));
        generalHolder.addUsr(new User("Usuario temporal","deez"));
        generalHolder.setCurrentUser(generalHolder.getUsrId(2));

        itemService.add(new Item("Bragas Mujer", "XL", "Desgastado, sucio", 15));
    }*/
    @PostConstruct
    public void init(){
        currentUser=new User("webo","webotes","webazos","webitos","weboncio");
    }


    @GetMapping("")     //redirect to StartPage.html, the main page
    public String homePage(Model model) {
        /*if(generalHolder.getCurrentUser() == null){
            model.addAttribute("login", "LogIn");   //login link will show up only if you're not logged in
        }else{
            model.addAttribute("login", "");
        }
        return "StartPage";*/
        return "StartPage";
    }

    @GetMapping("/item/edit/{id}")
    public String updateItem(Model model,@PathVariable long id){
        /*model.addAttribute("item",generalHolder.getItemId(id));
        return "ItemEdit";*/

        model.addAttribute("item", itemService.findById(id));
        return "ItemEdit";
    }

    @PostMapping("/editting/{id}")
    public String updatingItem(Model model,@PathVariable long id,Item item){
        /*generalHolder.getItemId(id).update(item);
        return "ItemEdited";*/
        itemService.findById(id).update(item);
        return "ItemEdited";
    }

    @GetMapping("/item/{id}")   //redirect to ItemPage.html, where you can see the info of one item
    public String itemPage(Model model, @PathVariable long id) {
        /*model.addAttribute("item",generalHolder.getItemId(id));
        return "ItemPage";*/

        model.addAttribute("item", itemService.findById(id));
        return "ItemPage";
    }

    @GetMapping("/usr") //redirect to UsrPage.html with your usr info (right now just the admin user)
    public String usrPage(Model model) {
        /*model.addAttribute("user", generalHolder.getCurrentUser());
        return "UsrPage";*/

        model.addAttribute("user", currentUser);
        return "UsrPage";

    }

    @PostMapping("/item/new")   //redirect to ItemAdded.html after adding an item to our general List
    public String addItem(Model model,Item item){
        /*generalHolder.addItem(item);
        return "ItemAdded";*/
        itemService.add(item);
        return "ItemAdded";
    }

    @GetMapping("/items")   //redirect to ItemsList.html, where you can see every product aviable
    public String listaItems(Model model){
        /*model.addAttribute("items", generalHolder.getItems().values());
        return "ItemsList";*/
        model.addAttribute("items", itemService.getAll());
        return "ItemsList";
    }

    @GetMapping("/item/del/{id}")
    public String delFromList(@PathVariable int id){
        itemService.delete(itemService.findById(id));
        return "ItemDeletedCompletely";

    }

    @GetMapping("/cart")    //redirect to Cart.html, with your cart info
    public String carrito(Model model){
        /*model.addAttribute("cart", generalHolder.getCurrentUser().getCart());
        return "Cart";*/
        model.addAttribute("cart", currentUser.getCart());
        return "Cart";
    }

    @GetMapping("/cart/{id}")   //redirect to CartAdded.html or CartAlreadyContains.html
    public String addCarrito(Model model, @PathVariable long id){
        /*if(!generalHolder.getCurrentUser().cartContains(generalHolder.getItemId(id))){
            generalHolder.getCurrentUser().addCart(generalHolder.getItemId(id));
            return "CartAdded"; //item added
        }else{
            return "CartAlreadyContains";   //you already have the item in your cart
        }*/
        if(currentUser.getCart().contains(itemService.findById(id))){
            return "CartAlreadyContains";
        }else{
            currentUser.addCart(itemService.findById(id));
            return "CartAdded";
        }
    }

    @GetMapping("/cart/del/{id}")   //redirect to ItemDeleted.html, to confirm the item was deleted
    public String deleteItem(@PathVariable int id){
        currentUser.delCart(id-1);
        return "ItemDeleted";

    }

    @GetMapping("/login")   //redirect to LogIn.html, where you'll be able to log in
    public String logIn(){
        return "LogIn";
    }

    @GetMapping("/createAccount")   //redirect to CreateAccount.html, to sign up
    public String createAccount(){
        return "CreateAccount";

    }

    @GetMapping("/comments")    //see every comment in our database
    public String comments(Model model){
        model.addAttribute("comment",commentService.getAll());
        return "comments";
    }

    @GetMapping("/NewComment")     //add a comment to our database
    public String addComment(Model model){
        return "NewComment";
    }

    @PostMapping("/createComment")
    public String createComment(@RequestParam String content,@RequestParam String user){
       PolicyFactory sanitizer=new HtmlPolicyBuilder()
                .allowStandardUrlProtocols()
                .allowAttributes("title").globally() //We allow the title attribute wherever it is
                .allowAttributes("href").onElements("a") //We allow the use of link in elements of type <a>
                .requireRelNofollowOnLinks() //Because we allow link, we use this option t avoid spamming
                .allowAttributes("lang").matching(Pattern.compile("[a-zA-Z]{2,20}")).globally() //Allow alphabetic values in lang attributes wherever it is
                .allowAttributes("align").matching(true,"left","rigth","center",
                        "justify","char").onElements("p") //Allow alignment options in <p> elements
               .allowAttributes("style").onElements("span")
                .allowElements("a","p","div","i","b","em","blockquote","tt","strong",
                        "br","ul","ol","li", "span") //List of all the elements that are allowed in our "String"
                .toFactory(); //Make it be a factory so that the Policy Builder matches the PolicyFactory type
        String sanitized=sanitizer.sanitize(content);
        String sanUser=sanitizer.sanitize(user);
        Comment comment=new Comment();
        comment.setContent(sanitized);
        comment.setUser(sanUser);

        /*Comment comment=new Comment();
        comment.setContent(content);
        comment.setUser(user);*/
        commentService.addComment(comment);
        return "CommentAdded";
    }

    @GetMapping("/payments")
    public String payment(Model model){
        model.addAttribute("cart",currentUser.getCart());
        return "Payments";
    }

    @GetMapping("/pay")
    public String pay(Model model){
        model.addAttribute("precio",currentUser.getPrice());
        return "PayForm";
    }

    @PostMapping("/price/final")
    public String finalPrice(Model model, @RequestParam long cupon){
        /*if(cupon==null){
            model.addAttribute("precioFinal", currentUser.getPrice());
            return "SuccessfulPurchase";
        }
        else */if(!cuponService.exists(cupon)){
            model.addAttribute("precioFinal", currentUser.getPrice());
            return "SuccessfulPurchase";

        } else {
            Cupon cupone = cuponService.findById(cupon);

            model.addAttribute("precioFinal", currentUser.priceCupon(cupone));

            return "SuccessfulPurchase";
        }

    }



    @GetMapping("/coupons")
    public String coupons(Model model){
        model.addAttribute("coupons",cuponService.getAll());
        return "CouponList";
    }

    @PostMapping("/coupon/new")
    public String createCoupon(Cupon coupon){
        cuponService.addCupon(coupon);
        return "CouponAdded";
    }

    @PostMapping("/search")
    public String searchByName(Model model, @RequestBody String name){
        entityManager.getTransaction().begin();
        TypedQuery<Item> q1=entityManager.createQuery("SELECT c FROM Item c WHERE lower(c.productName)=lower(:name)",Item.class).setParameter("name",name);
        List<Item> items=q1.getResultList();
        model.addAttribute("items",items);
        return "ItemsList";
    }


    /*@GetMapping("/testform")
    public String testform(){
        return "TestForm";
    }

    @PostMapping("/test")
    public String test(Model model,@RequestParam String texto){
        PolicyFactory sanitizer=new HtmlPolicyBuilder()
                .allowStandardUrlProtocols()
                .allowAttributes("title").globally() //We allow the title attribute wherever it is
                .allowAttributes("href").onElements("a") //We allow the use of link in elements of type <a>
                .requireRelNofollowOnLinks() //Because we allow link, we use this option t avoid spamming
                .allowAttributes("lang").matching(Pattern.compile("[a-zA-Z]{2,20}")).globally() //Allow alphabetic values in lang attributes wherever it is
                .allowAttributes("align").matching(true,"left","rigth","center",
                        "justify","char").onElements("p") //Allow alignment options in <p> elements
                .allowElements("a","p","div","i","b","em","blockquote","tt","strong",
                        "br","ul","ol","li") //List of all the elements that are allowed in our "String"
                .toFactory(); //Make it be a factory so that the Policy Builder matches the PolicyFactory type
        PolicyFactory sanitizer2=Sanitizers.FORMATTING.and(Sanitizers.LINKS);//Pasamos otra politica para mas seguridad
        String sanitized=sanitizer.sanitize(texto);
        String sanitized2=sanitizer2.sanitize(sanitized);
        model.addAttribute("texto",sanitized2);
        return "Test";
    }*/

}
