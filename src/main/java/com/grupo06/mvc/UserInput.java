package com.grupo06.mvc;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple wrapper around Scanner to facilitate user input with validation and
 * conversion.
 */
public class UserInput {
    /** Scanner instance used internally to read user input. */
    private Scanner scanner;

    /**
     * Initializes the wrapper with a Scanner reading from standard input.
     */
    public UserInput() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Copy constructor for the wrapper. Since Scanner cannot be cloned, a new
     * Scanner is created instead.
     *
     * @param scanner Another UserInput instance to copy from.
     */
    public UserInput(UserInput scanner) {
        this();
    }

    /**
     * Continuously prompts the user until a valid input is provided.
     * Input is validated by a predicate and then converted by a function.
     *
     * @param prompt   Message displayed to the user before input.
     * @param error    Message displayed on error, or null for no error message.
     * @param validate Predicate to check if input is valid.
     * @param convert  Function to transform input string to desired type.
     * @return The processed and validated input.
     */
    public Object read(String prompt,
            String error,
            Predicate<String> validate,
            Function<String, Object> convert) {

        String ret = null;
        do {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (validate.test(line))
                ret = line;
            else if (error != null)
                System.err.println(error);
        } while (ret == null);
        return convert.apply(ret);
    }

    /**
     * Prompts the user and reads a single line of text with no validation.
     *
     * @param prompt Message displayed before reading input.
     * @return The raw user input as a string.
     */
    public String readString(String prompt) {
        return (String) this.read(prompt, null, s -> true, s -> s);
    }

    /**
     * Prompts the user for input and validates it until valid text is entered.
     *
     * @param prompt   Message shown before input.
     * @param error    Error message shown if input fails validation.
     * @param validate Validation predicate for the input.
     * @return Validated user input as a string.
     */
    public String readString(String prompt, String error, Predicate<String> validate) {
        return (String) this.read(prompt, error, validate, s -> s);
    }

    /**
     * Reads an integer input, retrying until a valid integer matching the predicate
     * is entered.
     *
     * @param prompt   Message shown before input.
     * @param error    Error message for invalid input.
     * @param validate Predicate to test the integer value.
     * @return The validated integer input.
     */
    public int readInt(String prompt, String error, Predicate<Integer> validate) {
        return (Integer) this.read(prompt, error, s -> {
            try {
                int i = Integer.parseInt(s);
                return validate.test(i);
            } catch (NumberFormatException e) {
                return false;
            }
        }, s -> Integer.parseInt(s));
    }

    /**
     * Reads a double input, retrying until a valid double matching the predicate is
     * entered.
     *
     * @param prompt   Message shown before input.
     * @param error    Error message for invalid input.
     * @param validate Predicate to test the double value.
     * @return The validated double input.
     */
    public double readDouble(String prompt, String error, Predicate<Double> validate) {
        return (Double) this.read(prompt, error, s -> {
            try {
                double d = Double.parseDouble(s);
                return validate.test(d);
            } catch (NumberFormatException e) {
                return false;
            }
        }, s -> Double.parseDouble(s));
    }

    /**
     * Reads a date from the user, prompting separately for year, month, and day,
     * retrying until a valid date satisfying the predicate is entered.
     *
     * @param error    Error message shown for invalid dates.
     * @param validate Predicate to validate the composed LocalDate.
     * @return The validated LocalDate.
     */
    public LocalDate readDate(String error, Predicate<LocalDate> validate) {
        LocalDate d = LocalDate.of(1, 1, 1);
        boolean success = false;
        do {
            try {
                d = d.withYear(this.readInt("Year > ", "Must be an integer!", y -> true));
                d = d.withMonth(this.readInt("Month > ", "Must be an integer!", m -> true));
                d = d.withDayOfMonth(this.readInt("Day > ", "Must be an integer!", i -> true));

                if (validate.test(d))
                    success = true;
                else
                    System.err.println(error);
            } catch (DateTimeException e) {
                System.err.println(error);
            }
        } while (!success);
        return d;
    }

    /**
     * Returns the hash code of the internal Scanner.
     *
     * @return hash code of the scanner.
     */
    @Override
    public int hashCode() {
        return this.scanner.hashCode();
    }

    /**
     * Checks equality by class type, treating all instances as equal.
     *
     * @param obj Object to compare.
     * @return True if same instance or same class; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        return true; // All UserInput instances are considered equal.
    }

    /**
     * Returns a new UserInput instance (cloning wrapper but not Scanner itself).
     *
     * @return A new UserInput wrapping a new Scanner.
     */
    @Override
    public UserInput clone() {
        return new UserInput(this);
    }
}
