package com.markaz.pillar.transaction.controller;

import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.repository.MarkazRepository;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.repository.SantriRepository;
import com.markaz.pillar.santri.repository.model.Santri;
import com.markaz.pillar.tools.file.impl.RollingFileStorage;
import com.markaz.pillar.tools.sequence.SequenceGenerator;
import com.markaz.pillar.transaction.controller.model.TransactionDTO;
import com.markaz.pillar.transaction.controller.model.TransactionRequestDTO;
import com.markaz.pillar.transaction.repository.UserTransactionRepository;
import com.markaz.pillar.transaction.repository.model.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;

@RestController
@RequestMapping("/transaction")
@PreAuthorize("isAuthenticated()")
public class UserTransactionController {
    private UserTransactionRepository repository;
    private MarkazRepository markazRepository;
    private SantriRepository santriRepository;
    private UserRepository userRepository;
    private SequenceGenerator sequenceGenerator;
    private RollingFileStorage fileStorage;

    @Autowired
    public void setSantriRepository(SantriRepository santriRepository) {
        this.santriRepository = santriRepository;
    }

    @Autowired
    public void setFileStorage(RollingFileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Autowired
    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setMarkazRepository(MarkazRepository markazRepository) {
        this.markazRepository = markazRepository;
    }

    @Autowired
    public void setRepository(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/markaz")
    public TransactionDTO createTransactionMarkaz(@RequestPart MultipartFile payment,
                                                  @RequestPart(name = "detail") @Valid TransactionRequestDTO requestDTO,
                                                  Principal principal) throws IOException {
        Markaz markaz = markazRepository.getById(requestDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz doesn't exist!"));
        if(markaz.getDonationDetails().isEmpty()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz doesn't have existing donation!");
        }

        return createTransaction(payment, requestDTO, principal, markaz.getDonationDetails().get(0));
    }

    @PostMapping("/santri")
    public TransactionDTO createTransaction(@RequestPart MultipartFile payment,
                                            @RequestPart(name = "detail") @Valid TransactionRequestDTO requestDTO,
                                            Principal principal) throws IOException {
        Santri santri = santriRepository.getById(requestDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Santri doesn't exist!"));
        if(santri.getDonationDetails().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Santri doesn't have existing donation!");
        }

        return createTransaction(payment, requestDTO, principal, santri.getDonationDetails().get(0));
    }

    private TransactionDTO createTransaction(@RequestPart MultipartFile payment,
                                             @RequestPart(name = "detail") @Valid TransactionRequestDTO requestDTO,
                                             Principal principal,
                                             DonationDetail donationDetail) throws IOException {
        AuthUser user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is Unauthorized!"));

        String paymentURL = fileStorage.saveFile(payment, Paths.get(String.valueOf(user.getId())));

        UserTransaction transaction = new UserTransaction();
        transaction.setTrxId(sequenceGenerator.nextId());
        transaction.setDonationDetail(donationDetail);
        transaction.setUser(user);
        transaction.setDonationURL(paymentURL);
        transaction.setAmount(requestDTO.getAmount());

        return TransactionDTO.mapFrom(repository.save(transaction));
    }

    @GetMapping
    public Page<TransactionDTO> fetchUserTransactions(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int n,
                                                      Principal principal) {
        AuthUser user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is Unauthorized!"));

        return repository.findAllByUser(user, PageRequest.of(page, n))
                .map(TransactionDTO::mapFrom);
    }
}
