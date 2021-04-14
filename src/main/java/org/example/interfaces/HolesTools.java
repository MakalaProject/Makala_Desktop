package org.example.interfaces;


import org.example.model.products.Hole;
import org.example.model.products.Measure2Dimensions;
import java.util.List;

public class HolesTools {
    private Measure2Dimensions internalDimension;
    private List<Hole> holesDimensions;

    public HolesTools(Measure2Dimensions internalDimension, List<Hole> holesDimensions) {
        this.internalDimension = internalDimension;
        this.holesDimensions = holesDimensions;
    }

    /**
     * Function to know if the new hole can fix with the free space available.
     * This is do it with a list previusly ordered with the hole number, because is ordered to left to right
     * @param newHole
     * @return
     */
    /*
    public boolean verifyNewHole(Hole newHole){
        BigDecimal x = new BigDecimal("0");//The summary of the length of all the holes in the row in the axis x
        BigDecimal y = new BigDecimal("0");//The summary of the length of all rows in the axis y
        BigDecimal rowY = new BigDecimal("0");//The length of the compared row in the axis y
        //Check all the holes
        for (Hole hole : holesDimensions){
            Measure2Dimensions holeDimension = hole.getHoleDimensions();
            if(holeDimension != null){
                if(holeDimension.getX() != null && holeDimension.getY() != null){
                    //Check the x length to make it lower than the internal dimension in the x dimension
                    BigDecimal result = x.add(holeDimension.getX());
                    if(result.compareTo(internalDimension.getX()) > 0){//If the new hole with the previous holes of this row is greater than the internal dimension in the x dimension
                        //The code has to jump to the next row
                        rowY = rowY.add(y);
                    }
                    else{
                        //Y is going to get the higher value of the row
                        y = y.max(holeDimension.getY());
                    }
                }
            }
        }
    }
     */
}
