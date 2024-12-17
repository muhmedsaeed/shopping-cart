package com.mosaeed.shoppingcart.service.image;

import com.mosaeed.shoppingcart.dto.ImageDto;
import com.mosaeed.shoppingcart.exceptions.ResourceNotFoundException;
import com.mosaeed.shoppingcart.model.Image;
import com.mosaeed.shoppingcart.model.Product;
import com.mosaeed.shoppingcart.repo.ImageRepository;
import com.mosaeed.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    // no need to @Autowired because of final field and @RequiredArgsConstructor
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image Not Found!"));
    }


    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {

        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setProduct(product);
                image.setImage(new SerialBlob(file.getBytes()));

                String buildDownloadUrl = "/api/v1/images/image/download/";
                // TODO I Thought it is not true to do .. but instructor did it
                //image.setDownloadUrl(buildDownloadUrl + image.getId()); // this id is not true

                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImagesDto.add(imageDto);

            } catch(IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            // TODO I'm Not Ensure From That
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(
                imageRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Image Not Found!");
                });
    }



}
