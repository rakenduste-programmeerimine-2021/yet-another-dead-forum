package ee.tlu.forum.service;

import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.model.Maintenance;
import ee.tlu.forum.repository.MaintenanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MaintenanceService implements MaintenanceServiceInterface {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public Maintenance updateMaintenance(String message) {
        Maintenance maintenance = maintenanceRepository.findById(1L).get();
        if (maintenanceRepository.findById(1L).isEmpty()) {
            throw new BadRequestException("Catastrophic failure. This is not supposed to happen!");
        }
        if (message == null) {
            throw new BadRequestException("Cannot set maintenance message without 'maintenance' field.");
        }
        maintenance.setMessage(message);
        log.info("Setting maintenance message to: " + message);
        return maintenance;
    }

    public Maintenance getMaintenance() {
        Maintenance maintenance = maintenanceRepository.findById(1L).get();
        if (maintenanceRepository.findById(1L).isEmpty()) {
            throw new BadRequestException("Catastrophic failure. This is not supposed to happen!");
        }
        return maintenance;
    }
}
