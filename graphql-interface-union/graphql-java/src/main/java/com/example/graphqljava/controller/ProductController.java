package com.example.graphqljava.controller;

import com.example.graphqljava.domain.Product;
import com.example.graphqljava.service.CartService;
import com.example.graphqljava.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @QueryMapping
    public Product getProductDetails(@Argument String id) {
        return productService.findProductById(id);
    }

    @QueryMapping
    public List<Object> getCartResult() {
        return cartService.getCartItems();
    }
}
