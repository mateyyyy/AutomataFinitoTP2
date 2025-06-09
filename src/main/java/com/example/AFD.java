package com.example;

import java.util.*;

public class AFD {
    Set<Set<String>> estados;
    Set<Character> alfabeto;
    Map<Set<String>, Map<Character, Set<String>>> transiciones;
    Set<String> estadoInicial;
    Set<Set<String>> estadosFinales;
}