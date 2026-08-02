package com.example.b07demosummer2024.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.b07demosummer2024.user.SessionManager;
import com.example.b07demosummer2024.user.User;
import com.example.b07demosummer2024.user.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

public class RegisterPresenterTest {

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_UID = "test_uid_123";

    private RegisterContract.View mockView;
    private AuthRepository mockAuthRepository;
    private UserRepository mockUserRepository;
    private SessionManager mockSessionManager;
    private RegisterPresenter presenter;

    @Before
    public void setUp() {
        mockView = mock(RegisterContract.View.class);
        mockAuthRepository = mock(AuthRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockSessionManager = mock(SessionManager.class);

        presenter = new RegisterPresenter(mockAuthRepository, mockUserRepository, mockSessionManager);
        presenter.attachView(mockView);
    }

    @Test
    public void handleRegister_emptyFields_displaysErrorMessage() {
        presenter.handleRegister("", TEST_EMAIL, TEST_PASSWORD);
        verify(mockView).displayToastMessage("Please fill in all the fields");

        presenter.handleRegister(TEST_USERNAME, "", TEST_PASSWORD);
        verify(mockView, times(2)).displayToastMessage("Please fill in all the fields");

        presenter.handleRegister(TEST_USERNAME, TEST_EMAIL, "");
        verify(mockView, times(3)).displayToastMessage("Please fill in all the fields");

        verifyNoInteractions(mockAuthRepository);
    }

    @Test
    public void handleRegister_signUpFailure_displaysErrorMessage() {
        String errorMsg = "Email already in use";
        setupSignUpFailure(errorMsg);

        presenter.handleRegister(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).displayToastMessage(errorMsg);
        verify(mockView, never()).navigateToHome(anyBoolean());
    }

    @Test
    public void handleRegister_signUpSuccess_missingUid_displaysErrorMessage() {
        setupSignUpSuccess();
        when(mockAuthRepository.getUID()).thenReturn(null);

        presenter.handleRegister(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).displayToastMessage("Registration failed: Missing user ID.");
        verify(mockView, never()).navigateToHome(anyBoolean());
    }

    @Test
    public void handleRegister_signUpSuccess_saveProfileFailure_displaysErrorMessage() {
        String errorMsg = "Database error";
        setupSignUpSuccess();
        when(mockAuthRepository.getUID()).thenReturn(TEST_UID);
        setupSaveProfileFailure(new Exception(errorMsg));

        presenter.handleRegister(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).displayToastMessage("Failed to save user: " + errorMsg);
        verify(mockView, never()).navigateToHome(anyBoolean());
    }

    @Test
    public void handleRegister_signUpSuccess_saveProfileSuccess_sessionSuccess_navigatesToHome() {
        setupSignUpSuccess();
        when(mockAuthRepository.getUID()).thenReturn(TEST_UID);
        setupSaveProfileSuccess();
        setupSessionSuccess();

        presenter.handleRegister(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).navigateToHome(false);
    }

    @Test
    public void handleRegister_detachedView_doesNothing() {
        presenter.detachView();

        presenter.handleRegister(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        verifyNoInteractions(mockView);
        verifyNoInteractions(mockAuthRepository);
        verifyNoInteractions(mockUserRepository);
    }

    @Test
    public void handleLoginClick_navigatesToLogin() {
        presenter.handleLoginClick();

        verify(mockView).navigateToLogin();
    }

    // Helper methods for Mockito responses ------------------------------------------

    private void setupSignUpFailure(String errorMsg) {
        doAnswer((Answer<Void>) invocation -> {
            AuthRepository.AuthCallback callback = invocation.getArgument(2);
            callback.onFailure(errorMsg);
            return null;
        }).when(mockAuthRepository)
                .signUp(anyString(), anyString(), any(AuthRepository.AuthCallback.class));
    }

    private void setupSignUpSuccess() {
        doAnswer((Answer<Void>) invocation -> {
            AuthRepository.AuthCallback callback = invocation.getArgument(2);
            callback.onSuccess();
            return null;
        }).when(mockAuthRepository)
                .signUp(anyString(), anyString(), any(AuthRepository.AuthCallback.class));
    }

    private void setupSaveProfileSuccess() {
        doAnswer((Answer<Void>) invocation -> {
            UserRepository.UserSaveCallback callback = invocation.getArgument(1);
            callback.onSuccess();
            return null;
        }).when(mockUserRepository)
                .saveNewUserProfile(any(User.class), any(UserRepository.UserSaveCallback.class));
    }

    private void setupSaveProfileFailure(Exception error) {
        doAnswer((Answer<Void>) invocation -> {
            UserRepository.UserSaveCallback callback = invocation.getArgument(1);
            callback.onFailure(error);
            return null;
        }).when(mockUserRepository)
                .saveNewUserProfile(any(User.class), any(UserRepository.UserSaveCallback.class));
    }

    private void setupSessionSuccess() {
        doAnswer((Answer<Void>) invocation -> {
            SessionManager.SessionCallback callback = invocation.getArgument(0);
            callback.onSuccess();
            return null;
        }).when(mockSessionManager)
                .startSession(any(SessionManager.SessionCallback.class));
    }
}