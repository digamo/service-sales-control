package br.com.digamo.salescontrol.model.repository.custom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.com.digamo.salescontrol.model.entity.ServiceProvided;

@Repository
public class ServiceProvidedCustomRepository{ 

    @PersistenceContext
    private EntityManager entityManager;

	public List<ServiceProvided> findServiceByCustomerNameAndServiceMonth(String customerName, Integer serviceMonth) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceProvided> criteria = builder.createQuery(ServiceProvided.class);
        Root<ServiceProvided> service = criteria.from(ServiceProvided.class);
 
        List < Predicate > predicates = new ArrayList < Predicate > ();
        
        if(!StringUtils.isEmpty(customerName)) {
        	Path<String> path = service.get("customer").get("name");
        	predicates.add(builder.like(path, "%" + customerName + "%"));
        }
        
        if(serviceMonth != null) 
        	predicates.add(builder.equal(builder.function("MONTH", Integer.class, service.get("dateService")), serviceMonth));

        
        if (!predicates.isEmpty()) 
        	criteria.select(service).where(predicates.toArray(new Predicate[] {}));
        
        return entityManager.createQuery(criteria).getResultList();
	}
	
}
