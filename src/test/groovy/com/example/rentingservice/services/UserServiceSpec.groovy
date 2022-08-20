package com.example.rentingservice.services

import com.example.rentingservice.entities.Reservation
import com.example.rentingservice.entities.User
import com.example.rentingservice.mappres.reservationresponse.ReservationToReservationResponseMapper
import com.example.rentingservice.models.reservationresponse.ReservationResponse
import com.example.rentingservice.repositories.UserRepository
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityNotFoundException
import java.time.Instant

class UserServiceSpec extends Specification {

    UserRepository userRepository = Mock()
    ReservationToReservationResponseMapper reservationResponseMapper = Mock()

    @Subject
    def service = new UserService(userRepository, reservationResponseMapper)

    def "It should properly return reservations"() {
        given:
        def userId = 1
        def reservation1 = new Reservation(start: Instant.parse("2007-12-03T10:15:30.00Z"))
        def reservation2 = new Reservation(start: Instant.parse("1999-12-03T10:15:30.00Z"))
        def user = new User(userId: userId, reservations: [reservation1, reservation2])

        and:
        def response1 = ReservationResponse.builder()
                .start(Instant.parse("2007-12-03T10:15:30.00Z"))
                .build()
        def response2 = ReservationResponse.builder()
                .start(Instant.parse("1999-12-03T10:15:30.00Z"))
                .build()

        when:
        def result = service.getReservations(userId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * reservationResponseMapper.map(reservation1) >> response1
        1 * reservationResponseMapper.map(reservation2) >> response2
        result == [response1, response2]
    }

    def "It should properly throw exception given user not found"() {
        given:
        def userId = 11

        when:
        service.getReservations(userId)

        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        def e = thrown(EntityNotFoundException.class)
        e.getMessage() == "User with id: $userId not found"
    }

}
