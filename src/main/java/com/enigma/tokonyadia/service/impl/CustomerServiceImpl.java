package com.enigma.tokonyadia.service.impl;

import com.enigma.tokonyadia.entity.Customer;
import com.enigma.tokonyadia.model.request.RegisterWalletRequest;
import com.enigma.tokonyadia.model.response.WalletResponse;
import com.enigma.tokonyadia.repository.CustomerRepository;
import com.enigma.tokonyadia.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    @Override
    public Customer create(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already used");
        }
    }

    @Override
    public WalletResponse createWallet(RegisterWalletRequest request) {
        HttpEntity<RegisterWalletRequest> entity = new HttpEntity<>(request);

        ResponseEntity<WalletResponse> api = restTemplate.exchange(
                "https://5c9d-2001-448a-2020-e1c9-e492-df98-b1f0-191e.ngrok-free.app/wallets",
                HttpMethod.POST,
                entity,
                WalletResponse.class
        );

        return api.getBody();
    }

    @Override
    public List<Customer> createBulk(List<Customer> customers) {
        return customers.stream().map(this::create).collect(Collectors.toList());
    }

    @Override
    public Customer getById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found"));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> searchByNameOrPhoneOrEmail(String name, String mobilePhone, String email) {
        Specification<Customer> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (mobilePhone != null) {
                Predicate mobilePhonePredicate = criteriaBuilder.equal(root.get("mobilePhone"), mobilePhone);
                predicates.add(mobilePhonePredicate);
            }

            if (email != null) {
                Predicate emailPredicate = criteriaBuilder.equal(root.get("email"), email);
                predicates.add(emailPredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        return customerRepository.findAll(specification);
    }

    @Override
    public Customer update(Customer customer) {
        Customer currentCustomer = getById(customer.getId());

        if (currentCustomer != null) {
            return customerRepository.save(customer);
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
    }
}
