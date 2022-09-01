package com.example.demo.user;

import com.example.demo.controller.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Mock
    private  UserRepository userRepository;
    private AutoCloseable autoclosable;

    private UserService underTest;

    @Before
    public void init(){
        this.autoclosable = MockitoAnnotations.openMocks(this);
        this.underTest = new UserService(userRepository);
    }

    @Test
    public void updateUser(){
        User user = new User("micaela","micaela@mail.com", "password");
        System.out.println("User" + underTest.createUser(user));
    }
}
