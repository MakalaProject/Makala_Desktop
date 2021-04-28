package org.example.controllers.products.subcontrollers;

import org.example.model.products.Product;
import org.example.model.products.StaticProduct;

public class StaticProductController extends StaticParentProductController<StaticProduct>{

    @Override
    public StaticProduct getObject() {
        return super.getObject(StaticProduct.class);
    }

    @Override
    public StaticProduct findObject(Product object) {
        return findObject( object,"products/statics", StaticProduct.class);
    }
}
