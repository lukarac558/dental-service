package com.student.reservationservice.user.service;

import com.student.reservationservice.user.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ApplicationUser>> getAllUsers() {
        List<ApplicationUser> employees = userService.findAllUsers();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ApplicationUser> getUserById(@PathVariable("id") Long id) {
        ApplicationUser employee = userService.findUserById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApplicationUser> addUser(@RequestBody ApplicationUser employee) {
        ApplicationUser newEmployee = userService.addUser(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApplicationUser> updateUser(@RequestBody ApplicationUser employee) {
        ApplicationUser updateEmployee = userService.addUser(employee);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
