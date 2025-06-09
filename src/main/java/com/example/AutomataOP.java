package com.example;

import java.util.ArrayList;

public class AutomataOP {

    // Concatenación de dos autómatas: a · b
    public Automata concatenar(Automata a, Automata b) {
        Transicion[] transicionesIntermedias = new Transicion[a.estadoFinal.length];
        for (int i = 0; i < a.estadoFinal.length; i++) {
            transicionesIntermedias[i] = new Transicion(a.estadoFinal[i], 'λ', b.estadoInicial);
        }

        Transicion[] nuevasTransiciones = concatenarArreglos(a.transiciones, b.transiciones, transicionesIntermedias);
        return new Automata(a.estadoInicial, b.estadoFinal, nuevasTransiciones);
    }

    // Unión de dos autómatas: a | b
    public Automata union(Automata a, Automata b) {
        int nuevoEstadoInicial = generarNuevoEstado(a, b);
        int nuevoEstadoFinal = nuevoEstadoInicial + 1;

        ArrayList<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(nuevoEstadoInicial, 'λ', a.estadoInicial));
        transiciones.add(new Transicion(nuevoEstadoInicial, 'λ', b.estadoInicial));

        for (int f : a.estadoFinal) {
            transiciones.add(new Transicion(f, 'λ', nuevoEstadoFinal));
        }

        for (int f : b.estadoFinal) {
            transiciones.add(new Transicion(f, 'λ', nuevoEstadoFinal));
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
    public Automata clausuraKleene(Automata a) {
        int nuevoEstadoInicial = generarNuevoEstado(a, null);
        int nuevoEstadoFinal = nuevoEstadoInicial + 1;

        ArrayList<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(nuevoEstadoInicial, 'λ', a.estadoInicial)); // λ → A
        transiciones.add(new Transicion(nuevoEstadoInicial, 'λ', nuevoEstadoFinal)); // λ → final directo

        for (int f : a.estadoFinal) {
            transiciones.add(new Transicion(f, 'λ', a.estadoInicial)); // ciclo
            transiciones.add(new Transicion(f, 'λ', nuevoEstadoFinal)); // salida
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

    // Ayuda para crear nuevos estados sin colisión
    private int generarNuevoEstado(Automata a, Automata b) {
        int maxA = obtenerEstadoMaximo(a);
        int maxB = (b != null) ? obtenerEstadoMaximo(b) : -1;
        return Math.max(maxA, maxB) + 1;
    }

    private int obtenerEstadoMaximo(Automata a) {
        int max = a.estadoInicial;
        for (int f : a.estadoFinal) {
            max = Math.max(max, f);
        }
        for (Transicion t : a.transiciones) {
            max = Math.max(max, Math.max(t.getEstadoInicial(), t.getEstadoFinal()));
        }
        return max;
    }
}
