package com.example.visitor;

import com.example.model.Product;
import com.example.model.Buyer;

public interface Visitor {
    void visitProduct(Product product);
    void visitBuyer(Buyer buyer);
}