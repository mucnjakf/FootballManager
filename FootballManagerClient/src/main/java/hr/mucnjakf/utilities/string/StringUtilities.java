package hr.mucnjakf.utilities.string;

public class StringUtilities {
    public static boolean isNumber(String value) {
        if (value == null) {
            return false;
        }

        try {
            Integer i = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
