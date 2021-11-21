package ee.tlu.forum.model.output;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class EverypayData {
   private String api_username;
   private String account_name;
   private BigDecimal amount;
   private String order_reference;
   private String token_agreement;
   private String nonce;
   private ZonedDateTime timestamp;
   private String customer_url;
}
