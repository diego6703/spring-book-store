package p.projects.springbookstore.dto;

public record BookSearchParametersDto(
        String[] title,
        String[] author,
        String[] isbn
) {
}
