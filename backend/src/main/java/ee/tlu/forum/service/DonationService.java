package ee.tlu.forum.service;

import ee.tlu.forum.exception.NotFoundException;
import ee.tlu.forum.model.Donation;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.DonationInput;
import ee.tlu.forum.repository.DonationRepository;
import ee.tlu.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class DonationService implements DonationServiceInterface {


    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    public Donation saveDonation(DonationInput donationInput) {
        Optional<User> user = userRepository.findByUsername(donationInput.getUsername());
        if (user.isEmpty()) {
            throw new NotFoundException("User with username "
                    + donationInput.getUsername()
                    + " could not be found");
        }
        Donation donation = new Donation();
        donation.setAmount(donationInput.getAmount());
        donation.setUser(user.get());
        return donationRepository.save(donation);
    }
}
