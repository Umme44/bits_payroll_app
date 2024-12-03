package com.bits.hr.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;
    private GitProperties gitProperties;
    private BuildProperties buildProperties;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.bits.hr.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.bits.hr.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.bits.hr.domain.User.class.getName());
            createCache(cm, com.bits.hr.domain.Authority.class.getName());
            createCache(cm, com.bits.hr.domain.User.class.getName() + ".authorities");
            createCache(cm, com.bits.hr.domain.Designation.class.getName());
            createCache(cm, com.bits.hr.domain.Nationality.class.getName());
            createCache(cm, com.bits.hr.domain.BankBranch.class.getName());
            createCache(cm, com.bits.hr.domain.UnitOfMeasurement.class.getName());
            createCache(cm, com.bits.hr.domain.TimeSlot.class.getName());
            createCache(cm, com.bits.hr.domain.Building.class.getName());
            createCache(cm, com.bits.hr.domain.Floor.class.getName());
            createCache(cm, com.bits.hr.domain.RoomType.class.getName());
            createCache(cm, com.bits.hr.domain.Room.class.getName());
            createCache(cm, com.bits.hr.domain.Unit.class.getName());
            createCache(cm, com.bits.hr.domain.Band.class.getName());
            createCache(cm, com.bits.hr.domain.Department.class.getName());
            createCache(cm, com.bits.hr.domain.Employee.class.getName());
            createCache(cm, com.bits.hr.domain.ArrearSalaryMaster.class.getName());
            createCache(cm, com.bits.hr.domain.ArrearSalaryItem.class.getName());
            createCache(cm, com.bits.hr.domain.Attendance.class.getName());
            createCache(cm, com.bits.hr.domain.AttendanceEntry.class.getName());
            createCache(cm, com.bits.hr.domain.AttendanceSummary.class.getName());
            createCache(cm, com.bits.hr.domain.EducationDetails.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeeNOC.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeePin.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeePinConfiguration.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeeResignation.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeeSalary.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeeSalaryTempData.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeeStaticFile.class.getName());
            createCache(cm, com.bits.hr.domain.EmploymentCertificate.class.getName());
            createCache(cm, com.bits.hr.domain.EmploymentHistory.class.getName());
            createCache(cm, com.bits.hr.domain.EventLog.class.getName());
            createCache(cm, com.bits.hr.domain.Festival.class.getName());
            createCache(cm, com.bits.hr.domain.FestivalBonusDetails.class.getName());
            createCache(cm, com.bits.hr.domain.FinalSettlement.class.getName());
            createCache(cm, com.bits.hr.domain.FlexSchedule.class.getName());
            createCache(cm, com.bits.hr.domain.FlexScheduleApplication.class.getName());
            createCache(cm, com.bits.hr.domain.HoldFbDisbursement.class.getName());
            createCache(cm, com.bits.hr.domain.HoldSalaryDisbursement.class.getName());
            createCache(cm, com.bits.hr.domain.AitConfig.class.getName());
            createCache(cm, com.bits.hr.domain.AitPayment.class.getName());
            createCache(cm, com.bits.hr.domain.IncomeTaxChallan.class.getName());
            createCache(cm, com.bits.hr.domain.InsuranceRegistration.class.getName());
            createCache(cm, com.bits.hr.domain.InsuranceClaim.class.getName());
            createCache(cm, com.bits.hr.domain.PfAccount.class.getName());
            createCache(cm, com.bits.hr.domain.PfCollection.class.getName());
            createCache(cm, com.bits.hr.domain.PfLoanApplication.class.getName());
            createCache(cm, com.bits.hr.domain.PfLoan.class.getName());
            createCache(cm, com.bits.hr.domain.PfLoanRepayment.class.getName());
            createCache(cm, com.bits.hr.domain.PfNominee.class.getName());
            createCache(cm, com.bits.hr.domain.ItemInformation.class.getName());
            createCache(cm, com.bits.hr.domain.ProcReqMaster.class.getName());
            createCache(cm, com.bits.hr.domain.ProcReq.class.getName());
            createCache(cm, com.bits.hr.domain.ProcReqMaster.class.getName() + ".procReqs");
            createCache(cm, com.bits.hr.domain.ProRataFestivalBonus.class.getName());
            createCache(cm, com.bits.hr.domain.RecruitmentRequisitionForm.class.getName());
            createCache(cm, com.bits.hr.domain.References.class.getName());
            createCache(cm, com.bits.hr.domain.DeductionType.class.getName());
            createCache(cm, com.bits.hr.domain.SalaryCertificate.class.getName());
            createCache(cm, com.bits.hr.domain.SalaryDeduction.class.getName());
            createCache(cm, com.bits.hr.domain.SpecialShiftTiming.class.getName());
            createCache(cm, com.bits.hr.domain.TaxAcknowledgementReceipt.class.getName());
            createCache(cm, com.bits.hr.domain.TrainingHistory.class.getName());
            createCache(cm, com.bits.hr.domain.Vehicle.class.getName());
            createCache(cm, com.bits.hr.domain.VehicleRequisition.class.getName());
            createCache(cm, com.bits.hr.domain.UserFeedback.class.getName());
            createCache(cm, com.bits.hr.domain.Area.class.getName());
            createCache(cm, com.bits.hr.domain.ArrearPayment.class.getName());
            createCache(cm, com.bits.hr.domain.ArrearSalary.class.getName());
            createCache(cm, com.bits.hr.domain.AttendanceSyncCache.class.getName());
            createCache(cm, com.bits.hr.domain.Config.class.getName());
            createCache(cm, com.bits.hr.domain.CalenderYear.class.getName());
            createCache(cm, com.bits.hr.domain.FestivalBonusConfig.class.getName());
            createCache(cm, com.bits.hr.domain.FileTemplates.class.getName());
            createCache(cm, com.bits.hr.domain.Holidays.class.getName());
            createCache(cm, com.bits.hr.domain.LeaveBalance.class.getName());
            createCache(cm, com.bits.hr.domain.WorkFromHomeApplication.class.getName());
            createCache(cm, com.bits.hr.domain.IndividualArrearSalary.class.getName());
            createCache(cm, com.bits.hr.domain.WorkingExperience.class.getName());
            createCache(cm, com.bits.hr.domain.PfArrear.class.getName());
            createCache(cm, com.bits.hr.domain.LeaveAllocation.class.getName());
            createCache(cm, com.bits.hr.domain.MobileBill.class.getName());
            createCache(cm, com.bits.hr.domain.SalaryGeneratorMaster.class.getName());
            createCache(cm, com.bits.hr.domain.Offer.class.getName());
            createCache(cm, com.bits.hr.domain.OfficeNotices.class.getName());
            createCache(cm, com.bits.hr.domain.MovementEntry.class.getName());
            createCache(cm, com.bits.hr.domain.RoomRequisition.class.getName());
            createCache(cm, com.bits.hr.domain.LeaveApplication.class.getName());
            createCache(cm, com.bits.hr.domain.Organization.class.getName());
            createCache(cm, com.bits.hr.domain.InsuranceConfiguration.class.getName());
            createCache(cm, com.bits.hr.domain.ManualAttendanceEntry.class.getName());
            createCache(cm, com.bits.hr.domain.Nominee.class.getName());
            createCache(cm, com.bits.hr.domain.FailedLoginAttempt.class.getName());
            createCache(cm, com.bits.hr.domain.Location.class.getName());
            createCache(cm, com.bits.hr.domain.EmployeeDocument.class.getName());
            createCache(cm, com.bits.hr.domain.RecruitmentRequisitionBudget.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
