package com.example.rentingservice;

import com.example.rentingservice.entities.ReservationObject;
import com.example.rentingservice.entities.User;
import com.example.rentingservice.repositories.ReservationObjectRepository;
import com.example.rentingservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RequiredArgsConstructor
public class RentingServiceApplication {

    private final UserRepository userRepository;
    private final ReservationObjectRepository objectRepository;

    public static void main(String[] args) {
        SpringApplication.run(RentingServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {
            User user1 = User.builder()
                    .userId(1)
                    .email("jan.kowalski@mail.com")
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .build();

            User user2 = User.builder()
                    .userId(2)
                    .email("marcin.zieliński@mail.com")
                    .firstName("Marcin")
                    .lastName("Zieliński")
                    .build();

            userRepository.saveAll(List.of(user1, user2));

            objectRepository.save(ReservationObject.builder()
                    .reservationObjectId(1)
                    .shortDescription("Barak")
                    .longDescription("Bardzo fajny barak")
                    .unitPrice(BigDecimal.valueOf(3000, 100))
                    .unit(TimeUnit.DAYS)
                    .owner(user1)
                    .build());

            objectRepository.save(ReservationObject.builder()
                    .reservationObjectId(2)
                    .shortDescription("Szopa")
                    .longDescription("Bardzo fajna szopa")
                    .unitPrice(BigDecimal.valueOf(10000, 100))
                    .unit(TimeUnit.DAYS)
                    .owner(user1)
                    .build());

            objectRepository.save(ReservationObject.builder()
                    .reservationObjectId(3)
                    .shortDescription("Pałac")
                    .longDescription("Bardzo fajny pałac")
                    .unitPrice(BigDecimal.valueOf(99999999, 100))
                    .unit(TimeUnit.DAYS)
                    .owner(user2)
                    .build());
        };
    }

}
