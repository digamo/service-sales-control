package br.com.digamo.salescontrol.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.digamo.salescontrol.model.entity.ServiceProvided;

public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, Long>{

}
