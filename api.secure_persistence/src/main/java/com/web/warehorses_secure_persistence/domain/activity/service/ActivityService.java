package com.web.warehorses_secure_persistence.domain.activity.service;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityRequestDto;
import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;
import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.activity.repository.ActivityRepository;
import com.web.warehorses_secure_persistence.domain.activity.util.mapper.ActivityMapper;
import com.web.warehorses_secure_persistence.exception.ActivityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityService(ActivityRepository activityRepository,
                           ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    public Activity create(ActivityRequestDto dto) {
        return activityRepository.save(activityMapper.toEntity(dto));
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() ->
                        new ActivityNotFoundException("Activity not found in the query"));
    }

    public List<ActivityResponseDto> listByType(String type, int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return activityRepository.findByType(
                Activity.TxnType.valueOf(type),
                pageable)
                        .stream()
                        .map(activityMapper::toDto)
                        .toList();
    }

public List<Activity> findByClientId(int clientId, int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return activityRepository.findByClientClientId(
                        clientId,
                        pageable)
                .stream()
                .toList();
    }

    public List<Activity> listAllActivities() {
        return activityRepository.findAll();
    }

    public List<Activity> listByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return activityRepository.findByCreatedAtBetween(start, end);
    }
    public Activity update(long id, Activity activity) {
        Activity existing = activityRepository.findById(id)
                .orElseThrow(ActivityNotFoundException::new);
        existing.setType(activity.getType());
        existing.setItems(activity.getItems());
        existing.setClient(activity.getClient());
        existing.setEvidenceUrl(activity.getEvidenceUrl());
        existing.setEvidenceType(activity.getEvidenceType());
        return activityRepository.saveAndFlush(existing);
    }

    public void delete(long id) {
        final Optional<Activity> activityOptional = Optional.of(
                activityRepository.findById(id))
                        .orElseThrow(ActivityNotFoundException::new);
        activityOptional.ifPresent(activityRepository::delete);
    }
}