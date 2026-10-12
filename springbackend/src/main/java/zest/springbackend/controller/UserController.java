package zest.springbackend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zest.springbackend.dto.ApiResponse;
import zest.springbackend.dto.CreateUserRequest;
import zest.springbackend.model.User;
import zest.springbackend.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ApiResponse<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ApiResponse.ok("User created", user);
    }

    @GetMapping("/users/{id}")
    public ApiResponse<User> getUser(@PathVariable String id) {
        return userService.findById(id)
            .map(user -> ApiResponse.ok("User loaded", user))
            .orElse(ApiResponse.fail("User not found"));
    }
}
