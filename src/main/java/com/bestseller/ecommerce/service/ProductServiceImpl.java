package com.bestseller.ecommerce.service;

import com.bestseller.ecommerce.entity.Product;
import com.bestseller.ecommerce.exception.DuplicateProductException;
import com.bestseller.ecommerce.exception.ProductNotFoundException;
import com.bestseller.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<Product> getAllProducts() {
		return StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public Optional<Product> getProduct(Long id) {
		return productRepository.findById(id);
	}

	/**
	 * Creates a new product.
	 *
	 * @param product
	 *            {@link Product} details to be added.
	 */
	@Override
	public void create(Product product){
		productRepository.findByNameIgnoreCase(product.getName()).ifPresent(p -> {
			throw new DuplicateProductException(p.getName());
		});
		productRepository.save(product);
	}

	/**
	 * Updated the specified product.
	 *
	 * @param product
	 * 			  {@link Product} details to be updated.
	 */
	@Override
	public void update(Product product) {
		Long id = productRepository.findByNameIgnoreCase(product.getName()).map(Product::getId).orElseThrow(() -> new ProductNotFoundException(product.getId()));
		product.setId(id);
		productRepository.save(product);
	}

	/**
	 * Deletes the product.
	 *
	 * @param productId
	 *            Product id which will be deleted.
	 */
	@Override
	public void delete(Long productId) {
		productRepository.findById(productId).ifPresent(productRepository::delete);
	}

}
