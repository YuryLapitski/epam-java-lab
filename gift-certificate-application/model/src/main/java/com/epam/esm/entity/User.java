package com.epam.esm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.entity.Permission.AUTH_SIGN_UP;
import static com.epam.esm.entity.Permission.GIFT_CERTIFICATES_CREATE;
import static com.epam.esm.entity.Permission.GIFT_CERTIFICATES_DELETE;
import static com.epam.esm.entity.Permission.GIFT_CERTIFICATES_UPDATE;
import static com.epam.esm.entity.Permission.ORDERS_CREATE;
import static com.epam.esm.entity.Permission.ORDERS_DELETE;
import static com.epam.esm.entity.Permission.ORDERS_READ;
import static com.epam.esm.entity.Permission.TAGS_CREATE;
import static com.epam.esm.entity.Permission.TAGS_DELETE;
import static com.epam.esm.entity.Permission.USERS_CREATE;
import static com.epam.esm.entity.Permission.USERS_READ;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User extends RepresentationModel<User> {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, login, password, role);
    }

    public enum Role {
        USER(new HashSet<>(Arrays.asList(USERS_CREATE, AUTH_SIGN_UP))),
        ADMIN(new HashSet<>(Arrays.asList(GIFT_CERTIFICATES_CREATE, GIFT_CERTIFICATES_UPDATE,
                GIFT_CERTIFICATES_DELETE, TAGS_CREATE, TAGS_DELETE, USERS_CREATE, USERS_READ,
                ORDERS_CREATE, ORDERS_READ, ORDERS_DELETE, AUTH_SIGN_UP)));

        private final Set<Permission> permissions;

        Role(Set<Permission> permissionSet) {
            this.permissions = permissionSet;
        }

        public Set<SimpleGrantedAuthority> getAuthorities() {
            return permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toSet());
        }
    }
}
