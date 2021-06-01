import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.time.temporal.ChronoUnit.SECONDS;

public class AlgorytmGenetyczny {
    private final int wielkoscPopulacji;
    private final int wielkoscGenomu;
    private final int liczbaMiast;
    private final int wielkoscElity;
    private final double wspolczynnikMutacji;
    private final int wielkoscGrup;
    private final int[][] koszty;
    private final long koncowyCzas;
    private final LocalTime czas;
    private int min;
    private Genom theBest;

    public AlgorytmGenetyczny(int liczbaMiast, int[][] koszty) {
        this.liczbaMiast = liczbaMiast;
        this.koszty = koszty;
        wielkoscGenomu = liczbaMiast;
        wielkoscPopulacji = 5000;
        wielkoscElity = 200;
        wielkoscGrup = 40;
        wspolczynnikMutacji = 0.1;
        czas = LocalTime.now();
        long start = System.currentTimeMillis();
        koncowyCzas = start + 300 * 1000;
        min = Integer.MAX_VALUE;
    }

    public Genom start() {
        List<Genom> populacja = pierwszaPopulacja();
        Genom najlepszyGenom = Collections.min(populacja);

        while (System.currentTimeMillis() < koncowyCzas) {

            if (najlepszyGenom.getOdleglosc() < min) {
                min = najlepszyGenom.getOdleglosc();
                theBest = najlepszyGenom;
                System.out.println("Najlepszy: " + min);
                System.out.println(SECONDS.between(czas, LocalTime.now()) + " sekund");
            }

            List<Genom> najlepszy = selekcja(populacja);
            populacja = tworzGeneracje(najlepszy);
            najlepszyGenom = Collections.min(populacja);
        }

        System.out.println("Finalnie najlepszy: " + theBest.getOdleglosc());
        System.out.println(theBest.getTrasa().size());
        zapiszPlik(theBest.toString());

        return theBest;
    }

    private List<Genom> pierwszaPopulacja() {
        List<Genom> populacja = new ArrayList<>();

        populacja.add(new Genom(liczbaMiast, koszty, true));

        for (int i = 0; i < wielkoscPopulacji - 1; i++) {
            populacja.add(new Genom(liczbaMiast, koszty, false));
        }
        return populacja;
    }

    private List<Genom> selekcja(List<Genom> populacja) {
        List<Genom> najlepszy = new ArrayList<>();

        for (int i = 0; i < wielkoscElity; i++) {
            najlepszy.add(selekcjaTurniejowa(populacja));
        }

        return najlepszy;
    }

    private Genom selekcjaTurniejowa(List<Genom> populacja) {
        List<Genom> najlepszy = wybierzLosowy(populacja, wielkoscGrup);

        return Collections.min(najlepszy);
    }

    private List<Genom> wybierzLosowy(List<Genom> populacja, int wielkoscGrup) {
        Random ran = new Random();
        int wielkoscPopulacji = populacja.size();

        for (int i = wielkoscPopulacji - 1; i >= wielkoscPopulacji - wielkoscGrup; i--) { //--i
            Collections.swap(populacja, i, ran.nextInt(i + 1));
        }
        return populacja.subList(wielkoscPopulacji - wielkoscGrup, wielkoscPopulacji);
    }

    private List<Genom> tworzGeneracje(List<Genom> najlepszy) {
        int wielkscGeneracji = 0;
        List<Genom> generacja = new ArrayList<>();

        while (wielkscGeneracji < wielkoscPopulacji) {
            List<Genom> rodzice = wybierzLosowy(najlepszy, 2);
            List<Genom> dzieci = krzyzowanie(rodzice);
            dzieci.set(0, mutacja(dzieci.get(0)));
            dzieci.set(1, mutacja(dzieci.get(1)));
            generacja.addAll(dzieci);
            wielkscGeneracji += 2;
        }
        return generacja;
    }

    //Krzyzowanie PMX
    private List<Genom> krzyzowanie(List<Genom> rodzice) {
        Random ran = new Random();

        List<Genom> dzieci = new ArrayList<>();
        int punkt1 = ran.nextInt(wielkoscGenomu);
        int punkt2 = ran.nextInt(wielkoscGenomu - punkt1) + punkt1;

        List<Integer> rodzic1 = new ArrayList<>(rodzice.get(0).getTrasa());
        List<Integer> rodzic2 = new ArrayList<>(rodzice.get(1).getTrasa());


        for (int i = punkt1; i < punkt2; i++) {
            int temp = rodzic1.get(i);
            Collections.swap(rodzic2, rodzic2.indexOf(temp), i);
        }

        dzieci.add(new Genom(rodzic2, liczbaMiast, koszty, false));
        rodzic2 = rodzice.get(1).getTrasa();

        for (int i = punkt1; i < punkt2; i++) {
            int temp = rodzic2.get(i);
            Collections.swap(rodzic1, rodzic1.indexOf(temp), i);
        }

        dzieci.add(new Genom(rodzic1, liczbaMiast, koszty, false));

        return dzieci;
    }

    private Genom mutacja(Genom genom) {
        Random ran = new Random();
        double mutacja = ran.nextDouble();

        if (mutacja < wspolczynnikMutacji) {
            List<Integer> nowyGenom = genom.getTrasa();
            Collections.swap(nowyGenom, ran.nextInt(wielkoscGenomu), ran.nextInt(wielkoscGenomu));
            return new Genom(nowyGenom, liczbaMiast, koszty, false);
        }
        return genom;
    }

    private void zapiszPlik(String doZapisu) {
        try {
            PrintWriter zapis = new PrintWriter(new BufferedWriter(new FileWriter("wynik.txt", true)));
            zapis.println(doZapisu);
            zapis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
