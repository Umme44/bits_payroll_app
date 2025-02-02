import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { RecruitmentRequisitionFormService } from '../recruitment-requisition-form.service';

import {
  swalForErrorWithMessage,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess
} from '../../../shared/swal-common/swal-common';
import { RequisitionStatus } from '../../../shared/model/enumerations/requisition-status.model';
import { IDesignation } from '../../../shared/legacy/legacy-model/designation.model';
import { IBand } from '../../../shared/legacy/legacy-model/band.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { IUnit } from '../../../shared/legacy/legacy-model/unit.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { DesignationService } from '../../../shared/legacy/legacy-service/designation.service';
import { BandService } from '../../../shared/legacy/legacy-service/band.service';
import { DepartmentService } from '../../../shared/legacy/legacy-service/department.service';
import { UnitService } from '../../../shared/legacy/legacy-service/unit.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import {
  IRecruitmentRequisitionForm,
  RecruitmentRequisitionForm,
} from '../../../shared/legacy/legacy-model/recruitment-requisition-form.model';
import { RecruitmentNature } from 'app/entities/enumerations/recruitment-nature.model';
import {
  IRecruitmentRequisitionBudget
} from '../../../entities/recruitment-requisition-budget/recruitment-requisition-budget.model';
import {
  RecruitmentRequisitionBudgetService
} from '../../../entities/recruitment-requisition-budget/service/recruitment-requisition-budget.service';
import { ConfigService } from '../../../entities/config/service/config.service';
import { DefinedKeys } from '../../../config/defined-keys.constant';
import {CustomValidator} from "../../../validators/custom-validator";
type SelectableEntity = IDesignation | IBand | IDepartment | IUnit | IEmployee;

@Component({
  selector: 'jhi-recruitment-requisition-form-update',
  templateUrl: './recruitment-requisition-form-raise-on-behalf-update.component.html',
})
export class RecruitmentRequisitionFormRaiseOnBehalfUpdateComponent implements OnInit {
  isSaving = false;
  designations: IDesignation[] = [];
  bands: IBand[] = [];
  departments: IDepartment[] = [];
  units: IUnit[] = [];
  employees: IEmployee[] = [];
  expectedJoiningDateDp: any;
  dateOfRequisitionDp: any;
  requestedDateDp: any;
  recommendationDate01Dp: any;
  recommendationDate02Dp: any;
  recommendationDate03Dp: any;
  recommendationDate04Dp: any;
  recommendationDate05Dp: any;

  isReplacement = false;

  editForm = this.fb.group({
    id: [],
    expectedJoiningDate: [null, [Validators.required]],
    project: [null, [Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    numberOfVacancies: [1, [Validators.required, Validators.min(1), Validators.max(1000)]],
    employmentType: [null, [Validators.required]],
    resourceType: [null, [Validators.required]],
    rrfNumber: [],
    preferredEducationType: [null, [Validators.minLength(2), Validators.maxLength(250), Validators.required, CustomValidator.naturalTextValidator()]],
    preferredSkillType: [null, [Validators.minLength(2), Validators.maxLength(250), Validators.required, CustomValidator.naturalTextValidator()]],
    specialRequirement: [null, [Validators.minLength(2), Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    recruitmentNature: [[Validators.required]],
    dateOfRequisition: [],
    requestedDate: [],
    recommendationDate01: [],
    recommendationDate02: [],
    recommendationDate03: [],
    recommendationDate04: [],
    recommendationDate05: [],
    requisitionStatus: [null],
    rejectedAt: [],
    functionalDesignationId: [null, Validators.required],
    bandId: [null, Validators.required],
    departmentId: [null, Validators.required],
    unitId: [null, Validators.required],
    recommendedBy01Id: [],
    recommendedBy02Id: [],
    recommendedBy03Id: [],
    recommendedBy04Id: [],
    recommendedBy05Id: [],
    requesterId: [null, [Validators.required]],
    rejectedById: [],
    isDeleted: [false],
    deletedById: [],
    employeeToBeReplacedId: [null],
  });

  recruitmentRequisitionBudgets: IRecruitmentRequisitionBudget[] = [];

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected designationService: DesignationService,
    protected bandService: BandService,
    protected departmentService: DepartmentService,
    protected unitService: UnitService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService,
    protected configService: ConfigService
  ) {}

  ngOnInit(): void {
    this.configService.findByKeyCommon(DefinedKeys.is_rrf_enabled_for_user_end).subscribe(res => {
      if (res.body!.value === 'FALSE') {
        swalForErrorWithMessage('Recruitment Requisition Form is temporary disabled. Please try again later.');
        window.history.back();
      }
    });

    this.activatedRoute.data.subscribe(({ recruitmentRequisitionForm }) => {
      this.updateForm(recruitmentRequisitionForm);

      this.recruitmentRequisitionBudgetService.findByLoggedInUserId().subscribe((res: HttpResponse<IRecruitmentRequisitionBudget[]>) => {
        if (res.body !== null) {
          this.recruitmentRequisitionBudgets = res.body.filter(item => item.year === new Date().getFullYear());
        } else this.recruitmentRequisitionBudgets = [];
      });

      if (recruitmentRequisitionForm && recruitmentRequisitionForm.recruitmentNature === RecruitmentNature.REPLACEMENT) {
        this.isReplacement = true;
      }

      this.designationService.commonQuery().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));

      this.bandService.findAllForCommon().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));

      this.departmentService.commonQuery().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));

      this.unitService.commonQuery().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(recruitmentRequisitionForm: IRecruitmentRequisitionForm): void {
    this.editForm.patchValue({
      id: recruitmentRequisitionForm.id,
      expectedJoiningDate: recruitmentRequisitionForm.expectedJoiningDate as any,
      project: recruitmentRequisitionForm.project as any,
      numberOfVacancies: recruitmentRequisitionForm.numberOfVacancies as any,
      employmentType: recruitmentRequisitionForm.employmentType as any,
      resourceType: recruitmentRequisitionForm.resourceType as any,
      rrfNumber: recruitmentRequisitionForm.rrfNumber,
      preferredEducationType: recruitmentRequisitionForm.preferredEducationType as any,
      preferredSkillType: recruitmentRequisitionForm.preferredSkillType as any,
      specialRequirement: recruitmentRequisitionForm.specialRequirement as any,
      recruitmentNature: recruitmentRequisitionForm.recruitmentNature as any,
      dateOfRequisition: recruitmentRequisitionForm.dateOfRequisition,
      requestedDate: recruitmentRequisitionForm.requestedDate,
      recommendationDate01: recruitmentRequisitionForm.recommendationDate01,
      recommendationDate02: recruitmentRequisitionForm.recommendationDate02,
      recommendationDate03: recruitmentRequisitionForm.recommendationDate03,
      recommendationDate04: recruitmentRequisitionForm.recommendationDate04,
      recommendationDate05: recruitmentRequisitionForm.recommendationDate05,
      requisitionStatus: recruitmentRequisitionForm.requisitionStatus,
      rejectedAt: recruitmentRequisitionForm.rejectedAt,
      functionalDesignationId: recruitmentRequisitionForm.functionalDesignationId as any,
      bandId: recruitmentRequisitionForm.bandId as any,
      departmentId: recruitmentRequisitionForm.departmentId as any,
      unitId: recruitmentRequisitionForm.unitId as any,
      recommendedBy01Id: recruitmentRequisitionForm.recommendedBy01Id,
      recommendedBy02Id: recruitmentRequisitionForm.recommendedBy02Id,
      recommendedBy03Id: recruitmentRequisitionForm.recommendedBy03Id,
      recommendedBy04Id: recruitmentRequisitionForm.recommendedBy04Id,
      recommendedBy05Id: recruitmentRequisitionForm.recommendedBy05Id,
      requesterId: recruitmentRequisitionForm.requesterId as any,
      rejectedById: recruitmentRequisitionForm.rejectedById,
      isDeleted: recruitmentRequisitionForm.isDeleted,
      deletedById: recruitmentRequisitionForm.deletedById,
      employeeToBeReplacedId: recruitmentRequisitionForm.employeeToBeReplacedId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recruitmentRequisitionForm = this.createFromForm();
    if (recruitmentRequisitionForm.recruitmentNature === RecruitmentNature.REPLACEMENT) {
      recruitmentRequisitionForm.employeeToBeReplacedId = this.editForm.get('employeeToBeReplacedId')!.value;
      recruitmentRequisitionForm.numberOfVacancies = 1;
    } else {
      recruitmentRequisitionForm.employeeToBeReplacedId = undefined;
      recruitmentRequisitionForm.numberOfVacancies = this.editForm.get('numberOfVacancies')!.value;
    }
    if (recruitmentRequisitionForm.id !== undefined) {
      this.subscribeToSaveResponse(this.recruitmentRequisitionFormService.updateOnBehalfCommon(recruitmentRequisitionForm));
    } else {
      this.subscribeToSaveResponse(this.recruitmentRequisitionFormService.raiseOnBehalfCommon(recruitmentRequisitionForm));
    }
  }

  private createFromForm(): IRecruitmentRequisitionForm {
    return {
      ...new RecruitmentRequisitionForm(),
      id: this.editForm.get(['id'])!.value,
      expectedJoiningDate: this.editForm.get(['expectedJoiningDate'])!.value,
      project: this.editForm.get(['project'])!.value,
      numberOfVacancies: this.editForm.get(['numberOfVacancies'])!.value,
      employmentType: this.editForm.get(['employmentType'])!.value,
      resourceType: this.editForm.get(['resourceType'])!.value,
      rrfNumber: this.editForm.get(['rrfNumber'])!.value,
      preferredEducationType: this.editForm.get(['preferredEducationType'])!.value,
      preferredSkillType: this.editForm.get(['preferredSkillType'])!.value,
      specialRequirement: this.editForm.get(['specialRequirement'])!.value,
      recruitmentNature: this.editForm.get(['recruitmentNature'])!.value,
      dateOfRequisition: this.editForm.get(['dateOfRequisition'])!.value,
      requestedDate: this.editForm.get(['requestedDate'])!.value,
      recommendationDate01: this.editForm.get(['recommendationDate01'])!.value,
      recommendationDate02: this.editForm.get(['recommendationDate02'])!.value,
      recommendationDate03: this.editForm.get(['recommendationDate03'])!.value,
      recommendationDate04: this.editForm.get(['recommendationDate04'])!.value,
      recommendationDate05: this.editForm.get(['recommendationDate05'])!.value,
      requisitionStatus: this.editForm.get(['requisitionStatus'])!.value ?? RequisitionStatus.PENDING,
      rejectedAt: this.editForm.get(['rejectedAt'])!.value,
      functionalDesignationId: this.editForm.get(['functionalDesignationId'])!.value,
      bandId: this.editForm.get(['bandId'])!.value,
      departmentId: this.editForm.get(['departmentId'])!.value,
      unitId: this.editForm.get(['unitId'])!.value,
      recommendedBy01Id: this.editForm.get(['recommendedBy01Id'])!.value,
      recommendedBy02Id: this.editForm.get(['recommendedBy02Id'])!.value,
      recommendedBy03Id: this.editForm.get(['recommendedBy03Id'])!.value,
      recommendedBy04Id: this.editForm.get(['recommendedBy04Id'])!.value,
      recommendedBy05Id: this.editForm.get(['recommendedBy05Id'])!.value,
      requesterId: this.editForm.get(['requesterId'])!.value,
      rejectedById: this.editForm.get(['rejectedById'])!.value,
      isDeleted: this.editForm.get(['isDeleted'])!.value,
      deletedById: this.editForm.get(['deletedById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecruitmentRequisitionForm>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    swalOnSavedSuccess();
    this.previousState();
  }

  protected onSaveError(err: any): void {
    this.isSaving = false;
    swalOnRequestErrorWithBackEndErrorTitle(err.error.title);
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  changeEmployee(employee: any): void {
    if (employee) {
      this.editForm.get('requesterId')!.setValue(employee.id);
    }
  }

  changeEmployeeToBeReplaced(employee: any): void {
    if (employee) {
      this.editForm.get('employeeToBeReplacedId')!.setValue(employee.id);
    }
  }

  checkIsReplacement(): boolean {
    if (this.editForm.get('recruitmentNature')!.value === RecruitmentNature.REPLACEMENT) {
      this.isReplacement = true;
      this.editForm.get('employeeToBeReplacedId')!.setValidators([Validators.required]);
      this.editForm.get('numberOfVacancies')!.setValue(1);
    } else {
      this.isReplacement = false;
      this.editForm.get('employeeToBeReplacedId')!.clearValidators();
    }

    if (this.isReplacement) {
      return true;
    } else return false;
  }
}
