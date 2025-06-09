package com.example;

public class Automata {
    int estadoInicial;
    int[] estadoFinal;
    Transicion[] transiciones;
    int estadoActual;
    int pos=0;

    public Automata(int estadoInicial, int[] estadoFinal, Transicion[] transiciones) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.transiciones = transiciones;
        this.estadoActual = estadoInicial;
    }
    
      public void validarCadena(String cadena) {
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
        if (esEstadoFinal(estado)) return true;
        // aún puede haber caminos con transiciones λ
        for (Transicion t : transiciones) {
            if (t.getEstadoInicial() == estado && t.getCaracter() == 'λ') {
                if (procesarAFND(t.getEstadoFinal(), cadena, pos)) return true;
            }
        }
        return false;
    }

    char simbolo = cadena.charAt(pos);
    boolean aceptado = false;

    // Primero seguir transiciones por el símbolo actual
    for (Transicion t : transiciones) {
        if (t.getEstadoInicial() == estado && t.getCaracter() == simbolo) {
            if (procesarAFND(t.getEstadoFinal(), cadena, pos + 1)) return true;
        }
    }

    // Luego probar caminos λ (sin consumir símbolo)
    for (Transicion t : transiciones) {
        if (t.getEstadoInicial() == estado && t.getCaracter() == 'λ') {
            if (procesarAFND(t.getEstadoFinal(), cadena, pos)) return true;
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
}
