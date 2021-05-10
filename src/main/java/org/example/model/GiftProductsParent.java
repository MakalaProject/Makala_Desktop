package org.example.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.interfaces.IChangeable;
import org.example.model.products.Action;
import org.example.model.products.StaticProduct;
@EqualsAndHashCode()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftProductsParent  implements IChangeable<Integer> {
    protected Integer id;
    protected String category = "Similar";
    protected Integer amount = 0;
    protected Action action;
    protected Integer holeNumber = 1;
    protected boolean toDelete;


}
