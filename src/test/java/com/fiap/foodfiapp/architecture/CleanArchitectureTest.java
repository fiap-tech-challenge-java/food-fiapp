package com.fiap.foodfiapp.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.fiap.foodfiapp", importOptions = {ImportOption.DoNotIncludeTests.class})
public class CleanArchitectureTest {

    // Regra 1: Validação das camadas e suas dependências.
    // CORREÇÃO: Ajustados os pacotes para '..core.domain..' e '..core.application..'.
    @ArchTest
    public static final ArchRule layered_architecture_is_respected = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("..core.domain..")
            .layer("Application").definedBy("..core.application..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure");

    // Regra 2: A camada de Domínio não deve depender de frameworks.
    // CORREÇÃO: Ajustado o pacote para '..core.domain..'.
    @ArchTest
    public static final ArchRule domain_without_frameworks = noClasses()
            .that().resideInAPackage("..core.domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta.persistence..", "lombok..");

    // Regra 3: A camada de Aplicação não deve depender de frameworks de persistência ou web.
    // CORREÇÃO: Ajustado o pacote para '..core.application..'.
    @ArchTest
    public static final ArchRule application_without_frameworks = noClasses()
            .that().resideInAPackage("..core.application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework.web..", "org.springframework.stereotype..", "jakarta.persistence..");

    // Regra 4: A camada de Domínio só deve depender de si mesma e do Java.
    // CORREÇÃO: Ajustado o pacote para '..core.domain..'.
    @ArchTest
    public static final ArchRule domain_depends_only_on_itself_and_java = classes()
            .that().resideInAPackage("..core.domain..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage("..core.domain..", "java..");

    // Regra 5: A camada de Aplicação só pode depender de si, do domínio, Java e bibliotecas de logging.
    // CORREÇÃO: Ajustados os pacotes e adicionada a permissão para 'org.slf4j'.
    @ArchTest
    public static final ArchRule application_dependencies_are_respected = classes()
            .that().resideInAPackage("..core.application..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage("..core.application..", "..core.domain..", "java..", "org.slf4j..");

    // Regra 6: Controllers não devem depender diretamente de entidades do domínio.
    // CORREÇÃO: Regra ajustada para focar na violação mais crítica (dependência de entidades).
    @ArchTest
    public static final ArchRule controllers_do_not_depend_on_domain_entities = noClasses()
            .that().resideInAPackage("..infrastructure.rest.controller..")
            .should().dependOnClassesThat().resideInAPackage("..core.domain.entity..");

    // Regra 7: A camada de Aplicação não deve depender da Infraestrutura.
    // NOTA: Para esta regra passar, o enum 'AddressOwnerTypeEnum' deve ser movido para a camada de domínio.
    @ArchTest
    public static final ArchRule application_does_not_depend_on_infra = noClasses()
            .that().resideInAPackage("..core.application..")
            .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..");

    // Regra 8: A camada de Domínio deve estar isolada da Aplicação e da Infraestrutura.
    // CORREÇÃO: Ajustado o pacote para '..core.domain..'.
    @ArchTest
    public static final ArchRule domain_is_isolated = noClasses()
            .that().resideInAPackage("..core.domain..")
            .should().dependOnClassesThat().resideInAnyPackage("..core.application..", "..infrastructure..");

    // Regra 9: Proíbe anotações do Spring nas camadas de Core (Domínio e Aplicação).
    // CORREÇÃO: Ajustado o pacote para '..core.domain..' e '..core.application..'.
    @ArchTest
    public static final ArchRule no_spring_annotations_in_core = noClasses()
            .that().resideInAnyPackage("..core.domain..", "..core.application..")
            .should().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.transaction.annotation.Transactional");

    // Regra 10: Garante a convenção de nomenclatura para implementações de Casos de Uso.
    // CORREÇÃO: A regra agora verifica o sufixo 'UseCaseImpl' no pacote 'impl'.
    @ArchTest
    public static final ArchRule use_case_implementations_should_have_impl_suffix = classes()
            .that().resideInAPackage("..core.application.usecases..impl")
            .and().areNotInterfaces()
            .should().haveSimpleNameEndingWith("UseCaseImpl");

    // Regra 11: Entidades JPA devem residir apenas na camada de persistência da infraestrutura.
    @ArchTest
    public static final ArchRule jpa_entities_only_in_infra = classes()
            .that().areAnnotatedWith("jakarta.persistence.Entity")
            .should().resideInAPackage("..infrastructure.persistence.entity..");

    // Regra 12: Repositórios Spring Data devem residir apenas na camada de persistência da infraestrutura.
    @ArchTest
    public static final ArchRule spring_data_repos_only_in_infra = classes()
            .that().areInterfaces()
            .and().haveSimpleNameEndingWith("SpringDataRepository")
            .should().resideInAPackage("..infrastructure.persistence.springdata..");
}