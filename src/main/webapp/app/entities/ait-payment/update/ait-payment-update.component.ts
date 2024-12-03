import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AitPaymentFormGroup, AitPaymentFormService } from './ait-payment-form.service';
import { IAitPayment, NewAitPayment } from '../ait-payment.model';
import { AitPaymentService } from '../service/ait-payment.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-ait-payment-update',
  templateUrl: './ait-payment-update.component.html',
})
export class AitPaymentUpdateComponent implements OnInit {
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
    protected aitPaymentService: AitPaymentService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aitPayment }) => {
      this.updateForm(aitPayment);
      this.selectedEmployeeId = aitPayment.employeeId;
      if (aitPayment.id) {
        this.editForm.get('employeeId')!.disable();
      }
      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(aitPayment: IAitPayment): void {
    this.editForm.patchValue({
      id: aitPayment.id,
      date: aitPayment.date ? dayjs(aitPayment.date) : null,
      amount: aitPayment.amount,
      description: aitPayment.description,
      employeeId: {
        employeeId: aitPayment.employeeId,
      },
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aitPayment = this.createFromForm();
    if (aitPayment.id != null) {
      this.subscribeToSaveResponse(this.aitPaymentService.update(aitPayment));
    } else {
      this.subscribeToSaveResponse(this.aitPaymentService.create(aitPayment));
    }
  }

  private createFromForm(): IAitPayment | NewAitPayment {
    return {
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      description: this.editForm.get(['description'])!.value,
      employeeId: this.editForm.get(['employeeId', 'employeeId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAitPayment>>): void {
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
