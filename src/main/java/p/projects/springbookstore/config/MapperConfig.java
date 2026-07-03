package p.projects.springbookstore.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        implementationPackage = "p.projects.springbookstore.mapper.impl"
)
public class MapperConfig {
}
