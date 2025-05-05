package com.inf.users.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "role")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<User> users;
}
