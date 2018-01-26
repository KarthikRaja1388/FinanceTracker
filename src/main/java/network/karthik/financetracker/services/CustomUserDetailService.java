package network.karthik.financetracker.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import network.karthik.financetracker.entity.CustomUserDetails;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.repository.IUserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private IUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		optionalUser
			.ifPresent(user ->{ 
				new CustomUserDetails(user);
			});
			
		return optionalUser
					.map(CustomUserDetails :: new).get();
	}

}
