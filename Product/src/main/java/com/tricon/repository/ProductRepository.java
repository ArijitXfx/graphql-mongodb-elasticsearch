package com.tricon.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tricon.bean.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

//	Product findById(String id);

}
