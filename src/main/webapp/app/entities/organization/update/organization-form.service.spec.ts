import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization.test-samples';

import { OrganizationFormService } from './organization-form.service';

describe('Organization Form Service', () => {
  let service: OrganizationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            shortName: expect.any(Object),
            fullName: expect.any(Object),
            slogan: expect.any(Object),
            domainName: expect.any(Object),
            emailAddress: expect.any(Object),
            hrEmailAddress: expect.any(Object),
            noReplyEmailAddress: expect.any(Object),
            contactNumber: expect.any(Object),
            financeManagerPIN: expect.any(Object),
            financeManagerSignature: expect.any(Object),
            logo: expect.any(Object),
            documentLetterHead: expect.any(Object),
            pfStatementLetterHead: expect.any(Object),
            taxStatementLetterHead: expect.any(Object),
            nomineeLetterHead: expect.any(Object),
            salaryPayslipLetterHead: expect.any(Object),
            festivalBonusPayslipLetterHead: expect.any(Object),
            recruitmentRequisitionLetterHead: expect.any(Object),
            hasOrganizationStamp: expect.any(Object),
            organizationStamp: expect.any(Object),
            linkedin: expect.any(Object),
            twitter: expect.any(Object),
            facebook: expect.any(Object),
            youtube: expect.any(Object),
            instagram: expect.any(Object),
            whatsapp: expect.any(Object),
          })
        );
      });

      it('passing IOrganization should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            shortName: expect.any(Object),
            fullName: expect.any(Object),
            slogan: expect.any(Object),
            domainName: expect.any(Object),
            emailAddress: expect.any(Object),
            hrEmailAddress: expect.any(Object),
            noReplyEmailAddress: expect.any(Object),
            contactNumber: expect.any(Object),
            financeManagerPIN: expect.any(Object),
            financeManagerSignature: expect.any(Object),
            logo: expect.any(Object),
            documentLetterHead: expect.any(Object),
            pfStatementLetterHead: expect.any(Object),
            taxStatementLetterHead: expect.any(Object),
            nomineeLetterHead: expect.any(Object),
            salaryPayslipLetterHead: expect.any(Object),
            festivalBonusPayslipLetterHead: expect.any(Object),
            recruitmentRequisitionLetterHead: expect.any(Object),
            hasOrganizationStamp: expect.any(Object),
            organizationStamp: expect.any(Object),
            linkedin: expect.any(Object),
            twitter: expect.any(Object),
            facebook: expect.any(Object),
            youtube: expect.any(Object),
            instagram: expect.any(Object),
            whatsapp: expect.any(Object),
          })
        );
      });
    });

    describe('getOrganization', () => {
      it('should return NewOrganization for default Organization initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrganizationFormGroup(sampleWithNewData);

        const organization = service.getOrganization(formGroup) as any;

        expect(organization).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganization for empty Organization initial value', () => {
        const formGroup = service.createOrganizationFormGroup();

        const organization = service.getOrganization(formGroup) as any;

        expect(organization).toMatchObject({});
      });

      it('should return IOrganization', () => {
        const formGroup = service.createOrganizationFormGroup(sampleWithRequiredData);

        const organization = service.getOrganization(formGroup) as any;

        expect(organization).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganization should not enable id FormControl', () => {
        const formGroup = service.createOrganizationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganization should disable id FormControl', () => {
        const formGroup = service.createOrganizationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
