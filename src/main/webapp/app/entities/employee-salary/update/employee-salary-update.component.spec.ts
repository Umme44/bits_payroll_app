import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeSalaryFormService } from './employee-salary-form.service';
import { EmployeeSalaryService } from '../service/employee-salary.service';
import { IEmployeeSalary } from '../employee-salary.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { EmployeeSalaryUpdateComponent } from './employee-salary-update.component';

describe('EmployeeSalary Management Update Component', () => {
  let comp: EmployeeSalaryUpdateComponent;
  let fixture: ComponentFixture<EmployeeSalaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeSalaryFormService: EmployeeSalaryFormService;
  let employeeSalaryService: EmployeeSalaryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeSalaryUpdateComponent],
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
      .overrideTemplate(EmployeeSalaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeSalaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeSalaryFormService = TestBed.inject(EmployeeSalaryFormService);
    employeeSalaryService = TestBed.inject(EmployeeSalaryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeSalary: IEmployeeSalary = { id: 456 };
      const employee: IEmployee = { id: 10877 };
      employeeSalary.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 55720 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeSalary });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeSalary: IEmployeeSalary = { id: 456 };
      const employee: IEmployee = { id: 3969 };
      employeeSalary.employee = employee;

      activatedRoute.data = of({ employeeSalary });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeeSalary).toEqual(employeeSalary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeSalary>>();
      const employeeSalary = { id: 123 };
      jest.spyOn(employeeSalaryFormService, 'getEmployeeSalary').mockReturnValue(employeeSalary);
      jest.spyOn(employeeSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeSalary }));
      saveSubject.complete();

      // THEN
      expect(employeeSalaryFormService.getEmployeeSalary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeSalaryService.update).toHaveBeenCalledWith(expect.objectContaining(employeeSalary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeSalary>>();
      const employeeSalary = { id: 123 };
      jest.spyOn(employeeSalaryFormService, 'getEmployeeSalary').mockReturnValue({ id: null });
      jest.spyOn(employeeSalaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeSalary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeSalary }));
      saveSubject.complete();

      // THEN
      expect(employeeSalaryFormService.getEmployeeSalary).toHaveBeenCalled();
      expect(employeeSalaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeSalary>>();
      const employeeSalary = { id: 123 };
      jest.spyOn(employeeSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeSalaryService.update).toHaveBeenCalled();
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
