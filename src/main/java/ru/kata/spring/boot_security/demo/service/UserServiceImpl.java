package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found",username));
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.stream().forEach(u -> u.setStringOfAllUserRoles(u.getStringOfRoles(u)));
        return users;
    }

    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void removeUserById(long id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }




}
