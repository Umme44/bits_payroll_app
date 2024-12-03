import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LeaveAllocationFormGroup, LeaveAllocationFormService } from './leave-allocation-form.service';
import { ILeaveAllocation } from '../leave-allocation.model';
import { LeaveAllocationService } from '../service/leave-allocation.service';
import { LeaveType } from 'app/entities/enumerations/leave-type.model';

@Component({
  selector: 'jhi-leave-allocation-update',
  templateUrl: './leave-allocation-update.component.html',
})
export class LeaveAllocationUpdateComponent implements OnInit {
  isSaving = false;
  leaveAllocation: ILeaveAllocation | null = null;
  leaveTypeValues = Object.keys(LeaveType);

  editForm: LeaveAllocationFormGroup = this.leaveAllocationFormService.createLeaveAllocationFormGroup();

  constructor(
    protected leaveAllocationService: LeaveAllocationService,
    protected leaveAllocationFormService: LeaveAllocationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveAllocation }) => {
      this.leaveAllocation = leaveAllocation;
      if (leaveAllocation) {
        this.updateForm(leaveAllocation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveAllocation = this.leaveAllocationFormService.getLeaveAllocation(this.editForm);
    if (leaveAllocation.id !== null) {
      this.subscribeToSaveResponse(this.leaveAllocationService.update(leaveAllocation));
    } else {
      this.subscribeToSaveResponse(this.leaveAllocationService.create(leaveAllocation as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveAllocation>>): void {
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

  protected updateForm(leaveAllocation: ILeaveAllocation): void {
    this.leaveAllocation = leaveAllocation;
    this.leaveAllocationFormService.resetForm(this.editForm, leaveAllocation);
  }
}
