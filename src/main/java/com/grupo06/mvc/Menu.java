package com.grupo06.mvc;

import java.util.Arrays;

/** Represents a menu composed of selectable options. */
public class Menu {

  /** List of available menu options. */
  private MenuOption[] options;

  /** Constructs an empty menu with no options. */
  public Menu() {
    this.options = new MenuOption[0];
  }

  /**
   * Constructs a menu using an array of options.
   *
   * @param entries Array of menu options.
   */
  public Menu(MenuOption[] entries) {
    this.setEntries(entries);
  }

  /**
   * Constructs a deep copy of another menu.
   *
   * @param otherMenu Menu to copy from.
   */
  public Menu(Menu otherMenu) {
    this.options = otherMenu.getEntries();
  }

  /**
   * Retrieves the menu's options as an array.
   *
   * @return A cloned array of menu options.
   */
  public MenuOption[] getEntries() {
    return Arrays.stream(this.options)
        .map(MenuOption::clone)
        .toArray(MenuOption[]::new);
  }

  /**
   * Sets the menu's options using a given array.
   *
   * @param entries New set of menu options.
   */
  public void setEntries(MenuOption[] entries) {
    this.options = Arrays.stream(entries)
        .map(MenuOption::clone)
        .toArray(MenuOption[]::new);
  }

  /** Displays the menu and handles user interaction. */
  public void run() {
    UserInput ui = new UserInput();

    System.out.println("\nChoose an option ...\n");
    for (int i = 0; i < this.options.length; i++) {
      System.out.printf("  %d -> %s%n", i + 1, this.options[i].getLabel());
    }
    System.out.println();

    int choice = ui.readInt(
        "Option > ",
        String.format("Must be an integer between 1 and %d!", this.options.length),
        n -> n > 0 && n <= this.options.length);

    this.options[choice - 1].getAction().accept(choice - 1);
  }

  /**
   * Computes the hash code of this menu instance.
   *
   * @return The hash code value.
   */
  @Override
  public int hashCode() {
    return Arrays.hashCode(this.options);
  }

  /**
   * Compares this menu to another object.
   *
   * @param obj The object to compare.
   * @return True if equal, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Menu otherMenu = (Menu) obj;
    return Arrays.equals(this.options, otherMenu.getEntries());
  }

  /**
   * Produces a deep copy of this menu.
   *
   * @return A cloned instance of this menu.
   */
  @Override
  public Menu clone() {
    return new Menu(this);
  }
}
