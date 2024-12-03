import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from '../../../shared/constants/input.constants';

import { IRecruitmentRequisitionForm, RecruitmentRequisitionForm } from '../../../shared/model/recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from '../recruitment-requisition-form.service';
import { IDesignation } from '../../../shared/model/designation.model';
import { DesignationService } from '../../designation/designation.service';
import { IBand } from '../../../shared/model/band.model';
import { BandService } from '../../band/band.service';
import { IDepartment } from '../../../shared/model/department.model';
import { DepartmentService } from '../../department/department.service';
import { IUnit } from '../../../shared/model/unit.model';
import { UnitService } from '../../unit/unit.service';
import { IEmployee } from '../../../shared/model/employee.model';
import { EmployeeService } from '../../employee/employee.service';
import { IUser } from '../../../core/user/user.model';
import { UserService } from '../../../core/user/user.service';

type SelectableEntity = IDesignation | IBand | IDepartment | IUnit | IEmployee | IUser;

@Component({
  selector: 'jhi-recruitment-requisition-form-update',
  templateUrl: './recruitment-requisition-form-update.component.html',
})
export class RecruitmentRequisitionFormUpdateComponent implements OnInit {
  isSaving = false;
  designations: IDesignation[] = [];
  bands: IBand[] = [];
  departments: IDepartment[] = [];
  units: IUnit[] = [];
  employees: IEmployee[] = [];
  users: IUser[] = [];
  expectedJoiningDateDp: any;
  dateOfRequisitionDp: any;
  requestedDateDp: any;
  recommendationDate01Dp: any;
  recommendationDate02Dp: any;
  recommendationDate03Dp: any;
  recommendationDate04Dp: any;
  rejectedAtDp: any;

  editForm = this.fb.group({
    id: [],
    expectedJoiningDate: [null, [Validators.required]],
    project: [null, [Validators.minLength(0), Validators.maxLength(250)]],
    numberOfVacancies: [null, [Validators.required, Validators.min(1), Validators.max(1000)]],
    employmentType: [null, [Validators.required]],
    resourceType: [null, [Validators.required]],
    rrfNumber: [],
    preferredEducationType: [null, [Validators.minLength(2), Validators.maxLength(250)]],
    dateOfRequisition: [],
    requestedDate: [],
    recommendationDate01: [],
    recommendationDate02: [],
    recommendationDate03: [],
    recommendationDate04: [],
    requisitionStatus: [null, [Validators.required]],
    rejectedAt: [],
    createdAt: [],
    updatedAt: [],
    functionalDesignationId: [null, Validators.required],
    bandId: [null, Validators.required],
    departmentId: [],
    unitId: [],
    recommendedBy01Id: [],
    recommendedBy02Id: [],
    recommendedBy03Id: [],
    recommendedBy04Id: [],
    requesterId: [],
    rejectedById: [],
    createdById: [],
    updatedById: [],
  });

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected designationService: DesignationService,
    protected bandService: BandService,
    protected departmentService: DepartmentService,
    protected unitService: UnitService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionForm }) => {
      if (!recruitmentRequisitionForm.id) {
        const today = moment().startOf('day');
        recruitmentRequisitionForm.createdAt = today;
        recruitmentRequisitionForm.updatedAt = today;
      }

      this.updateForm(recruitmentRequisitionForm);

      this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));

      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));

      this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));

      this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(recruitmentRequisitionForm: IRecruitmentRequisitionForm): void {
    this.editForm.patchValue({
      id: recruitmentRequisitionForm.id,
      expectedJoiningDate: recruitmentRequisitionForm.expectedJoiningDate,
      project: recruitmentRequisitionForm.project,
      numberOfVacancies: recruitmentRequisitionForm.numberOfVacancies,
      employmentType: recruitmentRequisitionForm.employmentType,
      resourceType: recruitmentRequisitionForm.resourceType,
      rrfNumber: recruitmentRequisitionForm.rrfNumber,
      preferredEducationType: recruitmentRequisitionForm.preferredEducationType,
      dateOfRequisition: recruitmentRequisitionForm.dateOfRequisition,
      requestedDate: recruitmentRequisitionForm.requestedDate,
      recommendationDate01: recruitmentRequisitionForm.recommendationDate01,
      recommendationDate02: recruitmentRequisitionForm.recommendationDate02,
      recommendationDate03: recruitmentRequisitionForm.recommendationDate03,
      recommendationDate04: recruitmentRequisitionForm.recommendationDate04,
      requisitionStatus: recruitmentRequisitionForm.requisitionStatus,
      rejectedAt: recruitmentRequisitionForm.rejectedAt,
      createdAt: recruitmentRequisitionForm.createdAt ? recruitmentRequisitionForm.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: recruitmentRequisitionForm.updatedAt ? recruitmentRequisitionForm.updatedAt.format(DATE_TIME_FORMAT) : null,
      functionalDesignationId: recruitmentRequisitionForm.functionalDesignationId,
      bandId: recruitmentRequisitionForm.bandId,
      departmentId: recruitmentRequisitionForm.departmentId,
      unitId: recruitmentRequisitionForm.unitId,
      recommendedBy01Id: recruitmentRequisitionForm.recommendedBy01Id,
      recommendedBy02Id: recruitmentRequisitionForm.recommendedBy02Id,
      recommendedBy03Id: recruitmentRequisitionForm.recommendedBy03Id,
      recommendedBy04Id: recruitmentRequisitionForm.recommendedBy04Id,
      requesterId: recruitmentRequisitionForm.requesterId,
      rejectedById: recruitmentRequisitionForm.rejectedById,
      createdById: recruitmentRequisitionForm.createdById,
      updatedById: recruitmentRequisitionForm.updatedById,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recruitmentRequisitionForm = this.createFromForm();
    if (recruitmentRequisitionForm.id !== undefined) {
      this.subscribeToSaveResponse(this.recruitmentRequisitionFormService.update(recruitmentRequisitionForm));
    } else {
      this.subscribeToSaveResponse(this.recruitmentRequisitionFormService.create(recruitmentRequisitionForm));
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
      dateOfRequisition: this.editForm.get(['dateOfRequisition'])!.value,
      requestedDate: this.editForm.get(['requestedDate'])!.value,
      recommendationDate01: this.editForm.get(['recommendationDate01'])!.value,
      recommendationDate02: this.editForm.get(['recommendationDate02'])!.value,
      recommendationDate03: this.editForm.get(['recommendationDate03'])!.value,
      recommendationDate04: this.editForm.get(['recommendationDate04'])!.value,
      requisitionStatus: this.editForm.get(['requisitionStatus'])!.value,
      rejectedAt: this.editForm.get(['rejectedAt'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      functionalDesignationId: this.editForm.get(['functionalDesignationId'])!.value,
      bandId: this.editForm.get(['bandId'])!.value,
      departmentId: this.editForm.get(['departmentId'])!.value,
      unitId: this.editForm.get(['unitId'])!.value,
      recommendedBy01Id: this.editForm.get(['recommendedBy01Id'])!.value,
      recommendedBy02Id: this.editForm.get(['recommendedBy02Id'])!.value,
      recommendedBy03Id: this.editForm.get(['recommendedBy03Id'])!.value,
      recommendedBy04Id: this.editForm.get(['recommendedBy04Id'])!.value,
      requesterId: this.editForm.get(['requesterId'])!.value,
      rejectedById: this.editForm.get(['rejectedById'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      updatedById: this.editForm.get(['updatedById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecruitmentRequisitionForm>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
