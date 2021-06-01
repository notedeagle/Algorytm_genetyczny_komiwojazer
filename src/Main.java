import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<String[]> miasta = new ArrayList<>();
        List<Integer> kolejnosc = new ArrayList<>();
        int wybor;

        System.out.println("""
                Wybierz:
                1. 127 miast
                2. 144 miasta
                0. Wyjście""");

        wybor = in.nextInt();

        try {
            switch (wybor) {
                case 1 -> miasta = odczytajPlik(1);
                case 2 -> miasta = odczytajPlik(2);
                case 0 -> System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Objects.requireNonNull(miasta).size(); i++) {
            kolejnosc.add(i);
        }

        int[][] koszty = new int[miasta.size()][miasta.size()];
        for (int i = 0; i < miasta.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j)
                    koszty[i][j] = 0;
                else {
                    int miastoAIndex = kolejnosc.get(i);
                    int miastoX1 = Integer.parseInt(miasta.get(miastoAIndex)[1]);
                    int miastoY1 = Integer.parseInt(miasta.get(miastoAIndex)[2]);
                    int miastoBIndex = kolejnosc.get(j);
                    int miastoX2 = Integer.parseInt(miasta.get(miastoBIndex)[1]);
                    int miastoY2 = Integer.parseInt(miasta.get(miastoBIndex)[2]);
                    int d = Math.abs(miastoX1 - miastoX2) + Math.abs(miastoY1 - miastoY2);
                    koszty[i][j] = d;
                    koszty[j][i] = d;
                }
            }
        }

        AlgorytmGenetyczny algorytmGenetyczny = new AlgorytmGenetyczny(miasta.size(), koszty);
        Genom wynik = algorytmGenetyczny.start();
        System.out.println(wynik);
    }

    private static List<String[]> odczytajPlik(int number) {

        List<String[]> miasta = new ArrayList<>();
        File plik = null;
        Scanner in;
        String nastepnaWartosc, linia;

        try {
            switch (number) {
                case 1 -> plik = new File("C:\\Projekty\\Java\\Algorytm_genetyczny_komiwojażer\\bier127.tsp");
                case 2 -> plik = new File("C:\\Projekty\\Java\\Algorytm_genetyczny_komiwojażer\\pr144.tsp");
            }

            assert plik != null;
            in = new Scanner(plik);

            while (in.hasNext()) {
                linia = in.nextLine();

                if (linia.equals("NODE_COORD_SECTION")) {
                    while (in.hasNextLine()) {
                        nastepnaWartosc = in.nextLine();

                        if (!nastepnaWartosc.equals("EOF")) {
                            miasta.add(nastepnaWartosc.trim().replaceAll(" +", " ").split(" "));
                        }
                    }
                }
            }
            in.close();
            return miasta;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
