package com.example.rentingservice.controllers

import com.example.rentingservice.WebMvcSpecIT
import com.example.rentingservice.config.ApplicationConstants
import org.springframework.http.MediaType

import javax.persistence.EntityNotFoundException

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReservationObjectControllerSpecIT extends WebMvcSpecIT {

    // FIXME:
    def "It should properly throw exception"() {
        when:
        def result = mvc.perform(get(ApplicationConstants.API_PREFIX + "/object/100/reservations")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isInternalServerError())
        thrown(EntityNotFoundException.class)
    }

}
