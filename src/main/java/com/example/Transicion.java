package com.example;

public class Transicion {
    private int estadoInicial;
    private int estadoFinal;
    private char caracter;

    public Transicion(int estadoInicial, char caracter, int estadoFinal) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.caracter = caracter;
    }

    public int getEstadoInicial() {
        return estadoInicial;
    }

    public int getEstadoFinal() {
        return estadoFinal;
    }

    public char getCaracter() {
        return caracter;
    }
}
