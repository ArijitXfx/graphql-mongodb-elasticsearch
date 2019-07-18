package com.tricon.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "product", type = "productelasticsearch")
public class ProductElasticSearch {
	@Id
	private String id;
	private String name;
	private Integer price;
}
