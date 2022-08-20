package com.example.rentingservice.controllers

import com.example.rentingservice.WebMvcSpecIT
import com.example.rentingservice.config.ApplicationConstants
import com.example.rentingservice.entities.Reservation
import com.example.rentingservice.repositories.ReservationObjectRepository
import com.example.rentingservice.repositories.ReservationRepository
import com.example.rentingservice.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Transactional
class ReservationObjectControllerSpecIT extends WebMvcSpecIT {

    @Autowired
    UserRepository userRepository

    @Autowired
    ReservationRepository reservationRepository

    @Autowired
    ReservationObjectRepository objectRepository

    def "It should properly return reservation list for user"() {
        given:
        def objectId = 3
        def userId = 1
        def user = userRepository.findById(userId).get()
        def object = objectRepository.findById(objectId).get()

        def reservation = Reservation.builder()
                .start(Instant.parse("2022-08-20T10:15:30.00Z"))
                .end(Instant.parse("2022-08-27T10:15:30.00Z"))
                .cost(BigDecimal.valueOf(2111, 2))
                .object(object)
                .lessee(user)
                .build()
        user.reservations << reservation
        reservationRepository.save(reservation)

        when:
        def result = mvc.perform(get(ApplicationConstants.API_PREFIX + "/object/$objectId/reservations")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath("\$.[0].start").value(reservation.start.toString()))
                .andExpect(jsonPath("\$.[0].end").value(reservation.end.toString()))
                .andExpect(jsonPath("\$.[0].cost").value("21.11"))
                .andExpect(jsonPath("\$.[0].object.objectId").value(object.reservationObjectId))
                .andExpect(jsonPath("\$.[0].object.shortDescription").value(object.shortDescription))
                .andExpect(jsonPath("\$.[0].user.userId").value(userId))
                .andExpect(jsonPath("\$.[0].user.firstName").value(user.firstName))
                .andExpect(jsonPath("\$.[0].user.lastName").value(user.lastName))
    }

    def "It should properly return not found for object that does not exist"() {
        given:
        def objectId = 100
        when:
        def result = mvc.perform(get(ApplicationConstants.API_PREFIX + "/object/$objectId/reservations")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("\$.message").value("Object with id: 100 not found"))
    }

}
