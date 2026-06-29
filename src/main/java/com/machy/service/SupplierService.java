package com.machy.service;

import com.machy.entity.Supplier;
import com.machy.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> findActive() {
        return supplierRepository.findByActivoTrueOrderByNombre();
    }

    public Supplier create(String nombre, String ruc, String contacto, String telefono) {
        return supplierRepository.save(Supplier.builder()
                .nombre(nombre)
                .ruc(ruc != null ? ruc : "")
                .contacto(contacto != null ? contacto : "")
                .telefono(telefono != null ? telefono : "")
                .activo(true)
                .build());
    }

    public void deactivate(UUID id) {
        Supplier sup = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        sup.setActivo(false);
        supplierRepository.save(sup);
    }
}
