package com.schwarz.service;

import com.schwarz.domain.CustomerRequestDTO;
import com.schwarz.domain.CustomerResponseDTO;
import com.schwarz.exceptions.BusinessException;
import com.schwarz.model.Customer;
import com.schwarz.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public CustomerResponseDTO create(CustomerRequestDTO customer) {
        validateEmail(customer.getEmail());

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new BusinessException("Error: Email is already in use!");
        }

        Customer user = new Customer(customer.getEmail(), encoder.encode(customer.getPassword()));

        return mapper.map(customerRepository.save(user), CustomerResponseDTO.class);
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO customer) {
        validateEmail(customer.getEmail());

        if (customer.getId() == null) {
            throw new BusinessException("Invalid id customer id null");
        }
        if (!Objects.equals(id, customer.getId())) {
            throw new BusinessException("Invalid ID customer id invalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BusinessException("Entity not found customer id not found");
        }

        Customer record = mapper.map(customer, Customer.class);
        return mapper.map(customerRepository.save(record), CustomerResponseDTO.class);

    }

    private void validateEmail(String email) {
        if (!EmailValidator.getInstance(false, true).isValid(email)) {
            throw new BusinessException("Invalid email " + email);
        }
    }


    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(e -> mapper.map(e, CustomerResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<CustomerResponseDTO> findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        return customer.map(value -> mapper.map(value, CustomerResponseDTO.class));
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }


}