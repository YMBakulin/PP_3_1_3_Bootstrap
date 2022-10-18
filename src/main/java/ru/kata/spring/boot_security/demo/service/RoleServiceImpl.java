package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
       return roleRepository.findAll();
    }

    public Role getRoleById(long id) {
        return roleRepository.getById(id);
    }

    public List<String> getListOfNamesUserRoles(User user) {
        return user.getRoles().stream()
               .map(x -> x.getRole().substring(5))
               .sorted().collect(Collectors.toList());
    }

}
