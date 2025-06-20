package com.example;

import java.util.ArrayList;

public class Estados {

    public ArrayList<Integer> conjuntos;
    private boolean done;
    private int id;
    private static int contadorGlobal = 0;

    public Estados(ArrayList<Integer> conjuntos){
        this.conjuntos = new ArrayList<>();
        this.conjuntos.addAll(conjuntos);
        this.done = false;
        this.id = contadorGlobal++;
    }

    public void setDone(){
        done = true;
    }

    public ArrayList<Integer> getConjuntos(){
        return conjuntos;
    }

    public boolean getDone(){
        return done;
    }

    public int getId(){
        return id;
    }
}
