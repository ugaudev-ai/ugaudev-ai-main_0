package com.enquiry.config;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.springframework.stereotype.Component;

import com.enquiry.request.ProductDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductClient {

    private final String productServiceUrl = "http://localhost:8082/Product/api/master/product/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductDTO saveProduct(ProductDTO product) {
        try {
        	String url = productServiceUrl + "save";
            // Convert product to JSON string
            String productJson = objectMapper.writeValueAsString(product);

            HttpResponse<String> response = Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .body(productJson)
                    .asString();

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                throw new RuntimeException("Failed to save product. Status: " + response.getStatus());
            }
            
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            if (dataNode == null || dataNode.isNull()) {
                throw new RuntimeException("No customer data found in response");
            }

            // Convert response JSON to ProductDto
            return objectMapper.treeToValue(dataNode, ProductDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }
    }
    
    public ProductDTO getProductById(Long productId) {
        try {
            HttpResponse<String> response = Unirest.get(productServiceUrl + productId)
                    .header("Accept", "application/json")
                    .asString();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to get product " + productId);
            }

            return objectMapper.readValue(response.getBody(), ProductDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching product " + productId, e);
        }
    }
    
    public ProductDTO updateProduct(ProductDTO product) {
        try {
            String url = productServiceUrl + "update";  // assuming your product service has /update endpoint

            // Convert product to JSON
            String productJson = objectMapper.writeValueAsString(product);

            System.out.println("Sending request to Customer API: " + url);
            System.out.println("Request body: " + productJson);
            
            HttpResponse<String> response = Unirest.post(url)  // or .put(url) if your service expects PUT
                    .header("Content-Type", "application/json")
                    .body(productJson)
                    .asString();

            if (response.getStatus() != 200 && response.getStatus() != 201) {
                throw new RuntimeException("Failed to update product. Status: " + response.getStatus());
            }
            
            System.out.println("Response status: " + response.getStatus());
            System.out.println("Response body: " + response.getBody());

            // Parse response JSON
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            if (dataNode == null || dataNode.isNull()) {
                throw new RuntimeException("No product data found in response");
            }

            return objectMapper.treeToValue(dataNode, ProductDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }

}
