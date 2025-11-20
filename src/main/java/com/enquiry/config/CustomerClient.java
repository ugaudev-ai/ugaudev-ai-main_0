package com.enquiry.config;

import org.springframework.stereotype.Component;

import com.enquiry.request.CustomerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class CustomerClient {

    private final String customerServiceUrl = "http://localhost:8081/Customer/api/customers/";
    private final ObjectMapper mapper = new ObjectMapper();

    public CustomerDTO saveCustomer(CustomerDTO customerRequest) {
        try {
            // Convert customer object to JSON string
            String jsonBody = mapper.writeValueAsString(customerRequest);
            
            String url = customerServiceUrl + "save";

            System.out.println("Sending request to Customer API: " + customerServiceUrl);
            System.out.println("Request body: " + jsonBody);

            // Send POST request
            HttpResponse<String> response = Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            System.out.println("Response status: " + response.getStatus());
            System.out.println("Response body: " + response.getBody());

            // Check status code (200 or 201 expected)
            int status = response.getStatus();
            if (status != 200 && status != 201) {
                throw new RuntimeException("Failed to save Customer. Status: " + status);
            }

            // Parse response JSON to get saved CustomerDTO
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            if (dataNode == null || dataNode.isNull()) {
                throw new RuntimeException("No customer data found in response");
            }

            return mapper.treeToValue(dataNode, CustomerDTO.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON processing error", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save Customer", e);
        }
    }
    
    public CustomerDTO getCustomerById(Long customerId) {
        try {
            HttpResponse<String> response = Unirest.post(customerServiceUrl + customerId)
                    .header("Accept", "application/json")
                    .asString();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to fetch customer " + customerId);
            }
         // Parse the outer JSON and extract "data"
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            if (dataNode == null || dataNode.isNull()) {
                throw new RuntimeException("No customer data found in response");
            }

            return mapper.treeToValue(dataNode, CustomerDTO.class);


//            return mapper.readValue(response.getBody(), CustomerDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer " + customerId, e);
        }
    }
    
    public CustomerDTO updateCustomer(CustomerDTO customerRequest) {
        try {
            String url = customerServiceUrl + "update";  // your customer service update endpoint

            // Convert customer object to JSON
            String jsonBody = mapper.writeValueAsString(customerRequest);

            System.out.println("Sending request to Customer API: " + url);
            System.out.println("Request body: " + jsonBody);

            // Send POST request (or PUT if your service uses PUT for update)
            HttpResponse<String> response = Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            System.out.println("Response status: " + response.getStatus());
            System.out.println("Response body: " + response.getBody());

            int status = response.getStatus();
            if (status != 200 && status != 201) {
                throw new RuntimeException("Failed to update Customer. Status: " + status);
            }

            // Parse response JSON to get updated CustomerDTO
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            if (dataNode == null || dataNode.isNull()) {
                throw new RuntimeException("No customer data found in response");
            }

            return mapper.treeToValue(dataNode, CustomerDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

}

