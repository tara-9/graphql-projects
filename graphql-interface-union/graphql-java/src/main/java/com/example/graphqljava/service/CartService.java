package com.example.graphqljava.service;

import com.example.graphqljava.domain.Clothing;
import com.example.graphqljava.domain.Food;
import com.example.graphqljava.domain.FoodType;
import com.example.graphqljava.domain.Recommendation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartService {

    private List<Object> cartItems;


    public CartService() {
        this.cartItems = Arrays.asList(new Clothing("product-1", "levis'shirt", "https://www.table.sample.org/lunch/shirt.php", "s"),
                new Food("product-2","apple", "https://town.sample.edu/tub/apple.html", FoodType.VEG));

    }

    public List<Object> getCartItems() {
        // Creating a size parameter to simulate empty or non empty cart
        int size = (int) Math.round(Math.random());
        if(size == 0) {
            // Cart is Empty
            Recommendation product = new Recommendation("product-45", "Nike's Snicker", 100);
            List<Object> recommendations = new ArrayList<>();
            recommendations.add(product);
            return recommendations;
        }
        // Cart is nonempty
        else return cartItems;

    }

}
