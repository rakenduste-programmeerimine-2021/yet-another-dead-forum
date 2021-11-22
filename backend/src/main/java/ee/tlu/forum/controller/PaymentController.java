package ee.tlu.forum.controller;

import ee.tlu.forum.model.Donation;
import ee.tlu.forum.model.input.DonationInput;
import ee.tlu.forum.model.output.EverypayLink;
import ee.tlu.forum.service.DonationService;
import ee.tlu.forum.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class PaymentController {

    PaymentService paymentService;
    DonationService donationService;

    public PaymentController(PaymentService paymentService, DonationService donationService) {
        this.paymentService = paymentService;
        this.donationService = donationService;
    }

    @PostMapping("/payment")
    public ResponseEntity<EverypayLink> makePayment(@RequestBody DonationInput donation) {
//        Donation savedDonation = donationService.saveDonation(donation);
        EverypayLink everypayLink = paymentService.makePayment(donation);
        if (everypayLink == null) {
            log.info("Sending empty result to frontend");
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(everypayLink);
    }
}