import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeePinFormGroup, EmployeePinFormService } from './employee-pin-form.service';
import { IEmployeePin } from '../employee-pin.model';
import { EmployeePinService } from '../service/employee-pin.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import { EmployeePinStatus } from 'app/entities/enumerations/employee-pin-status.model';
import { IEmployeePinConfiguration, NewEmployeePinConfiguration } from '../../employee-pin-configuration/employee-pin-configuration.model';
import { EmployeePinConfigurationService } from '../../employee-pin-configuration/service/employee-pin-configuration.service';
// import { IRecruitmentRequisitionForm } from '../../recruitment-requisition-form/recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from '../../../shared/legacy/legacy-service/recruitment-requisition-form.service';
// import {IRecruitmentRequisitionForm} from 'app/shared/model/recruitment-requisition-form.model';
// import { IRrfFilterModel } from 'app/entities/recruitment-requisition-form/recruitment-requisition-form-filter.model';
import { IRrfFilterModel } from "../../../payroll-management-system/recruitment-requisition-form/recruitment-requisition-form-filter.model";
// import {RequisitionStatus} from "../../enumerations/requisition-status.model";
import { RequisitionStatus } from 'app/shared/model/enumerations/requisition-status.model';
import {IRecruitmentRequisitionForm} from "../../../shared/legacy/legacy-model/recruitment-requisition-form.model";

type SelectableEntity = IDepartment | IDesignation | IUnit | IUser | IRecruitmentRequisitionForm;

@Component({
  selector: 'jhi-employee-pin-update',
  templateUrl: './employee-pin-update.component.html',
})
export class EmployeePinUpdateComponent implements OnInit {
  isSaving = false;
  employeePin: IEmployeePin | null = null;
  employeeCategoryValues = Object.keys(EmployeeCategory);
  employeePinStatusValues = Object.keys(EmployeePinStatus);
  recruitmentrequisitionforms: IRecruitmentRequisitionForm[] = [];
  departments: IDepartment[] = [];
  designations: IDesignation[] = [];
  units: IUnit[] = [];
  users: IUser[] = [];

  lastlyUsedPinExist = false;
  isConfigurationMissing = false;
  isPinUnique = true;
  isPinWithinDefinedRange = true;
  employeePinConfiguration!: IEmployeePinConfiguration;
  thePreviousPinFromTheSystem: string | null = null;
  selectedOption: any;

  departmentsSharedCollection: IDepartment[] = [];
  designationsSharedCollection: IDesignation[] = [];
  unitsSharedCollection: IUnit[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: EmployeePinFormGroup = this.employeePinFormService.createEmployeePinFormGroup();

  constructor(
    protected employeePinService: EmployeePinService,
    protected employeePinFormService: EmployeePinFormService,
    protected employeePinConfigurationService: EmployeePinConfigurationService,
    protected departmentService: DepartmentService,
    protected designationService: DesignationService,
    protected unitService: UnitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    private router: Router,
  ) {}

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareDesignation = (o1: IDesignation | null, o2: IDesignation | null): boolean => this.designationService.compareDesignation(o1, o2);

  compareUnit = (o1: IUnit | null, o2: IUnit | null): boolean => this.unitService.compareUnit(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.loadRequiredData();

    this.activatedRoute.data.subscribe(({ employeePin }) => {
      this.employeePin = employeePin;
      if (employeePin) {
        this.updateForm(employeePin);
      }

      this.loadRelationshipsOptions();
    });

    // this.editForm.valueChanges.subscribe(changes => {
    //   if (changes.employeeCategory !== this.getEmployeeCategory(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
    //     this.editForm.get('recruitmentRequisitionFormId')?.setValidators([Validators.required]);
    //   } else {
    //     this.editForm.get('recruitmentRequisitionFormId')?.setValidators([]);
    //   }
    // });
  }

  loadRequiredData(): void {
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
    this.loadRRFs();
  }

  loadRRFs(): void {
    const obj: IRrfFilterModel = {
      requisitionStatus: RequisitionStatus.CEO_APPROVED,
    };
    this.recruitmentRequisitionFormService.query(obj).subscribe((res: HttpResponse<IRecruitmentRequisitionForm[]>) => {
      this.recruitmentrequisitionforms = res.body ?? [];
    });
  }

  public isTotalOnboardLessThanNumberOfVacancies(form: IRecruitmentRequisitionForm): boolean {
    return form?.totalOnboard !== undefined && form?.numberOfVacancies !== undefined && form.totalOnboard < form.numberOfVacancies;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeePin = this.employeePinFormService.getEmployeePin(this.editForm);
    if (employeePin.id !== null) {
      this.subscribeToSaveResponse(this.employeePinService.update(employeePin));
    } else {
      this.subscribeToSaveResponse(this.employeePinService.create(employeePin as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeePin>>): void {
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

  protected updateForm(employeePin: IEmployeePin): void {
    this.employeePin = employeePin;
    this.employeePinFormService.resetForm(this.editForm, employeePin);

    // this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
    //   this.departmentsSharedCollection,
    //   employeePin.department
    // );
    // this.designationsSharedCollection = this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
    //   this.designationsSharedCollection,
    //   employeePin.designation
    // );
    // this.unitsSharedCollection = this.unitService.addUnitToCollectionIfMissing<IUnit>(this.unitsSharedCollection, employeePin.unit);
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   employeePin.updatedBy,
    //   employeePin.createdBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));

    // this.departmentService
    //   .query()
    //   .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
    //   .pipe(
    //     map((departments: IDepartment[]) =>
    //       this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.employeePin?.department)
    //     )
    //   )
    //   .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    // this.designationService
    //   .query()
    //   .pipe(map((res: HttpResponse<IDesignation[]>) => res.body ?? []))
    //   .pipe(
    //     map((designations: IDesignation[]) =>
    //       this.designationService.addDesignationToCollectionIfMissing<IDesignation>(designations, this.employeePin?.designation)
    //     )
    //   )
    //   .subscribe((designations: IDesignation[]) => (this.designationsSharedCollection = designations));

    // this.unitService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUnit[]>) => res.body ?? []))
    //   .pipe(map((units: IUnit[]) => this.unitService.addUnitToCollectionIfMissing<IUnit>(units, this.employeePin?.unit)))
    //   .subscribe((units: IUnit[]) => (this.unitsSharedCollection = units));

    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(users, this.employeePin?.updatedBy, this.employeePin?.createdBy)
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  getEmployeeCategory(category: string): string {
    if (category === 'REGULAR_CONFIRMED_EMPLOYEE') {
      return 'Regular Confirmed';
    } else if (category === 'CONTRACTUAL_EMPLOYEE') {
      return 'By Contract';
    } else {
      return 'Intern';
    }
  }

  onSelectEmployeeCategory(): void {
    if (this.editForm.get(['employeeCategory'])!.value !== null && this.editForm.get(['employeeCategory'])!.value !== undefined) {
      this.enableFieldsInEditForm();
      this.editForm.get(['pin'])!.reset();
      this.isPinWithinDefinedRange = true;
      this.getPinConfiguration();
    } else {
      this.disableFieldsInEditForm();
    }
  }

  getPinConfiguration(): void {
    this.isConfigurationMissing = false;
    const employeeCategory = this.editForm.get(['employeeCategory'])!.value;
    this.employeePinConfigurationService
      .getEmployeePinConfigurationByEmployeeCategory(employeeCategory)
      .subscribe((res: HttpResponse<NewEmployeePinConfiguration[]>) => {
        if (res.body!.length > 0) {
          this.isConfigurationMissing = false;
          this.employeePinConfiguration = res.body![0];
          if (this.employeePinConfiguration.lastSequence === null || this.employeePinConfiguration.lastSequence === undefined) {
            this.lastlyUsedPinExist = false;
          } else {
            this.lastlyUsedPinExist = true;
          }
        } else {
          this.isConfigurationMissing = true;
          this.lastlyUsedPinExist = false;
        }
      });
  }

  disableFieldsInEditForm(): void {
    this.editForm.get(['pin'])!.disable();
    this.editForm.get(['fullName'])!.disable();
    this.editForm.get(['employeePinStatus'])!.disable();
    this.editForm.get(['departmentId'])!.disable();
    this.editForm.get(['designationId'])!.disable();
    this.editForm.get(['unitId'])!.disable();
    this.editForm.get(['recruitmentRequisitionFormId'])!.disable();
  }

  enableFieldsInEditForm(): void {
    this.editForm.get(['pin'])!.enable();
    this.editForm.get(['fullName'])!.enable();
    this.editForm.get(['employeePinStatus'])!.enable();
    this.editForm.get(['departmentId'])!.enable();
    this.editForm.get(['designationId'])!.enable();
    this.editForm.get(['unitId'])!.enable();
    this.editForm.get(['recruitmentRequisitionFormId'])!.enable();
  }

  getRequestObject(): any {
    const pin = this.editForm.get(['pin'])!.value;
    const id = this.editForm.get(['id'])!.value;

    if (id !== null && id !== undefined) {
      return {
        pin,
        employeePinId: id,
      };
    } else {
      return {
        pin,
      };
    }
  }

  checkIfPinExistsBetweenDefinedRange(pin: string): boolean {
    const startRange = parseInt(this.employeePinConfiguration.sequenceStart!, 10);
    const endRange = parseInt(this.employeePinConfiguration.sequenceEnd!, 10);
    const selectedPin = parseInt(pin, 10);
    if (selectedPin >= startRange && selectedPin <= endRange) {
      return true;
    } else {
      return false;
    }
  }

  onChangeEmployeePin(): void {
    this.isPinUnique = true;
    this.isPinWithinDefinedRange = true;
    this.thePreviousPinFromTheSystem = null;

    const pin = this.editForm.get(['pin'])!.value;

    if (pin !== null && pin !== undefined && pin !== '') {
      this.employeePinService.isPinUnique(this.getRequestObject()).subscribe(res => {
        if (res.body === false) {
          this.isPinUnique = false;
          return;
        } else {
          const isPinExistBetweenRange = this.checkIfPinExistsBetweenDefinedRange(pin);
          if (isPinExistBetweenRange) {
            this.isPinWithinDefinedRange = true;
          } else {
            this.isPinWithinDefinedRange = false;
          }
        }
      });
    }
  }

  getMessageForTheFieldEmployeePin(): string {
    if (this.isPinUnique === false) {
      return 'This PIN is already in use.';
    } else if (this.isPinWithinDefinedRange === false) {
      return (
        'PIN for the selected category should be within ' +
        this.employeePinConfiguration.sequenceStart +
        ' and ' +
        this.employeePinConfiguration.sequenceEnd
      );
    } else {
      return '';
    }
  }

  createNewRRF(e: Event): void {
    e.preventDefault();
    this.router.navigate(['/recruitment-requisition-form/new']);
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
