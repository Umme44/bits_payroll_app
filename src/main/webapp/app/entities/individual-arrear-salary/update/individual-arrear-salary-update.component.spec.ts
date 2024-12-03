import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IndividualArrearSalaryFormService } from './individual-arrear-salary-form.service';
import { IndividualArrearSalaryService } from '../service/individual-arrear-salary.service';
import { IIndividualArrearSalary } from '../individual-arrear-salary.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IndividualArrearSalaryUpdateComponent } from './individual-arrear-salary-update.component';

describe('IndividualArrearSalary Management Update Component', () => {
  let comp: IndividualArrearSalaryUpdateComponent;
  let fixture: ComponentFixture<IndividualArrearSalaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let individualArrearSalaryFormService: IndividualArrearSalaryFormService;
  let individualArrearSalaryService: IndividualArrearSalaryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IndividualArrearSalaryUpdateComponent],
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
      .overrideTemplate(IndividualArrearSalaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IndividualArrearSalaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    individualArrearSalaryFormService = TestBed.inject(IndividualArrearSalaryFormService);
    individualArrearSalaryService = TestBed.inject(IndividualArrearSalaryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const individualArrearSalary: IIndividualArrearSalary = { id: 456 };
      const employee: IEmployee = { id: 46055 };
      individualArrearSalary.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 51188 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ individualArrearSalary });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const individualArrearSalary: IIndividualArrearSalary = { id: 456 };
      const employee: IEmployee = { id: 68166 };
      individualArrearSalary.employee = employee;

      activatedRoute.data = of({ individualArrearSalary });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.individualArrearSalary).toEqual(individualArrearSalary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIndividualArrearSalary>>();
      const individualArrearSalary = { id: 123 };
      jest.spyOn(individualArrearSalaryFormService, 'getIndividualArrearSalary').mockReturnValue(individualArrearSalary);
      jest.spyOn(individualArrearSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ individualArrearSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: individualArrearSalary }));
      saveSubject.complete();

      // THEN
      expect(individualArrearSalaryFormService.getIndividualArrearSalary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(individualArrearSalaryService.update).toHaveBeenCalledWith(expect.objectContaining(individualArrearSalary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIndividualArrearSalary>>();
      const individualArrearSalary = { id: 123 };
      jest.spyOn(individualArrearSalaryFormService, 'getIndividualArrearSalary').mockReturnValue({ id: null });
      jest.spyOn(individualArrearSalaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ individualArrearSalary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: individualArrearSalary }));
      saveSubject.complete();

      // THEN
      expect(individualArrearSalaryFormService.getIndividualArrearSalary).toHaveBeenCalled();
      expect(individualArrearSalaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIndividualArrearSalary>>();
      const individualArrearSalary = { id: 123 };
      jest.spyOn(individualArrearSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ individualArrearSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(individualArrearSalaryService.update).toHaveBeenCalled();
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
