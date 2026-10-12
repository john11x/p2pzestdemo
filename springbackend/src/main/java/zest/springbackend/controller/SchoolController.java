package zest.springbackend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zest.springbackend.dto.ApiResponse;
import zest.springbackend.model.School;
import zest.springbackend.model.enums.SchoolType;
import zest.springbackend.repository.SchoolRepository;

@RestController
@RequestMapping("/api")
public class SchoolController {

    private final SchoolRepository schoolRepository;

    public SchoolController(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @GetMapping("/schools")
    public ApiResponse<List<School>> getSchools(@RequestParam(required = false) SchoolType type) {
        List<School> schools = type == null ? schoolRepository.findAll() : schoolRepository.findByType(type);
        return ApiResponse.ok("Schools loaded", schools);
    }
}
