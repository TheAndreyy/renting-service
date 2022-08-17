package com.example.rentingservice.mappres.reservationresponse;

import com.example.rentingservice.entities.User;
import com.example.rentingservice.models.reservationresponse.UserResponse;
import org.springframework.stereotype.Component;

@Component
class UserToUserResponseMapper {

    UserResponse map(User user) {
        return new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName());
    }

}
