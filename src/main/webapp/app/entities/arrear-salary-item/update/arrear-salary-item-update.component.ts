import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ArrearSalaryItemFormGroup, ArrearSalaryItemFormService } from './arrear-salary-item-form.service';
import { IArrearSalaryItem } from '../arrear-salary-item.model';
import { ArrearSalaryItemService } from '../service/arrear-salary-item.service';
import { IArrearSalaryMaster } from 'app/entities/arrear-salary-master/arrear-salary-master.model';
import { ArrearSalaryMasterService } from 'app/entities/arrear-salary-master/service/arrear-salary-master.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-arrear-salary-item-update',
  templateUrl: './arrear-salary-item-update.component.html',
})
export class ArrearSalaryItemUpdateComponent implements OnInit {
  isSaving = false;
  arrearSalaryItem: IArrearSalaryItem | null = null;

  arrearSalaryMastersSharedCollection: IArrearSalaryMaster[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: ArrearSalaryItemFormGroup = this.arrearSalaryItemFormService.createArrearSalaryItemFormGroup();

  constructor(
    protected arrearSalaryItemService: ArrearSalaryItemService,
    protected arrearSalaryItemFormService: ArrearSalaryItemFormService,
    protected arrearSalaryMasterService: ArrearSalaryMasterService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareArrearSalaryMaster = (o1: IArrearSalaryMaster | null, o2: IArrearSalaryMaster | null): boolean =>
    this.arrearSalaryMasterService.compareArrearSalaryMaster(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearSalaryItem }) => {
      this.arrearSalaryItem = arrearSalaryItem;
      if (arrearSalaryItem) {
        this.updateForm(arrearSalaryItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arrearSalaryItem = this.arrearSalaryItemFormService.getArrearSalaryItem(this.editForm);
    if (arrearSalaryItem.id !== null) {
      this.subscribeToSaveResponse(this.arrearSalaryItemService.update(arrearSalaryItem));
    } else {
      this.subscribeToSaveResponse(this.arrearSalaryItemService.create(arrearSalaryItem as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArrearSalaryItem>>): void {
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

  protected updateForm(arrearSalaryItem: IArrearSalaryItem): void {
    this.arrearSalaryItem = arrearSalaryItem;
    this.arrearSalaryItemFormService.resetForm(this.editForm, arrearSalaryItem);

    this.arrearSalaryMastersSharedCollection =
      this.arrearSalaryMasterService.addArrearSalaryMasterToCollectionIfMissing<IArrearSalaryMaster>(
        this.arrearSalaryMastersSharedCollection,
        arrearSalaryItem.arrearSalaryMaster
      );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      arrearSalaryItem.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.arrearSalaryMasterService
      .query()
      .pipe(map((res: HttpResponse<IArrearSalaryMaster[]>) => res.body ?? []))
      .pipe(
        map((arrearSalaryMasters: IArrearSalaryMaster[]) =>
          this.arrearSalaryMasterService.addArrearSalaryMasterToCollectionIfMissing<IArrearSalaryMaster>(
            arrearSalaryMasters,
            this.arrearSalaryItem?.arrearSalaryMaster
          )
        )
      )
      .subscribe((arrearSalaryMasters: IArrearSalaryMaster[]) => (this.arrearSalaryMastersSharedCollection = arrearSalaryMasters));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.arrearSalaryItem?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
