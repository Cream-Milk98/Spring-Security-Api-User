package com.phuc.demo.entities;

import lombok.*;

import javax.persistence.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "about", catalog = "fashion")
public class About implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    private String image;

    private String title;

    @Column(name = "sub_title")
    private String subTitle;
    
    private String detail;
}

