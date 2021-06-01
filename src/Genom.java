import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genom implements Comparable<Genom>{

    private final List<Integer> trasa;
    private final int[][] koszty;
    private final int liczbaMiast;
    private final int odleglosc;
    private final boolean czyZerowa;

    public Genom(int liczbaMiast, int[][] koszty, boolean czyZerowa) {
        this.liczbaMiast = liczbaMiast;
        this.koszty = koszty;
        this.czyZerowa = czyZerowa;
        trasa = losowaTrasa();
        odleglosc = this.obliczDlugosc();
    }

    public Genom(List<Integer> permutacjaMiast, int liczbaMiast, int[][] koszty, boolean czyZerowa) {
        this.liczbaMiast = liczbaMiast;
        this.koszty = koszty;
        trasa = permutacjaMiast;
        this.czyZerowa = czyZerowa;
        odleglosc = this.obliczDlugosc();
    }

    private List<Integer> losowaTrasa() {        List<Integer> wynik = new ArrayList<>();

        for (int i = 0; i < liczbaMiast; i++) {
            wynik.add(i);
        }

        if (!czyZerowa) {
            Collections.shuffle(wynik);
        }

        return wynik;
    }

    private int obliczDlugosc() {
        int odleglosc = 0;
        int miasto = trasa.get(0);

        for (int gen : trasa) {
            odleglosc += koszty[miasto][gen];
            miasto = gen;
        }
        odleglosc += koszty[trasa.get(liczbaMiast - 1)][trasa.get(0)];
        return odleglosc;
    }

    public List<Integer> getTrasa() {
        return trasa;
    }

    public int getOdleglosc() {
        return odleglosc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int gene : trasa) {
            sb.append(gene);
            sb.append(" ");
        }
        sb.append(trasa.get(0));
        sb.append(" ");
        sb.append(this.odleglosc);
        return sb.toString();
    }

    @Override
    public int compareTo(Genom o) {
        return Integer.compare(this.odleglosc, o.odleglosc);
    }
}
