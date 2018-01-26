package network.karthik.financetracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import network.karthik.financetracker.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
	
	public User findByEmailAndPassword(String email,String password);
	
	Optional<User> findByEmail(String email);
	
}
