package com.markaz.pillar.santri.controller;

import com.markaz.pillar.santri.controller.model.SantriDetailDTO;
import com.markaz.pillar.santri.controller.model.SantriSimpleDTO;
import com.markaz.pillar.santri.controller.search.SantriSpecs;
import com.markaz.pillar.santri.repository.SantriRepository;
import com.markaz.pillar.santri.repository.model.Santri;
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
@RequestMapping("/santri")
@PreAuthorize("permitAll()")
public class SantriController {
    private SantriRepository repository;

    @Autowired
    public void setRepository(SantriRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"id"})
    public SantriDetailDTO getDetailById(@RequestParam int id) {
        return SantriDetailDTO.mapFrom(
                repository.getById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Santri not Found!"))
        );
    }

    @GetMapping(params = {"slug"})
    public SantriDetailDTO getDetailBySlug(@RequestParam String slug) {
        return SantriDetailDTO.mapFrom(
                repository.getBySlug(slug)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Santri not Found!"))
        );
    }

    @GetMapping(value = "/search")
    public Page<SantriSimpleDTO> fetchSimpleAll(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String markaz,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int n) {
        Specification<Santri> searchSpec = Specification.where(SantriSpecs.nameLike(name))
                .and(SantriSpecs.markazLike(markaz));

        return repository.findAll(
                searchSpec,
                PageRequest.of(page, n)
        ).map(SantriSimpleDTO::mapFrom);
    }

    @GetMapping(value = "/search", params = {"sortedAge"})
    public Page<SantriSimpleDTO> fetchSimpleAllAge(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String markaz,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sortedAge,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int n) {
        Specification<Santri> searchSpec = Specification.where(SantriSpecs.nameLike(name))
                .and(SantriSpecs.markazLike(markaz));

        return repository.findAll(
                searchSpec,
                PageRequest.of(
                        page, n,
                        Sort.by(sortedAge, "birthDate")
                )
        ).map(SantriSimpleDTO::mapFrom);
    }

    @GetMapping(value = "/search", params = {"sortedName"})
    public Page<SantriSimpleDTO> fetchSimpleAllName(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String markaz,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction sortedName,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int n) {
        Specification<Santri> searchSpec = Specification.where(SantriSpecs.nameLike(name))
                .and(SantriSpecs.markazLike(markaz));

        return repository.findAll(
                searchSpec,
                PageRequest.of(
                        page, n,
                        Sort.by(sortedName, "name")
                )
        ).map(SantriSimpleDTO::mapFrom);
    }

    @GetMapping(value = "/search", params = {"sortedName", "sortedAge"})
    public Page<SantriSimpleDTO> fetchSimpleAllError() {
        throw new IllegalArgumentException("sortedName and sortedAge cannot be used together");
    }
}
