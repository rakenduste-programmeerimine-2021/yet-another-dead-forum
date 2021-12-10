package ee.tlu.forum.service;

import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.input.AddNewPostInput;

import java.util.List;

public interface PostServiceInterface {
    Post createPost(AddNewPostInput form, String token);
    void deletePostById(Long id, String token);
    Post editPost (Post form, String token);
    List<Post> getAllPosts();
    Post getPostById(Long id);
    List<Post> getAllPostsByUserId(Long id);
    List<Post> getAllPostsByUsername(String username);
}
