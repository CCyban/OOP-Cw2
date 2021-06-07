package Classes;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import java.util.regex.Pattern;

public class RegexTextFormatters {
    public static void setSingleLineTextFormatter(TextInputControl control) {
        control.setTextFormatter(new TextFormatter<>(c ->
        {
            // Check if the input contains any unwanted characters and is no longer than 1024 characters
            Pattern pattern = Pattern.compile("[\\n\\r\\t]|^.{1024,}$");
            if (pattern.matcher(c.getControlNewText()).find())
                return null;    // Means that the new character contains a recognised 'not-allowed character' from regex, which ultimately cannot be allowed
            else
                return c;       // Allow the new input, since that it doesn't contain any of the unwanted characters from the regex check
        }));
    }

    public static void setNumbersOnlyTextFormatter(TextInputControl control) {
        control.setTextFormatter(new TextFormatter<>(c ->
        {
            // Check if the input contains only numbers and decimals, up to a 512 character limit
            Pattern pattern = Pattern.compile("^[0-9.]{0,512}$");
            if (pattern.matcher(c.getControlNewText()).find())
                return c;       // Allow the new input, since that it passed the regex check
            else
                return null;    // Means that the new character isn't a valid character from the regex, which cannot be allowed
        }));
    }

    public static void set3WholeNumbersOnlyTextFormatter(TextInputControl control) {
        control.setTextFormatter(new TextFormatter<>(c ->
        {
            // Check if the input contains only numbers, and up to 3 digits long
            Pattern pattern = Pattern.compile("^[0-9]{0,3}$");
            if (pattern.matcher(c.getControlNewText()).find())
                return c;       // Allow the new input, since that it passed the regex check
            else
                return null;    // Means that the new character isn't a valid character from the regex, which cannot be allowed
        }));
    }
}
