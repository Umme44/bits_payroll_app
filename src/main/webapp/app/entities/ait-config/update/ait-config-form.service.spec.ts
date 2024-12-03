import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ait-config.test-samples';

import { AitConfigFormService } from './ait-config-form.service';

describe('AitConfig Form Service', () => {
  let service: AitConfigFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AitConfigFormService);
  });

  describe('Service methods', () => {
    describe('createAitConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAitConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            taxConfig: expect.any(Object),
          })
        );
      });

      it('passing IAitConfig should create a new form with FormGroup', () => {
        const formGroup = service.createAitConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            taxConfig: expect.any(Object),
          })
        );
      });
    });

    describe('getAitConfig', () => {
      it('should return NewAitConfig for default AitConfig initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAitConfigFormGroup(sampleWithNewData);

        const aitConfig = service.getAitConfig(formGroup) as any;

        expect(aitConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewAitConfig for empty AitConfig initial value', () => {
        const formGroup = service.createAitConfigFormGroup();

        const aitConfig = service.getAitConfig(formGroup) as any;

        expect(aitConfig).toMatchObject({});
      });

      it('should return IAitConfig', () => {
        const formGroup = service.createAitConfigFormGroup(sampleWithRequiredData);

        const aitConfig = service.getAitConfig(formGroup) as any;

        expect(aitConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAitConfig should not enable id FormControl', () => {
        const formGroup = service.createAitConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAitConfig should disable id FormControl', () => {
        const formGroup = service.createAitConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
