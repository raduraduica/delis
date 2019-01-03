package dk.erst.delis.web.user;

import dk.erst.delis.dao.RoleRepository;
import dk.erst.delis.dao.UserRepository;
import dk.erst.delis.data.user.Role;
import dk.erst.delis.data.user.RoleType;
import dk.erst.delis.data.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Iehor Funtusov, created by 02.01.19
 */

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    User findById(Long id) {
        return findOne(id);
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole(RoleType.ADMIN);
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        userRepository.save(user);
    }

    void deleteUser(Long id) {
        userRepository.delete(findOne(id));
    }

    private User findOne(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user;
        } else {
            throw new RuntimeException();
        }
    }
}
