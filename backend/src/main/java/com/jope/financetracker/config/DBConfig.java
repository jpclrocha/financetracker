package com.jope.financetracker.config;

import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.CustomerRepository;
import com.jope.financetracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DBConfig implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DBConfig(RoleRepository roleRepository, CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if(roleRepository.findByName(Role.Values.BASIC.name()).isEmpty()){
            roleRepository.save(new Role(Role.Values.BASIC.name()));
        }

        if(roleRepository.findByName(Role.Values.ADMIN.name()).isEmpty()){
            roleRepository.save(new Role(Role.Values.ADMIN.name()));
        }

        boolean isAdminUserNotCreated = customerRepository.findByEmail("admin@gmail.com").isEmpty();
        boolean isBasicUserNotCreated = customerRepository.findByEmail("basic@gmail.com").isEmpty();

        if(isAdminUserNotCreated){
            customerRepository.save(new Customer(
                "JOPE ADMIN",
                Currency.BRL,
                "admin@gmail.com",
                passwordEncoder.encode("12345678")
            ));
        }

        if(isBasicUserNotCreated){
            customerRepository.save(new Customer(
                    "JOPE BASIC",
                    Currency.BRL,
                    "basic@gmail.com",
                    passwordEncoder.encode("12345678")
            ));
        }
    }
}
