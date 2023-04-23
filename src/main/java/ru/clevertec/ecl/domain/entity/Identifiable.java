package ru.clevertec.ecl.domain.entity;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> {
    T getId();
}
