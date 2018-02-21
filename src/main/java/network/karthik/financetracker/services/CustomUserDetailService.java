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
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private IUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		
		if(user == null){
			throw new UsernameNotFoundException("Username Not found");
		}else{
			return new CustomUserDetails(user);
		}
	}

}
