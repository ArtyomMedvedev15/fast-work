package io.project.fastwork.domains;


import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Points {
    private BigDecimal x;
    private BigDecimal y;
}
