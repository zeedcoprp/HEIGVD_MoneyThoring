package bll.model;

/**
 * CategoryModel class.
 * Allows the mapping between the DAL entities and the Business Logic.
 * Only implements the constructors, the getters and the setters.
 *
 * @authors Daniel Gonzalez Lopez
 * @version 1.0
 */
public class CategoryModel {

    private int id;

    private String name;
    private String color;

    private boolean isDefault;


    /**
     * Construct an instance of ClientModel.
     *
     * @param name Name of the category.
     * @param color Color of the category.
     * @param isDefault Flag for the default category.
     */
    public CategoryModel(String name, String color, boolean isDefault) {
        this.name = name;
        this.color = color;

        setDefault(isDefault);
    }


    // -------------------------------------------------------------------------
    // GETTERS -----------------------------------------------------------------

    /**
     * Get the ID of the category.
     *
     * @return ID of category.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the category.
     *
     * @return Name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the color of the category.
     *
     * @return Color of category.
     */
    public String getColor() {
        return color;
    }

    /**
     * Get the isDefault flag of the category.
     *
     * @return Default flag.
     */
    public boolean isDefault() {
        return isDefault;
    }


    // -------------------------------------------------------------------------
    // SETTERS -----------------------------------------------------------------

    /**
     * Set the ID of the category.
     *
     * @param id New ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the name of the category.
     *
     * @param name New name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the color of the category.
     *
     * @param color New color to set.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Set the default flag of the category.
     * Look for the previous Default Category and change its flag.
     *
     * @param aDefault New default flag to set.
     */
    public void setDefault(boolean aDefault) {
        // TODO - Check for previous default category.

        isDefault = aDefault;
    }
}
