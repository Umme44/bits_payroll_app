import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../calender-year.test-samples';

import { CalenderYearFormService } from './calender-year-form.service';

describe('CalenderYear Form Service', () => {
  let service: CalenderYearFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalenderYearFormService);
  });

  describe('Service methods', () => {
    describe('createCalenderYearFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCalenderYearFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
          })
        );
      });

      it('passing ICalenderYear should create a new form with FormGroup', () => {
        const formGroup = service.createCalenderYearFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
          })
        );
      });
    });

    describe('getCalenderYear', () => {
      it('should return NewCalenderYear for default CalenderYear initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCalenderYearFormGroup(sampleWithNewData);

        const calenderYear = service.getCalenderYear(formGroup) as any;

        expect(calenderYear).toMatchObject(sampleWithNewData);
      });

      it('should return NewCalenderYear for empty CalenderYear initial value', () => {
        const formGroup = service.createCalenderYearFormGroup();

        const calenderYear = service.getCalenderYear(formGroup) as any;

        expect(calenderYear).toMatchObject({});
      });

      it('should return ICalenderYear', () => {
        const formGroup = service.createCalenderYearFormGroup(sampleWithRequiredData);

        const calenderYear = service.getCalenderYear(formGroup) as any;

        expect(calenderYear).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICalenderYear should not enable id FormControl', () => {
        const formGroup = service.createCalenderYearFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCalenderYear should disable id FormControl', () => {
        const formGroup = service.createCalenderYearFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
