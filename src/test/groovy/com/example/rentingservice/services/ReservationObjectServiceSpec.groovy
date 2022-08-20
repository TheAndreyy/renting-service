package com.example.rentingservice.services

import com.example.rentingservice.entities.Reservation
import com.example.rentingservice.entities.ReservationObject
import com.example.rentingservice.entities.User
import com.example.rentingservice.exceptions.ObjectAlreadyReservedException
import com.example.rentingservice.mappres.CreateReservationRequestToReservationMapper
import com.example.rentingservice.mappres.reservationresponse.ReservationToReservationResponseMapper
import com.example.rentingservice.models.CreateReservationRequest
import com.example.rentingservice.models.reservationresponse.ReservationResponse
import com.example.rentingservice.repositories.ReservationObjectRepository
import com.example.rentingservice.repositories.ReservationRepository
import com.example.rentingservice.repositories.UserRepository
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityNotFoundException
import java.time.Instant

class ReservationObjectServiceSpec extends Specification {

    ReservationObjectRepository objectRepository = Mock()
    UserRepository userRepository = Mock()
    ReservationRepository reservationRepository = Mock()
    CreateReservationRequestToReservationMapper createReservationMapper = Mock()
    ReservationToReservationResponseMapper reservationResponseMapper = Mock()

    @Subject
    def service = new ReservationObjectService(objectRepository, userRepository, reservationRepository, createReservationMapper, reservationResponseMapper)

    def "It should properly return reservations for object id"() {
        given:
        def reservationObjectId = 1
        def reservation1 = new Reservation(reservationId: 1)
        def reservation2 = new Reservation(reservationId: 2)
        def response1 = new ReservationResponse(null, Instant.parse("2007-12-03T10:15:30.00Z"), null, null, null, null)
        def response2 = new ReservationResponse(null, Instant.parse("1999-12-03T10:15:30.00Z"), null, null, null, null)

        when:
        def result = service.getReservations(reservationObjectId)

        then:
        1 * objectRepository.findById(reservationObjectId) >> Optional.of(
                new ReservationObject(reservations: [reservation1, reservation2])
        )
        1 * reservationResponseMapper.map(reservation1) >> response1
        1 * reservationResponseMapper.map(reservation2) >> response2
        result == [response1, response2]
    }

    def "It should throw exception for object that does not exist"() {
        given:
        def reservationObjectId = 1

        when:
        service.getReservations(reservationObjectId)

        then:
        1 * objectRepository.findById(reservationObjectId) >> Optional.empty()
        def e = thrown(EntityNotFoundException.class)
        e.getMessage() == "Object with id: $reservationObjectId not found"
    }

    def "It should properly create reservation"() {
        given:
        def userId = 1
        def objectId = 2

        when:
        service.createReservation(objectId, new CreateReservationRequest(
                userId: userId,
                start: Instant.parse("2022-08-01T10:15:30.00Z"),
                end: Instant.parse("2022-08-03T10:15:30.00Z"),
                cost: BigDecimal.valueOf(4137, 2)
        ))

        then:
        1 * userRepository.findById(userId) >> Optional.of(new User(userId: userId))
        1 * objectRepository.findByIdWithLock(objectId) >> Optional.of(new ReservationObject(reservationObjectId: objectId))
        1 * reservationRepository.findAllCollidingReservations(objectId, _, _) >> []
        1 * createReservationMapper.map(_) >> new Reservation()
        1 * reservationRepository.save({
            it.lessee.userId == userId
            it.object.reservationObjectId == objectId
        })
        1 * reservationResponseMapper.map(_)
    }

    def "It should throw exception given object already reserved"() {
        given:
        def userId = 1
        def objectId = 2

        when:
        service.createReservation(objectId, new CreateReservationRequest(
                userId: userId
        ))

        then:
        1 * userRepository.findById(userId) >> Optional.of(new User(userId: userId))
        1 * objectRepository.findByIdWithLock(objectId) >> Optional.of(new ReservationObject(reservationObjectId: objectId))
        1 * reservationRepository.findAllCollidingReservations(objectId, _, _) >> [new Reservation()]
        thrown(ObjectAlreadyReservedException.class)
    }

}
