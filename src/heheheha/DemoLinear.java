package heheheha;

public class DemoLinear
{
    public static void main(String[] args) // Main list to help print stuff out...thanks AI documentation
    {

        HashTableLinear<String,Integer> ht = new HashTableLinear<>(8, 0.7);

        ht.add("A", 1);
        ht.add("B", 2);
        ht.add("C", 3);
        ht.printState();

        System.out.println("Get B = " + ht.get("B"));

        ht.remove("B");
        ht.printState();

        ht.add("D", 4);
        ht.add("E", 5);
        ht.add("F", 6);
        ht.add("G", 7);
        ht.add("H", 8);
        ht.add("I", 9); // triggers resize

        ht.printState();
    }
}
