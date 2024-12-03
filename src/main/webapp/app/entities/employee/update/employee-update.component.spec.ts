import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeFormService } from './employee-form.service';
import { EmployeeService } from '../service/employee.service';
import { IEmployee } from '../employee.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { INationality } from 'app/entities/nationality/nationality.model';
import { NationalityService } from 'app/entities/nationality/service/nationality.service';
import { IBankBranch } from 'app/entities/bank-branch/bank-branch.model';
import { BankBranchService } from 'app/entities/bank-branch/service/bank-branch.service';
import { IBand } from 'app/entities/band/band.model';
import { BandService } from 'app/entities/band/service/band.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeFormService: EmployeeFormService;
  let employeeService: EmployeeService;
  let locationService: LocationService;
  let designationService: DesignationService;
  let departmentService: DepartmentService;
  let nationalityService: NationalityService;
  let bankBranchService: BankBranchService;
  let bandService: BandService;
  let unitService: UnitService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeUpdateComponent],
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
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeFormService = TestBed.inject(EmployeeFormService);
    employeeService = TestBed.inject(EmployeeService);
    locationService = TestBed.inject(LocationService);
    designationService = TestBed.inject(DesignationService);
    departmentService = TestBed.inject(DepartmentService);
    nationalityService = TestBed.inject(NationalityService);
    bankBranchService = TestBed.inject(BankBranchService);
    bandService = TestBed.inject(BandService);
    unitService = TestBed.inject(UnitService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Location query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const officeLocation: ILocation = { id: 52287 };
      employee.officeLocation = officeLocation;

      const locationCollection: ILocation[] = [{ id: 62415 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [officeLocation];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining)
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Designation query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const designation: IDesignation = { id: 96982 };
      employee.designation = designation;

      const designationCollection: IDesignation[] = [{ id: 66817 }];
      jest.spyOn(designationService, 'query').mockReturnValue(of(new HttpResponse({ body: designationCollection })));
      const additionalDesignations = [designation];
      const expectedCollection: IDesignation[] = [...additionalDesignations, ...designationCollection];
      jest.spyOn(designationService, 'addDesignationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(designationService.query).toHaveBeenCalled();
      expect(designationService.addDesignationToCollectionIfMissing).toHaveBeenCalledWith(
        designationCollection,
        ...additionalDesignations.map(expect.objectContaining)
      );
      expect(comp.designationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const department: IDepartment = { id: 35363 };
      employee.department = department;

      const departmentCollection: IDepartment[] = [{ id: 78278 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const reportingTo: IEmployee = { id: 4374 };
      employee.reportingTo = reportingTo;

      const employeeCollection: IEmployee[] = [{ id: 10177 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [reportingTo];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Nationality query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const nationality: INationality = { id: 72628 };
      employee.nationality = nationality;

      const nationalityCollection: INationality[] = [{ id: 50115 }];
      jest.spyOn(nationalityService, 'query').mockReturnValue(of(new HttpResponse({ body: nationalityCollection })));
      const additionalNationalities = [nationality];
      const expectedCollection: INationality[] = [...additionalNationalities, ...nationalityCollection];
      jest.spyOn(nationalityService, 'addNationalityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(nationalityService.query).toHaveBeenCalled();
      expect(nationalityService.addNationalityToCollectionIfMissing).toHaveBeenCalledWith(
        nationalityCollection,
        ...additionalNationalities.map(expect.objectContaining)
      );
      expect(comp.nationalitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranch query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const bankBranch: IBankBranch = { id: 13448 };
      employee.bankBranch = bankBranch;

      const bankBranchCollection: IBankBranch[] = [{ id: 96681 }];
      jest.spyOn(bankBranchService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCollection })));
      const additionalBankBranches = [bankBranch];
      const expectedCollection: IBankBranch[] = [...additionalBankBranches, ...bankBranchCollection];
      jest.spyOn(bankBranchService, 'addBankBranchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(bankBranchService.query).toHaveBeenCalled();
      expect(bankBranchService.addBankBranchToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCollection,
        ...additionalBankBranches.map(expect.objectContaining)
      );
      expect(comp.bankBranchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Band query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const band: IBand = { id: 55044 };
      employee.band = band;

      const bandCollection: IBand[] = [{ id: 7083 }];
      jest.spyOn(bandService, 'query').mockReturnValue(of(new HttpResponse({ body: bandCollection })));
      const additionalBands = [band];
      const expectedCollection: IBand[] = [...additionalBands, ...bandCollection];
      jest.spyOn(bandService, 'addBandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(bandService.query).toHaveBeenCalled();
      expect(bandService.addBandToCollectionIfMissing).toHaveBeenCalledWith(
        bandCollection,
        ...additionalBands.map(expect.objectContaining)
      );
      expect(comp.bandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Unit query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const unit: IUnit = { id: 50952 };
      employee.unit = unit;

      const unitCollection: IUnit[] = [{ id: 58224 }];
      jest.spyOn(unitService, 'query').mockReturnValue(of(new HttpResponse({ body: unitCollection })));
      const additionalUnits = [unit];
      const expectedCollection: IUnit[] = [...additionalUnits, ...unitCollection];
      jest.spyOn(unitService, 'addUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(unitService.query).toHaveBeenCalled();
      expect(unitService.addUnitToCollectionIfMissing).toHaveBeenCalledWith(
        unitCollection,
        ...additionalUnits.map(expect.objectContaining)
      );
      expect(comp.unitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 84478 };
      employee.user = user;

      const userCollection: IUser[] = [{ id: 1447 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const officeLocation: ILocation = { id: 39045 };
      employee.officeLocation = officeLocation;
      const designation: IDesignation = { id: 92741 };
      employee.designation = designation;
      const department: IDepartment = { id: 60127 };
      employee.department = department;
      const reportingTo: IEmployee = { id: 79320 };
      employee.reportingTo = reportingTo;
      const nationality: INationality = { id: 97347 };
      employee.nationality = nationality;
      const bankBranch: IBankBranch = { id: 3222 };
      employee.bankBranch = bankBranch;
      const band: IBand = { id: 67999 };
      employee.band = band;
      const unit: IUnit = { id: 99065 };
      employee.unit = unit;
      const user: IUser = { id: 66698 };
      employee.user = user;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.locationsSharedCollection).toContain(officeLocation);
      expect(comp.designationsSharedCollection).toContain(designation);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.employeesSharedCollection).toContain(reportingTo);
      expect(comp.nationalitiesSharedCollection).toContain(nationality);
      expect(comp.bankBranchesSharedCollection).toContain(bankBranch);
      expect(comp.bandsSharedCollection).toContain(band);
      expect(comp.unitsSharedCollection).toContain(unit);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.employee).toEqual(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue(employee);
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(expect.objectContaining(employee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue({ id: null });
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(employeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareNationality', () => {
      it('Should forward to nationalityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(nationalityService, 'compareNationality');
        comp.compareNationality(entity, entity2);
        expect(nationalityService.compareNationality).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBankBranch', () => {
      it('Should forward to bankBranchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bankBranchService, 'compareBankBranch');
        comp.compareBankBranch(entity, entity2);
        expect(bankBranchService.compareBankBranch).toHaveBeenCalledWith(entity, entity2);
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
  });
});
