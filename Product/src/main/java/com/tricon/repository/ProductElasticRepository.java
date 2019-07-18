package com.tricon.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tricon.bean.ProductElasticSearch;

@Repository
public interface ProductElasticRepository extends ElasticsearchRepository<ProductElasticSearch, String>{

	ProductElasticSearch findByName(String name);

}
