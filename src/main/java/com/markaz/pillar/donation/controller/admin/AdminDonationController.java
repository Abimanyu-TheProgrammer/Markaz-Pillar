package com.markaz.pillar.donation.controller.admin;

import com.markaz.pillar.donation.controller.admin.model.AdminDonationDTO;
import com.markaz.pillar.donation.controller.admin.model.MarkazDonationRequestDTO;
import com.markaz.pillar.donation.controller.admin.model.SantriDonationRequestDTO;
import com.markaz.pillar.donation.controller.admin.search.DonationSpecs;
import com.markaz.pillar.donation.repository.DonationRepository;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.repository.MarkazRepository;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.repository.SantriRepository;
import com.markaz.pillar.santri.repository.model.Santri;
import com.markaz.pillar.tools.sequence.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/donation")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_DONATION') and hasAnyAuthority('CRUD_MARKAZ', 'CRUD_SANTRI')")
public class AdminDonationController {
    private DonationRepository repository;
    private MarkazRepository markazRepository;
    private SantriRepository santriRepository;
    private SequenceGenerator sequenceGenerator;

    @Autowired
    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Autowired
    public void setMarkazRepository(MarkazRepository markazRepository) {
        this.markazRepository = markazRepository;
    }

    @Autowired
    public void setSantriRepository(SantriRepository santriRepository) {
        this.santriRepository = santriRepository;
    }

    @Autowired
    public void setRepository(DonationRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"id"})
    public AdminDonationDTO getByUniqueID(@RequestParam String id) {
        return AdminDonationDTO.mapFrom(
                repository.getByUniqueId(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not Found!"))
        );
    }

    @GetMapping("/markaz")
    public Page<AdminDonationDTO> fetchAllMarkaz(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String s,
                                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortedStatus,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int n) {
        if(markazRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Markaz not Found!");
        }

        Specification<DonationDetail> searchSpec = Specification
                .where(DonationSpecs.markazIdEquals(id))
                .and(Specification.where(DonationSpecs.nameLike(s)).or(DonationSpecs.uniqueIdLike(s)));

        return repository.findAll(
                searchSpec,
                PageRequest.of(
                        page, n,
                        Sort.by(sortedStatus, "name").and(Sort.by("uniqueId"))
                )
        ).map(AdminDonationDTO::mapFrom);
    }

    @GetMapping("/santri")
    public Page<AdminDonationDTO> fetchAllSantri(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String s,
                                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortedStatus,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int n) {
        if(santriRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Santri not Found!");
        }

        Specification<DonationDetail> searchSpec = Specification
                .where(DonationSpecs.santriIdEquals(id))
                .and(Specification.where(DonationSpecs.nameLike(s)).or(DonationSpecs.uniqueIdLike(s)));

        return repository.findAll(
                searchSpec,
                PageRequest.of(
                        page, n,
                        Sort.by(sortedStatus, "name").and(Sort.by("uniqueId"))
                )
        ).map(AdminDonationDTO::mapFrom);
    }

    @PostMapping(value = "/markaz", params = {"id"})
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDonationDTO createMarkazDonation(@RequestParam int id,
                                                 @RequestBody @Valid MarkazDonationRequestDTO requestDTO) {
        Markaz markaz = markazRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Markaz not Found!"));

        if(markaz.getDonationDetail() != null) {
            throw new IllegalArgumentException("Markaz has existing shown donation!");
        }

        DonationDetail detail = new DonationDetail();
        detail.setMarkaz(markaz);
        detail.setUniqueId(sequenceGenerator.nextId());
        detail.setName(requestDTO.getName());
        detail.setDescription(requestDTO.getDescription());
        detail.setCategories(requestDTO.getCategories());
        detail.setNominal(requestDTO.getNominal());
        detail.setActive(requestDTO.getIsActive());

        markaz.setDonationDetail(detail);
        detail.setMarkaz(markaz);

        return AdminDonationDTO.mapFrom(repository.save(detail));
    }

    @PostMapping(value = "/santri", params = {"id"})
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDonationDTO createSantriDonation(@RequestParam int id,
                                                 @RequestBody @Valid SantriDonationRequestDTO requestDTO) {
        Santri santri = santriRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Santri not Found!"));

        if(santri.getDonationDetail() != null) {
            throw new IllegalArgumentException("Santri has existing shown donation!");
        }

        DonationDetail detail = new DonationDetail();
        detail.setSantri(santri);
        detail.setUniqueId(sequenceGenerator.nextId());
        detail.setName(requestDTO.getName());
        detail.setDescription(requestDTO.getDescription());
        detail.setNominal(requestDTO.getNominal());
        detail.setActive(requestDTO.getIsActive());

        santri.setDonationDetail(detail);
        detail.setSantri(santri);

        return AdminDonationDTO.mapFrom(repository.save(detail));
    }

    @PostMapping(value = "/markaz/edit", params = {"id"})
    public AdminDonationDTO updateMarkazDonation(@RequestParam String id,
                                                 @RequestBody @Valid MarkazDonationRequestDTO requestDTO) {
        DonationDetail detail = repository.getByUniqueId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not Found!"));

        detail.setName(requestDTO.getName());
        detail.setCategories(requestDTO.getCategories());
        detail.setDescription(requestDTO.getDescription());
        detail.setNominal(requestDTO.getNominal());
        detail.setActive(requestDTO.getIsActive());

        return AdminDonationDTO.mapFrom(repository.save(detail));
    }

    @PostMapping(value = "/santri/edit", params = {"id"})
    public AdminDonationDTO updateSantriDonation(@RequestParam String id,
                                                 @RequestBody @Valid SantriDonationRequestDTO requestDTO) {
        DonationDetail detail = repository.getByUniqueId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not Found!"));

        detail.setName(requestDTO.getName());
        detail.setDescription(requestDTO.getDescription());
        detail.setNominal(requestDTO.getNominal());
        detail.setActive(requestDTO.getIsActive());

        return AdminDonationDTO.mapFrom(repository.save(detail));
    }
}

