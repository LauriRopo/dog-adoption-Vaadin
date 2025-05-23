package com.example.application.views.login;

import com.example.application.entity.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import com.example.application.repository.AdoptionRequestRepository;
import com.example.application.repository.DogRepository;
import com.example.application.repository.LocationRepository;
import com.example.application.repository.TrainingProgramRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.util.List;

@PageTitle("Dashboard")
@Route("dashboard")
@RolesAllowed("EMPLOYER")
public class DashboardView extends com.vaadin.flow.component.Composite<VerticalLayout> {

    private final DogRepository dogRepo;
    private final AdoptionRequestRepository requestRepo;
    private final LocationRepository locationRepo;
    private final TrainingProgramRepository trainingRepo;
    private final Grid<Dog> dogGrid;
    private final Grid<AdoptionRequest> requestGrid;

    private static final String IMAGE_UPLOAD_DIR = "uploaded-images";

    public DashboardView(DogRepository dogRepo,
                         AdoptionRequestRepository requestRepo,
                         LocationRepository locationRepo,
                         TrainingProgramRepository trainingRepo) {
        this.dogRepo = dogRepo;
        this.requestRepo = requestRepo;
        this.locationRepo = locationRepo;
        this.trainingRepo = trainingRepo;

        VerticalLayout layout = getContent();
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setPadding(true);
        Button logoutButton = new Button("Logout", event -> handleLogout());
        Button addDogButton = new Button("Add New Dog", e -> openAddDogDialog());


        TextField locationNameField = new TextField();
        locationNameField.setPlaceholder("Enter Location Name");
        Button addLocationButton = new Button("Add Location", e -> {
            String name = locationNameField.getValue();
            if (!name.isEmpty()) {
                Location location = new Location();
                location.setName(name);
                locationRepo.save(location);
                locationNameField.clear();
                Notification.show("Location added");
            }
        });


        TextField trainingNameField = new TextField();
        trainingNameField.setPlaceholder("Enter Training Program Name");
        TextArea trainingDescriptionField = new TextArea();
        trainingDescriptionField.setPlaceholder("Enter Training Program Description");
        Button addTrainingButton = new Button("Add Training Program", e -> {
            String name = trainingNameField.getValue();
            String description = trainingDescriptionField.getValue();
            if (!name.isEmpty() && !description.isEmpty()) {
                TrainingProgram training = new TrainingProgram();
                training.setName(name);
                training.setDescription(description);
                trainingRepo.save(training);
                trainingNameField.clear();
                trainingDescriptionField.clear();
                Notification.show("Training program added");
            }
        });


        HorizontalLayout buttonsLayout = new HorizontalLayout(logoutButton,
                addDogButton, locationNameField, addLocationButton,
                trainingNameField, trainingDescriptionField, addTrainingButton
        );


        buttonsLayout.setSpacing(true);


        layout.add(buttonsLayout);

        // Dog Grid
        dogGrid = new Grid<>(Dog.class, false);
        dogGrid.addColumn(Dog::getName).setHeader("Name").setSortable(true);
        dogGrid.addColumn(Dog::getBreed).setHeader("Breed").setSortable(true);
        dogGrid.addColumn(Dog::getAge).setHeader("Age").setSortable(true);
        dogGrid.addColumn(Dog::getColor).setHeader("Color").setSortable(true);
        dogGrid.addColumn(dog -> dog.isVaccinated() ? "Yes" : "No").setHeader("Vaccinated").setSortable(true);
        dogGrid.addColumn(dog -> dog.getLocation() != null ? dog.getLocation().getName() : "No location")
                .setHeader("Location").setSortable(true);
        dogGrid.addColumn(dog -> dog.getTrainings() != null && !dog.getTrainings().isEmpty()
                        ? String.join(", ", dog.getTrainings().stream().map(TrainingProgram::getName).toList())
                        : "No training")
                .setHeader("Training").setSortable(true);

        dogGrid.addComponentColumn(dog -> {
            Button editButton = new Button("Edit", e -> openEditDogDialog(dog));
            Button deleteButton = new Button("Delete", e -> {
                dogRepo.delete(dog);
                refreshGrids();
                Notification.show("Dog deleted");
            });
            return new HorizontalLayout(editButton, deleteButton);
        });

        dogGrid.setItems(dogRepo.findAll());


        requestGrid = new Grid<>(AdoptionRequest.class, false);
        requestGrid.addColumn(AdoptionRequest::getFirstName).setHeader("First Name").setSortable(true);
        requestGrid.addColumn(AdoptionRequest::getLastName).setHeader("Last Name").setSortable(true);
        requestGrid.addColumn(AdoptionRequest::getEmail).setHeader("Email").setSortable(true);
        requestGrid.addColumn(AdoptionRequest::getPhoneNumber).setHeader("Phone").setSortable(true);
        requestGrid.addColumn(AdoptionRequest::getBirthday).setHeader("Birthday").setSortable(true);
        requestGrid.addColumn(AdoptionRequest::getOccupation).setHeader("Occupation").setSortable(true);
        requestGrid.addColumn(AdoptionRequest::getAbout).setHeader("About").setSortable(true);
        requestGrid.addColumn(req -> req.getDog() != null ? req.getDog().getName() : "No dog selected")
                .setHeader("Desired Dog").setSortable(true);

        requestGrid.addComponentColumn(request -> {
            Button deleteButton = new Button("Delete", e -> {
                requestRepo.delete(request);
                refreshGrids();
                Notification.show("Request deleted");
            });
            return deleteButton;
        });

        requestGrid.setItems(requestRepo.findAll());

        VerticalLayout dogLayout = new VerticalLayout(new H2("Dogs List"), dogGrid);
        dogLayout.setSizeFull();
        dogLayout.setFlexGrow(1, dogGrid);

        VerticalLayout requestLayout = new VerticalLayout(new H2("Adoption Requests"), requestGrid);
        requestLayout.setSizeFull();
        requestLayout.setFlexGrow(1, requestGrid);

        layout.add(dogLayout, requestLayout);
        layout.setFlexGrow(1, dogLayout, requestLayout);

    }

    private void refreshGrids() {
        dogGrid.setItems(dogRepo.findAll());
        requestGrid.setItems(requestRepo.findAll());
    }

    private void openEditDogDialog(Dog dog) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Dog");

        TextField nameField = new TextField("Name", dog.getName());
        TextField breedField = new TextField("Breed", dog.getBreed());
        IntegerField ageField = new IntegerField("Age");
        ageField.setValue(dog.getAge());
        TextField colorField = new TextField("Color", dog.getColor());
        Checkbox vaccinatedCheckbox = new Checkbox("Vaccinated", dog.isVaccinated());

        ComboBox<Location> locationComboBox = new ComboBox<>("Location");
        locationComboBox.setItems(locationRepo.findAll());
        locationComboBox.setItemLabelGenerator(Location::getName);
        locationComboBox.setValue(dog.getLocation());

        MultiSelectComboBox<TrainingProgram> trainingsComboBox = new MultiSelectComboBox<>("Trainings");
        trainingsComboBox.setItems(trainingRepo.findAll());
        trainingsComboBox.setItemLabelGenerator(TrainingProgram::getName);
        trainingsComboBox.setValue(dog.getTrainings());

        FormLayout formLayout = new FormLayout(
                nameField, breedField, ageField, colorField,
                vaccinatedCheckbox, locationComboBox, trainingsComboBox
        );
        dialog.add(formLayout);

        Button saveButton = new Button("Save", event -> {
            dog.setName(nameField.getValue());
            dog.setBreed(breedField.getValue());
            dog.setAge(ageField.getValue());
            dog.setColor(colorField.getValue());
            dog.setVaccinated(vaccinatedCheckbox.getValue());
            dog.setLocation(locationComboBox.getValue());
            dog.setTrainings(trainingsComboBox.getSelectedItems());
            dogRepo.save(dog);
            refreshGrids();
            dialog.close();
            Notification.show("Dog updated");
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
    }

    private void openAddDogDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add New Dog");

        TextField nameField = new TextField("Name");
        TextField breedField = new TextField("Breed");
        IntegerField ageField = new IntegerField("Age");
        TextField colorField = new TextField("Color");
        Checkbox vaccinatedCheckbox = new Checkbox("Vaccinated");

        ComboBox<Location> locationComboBox = new ComboBox<>("Location");
        locationComboBox.setItems(locationRepo.findAll());
        locationComboBox.setItemLabelGenerator(Location::getName);

        MultiSelectComboBox<TrainingProgram> trainingsComboBox = new MultiSelectComboBox<>("Trainings");
        trainingsComboBox.setItems(trainingRepo.findAll());
        trainingsComboBox.setItemLabelGenerator(TrainingProgram::getName);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(1);
        upload.setDropAllowed(true);

        FormLayout formLayout = new FormLayout(
                nameField, breedField, ageField, colorField,
                vaccinatedCheckbox, locationComboBox, trainingsComboBox, upload
        );
        dialog.add(formLayout);

        Button saveButton = new Button("Save", event -> {
            Dog newDog = new Dog();
            newDog.setName(nameField.getValue());
            newDog.setBreed(breedField.getValue());
            newDog.setAge(ageField.getValue());
            newDog.setColor(colorField.getValue());
            newDog.setVaccinated(vaccinatedCheckbox.getValue());
            newDog.setLocation(locationComboBox.getValue());
            newDog.setTrainings(trainingsComboBox.getSelectedItems());


            newDog.setStatus(DogStatus.AVAILABLE);




            dogRepo.save(newDog);
            refreshGrids();
            dialog.close();
            Notification.show("New dog added");
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
    }
    private void handleLogout() {

        SecurityContextHolder.clearContext();


        UI.getCurrent().navigate("login");


        Notification notification = new Notification("You have been logged out", 3000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }
}
