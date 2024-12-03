import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';
// import { JhiDataUtils, JhiEventManager, JhiEventWithContent, JhiFileLoadError } from 'ng-jhipster';
import { swalOnSentForApproval, swalSelectValidImage } from 'app/shared/swal-common/swal-common';
import { RelationType } from 'app/shared/model/enumerations/relation-type.model';
import { InsuranceStatus } from '../../shared/model/enumerations/insurance-status.model';
import { InsuranceRelation } from '../../shared/model/enumerations/insurance-relation.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { IUser } from '../../entities/user/user.model';
import { IInsuranceRegistration, InsuranceRegistration } from '../../shared/legacy/legacy-model/insurance-registration.model';
import { InsuranceConfiguration } from '../../shared/legacy/legacy-model/insurance-configuration.model';
import { InsuranceRegistrationService } from '../../entities/insurance-registration/service/insurance-registration.service';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';
import { UserService } from '../../entities/user/user.service';
import { UserInsuranceService } from './user-insurance.service';
import { IInsuranceRelations } from '../../shared/legacy/legacy-model/insurance-relations.model';
import { DATE_TIME_FORMAT } from '../../config/input.constants';

type SelectableEntity = IEmployee | IUser;

@Component({
  selector: 'jhi-insurance-registration-update',
  templateUrl: './user-insurance-registration-update.component.html',
  styleUrls: ['./user-insurance-registration-update.component.scss'],
})
export class UserInsuranceRegistrationUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  users: IUser[] = [];
  dateOfBirthDp: any;
  selectedRelation!: string;
  selectedRelationType!: RelationType;
  isChildAgeExceedMaxLimit = false;
  previousRegistrations: IInsuranceRegistration[] = [];
  relations: string[] = [];

  selfDetails!: IEmployee;
  maxDateForDateOfBirthDP: NgbDateStruct;

  editForm = this.fb.group({
    id: [],
    employeeName: [],
    name: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9]+(?:[ ]?[a-zA-Z0-9]+)*|[ ]*$')]],
    dateOfBirth: [],
    age: [Validators.required, Validators.pattern('^[\\d\\-/]{10}$')],
    insuranceRelation: [],
    photo: [],
    insuranceStatus: [],
    insuranceId: [],
    employeeId: [],
    unapprovalReason: [],
    availableBalance: [],
    approvedAt: [],
    createdAt: [],
    updatedAt: [],
    approvedById: [],
    createdById: [],
    updatedById: [],
  });
  inValidFileErrorMsg!: string;
  inValidFile = false;
  image!: File;
  isImageMissing = true;
  isImageRequired = false;
  insuranceConfiguration!: InsuranceConfiguration;
  isInsuranceConfigurationMissing = false;
  inValidAge = false;
  personsAge = 0;

  constructor(
    protected insuranceRegistrationService: InsuranceRegistrationService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected userInsuranceService: UserInsuranceService,
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    const current = new Date();
    this.maxDateForDateOfBirthDP = {
      year: current.getFullYear(),
      month: current.getMonth() + 1,
      day: current.getDate(),
    };
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceRegistration }) => {
      this.getInsuranceConfiguration();

      if (!insuranceRegistration.id) {
        this.editForm.get(['age'])!.setValue(0);
        this.disableInputFields();
        this.getRemainingInsuranceRelations();
      } else if (insuranceRegistration.id) {
        this.getAllInsuranceRelations();
        this.updateForm(insuranceRegistration);
        this.calculateAge();
        this.inValidFile = false;
      }

      this.getSelfDetails();
    });
  }

  getSelfDetails(): void {
    this.userInsuranceService.findSelfDetailsForInsuranceRegistration().subscribe(res => {
      this.selfDetails = res.body!;
    });
  }

  getAllInsuranceRelations(): void {
    this.userInsuranceService.queryAllInsuranceRelations().subscribe((res: HttpResponse<IInsuranceRelations>) => {
      this.relations = res.body!.relations || [];
    });
  }

  getRemainingInsuranceRelations(): void {
    this.userInsuranceService.queryRemainingInsuranceRelations().subscribe((res: HttpResponse<IInsuranceRelations>) => {
      this.relations = res.body!.relations || [];
    });
  }

  getInsuranceConfiguration(): void {
    this.userInsuranceService.queryInsuranceConfiguration().subscribe(res => {
      this.insuranceConfiguration = res.body!;
    });
  }

  updateForm(insuranceRegistration: IInsuranceRegistration): void {
    this.editForm.patchValue({
      id: insuranceRegistration.id,
      employeeName: insuranceRegistration.employeeName,
      name: insuranceRegistration.name,
      dateOfBirth: insuranceRegistration.dateOfBirth,
      photo: insuranceRegistration.photo,
      insuranceRelation: insuranceRegistration.insuranceRelation,
      insuranceStatus: insuranceRegistration.insuranceStatus,
      unapprovalReason: insuranceRegistration.unapprovalReason,
      availableBalance: insuranceRegistration.availableBalance,
      approvedAt: insuranceRegistration.approvedAt ? insuranceRegistration.approvedAt.format(DATE_TIME_FORMAT) : null,
      createdAt: insuranceRegistration.createdAt ? insuranceRegistration.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: insuranceRegistration.updatedAt ? insuranceRegistration.updatedAt.format(DATE_TIME_FORMAT) : null,
      insuranceId: insuranceRegistration.insuranceId,
      employeeId: insuranceRegistration.employeeId,
      approvedById: insuranceRegistration.approvedById,
      createdById: insuranceRegistration.createdById,
      updatedById: insuranceRegistration.updatedById,
    });
  }

  // byteSize(base64String: string): string {
  //   return this.dataUtils.byteSize(base64String);
  // }

  // openFile(contentType: string, base64String: string): void {
  //   this.dataUtils.openFile(contentType, base64String);
  // }
  //
  // setFileData(event: any, field: string, isImage: boolean): void {
  //   this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
  //     this.eventManager.broadcast(
  //       new JhiEventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })
  //     );
  //   });
  // }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insuranceRegistration = this.createFromForm();

    if (insuranceRegistration.id !== undefined && insuranceRegistration.id !== null) {
      if (this.image === null || this.image === undefined) {
        this.subscribeToSaveResponse(this.userInsuranceService.updateInsuranceRegistration(insuranceRegistration));
      } else {
        this.subscribeToSaveResponse(this.userInsuranceService.updateInsuranceRegistrationMultipart(this.image, insuranceRegistration));
      }
    } else {
      if (insuranceRegistration.insuranceRelation === 'SELF') {
        this.subscribeToSaveResponse(this.userInsuranceService.createInsuranceRegistration(insuranceRegistration));
      } else if (this.image === null || this.image === undefined) {
        this.subscribeToSaveResponse(this.userInsuranceService.createInsuranceRegistration(insuranceRegistration));
      } else {
        if (!this.inValidFile) {
          this.subscribeToSaveResponse(this.userInsuranceService.createInsuranceRegistrationMultipart(this.image, insuranceRegistration));
        } else {
          swalSelectValidImage();
          this.isSaving = false;
          return;
        }
      }
    }
  }

  disableInputFields(): void {
    this.editForm.get(['name'])!.disable();
    this.editForm.get(['dateOfBirth'])!.disable();
    this.editForm.get(['photo'])!.disable();
  }

  onChangeInsuranceRelation(): void {
    this.editForm.get(['name'])!.reset();
    this.editForm.get(['name'])!.enable();
    this.editForm.get(['age'])!.setValue(0);
    this.editForm.get(['dateOfBirth'])!.reset();
    this.editForm.get(['dateOfBirth'])!.enable();

    const insuranceRelation = this.editForm.get(['insuranceRelation'])!.value;
    if (insuranceRelation === InsuranceRelation.SELF) {
      this.populateSelfDetails(this.selfDetails);
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

  populateSelfDetails(selfDetails: IEmployee): void {
    this.editForm.get(['name'])!.setValue(selfDetails.fullName);
    this.editForm.get(['name'])!.disable();
    this.editForm.get(['dateOfBirth'])!.setValue(selfDetails.dateOfBirth);
    this.editForm.get(['dateOfBirth'])!.disable();
  }

  private createFromForm(): IInsuranceRegistration {
    return {
      ...new InsuranceRegistration(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      insuranceRelation: this.editForm.get(['insuranceRelation'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      insuranceStatus: this.editForm.get(['insuranceStatus'])!.value ?? InsuranceStatus.PENDING,
      unapprovalReason: this.editForm.get(['unapprovalReason'])!.value,
      availableBalance: this.editForm.get(['availableBalance'])!.value ?? this.insuranceConfiguration.maxTotalClaimLimitPerYear,
      approvedAt: this.editForm.get(['approvedAt'])!.value ? dayjs(this.editForm.get(['approvedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      insuranceId: this.editForm.get(['insuranceId'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      approvedById: this.editForm.get(['approvedById'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      updatedById: this.editForm.get(['updatedById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsuranceRegistration>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    swalOnSentForApproval();
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
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
      const birthDate = new Date(this.editForm.get('dateOfBirth')!.value);
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

  updateValidationOnSpouseOrChild(): void {
    if (this.selectedRelation === 'SPOUSE') {
      this.editForm.get(['spouseType'])!.setValidators([Validators.required]);
      this.editForm.get(['childType'])!.clearValidators();
    } else if (this.selectedRelation === 'CHILD') {
      this.editForm.get(['childType'])!.setValidators([Validators.required]);
      this.editForm.get(['spouseType'])!.clearValidators();
    }
    this.editForm.get(['childType'])!.updateValueAndValidity();
    this.editForm.get(['spouseType'])!.updateValueAndValidity();
  }

  // shouldDisableInsuranceRelationType(): boolean {
  //   if (this.insuranceRelation.relation === Relation.SELF) {
  //     this.editForm.get(['relationType'])!.setValue('SELF');
  //     return false;
  //   } else {
  //     return false;
  //   }
  // }

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
}
