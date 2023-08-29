package com.gudlad.rest.webservices.restfulwebservices.user;

import com.gudlad.rest.webservices.restfulwebservices.jpa.UserRepository;
import com.gudlad.rest.webservices.restfulwebservices.userpost.Post;
import com.gudlad.rest.webservices.restfulwebservices.userpost.PostRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResource {

    private UserRepository userRepository; // for user details
    private PostRepository postRepository; // for user posts

    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping(path = "/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping(path = "/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException("id " + id);

        EntityModel<User> entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    @PostMapping(path = "/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new UserNotFoundException("id " + id);
        }
    }


    // API's related to the Post

    @GetMapping(path = "/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException("id " + id);
        return user.get().getPosts();
    }

    @GetMapping(path = "/jpa/users/{uid}/posts/{pid}")
    public Post retrieveParticularPostsForUser(@PathVariable int uid, @PathVariable int pid) {
        Optional<User> user = userRepository.findById(uid);
        if (user.isEmpty())
            throw new UserNotFoundException("id " + uid);

        List<Post> posts = user.get().getPosts();
        for (Post post : posts) {
            if (post.getId() == pid)
                return post; // Return the post when found
        }

        throw new UserNotFoundException("Post with pid " + pid + " not found for user " + uid);
    }

    @PostMapping(path = "/jpa/users/{id}/posts")
    public ResponseEntity<Post> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException("id " + id);
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        /// returns uri
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/jpa/users/{id}/posts")
    public void deleteUserPost(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException("id " + id);
        List<Post> posts = user.get().getPosts();
        for (Post post : posts) {
            postRepository.delete(post);
        }

    }

    @DeleteMapping(path = "/jpa/users/{uid}/posts/{pid}")
    public void deleteParticularPost(@PathVariable int uid, @PathVariable int pid) {
        Optional<User> user = userRepository.findById(uid);
        if (user.isEmpty())
            throw new UserNotFoundException("id " + uid);

        List<Post> posts = user.get().getPosts();

        if (posts.isEmpty()) {
            throw new UserNotFoundException("No posts found for user " + uid);
        }

        boolean postFound = false;
        for (Post post : posts) {
            if (post.getId() == pid) {
                postRepository.delete(post);
                postFound = true;
                break; // Break out of the loop after deleting the post
            }
        }

        if (!postFound) {
            throw new UserNotFoundException("Post with pid " + pid + " not found for user " + uid);
        }
    }

}