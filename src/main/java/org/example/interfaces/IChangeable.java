package org.example.interfaces;

public interface IChangeable<I> {
    /**
     * Function to know the id the object.
     * Also is recommended to add the Annotation {@code @JsonIgnore} in the case to override this method
     * @return The id of the object.
     */
    I getId();

    /**
     * Function to set to delete the object that has to be deleted.
     * @param toDelete The value to know if this has to be deleted
     */
    void setToDelete(boolean toDelete);


}
