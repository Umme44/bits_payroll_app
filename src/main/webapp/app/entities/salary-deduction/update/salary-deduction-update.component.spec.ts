import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalaryDeductionFormService } from './salary-deduction-form.service';
import { SalaryDeductionService } from '../service/salary-deduction.service';
import { ISalaryDeduction } from '../salary-deduction.model';
import { IDeductionType } from 'app/entities/deduction-type/deduction-type.model';
import { DeductionTypeService } from 'app/entities/deduction-type/service/deduction-type.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { SalaryDeductionUpdateComponent } from './salary-deduction-update.component';

describe('SalaryDeduction Management Update Component', () => {
  let comp: SalaryDeductionUpdateComponent;
  let fixture: ComponentFixture<SalaryDeductionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salaryDeductionFormService: SalaryDeductionFormService;
  let salaryDeductionService: SalaryDeductionService;
  let deductionTypeService: DeductionTypeService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SalaryDeductionUpdateComponent],
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
      .overrideTemplate(SalaryDeductionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryDeductionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salaryDeductionFormService = TestBed.inject(SalaryDeductionFormService);
    salaryDeductionService = TestBed.inject(SalaryDeductionService);
    deductionTypeService = TestBed.inject(DeductionTypeService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DeductionType query and add missing value', () => {
      const salaryDeduction: ISalaryDeduction = { id: 456 };
      const deductionType: IDeductionType = { id: 70412 };
      salaryDeduction.deductionType = deductionType;

      const deductionTypeCollection: IDeductionType[] = [{ id: 29920 }];
      jest.spyOn(deductionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: deductionTypeCollection })));
      const additionalDeductionTypes = [deductionType];
      const expectedCollection: IDeductionType[] = [...additionalDeductionTypes, ...deductionTypeCollection];
      jest.spyOn(deductionTypeService, 'addDeductionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salaryDeduction });
      comp.ngOnInit();

      expect(deductionTypeService.query).toHaveBeenCalled();
      expect(deductionTypeService.addDeductionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        deductionTypeCollection,
        ...additionalDeductionTypes.map(expect.objectContaining)
      );
      expect(comp.deductionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const salaryDeduction: ISalaryDeduction = { id: 456 };
      const employee: IEmployee = { id: 11389 };
      salaryDeduction.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 58243 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salaryDeduction });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const salaryDeduction: ISalaryDeduction = { id: 456 };
      const deductionType: IDeductionType = { id: 9105 };
      salaryDeduction.deductionType = deductionType;
      const employee: IEmployee = { id: 50466 };
      salaryDeduction.employee = employee;

      activatedRoute.data = of({ salaryDeduction });
      comp.ngOnInit();

      expect(comp.deductionTypesSharedCollection).toContain(deductionType);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.salaryDeduction).toEqual(salaryDeduction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryDeduction>>();
      const salaryDeduction = { id: 123 };
      jest.spyOn(salaryDeductionFormService, 'getSalaryDeduction').mockReturnValue(salaryDeduction);
      jest.spyOn(salaryDeductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryDeduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryDeduction }));
      saveSubject.complete();

      // THEN
      expect(salaryDeductionFormService.getSalaryDeduction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salaryDeductionService.update).toHaveBeenCalledWith(expect.objectContaining(salaryDeduction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryDeduction>>();
      const salaryDeduction = { id: 123 };
      jest.spyOn(salaryDeductionFormService, 'getSalaryDeduction').mockReturnValue({ id: null });
      jest.spyOn(salaryDeductionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryDeduction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryDeduction }));
      saveSubject.complete();

      // THEN
      expect(salaryDeductionFormService.getSalaryDeduction).toHaveBeenCalled();
      expect(salaryDeductionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryDeduction>>();
      const salaryDeduction = { id: 123 };
      jest.spyOn(salaryDeductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryDeduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salaryDeductionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDeductionType', () => {
      it('Should forward to deductionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deductionTypeService, 'compareDeductionType');
        comp.compareDeductionType(entity, entity2);
        expect(deductionTypeService.compareDeductionType).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
