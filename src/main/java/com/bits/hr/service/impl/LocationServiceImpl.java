package com.bits.hr.service.impl;

import com.bits.hr.domain.Location;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.LocationRepository;
import com.bits.hr.service.LocationService;
import com.bits.hr.service.dto.LocationDTO;
import com.bits.hr.service.mapper.LocationMapper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private static final String ENTITY = "Location";

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    @Transactional
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);

        if (locationDTO.getParentId() != null) {
            Optional<Location> parentOptional = locationRepository.findById(locationDTO.getParentId());
            if (parentOptional.isPresent()) {
                Location parent = parentOptional.get();
                parent.isLastChild(false);
                location.setHasParent(true);
                locationRepository.save(parent);
            } else {
                throw new BadRequestAlertException("Parent location not found.", ENTITY, "");
            }
        }

        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository
            .findAll(pageable)
            .map(location -> {
                LocationDTO locationDTO = locationMapper.toDto(location);
                locationDTO.setFullLocation(fullLocationPath(location));
                return locationDTO;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }

    @Override
    public List<LocationDTO> getAllNonSuccessor(Long id) {
        Location source = locationRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("Location not found", ENTITY, ""));

        Map<Long, LocationDTO> locations = locationRepository
            .findAll()
            .stream()
            .map(locationMapper::toDto)
            .collect(Collectors.toMap(LocationDTO::getId, Function.identity()));

        Map<Long, ArrayList<Long>> adjList = new HashMap<>();

        for (LocationDTO location : locations.values()) {
            Long parent = location.getParentId();
            if (!adjList.containsKey(parent)) {
                adjList.put(parent, new ArrayList<>());
            }

            adjList.get(parent).add(location.getId());
        }

        Queue<Long> queue = new LinkedList<>();
        queue.add(source.getId());

        while (!queue.isEmpty()) {
            Long current = queue.poll();

            locations.remove(current);

            if (adjList.containsKey(current)) {
                for (Long next : adjList.get(current)) {
                    queue.add(next);
                }
            }
        }

        return new ArrayList<>(locations.values());
    }

    private String fullLocationPath(Location location) {
        StringBuilder path = new StringBuilder();

        path.append(location.getLocationCode());

        location = location.getParent();

        while (location != null) {
            path.insert(0, "-");
            path.insert(0, location.getLocationCode());
            location = location.getParent();
        }

        return path.toString();
    }
}
