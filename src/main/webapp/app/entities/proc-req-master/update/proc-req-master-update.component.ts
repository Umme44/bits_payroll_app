import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProcReqMasterFormService, ProcReqMasterFormGroup } from './proc-req-master-form.service';
import { IProcReqMaster } from '../proc-req-master.model';
import { ProcReqMasterService } from '../service/proc-req-master.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { RequisitionStatus } from 'app/entities/enumerations/requisition-status.model';
import { swalOnSavedSuccess, swalOnUpdatedSuccess } from '../../../shared/swal-common/swal-common';

type SelectableEntity = IDepartment | IEmployee | IUser;

@Component({
  selector: 'jhi-proc-req-master-update',
  templateUrl: './proc-req-master-update.component.html',
})
export class ProcReqMasterUpdateComponent implements OnInit {
  isSaving = false;
  procReqMaster: IProcReqMaster | null = null;
  requisitionStatusValues = Object.keys(RequisitionStatus);

  departmentsSharedCollection: IDepartment[] = [];
  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ProcReqMasterFormGroup = this.procReqMasterFormService.createProcReqMasterFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected procReqMasterService: ProcReqMasterService,
    protected procReqMasterFormService: ProcReqMasterFormService,
    protected departmentService: DepartmentService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  /*  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);*/

  departments: IDepartment[] = [];
  employees: IEmployee[] = [];
  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procReqMaster }) => {
      this.procReqMaster = procReqMaster;
      if (procReqMaster) {
        this.updateForm(procReqMaster);
      }

      this.loadRelationshipsOptions();
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
    const procReqMaster = this.procReqMasterFormService.getProcReqMaster(this.editForm);
    if (procReqMaster.id !== null) {
      this.subscribeToSaveResponse(this.procReqMasterService.update(procReqMaster));
    } else {
      this.subscribeToSaveResponse(this.procReqMasterService.create(procReqMaster as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcReqMaster>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  /*  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }*/

  protected onSaveSuccess(): void {
    if (this.editForm.get('id')!.value) swalOnUpdatedSuccess();
    else swalOnSavedSuccess();
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(procReqMaster: IProcReqMaster): void {
    this.procReqMaster = procReqMaster;
    this.procReqMasterFormService.resetForm(this.editForm, procReqMaster);

    /*   this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      procReqMaster.department
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      procReqMaster.requestedBy,
      procReqMaster.recommendedBy01,
      procReqMaster.recommendedBy02,
      procReqMaster.recommendedBy03,
      procReqMaster.recommendedBy04,
      procReqMaster.recommendedBy05,
      procReqMaster.nextApprovalFrom,
      procReqMaster.rejectedBy,
      procReqMaster.closedBy
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      procReqMaster.updatedBy,
      procReqMaster.createdBy
    );*/
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));

    this.employeeService.getAllMinimalOfNgSelect().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

    /*    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.procReqMaster?.department)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
            employees,
            this.procReqMaster?.requestedBy,
            this.procReqMaster?.recommendedBy01,
            this.procReqMaster?.recommendedBy02,
            this.procReqMaster?.recommendedBy03,
            this.procReqMaster?.recommendedBy04,
            this.procReqMaster?.recommendedBy05,
            this.procReqMaster?.nextApprovalFrom,
            this.procReqMaster?.rejectedBy,
            this.procReqMaster?.closedBy
          )
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.procReqMaster?.updatedBy, this.procReqMaster?.createdBy)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));*/
  }
}
