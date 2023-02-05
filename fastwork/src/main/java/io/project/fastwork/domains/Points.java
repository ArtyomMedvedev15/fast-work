package io.project.fastwork.domains;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Embeddable
public class Points {
    private BigDecimal x;
    private BigDecimal y;
}
