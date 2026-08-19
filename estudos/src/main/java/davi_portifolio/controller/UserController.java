package davi_portifolio.controller;

import davi_portifolio.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("/{id.user}")
    public HttpResponse getUser(Long id){
        return null;
    }

    @PostMapping()
    public HttpResponse setUser(@RequestBody String username){
        return null;
    }

}
