package com.example;

import java.util.ArrayList;

public class Parser {
    public int i = 0;
    public String token;
    public int lastState = 0;
    AutomataOP automataOP = new AutomataOP();

    public Parser(String token) {
        this.token = token + '#';
    }

    public void eat() {
        System.out.println("Consumido: " + token.charAt(i) + " \n");
        i++;
    }

    Automata E() {
        // E -> T E'
        if (token.charAt(i) == '(' || token.charAt(i) == 'a' || token.charAt(i) == 'b' || token.charAt(i) == 'c') {
            Automata t = T();
            Automata e = Eprima();
            if (e != null) {
                int estadoInicial = generarNuevoEstado();
                int estadoFinal = generarNuevoEstado();
                return automataOP.union(t, e, estadoInicial, estadoFinal);
            } else {
                return t;
            }
        } else {
            System.out.println("Syntax error en E\n");
            System.exit(1);
        }
        return null;
    }

    Automata Eprima() {
        // E' -> | T E' | λ
        if (token.charAt(i) == '|') {
            eat();
            Automata t = T();
            Automata e = Eprima();
            if (e != null) { // Entonces empieza con el |, tenemos que hacer la union
                int estadoInicial = generarNuevoEstado();
                int estadoFinal = generarNuevoEstado();
                return automataOP.union(t, e, estadoInicial, estadoFinal);
            } else {
                return t;
            }
        } else if (token.charAt(i) == ')' || token.charAt(i) == '#') {
            // lambda (no se hace nada)
        } else {
            System.out.println("Syntax error en E'\n");
            System.exit(1);
        }
        return null;
    }

    Automata T() {
        // T -> F T'
        if (token.charAt(i) == '(' || token.charAt(i) == 'a' || token.charAt(i) == 'b' || token.charAt(i) == 'c') {
            Automata f = F();
            Automata t = Tprima();
            if (t != null) {
                return automataOP.concatenar(f, t);
            } else {
                return f;
            }

        } else {
            System.out.println("Syntax error en T\n");
            System.exit(1);
        }
        return null;
    }

    Automata Tprima() {
        // T' -> . F T' | λ
        if (token.charAt(i) == '.') {
            eat();
            Automata f = F();
            Automata t = Tprima();
            if (t == null) {
                return f;
            } else {
                return automataOP.concatenar(f, t);
            }

        } else if (token.charAt(i) == '|' || token.charAt(i) == ')' || token.charAt(i) == '#') {
            // lambda (no se hace nada)
        } else {
            System.out.println("Syntax error en T'\n");
            System.exit(1);
        }
        return null;
    }

    Automata F() {
        // F -> P F'
        if (token.charAt(i) == '(' || token.charAt(i) == 'a' || token.charAt(i) == 'b' || token.charAt(i) == 'c') {
            Automata p = P();
            if (Fprima()) {
                int estadoInicial = generarNuevoEstado();
                int estadoFinal = generarNuevoEstado();
                return automataOP.clausuraKleene(p, estadoInicial, estadoFinal);
            } else {
                return p;
            }
        } else {
            System.out.println("Syntax error en F\n");
            System.exit(1);
        }
        return null;
    }

    boolean Fprima() {
        // F' -> * | λ
        if (token.charAt(i) == '*') {
            eat();
            return true;
        } else if (token.charAt(i) == '.' || token.charAt(i) == '|' || token.charAt(i) == ')'
                || token.charAt(i) == '#') {
            // lambda (no se hace nada)

        } else {
            System.out.println("Syntax error en F'\n");
            System.exit(1);
        }
        return false;
    }

    Automata P() {
        // P -> (E) | L
        if (token.charAt(i) == '(') {
            eat();
            Automata e = E();
            if (token.charAt(i) == ')') {
                eat();
                return e;
            } else {
                System.out.println("Syntax error: falta )\n");
                System.exit(1);
            }
        } else if (token.charAt(i) == 'a' || token.charAt(i) == 'b' || token.charAt(i) == 'c') {
            return L();
        } else {
            System.out.println("Syntax error en P\n");
            System.exit(1);
        }
        return null;
    }

    Automata L() {
        // L -> a | b | c
        if (token.charAt(i) == 'a' || token.charAt(i) == 'b' || token.charAt(i) == 'c') {

            char simbolo = token.charAt(i); // guarda el caracter antes de consumirlo
            eat();
            int originState = generarNuevoEstado();
            ArrayList<Integer> destinyState = new ArrayList<>();
            destinyState.add(generarNuevoEstado());
            ArrayList<Transicion> transicion = new ArrayList<>();
            transicion.add(new Transicion(originState, simbolo, destinyState.get(0)));
            return new Automata(originState, destinyState, transicion);
        } else {
            System.out.println("Syntax error en L\n");
            System.exit(1);
        }
        return null;
    }

    public int generarNuevoEstado() {
        lastState++;
        return lastState - 1;

    }

    public Automata generarAutomata() {
        Automata e = E();
        if (token.charAt(i) == '#') {
            System.out.println("Expresion Regular válida");
            AFNtoAFDConverter converter = new AFNtoAFDConverter();
            return e;
        } else {
            System.out.println("Cadena inválida. Sobra algo: \n" + token.charAt(i));
        }
        return null;
    }
}
