package com.schwarz.controller;

import com.schwarz.domain.CustomerRequestDTO;
import com.schwarz.domain.CustomerResponseDTO;
import com.schwarz.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@Slf4j
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);

        CustomerResponseDTO result = service.create(customer);

        return ResponseEntity
                .created(new URI("/customers/" + result.getId()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        log.debug("REST request to get all Customers");

        return ResponseEntity
                .ok()
                .body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);

        Optional<CustomerResponseDTO> customer = service.findById(id);

        return customer.map(value -> ResponseEntity
                .ok()
                .body(value)).orElseGet(() -> ResponseEntity
                .notFound()
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable(value = "id", required = false) final Long id,
                                                              @RequestBody CustomerRequestDTO customer) {
        log.debug("REST request to update Customer : {}, {}", id, customer);

        CustomerResponseDTO result = service.update(id, customer);

        return ResponseEntity
                .ok()
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);

        service.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
