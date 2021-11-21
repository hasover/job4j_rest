package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Address;
import ru.job4j.auth.repository.AddressRepository;

import java.util.Optional;

@Service
public class AddressService {
    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Optional<Address> findById(int id) {
        return addressRepository.findById(id);
    }
}
