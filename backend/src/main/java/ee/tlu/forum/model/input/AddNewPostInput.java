package ee.tlu.forum.model.input;

import lombok.Data;

@Data
public class AddNewPostInput {
    private String text;
    private String username;
    private Long threadId;
}
