package com.markaz.pillar.volunteer.controller;

import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramDTO;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramSimpleDTO;
import com.markaz.pillar.volunteer.controller.search.ProgramSpecs;
import com.markaz.pillar.volunteer.repository.ProgramRepository;
import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/volunteer")
@PreAuthorize("permitAll()")
public class VolunteerController {
    private ProgramRepository repository;

    @Autowired
    public void setRepository(ProgramRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"id"})
    public VolunteerProgramDTO getDetailByID(@RequestParam Integer id) {
        return VolunteerProgramDTO.mapFrom(
                repository.getById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program doesn't exist"))
        );
    }

    @GetMapping(params = {"slug"})
    public VolunteerProgramDTO getDetailBySlug(@RequestParam String slug) {
        return VolunteerProgramDTO.mapFrom(
                repository.getBySlug(slug)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program doesn't exist"))
        );
    }

    @GetMapping
    public Page<VolunteerProgramSimpleDTO> fetchPrograms(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int n,
                                                         @RequestParam(required = false) String name) {
        Specification<VolunteerProgram> specification = ProgramSpecs.nameLike(name);

        return repository.findAll(specification, PageRequest.of(page, n))
                .map(VolunteerProgramSimpleDTO::mapFrom);
    }
}
