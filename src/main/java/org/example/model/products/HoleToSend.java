package org.example.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class HoleToSend {
    Hole hole;
    Action action;

    public HoleToSend(Hole hole, Action action) {
        this.hole = hole;
        this.action = action;
    }
}
