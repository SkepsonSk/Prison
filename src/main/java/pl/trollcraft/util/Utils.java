package pl.trollcraft.util;

public class Utils {

    public static String roman(int n) {
        switch (n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";

            default:
                return "";
        }
    }

    public static int fromRoman(String r) {
        if (r.equals("I")) return 1;
        else if (r.equals("II")) return 2;
        else if (r.equals("III")) return 3;
        else if (r.equals("IV")) return 4;
        else if (r.equals("V")) return 5;
        else if (r.equals("VI")) return 6;

        return 0;
    }

    public static boolean stringEqual(String a, String b) {
        if (a.length() != b.length()) return false;
        for (int i = 0 ; i < a.length() ; i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
        }
        return true;
    }

}
