package com.jope.financetracker.controller;

import com.jope.financetracker.dto.dashboard.SummaryRequestDTO;
import com.jope.financetracker.dto.dashboard.SummaryResponseDTO;
import com.jope.financetracker.service.DashboardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @PostMapping("/summary")
    public ResponseEntity<SummaryResponseDTO> getUserSummary(@RequestBody @Valid SummaryRequestDTO obj){
        return  ResponseEntity.ok(service.getSummary(obj));
    }
}
