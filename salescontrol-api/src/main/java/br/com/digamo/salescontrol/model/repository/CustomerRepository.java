package br.com.digamo.salescontrol.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.digamo.salescontrol.model.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findByNameOrCpf(String name, String cpf);

	@Query(value = "SELECT c FROM Customer c WHERE (c.name = ?1 OR c.cpf = ?2) AND c.id <> ?3 ")
	Optional<Customer> findByNameOrCpfWithDiffId(String name, String cpf, Long id);
}
