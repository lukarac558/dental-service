package com.student.reservationservice.user.applicationuser.service;

import com.student.reservationservice.common.exception.entity.IncorrectFormatException;
import com.student.reservationservice.common.exception.entity.IncorrectValueException;
import com.student.reservationservice.common.exception.entity.ObjectAlreadyExistsException;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.PESELValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PESELValidator peselValidator = new PESELValidator();
    private final EmailValidator emailValidator = new EmailValidator();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        peselValidator.initialize(null);
        emailValidator.initialize(null);
    }

    public ApplicationUser register(ApplicationUser user) {
        validate(user);
        // encodePassword(user);
        return userRepository.save(user);
    }

    public ApplicationUser login(String email, String password) {
        // String encodedPassword = getEncodedPassword(password);
        return userRepository.findApplicationUserByEmailAndPassword(email, password)
                .orElseThrow(() -> new IncorrectValueException(INCORRECT_LOGIN_DATA_MESSAGE));
    }

    public ApplicationUser assignCompetencyInformation(ApplicationUser user) {
        return userRepository.save(user);
    }

    public Optional<ApplicationUser> findUserById(Long id) {
        return userRepository.findApplicationUserById(id);
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

    private void validate(ApplicationUser user) {
        validateEmail(user.getEmail());
        validatePesel(user.getPesel());
    }

    private void validateEmail(String email) {
        validateEmailUniqueness(email);
        validateEmailFormat(email);
    }

    private void validateEmailUniqueness(String email) {
        Optional<ApplicationUser> user = userRepository.findApplicationUserByEmail(email);
        user.ifPresent(u -> {
            throw new ObjectAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, email));
        });
    }

    private void validateEmailFormat(String email) {
        if (!emailValidator.isValid(email, null)) {
            throw new IncorrectFormatException(String.format(INCORRECT_EMAIL_FORMAT_MESSAGE, email));
        }
    }

    private void validatePesel(Long pesel) {
        if (!peselValidator.isValid(pesel.toString(), null)) {
            throw new IncorrectValueException(String.format(INCORRECT_PESEL_MESSAGE, pesel));
        }
    }
}
