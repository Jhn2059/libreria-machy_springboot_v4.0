package com.machy.service;

import com.machy.entity.Config;
import com.machy.repository.ConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigService {

    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Map<String, String> findAllAsMap() {
        return configRepository.findAll().stream()
                .collect(Collectors.toMap(Config::getClave, Config::getValor));
    }

    public void save(List<Config> configs) {
        configRepository.saveAll(configs);
    }

    public void saveFromMap(Map<String, String> configMap) {
        configMap.forEach((clave, valor) -> {
            var opt = configRepository.findByClave(clave);
            if (opt.isPresent()) {
                Config c = opt.get();
                c.setValor(valor);
                configRepository.save(c);
            } else {
                configRepository.save(Config.builder()
                        .clave(clave).valor(valor).tipo("text").build());
            }
        });
    }
}
