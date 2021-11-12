package com.Methods;

import com.N3_SemanticActions.Tokens.Token;

import java.util.Collection;

public class stringMethods {
    //Print multiples of variables inside brackets.
    public static String encapsulatedTuple(String beginChar, String endChar, String limit, Object ... str) {
        StringBuilder tuple = new StringBuilder().append(beginChar);
        try {
            tuple.append(determineObjectPrintout(str[0], limit, true));
        }
        catch (IndexOutOfBoundsException e) {

        }
        for (int i = 1; i < str.length; i += 1) {
            if (str[i] != null) {
                tuple.append(determineObjectPrintout(str[i], limit, false));
            } else {
                tuple.append(limit).append(str[i]);
            }
        }
        tuple.append(endChar);
        return tuple.toString();
    }
    public static String encapsulatedTuple(String beginChar, String endChar, String limit, Collection<?> str) {
        return encapsulatedTuple(beginChar, endChar, limit, str.toArray());
    }

    public static String determineObjectPrintout(Object obj, String limit, boolean start) {
        String startLimit = limit;
        if (start) {
            startLimit = "";
        }
        if (obj != null) {
            if (obj instanceof Token) {
                return startLimit + ((Token) obj).toString(0);
            } else if (obj instanceof Collection) {
                return startLimit + encapsulatedTuple("<", ">", limit, (Collection<?>) obj);
            } else if (obj.getClass().isArray()) {
                return obj.toString();
            } else {
                return startLimit + obj;
            }
        }
        return startLimit + null;
    }

    public static String limitedTuple(String limit, Object ... str) {
        return encapsulatedTuple("", "", limit, str);
    }

    public static String tuple(Object ... str) {
        return encapsulatedTuple("(", ")", ", ", str);
    }

    public static <T> boolean arrayContains(T obj, T[] array) {
        for (T item : array) {
            if (obj.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static Character[] toCharacterList(String input) {
        Character[] output = new Character[input.length()];
        for (int i = 0; i < input.length(); i += 1) {
            output[i] = input.charAt(i);
        }
        return output;
    }

    public static void silentPrintLine(Object str, boolean silent) {
        if (!silent) {
            if (str == null) {
                System.out.println();
            }
            else {
                System.out.println(str);
            }
        }
    }
    public static void silentPrintLine(boolean silent) {
        silentPrintLine(null, silent);
    }
    public static void silentPrint(Object str, boolean silent) {
        if (!silent) {
            System.out.print(str);
        }
    }
    public static String indent(int i) {
        return " ".repeat(Math.max(0, i));
    }
}
