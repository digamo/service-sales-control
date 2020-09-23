package br.com.digamo.salescontrol.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.digamo.salescontrol.model.entity.ServiceProvided;

@Repository
public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, Long>, ServiceProvidedRepositoryCustom {

}
