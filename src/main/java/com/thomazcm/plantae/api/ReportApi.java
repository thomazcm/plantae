package com.thomazcm.plantae.api;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.model.Stats;
import com.thomazcm.plantae.repository.StatsRepository;

@RestController
@RequestMapping("/report")
public class ReportApi {
    
    @Autowired StatsRepository repository;
    
    @GetMapping
    public ResponseEntity<String> getReport() {
        Stats stats = repository.findAll().get(0);
        Long totalUsers = stats.getRequestIps().stream().distinct().count();
        List<LocalDateTime> datas = stats.getDatas();
        Map<String, List<String>> report = new HashMap<>();
        datas = datas.stream()
                .filter(data -> data.compareTo
                        (LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).minus(Duration.ofHours(24)))> 0)
                .collect(Collectors.toList());
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("mm");
        
        for (LocalDateTime data : datas) {
            int hour = data.getHour();
            String key = hour + ":00 as " + (hour + 1) + ":00";
            if (hour < 10) {
                key = "0" + key;
            }
            int horaAtual = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).getHour();
            if (hour <= horaAtual) {
                String time = timeFormatter.format(data);
                report.putIfAbsent(key, new ArrayList<>());
                report.get(key).add(time);
            }
        }
        StringBuilder reportBuilder = new StringBuilder("Acessos de IPs Diferentes: " + totalUsers + "<br/><br/>");
        
        
        List<String> hours = getHoursList(report);
        hours.forEach(hour -> {
           hour = "======== " + hour;
           reportBuilder.append(hour); 
        });
        
        return ResponseEntity.ok(reportBuilder.toString());
    }

    private List<String> getHoursList(Map<String, List<String>> report) {
        List<String> hours = new ArrayList<>();
        report.forEach((key, values) -> {
            StringBuilder hourBuilder = new StringBuilder();
            hourBuilder.append(key + " ========");
            hourBuilder.append("<br/>");
            values.forEach(value -> {
                hourBuilder.append(key.substring(0, 3)+value);
                hourBuilder.append("<br/>");
            });
            hours.add(hourBuilder.toString());
        });
        return hours.stream().sorted().collect(Collectors.toList());
    }

}
