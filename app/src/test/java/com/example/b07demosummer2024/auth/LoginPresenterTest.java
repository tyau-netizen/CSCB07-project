package com.example.b07demosummer2024.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.example.b07demosummer2024.user.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "testPassword";

    @Mock
    private LoginContract.View mockView;
    @Mock
    private AuthRepository mockAuthRepository;
    @Mock
    private SessionManager mockSessionManager;
    private LoginContract.Presenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(mockAuthRepository, mockSessionManager);
        presenter.attachView(mockView);
    }

    @Test
    public void handleLogin_emptyFields_displaysErrorMessage() {
        presenter.handleLogin("", "");

        verify(mockView).displayToastMessage("Please fill out all the fields");
        verifyNoInteractions(mockAuthRepository);
        verifyNoInteractions(mockSessionManager);
    }

    @Test
    public void handleLogin_authFailure_displaysErrorMessage() {
        String errorMsg = "Invalid Credentials";

        doAnswer((Answer<Void>) invocation -> {
            AuthRepository.AuthCallback callback = invocation.getArgument(2);
            callback.onFailure(errorMsg);
            return null;
        }).when(mockAuthRepository)
                .signIn(anyString(), anyString(), any(AuthRepository.AuthCallback.class));

        presenter.handleLogin(TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).displayToastMessage(errorMsg);
        verifyNoInteractions(mockSessionManager);
        verify(mockView, never()).navigateToHome(anyBoolean());
    }

    @Test
    public void handleLogin_authSuccess_sessionFailure_displaysErrorMessage() {
        String sessionErrorMsg = "User data not found";
        Exception sessionError = new Exception(sessionErrorMsg);

        doAnswer((Answer<Void>) invocation -> {
            AuthRepository.AuthCallback callback = invocation.getArgument(2);
            callback.onSuccess();
            return null;
        }).when(mockAuthRepository)
                .signIn(anyString(), anyString(), any(AuthRepository.AuthCallback.class));

        doAnswer((Answer<Void>) invocation -> {
            SessionManager.SessionCallback callback = invocation.getArgument(0);
            callback.onFailure(sessionError);
            return null;
        }).when(mockSessionManager)
                .startSession(any(SessionManager.SessionCallback.class));

        presenter.handleLogin(TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).displayToastMessage("Failed to load user profile: " + sessionErrorMsg);
        verify(mockView, never()).navigateToHome(anyBoolean());
    }

    @Test
    public void handleLogin_authSuccess_sessionSuccess_navigatesToHome() {
        doAnswer((Answer<Void>) invocation -> {
            AuthRepository.AuthCallback callback = invocation.getArgument(2);
            callback.onSuccess();
            return null;
        }).when(mockAuthRepository)
                .signIn(anyString(), anyString(), any(AuthRepository.AuthCallback.class));

        doAnswer((Answer<Void>) invocation -> {
            SessionManager.SessionCallback callback = invocation.getArgument(0);
            callback.onSuccess();
            return null;
        }).when(mockSessionManager)
                .startSession(any(SessionManager.SessionCallback.class));

        presenter.handleLogin(TEST_EMAIL, TEST_PASSWORD);

        verify(mockView).navigateToHome(false);
    }

    @Test
    public void handleRegisterClick_navigatesToRegister() {
        presenter.handleRegisterClick();

        verify(mockView).navigateToRegister();
    }
}
