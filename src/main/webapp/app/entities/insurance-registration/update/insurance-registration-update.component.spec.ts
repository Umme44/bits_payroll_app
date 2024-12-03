import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InsuranceRegistrationFormService } from './insurance-registration-form.service';
import { InsuranceRegistrationService } from '../service/insurance-registration.service';
import { IInsuranceRegistration } from '../insurance-registration.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { InsuranceRegistrationUpdateComponent } from './insurance-registration-update.component';

describe('InsuranceRegistration Management Update Component', () => {
  let comp: InsuranceRegistrationUpdateComponent;
  let fixture: ComponentFixture<InsuranceRegistrationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insuranceRegistrationFormService: InsuranceRegistrationFormService;
  let insuranceRegistrationService: InsuranceRegistrationService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InsuranceRegistrationUpdateComponent],
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
      .overrideTemplate(InsuranceRegistrationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsuranceRegistrationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insuranceRegistrationFormService = TestBed.inject(InsuranceRegistrationFormService);
    insuranceRegistrationService = TestBed.inject(InsuranceRegistrationService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const insuranceRegistration: IInsuranceRegistration = { id: 456 };
      const employee: IEmployee = { id: 46464 };
      insuranceRegistration.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 11800 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insuranceRegistration });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const insuranceRegistration: IInsuranceRegistration = { id: 456 };
      const approvedBy: IUser = { id: 65497 };
      insuranceRegistration.approvedBy = approvedBy;
      const updatedBy: IUser = { id: 26823 };
      insuranceRegistration.updatedBy = updatedBy;
      const createdBy: IUser = { id: 75738 };
      insuranceRegistration.createdBy = createdBy;

      const userCollection: IUser[] = [{ id: 18099 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [approvedBy, updatedBy, createdBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insuranceRegistration });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const insuranceRegistration: IInsuranceRegistration = { id: 456 };
      const employee: IEmployee = { id: 9785 };
      insuranceRegistration.employee = employee;
      const approvedBy: IUser = { id: 30558 };
      insuranceRegistration.approvedBy = approvedBy;
      const updatedBy: IUser = { id: 3660 };
      insuranceRegistration.updatedBy = updatedBy;
      const createdBy: IUser = { id: 32748 };
      insuranceRegistration.createdBy = createdBy;

      activatedRoute.data = of({ insuranceRegistration });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.usersSharedCollection).toContain(approvedBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.insuranceRegistration).toEqual(insuranceRegistration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceRegistration>>();
      const insuranceRegistration = { id: 123 };
      jest.spyOn(insuranceRegistrationFormService, 'getInsuranceRegistration').mockReturnValue(insuranceRegistration);
      jest.spyOn(insuranceRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceRegistration }));
      saveSubject.complete();

      // THEN
      expect(insuranceRegistrationFormService.getInsuranceRegistration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insuranceRegistrationService.update).toHaveBeenCalledWith(expect.objectContaining(insuranceRegistration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceRegistration>>();
      const insuranceRegistration = { id: 123 };
      jest.spyOn(insuranceRegistrationFormService, 'getInsuranceRegistration').mockReturnValue({ id: null });
      jest.spyOn(insuranceRegistrationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceRegistration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceRegistration }));
      saveSubject.complete();

      // THEN
      expect(insuranceRegistrationFormService.getInsuranceRegistration).toHaveBeenCalled();
      expect(insuranceRegistrationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceRegistration>>();
      const insuranceRegistration = { id: 123 };
      jest.spyOn(insuranceRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insuranceRegistrationService.update).toHaveBeenCalled();
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
