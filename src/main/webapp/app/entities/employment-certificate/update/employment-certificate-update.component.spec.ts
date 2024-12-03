import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmploymentCertificateFormService } from './employment-certificate-form.service';
import { EmploymentCertificateService } from '../service/employment-certificate.service';
import { IEmploymentCertificate } from '../employment-certificate.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { EmploymentCertificateUpdateComponent } from './employment-certificate-update.component';

describe('EmploymentCertificate Management Update Component', () => {
  let comp: EmploymentCertificateUpdateComponent;
  let fixture: ComponentFixture<EmploymentCertificateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employmentCertificateFormService: EmploymentCertificateFormService;
  let employmentCertificateService: EmploymentCertificateService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmploymentCertificateUpdateComponent],
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
      .overrideTemplate(EmploymentCertificateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmploymentCertificateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employmentCertificateFormService = TestBed.inject(EmploymentCertificateFormService);
    employmentCertificateService = TestBed.inject(EmploymentCertificateService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employmentCertificate: IEmploymentCertificate = { id: 456 };
      const employee: IEmployee = { id: 38945 };
      employmentCertificate.employee = employee;
      const signatoryPerson: IEmployee = { id: 65059 };
      employmentCertificate.signatoryPerson = signatoryPerson;

      const employeeCollection: IEmployee[] = [{ id: 52337 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee, signatoryPerson];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentCertificate });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const employmentCertificate: IEmploymentCertificate = { id: 456 };
      const createdBy: IUser = { id: 76462 };
      employmentCertificate.createdBy = createdBy;
      const updatedBy: IUser = { id: 42675 };
      employmentCertificate.updatedBy = updatedBy;
      const generatedBy: IUser = { id: 57933 };
      employmentCertificate.generatedBy = generatedBy;

      const userCollection: IUser[] = [{ id: 81098 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, generatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentCertificate });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employmentCertificate: IEmploymentCertificate = { id: 456 };
      const employee: IEmployee = { id: 12498 };
      employmentCertificate.employee = employee;
      const signatoryPerson: IEmployee = { id: 86376 };
      employmentCertificate.signatoryPerson = signatoryPerson;
      const createdBy: IUser = { id: 25085 };
      employmentCertificate.createdBy = createdBy;
      const updatedBy: IUser = { id: 6944 };
      employmentCertificate.updatedBy = updatedBy;
      const generatedBy: IUser = { id: 85284 };
      employmentCertificate.generatedBy = generatedBy;

      activatedRoute.data = of({ employmentCertificate });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeesSharedCollection).toContain(signatoryPerson);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(generatedBy);
      expect(comp.employmentCertificate).toEqual(employmentCertificate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentCertificate>>();
      const employmentCertificate = { id: 123 };
      jest.spyOn(employmentCertificateFormService, 'getEmploymentCertificate').mockReturnValue(employmentCertificate);
      jest.spyOn(employmentCertificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentCertificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employmentCertificate }));
      saveSubject.complete();

      // THEN
      expect(employmentCertificateFormService.getEmploymentCertificate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employmentCertificateService.update).toHaveBeenCalledWith(expect.objectContaining(employmentCertificate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentCertificate>>();
      const employmentCertificate = { id: 123 };
      jest.spyOn(employmentCertificateFormService, 'getEmploymentCertificate').mockReturnValue({ id: null });
      jest.spyOn(employmentCertificateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentCertificate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employmentCertificate }));
      saveSubject.complete();

      // THEN
      expect(employmentCertificateFormService.getEmploymentCertificate).toHaveBeenCalled();
      expect(employmentCertificateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentCertificate>>();
      const employmentCertificate = { id: 123 };
      jest.spyOn(employmentCertificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentCertificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employmentCertificateService.update).toHaveBeenCalled();
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
