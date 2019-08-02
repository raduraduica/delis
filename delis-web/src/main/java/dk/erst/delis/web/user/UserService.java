package dk.erst.delis.web.user;

import dk.erst.delis.dao.UserRepository;

import dk.erst.delis.data.entities.user.User;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    User findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
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

    void saveOrUpdateUser(UserData userData) {
        User user;
        if (userData.getId() == null) {
            user = new User();
        } else {
            user = findById(userData.getId());
        }
        if (StringUtils.isNotBlank(userData.getUsername())) {
            user.setUsername(userData.getUsername());
        }
        if (StringUtils.isNotBlank(userData.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        }
        if (StringUtils.isNotBlank(userData.getEmail())) {
            user.setEmail(userData.getEmail());
        }

        user.setUsername(userData.getUsername());
        user.setLastName(userData.getLastName());
        user.setFirstName(userData.getFirstName());
        
        if (userData.isDisabledIrForm()) {
        	user.setDisabledIrForm(Boolean.TRUE);
        } else {
        	user.setDisabledIrForm(Boolean.FALSE);
        }

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
