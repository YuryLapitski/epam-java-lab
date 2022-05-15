package com.epam.esm.controller.hateoas;

public interface LinkBuilder<T> {
    void setLinks(T t);
}
