package grp.javatemplate.service;

import grp.javatemplate.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class UserServiceTest extends BaseIntegrationTest {

    static Stream<String> validEmails() {
        return Stream.of("email@gmail.com", "myEmail@outlook.com", "username@domain.com",
                "username@domain.eu",
                "username@domain.co.uk",
                null,
                "email@gmail.com",
                "test@domain.com",
                "lastname@domain.com",
                "test.email.with+symbol@domain.com");
    }

    static Stream<String> invalidEmails() {
        return Stream.of("tere", " ", "", "email@", "email@.com", "email@gmail", "email@gmail.", "123", "use@123-com",
                "usernamedomain.com",
                "username@domaincom",
                "A@b@c@domain.com",
                "abc\\ is\\”not\\valid@domain.com",
                "a”b(c)d,e:f;gi[j\\k]l@domain.com");
    }

    static Stream<String> validPhoneNumbers() {
        return Stream.of("+372 1234567", "+123 12345678", "+44 1234567890", "+1 1234567890", null, "+123 123456789");
    }

    static Stream<String> inValidPhoneNumbers() {
        return Stream.of("1234567", "+44 12345678901", "+1 12345678901", "+1 123 456 78901", "+1 123-456-78901");
    }

    @ParameterizedTest
    @MethodSource("validEmails")
    void isEmailValid_ShouldReturnTrueForValidEmails(String email) {
        Assertions.assertTrue(userService.isEmailValid(email));
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void isEmailValid_ShouldReturnFalseForInvalidEmails(String email) {
        Assertions.assertFalse(userService.isEmailValid(email));
    }

    @ParameterizedTest
    @MethodSource("validPhoneNumbers")
    void isPhoneNumberValid_ShouldReturnTrueForValidPhoneNumbers(String phoneNumber) {
        Assertions.assertTrue(userService.isPhoneNumberValid(phoneNumber));
    }

    @ParameterizedTest
    @MethodSource("inValidPhoneNumbers")
    void isPhoneNumberValid_ShouldReturnFalseForInvalidPhoneNumbers(String phoneNumber) {
        Assertions.assertFalse(userService.isPhoneNumberValid(phoneNumber));
    }

}