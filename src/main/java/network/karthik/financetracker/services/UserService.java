package network.karthik.financetracker.services;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.repository.IUserRepository;

@Service
public class UserService {

	@Autowired
	private IUserRepository userRepository;

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void add(User user) {
		userRepository.save(user);
	}
	
	public void update(User user) {
		User userById = userRepository.findOne(user.getUserId());
		userById.setFirstName(user.getFirstName());
		userById.setLastName(user.getLastName());
		userById.setEmail(user.getEmail());
		
		userRepository.save(userById);
	}
	
	public void delete(Long userId) {
		userRepository.delete(userId);
	}
	
	public User login(String email, String password) {
		User userFromDb = userRepository.findByEmailAndPassword(email, password);
		return userFromDb;
	}
	
	public User findUserById(Long id) {
		return userRepository.findOne(id);
	}
	
	public void sendResetPasswordEmail(String email) {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.karthik.network");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLFactory");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "465");
		
	}
}
