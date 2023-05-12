package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.softue.repositories.userRepository;

@Service
public class userServices {
    @Autowired
    userRepository userrepository;
}
