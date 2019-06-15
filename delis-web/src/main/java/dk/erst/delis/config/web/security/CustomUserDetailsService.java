package dk.erst.delis.config.web.security;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dk.erst.delis.dao.UserRepository;
import dk.erst.delis.data.entities.user.User;
import dk.erst.delis.web.main.GlobalController;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

		User user = userRepository.findByUsernameIgnoreCase(login);

		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException(login + " not found");
		}

		List<GrantedAuthority> authorities;
		if (user.getOrganisation() == null) {
			authorities = Arrays.asList(GlobalController.ADMIN_AUTHORITY);
		} else {
			authorities = Arrays.asList(GlobalController.USER_AUTHORITY);
		}

		return new CustomUserDetails(user, authorities);
	}
}
