package com.enigma.tokonyadia.service;

import com.enigma.tokonyadia.entity.Customer;
import com.enigma.tokonyadia.model.request.RegisterWalletRequest;
import com.enigma.tokonyadia.model.response.WalletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    Customer create(Customer customer);
    WalletResponse createWallet(RegisterWalletRequest request);
    List<Customer> createBulk(List<Customer> customers);
    Customer getById(String id);
    List<Customer> getAll();
    List<Customer> searchByNameOrPhoneOrEmail(String name, String phone, String email);
    Customer update(Customer customer);
    void deleteById(String id);

}
