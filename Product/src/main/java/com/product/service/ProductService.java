package com.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.entity.Principal;
import com.product.entity.Product;
import com.product.entity.ProductAuditTrial;
import com.product.repository.PrincipalRepository;
import com.product.repository.ProductAuditTrialRepository;
import com.product.repository.ProductRepository;
import com.product.request.ProductPrincipalMapReq;
import com.product.request.ProductReq;

@Service
public class ProductService {

	@Autowired	private ProductRepository productRepo;
	@Autowired	private ProductAuditTrialRepository productAuditRepo;
	@Autowired	private PrincipalRepository princeRepo;

	//Getting Product from database
	private Product dbProd(ProductReq req) {
		Product prod = null;
		if (req.getId() != null) {
			prod = productRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Product not found with id: " + req.getId()));
		} 
		return prod;
	}
	
	//Getting Product By id, Id from the request Body
	public Product getprodwithid(Long id) {
		Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
		return product;
	}
	
	//Mapping data to AuditTrial Before Updating
	private void prodAT(Product product, ProductReq req) {
		ProductAuditTrial audit = new ProductAuditTrial();
		BeanUtils.copyProperties(product, audit);
		audit.setpReq_description(req.getpReq_description());
		productAuditRepo.save(audit);
	}
	
	//Retrieving data
	public Product getProduct(ProductReq req) {
		Product product = dbProd(req);
		return product;
	}

	//Updating the data
	public Product updateProd(ProductReq prodReq) {
		Product existing = dbProd(prodReq);
		prodAT(existing, prodReq);
		if (prodReq.getProductName() != null) existing.setProductName(prodReq.getProductName());
		if (prodReq.getDescription() != null) existing.setDescription(prodReq.getDescription());
		existing.setStatus("U");
		return productRepo.save(existing);
	}

	//SoftDeleting the Product
	public Product deleteProd(ProductReq prodReq) {
		Product existing = dbProd(prodReq);
		prodAT(existing, prodReq);
		existing.setStatus("D");
		return productRepo.save(existing);
	}
	
	//SoftDelete Multiple products By List of Ids
	public Product deleteProds(List<Long> ids) {
		List<Product> products = (List<Product>) productRepo.findAllById(ids);

        for (Product product : products) {
            product.setStatus("D");
        }
		return productRepo.save(products);
	}
	
	//Mapping Product to the Principal
	public Principal addProdcutToPrincipal(ProductPrincipalMapReq req) {
		Optional<Principal> principal = princeRepo.findById(req.getPrincipalId());
		Optional<Product> product = productRepo.findById(req.getProductId());
		
		if(principal.isPresent() && product.isPresent()) {
			principal.get().getProducts().add(product.get());
			princeRepo.save(principal.get());		
			return principal.get();
		}else {
			throw new IllegalArgumentException("Principal or Product not found");
		}
	}
	
}
