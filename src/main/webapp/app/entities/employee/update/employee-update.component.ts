import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeFormService, EmployeeFormGroup } from './employee-form.service';
import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
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
import { BloodGroup } from 'app/entities/enumerations/blood-group.model';
import { Religion } from 'app/entities/enumerations/religion.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import { PayType } from 'app/entities/enumerations/pay-type.model';
import { DisbursementMethod } from 'app/entities/enumerations/disbursement-method.model';
import { CardType } from 'app/entities/enumerations/card-type.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { EmploymentStatus } from 'app/entities/enumerations/employment-status.model';
import { ILocation } from '../../../shared/model/location.model';
import { LocationService } from '../../location/location.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;
  bloodGroupValues = Object.keys(BloodGroup);
  religionValues = Object.keys(Religion);
  maritalStatusValues = Object.keys(MaritalStatus);
  employeeCategoryValues = Object.keys(EmployeeCategory);
  payTypeValues = Object.keys(PayType);
  disbursementMethodValues = Object.keys(DisbursementMethod);
  cardTypeValues = Object.keys(CardType);
  genderValues = Object.keys(Gender);
  employmentStatusValues = Object.keys(EmploymentStatus);

  locationsSharedCollection: ILocation[] = [];
  designationsSharedCollection: IDesignation[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  employeesSharedCollection: IEmployee[] = [];
  nationalitiesSharedCollection: INationality[] = [];
  bankBranchesSharedCollection: IBankBranch[] = [];
  bandsSharedCollection: IBand[] = [];
  unitsSharedCollection: IUnit[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  constructor(
    protected employeeService: EmployeeService,
    protected employeeFormService: EmployeeFormService,
    protected locationService: LocationService,
    protected designationService: DesignationService,
    protected departmentService: DepartmentService,
    protected nationalityService: NationalityService,
    protected bankBranchService: BankBranchService,
    protected bandService: BandService,
    protected unitService: UnitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDesignation = (o1: IDesignation | null, o2: IDesignation | null): boolean => this.designationService.compareDesignation(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareNationality = (o1: INationality | null, o2: INationality | null): boolean => this.nationalityService.compareNationality(o1, o2);

  compareBankBranch = (o1: IBankBranch | null, o2: IBankBranch | null): boolean => this.bankBranchService.compareBankBranch(o1, o2);

  compareBand = (o1: IBand | null, o2: IBand | null): boolean => this.bandService.compareBand(o1, o2);

  compareUnit = (o1: IUnit | null, o2: IUnit | null): boolean => this.unitService.compareUnit(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.designationsSharedCollection = this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
      this.designationsSharedCollection,
      employee.designation
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      employee.department
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employee.reportingTo
    );
    this.nationalitiesSharedCollection = this.nationalityService.addNationalityToCollectionIfMissing<INationality>(
      this.nationalitiesSharedCollection,
      employee.nationality
    );
    this.bankBranchesSharedCollection = this.bankBranchService.addBankBranchToCollectionIfMissing<IBankBranch>(
      this.bankBranchesSharedCollection,
      employee.bankBranch
    );
    this.bandsSharedCollection = this.bandService.addBandToCollectionIfMissing<IBand>(this.bandsSharedCollection, employee.band);
    this.unitsSharedCollection = this.unitService.addUnitToCollectionIfMissing<IUnit>(this.unitsSharedCollection, employee.unit);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, employee.user);
  }

  protected loadRelationshipsOptions(): void {
    this.designationService
      .query()
      .pipe(map((res: HttpResponse<IDesignation[]>) => res.body ?? []))
      .pipe(
        map((designations: IDesignation[]) =>
          this.designationService.addDesignationToCollectionIfMissing<IDesignation>(designations, this.employee?.designation)
        )
      )
      .subscribe((designations: IDesignation[]) => (this.designationsSharedCollection = designations));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.employee?.department)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employee?.reportingTo)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.nationalityService
      .query()
      .pipe(map((res: HttpResponse<INationality[]>) => res.body ?? []))
      .pipe(
        map((nationalities: INationality[]) =>
          this.nationalityService.addNationalityToCollectionIfMissing<INationality>(nationalities, this.employee?.nationality)
        )
      )
      .subscribe((nationalities: INationality[]) => (this.nationalitiesSharedCollection = nationalities));

    this.bankBranchService
      .query()
      .pipe(map((res: HttpResponse<IBankBranch[]>) => res.body ?? []))
      .pipe(
        map((bankBranches: IBankBranch[]) =>
          this.bankBranchService.addBankBranchToCollectionIfMissing<IBankBranch>(bankBranches, this.employee?.bankBranch)
        )
      )
      .subscribe((bankBranches: IBankBranch[]) => (this.bankBranchesSharedCollection = bankBranches));

    this.bandService
      .query()
      .pipe(map((res: HttpResponse<IBand[]>) => res.body ?? []))
      .pipe(map((bands: IBand[]) => this.bandService.addBandToCollectionIfMissing<IBand>(bands, this.employee?.band)))
      .subscribe((bands: IBand[]) => (this.bandsSharedCollection = bands));

    this.unitService
      .query()
      .pipe(map((res: HttpResponse<IUnit[]>) => res.body ?? []))
      .pipe(map((units: IUnit[]) => this.unitService.addUnitToCollectionIfMissing<IUnit>(units, this.employee?.unit)))
      .subscribe((units: IUnit[]) => (this.unitsSharedCollection = units));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.employee?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
