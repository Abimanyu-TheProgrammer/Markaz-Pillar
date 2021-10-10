package com.markaz.pillar.markaz.controller;

import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import com.markaz.pillar.markaz.controller.model.MarkazDetailDTO;
import com.markaz.pillar.markaz.controller.model.MarkazSimpleDTO;
import com.markaz.pillar.markaz.controller.search.MarkazSpecs;
import com.markaz.pillar.markaz.repository.MarkazRepository;
import com.markaz.pillar.markaz.repository.model.Markaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/markaz")
@PreAuthorize("permitAll()")
public class MarkazController {
    private MarkazRepository repository;

    @Autowired
    public void setRepository(MarkazRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"id"})
    public MarkazDetailDTO getDetailById(@RequestParam int id) {
        return MarkazDetailDTO.mapFrom(
                repository.getById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Markaz not Found!"))
        );
    }

    @GetMapping(params = {"slug"})
    public MarkazDetailDTO getDetailBySlug(@RequestParam String slug) {
        return MarkazDetailDTO.mapFrom(
                repository.getBySlug(slug)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Markaz not Found!"))
        );
    }

    @GetMapping("/search")
    public Page<MarkazSimpleDTO> fetchSimpleAll(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) Boolean address,
                                                @RequestParam(required = false) MarkazDonationCategory category,
                                                @RequestParam(defaultValue = "ASC") Sort.Direction sortedName,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int n) {
        Specification<Markaz> searchSpec = Specification.where(MarkazSpecs.nameLike(name))
                .and(MarkazSpecs.addressLike(address))
                .and(MarkazSpecs.categoryLike(category));

        return repository.findAll(
                        searchSpec,
                        PageRequest.of(page, n, sortedName, "name")
                ).map(MarkazSimpleDTO::mapFrom);
    }
}
