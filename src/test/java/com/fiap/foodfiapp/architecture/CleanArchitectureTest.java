package com.fiap.foodfiapp.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.fiap.foodfiapp")
public class CleanArchitectureTest {

        // Rule 1: layers and dependencies
        @ArchTest
        public static final ArchRule layered_architecture_is_respected = layeredArchitecture()
                        .consideringOnlyDependenciesInLayers()
                        .layer("Domain").definedBy("..domain..")
                        .layer("Application").definedBy("..application..")
                        .layer("Infrastructure").definedBy("..infrastructure..")
                        .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
                        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure");

        // Rule 2: domain without frameworks
        @ArchTest
        public static final ArchRule domain_without_frameworks = noClasses()
                        .that().resideInAPackage("..domain..")
                        .should().dependOnClassesThat().resideInAnyPackage(
                                        "org.springframework..", "jakarta.persistence..", "javax.persistence..",
                                        "org.hibernate..", "lombok..");

        // Rule 3: application without frameworks
        @ArchTest
        public static final ArchRule application_without_frameworks = noClasses()
                        .that().resideInAPackage("..application..")
                        .should().dependOnClassesThat().resideInAnyPackage(
                                        "org.springframework..", "jakarta.persistence..", "javax.persistence..",
                                        "org.hibernate..", "lombok..");

        // Rule 4: domain only depends on itself and Java (excluding test classes)
        @ArchTest
        public static final ArchRule domain_depends_only_on_itself_and_java = classes()
                        .that().resideInAPackage("..domain..")
                        .and().haveSimpleNameNotEndingWith("Test")
                        .should().onlyDependOnClassesThat()
                        .resideInAnyPackage("..domain..", "java..");

        // Rule 5: application depends only on itself, domain and Java (excluding test
        // classes)
        @ArchTest
        public static final ArchRule application_depends_only_on_itself_domain_and_java = classes()
                        .that().resideInAPackage("..application..")
                        .and().haveSimpleNameNotEndingWith("Test")
                        .should().onlyDependOnClassesThat()
                        .resideInAnyPackage("..application..", "..domain..", "java..");

        // Rule 6: controllers do not depend on domain (except mappers, excluding test
        // classes)
        @ArchTest
        public static final ArchRule controller_does_not_expose_domain = noClasses()
                        .that().resideInAPackage("..infrastructure.rest..")
                        .and().resideOutsideOfPackage("..infrastructure.rest.mapper..")
                        .and().haveSimpleNameNotEndingWith("Test")
                        .should().dependOnClassesThat().resideInAPackage("..domain..");

        // --- Recommended extras ---

        // Application does not depend on infra
        @ArchTest
        public static final ArchRule application_does_not_depend_on_infra = noClasses()
                        .that().resideInAPackage("..application..")
                        .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..");

        // Domain isolated from app/infra
        @ArchTest
        public static final ArchRule domain_isolated = noClasses()
                        .that().resideInAPackage("..domain..")
                        .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..");

        // Forbid Spring annotations in core
        @ArchTest
        public static final ArchRule no_spring_annotations_in_core = noClasses()
                        .that().resideInAnyPackage("..domain..", "..application..")
                        .should().beAnnotatedWith("org.springframework.stereotype.Service")
                        .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
                        .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
                        .orShould().beAnnotatedWith("org.springframework.transaction.annotation.Transactional");

        // UseCases end with UseCase (excluding test classes)
        @ArchTest
        public static final ArchRule usecases_standard_name = classes()
                        .that().resideInAPackage("..application..")
                        .and().areNotInterfaces()
                        .and().haveSimpleNameNotEndingWith("Test")
                        .should().haveSimpleNameEndingWith("UseCase");

        // JPA entities only in infra
        @ArchTest
        public static final ArchRule jpa_entities_only_in_infra = classes()
                        .that().areAnnotatedWith("jakarta.persistence.Entity")
                        .should().resideInAPackage("..infrastructure.persistence..");

        // Spring Data repositories only in infra
        @ArchTest
        public static final ArchRule spring_data_repos_only_in_infra = classes()
                        .that().areInterfaces()
                        .and().haveSimpleNameEndingWith("SpringDataRepository")
                        .should().resideInAPackage("..infrastructure.persistence..");

        // Infra repository implementations implement domain ports (ignoring Spring Data
        // repositories)
        /*@ArchTest
        public static final ArchRule infra_repositories_implement_domain_ports = classes()
                        .that().resideInAPackage("..infrastructure..")
                        .and().haveSimpleNameEndingWith("Repository")
                        .and().doNotImplement(org.springframework.data.repository.Repository.class)
                        .should().implement(resideInAnyPackage("..domain..", "..domain.port.."));*/

        // Controller methods do not return domain entity User directly
        /*@ArchTest
        public static final ArchRule controller_does_not_return_domain = noMethods()
                        .that().areDeclaredInClassesThat()
                        .resideInAPackage("..infrastructure.rest.controller..")
                        .and().areDeclaredInClassesThat().haveSimpleNameEndingWith("Controller")
                        .should().notHaveRawReturnType(com.fiap.foodfiapp.domain.entity.User.class);*/
}
