/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    Login login = new Login();
    // =========================
    // 6 assertEquals TESTS
    // =========================
    @Test
    public void testUsernameCorrectMessage() {
        login.setUserDetails(
                "Kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );
        assertEquals(
                "Username successfully captured.",
                login.checkUserName("Kyl_1")
                        ? "Username successfully captured."
                        : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length."
        );
    }
    @Test
    public void testUsernameIncorrectMessage() {
        login.setUserDetails(
                "Kyle!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );
        assertEquals(
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.checkUserName("Kyle!!!!")
                        ? "Username successfully captured."
                        : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length."
        );
    }
    @Test
    public void testPasswordCorrectMessage() {
        assertEquals(
                "Password successfully captured.",
                login.checkPasswordComplexity("Ch&&sec@ke99!")
                        ? "Password successfully captured."
                        : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character."
        );
    }
    @Test
    public void testPasswordIncorrectMessage() {
        assertEquals(
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.checkPasswordComplexity("password")
                        ? "Password successfully captured."
                        : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character."
        );
    }
    @Test
    public void testCellPhoneCorrectMessage() {
        assertEquals(
                "Cell number successfully captured.",
                login.checkCellPhoneNumber("+27838968976")
                        ? "Cell number successfully captured."
                        : "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again."
        );
    }
    @Test
    public void testCellPhoneIncorrectMessage() {
        assertEquals(
                "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                login.checkCellPhoneNumber("08966553")
                        ? "Cell number successfully captured."
                        : "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again."
        );
    }
    // =========================
    // 8 assertTrue / assertFalse TESTS
    // =========================
    @Test
    public void testLoginSuccessful() {
        login.setUserDetails(
                "Kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );
        assertTrue(login.loginUser("Kyl_1", "Ch&&sec@ke99!"));
    }
    @Test
    public void testLoginFailed() {
        login.setUserDetails(
                "Kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );
        assertFalse(login.loginUser("Kyl_1", "wrongPassword"));
    }
    @Test
    public void testUsernameCorrect() {
        assertTrue(login.checkUserName("Kyl_1"));
    }
    @Test
    public void testUsernameIncorrect() {
        assertFalse(login.checkUserName("Kyle!!!!"));
    }
    @Test
    public void testPasswordCorrect() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }
    @Test
    public void testPasswordIncorrect() {
        assertFalse(login.checkPasswordComplexity("password"));
    }
    @Test
    public void testCellPhoneCorrect() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }
    @Test
    public void testCellPhoneIncorrect() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}