package grp.javatemplate.service;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.exception.UserException;
import grp.javatemplate.mapper.UserMapper;
import grp.javatemplate.model.User;
import grp.javatemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static grp.javatemplate.exception.UserException.*;
import static grp.javatemplate.model.User.EMAIL_REGEX;
import static grp.javatemplate.model.User.PHONE_NR_REGEX;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String USER_NAME = "name";
    private static final String USER_ID = "id";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_SIZE_OF_PAGE = 10;

    public List<User> findAll( String sortBy ) {
        if(Objects.equals(sortBy, USER_NAME)) {
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, USER_NAME));
        }
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, USER_ID));
    }

    public Page<User> findAllPages( String sortBy, Integer pageNumber, Integer sizeOfPage ) {
        if (pageNumber == null) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (sizeOfPage == null) {
            sizeOfPage = DEFAULT_SIZE_OF_PAGE;
        }
        if (sortBy == null) {
            sortBy = USER_ID;
        }

        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage,Sort.by(Sort.Direction.ASC, sortBy));
        return userRepository.findAll(pageable);
    }

    //TODO: sync users with keycloak?
    public User save( UserDto userDto ) {
        if ( userRepository.existsByName(userDto.getName()) ) {
            throw new UserException(USER_DUPLICATE_NAME);
        }
        User user = userMapper.toEntity(userDto);
        return userRepository.save(user);
    }

    public User update( User user ) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(USER_DOES_NOT_EXIST));
        user.setCreatedAt(existingUser.getCreatedAt()).setCreatedBy(existingUser.getCreatedBy());
        return userRepository.save(user);
    }

    public void delete( Long id ) {
        if (!userRepository.existsById(id)) {
            throw new UserException(USER_DOES_NOT_EXIST);
        }
        userRepository.deleteById(id);
    }

    public static boolean isEmailValid(String email) {
        if (email == null) {
            return true;
        }
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null) {
            return true;
        }
        return phoneNumber.matches(PHONE_NR_REGEX);
    }
}
