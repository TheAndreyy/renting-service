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
import org.springframework.util.ResourceUtils

import java.nio.file.Files
import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Transactional
class ReservationObjectControllerSpecIT extends WebMvcSpecIT {

    private static String CREATE_RESERVATION_REQUEST_FILE = "classpath:controllers/create_reservation_request.json"
    private static String UPDATE_RESERVATION_REQUEST_FILE = "classpath:controllers/update_reservation_request.json"

    @Autowired
    UserRepository userRepository

    @Autowired
    ReservationRepository reservationRepository

    @Autowired
    ReservationObjectRepository objectRepository

    void cleanup() {
        reservationRepository.deleteAll()
    }

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

    def "It should properly create reservation"() {
        given:
        def request = Files.readString(ResourceUtils.getFile(CREATE_RESERVATION_REQUEST_FILE).toPath())
        def objectId = 3

        when:
        def result = mvc.perform(post(ApplicationConstants.API_PREFIX + "/object/$objectId/reserve")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
        reservationRepository.findAll().size() == 1

        def reservation = objectRepository.findById(objectId).get().reservations[0]
        reservation.start == Instant.parse("2022-08-18T17:03:35.00Z")
        reservation.end == Instant.parse("2022-08-20T17:03:35.00Z")
        reservation.cost == BigDecimal.valueOf(2111, 2)
        reservation.lessee.userId == 2
    }

    def "It should not create reservation for already reserved object"() {
        given:
        def request = Files.readString(ResourceUtils.getFile(CREATE_RESERVATION_REQUEST_FILE).toPath())
        def objectId = 3

        and:
        reservationRepository.save(new Reservation(
                start: Instant.parse("2022-08-16T17:03:35.00Z"),
                end: Instant.parse("2022-08-19T17:03:35.00Z"),
                cost: BigDecimal.valueOf(3377, 2),
                lessee: userRepository.findById(2).get(),
                object: objectRepository.findById(objectId).get()
        ))

        when:
        def result = mvc.perform(post(ApplicationConstants.API_PREFIX + "/object/$objectId/reserve")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("\$.message").value("Object already reserved at this time."))
        reservationRepository.findAll().size() == 1
    }

    def "It should properly update reservation"() {
        given:
        def request = Files.readString(ResourceUtils.getFile(UPDATE_RESERVATION_REQUEST_FILE).toPath())
        def objectId = 3
        def reservationId = reservationRepository.save(new Reservation(
                start: Instant.parse("2022-08-01T17:03:35.00Z"),
                end: Instant.parse("2022-08-03T17:03:35.00Z"),
                cost: BigDecimal.valueOf(3377, 2),
                lessee: userRepository.findById(2).get(),
                object: objectRepository.findById(objectId).get()
        )).reservationId

        when:
        def result = mvc.perform(patch(ApplicationConstants.API_PREFIX + "/object/$objectId/reservation/$reservationId")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
        then:
        result.andExpect(status().isOk())

        def reservation = reservationRepository.findById(reservationId).get()
        reservation.start == Instant.parse("2022-08-18T17:03:35.00Z")
        reservation.end == Instant.parse("2022-08-20T17:03:35.00Z")
        reservation.cost == BigDecimal.valueOf(2111, 2)
    }

    def "It should return bad request given extending reservation is not possible"() {
        given:
        def objectId = 3

        def start = Instant.parse("2022-08-01T17:03:35.00Z")
        def end = Instant.parse("2022-08-03T17:03:35.00Z")
        def cost = BigDecimal.valueOf(3377, 2)
        def reservationId = reservationRepository.save(new Reservation(
                start: start,
                end: end,
                cost: cost,
                lessee: userRepository.findById(2).get(),
                object: objectRepository.findById(objectId).get()
        )).reservationId

        and:
        reservationRepository.save(new Reservation(
                start: end,
                end: Instant.parse("2022-08-05T17:03:35.00Z"),
                cost: BigDecimal.valueOf(1156, 2),
                lessee: userRepository.findById(2).get(),
                object: objectRepository.findById(objectId).get()
        ))

        when:
        def result = mvc.perform(patch(ApplicationConstants.API_PREFIX + "/object/$objectId/reservation/$reservationId")
                .content("{\"end\": \"2022-08-05T17:03:35.00Z\"}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("\$.message").value("Object already reserved at this time."))

        def reservation = reservationRepository.findById(reservationId).get()
        reservation.start == start
        reservation.end == end
        reservation.cost == cost
    }

}
