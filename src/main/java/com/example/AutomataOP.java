package com.example;

import java.util.ArrayList;

public class AutomataOP {

    // Concatenación de dos autómatas: a · b
    public Automata concatenar(Automata a, Automata b) {
        Transicion[] transicionesIntermedias = new Transicion[a.estadoFinal.length];
        for (int i = 0; i < a.estadoFinal.length; i++) {
        transicionesIntermedias[i] = new Transicion(a.estadoFinal[i], '-', b.estadoInicial);
        }

        Transicion[] nuevasTransiciones = concatenarArreglos(a.transiciones, b.transiciones, transicionesIntermedias);
        return new Automata(a.estadoInicial, b.estadoFinal, nuevasTransiciones);
    }

    // Unión de dos autómatas: a | b
    public Automata union(Automata a, Automata b, int estadoInicial, int estadoFinal ) {
        int nuevoEstadoInicial = estadoInicial;
        int nuevoEstadoFinal = estadoFinal;

        ArrayList<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', a.estadoInicial));
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', b.estadoInicial));

        for (int f : a.estadoFinal) {
            transiciones.add(new Transicion(f, '-', nuevoEstadoFinal));
        }

        for (int f : b.estadoFinal) {
            transiciones.add(new Transicion(f, '-', nuevoEstadoFinal));
        }

        // Combinar transiciones existentes con las nuevas
        Transicion[] todas = concatenarArreglos(
            a.transiciones,
            b.transiciones,
            transiciones.toArray(new Transicion[0])
        );

        return new Automata(nuevoEstadoInicial, new int[]{nuevoEstadoFinal}, todas);
    }

    // Clausura de Kleene: a*
    public Automata clausuraKleene(Automata a, int estadoInicial, int estadoFinal) {
        int nuevoEstadoInicial = estadoInicial;
        int nuevoEstadoFinal = estadoFinal;

        ArrayList<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', a.estadoInicial)); // λ → A
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', nuevoEstadoFinal)); // λ → final directo

        for (int f : a.estadoFinal) {
            transiciones.add(new Transicion(f, '-', a.estadoInicial)); // ciclo
            transiciones.add(new Transicion(f, '-', nuevoEstadoFinal)); // salida
        }

        Transicion[] todas = concatenarArreglos(
            a.transiciones,
            transiciones.toArray(new Transicion[0])
        );

        return new Automata(nuevoEstadoInicial, new int[]{nuevoEstadoFinal}, todas);
    }

    // Concatenar varios arreglos de transiciones
    public static Transicion[] concatenarArreglos(Transicion[]... arreglos) {
        int totalLength = 0;
        for (Transicion[] arr : arreglos) {
            totalLength += arr.length;
        }

        Transicion[] resultado = new Transicion[totalLength];
        int currentIndex = 0;

        for (Transicion[] arr : arreglos) {
            System.arraycopy(arr, 0, resultado, currentIndex, arr.length);
            currentIndex += arr.length;
        }

        return resultado;
    }
}
