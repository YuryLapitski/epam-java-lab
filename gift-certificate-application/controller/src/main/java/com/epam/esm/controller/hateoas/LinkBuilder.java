package com.epam.esm.controller.hateoas;

import java.util.List;

public interface LinkBuilder<T> {
    void setLinks(T t);

    void setLinks(List<T> tList);
}
