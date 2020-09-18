package br.com.digamo.salescontrol.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.digamo.salescontrol.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUsername (String username);

	boolean existsByUsername(String username);

}
