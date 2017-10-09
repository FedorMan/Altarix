package com.company.controll.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "positions")
public class Position {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
