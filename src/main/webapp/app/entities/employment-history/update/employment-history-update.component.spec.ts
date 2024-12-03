import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmploymentHistoryFormService } from './employment-history-form.service';
import { EmploymentHistoryService } from '../service/employment-history.service';
import { IEmploymentHistory } from '../employment-history.model';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';
import { IBand } from 'app/entities/band/band.model';
import { BandService } from 'app/entities/band/service/band.service';

import { EmploymentHistoryUpdateComponent } from './employment-history-update.component';

describe('EmploymentHistory Management Update Component', () => {
  let comp: EmploymentHistoryUpdateComponent;
  let fixture: ComponentFixture<EmploymentHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employmentHistoryFormService: EmploymentHistoryFormService;
  let employmentHistoryService: EmploymentHistoryService;
  let designationService: DesignationService;
  let departmentService: DepartmentService;
  let employeeService: EmployeeService;
  let unitService: UnitService;
  let bandService: BandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmploymentHistoryUpdateComponent],
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
      .overrideTemplate(EmploymentHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmploymentHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employmentHistoryFormService = TestBed.inject(EmploymentHistoryFormService);
    employmentHistoryService = TestBed.inject(EmploymentHistoryService);
    designationService = TestBed.inject(DesignationService);
    departmentService = TestBed.inject(DepartmentService);
    employeeService = TestBed.inject(EmployeeService);
    unitService = TestBed.inject(UnitService);
    bandService = TestBed.inject(BandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Designation query and add missing value', () => {
      const employmentHistory: IEmploymentHistory = { id: 456 };
      const previousDesignation: IDesignation = { id: 97279 };
      employmentHistory.previousDesignation = previousDesignation;
      const changedDesignation: IDesignation = { id: 59154 };
      employmentHistory.changedDesignation = changedDesignation;

      const designationCollection: IDesignation[] = [{ id: 57791 }];
      jest.spyOn(designationService, 'query').mockReturnValue(of(new HttpResponse({ body: designationCollection })));
      const additionalDesignations = [previousDesignation, changedDesignation];
      const expectedCollection: IDesignation[] = [...additionalDesignations, ...designationCollection];
      jest.spyOn(designationService, 'addDesignationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      expect(designationService.query).toHaveBeenCalled();
      expect(designationService.addDesignationToCollectionIfMissing).toHaveBeenCalledWith(
        designationCollection,
        ...additionalDesignations.map(expect.objectContaining)
      );
      expect(comp.designationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const employmentHistory: IEmploymentHistory = { id: 456 };
      const previousDepartment: IDepartment = { id: 40484 };
      employmentHistory.previousDepartment = previousDepartment;
      const changedDepartment: IDepartment = { id: 907 };
      employmentHistory.changedDepartment = changedDepartment;

      const departmentCollection: IDepartment[] = [{ id: 66126 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [previousDepartment, changedDepartment];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const employmentHistory: IEmploymentHistory = { id: 456 };
      const previousReportingTo: IEmployee = { id: 39832 };
      employmentHistory.previousReportingTo = previousReportingTo;
      const changedReportingTo: IEmployee = { id: 89288 };
      employmentHistory.changedReportingTo = changedReportingTo;
      const employee: IEmployee = { id: 80111 };
      employmentHistory.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 7206 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [previousReportingTo, changedReportingTo, employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Unit query and add missing value', () => {
      const employmentHistory: IEmploymentHistory = { id: 456 };
      const previousUnit: IUnit = { id: 25664 };
      employmentHistory.previousUnit = previousUnit;
      const changedUnit: IUnit = { id: 8631 };
      employmentHistory.changedUnit = changedUnit;

      const unitCollection: IUnit[] = [{ id: 25176 }];
      jest.spyOn(unitService, 'query').mockReturnValue(of(new HttpResponse({ body: unitCollection })));
      const additionalUnits = [previousUnit, changedUnit];
      const expectedCollection: IUnit[] = [...additionalUnits, ...unitCollection];
      jest.spyOn(unitService, 'addUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      expect(unitService.query).toHaveBeenCalled();
      expect(unitService.addUnitToCollectionIfMissing).toHaveBeenCalledWith(
        unitCollection,
        ...additionalUnits.map(expect.objectContaining)
      );
      expect(comp.unitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Band query and add missing value', () => {
      const employmentHistory: IEmploymentHistory = { id: 456 };
      const previousBand: IBand = { id: 26974 };
      employmentHistory.previousBand = previousBand;
      const changedBand: IBand = { id: 63581 };
      employmentHistory.changedBand = changedBand;

      const bandCollection: IBand[] = [{ id: 59732 }];
      jest.spyOn(bandService, 'query').mockReturnValue(of(new HttpResponse({ body: bandCollection })));
      const additionalBands = [previousBand, changedBand];
      const expectedCollection: IBand[] = [...additionalBands, ...bandCollection];
      jest.spyOn(bandService, 'addBandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      expect(bandService.query).toHaveBeenCalled();
      expect(bandService.addBandToCollectionIfMissing).toHaveBeenCalledWith(
        bandCollection,
        ...additionalBands.map(expect.objectContaining)
      );
      expect(comp.bandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employmentHistory: IEmploymentHistory = { id: 456 };
      const previousDesignation: IDesignation = { id: 93423 };
      employmentHistory.previousDesignation = previousDesignation;
      const changedDesignation: IDesignation = { id: 83065 };
      employmentHistory.changedDesignation = changedDesignation;
      const previousDepartment: IDepartment = { id: 26131 };
      employmentHistory.previousDepartment = previousDepartment;
      const changedDepartment: IDepartment = { id: 51758 };
      employmentHistory.changedDepartment = changedDepartment;
      const previousReportingTo: IEmployee = { id: 58022 };
      employmentHistory.previousReportingTo = previousReportingTo;
      const changedReportingTo: IEmployee = { id: 25540 };
      employmentHistory.changedReportingTo = changedReportingTo;
      const employee: IEmployee = { id: 84060 };
      employmentHistory.employee = employee;
      const previousUnit: IUnit = { id: 79469 };
      employmentHistory.previousUnit = previousUnit;
      const changedUnit: IUnit = { id: 78140 };
      employmentHistory.changedUnit = changedUnit;
      const previousBand: IBand = { id: 15920 };
      employmentHistory.previousBand = previousBand;
      const changedBand: IBand = { id: 47863 };
      employmentHistory.changedBand = changedBand;

      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      expect(comp.designationsSharedCollection).toContain(previousDesignation);
      expect(comp.designationsSharedCollection).toContain(changedDesignation);
      expect(comp.departmentsSharedCollection).toContain(previousDepartment);
      expect(comp.departmentsSharedCollection).toContain(changedDepartment);
      expect(comp.employeesSharedCollection).toContain(previousReportingTo);
      expect(comp.employeesSharedCollection).toContain(changedReportingTo);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.unitsSharedCollection).toContain(previousUnit);
      expect(comp.unitsSharedCollection).toContain(changedUnit);
      expect(comp.bandsSharedCollection).toContain(previousBand);
      expect(comp.bandsSharedCollection).toContain(changedBand);
      expect(comp.employmentHistory).toEqual(employmentHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentHistory>>();
      const employmentHistory = { id: 123 };
      jest.spyOn(employmentHistoryFormService, 'getEmploymentHistory').mockReturnValue(employmentHistory);
      jest.spyOn(employmentHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employmentHistory }));
      saveSubject.complete();

      // THEN
      expect(employmentHistoryFormService.getEmploymentHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employmentHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(employmentHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentHistory>>();
      const employmentHistory = { id: 123 };
      jest.spyOn(employmentHistoryFormService, 'getEmploymentHistory').mockReturnValue({ id: null });
      jest.spyOn(employmentHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employmentHistory }));
      saveSubject.complete();

      // THEN
      expect(employmentHistoryFormService.getEmploymentHistory).toHaveBeenCalled();
      expect(employmentHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentHistory>>();
      const employmentHistory = { id: 123 };
      jest.spyOn(employmentHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employmentHistoryService.update).toHaveBeenCalled();
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

    describe('compareUnit', () => {
      it('Should forward to unitService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(unitService, 'compareUnit');
        comp.compareUnit(entity, entity2);
        expect(unitService.compareUnit).toHaveBeenCalledWith(entity, entity2);
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
  });
});
