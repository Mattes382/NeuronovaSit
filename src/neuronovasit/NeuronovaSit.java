/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronovasit;

import java.util.Random;
import java.util.function.Function;

/**
 *
 * @author Matej
 */
public class NeuronovaSit {

    int pocetVstupu;
    int skryteNeurony1;

    int pocetVystupu;
    //pro 1 hidden layer
    Matice vahy_ih;
    Matice vahy_ho;
    //bias a learning rate
    Matice bias_h;
    Matice bias_o;
    double learningRate = 0.5;

    Function<Double, Double> sigmoid = x -> 1 / (1 + Math.pow(Math.E, -x));
    Function<Double, Double> derivujsigmoid = y -> sigmoid.apply(y) * (1 - sigmoid.apply(y));
    Function<Double, Double> inverze = y -> -y;
//    Function<Double, Double> tanh = x -> Math.tanh(x);
//    Function<Double, Double> derivujtanh = y -> 1 - (sigmoid.apply(y) * sigmoid.apply(y));
    Function<Double, Double> mutujNeurony;

    public NeuronovaSit(int pocetVstupu, int skryteNeurony1, int pocetVystupu) {
        this.pocetVstupu = pocetVstupu;
        this.skryteNeurony1 = skryteNeurony1;
        this.pocetVystupu = pocetVystupu;

        vahy_ih = new Matice(this.skryteNeurony1, this.pocetVstupu);
        vahy_ho = new Matice(this.pocetVystupu, this.skryteNeurony1);
        vahy_ih.naplnMaticiNahodne();
        vahy_ho.naplnMaticiNahodne();

        bias_h = new Matice(this.skryteNeurony1, 1);
        bias_o = new Matice(this.pocetVystupu, 1);
        bias_h.naplnMaticiNahodne();
        bias_o.naplnMaticiNahodne();

    }

    public NeuronovaSit(NeuronovaSit nn) {
        this.pocetVstupu = nn.pocetVstupu;
        this.skryteNeurony1 = nn.skryteNeurony1;
        this.pocetVystupu = nn.pocetVystupu;
        this.vahy_ih = nn.vahy_ih;
        this.vahy_ho = nn.vahy_ho;

        this.bias_h = nn.bias_h;
        this.bias_o = nn.bias_o;

    }

    public NeuronovaSit kopirujMozek() {
        return new NeuronovaSit(this);
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double[] aktivujMozek(double[] vstupy) {
        // Generating the Hidden Outputs
        Matice inputs = Matice.nactiZPole(vstupy);
        Matice hidden = Matice.soucin(this.vahy_ih, inputs);
        hidden.soucet(this.bias_h);
        hidden.aplikujFunkci(sigmoid);
        Matice output = Matice.soucin(this.vahy_ho, hidden);
        output.soucet(this.bias_o);
        output.aplikujFunkci(sigmoid);
        return output.nactiZMatice();
    }

    public void trenujMozek(double[] vstupy, double[] spravneVyskedky) {
        Matice inputs = Matice.nactiZPole(vstupy);
//        System.out.println(inputs.toString());
        Matice hidden = Matice.soucin(this.vahy_ih, inputs);
//        System.out.println("Hiddeen pred aplikaci biasu");
//        System.out.println(hidden.toString());
//        System.out.println("Bias hidden");
//        System.out.println(bias_h);
        hidden.soucet(this.bias_h);
//        System.out.println("Hiddeen po aplikaci biasu");
//        System.out.println(hidden.toString());
        hidden.aplikujFunkci(sigmoid);
//        System.out.println("Hiddeen output");
//        System.out.println(hidden.toString());
        Matice outputs = Matice.soucin(this.vahy_ho, hidden);
//        System.out.println("Output pred aplikaci biasu");
//        System.out.println(outputs.toString());
//        System.out.println("Bias output");
//        System.out.println(bias_o);
        outputs.soucet(this.bias_o);
//        System.out.println("Output po aplikaci biasu");
//        System.out.println(outputs.toString());
        outputs.aplikujFunkci(sigmoid);
//        System.out.println("Output output");
//        System.out.println(outputs.toString());
        Matice targets = Matice.nactiZPole(spravneVyskedky);
//        System.out.println("Spravne vysledky");
//        System.out.println(targets.toString());

//      Matice output_errors = Matice.rozdil(targets, outputs);
//      nove
//        double[][] testovaciOutput = {{0.75136507}};
//        outputs = new Matice(testovaciOutput);
        Matice lossFunction = Matice.rozdil(targets, outputs);
        lossFunction.soucin(lossFunction);
        lossFunction.soucin(0.5);
//        System.out.println("Loss funkce");
//        System.out.println(lossFunction); //correcto

        Matice out = Matice.rozdil(outputs, targets);
//        System.out.println("Out");
//        System.out.println(out);

//        System.out.println(outputs);
        Matice net = Matice.aplikujFunkci(outputs, derivujsigmoid);
//        System.out.println("Net");
//        System.out.println(net);

//        net = Matice.transponovat(net);
        Matice delta = new Matice(outputs.pocetRadku, 1);
        for (int i = 0; i < outputs.pocetRadku; i++) {
            delta.data[i][0] = out.data[i][0] * net.data[i][0];
        }

//        Matice delta = Matice.soucin(out, net); funguje
//        System.out.println("delta");
//        System.out.println(delta.toString());
//
//        System.out.println("hidden");
//        System.out.println(hidden.toString());
//        System.out.println("vahy hidden-output");
//        System.out.println(hmotnostNeuronu_ho.toString());
//
//        System.out.println("bias output");
//        System.out.println(bias_o);
        Matice deltaBiasO = delta;
        deltaBiasO.soucin(learningRate);
        bias_o = Matice.rozdil(bias_o, deltaBiasO);

//        System.out.println("bias output po delte");
//        System.out.println(bias_o);
        Matice delta_T = Matice.transponovat(delta);
        Matice deltaOutput = Matice.soucin(hidden, delta_T);
        Matice deltaOutputUntouched = deltaOutput;
//        System.out.println("delta output");
//        System.out.println(deltaOutput);
        deltaOutput.soucin(learningRate);
        deltaOutput = Matice.transponovat(deltaOutput);

//        System.out.println("delta output * learning rate");
//        System.out.println(deltaOutput);
        vahy_ho = Matice.rozdil(vahy_ho, deltaOutput);
//        System.out.println("vahy hidden-output po adjustmentu");
//        System.out.println(hmotnostNeuronu_ho.toString());

        //vahy input hidden
        Matice hiddenNet = Matice.aplikujFunkci(hidden, derivujsigmoid); // vim ze budu pouzivat
//        System.out.println("hidden net");
//        System.out.println(hiddenNet);
//        System.out.println("deltaOutputUntouched");
//        System.out.println(deltaOutputUntouched);

        Matice deltaHidden = new Matice(hidden.pocetRadku, 1); //Matice.soucin(deltaOutputUntouched, hiddenNet); // ma byt suma vsech erroru outputu

        for (int i = 0; i < hidden.pocetRadku; i++) {
            deltaHidden.data[i][0] = deltaOutputUntouched.data[i][0] * hiddenNet.data[i][0];
        }

        Matice deltaBiasH = deltaHidden;
        deltaBiasH.soucin(learningRate);
        bias_h = Matice.rozdil(bias_h, deltaBiasH);
//        System.out.println("delta hidden");
//        System.out.println(deltaHidden);

//        deltaHidden = Matice.transponovat(deltaHidden);
        Matice inputs_T = Matice.transponovat(inputs);
        deltaHidden = Matice.soucin(deltaHidden, inputs_T);
        deltaHidden.soucin(learningRate);
//        System.out.println("delta hidden pro vahy");
//        System.out.println(deltaHidden);

        vahy_ih = Matice.rozdil(vahy_ih, deltaHidden); // musi byt oboji matice 4x4

    }

    public void mutujNeurony(double mutace) {
        if (Math.random() < mutace) {
            Random r = new Random();
            mutujNeurony = x -> x + r.nextGaussian() * 0.1;
        } else {
            mutujNeurony = x -> x;
        }

        this.vahy_ih.aplikujFunkci(mutujNeurony);
        this.vahy_ho.aplikujFunkci(mutujNeurony);
        this.bias_h.aplikujFunkci(mutujNeurony);
        this.bias_o.aplikujFunkci(mutujNeurony);

    }

}
