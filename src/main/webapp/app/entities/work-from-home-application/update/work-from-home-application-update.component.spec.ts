import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkFromHomeApplicationFormService } from './work-from-home-application-form.service';
import { WorkFromHomeApplicationService } from '../service/work-from-home-application.service';
import { IWorkFromHomeApplication } from '../work-from-home-application.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { WorkFromHomeApplicationUpdateComponent } from './work-from-home-application-update.component';

describe('WorkFromHomeApplication Management Update Component', () => {
  let comp: WorkFromHomeApplicationUpdateComponent;
  let fixture: ComponentFixture<WorkFromHomeApplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workFromHomeApplicationFormService: WorkFromHomeApplicationFormService;
  let workFromHomeApplicationService: WorkFromHomeApplicationService;
  let userService: UserService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WorkFromHomeApplicationUpdateComponent],
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
      .overrideTemplate(WorkFromHomeApplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkFromHomeApplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workFromHomeApplicationFormService = TestBed.inject(WorkFromHomeApplicationFormService);
    workFromHomeApplicationService = TestBed.inject(WorkFromHomeApplicationService);
    userService = TestBed.inject(UserService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const workFromHomeApplication: IWorkFromHomeApplication = { id: 456 };
      const appliedBy: IUser = { id: 3786 };
      workFromHomeApplication.appliedBy = appliedBy;
      const createdBy: IUser = { id: 91019 };
      workFromHomeApplication.createdBy = createdBy;
      const updatedBy: IUser = { id: 62421 };
      workFromHomeApplication.updatedBy = updatedBy;
      const sanctionedBy: IUser = { id: 15342 };
      workFromHomeApplication.sanctionedBy = sanctionedBy;

      const userCollection: IUser[] = [{ id: 26532 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [appliedBy, createdBy, updatedBy, sanctionedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workFromHomeApplication });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const workFromHomeApplication: IWorkFromHomeApplication = { id: 456 };
      const employee: IEmployee = { id: 93091 };
      workFromHomeApplication.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 26902 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workFromHomeApplication });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workFromHomeApplication: IWorkFromHomeApplication = { id: 456 };
      const appliedBy: IUser = { id: 99792 };
      workFromHomeApplication.appliedBy = appliedBy;
      const createdBy: IUser = { id: 3844 };
      workFromHomeApplication.createdBy = createdBy;
      const updatedBy: IUser = { id: 92190 };
      workFromHomeApplication.updatedBy = updatedBy;
      const sanctionedBy: IUser = { id: 54497 };
      workFromHomeApplication.sanctionedBy = sanctionedBy;
      const employee: IEmployee = { id: 37090 };
      workFromHomeApplication.employee = employee;

      activatedRoute.data = of({ workFromHomeApplication });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(appliedBy);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(sanctionedBy);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.workFromHomeApplication).toEqual(workFromHomeApplication);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkFromHomeApplication>>();
      const workFromHomeApplication = { id: 123 };
      jest.spyOn(workFromHomeApplicationFormService, 'getWorkFromHomeApplication').mockReturnValue(workFromHomeApplication);
      jest.spyOn(workFromHomeApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workFromHomeApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workFromHomeApplication }));
      saveSubject.complete();

      // THEN
      expect(workFromHomeApplicationFormService.getWorkFromHomeApplication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workFromHomeApplicationService.update).toHaveBeenCalledWith(expect.objectContaining(workFromHomeApplication));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkFromHomeApplication>>();
      const workFromHomeApplication = { id: 123 };
      jest.spyOn(workFromHomeApplicationFormService, 'getWorkFromHomeApplication').mockReturnValue({ id: null });
      jest.spyOn(workFromHomeApplicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workFromHomeApplication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workFromHomeApplication }));
      saveSubject.complete();

      // THEN
      expect(workFromHomeApplicationFormService.getWorkFromHomeApplication).toHaveBeenCalled();
      expect(workFromHomeApplicationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkFromHomeApplication>>();
      const workFromHomeApplication = { id: 123 };
      jest.spyOn(workFromHomeApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workFromHomeApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workFromHomeApplicationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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
