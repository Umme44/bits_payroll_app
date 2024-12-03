import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InsuranceConfigurationFormService } from './insurance-configuration-form.service';
import { InsuranceConfigurationService } from '../service/insurance-configuration.service';
import { IInsuranceConfiguration } from '../insurance-configuration.model';

import { InsuranceConfigurationUpdateComponent } from './insurance-configuration-update.component';

describe('InsuranceConfiguration Management Update Component', () => {
  let comp: InsuranceConfigurationUpdateComponent;
  let fixture: ComponentFixture<InsuranceConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insuranceConfigurationFormService: InsuranceConfigurationFormService;
  let insuranceConfigurationService: InsuranceConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InsuranceConfigurationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InsuranceConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsuranceConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insuranceConfigurationFormService = TestBed.inject(InsuranceConfigurationFormService);
    insuranceConfigurationService = TestBed.inject(InsuranceConfigurationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const insuranceConfiguration: IInsuranceConfiguration = { id: 456 };

      activatedRoute.data = of({ insuranceConfiguration });
      comp.ngOnInit();

      expect(comp.insuranceConfiguration).toEqual(insuranceConfiguration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceConfiguration>>();
      const insuranceConfiguration = { id: 123 };
      jest.spyOn(insuranceConfigurationFormService, 'getInsuranceConfiguration').mockReturnValue(insuranceConfiguration);
      jest.spyOn(insuranceConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceConfiguration }));
      saveSubject.complete();

      // THEN
      expect(insuranceConfigurationFormService.getInsuranceConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insuranceConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(insuranceConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceConfiguration>>();
      const insuranceConfiguration = { id: 123 };
      jest.spyOn(insuranceConfigurationFormService, 'getInsuranceConfiguration').mockReturnValue({ id: null });
      jest.spyOn(insuranceConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceConfiguration }));
      saveSubject.complete();

      // THEN
      expect(insuranceConfigurationFormService.getInsuranceConfiguration).toHaveBeenCalled();
      expect(insuranceConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceConfiguration>>();
      const insuranceConfiguration = { id: 123 };
      jest.spyOn(insuranceConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insuranceConfigurationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
