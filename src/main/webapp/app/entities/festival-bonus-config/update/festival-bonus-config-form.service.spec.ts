import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../festival-bonus-config.test-samples';

import { FestivalBonusConfigFormService } from './festival-bonus-config-form.service';

describe('FestivalBonusConfig Form Service', () => {
  let service: FestivalBonusConfigFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FestivalBonusConfigFormService);
  });

  describe('Service methods', () => {
    describe('createFestivalBonusConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFestivalBonusConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeCategory: expect.any(Object),
            percentageFromGross: expect.any(Object),
          })
        );
      });

      it('passing IFestivalBonusConfig should create a new form with FormGroup', () => {
        const formGroup = service.createFestivalBonusConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeCategory: expect.any(Object),
            percentageFromGross: expect.any(Object),
          })
        );
      });
    });

    describe('getFestivalBonusConfig', () => {
      it('should return NewFestivalBonusConfig for default FestivalBonusConfig initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFestivalBonusConfigFormGroup(sampleWithNewData);

        const festivalBonusConfig = service.getFestivalBonusConfig(formGroup) as any;

        expect(festivalBonusConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewFestivalBonusConfig for empty FestivalBonusConfig initial value', () => {
        const formGroup = service.createFestivalBonusConfigFormGroup();

        const festivalBonusConfig = service.getFestivalBonusConfig(formGroup) as any;

        expect(festivalBonusConfig).toMatchObject({});
      });

      it('should return IFestivalBonusConfig', () => {
        const formGroup = service.createFestivalBonusConfigFormGroup(sampleWithRequiredData);

        const festivalBonusConfig = service.getFestivalBonusConfig(formGroup) as any;

        expect(festivalBonusConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFestivalBonusConfig should not enable id FormControl', () => {
        const formGroup = service.createFestivalBonusConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFestivalBonusConfig should disable id FormControl', () => {
        const formGroup = service.createFestivalBonusConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
