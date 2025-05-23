package com.example.application.views.imagegallery;

import com.example.application.entity.Dog;
import com.example.application.repository.DogRepository;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import jakarta.annotation.PostConstruct;

@PageTitle("Dog Adoption")
@Route("")
@PermitAll
@Menu(order = 0, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
public class ImageGalleryView extends Main implements HasComponents, HasStyle {

    private final DogRepository dogRepository;
    private OrderedList imageContainer;

    @Autowired
    public ImageGalleryView(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
        constructUI();
    }

    @PostConstruct
    private void loadData() {
        dogRepository.findAllWithTrainings()
                .forEach(dog -> {
                    imageContainer.add(new ImageGalleryViewCard(dog));
                });
    }

    private void constructUI() {
        addClassNames("image-gallery-view", MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);


        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);


        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Dogs for Adoption");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("These dogs are looking for a new home.");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);


        imageContainer = new OrderedList();
        imageContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);


        imageContainer.getStyle().set("display", "grid");
        imageContainer.getStyle().set("grid-template-columns", "repeat(4, 1fr)");
        imageContainer.getStyle().set("gap", "16px");


        imageContainer.getStyle().set("grid-template-columns", "repeat(auto-fill, minmax(220px, 1fr))");




        add(container, headerContainer, imageContainer);
    }
}
