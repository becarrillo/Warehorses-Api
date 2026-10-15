package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;
import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.activity.service.ActivityService;
import com.web.warehorses_secure_persistence.domain.activity.util.mapper.ActivityMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/v1/activities")
public class ActivityV1Controller {
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    public ActivityV1Controller(ActivityService activityService, ActivityMapper activityMapper) {
        this.activityService = activityService;
        this.activityMapper = activityMapper;
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> listAllActivities() {
        final List<Activity> activities = activityService.listAllActivities();
        return ResponseEntity.ok(
                activities.stream()
                        .map(activityMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping(params = {"type", "page-number", "page-size"})
    public ResponseEntity<List<ActivityResponseDto>> listActivitiesByType(
            @RequestParam("type") String type,
            @RequestParam("page-number") int pageNumber,
            @RequestParam("page-size") int pageSize) {
        final List<ActivityResponseDto> activities = activityService.listByType(
                type,
                pageNumber,
                pageSize);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDto>
    getActivityById(@PathVariable("activityId") long id) {
        try {
            final Activity activity = activityService.findById(id);
            return ResponseEntity.ok(activityMapper.toDto(activity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/client", params = {"id", "page-number", "page-size"})
    public ResponseEntity<List<ActivityResponseDto>> listActivitiesByClientId(
            @RequestParam("id") int clientId,
            @RequestParam("page-number") int pageNumber,
            @RequestParam("page-size") int pageSize) {

        List<ActivityResponseDto> activityResponseDtoList;
        try {
            activityResponseDtoList = activityService.findByClientId(
                    clientId,
                    pageNumber,
                    pageSize
            )
                    .stream()
                    .map(activityMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(activityResponseDtoList);
    }

    @GetMapping(params = {"fecha-inicio", "fecha-fin"})
    public ResponseEntity<List<ActivityResponseDto>> listByCreatedAtBetween(
            @RequestParam("fecha-inicio") LocalDateTime start,
            @RequestParam("fecha-fin")LocalDateTime end) {

        return ResponseEntity.ok(
                activityService.listByCreatedAtBetween(start, end)
                        .stream()
                        .map(activityMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}
