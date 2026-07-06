package p.projects.springbookstore.dto;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns
) {
}
