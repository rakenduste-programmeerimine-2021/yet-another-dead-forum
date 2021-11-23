package ee.tlu.forum.model.input;

import lombok.Data;

@Data
public class AddNewThreadInput {
    private String title;
    private String content;
    private String username;
}
