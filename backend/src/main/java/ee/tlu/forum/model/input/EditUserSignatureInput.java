package ee.tlu.forum.model.input;

import lombok.Data;

@Data
public class EditUserSignatureInput {
    private String username;
    private String signature;
}
