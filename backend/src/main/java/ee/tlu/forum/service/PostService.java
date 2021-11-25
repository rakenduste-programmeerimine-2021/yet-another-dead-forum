package ee.tlu.forum.service;

import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.AddNewPostInput;
import ee.tlu.forum.repository.PostRepository;
import ee.tlu.forum.repository.ThreadRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;

    @Override
    public Post createPost(AddNewPostInput form) {
        if (form.getText() == null || form.getText().isEmpty()) {
            throw new BadRequestException("Text field cannot be empty.");
        }
        if (form.getUsername() == null || form.getUsername().isEmpty()) {
            throw new BadRequestException("Username field cannot be empty.");
        }
        if (form.getThreadId() == null) {
            throw new BadRequestException("Thread ID field cannot be empty.");
        }
        if (form.getText().length() > 1024) {
            throw new BadRequestException("Content character limit exceeded." +
                    " Maximum: 1024 characters. You have: " + form.getText().length());
        }
        Optional<User> user = userRepository.findByUsername(form.getUsername());
        if (user.isEmpty()) {
            throw new NotFoundException("User with username " + form.getUsername() + " was not found.");
        }
        Optional<Thread> thread = threadRepository.findById(form.getThreadId());
        if (thread.isEmpty()) {
            throw new NotFoundException("Thread with ID " + form.getThreadId() + " was not found.");
        }
        Post post = new Post();
        post.setAuthor(user.get());
        post.setText(form.getText());
        post.setThread(thread.get());
        log.info("Creating new post by " + user.get().getUsername());
        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot delete without the post ID.");
        }
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new NotFoundException("Post with ID " + id + " does not exist.");
        }
        log.info("Deleting post with ID " + post.get().getId());
        postRepository.deleteById(id);
    }

    @Override
    public Post editPost(Post post) {
        log.info(post.getId().toString());
        if (post.getId() == null) {
            throw new BadRequestException("Field must contain a post ID");
        }
        Optional<Post> postOptional = postRepository.findById(post.getId());
        if (postOptional.isEmpty()) {
            throw new NotFoundException("Post with ID " + post.getId() + " does not exist");
        }
        if (post.getText() != null) {
            if (post.getText().length() == 0) {
                throw new BadRequestException("Text field cannot be empty!");
            }
            if (post.getText().length() > 1024) {
                throw new BadRequestException("Content character limit exceeded." +
                        " Maximum: 1024 characters. You have: " + post.getText().length());
            }
            postOptional.get().setText(post.getText());
        }
        log.info("Saving post");
        return postOptional.get();
    }

    @Override
    public List<Post> getAllPosts() {
        log.info("Fetching all posts");
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot get post without ID");
        }
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new NotFoundException("Post with ID " + id + " does not exist");
        }
        log.info("Fetching post with ID: " + id);
        return post.get();
    }

    @Override
    public List<Post> getAllPostsByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Cannot get posts without username.");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("No user with username " + username + " exists.");
        }
        return (List<Post>) user.get().getPosts();
    }

    @Override
    public List<Post> getAllPostsByUserId(Long id) {
        if (id == null) {
            throw new BadRequestException("Cannot get posts without user ID.");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("No user with ID " + id + " exists.");
        }
        return (List<Post>) user.get().getPosts();
    }
}


