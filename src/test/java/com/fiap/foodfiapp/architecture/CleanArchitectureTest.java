package com.fiap.foodfiapp.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.fiap.foodfiapp", importOptions = {ImportOption.DoNotIncludeTests.class})
public class CleanArchitectureTest {

    // Regra 1: Valida as dependências entre as camadas
    // Exemplo: a camada de infraestrutura deve depender apenas da camada de aplicação ou de domínio
    @ArchTest
    public static final ArchRule layered_architecture_is_respected = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure");

    // Regra 2: Garante que a camada de domínio não dependa de frameworks
    // Isso mantém as regras de negócio puras e reutilizáveis
    @ArchTest
    public static final ArchRule domain_sem_frameworks =
            ArchRuleDefinition.noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework..",
                            "jakarta.persistence..",
                            "javax.persistence..",
                            "org.hibernate..",
                            "lombok.."
                    );

    // Regra 3: Garante que a camada de aplicação não dependa de frameworks
    // Mantém a lógica dos casos de uso independente da infraestrutura
    @ArchTest
    public static final ArchRule application_sem_frameworks =
            ArchRuleDefinition.noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework..",
                            "jakarta.persistence..",
                            "javax.persistence..",
                            "org.hibernate..",
                            "lombok.."
                    );

    // Regra 4: Valida que a camada de infraestrutura só depende de si mesma ou das camadas internas
    // Reforça a "Regra de Dependência" da Arquitetura Limpa
    @ArchTest
    public static final ArchRule dependencias_apontam_para_dentro =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..infrastructure..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage("..infrastructure..", "..application..", "..domain..", "java..");

    // Regra 5: Garante que a camada de REST/controller não exponha entidades de domínio diretamente
    // Promove a utilização de DTOs (UserRequestDTO, UserResponseDTO) para comunicação externa
    @ArchTest
    public static final ArchRule controller_nao_expoe_domain =
            ArchRuleDefinition.noClasses()
                    .that().resideInAPackage("..infrastructure.rest..")
                    .should().dependOnClassesThat().resideInAPackage("..domain..");

}
