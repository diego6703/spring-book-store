package p.projects.springbookstore.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String isbn;
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
