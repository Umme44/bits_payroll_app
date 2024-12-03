import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { InsuranceRegistrationFormGroup, InsuranceRegistrationFormService } from './insurance-registration-form.service';
import { IInsuranceRegistration } from '../insurance-registration.model';
import { InsuranceRegistrationService } from '../service/insurance-registration.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { InsuranceStatus } from 'app/entities/enumerations/insurance-status.model';
import { InsuranceRelations } from '../../../shared/legacy/legacy-model/insurance-relations.model';
import { IInsuranceConfiguration } from '../../insurance-configuration/insurance-configuration.model';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { IEmployeeMinimal } from '../../../shared/model/employee-minimal.model';
import { InsuranceRelation } from '../../../shared/model/enumerations/insurance-relation.model';
import { UserInsuranceService } from '../../../payroll-management-system/insurance-profile/user-insurance.service';
import { swalSelectValidImage } from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-insurance-registration-update',
  templateUrl: './insurance-registration-update.component.html',
})
export class InsuranceRegistrationUpdateComponent implements OnInit {
  isSaving = false;
  insuranceRegistration: IInsuranceRegistration | null = null;
  insuranceRelationValues = Object.keys(InsuranceRelations);
  insuranceStatusValues = Object.keys(InsuranceStatus);

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  previousInsuranceRegistration!: IInsuranceRegistration[];
  selectedEmployeeDetails!: IEmployeeMinimal;

  selectedEmployeeId!: number;
  users: IUser[] = [];
  dateOfBirthDp: any;

  inValidFileErrorMsg!: string;
  inValidFile = false;
  image!: File;
  isImageMissing = true;
  isImageRequired = false;

  insuranceConfiguration!: IInsuranceConfiguration;
  isInsuranceConfigurationMissing = false;
  inValidAge = false;
  personsAge = 0;
  isChildAgeExceedMaxLimit = false;
  isEmployeeEligible = true;
  maxDateForDateOfBirthDP: NgbDateStruct;

  relations!: string[] | [];
  employees!: IEmployeeMinimal[] | [];

  inValidInsuranceId = false;

  editForm: InsuranceRegistrationFormGroup = this.insuranceRegistrationFormService.createInsuranceRegistrationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected userInsuranceService: UserInsuranceService,
    protected insuranceRegistrationService: InsuranceRegistrationService,
    protected insuranceRegistrationFormService: InsuranceRegistrationFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceRegistration }) => {
      this.insuranceRegistration = insuranceRegistration;

      this.getInsuranceConfiguration();
      this.getEligibleEmployeesForInsuranceRegistrations();

      if (insuranceRegistration) {
        this.getSelfDetails(insuranceRegistration.employeeId!);
        this.getAllInsuranceRelations();
        this.updateForm(insuranceRegistration);
        this.calculateAge();
        this.inValidFile = false;
      } else {
        this.disableInputFields();
      }

      this.loadRelationshipsOptions();
    });
  }

  disableInputFields(): void {
    this.editForm.get(['insuranceRelation'])!.disable();
    this.editForm.get(['name'])!.disable();
    this.editForm.get(['dateOfBirth'])!.disable();
    this.editForm.get(['photo'])!.disable();
  }

  getSelfDetails(id: number): void {
    this.insuranceRegistrationService.queryEmployeeDetails(id).subscribe(res => {
      this.selectedEmployeeDetails = res.body!;
    });
  }

  getInsuranceConfiguration(): void {
    this.userInsuranceService.queryInsuranceConfiguration().subscribe(res => {
      this.insuranceConfiguration = res.body!;
    });
  }

  getAllInsuranceRelations(): void {
    this.insuranceRegistrationService.queryAllInsuranceRelations().subscribe(res => {
      this.relations = res.body!.relations || [];
    });
  }

  getEligibleEmployeesForInsuranceRegistrations(): void {
    this.insuranceRegistrationService.getEligibleEmployees().subscribe((res: HttpResponse<IEmployeeMinimal[]>) => {
      this.employees = res.body || [];
      this.employees = this.employees.map(item => {
        return {
          id: item.id,
          pin: item.pin,
          name: item.fullName,
          designation: item.designationName,
          fullName: item.pin + '-' + item.fullName + '-' + item.designationName,
        };
      });
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insuranceRegistration = this.insuranceRegistrationFormService.getInsuranceRegistration(this.editForm);

    // if (insuranceRegistration.id !== null) {
    //   this.subscribeToSaveResponse(this.insuranceRegistrationService.update(insuranceRegistration));
    // } else {
    //   this.subscribeToSaveResponse(this.insuranceRegistrationService.create(insuranceRegistration as any));
    // }

    if (insuranceRegistration.id !== null) {
      if (this.image === null || this.image === undefined) {
        this.subscribeToSaveResponse(this.insuranceRegistrationService.updateInsuranceRegistration(insuranceRegistration));
      } else {
        this.subscribeToSaveResponse(
          this.insuranceRegistrationService.updateInsuranceRegistrationMultipart(this.image, insuranceRegistration)
        );
      }
    } else {
      insuranceRegistration.insuranceStatus = InsuranceStatus.PENDING;
      insuranceRegistration.availableBalance = this.insuranceConfiguration.maxTotalClaimLimitPerYear;

      // this.editForm.get(['availableBalance']).setValue(this.insuranceConfiguration.maxTotalClaimLimitPerYear);
      // this.editForm.get(['insuranceStatus']).setValue(InsuranceStatus.PENDING);

      if (insuranceRegistration.insuranceRelation === 'SELF') {
        this.subscribeToSaveResponse(this.insuranceRegistrationService.create(insuranceRegistration as any));
      } else {
        if (this.image === null || this.image === undefined) {
          this.subscribeToSaveResponse(this.insuranceRegistrationService.create(insuranceRegistration as any));
        } else if (!this.inValidFile) {
          this.subscribeToSaveResponse(
            this.insuranceRegistrationService.createInsuranceRegistrationAdminMultipart(this.image, insuranceRegistration)
          );
        } else {
          swalSelectValidImage();
          this.isSaving = false;
          return;
        }
      }
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsuranceRegistration>>): void {
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

  protected updateForm(insuranceRegistration: IInsuranceRegistration): void {
    this.insuranceRegistration = insuranceRegistration;
    this.insuranceRegistrationFormService.resetForm(this.editForm, insuranceRegistration);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   insuranceRegistration.employee
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   insuranceRegistration.approvedBy,
    //   insuranceRegistration.updatedBy,
    //   insuranceRegistration.createdBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.insuranceRegistration?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(
    //         users,
    //         this.insuranceRegistration?.approvedBy,
    //         this.insuranceRegistration?.updatedBy,
    //         this.insuranceRegistration?.createdBy
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  getRegistrationAccountImageImage(registrationId: number): String {
    const url = SERVER_API_URL + 'files/common/insurance-registration-image/' + registrationId;
    return url;
  }

  getInsuranceRelationName(relationName: string): string {
    if (relationName === InsuranceRelation.SELF) {
      return 'Self';
    } else if (relationName === InsuranceRelation.SPOUSE) {
      return 'Spouse';
    } else if (relationName === InsuranceRelation.CHILD_1) {
      return 'Child 1';
    } else if (relationName === InsuranceRelation.CHILD_2) {
      return 'Child 2';
    } else {
      return 'Child 3';
    }
  }

  getEmployeeID(employee: any): void {
    const employeeId = employee.id;
    this.editForm.get(['employeeId'])!.setValue(employeeId);
    //this.editForm.reset();
    this.enableInputFields();
    this.selectedEmployeeId = employeeId;
    this.getSelfDetailsAndRemainingRelations(this.selectedEmployeeId);
    this.getPreviousRegistrationsByEmployeeId(this.selectedEmployeeId);
  }

  getSelfDetailsAndRemainingRelations(id: number): void {
    this.insuranceRegistrationService.queryEmployeeDetails(id).subscribe(res => {
      this.selectedEmployeeDetails = res.body!;
      this.queryRemainingInsuranceRelationForEmployee(id);
    });
  }

  queryRemainingInsuranceRelationForEmployee(id: number): void {
    this.relations = [];
    this.insuranceRegistrationService.queryRemainingInsuranceRelationByEmployeeId(id).subscribe(res => {
      this.relations = res.body!.relations || [];
    });
  }

  getPreviousRegistrationsByEmployeeId(id: number): void {
    this.insuranceRegistrationService.queryPreviousRegistrationsByEmployeeId(id).subscribe(res => {
      this.previousInsuranceRegistration = res.body!;
    });
  }

  enableInputFields(): void {
    this.editForm.get(['insuranceRelation'])!.reset();
    this.editForm.get(['name'])!.reset();
    this.editForm.get(['dateOfBirth'])!.reset();
    this.editForm.get(['photo'])!.reset();

    this.editForm.get(['insuranceRelation'])!.enable();
    this.editForm.get(['name'])!.enable();
    this.editForm.get(['dateOfBirth'])!.enable();
    this.editForm.get(['photo'])!.enable();
  }

  onChangeInsuranceRelation(): void {
    this.editForm.get(['name'])!.reset();
    this.editForm.get(['name'])!.enable();
    this.editForm.get(['age'])!.setValue(0);
    this.editForm.get(['dateOfBirth'])!.reset();
    this.editForm.get(['dateOfBirth'])!.enable();

    const insuranceRelation = this.editForm.get(['insuranceRelation'])!.value;
    if (insuranceRelation === InsuranceRelation.SELF) {
      this.populateSelfDetails();
      this.isImageMissing = false;
    } else {
      if (this.isImageRequired) {
        if (this.image !== null && this.image !== undefined) {
          this.isImageMissing = false;
        } else {
          this.isImageMissing = true;
        }
      } else this.isImageMissing = false;
    }
  }

  populateSelfDetails(): void {
    this.editForm.get(['name'])!.reset();
    this.editForm.get(['dateOfBirth'])!.reset();

    this.editForm.get(['name'])!.setValue(this.selectedEmployeeDetails.fullName);
    this.editForm.get(['dateOfBirth'])!.setValue(this.selectedEmployeeDetails.dateOfBirth);
    this.editForm.get(['insuranceRelation'])!.setValue(InsuranceRelation.SELF);

    this.editForm.get(['name'])!.disable();
    this.editForm.get(['dateOfBirth'])!.disable();
  }

  calculateAge(): void {
    let age = 0;
    const relationType = this.editForm.get(['insuranceRelation'])!.value;

    if (dayjs(this.editForm.get('dateOfBirth')!.value).isAfter(dayjs())) {
      this.inValidAge = true;
      this.editForm.get(['age'])!.setValue(0);
      return;
    } else {
      this.inValidAge = false;
    }
    const date = this.editForm.get(['dateOfBirth'])!.value;

    if (date !== null && date !== undefined) {
      const birthDate = this.editForm.get('dateOfBirth')!.value.toDate();
      const todayDate = new Date();

      let years = todayDate.getFullYear() - birthDate.getFullYear();

      if (
        todayDate.getMonth() < birthDate.getMonth() ||
        (todayDate.getMonth() === birthDate.getMonth() && todayDate.getDate() < birthDate.getDate())
      ) {
        years--;
      }
      this.editForm.get(['age'])!.setValue(years);
      age = years;
    } else {
      this.editForm.get(['age'])!.setValue(0);
      age = 0;
    }

    if (relationType !== InsuranceRelation.SELF && relationType !== InsuranceRelation.SPOUSE) {
      if (age > this.insuranceConfiguration.maxAllowedChildAge!) {
        this.isChildAgeExceedMaxLimit = true;
      } else {
        this.isChildAgeExceedMaxLimit = false;
      }
    }
  }

  onChangeInsuranceCardId(): void {
    this.inValidInsuranceId = false;
    const cardId = this.editForm.get(['insuranceId'])!.value;
    if (cardId !== null && cardId !== undefined && cardId !== '') {
      this.insuranceRegistrationService.isCardIdUnique(cardId, { id: this.editForm.get(['id'])!.value }).subscribe(res => {
        if (res.body! === true) {
          this.inValidInsuranceId = false;
        } else {
          this.inValidInsuranceId = true;
        }
      });
    }
  }

  onChangeSelectImage(event: any): void {
    /* validation: [file type: {image/jpeg}, file size: 2MB,] */
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      this.isImageMissing = false;
      const file = event.target.files[0];

      if (!file || file.type !== 'image/jpeg') {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'select JPEG image only';
        this.isImageMissing = true;
        return;
      }
      const sizeInKB = Number(file.size / 1024);
      if (sizeInKB > 2048) {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Max allowed file size is 2MB';
        this.isImageMissing = true;
        return;
      }
      this.image = file;
      this.isImageMissing = false;
    }
    return;
  }
}
