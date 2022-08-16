package com.learnings.wwl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnings.wwl.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>  {

}
