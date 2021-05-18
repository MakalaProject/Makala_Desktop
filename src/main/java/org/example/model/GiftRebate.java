package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GiftRebate extends Rebate{
    @EqualsAndHashCode.Exclude
    private Gift gift;

    @Override
    public String getRoute() {
        return "gift-rebates";
    }
}
