package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Automata(String archivoDot) {
        Pattern patronEstadoInicial = Pattern.compile("inic\\s*->\\s*(\\d+)\\s*;");
        Pattern patronTransicion = Pattern.compile("(\\d+)\\s*->\\s*(\\d+)\\s*\\[label=\"(.)\"\\]\\s*;");
        Pattern patronEstadoFinal = Pattern.compile("(\\d+)\\s*\\[shape=doublecircle\\]\\s*;");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoDot))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Matcher mIni = patronEstadoInicial.matcher(linea);
                if (mIni.find()) {
                    estadoInicial = Integer.parseInt(mIni.group(1));
                    estadoActual = estadoInicial; // también inicializo estadoActual
                }

                Matcher mTran = patronTransicion.matcher(linea);
                if (mTran.find()) {
                    int origen = Integer.parseInt(mTran.group(1));
                    int destino = Integer.parseInt(mTran.group(2));
                    char simbolo = mTran.group(3).charAt(0);
                    transiciones.add(new Transicion(origen, simbolo, destino));
                }

                Matcher mFinal = patronEstadoFinal.matcher(linea);
                if (mFinal.find()) {
                    estadoFinal.add(Integer.parseInt(mFinal.group(1)));
                }
            }
        } catch (IOException e) {
            // Si no vas a manejar errores, simplemente dejalo vacío o logueá si querés
        }
    }

    public void validarCadena(String cadena) {
        pos = 0;
        estadoActual = estadoInicial;
        while (cadena.charAt(pos) != '#') {
            estadoActual = transicion(estadoActual, cadena.charAt(pos));
            pos++;
        }

        if (esEstadoFinal(estadoActual)) {
            System.out.println("Cadena válida : " + cadena.substring(0, cadena.length() - 1));
        } else {
            System.out.println("Syntax error");
        }
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
        System.out.println("\n===========================");
        System.out.println("      AUTÓMATA FINITO");
        System.out.println("===========================\n");

        System.out.println(" Estado inicial: q" + estadoInicial);

        System.out.print(" Estados finales: ");
        if (estadoFinal.isEmpty()) {
            System.out.println("ninguno");
        } else {
            for (int i = 0; i < estadoFinal.size(); i++) {
                System.out.print("q" + estadoFinal.get(i));
                if (i < estadoFinal.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();
        }

        System.out.println("\n Transiciones:");
        for (Transicion t : transiciones) {
            t.printTransition(); // Asegurate de que esto imprima con buen formato
        }

        System.out.println();
    }

    public void exportarDot(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
            writer.println("digraph AFN {");
            writer.println("    rankdir=LR;");
            writer.println("    inic [shape=point];");
            writer.println("    inic -> " + estadoInicial + ";");

            for (int f : estadoFinal) {
                writer.println("    " + f + " [shape=doublecircle];");
            }

            for (Transicion t : transiciones) {
                writer.println("    " + t.getEstadoInicial() + " -> " + t.getEstadoFinal() +
                        " [label=\"" + t.getCaracter() + "\"];");
            }

            writer.println("}");
        } catch (IOException e) {
            System.out.println("Error al generar .dot: " + e.getMessage());
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
