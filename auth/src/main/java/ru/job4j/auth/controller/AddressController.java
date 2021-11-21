package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.model.Address;
import ru.job4j.auth.model.User;
import ru.job4j.auth.model.UserDTO;
import ru.job4j.auth.service.AddressService;
import ru.job4j.auth.service.UserService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/address")
public class AddressController {
    private AddressService addressService;
    private UserService userService;
    private final Map<Integer, Address> addresses = new HashMap<>(Map.ofEntries(
            Map.entry(1, new Address(
                    1, "Russia", "Moscow", "Gogolya", "5a"
            )),
            Map.entry(2, new Address(
                    2, "Russia", "St. Petersburg", "Dostoevskogo", "10"
            )),
            Map.entry(3, new Address(
                    3, "Russia", "Ufa", "Aksakova", "93"
            ))
    ));

    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @PostMapping("/")
    public User example1(@RequestBody UserDTO userDTO) {
        var address = addressService.findById(userDTO.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user = new User(userDTO.getName(), userDTO.getSurname());
        user.setAddress(address);
        return userService.save(user);
    }

    @PatchMapping("/example2")
    public Address example2(@RequestBody Address address)
            throws InvocationTargetException, IllegalAccessException {
        var current = addresses.get(address.getId());
        if (current == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var methods = current.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Invalid properties mapping");
                }
                var newValue = getMethod.invoke(address);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        addresses.put(address.getId(), address);
        return current;
    }

    @PostMapping("/example3")
    public Map<String, String> saveAddress(@RequestBody Map<String, String> body) {
        var address = new Address(
                0, body.get("country"), body.get("city"), body.get("street"), body.get("house")
        );
        address.setId(addresses.size() + 1);
        addresses.put(address.getId(), address);
        return Map.of(
                "id", String.valueOf(address.getId()),
                "country", address.getCountry(),
                "city", address.getCity(),
                "street", address.getStreet(),
                "house", address.getHouse()
        );
    }
}

