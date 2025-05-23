package com.example.application.views.personform;

import com.example.application.entity.AdoptionRequest;
import com.example.application.entity.Dog;
import com.example.application.entity.DogStatus;
import com.example.application.repository.AdoptionRequestRepository;
import com.example.application.repository.DogRepository;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;

import jakarta.annotation.security.PermitAll;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Adoption Request Form")
@Route("person-form")
@PermitAll
@Menu(order = 1, icon = LineAwesomeIconUrl.USER)
public class PersonFormView extends Composite<VerticalLayout> {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final DogRepository dogRepository;

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final EmailField email = new EmailField("Email");
    private final TextField phone = new TextField("Phone Number");
    private final DatePicker birthday = new DatePicker("Your Birthday");
    private final TextField occupation = new TextField("Occupation");
    private final TextArea aboutYou = new TextArea("Tell us about yourself");
    private final ComboBox<Dog> dogSelect = new ComboBox<>("Choose a Dog");

    public PersonFormView(AdoptionRequestRepository adoptionRequestRepository, DogRepository dogRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.dogRepository = dogRepository;

        VerticalLayout layout = getContent();
        layout.setWidth("100%");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("800px");

        H3 title = new H3("Adoption Request Form");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");


        dogSelect.setItems(dogRepository.findAll());
        dogSelect.setItemLabelGenerator(dog -> {
            if (dog == null || dog.getName() == null) {
                return "";
            }
            return dog.getName();
        });

        dogSelect.setWidthFull();

        aboutYou.setPlaceholder("Describe your living situation, why you're interested in adoption, experience with pets, etc.");
        aboutYou.setWidthFull();
        aboutYou.setMinHeight("150px");

        formLayout.add(firstName, lastName, email, phone, birthday, occupation, dogSelect, aboutYou);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addClassName("gap-medium");

        Button sendButton = new Button("Send", e -> {
            Dog selectedDog = dogSelect.getValue();


            if (selectedDog != null && selectedDog.getStatus() == DogStatus.AVAILABLE) {

                selectedDog.setStatus(DogStatus.ADOPTED);
                dogRepository.save(selectedDog);


                AdoptionRequest request = new AdoptionRequest();
                request.setFirstName(firstName.getValue());
                request.setLastName(lastName.getValue());
                request.setEmail(email.getValue());
                request.setPhone(phone.getValue());
                request.setBirthday(birthday.getValue());
                request.setOccupation(occupation.getValue());
                request.setAbout(aboutYou.getValue());
                request.setDog(selectedDog);

                adoptionRequestRepository.save(request);
                Notification.show("Thanks! We've received your adoption request.");
            } else {
                Notification.show("Sorry, this dog is no longer available for adoption.");
            }
        });
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel");

        buttons.add(sendButton, cancelButton);
        container.add(title, formLayout, buttons);
        layout.add(container);
    }
}
