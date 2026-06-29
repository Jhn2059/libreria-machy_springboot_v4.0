package com.machy.config;

import com.machy.entity.Category;
import com.machy.entity.Supplier;
import com.machy.entity.User;
import com.machy.repository.CategoryRepository;
import com.machy.repository.SupplierRepository;
import com.machy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, CategoryRepository categoryRepository,
                          SupplierRepository supplierRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded, skipping");
            return;
        }
        log.info("Seeding initial data...");

        seedUsers();
        seedCategories();
        seedSuppliers();

        log.info("Database seeding completed");
    }

    private void seedUsers() {
        var admin = new User();
        admin.setNombre("Jhon");
        admin.setApellidos("Taipe");
        admin.setDni("00000000");
        admin.setCorreo("admin@machy.com");
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));

        // ...

        var vendedor = new User();
        vendedor.setNombre("Ana");
        vendedor.setApellidos("Flores");

        // ...

        var vendedor2 = new User();
        vendedor2.setNombre("Miguel");
        vendedor2.setApellidos("Torres");
        vendedor2.setDni("22222222");
        vendedor2.setCorreo("miguel@machy.com");
        vendedor2.setUsername("miguel");
        vendedor2.setPasswordHash(passwordEncoder.encode("vendedor123"));
        vendedor2.setRol("vendedor");
        vendedor2.setTurno("tarde");
        vendedor2.setActivo(true);
        vendedor2.setIntentosFallidos(0);
        userRepository.save(vendedor2);
        log.info("Vendedor user created: miguel / vendedor123");
    }

    private void seedCategories() {
        String[][] cats = {
            {"Útiles escolares", "Cuadernos, lapiceros, lápices, colores y más"},
            {"Papelería", "Hojas, sobres, folders, resmas y artículos de oficina"},
            {"Libros", "Textos escolares, novelas y material de lectura"},
            {"Manualidades", "Tijeras, goma, escarcha, cartulinas y más"},
            {"Juguetes", "Juguetes educativos y recreativos"},
            {"Otros", "Productos varios"}
        };
        for (String[] c : cats) {
            var cat = new Category();
            cat.setNombre(c[0]);
            cat.setDescripcion(c[1]);
            cat.setActivo(true);
            categoryRepository.save(cat);
        }
        log.info("{} categories created", cats.length);
    }

    private void seedSuppliers() {
        String[][] provs = {
            {"Distribuidora ABC", "20123456789", "Carlos López", "999888777", "abc@proveedores.com", "Av. Principal 123"},
            {"Papeles del Perú", "20987654321", "María García", "987654321", "papeles@proveedores.com", "Jr. Comercio 456"},
            {"Libros Mundo SAC", "20456789123", "Pedro Sánchez", "976543210", "libros@mundo.com", "Calle Real 789"}
        };
        for (String[] p : provs) {
            var prov = new Supplier();
            prov.setNombre(p[0]);
            prov.setRuc(p[1]);
            prov.setContacto(p[2]);
            prov.setTelefono(p[3]);
            prov.setEmail(p[4]);
            prov.setDireccion(p[5]);
            prov.setActivo(true);
            supplierRepository.save(prov);
        }
        log.info("{} suppliers created", provs.length);
    }
}
