package br.com.digamo.salescontrol.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.digamo.salescontrol.model.entity.ServiceProvided;

@Service
@Transactional(readOnly = true)
public class ServiceProvidedRepositoryImpl implements ServiceProvidedRepositoryCustom{ 

    @PersistenceContext
    private EntityManager entityManager;

    @Override
	public Page<ServiceProvided> findServiceByCustomerNameAndServiceMonth(String customerName, Integer serviceMonth, Pageable pageable) {

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
        
        TypedQuery<ServiceProvided> listOfServices = entityManager.createQuery(criteria);
		
        List<ServiceProvided> resultList = listOfServices.getResultList();

        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        
        int max = (size*(page+1)>resultList.size())? resultList.size(): size*(page+1);
        
        Page<ServiceProvided> pageServiceProvided = new PageImpl<ServiceProvided>(resultList.subList(page*size, max), pageable, resultList.size());
        
        return pageServiceProvided;
	}
	
}
