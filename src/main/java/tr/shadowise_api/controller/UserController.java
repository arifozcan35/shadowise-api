package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.entity.User;
import tr.shadowise_api.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }
}
