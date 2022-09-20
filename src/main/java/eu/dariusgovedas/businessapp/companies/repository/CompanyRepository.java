package eu.dariusgovedas.businessapp.companies.repository;

import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("FROM Company c WHERE c.companyID=:id OR UPPER(c.companyName) LIKE %:name% OR UPPER(c.registrationAddress.country) LIKE %:country% OR UPPER(c.registrationAddress.city) LIKE %:city%")
    List<Company> findCompanies(
            @Param("id") Long companyID,
            @Param("name") String companyName,
            @Param("country") String country,
            @Param("city") String city
    );

    Company findByCompanyID(Long businessID);

    List<Company> findByCompanyNameContainingIgnoreCase(String businessName);

    @Query("FROM Company c WHERE UPPER(c.registrationAddress.country) LIKE %:country%")
    List<Company> findByCountry(@Param("country") String country);

    @Query("FROM Company c WHERE UPPER(c.registrationAddress.city) LIKE %:city%")
    List<Company> findByCity(@Param("city") String city);

    List<Company> findByCompanyType(CompanyType type);

    Company findByCompanyName(String clientName);

}

