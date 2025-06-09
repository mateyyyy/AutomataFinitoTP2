package com.example;

import java.util.*;

public class AFN {
    Set<String> estados;
    Set<Character> alfabeto;
    Map<String, Map<Character, Set<String>>> transiciones;
    String estadoInicial;
    Set<String> estadosFinales;
}