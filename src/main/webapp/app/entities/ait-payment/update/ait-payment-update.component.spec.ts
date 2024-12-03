import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AitPaymentFormService } from './ait-payment-form.service';
import { AitPaymentService } from '../service/ait-payment.service';
import { IAitPayment } from '../ait-payment.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { AitPaymentUpdateComponent } from './ait-payment-update.component';

describe('AitPayment Management Update Component', () => {
  let comp: AitPaymentUpdateComponent;
  let fixture: ComponentFixture<AitPaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aitPaymentFormService: AitPaymentFormService;
  let aitPaymentService: AitPaymentService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AitPaymentUpdateComponent],
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
      .overrideTemplate(AitPaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AitPaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aitPaymentFormService = TestBed.inject(AitPaymentFormService);
    aitPaymentService = TestBed.inject(AitPaymentService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const aitPayment: IAitPayment = { id: 456 };
      const employee: IEmployee = { id: 1094 };
      aitPayment.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 41036 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aitPayment });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aitPayment: IAitPayment = { id: 456 };
      const employee: IEmployee = { id: 51051 };
      aitPayment.employee = employee;

      activatedRoute.data = of({ aitPayment });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.aitPayment).toEqual(aitPayment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAitPayment>>();
      const aitPayment = { id: 123 };
      jest.spyOn(aitPaymentFormService, 'getAitPayment').mockReturnValue(aitPayment);
      jest.spyOn(aitPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aitPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aitPayment }));
      saveSubject.complete();

      // THEN
      expect(aitPaymentFormService.getAitPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aitPaymentService.update).toHaveBeenCalledWith(expect.objectContaining(aitPayment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAitPayment>>();
      const aitPayment = { id: 123 };
      jest.spyOn(aitPaymentFormService, 'getAitPayment').mockReturnValue({ id: null });
      jest.spyOn(aitPaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aitPayment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aitPayment }));
      saveSubject.complete();

      // THEN
      expect(aitPaymentFormService.getAitPayment).toHaveBeenCalled();
      expect(aitPaymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAitPayment>>();
      const aitPayment = { id: 123 };
      jest.spyOn(aitPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aitPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aitPaymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
