package com.example.application.views.imagegallery;

import com.example.application.entity.Dog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

public class ImageGalleryViewCard extends ListItem {

    public ImageGalleryViewCard(Dog dog) {


        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.LARGE, Width.FULL);


        div.setHeight("400px");
        div.setWidth("220px");
        div.getStyle().set("border", "1px solid #000");
        div.getStyle().set("box-shadow", "2px 2px 8px rgba(0, 0, 0, 0.15)");


        String imageUrl = "https://place-puppy.com/puppy/y:" + (dog.getId() % 100) + "/x:250";
        Image image = new Image();
        image.setWidth("100%");
        image.setHeight("60%");
        image.setSrc(imageUrl);
        image.setAlt(dog.getName());

        div.add(image);


        Span header = new Span();
        header.addClassNames(FontSize.LARGE, FontWeight.SEMIBOLD);
        header.setText(dog.getName());


        Span subtitle = new Span();
        subtitle.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subtitle.setText(dog.getBreed());


        Paragraph description = new Paragraph(
                "Ikä: " + dog.getAge() + " vuotta, Väri: " + dog.getColor()
        );
        description.addClassName(Margin.Vertical.MEDIUM);


        String locationName = (dog.getLocation() != null) ? dog.getLocation().getName() : "Ei määritelty";
        Paragraph description2 = new Paragraph("Sijainti: " + locationName);
        description2.addClassName(Margin.Vertical.MEDIUM);


        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge " + (dog.isVaccinated() ? "success" : "error"));
        badge.setText(dog.isVaccinated() ? "Rokotettu" : "Ei rokotettu");


        add(div, header, subtitle, description, description2, badge);
    }
}
