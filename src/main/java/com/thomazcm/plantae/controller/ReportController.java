package com.thomazcm.plantae.controller;

import java.time.Duration;
import java.time.LocalDateTime;
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
public class ReportController {
    
    @Autowired StatsRepository repository;
    
    @GetMapping
    public ResponseEntity<String> getReport() {
        Stats stats = repository.findAll().get(0);
        Long totalUsers = stats.getRequestIps().stream().distinct().count();
        List<LocalDateTime> datas = stats.getDatas();
        Map<String, List<String>> report = new HashMap<>();
        datas = datas.stream()
                .filter(data -> data.compareTo(LocalDateTime.now().minus(Duration.ofHours(27)))> 0)
                .collect(Collectors.toList());
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("mm:ss");
        
        for (LocalDateTime data : datas) {
            int hour = data.getHour();
            String key = hour + ":00 as " + (hour + 1) + ":00";
            String time = timeFormatter.format(data);
            report.putIfAbsent(key, new ArrayList<>());
            report.get(key).add(time);
        }
        StringBuilder builder = new StringBuilder("Acessos de IPs Diferentes: " + totalUsers + "<br/>");
        
        report.forEach((key, values) -> {
            builder.append("======== " +key + " ==========");
            builder.append("<br/>");
            values.forEach(value -> {
                builder.append(value);
                builder.append("<br/>");
            });
            
        });
        return ResponseEntity.ok(builder.toString());
    }

}
