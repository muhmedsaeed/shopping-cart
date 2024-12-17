package com.mosaeed.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String downloadUrl;

    @Lob
    private Blob image;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;


}
