package neuronovasit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Matice {

    public int pocetRadku;
    public int pocetSloupcu;
    public double[][] data;

    public Matice(int pocetRadku, int pocetSloupcu) {
        this.pocetRadku = pocetRadku;
        this.pocetSloupcu = pocetSloupcu;
        data = new double[pocetRadku][pocetSloupcu];
        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                this.data[i][j] = 0;
            }
        }
    }

    public Matice(double[][] data) {
        pocetRadku = data.length;
        pocetSloupcu = data[0].length;
        this.data = new double[pocetRadku][pocetSloupcu];
        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    /*----------------------------------------
				     OPERACE
	-----------------------------------------*/
    boolean zkontrolovatPocetRadkuASloupcuMatice() {
        boolean vysledek;
        if (this.pocetSloupcu != this.pocetRadku) {
            vysledek = false;
        } else {
            vysledek = true;
        }
        return vysledek;
    }

    static boolean zkontrolovatPocetSloupcuAPocetRadkuDvouMatic(Matice A, Matice B) {
        boolean vysledek;
        if (A.pocetSloupcu != B.pocetRadku) {

            vysledek = false;
        } else {
            vysledek = true;
        }
        return vysledek;
    }

    static boolean zkontrolovatPocetRadkuASloupcuDvouMatic(Matice A, Matice B) {
        boolean vysledek;
        if (B.pocetRadku != A.pocetRadku || B.pocetSloupcu != A.pocetSloupcu) {
            vysledek = false;
        } else {
            vysledek = true;
        }
        return vysledek;
    }

    public void soucet(Matice B) throws IncorrectMatrixSizeException {
        if (zkontrolovatPocetRadkuASloupcuDvouMatic(this, B)) {

            for (int i = 0; i < pocetRadku; i++) {
                for (int j = 0; j < pocetSloupcu; j++) {
                    this.data[i][j] = this.data[i][j] + B.data[i][j];
                }
            }

        } else {
            throw new IncorrectMatrixSizeException("Nesprávný rozměr matic.");
        }

    }

    public static Matice rozdil(Matice A, Matice B) throws IncorrectMatrixSizeException {

        if (zkontrolovatPocetRadkuASloupcuDvouMatic(A, B)) {
            Matice vyslednaMatice = new Matice(A.pocetRadku, B.pocetSloupcu);
            for (int i = 0; i < A.pocetRadku; i++) {
                for (int j = 0; j < A.pocetSloupcu; j++) {
                    vyslednaMatice.data[i][j] = A.data[i][j] - B.data[i][j];
                }
            }
            return vyslednaMatice;
        } else {
            throw new IncorrectMatrixSizeException("Nesprávný rozměr matic.");
        }

    }
    
    

    public static Matice nactiZPole(double[] pole) {
        Matice a = new Matice(pole.length, 1);
        for (int i = 0; i < a.pocetRadku; i++) {

            a.data[i][0] = pole[i];

        }
        return a;
    }

    public double[] nactiZMatice() {

        List<Double> arr = new ArrayList<>();
        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                arr.add(this.data[i][j]);
            }
        }

        double[] target = new double[arr.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = arr.get(i);
        }
        return target;

    }

    public void aplikujFunkci(Function<Double, Double> f) {

        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                this.data[i][j] = f.apply(this.data[i][j]);
            }
        }

    }

    public static Matice aplikujFunkci(Matice matice, Function<Double, Double> f) {
        Matice a = matice;
        a.aplikujFunkci(f);
        return a;
    }

    public void soucin(Matice B) throws IncorrectMatrixSizeException {
        if (zkontrolovatPocetRadkuASloupcuDvouMatic(this, B)) {
            for (int i = 0; i < this.pocetRadku; i++) {
                for (int j = 0; j < this.pocetSloupcu; j++) {

                    this.data[i][j] = this.data[i][j] * B.data[i][j];

                }
            }
        } else {
            throw new IncorrectMatrixSizeException("Nesprávný rozměr matic.");
        }

    }

    public static Matice soucin(Matice A, Matice B) throws IncorrectMatrixSizeException {

        if (zkontrolovatPocetSloupcuAPocetRadkuDvouMatic(A, B)) {
            Matice vyslednaMatice = new Matice(A.pocetRadku, B.pocetSloupcu);
            for (int i = 0; i < vyslednaMatice.pocetRadku; i++) {
                for (int j = 0; j < vyslednaMatice.pocetSloupcu; j++) {
                    for (int k = 0; k < A.pocetSloupcu; k++) {
                        vyslednaMatice.data[i][j] += (A.data[i][k] * B.data[k][j]);
                    }
                }
            }
            return vyslednaMatice;
        } else {
            throw new IncorrectMatrixSizeException("Nesprávný rozměr matic.");
        }

    }

    public void soucin(double a) {

        for (int i = 0; i < this.pocetRadku; i++) {
            for (int j = 0; j < this.pocetSloupcu; j++) {

                this.data[i][j] = this.data[i][j] * a;

            }
        }

    }

    public static Matice transponovat(Matice matice) {
        Matice a = new Matice(matice.pocetSloupcu, matice.pocetRadku);
        for (int i = 0; i < matice.pocetRadku; i++) {
            for (int j = 0; j < matice.pocetSloupcu; j++) {
                a.data[j][i] = matice.data[i][j];
            }
        }
        return a;
    }

    public void naplnMaticiNahodne() {
        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                //(Math.random()/2) + 0.5 pro XOR
                // Math.random() * 10 pro AND
                //Math.random()*2-1
                this.data[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    public String toString() {
        String matice = " ";
        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                matice += String.format("%9.4f ", data[i][j]);
                if (j == pocetSloupcu - 1) {
                    matice += "\n ";
                }

            }
        }
        return matice;
    }

    public double[][] toArray() {
        double[][] X = new double[pocetRadku][pocetSloupcu];
        for (int i = 0; i < pocetRadku; i++) {
            for (int j = 0; j < pocetSloupcu; j++) {
                X[i][j] = data[i][j];
            }
        }
        return X;
    }
}
