package ee.tlu.forum.controller;

import ee.tlu.forum.model.Maintenance;
import ee.tlu.forum.service.MaintenanceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MaintenanceController {

    MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("/maintenance")
    public ResponseEntity<Maintenance> getMaintenance() {
        return ResponseEntity.ok().body(maintenanceService.getMaintenance());
    }

    @PatchMapping("maintenance/edit")
    public ResponseEntity<Maintenance> editMaintenance(@RequestBody UpdateMaintenanceInput message) {
        return ResponseEntity.ok().body(maintenanceService.updateMaintenance(message.getMessage()));
    }


}

@Data
class UpdateMaintenanceInput {
    private String message;
}
