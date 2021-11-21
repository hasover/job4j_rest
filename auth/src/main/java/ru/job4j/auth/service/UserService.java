package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import ru.job4j.auth.model.User;
import ru.job4j.auth.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
