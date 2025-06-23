package com.example;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
public class Main {
    public static void main(String[] args) {
        // Primero crea el automata con datos de ejemplo (puedes eliminar luego si usas archivo)
        // Variables para almacenar datos extraídos del archivo
        // int estadoInicialArchivo = -1;
        // ArrayList<Transicion> listaTransiciones = new ArrayList<>();
        // ArrayList<Integer> listaEstadosFinales = new ArrayList<>();

        // // Patrones regex para buscar en el archivo
        // Pattern patronEstadoInicial = Pattern.compile("inic\\s*->\\s*(\\d+)\\s*;");
        // Pattern patronTransicion = Pattern.compile("\\s*(\\d+)\\s*->\\s*(\\d+)\\s*\\[label=\"(\\w)\"\\]\\s*;");
        // Pattern patronEstadoFinal = Pattern.compile("\\s*(\\d+)\\s*\\[shape=doublecircle\\]\\s*;");

        // String archivo = "afn.dot";

        // try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        //     String linea;
        //     while ((linea = br.readLine()) != null) {
        //         // Buscar estado inicial
        //         Matcher mIni = patronEstadoInicial.matcher(linea);
        //         if (mIni.find()) {
        //             estadoInicialArchivo = Integer.parseInt(mIni.group(1));
        //         }

        //         // Buscar transiciones
        //         Matcher mTran = patronTransicion.matcher(linea);
        //         if (mTran.find()) {
        //             int estadoIni = Integer.parseInt(mTran.group(1));
        //             int estadoFin = Integer.parseInt(mTran.group(2));
        //             char caracter = mTran.group(3).charAt(0);

        //             // Crear objeto Transicion y agregar a la lista
        //             listaTransiciones.add(new Transicion(estadoIni, caracter, estadoFin));
        //         }

        //         // Buscar estados finales
        //         Matcher mFinal = patronEstadoFinal.matcher(linea);
        //         if (mFinal.find()) {
        //             int estadoFinal = Integer.parseInt(mFinal.group(1));
        //             listaEstadosFinales.add(estadoFinal);
        //         }
        //     }
        // } catch (IOException e) {
        //     System.out.println("Error al leer el archivo: " + e.getMessage());
        // }

        // // Convertir listas a arreglos
        // Transicion[] transicionesArchivo = listaTransiciones.toArray(new Transicion[0]);
        // int[] estadosFinalesArchivo = listaEstadosFinales.stream().mapToInt(i -> i).toArray();

        // // Mostrar resultados leídos (opcional)
        // System.out.println("Estado inicial leido: q" + estadoInicialArchivo);
        // System.out.println("Transiciones leídas:");
        // for (Transicion t : transicionesArchivo) {
        //     System.out.println("q" + t.getEstadoInicial() + " --" + t.getCaracter() + "--> q" + t.getEstadoFinal());
        // }
        // System.out.print("Estados finales: ");
        // for (int ef : estadosFinalesArchivo) {
        //     System.out.print("q" + ef + " ");
        // }
        // System.out.println();

        // // Finalmente, crear el autómata con los datos leídos del archivo
        // if (estadoInicialArchivo != -1 && transicionesArchivo.length > 0) {
        //     Automata automataArchivo = new Automata(estadoInicialArchivo, estadosFinalesArchivo, transicionesArchivo);
        //     // Ejemplo: validar una cadena con el autómata cargado desde archivo
        //     automataArchivo.validarCadena("aabababaaabbab#");
        // } else {
        //     System.out.println("No se pudo construir el autómata porque faltan datos.");
        // }

        // Transicion[] transA = { new Transicion(0, 'a', 1) };
        // Automata automataA = new Automata(0, new int[]{1}, transA);
        // Transicion[] transB = { new Transicion(0, 'b', 1) };
        // Automata automataB = new Automata(0, new int[]{1}, transB);
        // AutomataOP op = new AutomataOP();

        // // Unión: a | b
        // Automata unionAB = op.union(automataA, automataB);

        // // Validar algunas cadenas
        // System.out.println("Probando autómata a U b:");
        // unionAB.validarCadenaAFND("a#"); // válida
        // unionAB.pos = 0;
        // unionAB.estadoActual = unionAB.estadoInicial;
        // unionAB.validarCadenaAFND("b#"); // válida
        // unionAB.pos = 0;
        // unionAB.estadoActual = unionAB.estadoInicial;
        // unionAB.validarCadenaAFND("c#"); // inválida

        Parser parser = new Parser("(a.b)*#");
        parser.validateString();

    }
}
