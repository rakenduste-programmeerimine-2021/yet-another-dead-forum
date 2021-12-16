package ee.tlu.forum.model.input;

import lombok.Data;

@Data
public class EditUserAboutInput {
    private String username;
    private String about;
}
