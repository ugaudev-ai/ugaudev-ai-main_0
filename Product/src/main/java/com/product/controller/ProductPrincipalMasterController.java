package com.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.product.response.EndResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.product.appresouce.AppConst;
import com.product.entity.Principal;
import com.product.entity.PrincipalAuditTrial;
import com.product.entity.Product;
import com.product.entity.ProductAuditTrial;
import com.product.entity.ProductPrincipalMap;
import com.product.repository.PrincipalAuditTrialRepository;
import com.product.repository.PrincipalRepository;
import com.product.repository.ProductAuditTrialRepository;
import com.product.repository.ProductPrincipalMapRepository;
//import com.product.repository.ProductPrincipalMapRepository;
import com.product.repository.ProductRepository;
import com.product.request.PrincipalReq;
import com.product.request.ProductPrincipalMapReq;
import com.product.request.ProductReq;
import com.product.service.PrincipalService;
import com.product.service.ProductPrincipalMapService;
import com.product.service.ProductService;

@RestController
@RequestMapping("/api/master")
public class ProductPrincipalMasterController {

	@Autowired private ProductRepository productRepo;
	@Autowired private PrincipalRepository principalRepo;
	@Autowired private ProductPrincipalMapRepository mapRepo;

	@Autowired private ProductAuditTrialRepository productAuditRepo;
	@Autowired private PrincipalAuditTrialRepository principalAuditRepo;
	@Autowired private ProductService prodServ;
	@Autowired private PrincipalService princeServ;
	@Autowired private ProductPrincipalMapService mapServ;
	private static final Logger log = LoggerFactory.getLogger(ProductPrincipalMasterController.class);


	// ====================================
	// Product Operations
	// ====================================

	@PostMapping("/product")
	public String createProduct(@RequestBody Product prod) {
		String finalResp = null;
		EndResult result = null;
		try {
			Product product = productRepo.save(prod);
			result= setEndResult(result,AppConst.SUCCESS, "Product data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(product);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In createProduct(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Product",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In createProduct(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping("/products")
//	public String getAllProducts() {
//		String finalResp = null;
//		List<Product> prodLst = null;
//		EndResult result = new EndResult();
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			prodLst = (List<Product>) productRepo.findAll(pageable);
//			result = setEndResult(result,AppConst.SUCCESS ,"Recieved Product data", AppConst.ERRORCODE_SUCCESS);
//			result.setData(prodLst);
//			ObjectMapper objectMapper = new ObjectMapper();
//			finalResp = objectMapper.writeValueAsString(result);
//			log.info("In getAllProducts(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			log.error("Exception in getAllProducts(), Error Message:"+e.getMessage() , e);
//			result = setEndResult(result,AppConst.FAILURE ,"Exception in getAllProducts()" , AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			return finalResp;
//		}
//		return finalResp;
//	}

	@GetMapping("/products")
	public String getAllProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Product> adminPage = productRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Reports data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", adminPage.getContent());
			data.put("total", adminPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllReports(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllReports(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/product/get")
	public String getProduct(@RequestBody ProductReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(req.getId() == 0) {
				result = setEndResult(result,AppConst.FAILURE,"Invalid Product id:",AppConst.ERRORCODE_FAILURE); 
				log.info("In getProduct(), finalResp:"+finalResp);
				return finalResp;
			}
			Product product = prodServ.getProduct(req);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Product:",AppConst.ERRORCODE_SUCCESS); 
			result.setData(product);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getProduct(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Product:",AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new Gson().toJson(result); 
			log.error("In getProduct(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@GetMapping("/{id}")
	public Product prod(@PathVariable Long id) {
		Product product = prodServ.getprodwithid(id);
		return product;
	}

	@PostMapping("/product/add-product-to-principal")
	public String addProductToPrincipal(@RequestBody ProductPrincipalMapReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(req.getProductId() == 0) {
				result = setEndResult(result,AppConst.FAILURE,"Invalid Product id:"+req.getProductId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In Mapping(), finalResp:"+finalResp);
				return finalResp;
			}
			Principal principal = prodServ.addProdcutToPrincipal(req);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Product&Principal",AppConst.ERRORCODE_SUCCESS); 
			result.setData(principal);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In Mapping(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Product/Principal",AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new Gson().toJson(result); 
			log.error("In Mapping(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/product/update")
	public String updateProduct(@RequestBody ProductReq prodReq) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(prodReq.getId()==0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Product id:"+prodReq.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In updateProduct(), finalResp:"+finalResp);
				return finalResp; 
			}
			Product updatedProd = prodServ.updateProd(prodReq);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Product:"+prodReq.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(updatedProd);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In updateProduct(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Product:"+prodReq.getId(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In updateProduct(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/product/delete")
	public String deleteProduct(@RequestBody ProductReq prodReq) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(prodReq.getId()==0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Product id:"+prodReq.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In deleteProduct(), finalResp:"+finalResp);
				return finalResp; 
			}
			Product deletedProd = prodServ.deleteProd(prodReq);
			result= setEndResult(result,AppConst.SUCCESS,"Product data deleted:"+prodReq.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(deletedProd);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In deleteProduct(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Product:"+prodReq.getId(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In deleteProduct(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/products/delete")
	public String deleteProducts(@RequestBody List<Long> ids) {
		String finalResp = null;
		EndResult result = null;
		try {
			Product deletedProds = prodServ.deleteProds(ids);
			result= setEndResult(result,AppConst.SUCCESS,"Products data deleted",AppConst.ERRORCODE_SUCCESS); 
			result.setData(deletedProds);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In deleteProducts(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Products",AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In deleteProducts(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	// ====================================
	// Principal Operations
	// ====================================

	@PostMapping("/principal")
	public String createProduct(@RequestBody PrincipalReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Principal prince = princeServ.savePrincipal(req);
			result= setEndResult(result,AppConst.SUCCESS, "Principal data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(prince);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In createPrincipal(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Principal id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In createPrincipal(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping("/principals")
//	public String getAllPrincipals() {
//		String finalResp = null;
//		List<Principal> prinList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			prinList = (List<Principal>) principalRepo.findAll(pageable);
//			result = setEndResult(result,AppConst.SUCCESS ,"Recieved Principal data", AppConst.ERRORCODE_SUCCESS);
//			result.setData(prinList);
//			ObjectMapper objectMapper = new ObjectMapper();
//			finalResp = objectMapper.writeValueAsString(result);
//			log.info("In getAllPrincipals(), finalResp:"+finalResp);
//		}catch (Exception e) {
//			log.error("Exception in getAllPrincipals(), Error Message:"+e.getMessage() , e);
//			result = setEndResult(result,AppConst.FAILURE ,"Exception in getAllPrincipals()" , AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			return finalResp;
//		}
//		return finalResp;
//	}

	@GetMapping("/principals")
	public String getAllPrincipals(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Principal> adminPage = principalRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Reports data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", adminPage.getContent());
			data.put("total", adminPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllReports(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllReports(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/principal/get")
	public String getPrincipal(@RequestBody PrincipalReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(req.getId() == 0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid id:"+req.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In getPrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			Principal princpal = princeServ.getPrincipal(req);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Principal:"+req.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(princpal);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getPrincipal(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Principal:"+req.getId(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In getPrincipal(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/principal/update")
	public String updatePrincipal(@RequestBody PrincipalReq princeReq) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(princeReq.getId()==0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Principal id:"+princeReq.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In updatePrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			Principal updatedPrin = princeServ.updatePrincipal(princeReq);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Principal:"+princeReq.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(updatedPrin);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In updatePrincipal(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Principal:"+princeReq.getId(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In updatePrincipal(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/principal/delete")
	public String deletePrincipal(@RequestBody PrincipalReq princReq) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(princReq.getId()==0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid id:"+princReq.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In deletePrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			Principal deletedPrinc = princeServ.deletedPrincipal(princReq);
			result= setEndResult(result,AppConst.SUCCESS,"Principal data deleted:"+princReq.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(deletedPrinc);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In deletePrincipal(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Principal:"+princReq.getId(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In deletePrincipal(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;

	}



	// ====================================
	// MapOperations
	// ====================================

	@PostMapping("/map")
	public String createMap(@RequestBody ProductPrincipalMapReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			ProductPrincipalMap map = mapServ.saveMap(req);
			result= setEndResult(result,AppConst.SUCCESS, "Map data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(map);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In createMap(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage()+"Invalid Product",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In createMap(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
//	@GetMapping("/maps")
//	public String getAllMaps() {
//		String finalResp = null;
//		List<ProductPrincipalMap> mapList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			mapList = (List<ProductPrincipalMap>) mapRepo.findAll(pageable);
//			result = setEndResult(result,AppConst.SUCCESS ,"Recieved Principal data", AppConst.ERRORCODE_SUCCESS);
//			result.setData(mapList);
//			ObjectMapper objectMapper = new ObjectMapper();
//			finalResp = objectMapper.writeValueAsString(result);
//			log.info("In getAllPrincipals(), finalResp:"+finalResp);
//		}catch (Exception e) {
//			log.error("Exception in getAllPrincipals(), Error Message:"+e.getMessage() , e);
//			result = setEndResult(result,AppConst.FAILURE ,"Exception in getAllPrincipals()" , AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@GetMapping("/maps")
	public String getAllMaps(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<ProductPrincipalMap> adminPage = mapRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Reports data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", adminPage.getContent());
			data.put("total", adminPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllReports(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllReports(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/map/get")
	public String getMap(@RequestBody ProductPrincipalMapReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(req.getId() == 0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Id:"+req.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In getPrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			ProductPrincipalMap map = mapServ.getMap(req);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Map:"+req.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(map);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getMap(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In getMap(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	@PostMapping("/map/{id}")
	public String getMapById(@PathVariable Long id) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(id == 0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Id:"+id,AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In getPrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			ProductPrincipalMap map = mapServ.getMapById(id);
//			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Map:"+id,AppConst.ERRORCODE_SUCCESS); 
//			result.setData(map);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(map);
			log.info("In getMap(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In getMap(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	@PostMapping("/map/update")
	public String updateMap(@RequestBody ProductPrincipalMapReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(req.getId() == 0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Id:"+req.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In getPrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			ProductPrincipalMap updatedMap = mapServ.updateMap(req);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Map:"+req.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(updatedMap);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getMap(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In getMap(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/map/delete")
	public String deleteMap(@RequestBody ProductPrincipalMapReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			if(req.getId() == 0) { 
				result = setEndResult(result,AppConst.FAILURE,"Invalid Id:"+req.getId(),AppConst.ERRORCODE_FAILURE); 
				finalResp = new Gson().toJson(result); 
				log.info("In getPrincipal(), finalResp:"+finalResp);
				return finalResp; 
			}
			ProductPrincipalMap deletedMap = mapServ.deleteMap(req);
			result= setEndResult(result,AppConst.SUCCESS,"Recieved data for Map:"+req.getId(),AppConst.ERRORCODE_SUCCESS); 
			result.setData(deletedMap);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getMap(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION); 
			finalResp = new	Gson().toJson(result); 
			log.error("In getMap(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	// ====================================
	// View Audit Logs
	// ====================================

	@GetMapping("/audit/products")
	public String getProductAudits() {
		String finalResp = null;
		List<ProductAuditTrial> prodATList = null;
		EndResult result = null;
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(0, 2, sort);
			prodATList = (List<ProductAuditTrial>) productAuditRepo.findAll(pageable);
			result = setEndResult(result,AppConst.SUCCESS ,"Recieved Map data", AppConst.ERRORCODE_SUCCESS);
			result.setData(prodATList);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getAllProdAT(), finalResp:"+finalResp);
		}catch (Exception e) {
			log.error("Exception in getAllProdAT(), Error Message:"+e.getMessage() , e);
			result = setEndResult(result,AppConst.FAILURE ,"Exception in getAllProdAT()" , AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			return finalResp;
		}
		return finalResp;

	}

	@GetMapping("/audit/principals")
	public String getPrincipalAudits() {
		String finalResp = null;
		List<PrincipalAuditTrial> prinATList = null;
		EndResult result = null;
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(0, 2, sort);
			prinATList = (List<PrincipalAuditTrial>) principalAuditRepo.findAll(pageable);
			result = setEndResult(result,AppConst.SUCCESS ,"Recieved Map data", AppConst.ERRORCODE_SUCCESS);
			result.setData(prinATList);
			ObjectMapper objectMapper = new ObjectMapper();
			finalResp = objectMapper.writeValueAsString(result);
			log.info("In getAllPrinAT(), finalResp:"+finalResp);
		}catch (Exception e) {
			log.error("Exception in getAllPrinAT(), Error Message:"+e.getMessage() , e);
			result = setEndResult(result,AppConst.FAILURE ,"Exception in getAllPrinAT()" , AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			return finalResp;
		}
		return finalResp;
	}

	public EndResult setEndResult(EndResult result,String status, String message, String errorCode) {
		if(result==null) {
			result = new EndResult();
		}
		result.setMessage(message);
		result.setStatus(status);
		result.setErrorCode(errorCode);
		return result;
	}
}
