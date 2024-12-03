import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { FormBuilder, Validators } from '@angular/forms';

import { RecruitmentRequisitionFormFormService, RecruitmentRequisitionFormFormGroup } from './recruitment-requisition-form-form.service';
import { IRecruitmentRequisitionForm, NewRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from '../service/recruitment-requisition-form.service';
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
import { RecruitmentNature } from 'app/entities/enumerations/recruitment-nature.model';
import {
  IRecruitmentRequisitionBudget
} from '../../recruitment-requisition-budget/recruitment-requisition-budget.model';
import {
  RecruitmentRequisitionBudgetService
} from '../../recruitment-requisition-budget/service/recruitment-requisition-budget.service';
import { swalOnRequestErrorWithBackEndErrorTitle, swalOnSavedSuccess } from '../../../shared/swal-common/swal-common';
import {RequisitionStatus} from "../../enumerations/requisition-status.model";
type SelectableEntity = IDesignation | IBand | IDepartment | IUnit | IEmployee;

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
  expectedJoiningDateDp: any;
  dateOfRequisitionDp: any;
  requestedDateDp: any;
  recommendationDate01Dp: any;
  recommendationDate02Dp: any;
  recommendationDate03Dp: any;
  recommendationDate04Dp: any;
  recommendationDate05Dp: any;

  isReplacement = false;

  recruitmentRequisitionBudgets: IRecruitmentRequisitionBudget[] = [];

  recruitmentRequisitionForm: IRecruitmentRequisitionForm | null = null;
  editForm: RecruitmentRequisitionFormFormGroup = this.recruitmentRequisitionFormFormService.createRecruitmentRequisitionFormFormGroup();

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected recruitmentRequisitionFormFormService: RecruitmentRequisitionFormFormService,
    protected designationService: DesignationService,
    protected bandService: BandService,
    protected departmentService: DepartmentService,
    protected unitService: UnitService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionForm }) => {
      this.updateForm(recruitmentRequisitionForm);
      if (recruitmentRequisitionForm && recruitmentRequisitionForm.recruitmentNature === RecruitmentNature.REPLACEMENT) {
        this.isReplacement = true;
      }

      this.recruitmentRequisitionBudgetService.findByLoggedInUserId().subscribe((res: HttpResponse<IRecruitmentRequisitionBudget[]>) => {
        if (res.body !== null) {
          this.recruitmentRequisitionBudgets = res.body.filter(item => item.year === new Date().getFullYear());
        } else this.recruitmentRequisitionBudgets = [];
      });

      this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));

      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));

      this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => {
        this.departments = res.body || [];
      });

      this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(recruitmentRequisitionForm: IRecruitmentRequisitionForm): void {
    this.recruitmentRequisitionForm = recruitmentRequisitionForm;
    this.recruitmentRequisitionFormFormService.resetForm(this.editForm, recruitmentRequisitionForm);
    if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.CEO_APPROVED) {
      this.editForm.disable();
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recruitmentRequisitionForm = this.recruitmentRequisitionFormFormService.getRecruitmentRequisitionForm(this.editForm);
    if (recruitmentRequisitionForm.recruitmentNature=== RecruitmentNature.REPLACEMENT) {
      recruitmentRequisitionForm.employeeToBeReplacedId = this.editForm.get('employeeToBeReplacedId')!.value;
      recruitmentRequisitionForm.numberOfVacancies = 1;
    } else {
      recruitmentRequisitionForm.employeeToBeReplacedId = undefined;
      recruitmentRequisitionForm.numberOfVacancies = this.editForm.get('numberOfVacancies')!.value;
    }
    if (recruitmentRequisitionForm.id !== undefined) {
      this.subscribeToSaveResponse(this.recruitmentRequisitionFormService.update(recruitmentRequisitionForm));
    } else {
      this.subscribeToSaveResponse(this.recruitmentRequisitionFormService.create(recruitmentRequisitionForm as NewRecruitmentRequisitionForm));
    }
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
      this.editForm.patchValue({requesterId: employee})
    }
  }

  changeEmployeeToBeReplaced(employee: any): void {
    if (employee) {
      this.editForm.patchValue({employeeToBeReplacedId: employee.id})
      // this.editForm.get('employeeToBeReplacedId')!.setValue(employee.id);
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
