package com.markaz.pillar.markaz.admin.controller;

import com.github.slugify.Slugify;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import com.markaz.pillar.markaz.admin.controller.model.MarkazRequestDTO;
import com.markaz.pillar.markaz.controller.model.MarkazDetailDTO;
import com.markaz.pillar.markaz.repository.MarkazRepository;
import com.markaz.pillar.markaz.repository.model.Markaz;
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
@RequestMapping("/admin/markaz")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_MARKAZ')")
public class AdminMarkazController {
    private MarkazRepository repository;
    private StaticFileStorage fileStorage;

    @Autowired
    public void setFileStorage(StaticFileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Autowired
    public void setRepository(MarkazRepository repository) {
        this.repository = repository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseMessage("Markaz is created!")
    public MarkazDetailDTO create(@RequestPart MultipartFile thumbnail,
                                  @RequestPart @Valid MarkazRequestDTO markaz)
            throws IOException {
        if(repository.existsByName(markaz.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz with the same name exists");
        }

        String slug = new Slugify().slugify(markaz.getName());
        String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("markaz", "thumbnail"));

        Markaz entity = new Markaz();
        entity.setName(markaz.getName());
        entity.setSlug(slug);
        entity.setBackground(markaz.getBackground());
        entity.setThumbnailURL(thumbnailURL);
        entity.setCategory(markaz.getCategory());
        entity.setAddress(markaz.getAddress());
        entity.setContactName(markaz.getContactName());
        entity.setContactInfo(markaz.getContactInfo());

        return MarkazDetailDTO.mapFrom(repository.save(entity));
    }

    @PostMapping(
            value = "/edit",
            params = {"id"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseMessage("Markaz is updated!")
    public MarkazDetailDTO updateById(@RequestParam int id,
                                      @RequestPart(required = false) MultipartFile thumbnail,
                                      @RequestPart @Valid MarkazRequestDTO markaz) throws IOException {
        Markaz entity = repository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz not found"));

        if(!entity.getName().equals(markaz.getName()) && repository.existsByName(markaz.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz with the same name exists");
        }

        entity.setName(markaz.getName());
        entity.setBackground(markaz.getBackground());
        entity.setCategory(markaz.getCategory());
        entity.setAddress(markaz.getAddress());
        entity.setSlug(new Slugify().slugify(markaz.getName()));
        entity.setContactName(markaz.getContactName());
        entity.setContactInfo(markaz.getContactInfo());

        if(thumbnail != null) {
            String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("markaz", "thumbnail"));
            entity.setThumbnailURL(thumbnailURL);
        }

        return MarkazDetailDTO.mapFrom(repository.save(entity));
    }

    @DeleteMapping(params = {"id"})
    @ResponseMessage("Entity is deleted")
    public void softDelete(@RequestParam int id) {
        repository.deleteById(id);
    }
}
