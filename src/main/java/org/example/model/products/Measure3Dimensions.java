package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Measure class to wrap the measures of a object three-dimensional, as the:<br>
 * {@link #getX()} The width of the object.X<br>
 * {@link #getY()} The height of the object.Y<br>
 * {@link #getZ()} The length or deepness of the object.Z<br>
 * This measures for convention will be in cm.<br>
 * @author Luis
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measure3Dimensions extends Measure2Dimensions {
    private BigDecimal z;
}
