package se.hillerstadhill.backend.controller;

import org.springframework.data.repository.CrudRepository;
import se.hillerstadhill.backend.model.Customer;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
    Customer findById(long id);
}
