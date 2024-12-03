import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArrearSalaryFormService } from './arrear-salary-form.service';
import { ArrearSalaryService } from '../service/arrear-salary.service';
import { IArrearSalary } from '../arrear-salary.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ArrearSalaryUpdateComponent } from './arrear-salary-update.component';

describe('ArrearSalary Management Update Component', () => {
  let comp: ArrearSalaryUpdateComponent;
  let fixture: ComponentFixture<ArrearSalaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let arrearSalaryFormService: ArrearSalaryFormService;
  let arrearSalaryService: ArrearSalaryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArrearSalaryUpdateComponent],
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
      .overrideTemplate(ArrearSalaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArrearSalaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    arrearSalaryFormService = TestBed.inject(ArrearSalaryFormService);
    arrearSalaryService = TestBed.inject(ArrearSalaryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const arrearSalary: IArrearSalary = { id: 456 };
      const employee: IEmployee = { id: 61448 };
      arrearSalary.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 49852 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ arrearSalary });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const arrearSalary: IArrearSalary = { id: 456 };
      const employee: IEmployee = { id: 50210 };
      arrearSalary.employee = employee;

      activatedRoute.data = of({ arrearSalary });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.arrearSalary).toEqual(arrearSalary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalary>>();
      const arrearSalary = { id: 123 };
      jest.spyOn(arrearSalaryFormService, 'getArrearSalary').mockReturnValue(arrearSalary);
      jest.spyOn(arrearSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearSalary }));
      saveSubject.complete();

      // THEN
      expect(arrearSalaryFormService.getArrearSalary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(arrearSalaryService.update).toHaveBeenCalledWith(expect.objectContaining(arrearSalary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalary>>();
      const arrearSalary = { id: 123 };
      jest.spyOn(arrearSalaryFormService, 'getArrearSalary').mockReturnValue({ id: null });
      jest.spyOn(arrearSalaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearSalary }));
      saveSubject.complete();

      // THEN
      expect(arrearSalaryFormService.getArrearSalary).toHaveBeenCalled();
      expect(arrearSalaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalary>>();
      const arrearSalary = { id: 123 };
      jest.spyOn(arrearSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(arrearSalaryService.update).toHaveBeenCalled();
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
