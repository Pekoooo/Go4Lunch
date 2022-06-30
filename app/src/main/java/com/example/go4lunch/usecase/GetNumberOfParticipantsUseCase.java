package com.example.go4lunch.usecase;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class GetNumberOfParticipantsUseCase {

    public String restaurantId;
    public UserRepository userRepository;

    public GetNumberOfParticipantsUseCase(String restaurantId){
        this.restaurantId = restaurantId;
        userRepository = UserRepository.getInstance();
    }

    public int invoke(){
        List<User> users = userRepository.getAllCoworkers().getValue();
        List<User> userComing = new ArrayList<>();
        if(users != null){
            for (int i = 0; i < users.size(); i++) {
                User currentUser = users.get(i);

                if(currentUser.getRestaurantChoiceId() != null && currentUser.getRestaurantChoiceId().equals(restaurantId)){
                    userComing.add(currentUser);
                }
            }
        } else {
            return 0;
        }
        return userComing.size();
    }
}
