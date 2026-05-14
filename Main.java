

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

// ----------------- COLOR HELPERS -----------------
class Colors {
    // Toggle to enable/disable color output
    public static boolean ENABLE = true;

    public static final String RESET = ENABLE ? "\u001B[0m" : "";
    public static final String BLACK = ENABLE ? "\u001B[30m" : "";
    public static final String RED = ENABLE ? "\u001B[31m" : "";
    public static final String GREEN = ENABLE ? "\u001B[32m" : "";
    public static final String YELLOW = ENABLE ? "\u001B[33m" : "";
    public static final String BLUE = ENABLE ? "\u001B[34m" : "";
    public static final String PURPLE = ENABLE ? "\u001B[35m" : "";
    public static final String CYAN = ENABLE ? "\u001B[36m" : "";
    public static final String WHITE = ENABLE ? "\u001B[37m" : "";

    public static String bold(String s){ return (ENABLE? "\u001B[1m":"") + s + RESET; }
    public static String color(String c, String s){ return c + s + RESET; }
}

// ----------------- PRODUCT -----------------
class Product {
    String id, name, category;
    double price;
    int quantity;
    List<String> reviews = new ArrayList<>();
    List<Integer> ratings = new ArrayList<>();

    Product(String id, String name, String category, double price, int quantity){
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    double getAverageRating(){
        if(ratings.isEmpty()) return 0;
        int sum = 0;
        for(int r: ratings) sum += r;
        return (double) sum / ratings.size();
    }

    void addReview(String user, int rating, String comment){
        ratings.add(rating);
        reviews.add(Colors.color(Colors.CYAN, "⭐ " + user + ": ") + comment + " (" + rating + "/5)");
    }

    void showReviews(){
        if(reviews.isEmpty()){
            System.out.println(Colors.color(Colors.YELLOW, "No reviews yet for " + name + "."));
        } else {
            System.out.println(Colors.color(Colors.PURPLE, "\n--- Reviews for " + name + " ---"));
            for(String r : reviews) System.out.println("• " + r);
            System.out.printf(Colors.color(Colors.GREEN, "Average Rating: %.1f/5 ⭐\n"), getAverageRating());
        }
    }
}

// ----------------- ORDER -----------------
class OrderItem {
    Product product;
    int qty;
    OrderItem(Product p, int q){ this.product = p; this.qty = q; }
}

class Order {
    String orderId;
    List<OrderItem> items;
    double subtotal, discount, total;
    Date date;

    Order(String orderId, List<OrderItem> items, double subtotal, double discount, double total){
        this.orderId = orderId; this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
        this.date = new Date();
    }

    void printInvoice(){
        System.out.println(Colors.color(Colors.CYAN, "\n---------- INVOICE ----------"));
        System.out.println("Order ID: " + orderId);
        System.out.println("Date: " + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date));
        System.out.println("--------------------------------");
        System.out.printf("%-20s %-6s %-10s %-10s\n", "Product", "Qty", "Price", "LineTotal");
        for(OrderItem oi: items){
            System.out.printf("%-20s %-6d ₹%-9.2f ₹%-9.2f\n", oi.product.name, oi.qty, oi.product.price, oi.product.price*oi.qty);
        }
        System.out.println("--------------------------------");
        System.out.printf("Subtotal: ₹%.2f\n", subtotal);
        System.out.printf("Discount: ₹%.2f\n", discount);
        System.out.printf("Total: ₹%.2f\n", total);
        System.out.println("-------------------------------\n");
    }
}

// ----------------- LOAN -----------------
class Loan {
    double principal;
    int months;
    double annualRate;
    double monthlyInstallment;
    Date startDate;

    Loan(double principal, int months, double annualRate){
        this.principal = principal;
        this.months = months;
        this.annualRate = annualRate;
        this.startDate = new Date();
        double totalInterest = principal * (annualRate/100.0) * (months/12.0);
        double totalToRepay = principal + totalInterest;
        this.monthlyInstallment = totalToRepay / months;
    }
    void showLoanCycle() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
    
        
        if (cal.get(Calendar.DAY_OF_MONTH) > 5) {
            cal.add(Calendar.MONTH, 1);
        }
    

        cal.set(Calendar.DAY_OF_MONTH, 5);
    
        for (int i = 1; i <= months; i++) {
            
            System.out.printf(
                "Month %d: ₹%.2f due on %s\n",
                i,
                monthlyInstallment,
                new SimpleDateFormat("dd-MMM-yyyy").format(cal.getTime())
            );
    
            cal.add(Calendar.MONTH, 1);
        }
    }

}

// ----------------- BANK ACCOUNT -----------------
class BankAccount {
    String upiId;
    double balance;

    BankAccount(String mobile){
        String last4 = mobile.substring(mobile.length() - 4);
        this.upiId = last4 + "@bank";
        this.balance = 500.0;
    }

    void deposit(double amt){ balance += amt; System.out.printf(Colors.color(Colors.GREEN, "Deposited ₹%.2f | New balance: ₹%.2f\n"), amt, balance); }
    boolean withdraw(double amt){
        if(balance>=amt){ balance-=amt; System.out.printf(Colors.color(Colors.GREEN, "Withdrew ₹%.2f | New balance: ₹%.2f\n"), amt, balance); return true; }
        else{ System.out.println(Colors.color(Colors.RED, "Insufficient balance.")); return false; }
    }
    void showBalance(){ System.out.printf(Colors.color(Colors.BLUE, "Bank balance: ₹%.2f | UPI: %s\n"), balance, upiId); }
}

// ----------------- USER -----------------
class User {
    String username, password;
    int age;
    String panNumber;
    String phoneNumber;

    double totalSpent, walletBalance;
    
    List<Order> orderHistory;
    List<OrderItem> cart;
    boolean hasSpunThisSession;
    BankAccount bankAccount;
    Loan loan;

    User(String username, String password, int age, String pan, String phone){
        this.username = username;
        this.password = password;
        this.age = age;
        this.panNumber = pan;
        this.phoneNumber = phone;

        this.totalSpent = 0.0; 
        this.orderHistory = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.hasSpunThisSession = false;
        this.bankAccount = new BankAccount(phone);
        this.loan = null;
    }
}

// ----------------- ADMIN -----------------
class Admin {
    private final String username = "admin";
    private final String password = "admin123";

    boolean login(String u, String p){ return username.equals(u) && password.equals(p); }
}

// ----------------- MAIN -----------------
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final HashMap<String, User> users = new HashMap<>();
    private static final HashMap<String, Product> products = new HashMap<>();
    private static final Admin admin = new Admin();
    private static final Random rand = new Random();

    public static void main(String[] args){
        seedProducts();
        banner();
        mainMenu();
    }
    
    private static void banner(){
        System.out.println();
        System.out.println(Colors.color(Colors.CYAN, Colors.bold("🛒 |==============================================================| 🛒")));
        System.out.println(Colors.color(Colors.PURPLE,Colors.bold("       |         SMART ONLINE SHOPPING  SYSTEM             |")));
        System.out.println(Colors.color(Colors.BLUE, Colors.bold("          |            Retail Billing & Management      |")));
        System.out.println(Colors.color(Colors.CYAN, Colors.bold("🛍️ |==============================================================| 🛍️")));
        System.out.println();
    }

  

    private static void seedProducts(){
        //  Electronics
        products.put("E1", new Product("E1", "Laptop", "Electronics", 50000, 10));
        products.put("E2", new Product("E2", "Smartphone", "Electronics", 25000, 12));
        products.put("E3", new Product("E3", "Headphones", "Electronics", 2000, 15));
        products.put("E4", new Product("E4", "Smartwatch", "Electronics", 4000, 8));

        // Fashion
        products.put("F1", new Product("F1", "T-Shirt", "Fashion", 499, 20));
        products.put("F2", new Product("F2", "Jeans", "Fashion", 1499, 10));
        products.put("F3", new Product("F3", "Jacket", "Fashion", 2499, 5));
        products.put("F4", new Product("F4", "Sneakers", "Fashion", 1999, 7));

        //  Grocery
        products.put("G1", new Product("G1", "Rice (5kg)", "Grocery", 450, 30));
        products.put("G2", new Product("G2", "Cooking Oil 1L", "Grocery", 180, 25));
        products.put("G3", new Product("G3", "Wheat Flour (5kg)", "Grocery", 250, 20));
        products.put("G4", new Product("G4", "Sugar (1kg)", "Grocery", 60, 50));

        //  Home & Kitchen
        products.put("H1", new Product("H1", "Mixer Grinder", "Home & Kitchen", 3200, 6));
        products.put("H2", new Product("H2", "Dinner Set (20 pcs)", "Home & Kitchen", 1500, 10));
        products.put("H3", new Product("H3", "Water Bottle", "Home & Kitchen", 250, 40));
        products.put("H4", new Product("H4", "Electric Kettle", "Home & Kitchen", 1100, 12));

        //  Books
        products.put("B1", new Product("B1", "Java Programming", "Books", 600, 15));
        products.put("B2", new Product("B2", "Data Structures", "Books", 550, 10));
        products.put("B3", new Product("B3", "Clean Code", "Books", 800, 5));
        products.put("B4", new Product("B4", "Operating Systems", "Books", 700, 7));

        //  Beauty & Personal Care
        products.put("P1", new Product("P1", "Shampoo", "Beauty", 250, 25));
        products.put("P2", new Product("P2", "Face Cream", "Beauty", 400, 18));
        products.put("P3", new Product("P3", "Perfume", "Beauty", 1200, 10));
        products.put("P4", new Product("P4", "Lip Balm", "Beauty", 150, 30));

        //  Sports & Fitness
        products.put("S1", new Product("S1", "Cricket Bat", "Sports & Fitness", 1500, 8));
        products.put("S2", new Product("S2", "Football", "Sports & Fitness", 900, 10));
        products.put("S3", new Product("S3", "Yoga Mat", "Sports & Fitness", 700, 15));
        products.put("S4", new Product("S4", "Dumbbells (5kg)", "Sports & Fitness", 1200, 6));

        //  Toys & Games
        products.put("T1", new Product("T1", "Remote Car", "Toys", 800, 12));
        products.put("T2", new Product("T2", "Puzzle Set", "Toys", 300, 20));
        products.put("T3", new Product("T3", "Board Game", "Toys", 700, 10));
        products.put("T4", new Product("T4", "Action Figure", "Toys", 500, 15));
    }

    private static void mainMenu(){
        while(true){
            System.out.println("\n" + Colors.color(Colors.YELLOW, "Main Menu:"));
            System.out.println("1) Register   2) Login   3) Admin Login   4) Exit");
            System.out.print(Colors.color(Colors.GREEN, "Choose: "));
            String ch = sc.nextLine().trim();
            switch(ch){
                case "1": register(); break;
                case "2": login(); break;
                case "3": adminLogin(); break;
                case "4": System.out.println(Colors.color(Colors.PURPLE,"Goodbye! 👋")); return;
                default: System.out.println(Colors.color(Colors.RED,"Invalid choice."));
            }
        }
    }

    private static void register(){
        System.out.print("Username: ");
        String uname = sc.nextLine().trim();
        if(users.containsKey(uname)){
            System.out.println(Colors.color(Colors.RED, "Username exists."));
            return;
        }

        String phone;
        while(true){
            System.out.print("Enter phone number (10 digits): ");
            phone = sc.nextLine().trim();
            if(phone.matches("[6-9]\\d{9}")) break;
            else System.out.println(Colors.color(Colors.RED,"Invalid Indian phone number (must start with 6/7/8/9)."));
        }

        int otp = 1000 + rand.nextInt(9000);
        System.out.println("OTP sent to " + phone + ": " + otp);
        System.out.print("Enter OTP: ");
        int userOtp = readInt();
        if(userOtp != otp){
            System.out.println(Colors.color(Colors.RED,"OTP mismatch. Registration failed."));
            return;
        }

        String password;
        while(true){
            System.out.print("Password (8+ chars, 1 upper, 1 digit, 1 special): ");
            password = sc.nextLine();
            if(validatePassword(password)) break;
            else System.out.println(Colors.color(Colors.RED,"Password invalid."));
        }

        // Create new user
        users.put(uname, new User(uname,password,18,"NA",phone));
        
        // Fetch that user back
        User newUser = users.get(uname);
        
        // Show success + UPI
        System.out.println(Colors.color(Colors.GREEN, "Registered successfully!"));
        System.out.println(Colors.color(Colors.CYAN, "Your UPI ID is: " + newUser.bankAccount.upiId));

    }

    private static boolean validatePassword(String p){
        if(p.length()<8) return false;
        boolean hasUpper=false,hasDigit=false,hasSpecial=false;
        for(char c:p.toCharArray()){
            if(Character.isUpperCase(c)) hasUpper=true;
            else if(Character.isDigit(c)) hasDigit=true;
            else if(!Character.isLetterOrDigit(c)) hasSpecial=true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    private static void login(){
        System.out.print("Username: "); String uname = sc.nextLine().trim();
        System.out.print("Password: "); String pass = sc.nextLine();
        User user = users.get(uname);
        if(user==null || !user.password.equals(pass)){
            System.out.println(Colors.color(Colors.RED,"Invalid credentials."));
            System.out.println("Do you want to reset password? (yes/no)");
            String choice=sc.nextLine().trim();
            if(choice.equalsIgnoreCase("yes")){
                resetPassword(uname);
            }
            return;
        }

        int otp = 1000 + rand.nextInt(9000);
        System.out.println("OTP sent: "+otp);
        System.out.print("Enter OTP: "); int userOtp = readInt();
        if(userOtp!=otp){ System.out.println(Colors.color(Colors.RED,"Incorrect OTP. Login failed.")); return; }

        System.out.println(Colors.color(Colors.GREEN,"Login successful. Welcome, "+uname+"! 🎉"));
        userSession(user);
    }

    private static void resetPassword(String uname){
        User user = users.get(uname);
        if(user==null){ System.out.println(Colors.color(Colors.RED,"Username not found.")); return; }
        while(true){
            System.out.print("Enter new password: "); String newPass = sc.nextLine();
            if(validatePassword(newPass)){ user.password=newPass; System.out.println(Colors.color(Colors.GREEN,"Password reset successful.")); break; }
            else System.out.println(Colors.color(Colors.RED,"Password invalid."));
        }
    }

    private static void adminLogin(){
        System.out.print("Admin username: "); String auser=sc.nextLine().trim();
        System.out.print("Admin password: "); String apass=sc.nextLine();
        if(!admin.login(auser,apass)){ System.out.println(Colors.color(Colors.RED,"Invalid admin credentials.")); return; }
        System.out.println(Colors.color(Colors.GREEN,"Admin login successful."));
        adminPanel();
    }

    private static void adminPanel(){
        while(true){
            System.out.println("\n" + Colors.color(Colors.YELLOW,"--- Admin Panel ---"));
            System.out.println("1) View products   2) Add   3) Remove   4) Update   5) View users   6) Revenue   7) Back");
            System.out.print(Colors.color(Colors.GREEN,"Choose: ")); String c=sc.nextLine().trim();
            switch(c){
                case "1": viewAllProductsWithStock(); break;
                case "2": adminAddProduct(); break;
                case "3": adminRemoveProduct(); break;
                case "4": adminUpdateProduct(); break;
                case "5": adminViewUsers(); break;
                case "6": adminTotalRevenue(); break;
                case "7": return;
                default: System.out.println(Colors.color(Colors.RED,"Invalid option."));
            }
        }
    }

    private static void viewAllProductsWithStock(){
        System.out.println("\n" + Colors.color(Colors.BLUE,"Products:"));
        for(Product p:products.values()){
            System.out.printf("%s | %s | %s | ₹%.2f | qty:%d\n", p.id,p.name,p.category,p.price,p.quantity);
            if(p.quantity<5) System.out.println(Colors.color(Colors.RED,"  --> LOW STOCK ALERT (qty < 5)"));
        }
    }
    private static void adminAddProduct(){
        System.out.print("Enter product id: ");
        String id = sc.nextLine().trim().toUpperCase(); // <-- convert to UPPERCASE compulsory

        if(products.containsKey(id)){
            System.out.println(Colors.color(Colors.RED,"Product ID already exists. Please try another ID."));
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Category: ");
        String cat = sc.nextLine().trim();

        System.out.print("Price: ");
        double price = readDouble();

        System.out.print("Quantity: ");
        int qty = readInt();

        products.put(id,new Product(id,name,cat,price,qty));
        System.out.println(Colors.color(Colors.GREEN,"Product added with ID: "+id));
    }




    private static void adminRemoveProduct(){
        System.out.print("Product id to remove: "); String id=sc.nextLine().trim();
        if(products.remove(id)!=null) System.out.println(Colors.color(Colors.GREEN,"Product removed."));
        else System.out.println(Colors.color(Colors.RED,"Product not found."));
    }

    private static void adminUpdateProduct(){
        System.out.print("Product id to update: "); String id=sc.nextLine().trim();
        Product p=products.get(id);
        if(p==null){ System.out.println(Colors.color(Colors.RED,"Product not found.")); return; }
        System.out.println("1) Update price 2) Update qty 3) Both"); System.out.print("Choose: "); String ch=sc.nextLine();
        if(ch.equals("1")||ch.equals("3")) { System.out.print("New price: "); p.price=readDouble();}
        if(ch.equals("2")||ch.equals("3")) { System.out.print("New qty: "); p.quantity=readInt();}
        System.out.println(Colors.color(Colors.GREEN,"Product updated."));
    }

    private static void adminViewUsers(){
        System.out.println(Colors.color(Colors.BLUE,"Registered users:"));
        for(String u:users.keySet()) System.out.println("- "+u);
    }

    private static void adminTotalRevenue(){
        double total=0.0; for(User u:users.values()) total+=u.totalSpent;
        System.out.printf(Colors.color(Colors.CYAN,"Total revenue: ₹%.2f\n"), total);
    }

    private static void userSession(User user){
        while(true){
            System.out.println("\n" + Colors.color(Colors.YELLOW,"User Menu:"));
            System.out.println("1) Browse   2) View Cart   3) Orders   4) Wallet & Bank   5) Contact Support   6) Logout");
            System.out.print(Colors.color(Colors.GREEN,"Choose: ")); String c=sc.nextLine().trim();
            switch(c){
                case "1": browseProducts(user); break;
                case "2": viewCartAndCheckout(user); break;
                case "3": viewOrders(user); break;
                case "4": walletAndBankMenu(user); break;
                case "5": contactSupport(); break;
                case "6": System.out.println(Colors.color(Colors.PURPLE,"Logged out. 👋")); user.hasSpunThisSession=false; return;

            }
        }
    }

    private static void browseProducts(User user) {
        //  Display available categories dynamically
        Set<String> categories = new HashSet<>();
        for (Product p : products.values()) categories.add(p.category);

        System.out.println("\n" + Colors.color(Colors.BLUE, "Available categories:"));
        for (String c : categories) System.out.println(" - " + c);
        System.out.println("Or type 'all' to see everything.");

        //  Ask user for category
        System.out.print(Colors.color(Colors.GREEN, "Choose category: "));
        String cat = sc.nextLine().trim();

        //  Collect matching products
        List<Product> list = new ArrayList<>();
        for (Product p : products.values()) {
            if (cat.equalsIgnoreCase("all") || p.category.equalsIgnoreCase(cat)) {
                list.add(p);
            }
        }

        //  If none found
        if (list.isEmpty()) {
            System.out.println(Colors.color(Colors.RED, "❌ No products found for this category."));
            return;
        }

        //  Display products
        System.out.println("\n" + Colors.color(Colors.CYAN, "=== Products ==="));

        if (cat.equalsIgnoreCase("all")) {
            // Group products by category manually
            Map<String, List<Product>> grouped = new LinkedHashMap<>();

            // Step 1: Add products to their category lists
            for (Product p : list) {
                if (!grouped.containsKey(p.category)) {
                    grouped.put(p.category, new ArrayList<>());  // create list if not exists
                }
                grouped.get(p.category).add(p);  // add product to the list
            }

            // Step 2: Display grouped products
            for (String categoryName : grouped.keySet()) {
                System.out.println("\n🛍️ " + Colors.bold(categoryName.toUpperCase()) + ":");
                for (Product p : grouped.get(categoryName)) {
                    System.out.printf("  %s | %s | ₹%.2f | qty:%d | ⭐ %.1f/5\n",
                            p.id, p.name, p.price, p.quantity, p.getAverageRating());
                    if (p.quantity < 5)
                        System.out.println(Colors.color(Colors.RED, "    ⚠️ LOW STOCK ALERT"));
                }
            }

        } else {
            // Display products of chosen category
            for (Product p : list) {
                System.out.printf("%s | %s | ₹%.2f | qty:%d | ⭐ %.1f/5\n",
                        p.id, p.name, p.price, p.quantity, p.getAverageRating());
                if (p.quantity < 5)
                    System.out.println(Colors.color(Colors.RED, "  ⚠️ LOW STOCK ALERT"));
            }
        }

        //  Review or Cart actions
        System.out.println("\n" + Colors.color(Colors.YELLOW,
                "💬 You can type 'review <ProductID>' to see reviews or 'rate <ProductID>' to add a review."));
        System.out.println("Or enter product ID directly to add to cart, or 'back' to return.");

        String inputId = sc.nextLine().trim();
        if (inputId.equalsIgnoreCase("back")) return;

        //  Handle review commands
        if (inputId.toLowerCase().startsWith("review ")) {
            String pid = inputId.substring(7).trim();
            Product p = products.get(pid.toUpperCase());
            if (p == null) {
                System.out.println(Colors.color(Colors.RED, "❌ Product not found."));
            } else {
                p.showReviews();
            }
            return;
        }

        if (inputId.toLowerCase().startsWith("rate ")) {
            String pid = inputId.substring(5).trim();
            Product p = products.get(pid.toUpperCase());
            if (p == null) {
                System.out.println(Colors.color(Colors.RED, "❌ Product not found."));
                return;
            }
            System.out.print("Enter rating (1–5): ");
            int rating = readInt();
            if (rating < 1 || rating > 5) {
                System.out.println(Colors.color(Colors.RED, "Invalid rating! Must be 1–5."));
                return;
            }
            System.out.print("Write your review: ");
            String comment = sc.nextLine().trim();
            p.addReview(user.username, rating, comment);
            System.out.println(Colors.color(Colors.GREEN, "✅ Thanks for your feedback!"));
            return;
        }

        //  Add to cart
        Product chosen = null;
        for (Product prod : products.values()) {
            if (prod.id.equalsIgnoreCase(inputId)) {
                chosen = prod;
                break;
            }
        }

        if (chosen == null) {
            System.out.println(Colors.color(Colors.RED, "❌ Invalid product ID."));
            return;
        }

        System.out.print("Enter quantity: ");
        int q = readInt();
        if (q <= 0) {
            System.out.println(Colors.color(Colors.RED, "Quantity must be greater than 0."));
            return;
        }
        if (q > chosen.quantity) {
            System.out.println(Colors.color(Colors.RED, "Not enough stock. Available: " + chosen.quantity));
            return;
        }

        user.cart.add(new OrderItem(chosen, q));
        System.out.println(Colors.color(Colors.GREEN, "✅ Added to cart: " + chosen.name + " x" + q));
    }

    private static void viewOrders(User user){
        if(user.orderHistory.isEmpty()){ System.out.println(Colors.color(Colors.YELLOW,"No orders yet.")); return; }
        System.out.println(Colors.color(Colors.CYAN, "\n--- Your Orders ---"));
        for(Order o:user.orderHistory){
            System.out.println("Order ID: "+o.orderId+" | Total: ₹"+o.total+" | Date: "+new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(o.date));
        }
    }

    private static void walletAndBankMenu(User user){
        while(true){

            user.bankAccount.showBalance();
            if(user.loan!=null){
                System.out.printf("Active Loan: ₹%.2f | Monthly Installment: ₹%.2f\n", user.loan.principal, user.loan.monthlyInstallment);
            }
            System.out.println("\n1) Deposit to Bank   2) Withdraw   3) Pay Loan   4) Take Loan   5) Back");
            System.out.print(Colors.color(Colors.GREEN,"Choose: ")); String ch=sc.nextLine().trim();

            switch(ch){
                case "1":
                    // Deposit flow with registered mobile verification + OTP
                    System.out.print("Enter amount to deposit: ");
                    double d = readDouble();
                    if (d <= 0) { System.out.println(Colors.color(Colors.RED,"Invalid amount.")); break; }

                    System.out.print("Enter your UPI ID: ");
                    String enteredUpi = sc.nextLine().trim();
                    if(!enteredUpi.equals(user.bankAccount.upiId)){
                        System.out.println(Colors.color(Colors.RED,"Invalid UPI ID. Deposit cancelled."));
                        break;
                    }

                    // Verify mobile matches registered bank number
                    System.out.print("Enter phone number for OTP verification: ");
                    String phone = sc.nextLine().trim();
                    if (!phone.equals(user.phoneNumber)) {
                        System.out.println(Colors.color(Colors.RED,"❌ This number is not registered with your bank account."));
                        break;
                    }

                    // generate OTP only after number matches registered
                    int otp = 1000 + rand.nextInt(9000);
                    System.out.println("OTP sent to " + phone + ": " + otp);
                    System.out.print("Enter OTP: ");
                    int enteredOtp = readInt();
                    if (enteredOtp != otp) {
                        System.out.println(Colors.color(Colors.RED,"OTP mismatch. Deposit cancelled."));
                        break;
                    }

                    user.bankAccount.deposit(d);
                    break;

                case "2":
                    System.out.print("Enter amount to withdraw: ");
                    double w = readDouble();
                    
                    if (w <= 0) {
                        System.out.println(Colors.color(Colors.RED,"Invalid amount."));
                        break;
                    }
                    
                    user.bankAccount.withdraw(w);
                    break;   

                case "3":
                    if (user.loan == null) {
                        System.out.println(Colors.color(Colors.YELLOW,"No active loan to pay."));
                        break;
                    }
                
                    System.out.print("Enter amount to pay towards loan: ");
                    double pay = readDouble();
                
                    if (pay <= 0) {
                        System.out.println(Colors.color(Colors.RED,"Invalid amount."));
                        break;
                    }
                
                    // Limit payment to remaining principal
                    if (pay > user.loan.principal) pay = user.loan.principal;
                
                    // Check bank balance first
                    if (user.bankAccount.balance < pay) {
                        System.out.println(Colors.color(Colors.RED,"Not enough bank balance to pay loan."));
                        break;
                    }
                
                    // Verify phone number
                    System.out.print("Enter phone number for OTP verification: ");
                    String payPhone = sc.nextLine().trim();
                
                    if (!payPhone.equals(user.phoneNumber)) {
                        System.out.println(Colors.color(Colors.RED,"❌ This number is not registered with your bank account."));
                        break;
                    }
                
                    // OTP
                    int otpPay = 1000 + rand.nextInt(9000);
                    System.out.println("OTP sent to " + payPhone + ": " + otpPay);
                
                    System.out.print("Enter OTP: ");
                    int enteredOtpPay = readInt();
                
                    if (enteredOtpPay != otpPay) {
                        System.out.println(Colors.color(Colors.RED,"OTP mismatch. Payment cancelled."));
                        break;
                    }
                
                    // Deduct from bank
                    user.bankAccount.withdraw(pay);
                
                    // Reduce loan principal
                    user.loan.principal -= pay;
                
                    System.out.printf(
                        Colors.color(Colors.GREEN,"Paid ₹%.2f towards loan. Remaining principal: ₹%.2f\n"),
                        pay, user.loan.principal
                    );
                
                    // Loan finished
                    if (user.loan.principal <= 0) {
                        System.out.println(Colors.color(Colors.GREEN,"Loan fully repaid!"));
                        user.loan = null;
                    }
                
                    break;

                case "4":
                    if(user.loan != null){
                        System.out.println(Colors.color(Colors.RED,"Already have an active loan."));
                        break;
                    }

                    // Ask for eligibility info
                    System.out.print("Enter your age: ");
                    int userAge = readInt();
                    System.out.print("Enter your PAN number: ");
                    String userPan = sc.nextLine().trim().toUpperCase();
                    System.out.print("Enter your credit score: ");
                    int userCreditScore = readInt();

                    // Eligibility checks
                    if(userAge < 18){
                        System.out.println(Colors.color(Colors.RED,"Loan denied: Must be at least 18 years old."));
                        break;
                    }
                    if(userCreditScore < 650){
                        System.out.println(Colors.color(Colors.RED,"Loan denied: Credit score too low (" + userCreditScore + "). Minimum 650 required."));
                        break;
                    }
                    if(!userPan.matches("[A-Z]{5}[0-9]{4}[A-Z]")){
                        System.out.println(Colors.color(Colors.RED,"Loan denied: Invalid PAN format. Must be 10 characters with letters and digits."));
                        break;
                    }

                    // Loan amount and tenure
                    System.out.print("Enter loan amount: ");
                    double la = readDouble();
                    if(la <= 0){
                        System.out.println(Colors.color(Colors.RED,"Invalid loan amount."));
                        break;
                    }

                    System.out.print("Enter tenure in months: ");
                    int lm = readInt();
                    if(lm <= 0){
                        System.out.println(Colors.color(Colors.RED,"Invalid tenure."));
                        break;
                    }

                    // Annual rate (fixed/default)
                    double lr = 10.0;
                    System.out.println("Annual interest rate: " + lr + "%");

                    // Ask for UPI to receive loan
                    System.out.print("Enter your UPI to receive loan: ");
                    String loanUpi = sc.nextLine().trim();
                    if(!loanUpi.equals(user.bankAccount.upiId)){
                        System.out.println(Colors.color(Colors.RED,"Invalid UPI. Loan cancelled."));
                        break;
                    }

                    // Verify mobile matches registered bank number before sending OTP & granting loan
                    System.out.print("Enter phone number for OTP verification: ");
                    String loanPhone = sc.nextLine().trim();
                    if (!loanPhone.equals(user.phoneNumber)) {
                        System.out.println(Colors.color(Colors.RED,"❌ This number is not registered with your bank account."));
                        break;
                    }

                    int otpLoan = 1000 + rand.nextInt(9000);
                    System.out.println("OTP sent to " + loanPhone + ": " + otpLoan);
                    System.out.print("Enter OTP: ");
                    int enteredOtpLoan = readInt();

                    if(enteredOtpLoan != otpLoan){
                        System.out.println(Colors.color(Colors.RED,"OTP mismatch. Loan cancelled."));
                        break;
                    }
                    
                    // Grant loan
                    user.loan = new Loan(la, lm, lr);
                    
                    // Deposit loan amount to bank
                    user.bankAccount.deposit(la);
                    
                    System.out.printf(Colors.color(Colors.GREEN,
                        "Loan granted: ₹%.2f | Monthly installment: ₹%.2f | Annual rate: %.2f%%\n"),
                        la, user.loan.monthlyInstallment, lr);
                    
                    // Show EMI schedule
                    System.out.println(Colors.color(Colors.CYAN, "\nYour EMI Due Dates:"));
                    user.loan.showLoanCycle();
                    break;


                case "5": return;
                default: System.out.println(Colors.color(Colors.RED,"Invalid choice."));

            }
        }
    }

    // ----------------- VIEW CART & CHECKOUT -----------------
    private static void viewCartAndCheckout(User user){
        if(user.cart.isEmpty()){
            System.out.println(Colors.color(Colors.YELLOW,"🛒 Your cart is empty."));
            return;
        }

        while (true) {
            double subtotal = 0.0;
            System.out.println("\n" + Colors.color(Colors.CYAN,"--- Your Cart ---"));
            System.out.printf("%-4s %-20s %-6s %-10s\n", "No.", "Product", "Qty", "LineTotal");
            for (int i = 0; i < user.cart.size(); i++) {
                OrderItem oi = user.cart.get(i);
                double lineTotal = oi.product.price * oi.qty;
                System.out.printf("%-4d %-20s %-6d ₹%-9.2f\n", i + 1, oi.product.name, oi.qty, lineTotal);
                subtotal += lineTotal;
            }
            System.out.printf("Subtotal: ₹%.2f\n", subtotal);

            System.out.println("\nOptions:");
            System.out.println("1) Remove an item");
            System.out.println("2) Proceed to Checkout (Spin for discount allowed once per session)");
            System.out.println("3) Clear cart");
            System.out.println("4) Back to Menu");
            System.out.print(Colors.color(Colors.GREEN,"Choose: "));
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter item number to remove: ");
                int idx = readInt() - 1;
                if (idx >= 0 && idx < user.cart.size()) {
                    System.out.println(Colors.color(Colors.YELLOW,user.cart.get(idx).product.name + " removed from cart."));
                    user.cart.remove(idx);
                    if(user.cart.isEmpty()){
                        System.out.println(Colors.color(Colors.YELLOW,"Cart is now empty."));
                        return;
                    }
                } else {
                    System.out.println(Colors.color(Colors.RED,"Invalid item number."));
                }
            } else if (choice.equals("2")) {
                // proceed with checkout (spin + payment logic)
                double discount = 0.0;
                int discountPercent = 0;
                if (!user.hasSpunThisSession) {
                    System.out.println("\nDo you want to spin the wheel for up to 10% discount? (yes/no): ");
                    String spinChoice = sc.nextLine().trim();

                    if (spinChoice.equalsIgnoreCase("yes")) {
                        System.out.println("\nSpinning the wheel 🎡 ...");
                        char[] spinChars = {'|', '/', '—', '\\'};
                        for (int i = 0; i < 24; i++) {
                            System.out.print("\r" + spinChars[i % 4] + "  Spinning...");
                            try { Thread.sleep(80); } catch (InterruptedException e) { /* ignore */ }
                        }
                        discountPercent = rand.nextInt(11); // 0–10%
                        discount = subtotal * (discountPercent / 100.0);
                        System.out.printf("\r🎯 Wheel stopped! You got %d%% discount (₹%.2f)\n", discountPercent, discount);
                        user.hasSpunThisSession = true;
                    }
                } else {
                    System.out.println(Colors.color(Colors.YELLOW,"Note: You have already used the spin wheel this session."));
                }
                double total = subtotal - discount;
                System.out.printf("Total to pay: ₹%.2f\n", total);
                
                // ===== Delivery Option =====
                System.out.println("\nSelect Delivery Type:");
                System.out.println("1) Normal Delivery (Free) - arrives in 5 days");
                System.out.println("2) Express Delivery (₹50) - arrives in 2 days");
                System.out.print("Choose delivery option: ");
                String delOpt = sc.nextLine().trim();
                
                int deliveryCharge = 0;
                int days = 5;
                if(delOpt.equals("2")){
                    deliveryCharge = 50;
                    days = 2;
                }
                total += deliveryCharge;
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, days);
                String expectedDate = new SimpleDateFormat("dd-MMM-yyyy").format(cal.getTime());
                
                System.out.println("Delivery Charge: ₹" + deliveryCharge);
                System.out.println("Expected Delivery Date: " + expectedDate);
                System.out.printf("Final Total payable: ₹%.2f\n", total);

                // Payment flow
                
            System.out.println("\nSelect Payment Method:");
            System.out.println("1) Pay using Bank (UPI with OTP)");
            System.out.println("2) Cancel Payment");
            System.out.print("Choose: ");
            String payOpt = sc.nextLine().trim();
            
            if(payOpt.equals("2")){
                System.out.println(Colors.color(Colors.RED,"Payment cancelled."));
                return;
            }
            
            // OTP verification & payment
            int payOtp = 1000 + rand.nextInt(9000);
            System.out.println("OTP sent to " + user.phoneNumber + ": " + payOtp);
            
            System.out.print("Enter OTP: ");
            int enteredPayOtp = readInt();
            if(enteredPayOtp != payOtp){
                System.out.println(Colors.color(Colors.RED,"Incorrect OTP. Payment failed."));
                return;
            }
            // Payment success
            if(!user.bankAccount.withdraw(total)){
                System.out.println(Colors.color(Colors.RED,"Payment failed due to insufficient balance."));
                return;
            }
            
            // ↓↓↓ Reduce stock for each bought item ↓↓↓
            for (OrderItem oi : user.cart) {
                oi.product.quantity -= oi.qty;
            }
            
            // create order & update user history
            String oid = "ORD" + System.currentTimeMillis()%100000;
            Order order = new Order(oid, user.cart, subtotal, discount, total);
            user.orderHistory.add(order);
            user.totalSpent += total;
            System.out.println(Colors.color(Colors.GREEN,"✅ Payment successful!"));
            order.printInvoice();
            
            // clear cart LAST
            user.cart.clear();
            return;

            } else if (choice.equals("3")) {
                user.cart.clear();
                System.out.println(Colors.color(Colors.YELLOW,"Cart cleared."));
                return;
            } else if (choice.equals("4")) {
                return; // back to menu
            } else {
                System.out.println(Colors.color(Colors.RED,"Invalid option."));
            }
        }
    }
    private static void contactSupport(){
        System.out.println("\n" + Colors.color(Colors.CYAN,"--- CONTACT SUPPORT ---"));
        System.out.println("Customer Care Number : 9391097389");
        System.out.println("Customer Email        : support@smartmart.com");
        System.out.println("We are available 24/7 for your help!");
    }


    // ----------------- UTILS -----------------
    private static int readInt(){ try{ return Integer.parseInt(sc.nextLine().trim());} catch(Exception e){return 0;} }
    private static double readDouble(){ try{ return Double.parseDouble(sc.nextLine().trim());} catch(Exception e){return 0.0;} }
}
