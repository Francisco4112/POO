package com.grupo06.mvc;

import java.util.Objects;
import java.util.function.Consumer;

/** Represents an individual item in a menu. */
public class MenuOption {

  /** Label shown to the user. */
  private String label;

  /** Action performed when this option is selected. */
  private Consumer<Integer> action;

  /** Default constructor initializing with empty label and no-op action. */
  public MenuOption() {
    this.label = "";
    this.action = i -> {
      // default empty action
    };
  }

  /**
   * Constructs a menu option with specified label and action.
   *
   * @param label  Label to show.
   * @param action Action to run when selected.
   */
  public MenuOption(String label, Consumer<Integer> action) {
    this.label = label;
    this.action = action;
  }

  /**
   * Constructs a copy of another menu option.
   *
   * @param other MenuOption to copy.
   */
  public MenuOption(MenuOption other) {
    this.label = other.label;
    this.action = other.action;
  }

  /** @return the label of this option. */
  public String getLabel() {
    return label;
  }

  /** @return the action assigned to this option. */
  public Consumer<Integer> getAction() {
    return action;
  }

  /**
   * Changes the label of this menu option.
   *
   * @param label The new label.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Sets the action for this menu option.
   *
   * @param action The action to assign.
   */
  public void setAction(Consumer<Integer> action) {
    this.action = action;
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, action);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    MenuOption that = (MenuOption) obj;
    return Objects.equals(label, that.label) && Objects.equals(action, that.action);
  }

  /**
   * Creates a new identical instance of this option.
   *
   * @return A new MenuOption with the same values.
   */
  @Override
  public MenuOption clone() {
    return new MenuOption(this);
  }

  @Override
  public String toString() {
    return "MenuOption[label=\"" + label + "\", action=" + action + "]";
  }
}
