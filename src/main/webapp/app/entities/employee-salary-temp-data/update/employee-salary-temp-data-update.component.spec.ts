import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeSalaryTempDataFormService } from './employee-salary-temp-data-form.service';
import { EmployeeSalaryTempDataService } from '../service/employee-salary-temp-data.service';
import { IEmployeeSalaryTempData } from '../employee-salary-temp-data.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { EmployeeSalaryTempDataUpdateComponent } from './employee-salary-temp-data-update.component';

describe('EmployeeSalaryTempData Management Update Component', () => {
  let comp: EmployeeSalaryTempDataUpdateComponent;
  let fixture: ComponentFixture<EmployeeSalaryTempDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeSalaryTempDataFormService: EmployeeSalaryTempDataFormService;
  let employeeSalaryTempDataService: EmployeeSalaryTempDataService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeSalaryTempDataUpdateComponent],
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
      .overrideTemplate(EmployeeSalaryTempDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeSalaryTempDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeSalaryTempDataFormService = TestBed.inject(EmployeeSalaryTempDataFormService);
    employeeSalaryTempDataService = TestBed.inject(EmployeeSalaryTempDataService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeSalaryTempData: IEmployeeSalaryTempData = { id: 456 };
      const employee: IEmployee = { id: 87910 };
      employeeSalaryTempData.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 12339 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeSalaryTempData });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeSalaryTempData: IEmployeeSalaryTempData = { id: 456 };
      const employee: IEmployee = { id: 66213 };
      employeeSalaryTempData.employee = employee;

      activatedRoute.data = of({ employeeSalaryTempData });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeeSalaryTempData).toEqual(employeeSalaryTempData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeSalaryTempData>>();
      const employeeSalaryTempData = { id: 123 };
      jest.spyOn(employeeSalaryTempDataFormService, 'getEmployeeSalaryTempData').mockReturnValue(employeeSalaryTempData);
      jest.spyOn(employeeSalaryTempDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeSalaryTempData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeSalaryTempData }));
      saveSubject.complete();

      // THEN
      expect(employeeSalaryTempDataFormService.getEmployeeSalaryTempData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeSalaryTempDataService.update).toHaveBeenCalledWith(expect.objectContaining(employeeSalaryTempData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeSalaryTempData>>();
      const employeeSalaryTempData = { id: 123 };
      jest.spyOn(employeeSalaryTempDataFormService, 'getEmployeeSalaryTempData').mockReturnValue({ id: null });
      jest.spyOn(employeeSalaryTempDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeSalaryTempData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeSalaryTempData }));
      saveSubject.complete();

      // THEN
      expect(employeeSalaryTempDataFormService.getEmployeeSalaryTempData).toHaveBeenCalled();
      expect(employeeSalaryTempDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeSalaryTempData>>();
      const employeeSalaryTempData = { id: 123 };
      jest.spyOn(employeeSalaryTempDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeSalaryTempData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeSalaryTempDataService.update).toHaveBeenCalled();
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
