package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {

}
