package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HoleToSend {
    Hole hole;
    Action action;

    public HoleToSend(Hole hole, Action action) {
        this.hole = hole;
        this.action = action;
    }
}
