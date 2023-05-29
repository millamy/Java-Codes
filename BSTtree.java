//Nina Gutovska
/*
 Program jest implementacja drzewa BST , zawiera moduly kolekowania, raportowania i edycji, zlozonosc pojedynczych funkcji jest
 nie wieksza niz liniowa, zadna petla nie wykonuje sie wiecej niz n razy.
 */

import java.util.Scanner;

public class BSTtree {
    public static Scanner scan = new Scanner(System.in);
    public static Tree newTree;
    public static int index;

    //stos przechowujacy wartosci typu node
    public static class NodeStack {

        public class stackNode {
            public Node data;
            public stackNode Next;

            public stackNode(Node Data) {
                this.data = Data;
            }
        }

        public stackNode head;

        public boolean empty() {

            return head == null;

        }

        public Node pop() {
            Node Data = head.data;
            head = head.Next;
            return Data;
        }

        public void push(Node Data) {
            stackNode mode = new stackNode(Data);
            mode.Next = head;
            head = mode;
        }
    }

    public static class Person {
        public int priority;
        public String name;
        public String surname;

        Person(int priority, String name, String surname) {
            this.priority = priority;
            this.name = name;
            this.surname = surname;
        }
    }

    static class Node {
        public Person info;
        public Node left;
        public Node right;

        public Node(Person person) {
            info = person;
            left = null;
            right = null;
        }
    }

    public static class Tree {
        public static Node root;

        public Tree() {
            root = null;
        }
       
        // metoda tworzaca drzeewo bst w kolejnosci postorder
        public static Node createPostorder(Person[] people, int begin, int end) {

            int peoplesPriority = people[index].priority;

            // sprawdzamy zakres elementu, czy nie przekracza dlugosci tablicy i miesi sie w zakresie wyznaczonym przez inne priorytety
            if (index >= 0 && peoplesPriority >= begin && peoplesPriority <= end) {
                //tworzymy nowy wezel i zmniejeszamy wartosc indeksu
                Node group = new Node(people[index]);

                if (index != 0) {
                    index--;
                }

                // wywolujemy rekurencynie dla prawej i lewej podtablicy
                group.right = createPostorder(people, peoplesPriority + 1, end);
                group.left = createPostorder(people, begin, peoplesPriority - 1);

                return group;
            }
            return null;
        }

        // metoda ta tworzy drzewo bst w kolejnosci preorder
        public static Node createPreorder(Person[] people, int begin, int end, int length) {

            int peoplesPriority = 0;

            if (index <= length) {
                peoplesPriority = people[index].priority;
            }
            // sprawdzamy zakres elementu, czy nie przekracza dlugosci tablicy i miesci sie w zakresie wyznaczonym przez inne priorytety
            if (index <= length && peoplesPriority >= begin && peoplesPriority <= end) {
                // tworzenie nowego wezla i zwiekszanie indeksu
                Node group = new Node(people[index]);
                index++;
                //wywolujemy rekurencyjnie metode dla lewego i prawego poddrzewa
                if (index <= length) {
                    group.left = createPreorder(people, begin, peoplesPriority - 1, length);
                    group.right = createPreorder(people, peoplesPriority + 1, end, length);
                }

                return group;
            }

            return null;
        }

        public static void preorder() {

            NodeStack Stack = new NodeStack();
            Node person = root;
            System.out.print("PREORDER: ");

            while (person != null || Stack.empty()==false ) {

                if (person != null) {
                    System.out.print(person.info.priority + " - " + person.info.name + " " + person.info.surname);
                    Stack.push(person.right);
                    person = person.left;

                } else {
                    person = Stack.pop();
                }

                if (person != null) {
                    System.out.print(", ");
                }
            }

        }

        public static void postorder() {
            NodeStack stack = new NodeStack();
            NodeStack result = new NodeStack();

            System.out.print("POSTORDER: ");

            Node person = root;
            stack.push(person);

            while (stack.empty() == false) {

                person = stack.pop();
                result.push(person);

                if (person.left != null){
                    stack.push(person.left);
                }

                if (person.right != null){
                    stack.push(person.right);
                }
            }

            while (result.empty() == false) {

                person = result.pop();

                System.out.print(person.info.priority + " - " + person.info.name + " " + person.info.surname);

                if (result.empty() == false) {
                    System.out.print(", ");
                }

            }

        }

        public static void inorder() {
            NodeStack Stack = new NodeStack();
            Node person = root;
            System.out.print("INORDER: ");
            while (person != null || !Stack.empty()) {
                if (person != null) {
                    Stack.push(person);
                    person = person.left;
                } else {
                    person = Stack.pop();
                    System.out.print(person.info.priority + " - " + person.info.name + " " + person.info.surname);
                    person = person.right;
                    if (!Stack.empty() || person != null) {
                        System.out.print(", ");
                    }
                }
            }

        }

        // metoda delete usuwa wezel o wskazanym priorytecie
        public static void delete(int priority) {
            Node previousPerson = null;
            Node currentPerson = root;
            // wyszukujemy osobe o wskazanym priorytecie
            while (currentPerson != null && priority != currentPerson.info.priority) {
                previousPerson = currentPerson;
                if (priority < currentPerson.info.priority) {
                    currentPerson = currentPerson.left;
                } else
                    currentPerson = currentPerson.right;
            }
            // jezeli po zakonczeniu wyszukiwania napotkalismy nulla to takak osoba nie istnieje
            if (currentPerson == null) {
                System.out.println("DELETE " + priority + ": BRAK");
                return;
            }
            // jezeli dany wezel nie ma potomkow to usuwamy jedynie jego referencje
            if (currentPerson.left == null && currentPerson.right == null) {
                if (currentPerson == root) {
                    root = null;
                } else {
                    if (previousPerson.left != currentPerson) {
                        previousPerson.right = null;
                    } else {
                        previousPerson.left = null;
                    }
                }
                // jezeli ma jednego potomka zamieniamy jego referencje z referencja rodzica
            } else if (currentPerson.left != null && currentPerson.right == null) {
                if (currentPerson == root) {
                    root = currentPerson.left;
                } else {
                    if (currentPerson == previousPerson.left) {
                        previousPerson.left = currentPerson.left;
                    } else {
                        previousPerson.right = currentPerson.left;
                    }
                }
                // to samo tylko z prawym dzieckiem 
            } else if (currentPerson.left == null && currentPerson.right != null) {
                if (currentPerson == root) {
                    root = currentPerson.right;
                } else {
                    if (currentPerson == previousPerson.left) {
                        previousPerson.left = currentPerson.right;
                    } else {
                        previousPerson.right = currentPerson.right;
                    }
                }
                // jezeli ma dwojke potomkiw to zamienia referencje rodzica z nastepnikiem
            } else {
                Node nextPerson = currentPerson.right;
                Node parent = currentPerson;
                // wyszukiwanie nastepnika
                while (nextPerson.left != null) {
                    parent = nextPerson;
                    nextPerson = nextPerson.left;
                }
                if (parent == currentPerson) {
                    if (currentPerson == root) {
                        // lewe poddrzewo nastepnika ustawiamy na lewe poddrzewo
                        currentPerson.right.left = currentPerson.left;
                        root = currentPerson.right;
                    } else {
                        currentPerson.right.left = currentPerson.left;

                        if (previousPerson.left == currentPerson) {
                            previousPerson.left = currentPerson.right;
                        } else {
                            previousPerson.right = currentPerson.right;
                        }
                    }
                } else {
                    currentPerson.info = nextPerson.info;
                    // jesli nastepnik ma prawe drzewo to zmieniamy referencje rodzica na to poddrzewo
                    if (nextPerson.right != null) {
                        parent.left = nextPerson.right;
                    } else {
                        parent.left = null;
                    }
                }

            }

        }
        // metoda przechodzi po drzewie maksymalnie w prawo i usuwa najwiekszy element
        public static Node dequemax() {
            Node currentPerson = root;
            Node previousPerson = root;
            // przechodzimy do konca prawego poddrzewa
            while (currentPerson.right != null) {
                previousPerson = currentPerson;
                currentPerson = currentPerson.right;
            }
            // jezeli dalej jestesmy na root to ustaiamy go na null
            if (previousPerson == currentPerson) {

                if (previousPerson.left == null) {
                    root = null;

                } else {
                    root = previousPerson.left;
                }

            } else {
                // jezeli w przeszukiwaniu przesunelismy sie dalej tu ustawiamy nastepnika na lewe poddrzewo
                if (currentPerson.left == null) {
                    previousPerson.right = null;

                } else {
                    previousPerson.right = null;
                    previousPerson.right = currentPerson.left;
                }
            }
            return currentPerson;
        }
        
          // metoda przechodzi po drzewie maksymalnie w lewo i usuwa najmniejszy element
        public static Node dequemin() {
            Node currentPerson = root;
            Node previousPerson = root;
            // przesuwamy sie maksymalnie w lewo po drzewie
            while (currentPerson.left != null) {
                previousPerson = currentPerson;
                currentPerson = currentPerson.left;
            }

            //jezeli dalej jestesmy na root to ustawiamy go na null
            if (previousPerson == currentPerson) {

                if (previousPerson.right == null) {
                    root = null;
                } else {
                    root = previousPerson.right;
                }

            } else {
                // w przeciwnym razie ustawiamy nastepnika na prawe poddrzewo
                if (currentPerson.right == null) {
                    previousPerson.left = null;
                } else {
                    previousPerson.left = currentPerson.right;
                }

            }
            return currentPerson;
        }

        public static int height(Node person) {
            // jezeli wezel wskazuje na null to zwaracamy 0 jako wysokosc drzewa
            if (person == null)
                return 0;
            else
                // w przeciwnym razie zwracamy maximum z wysokosci lewego i prawego poddrzewa
                return Math.max(height(person.left), height(person.right)) + 1;

        }

        // metoda dodaje nowa osobe do drzewa
        public static void enque(Person person) {
            // tworzymy nowy wezel 
            Node newNode = new Node(person);
            Node currentPerson = root;
            Node previousPerson = root;
            // przesuwamy sie po drzewie do nulla
            while (currentPerson != null) {
                previousPerson = currentPerson;
                // jezeli znalezlismy osobe z priorytetem 
                if (person.priority >= currentPerson.info.priority) {
                    currentPerson = currentPerson.right;
                } else {
                    currentPerson = currentPerson.left;
                }
            }
            if (previousPerson == currentPerson) {
                root = newNode;
            } else if (person.priority > previousPerson.info.priority) {
                previousPerson.right = newNode;
            } else if (person.priority < previousPerson.info.priority) {
                previousPerson.left = newNode;
            }
        }

        //metoda zwraca  poprzednika ktorego priorytet jest ostro mniejszy
        public static Node prev(int priority) {
            Node person = root;
            Node previous = null;
            boolean isFound = false;
            while (person != null) {
                if (person.info.priority == priority) {

                    // jesli priorytety sa sobie rowne to idziemy raz w lewo i do konca w prawo                    
                    if (person.left != null) {
                        previous = person.left;
                        while (previous.right != null) {
                            previous = previous.right;
                        }
                    }
                    return previous;
                    // jesli znalezlismy osob o priorytecie mniejszym to zapisujemy ja
                } else if (person.info.priority < priority) {

                    previous = person;
                    person = person.right;
                } else
                    person = person.left;
            }
            // jak nie znalezlismy to zwracamy null
            return isFound ? previous : null;
        }
        // funkcja zwraca nastepnika o priorytecie ostro wiekszym niz podany 
        public static void next(int priority) {
            Node persone = root;
            Node next = null;
            boolean isFound = false;
            while (persone != null) {
                if (persone.info.priority == priority) {
                    // odwrotnie do poprzedniej funkcji idziemy raz w prawo i do konca w lewo
                    if (persone.right != null) {
                        next = persone.right;
                        while (next.left != null)
                            next = next.left;
                    }
                    // wypisujemy informacje o nastepniku
                    System.out.println("NEXT " + priority + ": " + next.info.priority + " - " + next.info.name + " "
                            + next.info.surname);
                    return;
                } else if (persone.info.priority < priority) {
                    persone = persone.right;
                } else {
                    next = persone;
                    persone = persone.left;
                }
            }
            //jesli w trakcie dziania funkcji nie znalezlismy nastepnika wypisujemy brak
            if (isFound) {
                System.out.println("NEXT " + priority + ": " + next.info.priority + " - " + next.info.name + " "
                        + next.info.surname);
            } else {
                System.out.println("NEXT " + priority + ": BRAK");
            }

           
        }

    }

    public static void main(String[] args) {
        int setQuantity = scan.nextInt();
        for (int i = 0; i < setQuantity; i++) {
            System.out.println("ZESTAW " + (i + 1));
            newTree = new Tree();
            int commandsQuantity = scan.nextInt();
            for (int j = 0; j < commandsQuantity; j++) {
                String command = scan.next();
                if (command.equals("CREATE")) {
                    String order = scan.next();
                    int elements = scan.nextInt();
                    Person[] people = new Person[elements];
                    for (int k = 0; k < elements; k++) {
                        int priority = scan.nextInt();
                        String name = scan.next();
                        String surname = scan.next();
                        people[k] = new Person(priority, name, surname);
                    }
                    if (order.equals("POSTORDER")) {
                        index = people.length - 1;
                        Tree.root = Tree.createPostorder(people, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    }
                    if (order.equals("PREORDER")) {
                        index = 0;
                        Tree.root = Tree.createPreorder(people, Integer.MIN_VALUE, Integer.MAX_VALUE, people.length-1);
                    }

                }
                if (command.equals("PREORDER")) {
                    Tree.preorder();
                    System.out.println();

                }
                if (command.equals("POSTORDER")) {
                    Tree.postorder();
                    System.out.println();

                }
                if (command.equals("INORDER")) {
                    Tree.inorder();
                    System.out.println();

                }
                if (command.equals("HEIGHT")) {
                    if (Tree.root == null) {
                        System.out.println("HEIGHT: 0");
                    } else {
                        System.out.println("HEIGHT: " + (Tree.height(Tree.root) - 1));
                    }

                }
                if (command.equals("DELETE")) {
                    Tree.delete(scan.nextInt());
                }
                if (command.equals("DEQUEMIN")) {
                    Node minPerson = Tree.dequemin();
                    System.out.println("DEQUEMIN: " + minPerson.info.priority + " - " + minPerson.info.name + " "
                            + minPerson.info.surname);

                }
                if (command.equals("DEQUEMAX")) {
                    Node maxPerson = Tree.dequemax();
                    System.out.println("DEQUEMAX: " + maxPerson.info.priority + " - " + maxPerson.info.name + " "
                            + maxPerson.info.surname);
                }
                if (command.equals("ENQUE")) {
                    Person newPerson = new Person(scan.nextInt(), scan.next(), scan.next());
                    Tree.enque(newPerson);
                }
                if (command.equals("NEXT")) {
                    int priority = scan.nextInt();
                    Tree.next(priority);

                }
                if (command.equals("PREV")) {
                    int priority = scan.nextInt();
                    Node prevPersone = Tree.prev(priority);
                    if (prevPersone == null)
                        System.out.println("PREV " + priority + ": BRAK");
                    else
                        System.out.println("PREV " + priority + ": " + prevPersone.info.priority + " - "
                                + prevPersone.info.name + " " + prevPersone.info.surname);

                }

            }
        }

    }

}

/*
1
10
CREATE POSTORDER 5 10 adam adamowicz 9 bartosz badamowicz 8 cyntia cadamowicz 7 dorota dadamoicz 6 ewelina edamowicz 
INORDER
PREORDER
POSTORDER
DEQUEMAX
INORDER
DEQUEMIN
INORDER
ENQUE 100 zenek zabamowicz
HEIGHT

output:
ZESTAW 1
INORDER: 6 - ewelina edamowicz, 7 - dorota dadamoicz, 8 - cyntia cadamowicz, 9 - bartosz badamowicz, 10 - adam adamowicz
PREORDER: 6 - ewelina edamowicz, 7 - dorota dadamoicz, 8 - cyntia cadamowicz, 9 - bartosz badamowicz, 10 - adam adamowicz
POSTORDER: 10 - adam adamowicz, 9 - bartosz badamowicz, 8 - cyntia cadamowicz, 7 - dorota dadamoicz, 6 - ewelina edamowicz
DEQUEMAX: 10 - adam adamowicz
INORDER: 6 - ewelina edamowicz, 7 - dorota dadamoicz, 8 - cyntia cadamowicz, 9 - bartosz badamowicz
DEQUEMIN: 6 - ewelina edamowicz
INORDER: 7 - dorota dadamoicz, 8 - cyntia cadamowicz, 9 - bartosz badamowicz
HEIGHT: 3
 */