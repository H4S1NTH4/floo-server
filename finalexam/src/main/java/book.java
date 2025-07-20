//title , auther, price, quantity , availability

@Document(collection="Books")
public class book {

    @Id
    private String id;

    private String titile;
    private String auther;
    private Double price;
    private int quantity;
    private boolean availability;

    //deafault constructor
    public Book(){}

    public Book(String title, String auther,Double price, int quanitiy){
        this.title = title;
        this.auther = auther;
        this.price = price;
        this.quantity = quantity;
        if(quantity >0){
            this.availability = true;
        }else{
            this.availability = false;
        }
    }

    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getAuther(){
        return auther;
    }
    public void setAuther(String auther){
        this.auther = auther;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int num){
        this.quantity = num;
    }
}
