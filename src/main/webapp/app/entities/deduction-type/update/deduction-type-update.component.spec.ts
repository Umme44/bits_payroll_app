import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeductionTypeFormService } from './deduction-type-form.service';
import { DeductionTypeService } from '../service/deduction-type.service';
import { IDeductionType } from '../deduction-type.model';

import { DeductionTypeUpdateComponent } from './deduction-type-update.component';

describe('DeductionType Management Update Component', () => {
  let comp: DeductionTypeUpdateComponent;
  let fixture: ComponentFixture<DeductionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deductionTypeFormService: DeductionTypeFormService;
  let deductionTypeService: DeductionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeductionTypeUpdateComponent],
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
      .overrideTemplate(DeductionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeductionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deductionTypeFormService = TestBed.inject(DeductionTypeFormService);
    deductionTypeService = TestBed.inject(DeductionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deductionType: IDeductionType = { id: 456 };

      activatedRoute.data = of({ deductionType });
      comp.ngOnInit();

      expect(comp.deductionType).toEqual(deductionType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeductionType>>();
      const deductionType = { id: 123 };
      jest.spyOn(deductionTypeFormService, 'getDeductionType').mockReturnValue(deductionType);
      jest.spyOn(deductionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deductionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deductionType }));
      saveSubject.complete();

      // THEN
      expect(deductionTypeFormService.getDeductionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deductionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(deductionType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeductionType>>();
      const deductionType = { id: 123 };
      jest.spyOn(deductionTypeFormService, 'getDeductionType').mockReturnValue({ id: null });
      jest.spyOn(deductionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deductionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deductionType }));
      saveSubject.complete();

      // THEN
      expect(deductionTypeFormService.getDeductionType).toHaveBeenCalled();
      expect(deductionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeductionType>>();
      const deductionType = { id: 123 };
      jest.spyOn(deductionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deductionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deductionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
