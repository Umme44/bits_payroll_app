import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeePinFormService } from './employee-pin-form.service';
import { EmployeePinService } from '../service/employee-pin.service';
import { IEmployeePin } from '../employee-pin.model';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRecruitmentRequisitionForm } from 'app/entities/recruitment-requisition-form/recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from 'app/entities/recruitment-requisition-form/service/recruitment-requisition-form.service';

import { EmployeePinUpdateComponent } from './employee-pin-update.component';

describe('EmployeePin Management Update Component', () => {
  let comp: EmployeePinUpdateComponent;
  let fixture: ComponentFixture<EmployeePinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeePinFormService: EmployeePinFormService;
  let employeePinService: EmployeePinService;
  let departmentService: DepartmentService;
  let designationService: DesignationService;
  let unitService: UnitService;
  let userService: UserService;
  let recruitmentRequisitionFormService: RecruitmentRequisitionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeePinUpdateComponent],
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
      .overrideTemplate(EmployeePinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeePinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeePinFormService = TestBed.inject(EmployeePinFormService);
    employeePinService = TestBed.inject(EmployeePinService);
    departmentService = TestBed.inject(DepartmentService);
    designationService = TestBed.inject(DesignationService);
    unitService = TestBed.inject(UnitService);
    userService = TestBed.inject(UserService);
    recruitmentRequisitionFormService = TestBed.inject(RecruitmentRequisitionFormService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Department query and add missing value', () => {
      const employeePin: IEmployeePin = { id: 456 };
      const department: IDepartment = { id: 68558 };
      //employeePin.department = department;

      const departmentCollection: IDepartment[] = [{ id: 65337 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Designation query and add missing value', () => {
      const employeePin: IEmployeePin = { id: 456 };
      const designation: IDesignation = { id: 2372 };
      //employeePin.designation = designation;

      const designationCollection: IDesignation[] = [{ id: 63944 }];
      jest.spyOn(designationService, 'query').mockReturnValue(of(new HttpResponse({ body: designationCollection })));
      const additionalDesignations = [designation];
      const expectedCollection: IDesignation[] = [...additionalDesignations, ...designationCollection];
      jest.spyOn(designationService, 'addDesignationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      expect(designationService.query).toHaveBeenCalled();
      expect(designationService.addDesignationToCollectionIfMissing).toHaveBeenCalledWith(
        designationCollection,
        ...additionalDesignations.map(expect.objectContaining)
      );
      expect(comp.designationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Unit query and add missing value', () => {
      const employeePin: IEmployeePin = { id: 456 };
      const unit: IUnit = { id: 42456 };
      //employeePin.unit = unit;

      const unitCollection: IUnit[] = [{ id: 81810 }];
      jest.spyOn(unitService, 'query').mockReturnValue(of(new HttpResponse({ body: unitCollection })));
      const additionalUnits = [unit];
      const expectedCollection: IUnit[] = [...additionalUnits, ...unitCollection];
      jest.spyOn(unitService, 'addUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      expect(unitService.query).toHaveBeenCalled();
      expect(unitService.addUnitToCollectionIfMissing).toHaveBeenCalledWith(
        unitCollection,
        ...additionalUnits.map(expect.objectContaining)
      );
      expect(comp.unitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const employeePin: IEmployeePin = { id: 456 };
      const updatedBy: IUser = { id: 31173 };
      //employeePin.updatedBy = updatedBy;
      const createdBy: IUser = { id: 25542 };
      //employeePin.createdBy = createdBy;

      const userCollection: IUser[] = [{ id: 75013 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [updatedBy, createdBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeePin: IEmployeePin = { id: 456 };
      const department: IDepartment = { id: 34276 };
      //employeePin.department = department;
      const designation: IDesignation = { id: 38769 };
      //employeePin.designation = designation;
      const unit: IUnit = { id: 26875 };
      //employeePin.unit = unit;
      const updatedBy: IUser = { id: 70722 };
      //employeePin.updatedBy = updatedBy;
      const createdBy: IUser = { id: 8073 };
      //employeePin.createdBy = createdBy;
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 65298 };
      //employeePin.recruitmentRequisitionForm = recruitmentRequisitionForm;

      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.designationsSharedCollection).toContain(designation);
      expect(comp.unitsSharedCollection).toContain(unit);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.employeePin).toEqual(employeePin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeePin>>();
      const employeePin = { id: 123 };
      jest.spyOn(employeePinFormService, 'getEmployeePin').mockReturnValue(employeePin);
      jest.spyOn(employeePinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeePin }));
      saveSubject.complete();

      // THEN
      expect(employeePinFormService.getEmployeePin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeePinService.update).toHaveBeenCalledWith(expect.objectContaining(employeePin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeePin>>();
      const employeePin = { id: 123 };
      jest.spyOn(employeePinFormService, 'getEmployeePin').mockReturnValue({ id: null });
      jest.spyOn(employeePinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeePin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeePin }));
      saveSubject.complete();

      // THEN
      expect(employeePinFormService.getEmployeePin).toHaveBeenCalled();
      expect(employeePinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeePin>>();
      const employeePin = { id: 123 };
      jest.spyOn(employeePinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeePin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeePinService.update).toHaveBeenCalled();
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

    describe('compareDesignation', () => {
      it('Should forward to designationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(designationService, 'compareDesignation');
        comp.compareDesignation(entity, entity2);
        expect(designationService.compareDesignation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUnit', () => {
      it('Should forward to unitService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(unitService, 'compareUnit');
        comp.compareUnit(entity, entity2);
        expect(unitService.compareUnit).toHaveBeenCalledWith(entity, entity2);
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

    // describe('compareRecruitmentRequisitionForm', () => {
    //   it('Should forward to recruitmentRequisitionFormService', () => {
    //     const entity = { id: 123 };
    //     const entity2 = { id: 456 };
    //     jest.spyOn(recruitmentRequisitionFormService, 'compareRecruitmentRequisitionForm');
    //     comp.compareRecruitmentRequisitionForm(entity, entity2);
    //     expect(recruitmentRequisitionFormService.compareRecruitmentRequisitionForm).toHaveBeenCalledWith(entity, entity2);
    //   });
    // });
  });
});
