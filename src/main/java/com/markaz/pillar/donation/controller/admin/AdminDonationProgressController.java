package com.markaz.pillar.donation.controller.admin;

import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import com.markaz.pillar.donation.controller.admin.model.DonationProgressDTO;
import com.markaz.pillar.donation.controller.admin.model.DonationProgressRequestDTO;
import com.markaz.pillar.donation.repository.DonationProgressRepository;
import com.markaz.pillar.donation.repository.DonationRepository;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.donation.repository.model.DonationProgress;
import com.markaz.pillar.tools.file.impl.StaticImageStorage;
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
@RequestMapping("/admin/donation/progress")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_DONATION')")
public class AdminDonationProgressController {
    private DonationRepository repository;
    private DonationProgressRepository progressRepository;
    private StaticImageStorage fileStorage;

    @Autowired
    public void setFileStorage(StaticImageStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Autowired
    public void setRepository(DonationRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setProgressRepository(DonationProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @PostMapping(params = {"donation_id"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DonationProgressDTO createProgress(@RequestParam(name = "donation_id") String id,
                                              @RequestPart MultipartFile thumbnail,
                                              @RequestPart @Valid DonationProgressRequestDTO detail)
            throws IOException {
        DonationDetail donationDetail = repository.getByUniqueId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Donation doesn't exist"));

        String thumbnailURL = fileStorage.saveFile(
                thumbnail, Paths.get("volunteer", "testimony", "thumbnail")
        );

        DonationProgress entity = new DonationProgress();
        entity.setProgressDate(detail.getProgressDate());
        entity.setThumbnailURL(thumbnailURL);
        entity.setDescription(detail.getDescription());

        donationDetail.getProgresses().add(entity);
        entity.setDonation(donationDetail);

        return DonationProgressDTO.mapFrom(progressRepository.save(entity));
    }

    @PostMapping(
            value = "/edit",
            params = {"id"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseMessage("Progress is updated!")
    public DonationProgressDTO updateById(@RequestParam int id,
                                          @RequestPart(required = false) MultipartFile thumbnail,
                                          @RequestPart @Valid DonationProgressRequestDTO detail) throws IOException {
        DonationProgress entity = progressRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Progress not found!"));

        entity.setProgressDate(detail.getProgressDate());
        entity.setDescription(detail.getDescription());

        if(thumbnail != null) {
            String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("markaz", "thumbnail"));
            entity.setThumbnailURL(thumbnailURL);
        }

        return DonationProgressDTO.mapFrom(progressRepository.save(entity));
    }

    @DeleteMapping(params = {"id"})
    @ResponseMessage("Entity is deleted")
    public void deleteProgressById(@RequestParam int id) {
        progressRepository.deleteById(id);
    }
}
