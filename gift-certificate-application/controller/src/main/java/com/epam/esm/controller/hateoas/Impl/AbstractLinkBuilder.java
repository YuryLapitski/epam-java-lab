package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.hateoas.LinkBuilder;
import org.springframework.hateoas.RepresentationModel;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class AbstractLinkBuilder<T extends RepresentationModel<T>> implements LinkBuilder<T> {
    protected static final String DELETE = "delete";
    protected static final String UPDATE = "update";
    protected static final String SELF = "self";

    protected void setIdLinks(Class<?> controllerClass, T entity, long id, String... linkNames) {
        Arrays.stream(linkNames).forEach(linkName ->
                entity.add(linkTo(controllerClass).slash(id).withRel(linkName)));
    }
}
