package services;

import com.backend.softue.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class userServices {
    @Autowired
    UserRepository userrepository;
}
