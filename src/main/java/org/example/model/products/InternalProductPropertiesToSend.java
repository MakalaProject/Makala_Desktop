package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InternalProductPropertiesToSend {
    private InsideProduct insideProduct;
    private Action action;
}
