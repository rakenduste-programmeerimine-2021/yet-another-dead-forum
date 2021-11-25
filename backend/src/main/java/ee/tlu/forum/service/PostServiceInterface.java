package ee.tlu.forum.service;

import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.input.AddNewPostInput;

import java.util.List;

public interface PostServiceInterface {
    Post createPost(AddNewPostInput form);
    void deletePostById(Long id);
    Post editPost (Post form);
    List<Post> getAllPosts();
    Post getPostById(Long id);
    List<Post> getAllPostsByUserId(Long id);
    List<Post> getAllPostsByUsername(String username);
}
