package p.projects.springbookstore.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UpdateBookRequestDto {
    @Size(max = 255, message = "Title is too long")
    private String title;

    @Size(max = 255, message = "Author name is too long")
    private String author;

    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN must be exactly 13 digits")
    private String isbn;

    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 1000, message = "Description is too long")
    private String description;

    @URL(message = "Cover image must be a valid URL")
    private String coverImage;
}
