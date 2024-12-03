import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProcReqMasterFormService } from './proc-req-master-form.service';
import { ProcReqMasterService } from '../service/proc-req-master.service';
import { IProcReqMaster } from '../proc-req-master.model';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ProcReqMasterUpdateComponent } from './proc-req-master-update.component';

describe('ProcReqMaster Management Update Component', () => {
  let comp: ProcReqMasterUpdateComponent;
  let fixture: ComponentFixture<ProcReqMasterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let procReqMasterFormService: ProcReqMasterFormService;
  let procReqMasterService: ProcReqMasterService;
  let departmentService: DepartmentService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProcReqMasterUpdateComponent],
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
      .overrideTemplate(ProcReqMasterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcReqMasterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    procReqMasterFormService = TestBed.inject(ProcReqMasterFormService);
    procReqMasterService = TestBed.inject(ProcReqMasterService);
    departmentService = TestBed.inject(DepartmentService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Department query and add missing value', () => {
      const procReqMaster: IProcReqMaster = { id: 456 };
      const department: IDepartment = { id: 51535 };
      procReqMaster.department = department;

      const departmentCollection: IDepartment[] = [{ id: 20396 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ procReqMaster });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const procReqMaster: IProcReqMaster = { id: 456 };
      const requestedBy: IEmployee = { id: 85748 };
      procReqMaster.requestedBy = requestedBy;
      const recommendedBy01: IEmployee = { id: 54903 };
      procReqMaster.recommendedBy01 = recommendedBy01;
      const recommendedBy02: IEmployee = { id: 58883 };
      procReqMaster.recommendedBy02 = recommendedBy02;
      const recommendedBy03: IEmployee = { id: 18522 };
      procReqMaster.recommendedBy03 = recommendedBy03;
      const recommendedBy04: IEmployee = { id: 53001 };
      procReqMaster.recommendedBy04 = recommendedBy04;
      const recommendedBy05: IEmployee = { id: 87133 };
      procReqMaster.recommendedBy05 = recommendedBy05;
      const nextApprovalFrom: IEmployee = { id: 22034 };
      procReqMaster.nextApprovalFrom = nextApprovalFrom;
      const rejectedBy: IEmployee = { id: 25668 };
      procReqMaster.rejectedBy = rejectedBy;
      const closedBy: IEmployee = { id: 86645 };
      procReqMaster.closedBy = closedBy;

      const employeeCollection: IEmployee[] = [{ id: 12816 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [
        requestedBy,
        recommendedBy01,
        recommendedBy02,
        recommendedBy03,
        recommendedBy04,
        recommendedBy05,
        nextApprovalFrom,
        rejectedBy,
        closedBy,
      ];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ procReqMaster });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const procReqMaster: IProcReqMaster = { id: 456 };
      const updatedBy: IUser = { id: 1102 };
      procReqMaster.updatedBy = updatedBy;
      const createdBy: IUser = { id: 21894 };
      procReqMaster.createdBy = createdBy;

      const userCollection: IUser[] = [{ id: 4063 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [updatedBy, createdBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ procReqMaster });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const procReqMaster: IProcReqMaster = { id: 456 };
      const department: IDepartment = { id: 18319 };
      procReqMaster.department = department;
      const requestedBy: IEmployee = { id: 37689 };
      procReqMaster.requestedBy = requestedBy;
      const recommendedBy01: IEmployee = { id: 28826 };
      procReqMaster.recommendedBy01 = recommendedBy01;
      const recommendedBy02: IEmployee = { id: 11064 };
      procReqMaster.recommendedBy02 = recommendedBy02;
      const recommendedBy03: IEmployee = { id: 46811 };
      procReqMaster.recommendedBy03 = recommendedBy03;
      const recommendedBy04: IEmployee = { id: 99912 };
      procReqMaster.recommendedBy04 = recommendedBy04;
      const recommendedBy05: IEmployee = { id: 1914 };
      procReqMaster.recommendedBy05 = recommendedBy05;
      const nextApprovalFrom: IEmployee = { id: 60203 };
      procReqMaster.nextApprovalFrom = nextApprovalFrom;
      const rejectedBy: IEmployee = { id: 2495 };
      procReqMaster.rejectedBy = rejectedBy;
      const closedBy: IEmployee = { id: 4954 };
      procReqMaster.closedBy = closedBy;
      const updatedBy: IUser = { id: 59606 };
      procReqMaster.updatedBy = updatedBy;
      const createdBy: IUser = { id: 93984 };
      procReqMaster.createdBy = createdBy;

      activatedRoute.data = of({ procReqMaster });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.employeesSharedCollection).toContain(requestedBy);
      expect(comp.employeesSharedCollection).toContain(recommendedBy01);
      expect(comp.employeesSharedCollection).toContain(recommendedBy02);
      expect(comp.employeesSharedCollection).toContain(recommendedBy03);
      expect(comp.employeesSharedCollection).toContain(recommendedBy04);
      expect(comp.employeesSharedCollection).toContain(recommendedBy05);
      expect(comp.employeesSharedCollection).toContain(nextApprovalFrom);
      expect(comp.employeesSharedCollection).toContain(rejectedBy);
      expect(comp.employeesSharedCollection).toContain(closedBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.procReqMaster).toEqual(procReqMaster);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcReqMaster>>();
      const procReqMaster = { id: 123 };
      jest.spyOn(procReqMasterFormService, 'getProcReqMaster').mockReturnValue(procReqMaster);
      jest.spyOn(procReqMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procReqMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: procReqMaster }));
      saveSubject.complete();

      // THEN
      expect(procReqMasterFormService.getProcReqMaster).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(procReqMasterService.update).toHaveBeenCalledWith(expect.objectContaining(procReqMaster));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcReqMaster>>();
      const procReqMaster = { id: 123 };
      jest.spyOn(procReqMasterFormService, 'getProcReqMaster').mockReturnValue({ id: null });
      jest.spyOn(procReqMasterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procReqMaster: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: procReqMaster }));
      saveSubject.complete();

      // THEN
      expect(procReqMasterFormService.getProcReqMaster).toHaveBeenCalled();
      expect(procReqMasterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcReqMaster>>();
      const procReqMaster = { id: 123 };
      jest.spyOn(procReqMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procReqMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(procReqMasterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
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
