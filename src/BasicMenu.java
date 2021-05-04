/**
 * Enum class to represent the basic menu of the application.
 */
public enum BasicMenu {

  ADD_CITY(1), ADD_TRAVELLER(2), CALCULATE_SIMILARITIES(3), COMPARE_CITIES(4), FREE_TICKET(5), CALC_SIM(6), EXIT(7),;

  /**
   * The code for this menu option.
   */
  private final int code;

  /**
   * Private constructor using the provided code.
   * 
   * @param code
   *          the code for this menu option
   */
  private BasicMenu(int code) {
    this.code = code;
  }

  /**
   * Return the code for this menu object.
   * 
   * @return the code for this menu object
   */
  public int getCode() {
    return code;
  }

  /**
   * Get the menu object based on the code.
   * 
   * @param code
   *          the provided code
   * @return the menu object if found, null otherwise
   */
  public static BasicMenu getByCode(int code) {
    for (BasicMenu menu : values()) {
      if (menu.getCode() == code) {
        return menu;
      }
    }
    return null;
  }
}
