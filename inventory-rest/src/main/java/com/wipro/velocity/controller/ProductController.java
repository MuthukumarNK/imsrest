package com.wipro.velocity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wipro.velocity.exception.ResourceNotFoundException;
import com.wipro.velocity.model.Product;
import com.wipro.velocity.repository.ProductRepository;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping(value="/api")
public class ProductController 
{
	@Autowired
	private ProductRepository prepo;
	
	@PostMapping("/products")
	public Product saveProduct(@Validated @RequestBody Product product) //@RequestBody automatically deserializes the JSON into a Java type
	{
		return prepo.save(product); //invokes save() method of MongoRepository (ProductRepository) 
	}
	
	@GetMapping("/products")
	public List<Product> getAllProducts()
	{
		return prepo.findAll();
	}
	
	// PUT - http://localhost:9095/ims/api/products
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value="id") String pId,
    		@Validated @RequestBody Product p) throws ResourceNotFoundException
    {
    	Product product=prepo.findById(pId).orElseThrow(() -> new ResourceNotFoundException
    			("Product Not Found for this Id: " +pId));
    	product.setName(p.getName());
    	product.setBrand(p.getBrand());
    	product.setMadein(p.getMadein());
    	product.setPrice(p.getPrice());
    	
    	final Product updatedProduct=prepo.save(product);
    	return ResponseEntity.ok(updatedProduct);
    }
    
 // DELETE - http://localhost:9095/ims/api/products/101
    @DeleteMapping("products/{id}")
    public Map<String,Boolean> deleteProduct(@PathVariable(value="id") String pId)
     throws ResourceNotFoundException
     {
     Product product=prepo.findById(pId).orElseThrow(() -> new ResourceNotFoundException
 			("Product Not Found for this Id: " +pId));
    
    prepo.delete(product);
    Map<String, Boolean> response=new HashMap<>();
    response.put("Deleted the product", Boolean.TRUE);
    return response;
     }
    
 // GET - http://localhost:9095/ims/api/products/101
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value="id") String pId)
    		 throws ResourceNotFoundException{
    	Product product=prepo.findById(pId).orElseThrow(() -> new ResourceNotFoundException
     			("Product Not Found for this Id: " +pId));
        
    	return ResponseEntity.ok().body(product);
    }
    
 // GET - http://localhost:9095/ims/api/getproductsbycountry/India
 	@GetMapping("/getproductsbycountry/{madein}")
 	public List<Product> getProductsByMadein(@PathVariable(value="madein") String madein)
 	{
 		//invokes findByMadein() custom method of MongoRepository
 		return (List<Product>) prepo.findByMadein(madein);  //invokes findAll() method of MongoRepository
 		
 	}
}
