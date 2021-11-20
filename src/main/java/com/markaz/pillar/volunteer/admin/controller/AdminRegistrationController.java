package com.markaz.pillar.volunteer.admin.controller;

import com.markaz.pillar.volunteer.admin.controller.model.EditRegistrationStatusRequestDTO;
import com.markaz.pillar.volunteer.admin.controller.model.RegistrationDTO;
import com.markaz.pillar.volunteer.admin.controller.search.RegistrationSpecs;
import com.markaz.pillar.volunteer.repository.VolunteerRepository;
import com.markaz.pillar.volunteer.repository.model.RegistrationStatus;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/volunteer/registration")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_VOLUNTEER')")
public class AdminRegistrationController {
    private VolunteerRepository repository;

    @Autowired
    public void setRepository(VolunteerRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"id"})
    public RegistrationDTO getByID(@RequestParam int id) {
        return RegistrationDTO.mapFrom(
                repository.getById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration not found!"))
        );
    }

    @GetMapping
    public Page<RegistrationDTO> fetchAllRegistration(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int n,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) RegistrationStatus status) {
        Specification<VolunteerRegistration> specification = RegistrationSpecs.nameLike(name)
                .and(RegistrationSpecs.statusEqual(status));

        return repository.findAll(specification, PageRequest.of(page, n))
                .map(RegistrationDTO::mapFrom);
    }

    @PostMapping(value = "/status", params = {"id"})
    public RegistrationDTO editVolunteerStatus(@RequestParam(name = "id") int id,
                                               @RequestBody @Valid EditRegistrationStatusRequestDTO requestDTO) {
        VolunteerRegistration registration = repository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not found!"));

        registration.setStatus(requestDTO.getStatus());
        if(requestDTO.getReason() != null
                && !requestDTO.getReason().isEmpty()
                && RegistrationStatus.PENDAFTARAN_DITOLAK.equals(requestDTO.getStatus())) {
            registration.setReason(requestDTO.getReason());
        }

        return RegistrationDTO.mapFrom(repository.save(registration));
    }
}
