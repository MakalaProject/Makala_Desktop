package org.example.interfaces;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Generic class to manage the changes of the internal items of some objects
 * @param <D> The object that extends of {@link IChangeable}.
 * @param <I> The id of the object {@link D}.
 */
public class ListToChangeTools<D extends IChangeable<I>, I >{
    /**
     * Function to set to delete the items selected that must to be deleted.
     * @param listOriginal the list original.
     * @param listSelected the list selected to compare with the original.
     */
    public void setToDeleteItems(Collection<D> listOriginal, Collection<D> listSelected)
    {
        Collection<D> listToDelete = new ArrayList<>();
        for (D originalItem : listOriginal)
        {
            boolean itemExist = false;
            for (D selectedItem : listSelected)
            {
                if (originalItem.getId() == selectedItem.getId())
                {
                    itemExist = true;
                }
            }
            if(!itemExist) {//The original items that do not exist in the select list has to be deleted
                originalItem.setToDelete(true);
                listToDelete.add(originalItem);
            }
        }
        listSelected.addAll(listToDelete);
    }
}
