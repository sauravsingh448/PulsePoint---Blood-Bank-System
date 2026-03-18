package com.bloodbank.userService.roleSeeder;

import com.bloodbank.userService.entity.Role;
import com.bloodbank.userService.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleSeeder implements CommandLineRunner {


    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        createRoleIfNotExist("ADMIN");
        createRoleIfNotExist("DONOR");
        createRoleIfNotExist("RECIPIENT");

        System.out.println("Default roles inserted successfully!");
    }

    /**
     * Helper method to insert role safely
     */

    private void createRoleIfNotExist(String roleName) {
      Optional<Role> existingRole = roleRepository.findByName(roleName);

      if(existingRole.isEmpty()) {
          Role role = new Role();
          role.setName(roleName);
          roleRepository.save(role);
      }
    }
}
