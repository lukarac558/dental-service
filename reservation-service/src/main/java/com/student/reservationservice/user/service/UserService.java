package com.student.reservationservice.user.service;

import com.student.reservationservice.user.entity.ApplicationUser;
import com.student.reservationservice.user.exception.UserNotFoundException;
import com.student.reservationservice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApplicationUser addUser(ApplicationUser user) {
        encodePassword(user);
        return userRepository.save(user);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        encodePassword(user);
        return userRepository.save(user);
    }

    public List<ApplicationUser> findAllUsers() {
        return userRepository.findAll();
    }

    public ApplicationUser findUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id= " + id + " was not found."));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }

    private void encodePassword(ApplicationUser user) {
        String encodedPassword = getEncodedPassword(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
