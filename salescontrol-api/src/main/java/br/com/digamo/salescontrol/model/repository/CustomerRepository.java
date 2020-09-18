package br.com.digamo.salescontrol.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.digamo.salescontrol.model.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
