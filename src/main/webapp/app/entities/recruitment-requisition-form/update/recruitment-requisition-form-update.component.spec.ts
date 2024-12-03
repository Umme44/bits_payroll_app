import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecruitmentRequisitionFormFormService } from './recruitment-requisition-form-form.service';
import { RecruitmentRequisitionFormService } from '../service/recruitment-requisition-form.service';
import { IRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IBand } from 'app/entities/band/band.model';
import { BandService } from 'app/entities/band/service/band.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { RecruitmentRequisitionFormUpdateComponent } from './recruitment-requisition-form-update.component';

describe('RecruitmentRequisitionForm Management Update Component', () => {
  let comp: RecruitmentRequisitionFormUpdateComponent;
  let fixture: ComponentFixture<RecruitmentRequisitionFormUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recruitmentRequisitionFormFormService: RecruitmentRequisitionFormFormService;
  let recruitmentRequisitionFormService: RecruitmentRequisitionFormService;
  let designationService: DesignationService;
  let bandService: BandService;
  let departmentService: DepartmentService;
  let unitService: UnitService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecruitmentRequisitionFormUpdateComponent],
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
      .overrideTemplate(RecruitmentRequisitionFormUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecruitmentRequisitionFormUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recruitmentRequisitionFormFormService = TestBed.inject(RecruitmentRequisitionFormFormService);
    recruitmentRequisitionFormService = TestBed.inject(RecruitmentRequisitionFormService);
    designationService = TestBed.inject(DesignationService);
    bandService = TestBed.inject(BandService);
    departmentService = TestBed.inject(DepartmentService);
    unitService = TestBed.inject(UnitService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Designation query and add missing value', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const functionalDesignation: IDesignation = { id: 12830 };
      recruitmentRequisitionForm.functionalDesignation = functionalDesignation;

      const designationCollection: IDesignation[] = [{ id: 26726 }];
      jest.spyOn(designationService, 'query').mockReturnValue(of(new HttpResponse({ body: designationCollection })));
      const additionalDesignations = [functionalDesignation];
      const expectedCollection: IDesignation[] = [...additionalDesignations, ...designationCollection];
      jest.spyOn(designationService, 'addDesignationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(designationService.query).toHaveBeenCalled();
      expect(designationService.addDesignationToCollectionIfMissing).toHaveBeenCalledWith(
        designationCollection,
        ...additionalDesignations.map(expect.objectContaining)
      );
      expect(comp.designationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Band query and add missing value', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const band: IBand = { id: 54636 };
      recruitmentRequisitionForm.band = band;

      const bandCollection: IBand[] = [{ id: 5386 }];
      jest.spyOn(bandService, 'query').mockReturnValue(of(new HttpResponse({ body: bandCollection })));
      const additionalBands = [band];
      const expectedCollection: IBand[] = [...additionalBands, ...bandCollection];
      jest.spyOn(bandService, 'addBandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(bandService.query).toHaveBeenCalled();
      expect(bandService.addBandToCollectionIfMissing).toHaveBeenCalledWith(
        bandCollection,
        ...additionalBands.map(expect.objectContaining)
      );
      expect(comp.bandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const department: IDepartment = { id: 41793 };
      recruitmentRequisitionForm.department = department;

      const departmentCollection: IDepartment[] = [{ id: 71873 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Unit query and add missing value', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const unit: IUnit = { id: 68477 };
      recruitmentRequisitionForm.unit = unit;

      const unitCollection: IUnit[] = [{ id: 96071 }];
      jest.spyOn(unitService, 'query').mockReturnValue(of(new HttpResponse({ body: unitCollection })));
      const additionalUnits = [unit];
      const expectedCollection: IUnit[] = [...additionalUnits, ...unitCollection];
      jest.spyOn(unitService, 'addUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(unitService.query).toHaveBeenCalled();
      expect(unitService.addUnitToCollectionIfMissing).toHaveBeenCalledWith(
        unitCollection,
        ...additionalUnits.map(expect.objectContaining)
      );
      expect(comp.unitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const recommendedBy01: IEmployee = { id: 32276 };
      recruitmentRequisitionForm.recommendedBy01 = recommendedBy01;
      const recommendedBy02: IEmployee = { id: 29898 };
      recruitmentRequisitionForm.recommendedBy02 = recommendedBy02;
      const recommendedBy03: IEmployee = { id: 72146 };
      recruitmentRequisitionForm.recommendedBy03 = recommendedBy03;
      const recommendedBy04: IEmployee = { id: 54984 };
      recruitmentRequisitionForm.recommendedBy04 = recommendedBy04;
      const requester: IEmployee = { id: 17593 };
      recruitmentRequisitionForm.requester = requester;
      const rejectedBy: IEmployee = { id: 40177 };
      recruitmentRequisitionForm.rejectedBy = rejectedBy;
      const recommendedBy05: IEmployee = { id: 20004 };
      recruitmentRequisitionForm.recommendedBy05 = recommendedBy05;
      const employeeToBeReplaced: IEmployee = { id: 61276 };
      recruitmentRequisitionForm.employeeToBeReplaced = employeeToBeReplaced;

      const employeeCollection: IEmployee[] = [{ id: 75498 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [
        recommendedBy01,
        recommendedBy02,
        recommendedBy03,
        recommendedBy04,
        requester,
        rejectedBy,
        recommendedBy05,
        employeeToBeReplaced,
      ];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const createdBy: IUser = { id: 32154 };
      recruitmentRequisitionForm.createdBy = createdBy;
      const updatedBy: IUser = { id: 73472 };
      recruitmentRequisitionForm.updatedBy = updatedBy;
      const deletedBy: IUser = { id: 94031 };
      recruitmentRequisitionForm.deletedBy = deletedBy;

      const userCollection: IUser[] = [{ id: 8384 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, deletedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recruitmentRequisitionForm: IRecruitmentRequisitionForm = { id: 456 };
      const functionalDesignation: IDesignation = { id: 1236 };
      recruitmentRequisitionForm.functionalDesignation = functionalDesignation;
      const band: IBand = { id: 74726 };
      recruitmentRequisitionForm.band = band;
      const department: IDepartment = { id: 93864 };
      recruitmentRequisitionForm.department = department;
      const unit: IUnit = { id: 59265 };
      recruitmentRequisitionForm.unit = unit;
      const recommendedBy01: IEmployee = { id: 52913 };
      recruitmentRequisitionForm.recommendedBy01 = recommendedBy01;
      const recommendedBy02: IEmployee = { id: 72840 };
      recruitmentRequisitionForm.recommendedBy02 = recommendedBy02;
      const recommendedBy03: IEmployee = { id: 91043 };
      recruitmentRequisitionForm.recommendedBy03 = recommendedBy03;
      const recommendedBy04: IEmployee = { id: 78156 };
      recruitmentRequisitionForm.recommendedBy04 = recommendedBy04;
      const requester: IEmployee = { id: 98682 };
      recruitmentRequisitionForm.requester = requester;
      const rejectedBy: IEmployee = { id: 40357 };
      recruitmentRequisitionForm.rejectedBy = rejectedBy;
      const recommendedBy05: IEmployee = { id: 98601 };
      recruitmentRequisitionForm.recommendedBy05 = recommendedBy05;
      const employeeToBeReplaced: IEmployee = { id: 22014 };
      recruitmentRequisitionForm.employeeToBeReplaced = employeeToBeReplaced;
      const createdBy: IUser = { id: 83533 };
      recruitmentRequisitionForm.createdBy = createdBy;
      const updatedBy: IUser = { id: 94092 };
      recruitmentRequisitionForm.updatedBy = updatedBy;
      const deletedBy: IUser = { id: 15226 };
      recruitmentRequisitionForm.deletedBy = deletedBy;

      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      expect(comp.designationsSharedCollection).toContain(functionalDesignation);
      expect(comp.bandsSharedCollection).toContain(band);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.unitsSharedCollection).toContain(unit);
      expect(comp.employeesSharedCollection).toContain(recommendedBy01);
      expect(comp.employeesSharedCollection).toContain(recommendedBy02);
      expect(comp.employeesSharedCollection).toContain(recommendedBy03);
      expect(comp.employeesSharedCollection).toContain(recommendedBy04);
      expect(comp.employeesSharedCollection).toContain(requester);
      expect(comp.employeesSharedCollection).toContain(rejectedBy);
      expect(comp.employeesSharedCollection).toContain(recommendedBy05);
      expect(comp.employeesSharedCollection).toContain(employeeToBeReplaced);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(deletedBy);
      expect(comp.recruitmentRequisitionForm).toEqual(recruitmentRequisitionForm);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruitmentRequisitionForm>>();
      const recruitmentRequisitionForm = { id: 123 };
      jest.spyOn(recruitmentRequisitionFormFormService, 'getRecruitmentRequisitionForm').mockReturnValue(recruitmentRequisitionForm);
      jest.spyOn(recruitmentRequisitionFormService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recruitmentRequisitionForm }));
      saveSubject.complete();

      // THEN
      expect(recruitmentRequisitionFormFormService.getRecruitmentRequisitionForm).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recruitmentRequisitionFormService.update).toHaveBeenCalledWith(expect.objectContaining(recruitmentRequisitionForm));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruitmentRequisitionForm>>();
      const recruitmentRequisitionForm = { id: 123 };
      jest.spyOn(recruitmentRequisitionFormFormService, 'getRecruitmentRequisitionForm').mockReturnValue({ id: null });
      jest.spyOn(recruitmentRequisitionFormService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruitmentRequisitionForm: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recruitmentRequisitionForm }));
      saveSubject.complete();

      // THEN
      expect(recruitmentRequisitionFormFormService.getRecruitmentRequisitionForm).toHaveBeenCalled();
      expect(recruitmentRequisitionFormService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruitmentRequisitionForm>>();
      const recruitmentRequisitionForm = { id: 123 };
      jest.spyOn(recruitmentRequisitionFormService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruitmentRequisitionForm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recruitmentRequisitionFormService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDesignation', () => {
      it('Should forward to designationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(designationService, 'compareDesignation');
        comp.compareDesignation(entity, entity2);
        expect(designationService.compareDesignation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBand', () => {
      it('Should forward to bandService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bandService, 'compareBand');
        comp.compareBand(entity, entity2);
        expect(bandService.compareBand).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
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
