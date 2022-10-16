package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAdminPage(@ModelAttribute("user") User user, Model model, Principal principal) {
        List<User> users = userService.getAllUsers();
        users.stream().forEach(u -> u.setStringOfAllUserRoles(u.getStringOfRoles(u)));
        model.addAttribute("users", users.stream().distinct().collect(Collectors.toList()));

        User adminUser = (User) userService.loadUserByUsername(principal.getName());
        List userRoles = adminUser.getRoles().stream()
                .map(x -> x.getRole().substring(5))
                .sorted().collect(Collectors.toList());
        model.addAttribute("adminUser", adminUser);
        model.addAttribute("roles", userRoles);
        model.addAttribute("allRoles", roleService.getAllRoles());
        System.out.println(userRoles);
        return "adminboot1";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/admin")
    public String addNewUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "new";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        user.setStringOfAllUserRoles(user.getStringOfRoles(user));
        model.addAttribute("user", user);
        return "delete";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        user.setStringOfAllUserRoles(user.getStringOfRoles(user));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "/edit";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

}
