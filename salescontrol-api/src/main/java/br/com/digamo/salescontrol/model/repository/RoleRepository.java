package br.com.digamo.salescontrol.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.digamo.salescontrol.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
