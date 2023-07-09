package com.example.graphqljava.service;

import com.example.graphqljava.domain.Clothing;
import com.example.graphqljava.domain.Food;
import com.example.graphqljava.domain.FoodType;
import com.example.graphqljava.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products;


    public ProductService() {
        this.products = Arrays.asList(new Clothing("product-1", "levis'shirt", "https://www.table.sample.org/lunch/shirt.php", "s"), new Food("product-2","apple", "https://town.sample.edu/tub/apple.html", FoodType.VEG));
    }

    public Product findProductById(String id) {
         return this.products.stream()
            .filter(product -> {
                if(product instanceof Food) {
                    Food food = (Food) product;
                    return food.getId().equalsIgnoreCase(id);
                }
                else {
                    Clothing clothing = (Clothing) product;
                    return clothing.getId().equalsIgnoreCase(id);
                }

            })
            .findFirst()
            .orElse(null);

    }

    public List<Product> getAllProducts() {
        return products;
    }
}
