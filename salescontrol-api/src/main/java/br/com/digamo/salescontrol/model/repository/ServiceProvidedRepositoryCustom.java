package br.com.digamo.salescontrol.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digamo.salescontrol.model.entity.ServiceProvided;

public interface ServiceProvidedRepositoryCustom {

	Page<ServiceProvided> findServiceByCustomerNameAndServiceMonth(String customerName, Integer serviceMonth, Pageable pageable);
}
