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
        boolean firstRun = userRepository.count() == 0;
        if (firstRun) {
            log.info("Seeding initial data...");
            seedCategories();
            seedSuppliers();
        }
        seedUsers();
        log.info("Database seeding completed");
    }

    private void seedUsers() {
        saveOrUpdateUser("admin", "Jhon", "Taipe", "00000000", "admin@machy.com", "admin123", "admin", "completo");
        saveOrUpdateUser("ana", "Ana", "Flores", "11111111", "ana@machy.com", "vendedor123", "vendedor", "completo");
        saveOrUpdateUser("miguel", "Miguel", "Torres", "22222222", "miguel@machy.com", "vendedor123", "vendedor", "tarde");
    }

    private void saveOrUpdateUser(String username, String nombre, String apellidos,
                                   String dni, String correo, String password,
                                   String rol, String turno) {
        var opt = userRepository.findByUsername(username);
        if (opt.isPresent()) {
            var user = opt.get();
            user.setNombre(nombre);
            user.setApellidos(apellidos);
            user.setDni(dni);
            user.setCorreo(correo);
            user.setRol(rol);
            user.setTurno(turno);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setActivo(true);
            userRepository.save(user);
            log.info("User updated: {} / {} {}", username, nombre, apellidos);
        } else {
            var user = new User();
            user.setUsername(username);
            user.setNombre(nombre);
            user.setApellidos(apellidos);
            user.setDni(dni);
            user.setCorreo(correo);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setRol(rol);
            user.setTurno(turno);
            user.setActivo(true);
            user.setIntentosFallidos(0);
            userRepository.save(user);
            log.info("User created: {} / {} {}", username, nombre, apellidos);
        }
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
