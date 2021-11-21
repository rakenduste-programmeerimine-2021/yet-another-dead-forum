package ee.tlu.forum.service;

import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Donation;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.DonationInput;
import ee.tlu.forum.model.input.EverypayResponse;
import ee.tlu.forum.model.output.EverypayData;
import ee.tlu.forum.model.output.EverypayLink;
import ee.tlu.forum.repository.DonationRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Transactional
@Service
public class PaymentService implements PaymentServiceInterface {
    @Value("${everypay.url}")
    private String url;

    @Value("${everypay.username}")
    private String userName;

    @Value("${everypay.accountname}")
    private String accountName;

    @Value("${everypay.tokenAgreement}")
    private String tokenAgreement;

    @Value("${everypay.customerUrl}")
    private String customerUrl;

    @Value("${everypay.authKey}")
    private String authKey;

    @Value("${everypay.nonceKey}")
    private String nonceKey;

    RestTemplate restTemplate;
    DonationService donationService;

    public PaymentService(RestTemplate restTemplate, DonationService donationService) {
        this.restTemplate = restTemplate;
        this.donationService = donationService;
    }

    public EverypayLink makePayment(DonationInput donationInput) {

        log.info(donationInput.getUsername() + " AAA " + donationInput.getAmount());

        Donation donation = donationService.saveDonation(donationInput);

        log.info("Started everypay payment for {}. Amount: {}",
                donationInput.getUsername(),
                donationInput.getAmount());
        ZonedDateTime timestamp = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        Long orderId = donation.getId();

        EverypayData data = new EverypayData();
        data.setApi_username(userName);
        data.setAccount_name(accountName);
        data.setAmount(donation.getAmount());
        data.setOrder_reference(orderId.toString());
        data.setToken_agreement(tokenAgreement);
        try {
            data.setNonce(encode(userName+orderId+timestamp));
        } catch (Exception e) {
            log.error("Error while generating nonce {}",e.getMessage());
        }
        data.setTimestamp(timestamp);
        data.setCustomer_url(customerUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authKey);
        HttpEntity<EverypayData> requestData = new HttpEntity<>(data, httpHeaders);
        ResponseEntity<EverypayResponse> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, requestData, EverypayResponse.class);
            return new EverypayLink(response.getBody().getPayment_link());
        } catch (RestClientException e) {
            log.error("Error while connecting everypay - {}",e.getMessage());
            return null;
        }
    }

    public static String encode(String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        sha256_HMAC.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
        byte[] result = sha256_HMAC.doFinal("example".getBytes());
        return DatatypeConverter.printHexBinary(result);
    }
}
