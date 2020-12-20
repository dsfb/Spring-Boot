package com.example.cruddemo.controllers;

import com.example.cruddemo.model.User;
import com.example.cruddemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user){
        return "add-user";
    }
    
    @GetMapping("/goback")
    public String goBack() {
        return "redirect:index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        Iterable<User> iterUser = userRepository.findAll();
        if (!iterUser.iterator().hasNext()) {
            return "redirect:/";
        }

        model.addAttribute("users", iterUser);
        return "index";
    }

    @PostMapping("/adduser")
    public String addUser (@Valid User user, BindingResult result, Model model){
        if (result.hasErrors()){
            return "add-user";
        }

        if (userRepository.checkOldUser(user)) {
            return "error";
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model){
        User user = userRepository.findById(id).
                orElseThrow( () -> new IllegalArgumentException("Invalid user Id: " + id) );
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model){
        if (result.hasErrors()){
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model){
        User user = userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

}
