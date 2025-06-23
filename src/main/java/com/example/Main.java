package com.example;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("(a.b)*#");
        Automata automata = parser.generarAutomata();
        automata.imprimirAutomata();
        AFNtoAFDConverter converter = new AFNtoAFDConverter();
        Automata eAFD = converter.AfnToAfd(automata);
        eAFD.validarCadena("ababab#");
        eAFD.validarCadena("abababa#");
        eAFD.imprimirAutomata();

    }
}
