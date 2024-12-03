import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalaryCertificateFormService } from './salary-certificate-form.service';
import { SalaryCertificateService } from '../service/salary-certificate.service';
import { ISalaryCertificate } from '../salary-certificate.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { SalaryCertificateUpdateComponent } from './salary-certificate-update.component';

describe('SalaryCertificate Management Update Component', () => {
  let comp: SalaryCertificateUpdateComponent;
  let fixture: ComponentFixture<SalaryCertificateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salaryCertificateFormService: SalaryCertificateFormService;
  let salaryCertificateService: SalaryCertificateService;
  let userService: UserService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SalaryCertificateUpdateComponent],
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
      .overrideTemplate(SalaryCertificateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryCertificateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salaryCertificateFormService = TestBed.inject(SalaryCertificateFormService);
    salaryCertificateService = TestBed.inject(SalaryCertificateService);
    userService = TestBed.inject(UserService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const salaryCertificate: ISalaryCertificate = { id: 456 };
      const createdBy: IUser = { id: 4172 };
      salaryCertificate.createdBy = createdBy;
      const updatedBy: IUser = { id: 57926 };
      salaryCertificate.updatedBy = updatedBy;
      const sanctionBy: IUser = { id: 1566 };
      salaryCertificate.sanctionBy = sanctionBy;

      const userCollection: IUser[] = [{ id: 80492 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, sanctionBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salaryCertificate });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const salaryCertificate: ISalaryCertificate = { id: 456 };
      const employee: IEmployee = { id: 97186 };
      salaryCertificate.employee = employee;
      const signatoryPerson: IEmployee = { id: 26512 };
      salaryCertificate.signatoryPerson = signatoryPerson;

      const employeeCollection: IEmployee[] = [{ id: 94000 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee, signatoryPerson];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salaryCertificate });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const salaryCertificate: ISalaryCertificate = { id: 456 };
      const createdBy: IUser = { id: 61629 };
      salaryCertificate.createdBy = createdBy;
      const updatedBy: IUser = { id: 58615 };
      salaryCertificate.updatedBy = updatedBy;
      const sanctionBy: IUser = { id: 31652 };
      salaryCertificate.sanctionBy = sanctionBy;
      const employee: IEmployee = { id: 30503 };
      salaryCertificate.employee = employee;
      const signatoryPerson: IEmployee = { id: 8533 };
      salaryCertificate.signatoryPerson = signatoryPerson;

      activatedRoute.data = of({ salaryCertificate });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(sanctionBy);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employeesSharedCollection).toContain(signatoryPerson);
      expect(comp.salaryCertificate).toEqual(salaryCertificate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryCertificate>>();
      const salaryCertificate = { id: 123 };
      jest.spyOn(salaryCertificateFormService, 'getSalaryCertificate').mockReturnValue(salaryCertificate);
      jest.spyOn(salaryCertificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryCertificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryCertificate }));
      saveSubject.complete();

      // THEN
      expect(salaryCertificateFormService.getSalaryCertificate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salaryCertificateService.update).toHaveBeenCalledWith(expect.objectContaining(salaryCertificate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryCertificate>>();
      const salaryCertificate = { id: 123 };
      jest.spyOn(salaryCertificateFormService, 'getSalaryCertificate').mockReturnValue({ id: null });
      jest.spyOn(salaryCertificateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryCertificate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryCertificate }));
      saveSubject.complete();

      // THEN
      expect(salaryCertificateFormService.getSalaryCertificate).toHaveBeenCalled();
      expect(salaryCertificateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryCertificate>>();
      const salaryCertificate = { id: 123 };
      jest.spyOn(salaryCertificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryCertificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salaryCertificateService.update).toHaveBeenCalled();
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
