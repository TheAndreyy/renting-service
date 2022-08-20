package com.example.rentingservice.controllers

import com.example.rentingservice.WebMvcSpecIT
import com.example.rentingservice.config.ApplicationConstants
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReservationObjectControllerSpecIT extends WebMvcSpecIT {

    def "It should properly throw exception"() {
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
