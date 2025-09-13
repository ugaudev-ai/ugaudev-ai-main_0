package com.product.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.entity.Principal;
import com.product.entity.Product;
import com.product.entity.ProductPrincipalMap;
import com.product.repository.PrincipalRepository;
import com.product.repository.ProductPrincipalMapRepository;
import com.product.repository.ProductRepository;
import com.product.request.ProductPrincipalMapReq;

@Service
public class ProductPrincipalMapService {
	
	@Autowired private ProductRepository prodRepo;
	@Autowired private PrincipalRepository princeRepo;
	@Autowired private ProductPrincipalMapRepository mapRepo;

	private ProductPrincipalMap dbMap(ProductPrincipalMapReq req) {
		ProductPrincipalMap map = null;
		if (req.getId() != null) {
			map = mapRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Map not found with Id: " + req.getId()));
		}else if(req.getProductCode() != null) {
			map = mapRepo.findByProductCode(req.getProductCode()).orElseThrow(() -> new RuntimeException("Map not found with ProductCode: " + req.getProductCode()));
		}
		return map;
	}
	
	public ProductPrincipalMap saveMap(ProductPrincipalMapReq req) {
		ProductPrincipalMap map = new ProductPrincipalMap();
		Optional<Principal> principal = princeRepo.findById(req.getPrincipalId());
		Optional<Product> product = prodRepo.findById(req.getProductId());
		
		if(principal.isPresent() && product.isPresent()) {
			map.setPrincipal(principal.get());
			map.setProduct(product.get());
			map.setProductCode(req.getProductCode());
			map.setStatus("N");
			addProdcutToPrincipal(req);
			mapRepo.save(map);
			return map;
		}
		else {
			throw new IllegalArgumentException("Principal or Product not found");
		}
	}
	
	public ProductPrincipalMap getMap(ProductPrincipalMapReq req) {
		ProductPrincipalMap existing = dbMap(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Map details not exist for id:"+req.getId());
		}else {
		return existing;
		}
	}
	public ProductPrincipalMap getMapById(Long id) {
		ProductPrincipalMap existing =mapRepo.findById(id).orElseThrow(() -> new RuntimeException("Map not found with Id: " + id)); ;
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Map details not exist for id:"+id);
		}else {
		return existing;
		}
	}
	
	public ProductPrincipalMap updateMap(ProductPrincipalMapReq req) {
		ProductPrincipalMap existing = dbMap(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Map details not exist for id:"+req.getId());
		}
		if(req.getPrincipalId() != null){

		}
		if(req.getProductId() != null) {
			Optional<Product> product = prodRepo.findById(req.getProductId());
			Product prod = existing.getProduct();
			Principal prince = existing.getPrincipal();
			updateProductInPrincipal(prod, prince, req);
			existing.setProduct(product.get());
		}
		if(req.getProductCode() != null) existing.setProductCode(req.getProductCode());
		existing.setStatus("U");
		mapRepo.save(existing);
		return existing;
	}
	
	public ProductPrincipalMap deleteMap(ProductPrincipalMapReq req) {
		ProductPrincipalMap existing = dbMap(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Map details not exist for id:"+req.getId());
		}else {
		existing.setStatus("D");
		Product product = existing.getProduct();
		Principal principal = existing.getPrincipal();
		removeProductFromPrincipal(product, principal);
		mapRepo.save(existing);
		return existing;
		}
	}
	
	//Mapping Product to the Principal
		public Principal addProdcutToPrincipal(ProductPrincipalMapReq req) {
			Optional<Principal> principal = princeRepo.findById(req.getPrincipalId());
			Optional<Product> product = prodRepo.findById(req.getProductId());
			
			if(principal.isPresent() && product.isPresent()) {
				principal.get().getProducts().add(product.get());
				princeRepo.save(principal.get());		
				return principal.get();
			}else {
				throw new IllegalArgumentException("Principal or Product not found");
			}
		}
		
		public Principal removeProductFromPrincipal(Product product, Principal principal) {
			principal.getProducts().remove(product);
			princeRepo.save(principal);
			return principal;
		}
		
		public Principal updateProductInPrincipal (Product product, Principal principal, ProductPrincipalMapReq req) {
			Optional<Product> prod = prodRepo.findById(req.getProductId());
			
			principal.getProducts().remove(product);
			principal.getProducts().add(prod.get());
			princeRepo.save(principal);
			return principal;
		}
}
