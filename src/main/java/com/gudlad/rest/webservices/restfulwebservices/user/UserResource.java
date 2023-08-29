package com.gudlad.rest.webservices.restfulwebservices.user;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResource {
    private UserDaoService service;

    public UserResource(UserDaoService service) {
        this.service = service;
    }

    // GET /users
    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return service.getUsers();
    }

    // GET /users

    // EntityModel
    // WebMvcLinkBuilder

    // GET /users/4
    @GetMapping(path = "/users/{id}")
    // adding a link to //http://localhost:8080/users
    // hateoas => in addition to data returning few links to consumers to perform subsequent actions
    public EntityModel<User> retrieveUser(@PathVariable int id) {


        User user = service.getUser(id);
        if (user == null)
            throw new UserNotFoundException("id " + id);

        EntityModel<User> entityModel = EntityModel.of(user);
        // linkTo and methodOn are static methods of WebMvcLinkBuilder
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    // POST /users
    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = UserDaoService.save(user);
        // users/4  => users/{id} , user.getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        // /users/4
        return ResponseEntity.created(location).build();
    }

    // GET /delete
    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    }

}