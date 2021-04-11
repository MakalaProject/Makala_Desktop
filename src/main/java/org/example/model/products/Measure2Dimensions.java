package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Measure class to wrap the measures of a object two-dimensional, as the:<br>
 * {@link #getX()} The width of the object.X<br>
 * {@link #getY()} The height of the object.Y<br>
 * This measures for convention will be in cm.<br>
 * @author Luis
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measure2Dimensions implements Serializable {
    private BigDecimal x;
    private BigDecimal y;
}
