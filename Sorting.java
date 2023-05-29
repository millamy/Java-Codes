//Nina Gutovska 
//A program that will sort data collected in CSV format (comma-separated values) after the selected column
//Ogolny opis dzilania programu:
// program sortuje dane podane w postaci stringow za pomoca Quicksorta i Selectionsorta wedlug podanego numeru kolumny 
//Zlozonosc pamieciowa sortowan wynosi O(1) - nie potrzebuja zadnych dodatkowych tablic czy innych zmiennych ktore 
// zaleza od danych wejsciowych
//Zlozonosc czasowa wynosi O(nlogn) - w srednim przypadku (gdy pivot nie znajduje sie na koncu tablicy) tablica za kazym
// razem jest dzielona na pol, wowczas glebokosc drzewa wywolan zalezy logarytmicznie od danych wejsciowych, zlozonosc
// zamiany elementow jest n wiec T(n) = n log2 n + O(n) = nlogn
import java.util.Scanner;

class Sorting {
    public static Scanner scan = new Scanner(System.in);

    public static void quickSort(String[][] array, int left, int right, int sortBy) {
        int size = array.length;
        int partition = 0;
        int end = 0;
        int tempR = right;

        while (true) {
            end--;

            while (left < tempR) {
                // dzielimy tablice tak zeby po lewej byly wartosci mniejsze od elementu a po prawej wieksze
                partition = partition(array, left, tempR, sortBy);
                array[tempR][sortBy] = "+" + array[tempR][sortBy]; // zaznaczamy zakres na ktorym juz byla wywolana funkcja
                tempR = partition - 1; // zmaniejszamy zakres do sortowania
                ++end;

            }
            if (end < 0)
                break;
            left++;
            tempR = newRight(array, left, size, sortBy); // ustawienie kolejnego zakresu do sortowania 
            array[tempR][sortBy] = array[tempR][sortBy].substring(1); // naprawienie stringa zeby nie zawieral plusa
        }
    }
// funkcja zwraca zakres wartosci ktore nie sa jeszcze posortowane
    private static int newRight(String[][] array, int left, int size, int sortBy) {
        for (int i = left; i < size; ++i) {
            if (array[i][sortBy].startsWith("+"))
                return i;
        }
        return size - 1;
    }
//funkcja do podzialu tablicy
    private static int partition(String[][] array, int left, int right, int sortBy) {
        String pivot = array[(left + right) / 2][sortBy];
        while (left <= right) {
            // sprawdzamy czy wartosc w stringu jest liczba, jesli tak to porownujemy jako liczbe a nie napis
            try {
                int value1Int = Integer.parseInt(array[right][sortBy]);
                int value2Int = Integer.parseInt(array[left][sortBy]);
                int pivotInt = Integer.parseInt(pivot);
                while (value1Int > pivotInt) {
                    right--;
                    value1Int = Integer.parseInt(array[right][sortBy]);
                }
                while (value2Int < pivotInt) {
                    left++;
                    value2Int = Integer.parseInt(array[left][sortBy]);
                }
                //wystapienie exeption oznacza ze w stringu jest napis
            } catch (NumberFormatException e) {
                while (array[right][sortBy].compareTo(pivot) > 0)
                    right--;
                while (array[left][sortBy].compareTo(pivot) < 0)
                    left++;
            }
            if (left <= right) {
                String[] tmp = array[right];
                array[right] = array[left];
                array[left] = tmp;
                left++;
                right--;
            }
        }
        return left;
    }
// Selescionsort do sortowania malych tablic
    public static void selectionSort(String[][] array, int numberOfRows, int sortBy) {
        int smallest;
        String[] temp;

        for (int i = 0; i < numberOfRows; i++) {
            smallest = i;

            for (int j = i + 1; j < numberOfRows; j++) {
                // sprawdzenie czy string jest liczba czy napisem
                try {
                    int Number1 = Integer.parseInt(array[j][sortBy]);
                    int Number2 = Integer.parseInt(array[smallest][sortBy]);
                    if (Number2 > Number1) {
                        smallest = j;
                    }
                } catch (NumberFormatException e) {
                    if (array[smallest][sortBy].compareTo(array[j][sortBy]) > 0) {
                        smallest = j;
                    }
                }

            }

            temp = array[i];
            array[i] = array[smallest];
            array[smallest] = temp;
        }

    }

    public static void main(String[] args) {
        int setQuantity = scan.nextInt();
        String trash = scan.nextLine();
        for (int k = 0; k < setQuantity; k++) {
            String line = scan.nextLine();
            String[] lineVector = line.split(",");
            int numberOfRows = Integer.parseInt(lineVector[0]);
            int sortingColumnNumber = Integer.parseInt(lineVector[1]);
            int isInverse = Integer.parseInt(lineVector[2]);
            String Aline = scan.nextLine();
            String[] titles = Aline.split(",");
            int numberOfColumns = titles.length;
            String[] data = new String[numberOfRows];
            String[][] array = new String[numberOfRows][numberOfColumns];
            for (int i = 0; i < numberOfRows; i++) {
                data[i] = scan.nextLine();
            }
            for (int i = 0; i < numberOfRows; i++) {
                array[i] = data[i].split(",");
            }
            if (numberOfRows <= 5)
                selectionSort(array, numberOfRows, sortingColumnNumber - 1);
            else
                quickSort(array, 0, numberOfRows - 1, sortingColumnNumber - 1);
                // zmiana tablicy tak, zeby pierwsza byla kolumna wedlug ktorej sortujemy
            if (sortingColumnNumber != 1) {
                String temp = "";
                for (int j = sortingColumnNumber - 2; j >= 0; j--) {
                    for (int i = 0; i < numberOfRows; i++) {
                        temp = array[i][j];
                        array[i][j] = array[i][j + 1];
                        array[i][j + 1] = temp;

                    }
                }
                for (int i = sortingColumnNumber - 2; i >= 0; i--) {
                    temp = titles[i + 1];
                    titles[i + 1] = titles[i];
                    titles[i] = temp;
                }
            }
            for (int i = 0; i < numberOfColumns; i++) {
                System.out.print(titles[i]);
                if (i != numberOfColumns - 1)
                    System.out.print(",");

            }
            System.out.println();
            if (isInverse == 1) {
                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        System.out.print(array[i][j]);
                        if (j != numberOfColumns - 1)
                            System.out.print(",");
                    }
                    System.out.println();
                }
                // jezeli kolejnosc sortowania ma byc malejaca to wypisywanie zaczyna sie od konca
            } else {
                for (int i = numberOfRows - 1; i >= 0; i--) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        System.out.print(array[i][j]);
                        if (j != numberOfColumns - 1)
                            System.out.print(",");
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }

    }
}

/*
testy:
2
26,1,1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
ta,tb,290,291
ya,yb,340,341
za,zb,350,351
ka,kb,200,201
va,vb,310,311
ea,eb,140,141
sa,sb,280,281
ba,bb,110,111
xa,xb,330,331
na,nb,230,231
ha,hb,170,171
ga,gb,160,161
ma,mb,220,221
ca,cb,120,121
ia,ib,180,181
ra,rb,270,271
26,4,-1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
ta,tb,290,291
ya,yb,340,341
za,zb,350,351
ka,kb,200,201
va,vb,310,311
ea,eb,140,141
sa,sb,280,281
ba,bb,110,111
xa,xb,330,331
na,nb,230,231
ha,hb,170,171
ga,gb,160,161
ma,mb,220,221
ca,cb,120,121
ia,ib,180,181
ra,rb,270,271

output:
LA,LB,DO,DI
aa,ab,100,101
ba,bb,110,111
ca,cb,120,121
da,db,130,131
ea,eb,140,141
fa,fb,150,151
ga,gb,160,161
ha,hb,170,171
ia,ib,180,181
ja,jb,190,191
ka,kb,200,201
la,lb,210,211
ma,mb,220,221
na,nb,230,231
oa,ob,240,241
pa,pb,250,251
qa,qb,260,261
ra,rb,270,271
sa,sb,280,281
ta,tb,290,291
ua,ub,300,301
va,vb,310,311
wa,wb,320,321
xa,xb,330,331
ya,yb,340,341
za,zb,350,351

DI,LA,LB,DO
351,za,zb,350
341,ya,yb,340
331,xa,xb,330
321,wa,wb,320
311,va,vb,310
301,ua,ub,300
291,ta,tb,290
281,sa,sb,280
271,ra,rb,270
261,qa,qb,260
251,pa,pb,250
241,oa,ob,240
231,na,nb,230
221,ma,mb,220
211,la,lb,210
201,ka,kb,200
191,ja,jb,190
181,ia,ib,180
171,ha,hb,170
161,ga,gb,160
151,fa,fb,150
141,ea,eb,140
131,da,db,130
121,ca,cb,120
111,ba,bb,110
101,aa,ab,100
*/
