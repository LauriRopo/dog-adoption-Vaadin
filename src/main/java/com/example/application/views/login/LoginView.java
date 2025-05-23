package com.example.application.views.login;

import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("login")
@PageTitle("Login")
public class LoginView extends LoginOverlay implements AfterNavigationObserver {

    public LoginView() {
        setAction("login");
        setTitle("Dog Adoption Admin");
        setDescription("For authorized employers only");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) {

            setOpened(true);
        } else {

            getUI().ifPresent(ui -> ui.navigate("dashboard"));
        }


        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            getElement().executeJs("this.error = true");
        }
    }
}
