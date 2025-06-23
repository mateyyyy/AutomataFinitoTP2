package com.example;

import java.util.ArrayList;

public class AFNtoAFDConverter {

    int newState = 0;

    public AFNtoAFDConverter(){

    }

    public int isInTheList(ArrayList<Estados> original, ArrayList<Integer> candidata) {
    for (Estados estado : original) {
        ArrayList<Integer> conjunto = estado.getConjuntos();

        if (conjunto.size() == candidata.size()) {;

        boolean iguales = true;
        for (Integer elem : candidata) {
            if (!conjunto.contains(elem)) {
                iguales = false;
                break;
            }
        }
    
        if (iguales) return estado.getId();
        }
    }

    return -1;
}

public ArrayList<Integer> clausuraLambda(ArrayList<Integer> estadosIniciales, ArrayList<Integer> lista, ArrayList<Transicion> transiciones){
    for (int j=0; j<estadosIniciales.size(); j++){
        for(int i=0; i<transiciones.size(); i++){
            if(transiciones.get(i).getEstadoInicial() == estadosIniciales.get(j) && transiciones.get(i).getCaracter() == '-'){
                lista.add(transiciones.get(i).getEstadoFinal());
                ArrayList<Integer> estadoFinal = new ArrayList<>();
                estadoFinal.add(transiciones.get(i).getEstadoFinal());
                clausuraLambda(estadoFinal, lista, transiciones);
            }
        }
    }
        return lista;
    }

    public ArrayList<Integer> Mover(int estado, char caracter, ArrayList<Transicion> transiciones){
        ArrayList<Integer> lista = new ArrayList<>();
        for(int i=0; i<transiciones.size(); i++){
            if(transiciones.get(i).getEstadoInicial() == estado && transiciones.get(i).getCaracter() == caracter){
                lista.add(transiciones.get(i).getEstadoFinal());
            }
        }
        return lista;
    }

    public int generateNewState(){
        newState++;
        return newState-1;
    }


    public Automata AfnToAfd(Automata automata){
        boolean allDone = false;
        boolean enter;

        ArrayList<Estados> estados  = new ArrayList<>();
        ArrayList<Integer> estadosIniciales = new ArrayList<>();
        ArrayList<Transicion> transicions = new ArrayList<>();
        estadosIniciales.add(automata.estadoInicial);
        estados.add(new Estados(clausuraLambda(estadosIniciales, estadosIniciales, automata.transiciones)));

        ArrayList<Integer> estadosInicialesA = new ArrayList<>();
            for(int i=0;i<estados.get(0).getConjuntos().size(); i++){
                clausuraLambda(Mover(estados.get(0).getConjuntos().get(i), 'a', automata.transiciones), estadosInicialesA, automata.transiciones);
            }
        ArrayList<Integer> estadosInicialesB = new ArrayList<>();
            for(int i=0;i<estados.get(0).getConjuntos().size(); i++){
                clausuraLambda(Mover(estados.get(0).getConjuntos().get(i), 'b', automata.transiciones), estadosInicialesB, automata.transiciones);
            }
        estados.get(0).setDone();

        int origen = estados.get(0).getId();
    
        int isInTheList = isInTheList(estados, estadosInicialesA);
        if (isInTheList==-1) {
            Estados nuevo = new Estados(estadosInicialesA);
            estados.add(nuevo);
            transicions.add(new Transicion(origen, 'a', nuevo.getId()));
        } else {
            transicions.add(new Transicion(origen, 'a', isInTheList));
        }

        isInTheList = isInTheList(estados, estadosInicialesB);
        if (isInTheList==-1) {
            Estados nuevo = new Estados(estadosInicialesB);
            estados.add(nuevo);
            transicions.add(new Transicion(origen, 'b', nuevo.getId()));
        } else {
            transicions.add(new Transicion(origen, 'b', isInTheList));
        }

        while (!allDone) {
            enter = false;
            for (int j = 0; j < estados.size(); j++) {
                if (!estados.get(j).getDone()) {
                    enter = true;
                    Estados actual = estados.get(j);
                    actual.setDone();

                    // Procesar 'a'
                    ArrayList<Integer> nuevoA = new ArrayList<>();
                    for (int e : actual.getConjuntos()) {
                        clausuraLambda(Mover(e, 'a', automata.transiciones), nuevoA, automata.transiciones);
                    }
                    isInTheList = isInTheList(estados, nuevoA);
                    origen = actual.getId();
                    if (isInTheList==-1) {
                        Estados nuevo = new Estados(nuevoA);
                        estados.add(nuevo);
                        
                        transicions.add(new Transicion(origen, 'a', nuevo.getId()));
                    } else {
                        transicions.add(new Transicion(origen, 'a', isInTheList));
                    }

                    // Procesar 'b'
                    ArrayList<Integer> nuevoB = new ArrayList<>();
                    for (int e : actual.getConjuntos()) {
                        clausuraLambda(Mover(e, 'b', automata.transiciones), nuevoB, automata.transiciones);
                    }
                    isInTheList = isInTheList(estados, nuevoB);
                    if (isInTheList==-1) {
                        Estados nuevo = new Estados(nuevoB);
                        estados.add(nuevo);
                        transicions.add(new Transicion(origen, 'b', nuevo.getId()));
                    } else {
                        transicions.add(new Transicion(origen, 'b', isInTheList));
                    }
                }
            }
        if (!enter) allDone = true;
        }
        ArrayList<Integer> nuevosFinales = new ArrayList<>();
        for (Estados e : estados) {
            for (int f : automata.estadoFinal) {
                if (e.getConjuntos().contains(f)) {
                    nuevosFinales.add(e.getId());
                    break;
                }
            }
        }        
        Automata nuevoAutomataAFD = new Automata(estados.get(0).getId(), nuevosFinales, transicions);
        return nuevoAutomataAFD;
}

}
