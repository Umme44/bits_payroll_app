import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pro-rata-festival-bonus.test-samples';

import { ProRataFestivalBonusFormService } from './pro-rata-festival-bonus-form.service';

describe('ProRataFestivalBonus Form Service', () => {
  let service: ProRataFestivalBonusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProRataFestivalBonusFormService);
  });

  describe('Service methods', () => {
    describe('createProRataFestivalBonusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProRataFestivalBonusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IProRataFestivalBonus should create a new form with FormGroup', () => {
        const formGroup = service.createProRataFestivalBonusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getProRataFestivalBonus', () => {
      it('should return NewProRataFestivalBonus for default ProRataFestivalBonus initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProRataFestivalBonusFormGroup(sampleWithNewData);

        const proRataFestivalBonus = service.getProRataFestivalBonus(formGroup) as any;

        expect(proRataFestivalBonus).toMatchObject(sampleWithNewData);
      });

      it('should return NewProRataFestivalBonus for empty ProRataFestivalBonus initial value', () => {
        const formGroup = service.createProRataFestivalBonusFormGroup();

        const proRataFestivalBonus = service.getProRataFestivalBonus(formGroup) as any;

        expect(proRataFestivalBonus).toMatchObject({});
      });

      it('should return IProRataFestivalBonus', () => {
        const formGroup = service.createProRataFestivalBonusFormGroup(sampleWithRequiredData);

        const proRataFestivalBonus = service.getProRataFestivalBonus(formGroup) as any;

        expect(proRataFestivalBonus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProRataFestivalBonus should not enable id FormControl', () => {
        const formGroup = service.createProRataFestivalBonusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProRataFestivalBonus should disable id FormControl', () => {
        const formGroup = service.createProRataFestivalBonusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
