import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeResignationFormService } from './employee-resignation-form.service';
import { EmployeeResignationService } from '../service/employee-resignation.service';
import { IEmployeeResignation } from '../employee-resignation.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { EmployeeResignationUpdateComponent } from './employee-resignation-update.component';

describe('EmployeeResignation Management Update Component', () => {
  let comp: EmployeeResignationUpdateComponent;
  let fixture: ComponentFixture<EmployeeResignationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeResignationFormService: EmployeeResignationFormService;
  let employeeResignationService: EmployeeResignationService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeResignationUpdateComponent],
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
      .overrideTemplate(EmployeeResignationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeResignationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeResignationFormService = TestBed.inject(EmployeeResignationFormService);
    employeeResignationService = TestBed.inject(EmployeeResignationService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeResignation: IEmployeeResignation = { id: 456 };
      const createdBy: IEmployee = { id: 95652 };
      employeeResignation.createdBy = createdBy;
      const uodatedBy: IEmployee = { id: 79588 };
      employeeResignation.uodatedBy = uodatedBy;
      const employee: IEmployee = { id: 95513 };
      employeeResignation.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 91900 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [createdBy, uodatedBy, employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeResignation });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeResignation: IEmployeeResignation = { id: 456 };
      const createdBy: IEmployee = { id: 38379 };
      employeeResignation.createdBy = createdBy;
      const uodatedBy: IEmployee = { id: 80432 };
      employeeResignation.uodatedBy = uodatedBy;
      const employee: IEmployee = { id: 78892 };
      employeeResignation.employee = employee;

      activatedRoute.data = of({ employeeResignation });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(createdBy);
      expect(comp.employeesSharedCollection).toContain(uodatedBy);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeeResignation).toEqual(employeeResignation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeResignation>>();
      const employeeResignation = { id: 123 };
      jest.spyOn(employeeResignationFormService, 'getEmployeeResignation').mockReturnValue(employeeResignation);
      jest.spyOn(employeeResignationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeResignation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeResignation }));
      saveSubject.complete();

      // THEN
      expect(employeeResignationFormService.getEmployeeResignation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeResignationService.update).toHaveBeenCalledWith(expect.objectContaining(employeeResignation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeResignation>>();
      const employeeResignation = { id: 123 };
      jest.spyOn(employeeResignationFormService, 'getEmployeeResignation').mockReturnValue({ id: null });
      jest.spyOn(employeeResignationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeResignation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeResignation }));
      saveSubject.complete();

      // THEN
      expect(employeeResignationFormService.getEmployeeResignation).toHaveBeenCalled();
      expect(employeeResignationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeResignation>>();
      const employeeResignation = { id: 123 };
      jest.spyOn(employeeResignationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeResignation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeResignationService.update).toHaveBeenCalled();
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
        /* comp.compareEmployee(entity, entity2); */
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
