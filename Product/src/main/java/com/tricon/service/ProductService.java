package com.tricon.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tricon.bean.Product;
import com.tricon.bean.ProductElasticSearch;
import com.tricon.repository.ProductElasticRepository;
import com.tricon.repository.ProductRepository;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@GraphQLApi
@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductElasticRepository elasticsearchRepository;

	@GraphQLQuery(name = "products")
	public List<Product> getProducts(){
		return productRepository.findAll();
	}

	@GraphQLQuery(name = "product")
	public Product getProductById(@GraphQLArgument(name = "id") String id) {
		return productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found with id: "+id));
	}

	@GraphQLQuery(name = "someProduct")
	public List<Product> getSomeProduct(int point, int first, int last){
		ArrayList<Product> productList = new ArrayList<Product>(productRepository.findAll(Sort.by("id")));
		if(first>0) first = point-first;
		else first = point;
		if(last!=-1) last = point+last-1;
		else last = point;
		if(first<0) first = 0;
		if(last>productRepository.count()) last = (int) productRepository.count();
		List<Product> requiredList = new LinkedList<Product>();
		for(int i=first;i<=last;i++) {
			requiredList.add(productList.get(i));
		}
		return requiredList;
	}

	@GraphQLMutation(name = "saveProduct")
	public Product saveProduct(Product product) {
		elasticsearchRepository.save(new ProductElasticSearch(product.getId(),product.getName(),product.getPrice()));
		return productRepository.save(product);
	}

	@GraphQLMutation(name = "updateProduct")
	public Product updateProduct(Product product) {

		Optional<ProductElasticSearch> oldElas = productFromElasticSearchById(product.getId());
		oldElas.get().setName(product.getName());
		oldElas.get().setPrice(product.getPrice());
		elasticsearchRepository.save(oldElas.get());

		Product oldProduct = getProductById(product.getId());
		oldProduct.setName(product.getName());
		oldProduct.setPrice(product.getPrice());
		return productRepository.save(oldProduct);

	}
	
	@GraphQLMutation(name = "deleteProduct")
	public String deleteProduct(String id) {
		elasticsearchRepository.deleteById(id);
		productRepository.deleteById(id);
		return "Deleted!";
	}

	@GraphQLQuery(name = "productsFormElasticSearch")
	public Iterable<ProductElasticSearch> productsFormElasticSearch(){
		return elasticsearchRepository.findAll();
	}

	@GraphQLQuery(name = "productFromElasticSearchByName")
	public ProductElasticSearch productFromElasticSearchByName(@GraphQLArgument(name = "name") String name) {
		return elasticsearchRepository.findByName(name);
	}

	@GraphQLQuery(name = "productFromElasticSearchById")
	public Optional<ProductElasticSearch> productFromElasticSearchById(@GraphQLArgument(name = "id") String id) {
		return elasticsearchRepository.findById(id);
	}
}
