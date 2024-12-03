import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import {
  RecruitmentRequisitionBudgetFormService,
  RecruitmentRequisitionBudgetFormGroup,
} from './recruitment-requisition-budget-form.service';
import {
  IRecruitmentRequisitionBudget,
  NewRecruitmentRequisitionBudget
} from '../recruitment-requisition-budget.model';
import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

type SelectableEntity = IEmployee | IDepartment;
@Component({
  selector: 'jhi-recruitment-requisition-budget-update',
  templateUrl: './recruitment-requisition-budget-update.component.html',
})
export class RecruitmentRequisitionBudgetUpdateComponent implements OnInit {
  isSaving = false;
  recruitmentRequisitionBudget: IRecruitmentRequisitionBudget | null = null;

  employees: IEmployee[] = [];
  departments: IDepartment[] = [];

  editForm: RecruitmentRequisitionBudgetFormGroup =
    this.recruitmentRequisitionBudgetFormService.createRecruitmentRequisitionBudgetFormGroup();

  constructor(
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService,
    protected recruitmentRequisitionBudgetFormService: RecruitmentRequisitionBudgetFormService,
    protected employeeService: EmployeeService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruitmentRequisitionBudget }) => {
      this.recruitmentRequisitionBudget = recruitmentRequisitionBudget;
      if (recruitmentRequisitionBudget) {
        this.updateForm(recruitmentRequisitionBudget);
      }
      this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
      this.employeeService.getAllMinimalOfNgSelect().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recruitmentRequisitionBudget = this.recruitmentRequisitionBudgetFormService.getRecruitmentRequisitionBudget(this.editForm);
    if (recruitmentRequisitionBudget.id !== null) {
      this.subscribeToSaveResponse(this.recruitmentRequisitionBudgetService.update(recruitmentRequisitionBudget));
    } else {
      this.subscribeToSaveResponse(this.recruitmentRequisitionBudgetService.create(recruitmentRequisitionBudget as NewRecruitmentRequisitionBudget));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecruitmentRequisitionBudget>>): void {
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

  protected updateForm(recruitmentRequisitionBudget: IRecruitmentRequisitionBudget): void {
    this.editForm.patchValue({
      id: recruitmentRequisitionBudget.id,
      year: recruitmentRequisitionBudget.year,
      budget: recruitmentRequisitionBudget.budget,
      remainingBudget: recruitmentRequisitionBudget.remainingBudget,
      remainingManpower: recruitmentRequisitionBudget.remainingManpower,
      employeeId: recruitmentRequisitionBudget.employeeId,
      departmentId: recruitmentRequisitionBudget.departmentId,
    });
  }
  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

}
