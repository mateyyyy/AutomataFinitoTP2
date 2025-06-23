package com.example;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("(a.b)*");
        Automata automata = parser.generarAutomata();
        automata.imprimirAutomata();
        AFNtoAFDConverter converter = new AFNtoAFDConverter();
        Automata eAFD = converter.AfnToAfd(automata);
        eAFD.validarCadena("ababab#");
        eAFD.validarCadena("bbabbabba");
        eAFD.imprimirAutomata();
        eAFD.exportarDot("NuevoAutomata");

        // Lectura, impresion de automata cargado con archivo .dot
        Automata automataDot = new Automata("afn.dot");
        automataDot.imprimirAutomata();
        automataDot.validarCadena("aabaaababaabba");
    }
}
