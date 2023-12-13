package com.springbootcrud.springbootcrud.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    HashMap<String, Object> datos;
    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> getProducts(){
        return productRepository.findAll();
    }
    public ResponseEntity<Object> newProduct(Product product) throws Exception {
        Optional<Product> res = productRepository.findProductByName(product.getName());
        datos = new HashMap<>();
        if(res.isPresent() && product.getId()==null){
            //throw new IllegalAccessException("There is already a product with that name");
            datos.put("error",true);
            datos.put("message","There is already a product with that name");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        datos.put("message","saved successfully");
        if(product.getId()!=null){
            datos.put("message","updated successfully");
        }
        productRepository.save(product);
        datos.put("data",product);

        return new ResponseEntity<>(
                datos,
                HttpStatus.CREATED
        );
    }
    public ResponseEntity<Object> deleteProduct(Long id){
        datos = new HashMap<>();
        boolean exists = this.productRepository.existsById(id);
        if(!exists){
            datos.put("error",true);
            datos.put("message","There is no product with that id");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        productRepository.deleteById(id);
        datos.put("message","product deleted");
        return new ResponseEntity<>(
                datos,
                HttpStatus.ACCEPTED
        );
    }
}
