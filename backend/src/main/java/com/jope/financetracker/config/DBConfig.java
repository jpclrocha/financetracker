package com.jope.financetracker.config;

import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.enums.ExpenseType;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public DBConfig(RoleRepository roleRepository, CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if(!roleRepository.existsByName(Role.Values.BASIC.name())){
            roleRepository.save(new Role(Role.Values.BASIC.name()));
        }

        if(!roleRepository.existsByName(Role.Values.ADMIN.name())){
            roleRepository.save(new Role(Role.Values.ADMIN.name()));
        }

        boolean isAdminUserCreated = customerRepository.existsByEmail("admin@gmail.com");
        boolean isBasicUserCreated = customerRepository.existsByEmail("basic@gmail.com");

        if(!isAdminUserCreated){
            customerRepository.save(new Customer(
                "JOPE ADMIN",
                Currency.BRL,
                "admin@gmail.com",
                passwordEncoder.encode("12345678")
            ));
        }

        if(!isBasicUserCreated){
            customerRepository.save(new Customer(
                    "JOPE BASIC",
                    Currency.BRL,
                    "basic@gmail.com",
                    passwordEncoder.encode("12345678")
            ));
        }

        final String PUBLIC_CATEGORY_1 = "Groceries";
        final String PUBLIC_CATEGORY_2 = "Entertainment";
        final String PUBLIC_CATEGORY_3 = "Transportation";

        boolean isPublicCategoryCreated = categoryRepository.existsByNameAndIsPublic(PUBLIC_CATEGORY_1, true);
        boolean isPublicCategoryCreated2 = categoryRepository.existsByNameAndIsPublic(PUBLIC_CATEGORY_2, true);
        boolean isPublicCategoryCreated3 = categoryRepository.existsByNameAndIsPublic(PUBLIC_CATEGORY_3, true);

        if(!isPublicCategoryCreated){
            categoryRepository.save(new Category(
                null,
                true,
                PUBLIC_CATEGORY_1,
                ExpenseType.EXPENSE
            ));
        }

        if(!isPublicCategoryCreated2){
            categoryRepository.save(new Category(
                    null,
                    true,
                    PUBLIC_CATEGORY_2,
                    ExpenseType.EXPENSE
            ));
        }

        if(!isPublicCategoryCreated3){
            categoryRepository.save(new Category(
                    null,
                    true,
                    PUBLIC_CATEGORY_3,
                    ExpenseType.EXPENSE
            ));
        }

    }
}
