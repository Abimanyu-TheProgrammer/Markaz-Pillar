package com.markaz.pillar.santri.admin.controller;

import com.github.slugify.Slugify;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import com.markaz.pillar.donation.admin.controller.model.SantriDonationRequestDTO;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.repository.MarkazRepository;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.admin.controller.markaz.SantriRequestDTO;
import com.markaz.pillar.santri.controller.model.SantriDetailDTO;
import com.markaz.pillar.santri.repository.SantriRepository;
import com.markaz.pillar.santri.repository.model.Santri;
import com.markaz.pillar.tools.file.impl.StaticFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/admin/santri")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_SANTRI') and hasAuthority('CRUD_DONATION')")
public class AdminSantriController {
    private SantriRepository repository;
    private MarkazRepository markazRepository;
    private StaticFileStorage fileStorage;

    @Autowired
    public void setMarkazRepository(MarkazRepository markazRepository) {
        this.markazRepository = markazRepository;
    }

    @Autowired
    public void setRepository(SantriRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFileStorage(StaticFileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @PostMapping(params = {"markaz_id"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseMessage("Santri is created!")
    public SantriDetailDTO create(@RequestParam(name = "markaz_id") int markazId,
                                  @RequestPart MultipartFile thumbnail,
                                  @RequestPart @Valid SantriRequestDTO santri,
                                  @RequestPart(required = false) @Valid SantriDonationRequestDTO donation)
            throws IOException {
        Markaz markaz = markazRepository.getById(markazId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz not found"));
        if(repository.existsByNameAndMarkaz_Id(santri.getName(), markazId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Santri with the same name exists");
        }

        String slug = new Slugify().slugify(santri.getName());
        String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("santri", "thumbnail"));

        Santri entity = new Santri();
        entity.setName(santri.getName());
        entity.setSlug(slug);
        entity.setBackground(santri.getBackground());
        entity.setThumbnailURL(thumbnailURL);
        entity.setAddress(santri.getAddress());
        entity.setGender(santri.getGender());
        entity.setBirthPlace(santri.getBirthPlace());
        entity.setBirthDate(santri.getBirthDate());

        markaz.getSantri().add(entity);
        entity.setMarkaz(markaz);

        if(donation != null) {
            DonationDetail donationDetail = new DonationDetail();
            donationDetail.setSantri(entity);
            donationDetail.setDescription(donation.getDescription());
            donationDetail.setNominal(donation.getNominal());

            entity.setDonationDetail(donationDetail);
        }

        return SantriDetailDTO.mapFrom(repository.save(entity));
    }

    @PutMapping(params = {"id"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseMessage("Santri is updated!")
    public SantriDetailDTO updateById(@RequestParam int id,
                                      @RequestPart(required = false) MultipartFile thumbnail,
                                      @RequestPart @Valid SantriRequestDTO santri) throws IOException {
        Santri entity = repository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Santri not found"));
        if(repository.existsByNameAndMarkaz_Id(santri.getName(), entity.getMarkaz().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Santri with the same name exists");
        }

        String slug = new Slugify().slugify(santri.getName());

        entity.setName(santri.getName());
        entity.setSlug(slug);
        entity.setBackground(santri.getBackground());
        entity.setAddress(santri.getAddress());
        entity.setGender(santri.getGender());
        entity.setBirthPlace(santri.getBirthPlace());
        entity.setBirthDate(santri.getBirthDate());

        if(thumbnail != null) {
            String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("santri", "thumbnail"));
            entity.setThumbnailURL(thumbnailURL);
        }

        return SantriDetailDTO.mapFrom(repository.save(entity));
    }

    @DeleteMapping(params = {"id"})
    @ResponseMessage("Entity is deleted")
    public void softDelete(@RequestParam int id) {
        repository.deleteById(id);
    }
}
