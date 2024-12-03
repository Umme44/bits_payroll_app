import { Component, OnInit , ViewChild, HostListener} from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { finalize, map } from 'rxjs/operators';
/* import { FormGroup } from '@angular/forms'; */
import { AttendanceEntryFormGroup, AttendanceEntryFormService } from './attendance-entry-form.service';
import { IAttendanceEntry, NewAttendanceEntry } from '../attendance-entry.model';
import { AttendanceEntryService } from '../service/attendance-entry.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Status } from 'app/entities/enumerations/status.model';
import {SelectEmployeeFormComponent} from "../../../shared/select-employee-form/select-employee-form.component";

@Component({
  selector: 'jhi-attendance-entry-update',
  templateUrl: './attendance-entry-update.component.html',
})
export class AttendanceEntryUpdateComponent implements OnInit {
  // @ViewChild(SelectEmployeeFormComponent) child!: SelectEmployeeFormComponent;

  isSaving = false;
  dateDp: any;
  isInvalid = false;

  searchDate: any;
  selectEmployeeId!: number;
  attendanceEntry!: IAttendanceEntry;


  statusValues = Object.keys(Status);

  employeesSharedCollection: IEmployee[] = [];

  editForm: AttendanceEntryFormGroup = this.attendanceEntryFormService.createAttendanceEntryFormGroup();

  constructor(
    protected attendanceEntryService: AttendanceEntryService,
    protected attendanceEntryFormService: AttendanceEntryFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendanceEntry }) => {
      this.attendanceEntry = attendanceEntry;
      if (attendanceEntry) {
        this.updateForm(attendanceEntry);
      }

      this.loadRelationshipsOptions();
    });
  }

  // ngAfterViewInit(): void {
  //   if (this.activatedRoute.snapshot.url[0].path !== 'new') {
  //     this.activatedRoute.data.subscribe(({ attendanceEntry }) => {
  //       this.updateForm(attendanceEntry);
  //     });
  //
  //     this.editForm.controls['employeeId'].disable();
  //     this.editForm.controls['date'].disable();
  //
  //     this.editForm.markAllAsTouched();
  //   }
  // }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendanceEntry = this.attendanceEntryFormService.getAttendanceEntry(this.editForm);
    if (attendanceEntry.id) {
      this.subscribeToUpdateResponse(this.attendanceEntryService.update(attendanceEntry));
    } else {
      this.subscribeToSaveResponse(this.attendanceEntryService.create(attendanceEntry as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendanceEntry>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToUpdateResponse(result: Observable<HttpResponse<IAttendanceEntry>>): void {
    result.subscribe(
      () => this.onUpdateSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    Swal.fire({
      icon: 'success',
      html: 'Saved!',
      showConfirmButton: false,
      timer: 1500,
    });
    this.previousState();
  }

  protected onUpdateSuccess(): void {
    this.isSaving = false;
    Swal.fire({
      icon: 'success',
      html: 'Updated!',
      showConfirmButton: false,
      timer: 1500,
    });
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
   this.showErrorForInvalidInput()
  }

     showErrorForInvalidInput(){
      Swal.fire({
      icon: 'error',
      text: 'Invalid input',
      timer: 3500,
      showConfirmButton: false,
    });
  }

  protected updateForm(attendanceEntry: IAttendanceEntry): void {
    this.attendanceEntry = attendanceEntry;
    this.attendanceEntryFormService.resetForm(this.editForm, attendanceEntry);

    // this.employeeIdForm.patchValue({
    //   employeeId: attendanceEntry.employeeId,
    // });

    // this.child.setEmployee(attendanceEntry.employeeId!);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   attendanceEntry.employee
    // );
  }

  protected loadRelationshipsOptions(): void {

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.attendanceEntry?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  checkTime(): void {
    const inTime = this.editForm.get(['inTime'])!.value;
    const outTime = this.editForm.get(['outTime'])!.value;

    if (inTime && outTime && inTime > outTime) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  get employeeIdForm(): any {
    return this.editForm.get('employeeId');
  }

  patchSelectedEmployeeId($event: any) {
    this.selectEmployeeId = this.editForm.get('employeeId')!.value;
    this.editForm.patchValue({ employeeId: $event.id });
  }

  // @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
  //   if (!this.editForm.get('id')!.value) {
  //     this.editForm.reset();
  //   }
  // }

  checkForAttendance(): void {
    const id = this.selectEmployeeId;
    const date = this.editForm.get(['date'])!.value;

    if (date !== this.searchDate) {
      if (id !== null && id !== undefined && date !== null && date !== undefined) {
        this.attendanceEntryService.findExistingEntry(id, date).subscribe((res: HttpResponse<IAttendanceEntry>) => {
          this.attendanceEntry = res.body!;
          if (this.attendanceEntry.id !== null && this.attendanceEntry.id !== undefined) {
            this.updateForm(this.attendanceEntry);
            this.searchDate = date;
          }
        });
      }
    }
  }
}
