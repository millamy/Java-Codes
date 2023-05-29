//Nina Gutovska
//The program finds from the given elements such a combination whose total weight is equal to the capacity of the backpack
// It does this with an iterative (stack-based) and recursive function.
// ogolny opis dzialania programu 
// program ma za zadanie znalezc sposrod podanych elementow taka kombinacje ktorej sumaryczna waga jest rowna pojemnosci plecaka
// Wykonuje to za pomoca funkcji iteracyjnej ( z wykorzystaniem stosu) i rekurencyjnej.
import java.util.Scanner;

public class BackpackProblem {
    public static Scanner scan = new Scanner(System.in);
    public static int[] result;
    public static int isFound = 0; //zmienna dla sledzenia czy znaleziono odpowiedz

//Opis funkcji rec_pakuj
        // funkcja wykorzystuje rekurencje dla znalezienia elementow do wypelnienia plecaka
    public static void rec_pakuj(int[] elements, int currCapacity, int i, int maxCapacity, int elementQuantity,
            int finalElements) {
        if (currCapacity == maxCapacity) { //jesli pojemnosc w danym momencie jest rowna maksymalnej pojemnosci to mamy odpowiedz

            isFound = 1;

        } else if (i == elementQuantity) { //jezeli i == ilosci dostepnych elementow i ojemnosc aktualna i pozadana nie sa sobie rowne to przerywamy 
                                            // dzialanie funkcji bo nie mamy wiecej elementow do dodania
            return;
        } else if (currCapacity > maxCapacity) {
            // jezeli pojemnosc w danym momencie jest wieksza od pozadanej to przerywamy dialanie
            return;
        } else {
            //jezeli pojemnosc jest mniejsza od maksymalnej to dodajemy do finalnej tablicy kolejny element i zwiekszamy
            // maksymalna sume o ten element, rowniez zwiekszamy licznik elementow w finalnej tablicy 
            result[finalElements] = elements[i];
            currCapacity += elements[i];
            finalElements++;
            //wywolujemy rekurencje
            rec_pakuj(elements, currCapacity, i + 1, maxCapacity, elementQuantity, finalElements);
            //jezeli po wyjsciu z rekurencji zmienna isFounfd rowna sie 1 to mamy pozadany wynik i przerywamy dzialannie funkcji 
            if (isFound == 1) {
                return;
            }
            //jezeli zmienna isFound rowna sie 0 to odejmujemy od aktualnej pojemnosci ostatni dodany element, usuwamy go z wynikowej tablicy
            // i wywolujemy ponownie rekurencje
            if (isFound==0){
            currCapacity -= elements[i];
            finalElements--;
            rec_pakuj(elements, currCapacity, i + 1, maxCapacity, elementQuantity, finalElements);
            }
            if (isFound == 1) {
                return;
            }
        }
        // na koncu sprawdzamy czy istnieje kombinacja elementow i jesli tak to wypisujemy wynikowa tablice na ekran
        if (isFound == 1) {
            System.out.print("REC:  " + maxCapacity + " = ");
            for (int k = 0; k < finalElements; k++) {
                System.out.print(result[k] + " ");
            }
            System.out.println();
        }
        return;

    }
    // stos dla funkcji iteracyjnej
    public static class Stack {
        public int[] elements;
        public int maximumCapacity;
        public int finalElements;
        // konstruktor stosu
        Stack(int maxSize) {
            maximumCapacity = maxSize;
            elements = new int[maxSize];
            finalElements = 0;
        }

        public void push(int element) {
            elements[finalElements] = element;
            finalElements++;
        }

        public int pop() {
            if (finalElements == 0) {
                return -1;
            } else{
                finalElements--;
                return elements[finalElements];
            }
        }

        public int putOn() {
            if (finalElements == 0) {
                return -1;
            } else {
                return elements[finalElements - 1];
            }
        }

    }
// funkcja iteracyjna
    public static void iter_pakuj(int[] elements, Stack stack, int maxCapacity) {
        int size = elements.length - 1;
        for (int i = 0; i < size + 1;) {
            stack.push(i);
            // wyznaczamy nowa maksymalna pojemnosc przez odejmowanie od aktualnej pierwszego elementu 
            maxCapacity -= elements[i];
            // jezeli rowna sie 0 to mamy wynik
            if (maxCapacity == 0) {
                return;
            } else if (i == size) {
                // jezeli doszlismy do konca elementow to i nie mamy pozadanej kombinacji to dodajemy element z powrotem 
                maxCapacity += elements[i];
                stack.pop();
                if (stack.finalElements == 0) {//jezeli stos jest pusty to konczymy dzialanie funkcji
                    return;
                }
                maxCapacity += elements[stack.putOn()];
                i = stack.pop(); //ustawiamy i na kolejny element ze stosu bo z poprzednim nie znalezlismy pozadanej kombinacji
            } else if (i != size) {
                if (maxCapacity > 0) {//jezeli ilosc pozostalych elementow jest inna niz i to dodajemy kolejny element
                    i++;
                    continue;
                } else if (maxCapacity < 0) {//jezeli element byl wiekszy niz maksymalna pojemnosc plecaka to dodajemy elemen z powrotem i 
                    // sciagamy ze stosu indeks tego elementu
                    if (i != size) {
                        maxCapacity += elements[i];
                        stack.pop();
                    }
                }
            }
            i++;
        }
    }

    public static void main(String args[]) {
        int dataSets = scan.nextInt();
        for (int i = 0; i < dataSets; i++) {
            int maxCapacity = scan.nextInt();
            int elementQuantity = scan.nextInt();
            int[] elements = new int[elementQuantity];
            for (int j = 0; j < elementQuantity; j++) {
                elements[j] = scan.nextInt();
            }
            result = new int[elementQuantity];
            rec_pakuj(elements, 0, 0, maxCapacity, elementQuantity, 0);
            // jezeli pod koniec dzialania rekurencujnej wersji programu zmienna isFoubd nie przyjela wartosc 1 to wyposujemy BRAK i nie wywolujemy juz
            // funkcje iteracyjna
            if (isFound == 0) {
                System.out.println("BRAK");
            } else {
                //resetujemy zmienna isFound i tworzymy nowy stos
                isFound = 0;
                Stack stack = new Stack(elementQuantity);
                System.out.print("ITER: " + maxCapacity + " = ");
                iter_pakuj(elements, stack, maxCapacity);

                for (int k = 0; k < stack.finalElements; k++) {
                    System.out.print(elements[stack.elements[k]] + " ");
                }
                System.out.println();

            }

        }
    }
}
/*
TESTY:
4
55
5
1 5 10 23 40
20
5
1 2 10 10 5
100
6
1 2 3 4 5 1001
5
5
1 1 1 1 1

WYNIK
REC:  55 = 5 10 40 
ITER: 55 = 5 10 40 
REC:  20 = 10 10
ITER: 20 = 10 10 
BRAK
REC:  5 = 1 1 1 1 1
ITER: 5 = 1 1 1 1 1 
*/