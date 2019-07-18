package com.tricon.load;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tricon.bean.ProductElasticSearch;
import com.tricon.repository.ProductElasticRepository;
import com.tricon.repository.ProductRepository;

@Component
public class Loaders {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductElasticRepository elasticRepository;
	
	@PostConstruct
	public void loadData() {
		productRepository.findAll().forEach(product->{
			elasticRepository.save(new ProductElasticSearch(product.getId(),product.getName(),product.getPrice()));
		});
		
	}
}
