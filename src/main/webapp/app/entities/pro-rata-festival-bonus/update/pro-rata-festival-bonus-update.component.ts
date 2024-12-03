import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { IProRataFestivalBonus, ProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import { ProRataFestivalBonusService } from '../service/pro-rata-festival-bonus.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-pro-rata-festival-bonus-update',
  templateUrl: './pro-rata-festival-bonus-update.component.html',
})
export class ProRataFestivalBonusUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  dateDp: any;
  selectedEmployeeId!: number;

  editForm = this.fb.group({
    id: [],
    date: [null as dayjs.Dayjs | null, [Validators.required]],
    amount: [null as number, [Validators.required]],
    description: [null as string, [Validators.required]],
    employeeId: this.fb.group({
      employeeId: [null as number | null, [Validators.required]],
    }),
  });

  constructor(
    protected proRataFestivalBonusService: ProRataFestivalBonusService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proRataFestivalBonus }) => {
      this.updateForm(proRataFestivalBonus);
      this.selectedEmployeeId = proRataFestivalBonus.employeeId;
      if (proRataFestivalBonus.id) {
        this.editForm.get('employeeId')!.disable();
      }
      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  updateForm(proRataFestivalBonus: IProRataFestivalBonus): void {
    this.editForm.patchValue({
      id: proRataFestivalBonus.id,
      date: proRataFestivalBonus.date ? dayjs(proRataFestivalBonus.date) : null,
      amount: proRataFestivalBonus.amount,
      description: proRataFestivalBonus.description,
      employeeId: {
        employeeId: proRataFestivalBonus.employeeId,
      },
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    const proRataFestivalBonus = this.createFromForm();
    if (proRataFestivalBonus.id !== undefined) {
      this.subscribeToSaveResponse(this.proRataFestivalBonusService.update(proRataFestivalBonus));
    } else {
      this.subscribeToSaveResponse(this.proRataFestivalBonusService.create(proRataFestivalBonus));
    }
  }

  private createFromForm(): IProRataFestivalBonus {
    return {
      ...new ProRataFestivalBonus(),
      id: this.editForm.get(['id'])!.value ?? undefined,
      date: this.editForm.get(['date'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      description: this.editForm.get(['description'])!.value,
      employeeId: this.editForm.get(['employeeId', 'employeeId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProRataFestivalBonus>>): void {
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

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }
}
