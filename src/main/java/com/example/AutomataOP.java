package com.example;

import java.util.ArrayList;

public class AutomataOP {

    // Concatenación de dos autómatas: a · b
    public Automata concatenar(Automata a, Automata b) {
        ArrayList<Transicion> transicionesIntermedias = new ArrayList<>();
        for (int i = 0; i < a.estadoFinal.size(); i++) {
            transicionesIntermedias.add(new Transicion(a.estadoFinal.get(i), '-', b.estadoInicial));
        }

        ArrayList<Transicion> nuevasTransiciones = new ArrayList<>();
        nuevasTransiciones.addAll(a.transiciones);
        nuevasTransiciones.addAll(b.transiciones);
        nuevasTransiciones.addAll(transicionesIntermedias);
        return new Automata(a.estadoInicial, b.estadoFinal, nuevasTransiciones);
    }

    // Unión de dos autómatas: a | b
    public Automata union(Automata a, Automata b, int estadoInicial, int estadoFinal ) {
        int nuevoEstadoInicial = estadoInicial;
        ArrayList<Integer> nuevoEstadoFinal = new ArrayList<>();
        nuevoEstadoFinal.add(estadoFinal);
        ArrayList<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', a.estadoInicial));
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', b.estadoInicial));

        for (int f : a.estadoFinal) {
            transiciones.add(new Transicion(f, '-', nuevoEstadoFinal.get(0)));
        }

        for (int f : b.estadoFinal) {
            transiciones.add(new Transicion(f, '-', nuevoEstadoFinal.get(0)));
        }

        // Combinar transiciones existentes con las nuevas
        
        ArrayList<Transicion> nuevasTraciciones = new ArrayList<>();
        nuevasTraciciones.addAll(a.transiciones);
        nuevasTraciciones.addAll(b.transiciones);
        nuevasTraciciones.addAll(transiciones);

        return new Automata(nuevoEstadoInicial, nuevoEstadoFinal, nuevasTraciciones);
    }

    // Clausura de Kleene: a*
    public Automata clausuraKleene(Automata a, int estadoInicial, int estadoFinal) {
        int nuevoEstadoInicial = estadoInicial;
        ArrayList<Integer> nuevoEstadoFinal = new ArrayList<>();
        nuevoEstadoFinal.add(estadoFinal);
        ArrayList<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', a.estadoInicial)); // λ → A
        transiciones.add(new Transicion(nuevoEstadoInicial, '-', nuevoEstadoFinal.get(0))); // λ → final directo

        for (int f : a.estadoFinal) {
            transiciones.add(new Transicion(f, '-', a.estadoInicial)); // ciclo
            transiciones.add(new Transicion(f, '-', nuevoEstadoFinal.get(0))); // salida
        }

        ArrayList<Transicion> nuevasTraciciones = new ArrayList<>();
        nuevasTraciciones.addAll(a.transiciones);
        nuevasTraciciones.addAll(transiciones);

        return new Automata(nuevoEstadoInicial, nuevoEstadoFinal, nuevasTraciciones);
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
