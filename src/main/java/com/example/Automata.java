package com.example;
import java.util.ArrayList;

public class Automata {
    int estadoInicial;
    int[] estadoFinal;
    Transicion[] transiciones;
    int estadoActual;
    int pos=0;
    int newState = 0;

    public Automata(int estadoInicial, int[] estadoFinal, Transicion[] transiciones) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.transiciones = transiciones;
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

    public void imprimirAutomata(){
        System.out.println("Estado inicial : " + estadoInicial);
        System.out.print("Estado/s finales : ");
        for(int i=0; i<estadoFinal.length; i++){
            System.out.print(estadoFinal[i]);
        }
        System.out.println("");
        for(int i=0; i<transiciones.length; i++){
            transiciones[i].printTransition();           
        }

    }

    public ArrayList<Integer> Mover(int estado, char caracter){
        ArrayList<Integer> lista = new ArrayList<>();
        for(int i=0; i<transiciones.length; i++){
            if(transiciones[i].getEstadoInicial() == estado && transiciones[i].getCaracter() == caracter){
                lista.add(transiciones[i].getEstadoFinal());
            }
        }
        return lista;
    }

    public int generateNewState(){
        newState++;
        return newState-1;
    }


    public void AfnToAfd(){
        boolean allDone = false;
        boolean enter;

        //primero aplicar la funcion lambda sobre el estado incial. Esto me devuelve el conjunto de estados iniciales alcanzables con lambda
        ArrayList<Estados> estados  = new ArrayList<>();
        ArrayList<Integer> estadosIniciales = new ArrayList<>();
        ArrayList<Transicion> transicions = new ArrayList<>();
        estadosIniciales.add(estadoInicial);
        estados.add(new Estados(clausuraLambda(estadosIniciales, estadosIniciales)));

        //Guardar este conjunto de estados en una lista que me diga los estados que voy procesando, este conj. de estados lo tengo que marcar como pendiente
        ArrayList<Integer> estadosInicialesA = new ArrayList<>();
            for(int i=0;i<estados.get(0).getConjuntos().size(); i++){
                clausuraLambda(Mover(estados.get(0).getConjuntos().get(i), 'a'), estadosInicialesA);
            }
        ArrayList<Integer> estadosInicialesB = new ArrayList<>();
            for(int i=0;i<estados.get(0).getConjuntos().size(); i++){
                clausuraLambda(Mover(estados.get(0).getConjuntos().get(i), 'b'), estadosInicialesB);
            }
        estados.get(0).setDone();
        //Me fijo si estos estados ya estan cargados en la lista original
            // Procesar transiciones del estado inicial
        int origen = estados.get(0).getId();
        // A para 'a'
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
                        clausuraLambda(Mover(e, 'a'), nuevoA);
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
                        clausuraLambda(Mover(e, 'b'), nuevoB);
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
        this.transiciones = transicions.toArray(new Transicion[0]);
        this.estadoInicial = estados.get(0).getId();
        ArrayList<Integer> nuevosFinales = new ArrayList<>();
        for (Estados e : estados) {
            for (int f : estadoFinal) {
                if (e.getConjuntos().contains(f)) {
                    nuevosFinales.add(e.getId());
                    break;
                }
            }
        }
        this.estadoFinal = nuevosFinales.stream().mapToInt(i -> i).toArray();
        this.estadoActual = estadoInicial;
        this.pos = 0;
        System.out.println("Estado Inicial del AFD:");
        System.out.println(estadoInicial);

    
        System.out.println("Transiciones del AFD:");
        for (Transicion t : this.transiciones) {
            t.printTransition();
        }

        System.out.print("Estados finales del AFD: ");
        for (int f : this.estadoFinal) {
            System.out.print("q" + f + " ");
        }
        System.out.println();
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

    public ArrayList<Integer> clausuraLambda(ArrayList<Integer> estadosIniciales, ArrayList<Integer> lista){ //Retorna conj. de estados alcanzables con lambda comenzando en un unico estado
    for (int j=0; j<estadosIniciales.size(); j++){
        for(int i=0; i<transiciones.length; i++){
            if(transiciones[i].getEstadoInicial() == estadosIniciales.get(j) && transiciones[i].getCaracter() == '-'){
                lista.add(transiciones[i].getEstadoFinal());
                ArrayList<Integer> estadoFinal = new ArrayList<>();
                estadoFinal.add(transiciones[i].getEstadoFinal());
                clausuraLambda(estadoFinal, lista);
            }
        }
    }
        return lista;
    }
}



