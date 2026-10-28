import java.util.*;

public class game
{
    public static void main(String[] args)
    {
        game g = new game();
        g.turn();
    }

    Scanner sc = new Scanner(System.in);

    player player1 = new player("player1");
    player player2 = new player("player2");

    asset stock = new stock();
    asset bond = new bond();
    asset gold = new gold();
    asset crypto = new crypto();
    marketengine marketEngine= new marketengine();
    gamerules rules= new gamerules();

    int currentplayer = 1;
    int round = 1;

    void turn()
    {
        for(round = 1; round <= 3; round++)
        {
            System.out.println("\n-round " + round + "-");

            for(currentplayer = 1; currentplayer <= 2; currentplayer++)
            {
                boolean done = false;

                while(!done)
                {
                    player p = (currentplayer == 1) ? player1 : player2;

                    System.out.println("\n" + p.getname() + "'s turn");
                    System.out.println("\n~menu~");
                    System.out.println("1.view market");
                    System.out.println("2.sell asset");
                    System.out.println("3.buy");
                    System.out.println("4.view my portfolio");
                    System.out.println("5.view balance");
                    System.out.println("6.end turn");
                    System.out.println("\nenter choice:");

                    int choice = sc.nextInt();

                    if(choice == 1)
                    {
                        market();
                    }
                    else if(choice == 2)
                    {
                        sell(p);
                    }
                    else if(choice == 3)
                    {
                        buy(p);
                    }
                    else if(choice == 4)
                    {
                        p.portfolio();
                    }
                    else if(choice == 5)
                    {
                        balance();
                    }
                    else if(choice == 6)
                    {
                        done = true;
                    }
                    else
                    {
                        System.out.println("choose from 1-6");
                    }
                }

            }

            marketEngine.updateprices(stock, bond, gold, crypto);
            System.out.println("\nmarket changed");
            market();
        }

        winner();
    }

    void market()
    {
        marketEngine.showmarket(stock, bond, gold, crypto); 

    }

    void buy(player p)
    {
        System.out.println("\n-buy an asset-");
        System.out.println("1. stock: " + stock.price);
        System.out.println("2. bond: " + bond.price);
        System.out.println("3. gold: " + gold.price);
        System.out.println("4. crypto: " + crypto.price);

        System.out.println("\nchoose asset to buy:");
        int choice = sc.nextInt();

        System.out.println("\nenter quantity to buy:");
        int quantity = sc.nextInt();

        if(choice == 1 && rules.canbuy(p, stock, quantity))
        {
            p.removemoney(stock.getvalue(quantity));
            p.addstock(quantity);
            System.out.println("stock purchased");
        }
        else if(choice == 2 && rules.canbuy(p, bond, quantity))
        {
            p.removemoney(bond.getvalue(quantity));
            p.addbond(quantity);
            System.out.println("bond purchased");
        }
        else if(choice == 3 && rules.canbuy(p, gold, quantity))
        {
            p.removemoney(gold.getvalue(quantity));
            p.addgold(quantity);
            System.out.println("gold purchased");
        }
        else if(choice == 4 && rules.canbuy(p, crypto, quantity))
        {
            p.removemoney(crypto.getvalue(quantity));
            p.addcrypto(quantity);
            System.out.println("crypto purchased");
        }
        else
        {
            System.out.println("insufficient amount or invalid choice");
        }    
    }

    
void sell(player p)
{
    System.out.println("\n-sell an asset-");
    System.out.println("1. stock");
    System.out.println("2. bond");
    System.out.println("3. gold");
    System.out.println("4. crypto");

    System.out.println("\nchoose asset to sell:");
    int choice = sc.nextInt();

    System.out.println("\nenter quantity to sell:");
    int quantity = sc.nextInt();

    if(choice == 1 && rules.cansell(p.getstock(), quantity))
    {
        p.removestock(quantity);
        p.addmoney(stock.getvalue(quantity));
        System.out.println("stock sold");
    }
    else if(choice == 2 && rules.cansell(p.getbond(), quantity))
    {
        p.removebond(quantity);
        p.addmoney(bond.getvalue(quantity));
        System.out.println("bond sold");
    }
    else if(choice == 3 && rules.cansell(p.getgold(), quantity))
    {
        p.removegold(quantity);
        p.addmoney(gold.getvalue(quantity));
        System.out.println("gold sold");
    }
    else if(choice == 4 && rules.cansell(p.getcrypto(), quantity))
    {
        p.removecrypto(quantity);
        p.addmoney(crypto.getvalue(quantity));
        System.out.println("crypto sold");
    }
    else
    {
        System.out.println("invalid choice or insufficient holdings");
    }
}


   
void balance()
{
    System.out.println("\n" + player1.getname() + " balance: " + player1.getmoney());
    System.out.println(player2.getname() + " balance: " + player2.getmoney());
}


    
    

void winner()
{
    int total1 = player1.totalvalue(stock, bond, gold, crypto);
    int total2 = player2.totalvalue(stock, bond, gold, crypto);

    System.out.println("\n-final result-");
    System.out.println(player1.getname() + " total wealth: " + total1);
    System.out.println(player2.getname() + " total wealth: " + total2);

    if(total1 > total2)
    {
        System.out.println(player1.getname() + " wins");
    }
    else if(total2 > total1)
    {
        System.out.println(player2.getname() + " wins");
    }
    else
    {
        System.out.println("its a tie");
    }
}



}


class player
{
    private String name;
    private int money = 10000;

    private int stock = 0;
    private int bond = 0;
    private int gold = 0;
    private int crypto = 0;

    player(String name)
    {
        this.name = name;
    }

    String getname()
    {
        return name;
    }

    int getmoney()
    {
        return money;
    }

    int getstock()
    {
        return stock;
    }

    int getbond()
    {
        return bond;
    }

    int getgold()
    {
        return gold;
    }

    int getcrypto()
    {
        return crypto;
    }

    void addmoney(int amount)
    {
        money += amount;
    }

    void removemoney(int amount)
    {
        money -= amount;
    }

    void addstock(int quantity)
    {
        stock += quantity;
    }

    void addbond(int quantity)
    {
        bond += quantity;
    }

    void addgold(int quantity)
    {
        gold += quantity;
    }

    void addcrypto(int quantity)
    {
        crypto += quantity;
    }

    void removestock(int quantity)
    {
        stock -= quantity;
    }

    void removebond(int quantity)
    {
        bond -= quantity;
    }

    void removegold(int quantity)
    {
        gold -= quantity;
    }

    void removecrypto(int quantity)
    {
        crypto -= quantity;
    }

    void portfolio()
    {
        System.out.println("\n-" + name + "'s portfolio-");
        System.out.println("current balance: " + money);
        System.out.println("stocks: " + stock);
        System.out.println("bonds: " + bond);
        System.out.println("gold: " + gold);
        System.out.println("crypto: " + crypto);
    }

    int totalvalue(asset stock, asset bond, asset gold, asset crypto)
    {
        return money
            + stock.getvalue(this.stock)
            + bond.getvalue(this.bond)
            + gold.getvalue(this.gold)
            + crypto.getvalue(this.crypto);
    }
}


interface tradable
{
    int getvalue(int quantity);
}




abstract class asset implements tradable
{
    String name;
    int price;

    asset(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    public abstract int getvalue(int quantity);
}


class stock extends asset
{
    stock()
    {
        super("stock", 500);
    }

    public int getvalue(int quantity)
    {
        return price * quantity;
    }
}

class bond extends asset
{
    bond()
    {
        super("bond", 300);
    }

    public int getvalue(int quantity)
    {
        return price * quantity;
    }
}

class gold extends asset
{
    gold()
    {
        super("gold", 700);
    }

    public int getvalue(int quantity)
    {
        return price * quantity;
    }
}

class crypto extends asset
{
    crypto()
    {
        super("crypto", 1000);
    }

    public int getvalue(int quantity)
    {
        return price * quantity;
    }
}



class marketengine
{
    Random rand = new Random();

    void showmarket(asset stock, asset bond, asset gold, asset crypto)
    {
        System.out.println("\n-market prices-");
        System.out.println("stock: " + stock.price);
        System.out.println("bond: " + bond.price);
        System.out.println("gold: " + gold.price);
        System.out.println("crypto: " + crypto.price);
    }

    void changeprice(asset a, int maxpercent)
    {
        int change = rand.nextInt(2 * maxpercent + 1) - maxpercent; // -max to +max
        a.price = Math.max(1, a.price + a.price * change / 100);
    }

    void updateprices(asset stock, asset bond, asset gold, asset crypto)
    {
        changeprice(stock, 10);   
        changeprice(bond, 3);     
        changeprice(gold, 6);     
        changeprice(crypto, 25);  
    }
}


class gamerules
{
    
boolean canbuy(player p, asset a, int quantity)
{
    return quantity > 0 && (long) a.price * quantity <= p.getmoney();
}


    boolean cansell(int holdings, int quantity)
    {
        return quantity > 0 && holdings >= quantity;
    }
}