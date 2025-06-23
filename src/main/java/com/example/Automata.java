package com.example;

import java.util.ArrayList;

public class Automata {
    int estadoInicial;
    ArrayList<Integer> estadoFinal = new ArrayList<>();
    ArrayList<Transicion> transiciones = new ArrayList<>();
    int estadoActual;
    int pos = 0;
    int newState = 0;

    public Automata(int estadoInicial, ArrayList<Integer> estadoFinal, ArrayList<Transicion> transiciones) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal.addAll(estadoFinal);
        this.transiciones.addAll(transiciones);
        this.estadoActual = estadoInicial;
    }

    public void validarCadena(String cadena) {
        pos = 0;
        estadoActual = estadoInicial;
        while (cadena.charAt(pos) != '#') {
            estadoActual = transicion(estadoActual, cadena.charAt(pos));
            pos++;
        }

        if (esEstadoFinal(estadoActual)) {
            System.out.println("Cadena válida");
        } else {
            System.out.println("Syntax error");
        }
    }

    public void validarCadenaAFND(String cadena) {
        boolean aceptada = procesarAFND(estadoInicial, cadena, 0);
        if (aceptada) {
            System.out.println("Cadena válida");
        } else {
            System.out.println("Syntax error");
        }
    }

    private boolean procesarAFND(int estado, String cadena, int pos) {
        // Caso base: cadena terminada
        if (pos >= cadena.length() || cadena.charAt(pos) == '#') {
            if (esEstadoFinal(estado))
                return true;
            // aún puede haber caminos con transiciones λ
            for (Transicion t : transiciones) {
                if (t.getEstadoInicial() == estado && t.getCaracter() == 'λ') {
                    if (procesarAFND(t.getEstadoFinal(), cadena, pos))
                        return true;
                }
            }
            return false;
        }

        char simbolo = cadena.charAt(pos);
        boolean aceptado = false;

        // Primero seguir transiciones por el símbolo actual
        for (Transicion t : transiciones) {
            if (t.getEstadoInicial() == estado && t.getCaracter() == simbolo) {
                if (procesarAFND(t.getEstadoFinal(), cadena, pos + 1))
                    return true;
            }
        }

        for (Transicion t : transiciones) {
            if (t.getEstadoInicial() == estado && t.getCaracter() == 'λ') {
                if (procesarAFND(t.getEstadoFinal(), cadena, pos))
                    return true;
            }
        }

        return false;
    }

    private boolean esEstadoFinal(int estado) {
        for (int finalState : estadoFinal) {
            if (estado == finalState) {
                return true;
            }
        }
        return false;
    }

    private int transicion(int estadoActual, char caracter) {
        for (Transicion t : transiciones) {
            if (t != null &&
                    t.getEstadoInicial() == estadoActual &&
                    t.getCaracter() == caracter) {
                return t.getEstadoFinal();
            }
        }
        return -1;
    }

    public void imprimirAutomata() {
        System.out.println("\n\nAutomata");
        System.out.println("Estado inicial : " + estadoInicial);
        System.out.print("Estado/s finales : ");
        for (int i = 0; i < estadoFinal.size(); i++) {
            if (estadoFinal.size() > 1) {
                if (i < estadoFinal.size() - 1) {
                    System.out.print(estadoFinal.get(i) + ", ");
                } else {
                    System.out.print(estadoFinal.get(i));

                }
            } else {
                System.out.print(estadoFinal.get(i));

            }
        }
        System.out.println("");
        for (int i = 0; i < transiciones.size(); i++) {
            transiciones.get(i).printTransition();
        }

    }

    public ArrayList<Integer> Mover(int estado, char caracter) {
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i = 0; i < transiciones.size(); i++) {
            if (transiciones.get(i).getEstadoInicial() == estado && transiciones.get(i).getCaracter() == caracter) {
                lista.add(transiciones.get(i).getEstadoFinal());
            }
        }
        return lista;
    }
}
