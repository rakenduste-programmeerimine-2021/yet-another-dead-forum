package ee.tlu.forum.model.input;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DonationInput {
    private BigDecimal amount;
    private String username;
}
