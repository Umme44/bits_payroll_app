import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeNOCFormService } from './employee-noc-form.service';
import { EmployeeNOCService } from '../service/employee-noc.service';
import { IEmployeeNOC } from '../employee-noc.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { EmployeeNOCUpdateComponent } from './employee-noc-update.component';

describe('EmployeeNOC Management Update Component', () => {
  let comp: EmployeeNOCUpdateComponent;
  let fixture: ComponentFixture<EmployeeNOCUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeNOCFormService: EmployeeNOCFormService;
  let employeeNOCService: EmployeeNOCService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeNOCUpdateComponent],
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
      .overrideTemplate(EmployeeNOCUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeNOCUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeNOCFormService = TestBed.inject(EmployeeNOCFormService);
    employeeNOCService = TestBed.inject(EmployeeNOCService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeNOC: IEmployeeNOC = { id: 456 };
      const employee: IEmployee = { id: 96582 };
      employeeNOC.employee = employee;
      const signatoryPerson: IEmployee = { id: 9875 };
      employeeNOC.signatoryPerson = signatoryPerson;

      const employeeCollection: IEmployee[] = [{ id: 18569 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee, signatoryPerson];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeNOC });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const employeeNOC: IEmployeeNOC = { id: 456 };
      const createdBy: IUser = { id: 49562 };
      employeeNOC.createdBy = createdBy;
      const updatedBy: IUser = { id: 18168 };
      employeeNOC.updatedBy = updatedBy;
      const generatedBy: IUser = { id: 11964 };
      employeeNOC.generatedBy = generatedBy;

      const userCollection: IUser[] = [{ id: 78395 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, generatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeNOC });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeNOC: IEmployeeNOC = { id: 456 };
      const employee: IEmployee = { id: 65473 };
      employeeNOC.employee = employee;
      const signatoryPerson: IEmployee = { id: 16679 };
      employeeNOC.signatoryPerson = signatoryPerson;
      const createdBy: IUser = { id: 24757 };
      employeeNOC.createdBy = createdBy;
      const updatedBy: IUser = { id: 75415 };
      employeeNOC.updatedBy = updatedBy;
      const generatedBy: IUser = { id: 16204 };
      employeeNOC.generatedBy = generatedBy;

      activatedRoute.data = of({ employeeNOC });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeesSharedCollection).toContain(signatoryPerson);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(generatedBy);
      expect(comp.employeeNOC).toEqual(employeeNOC);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeNOC>>();
      const employeeNOC = { id: 123 };
      jest.spyOn(employeeNOCFormService, 'getEmployeeNOC').mockReturnValue(employeeNOC);
      jest.spyOn(employeeNOCService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeNOC });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeNOC }));
      saveSubject.complete();

      // THEN
      expect(employeeNOCFormService.getEmployeeNOC).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeNOCService.update).toHaveBeenCalledWith(expect.objectContaining(employeeNOC));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeNOC>>();
      const employeeNOC = { id: 123 };
      jest.spyOn(employeeNOCFormService, 'getEmployeeNOC').mockReturnValue({ id: null });
      jest.spyOn(employeeNOCService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeNOC: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeNOC }));
      saveSubject.complete();

      // THEN
      expect(employeeNOCFormService.getEmployeeNOC).toHaveBeenCalled();
      expect(employeeNOCService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeNOC>>();
      const employeeNOC = { id: 123 };
      jest.spyOn(employeeNOCService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeNOC });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeNOCService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
